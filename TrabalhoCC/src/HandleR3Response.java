import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class HandleR3Response implements Runnable{
    String serverAddress;
    int serverPort;
    DatagramPacket packet;
    DatagramSocket socket;
    private byte[] buf = new byte[256];
    final Map<Map.Entry<String,Integer>,Socket> pedidos;
    final Map<Map.Entry<String,Integer>,String []> builders;
    final Map<Map.Entry<String,Integer>,String []> totais;
    final Map<Integer,Map.Entry<InetAddress,String>> porResponder;
    IntHolder udpID;
    public HandleR3Response(String serverAddress, int serverPort, DatagramSocket socket,DatagramPacket packet,Map<Map.Entry<String,Integer>,Socket> pedidos,Map<Map.Entry<String,Integer>,String []> builders,Map<Map.Entry<String,Integer>,String []> totais,Map<Integer,Map.Entry<InetAddress,String>> porResponder,IntHolder udpID)
    {
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
        this.packet=packet;
        this.pedidos=pedidos;
        this.socket=socket;
        this.builders=builders;
        this.totais=totais;
        this.porResponder=porResponder;
        this.udpID=udpID;
    }
    private void sendToPeer(String s,boolean toAck) throws Exception
    {
        InetAddress peerAddress;
        buf = Base64.getEncoder().encode(s.getBytes());
        peerAddress = packet.getAddress();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, peerAddress, 6666);
        socket.send(packet);
        if (toAck)
        {
            synchronized (porResponder) {
                Map.Entry<InetAddress, String> entry = new AbstractMap.SimpleEntry<>(peerAddress, s);
                porResponder.put(udpID.i, entry);
                udpID.i=udpID.i+1;
            }

        }

    }
    private int conta(String [] arg)
    {
        int i=0;
        for(String s:arg)
        {
            if (s!=null)
                i++;
        }
        return i;
    }
    @Override
    public void run() {
        PrintWriter out = new PrintWriter(System.out);
        String receivedEncoded = new String(packet.getData(), 0, packet.getLength());
        String received = new String(Base64.getDecoder().decode(receivedEncoded.getBytes()));
        R3Package r3Package = new R3Package(received);
        Socket s;
        String resposta="";
        Map.Entry<String,Integer> entry;
        try {
            switch (r3Package.tipo) {
                case "GET":
                    entry = new AbstractMap.SimpleEntry<>(r3Package.clientAddress, r3Package.id);
                    s = new Socket(serverAddress, serverPort);
                    String response;
                    BufferedReader inSocket = new BufferedReader((new InputStreamReader(s.getInputStream())));
                    PrintWriter outSocket = new PrintWriter(s.getOutputStream());
                    out.println("Entrou no ANONGW peer!");
                    out.flush();
                    out.println("String Recebida:" + r3Package.toString());
                    out.flush();
                    outSocket.println(r3Package.toString());
                    outSocket.flush();
                    response = inSocket.readLine();
                    int tamanho = 40;
                    int size = (int) Math.ceil(response.length()/(tamanho*1.0)); // Tamanho maximo da string de pacote são 40 bytes
                    String strings [] = new String[size];
                    for (int i=0;i<size;i++)
                        strings[i]=response.substring(i*tamanho,Math.min((i+1)*tamanho-1,response.length()-1));
                    synchronized (totais){
                    totais.put(entry,strings);} // Adicionada resposta do servidor ao buffer de respostas
                    out.println("Resposta a enviar ANONGW peer: " + response);
                    out.flush();
                    s.close();
                    response = "BEGIN "+"MSG"+" "+"0 "+size+" "+r3Package.clientAddress+" "+r3Package.id+" "+udpID.i;
                    out.println("Enviado BEGIN");
                    out.flush();
                    sendToPeer(response,true);
                    break;
                case "END": //DAR ACK DE UM END
                    entry = new AbstractMap.SimpleEntry<>(r3Package.clientAddress, r3Package.id);
                    if (r3Package.totalPacotes == conta(builders.get(entry))) {
                        //SEND ACK
                        response="ACK "+"END"+" 0 "+r3Package.totalPacotes+" "+r3Package.clientAddress +" "+r3Package.id+" "+r3Package.udpID;
                        sendToPeer(response,false);
                        out.println("ENVIADO ACK END");
                        out.flush();
                        //RESPONDE A CLIENTE
                        synchronized (pedidos) {
                            s = pedidos.get(entry);
                        }
                        PrintWriter outSocket2 = new PrintWriter(s.getOutputStream());
                        out.println("Pronto para Ligar ao TCP do Client");
                        out.flush();
                        response = "";
                        synchronized (builders) {
                            for (int i = 0; i < builders.get(entry).length; i++)
                                response = response + builders.get(entry)[i];
                        }
                        outSocket2.println(response);
                        outSocket2.flush();
                        s.close(); //FECHA SOCKET
                        pedidos.remove(entry); //RETIRA DOS PEDIDOS
                    }
                    break;
                case "BEGIN": //DAR ACK DE UM BEGIN
                    String [] build = new String[r3Package.totalPacotes];
                    entry = new AbstractMap.SimpleEntry<>(r3Package.clientAddress, r3Package.id);
                    synchronized (builders){
                    builders.put(entry,build);}
                    response="ACK "+"BEGIN"+" 0 "+r3Package.totalPacotes+" "+r3Package.clientAddress +" "+r3Package.id+" "+r3Package.udpID;
                    sendToPeer(response,false);
                    out.println("ENVIADO ACK BEGIN");
                    out.flush();
                    break;
                case "DATA": // DAR ACK DE UM DATA
                    out.println("RECEBEU DATA "+r3Package.numSeq);
                    out.flush();
                    entry = new AbstractMap.SimpleEntry<>(r3Package.clientAddress, r3Package.id);
                    synchronized (builders){
                    strings = builders.get(entry);
                    strings[r3Package.numSeq] = r3Package.data;
                    builders.put(entry,strings);}
                    response="ACK "+"DATA"+" "+(r3Package.numSeq+1)+" "+r3Package.totalPacotes+" "+r3Package.clientAddress +" "+r3Package.id+" "+r3Package.udpID;
                    sendToPeer(response,false);
                    out.println("ENVIADO ACK DATA"+(r3Package.numSeq+1));
                    out.flush();
                    break;
                case "ACK":
                    out.println("RECEBEU ACK DE UDPID"+r3Package.udpID);
                    R3Package p = new R3Package(porResponder.get(r3Package.udpID).getValue());
                    synchronized (porResponder){
                        for (int chave : porResponder.keySet()) {
                            if (chave!=r3Package.udpID)
                            {
                                R3Package p2 = new R3Package(porResponder.get(chave).getValue());
                                if(p2.tipo.equals(p.tipo) && p2.id==p.id && p2.numSeq == p.numSeq && p2.clientAddress.equals(p.clientAddress))
                                    porResponder.remove(chave);
                            }
                        }
                        porResponder.remove(r3Package.udpID);
                    }
                    switch (r3Package.data)
                    {
                        case "BEGIN":
                            entry = new AbstractMap.SimpleEntry<>(r3Package.clientAddress, r3Package.id);
                            synchronized (totais)
                            {
                                response = "DATA "+"teste"+" "+0+" "+r3Package.totalPacotes+" "+r3Package.clientAddress+" "+r3Package.id+" "+udpID.i;
                                sendToPeer(response,true);
                                out.println("Enviado 1º DATA");
                                out.flush();
                            }
                            break;
                        case "END":
                            out.println("PACOTE ENVIADO COM SUCESSO!");
                            out.flush();
                            break;
                        case "DATA":
                        {
                            if (r3Package.numSeq<r3Package.totalPacotes) {
                            response = "DATA "+r3Package.numSeq+" "+r3Package.numSeq+" "+r3Package.totalPacotes+" "+r3Package.clientAddress+" "+r3Package.id+" "+udpID.i;
                            sendToPeer(response,true);
                            out.println("Enviado DATA "+r3Package.numSeq);
                            out.flush();}
                            else {
                                response = "END "+"0"+" "+r3Package.numSeq+" "+r3Package.totalPacotes+" "+r3Package.clientAddress+" "+r3Package.id+" "+udpID.i;
                                sendToPeer(response,true);
                                out.println("Enviado END "+r3Package.numSeq);
                                out.flush();}
                            }
                        }


                    }


            }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
