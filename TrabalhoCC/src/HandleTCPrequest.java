import java.io.*;
import java.net.*;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class HandleTCPrequest implements Runnable{
    private Socket s;
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;
    Map<Map.Entry<String,Integer>,Socket> pedidos;

    public HandleTCPrequest(Socket s,InetAddress peer,DatagramSocket socket,Map<Map.Entry<String,Integer>,Socket> pedidos)
    {
        this.s=s;
        address = peer;
        this.socket=socket;
        this.pedidos=pedidos;
    }
    private synchronized void insertOnPedidos(String cliente,int numPedido)
    {
        Map.Entry<String,Integer> entry =
                new AbstractMap.SimpleEntry<String, Integer>(cliente, numPedido);
        pedidos.put(entry,s);
    }
    @Override
    public void run() {
        String line = " ";
        try {
            PrintWriter out = new PrintWriter(new File("out.txt"));
            if(line!=null)
            {
                out.println("À espera de pedido TCP...");
                out.flush();
                BufferedReader inCliente = new BufferedReader(new InputStreamReader(s.getInputStream()));
                line = inCliente.readLine();
                out.println("Recebeu este pedido por TCP: "+line);
                String [] args = line.split(" ");
                out.flush();
                R3Package r3Packet = null;
                if (args.length==4 && args[0].equals("GET"))
                {
                    insertOnPedidos(args[2],Integer.parseInt(args[3]));
                    r3Packet = new R3Package(args[0],args[1],-1,-1,args[2],Integer.parseInt(args[3]));
                    buf = Base64.getEncoder().encode(r3Packet.toString().getBytes());
                    DatagramPacket packet
                            = new DatagramPacket(buf, buf.length, address, 6666);
                    socket.send(packet);
                    out.println("Envio pacote para o ANONGW peer: "+address);
                    out.flush();
                    //packet = new DatagramPacket(buf, buf.length);
                /*socket.receive(packet);
                System.out.println("Recebeu pacote do 2 ANONGW");
                r3Packet = new R3Package(new String(packet.getData(),0,packet.getLength()));*/
                }
                else if (args.length==4 && args[0].equals("DATA"))
                {
                    r3Packet = new R3Package(args[0],args[1],-1,-1,args[2],Integer.parseInt(args[3]));
                    buf = Base64.getEncoder().encode(r3Packet.toString().getBytes());
                    DatagramPacket packet
                            = new DatagramPacket(buf, buf.length, address, 6666);
                    socket.send(packet);
                    out.println("Envio pacote para o UDP do ANONGW peer: "+address);
                    out.flush();
                }
            /*if (r3Packet != null);
            {
                outCliente.println(r3Packet.data);
                outCliente.flush();
            }*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
