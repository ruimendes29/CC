import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListenR3 implements Runnable{
    DatagramSocket socket;
    String serverAddress;
    int serverPort;
    InetAddress peerAddress;
    private byte[] buf = new byte[256];
    final Map<Map.Entry<String,Integer>,Socket> pedidos;
    final Map<Map.Entry<String,Integer>,String []> builders;
    final Map<Map.Entry<String,Integer>,String []> totais;
    final Map<Integer,Map.Entry<InetAddress,String>> porResponder;
    ArrayList<InetAddress> peers;
    IntHolder udpID;
    public ListenR3(ArrayList<InetAddress> peers,String serverAddress,int serverPort,DatagramSocket socket,Map<Map.Entry<String,Integer>,Socket> pedidos,Map<Integer,Map.Entry<InetAddress,String>> porResponder)
    {
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
        this.socket=socket;
        this.pedidos=pedidos;
        this.peers=peers;
        builders=new HashMap<>();
        totais= new HashMap<>();
        this.porResponder=porResponder;
        udpID = new IntHolder(1);
    }
    @Override
    public void run() {

        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                new Thread(new HandleR3Response(serverAddress,serverPort,socket,packet,pedidos,builders,totais,porResponder,udpID)).start();
                }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
