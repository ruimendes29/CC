import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.Map;

public class HandleR3Response implements Runnable{
    String serverAddress;
    int serverPort;
    DatagramPacket packet;
    DatagramSocket socket;
    private byte[] buf = new byte[256];
    Map<Map.Entry<String,Integer>,Socket> pedidos;
    public HandleR3Response(String serverAddress, int serverPort, DatagramSocket socket,DatagramPacket packet,Map<Map.Entry<String,Integer>,Socket> pedidos)
    {
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
        this.packet=packet;
        this.pedidos=pedidos;
        this.socket=socket;
    }
    @Override
    public void run() {
        PrintWriter out = new PrintWriter(System.out);
        String receivedEncoded = new String(packet.getData(), 0, packet.getLength());
        String received = new String(Base64.getDecoder().decode(receivedEncoded));
        R3Package r3Package = new R3Package(received);
        Socket s;
        InetAddress peerAddress;
        try {
            switch (r3Package.tipo) {
                case "GET":
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
                    out.println("Resposta a enviar ANONGW peer: " + response);
                    out.flush();
                    s.close();
                    buf = Base64.getEncoder().encode(response.getBytes());
                    peerAddress = packet.getAddress();
                    out.println("Enviado para : " + peerAddress);
                    out.flush();
                    packet = new DatagramPacket(buf, buf.length, peerAddress, 6666);
                    socket.send(packet);
                    out.println("Saiu pacote para o ANONGW peer");
                    out.flush();
                    break;
                case "DATA":
                    Map.Entry<String,Integer> entry = new AbstractMap.SimpleEntry<>(r3Package.clientAddress, r3Package.id);
                    s = pedidos.get(entry);
                    PrintWriter outSocket2 = new PrintWriter(s.getOutputStream());
                    out.println("Pronto para Ligar ao TCP do Client");
                    out.flush();
                    out.println("String Recebida:" + r3Package.toString());
                    out.flush();
                    outSocket2.println(r3Package.toString());
                    outSocket2.flush();
                    s.close();
                    break;

            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
