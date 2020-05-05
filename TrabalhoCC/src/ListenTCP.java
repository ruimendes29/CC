import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ListenTCP implements Runnable{
    ArrayList<InetAddress> peers;
    int port;
    DatagramSocket socket;
    ServerSocket ss;
    Map<Map.Entry<String,Integer>,Socket> pedidos;
    public ListenTCP(ArrayList<InetAddress> peers,int port,DatagramSocket socket,ServerSocket ss,Map<Map.Entry<String,Integer>,Socket> pedidos)
    {
        this.ss=ss;
        this.peers=peers;
        this.port=port;
        this.socket=socket;
        this.pedidos=pedidos;
    }
    @Override
    public void run() {
        Random r = new Random();
        try {
            while (true)
            {
                System.out.println("À espera...");
                Socket s = ss.accept();
                System.out.println("Ligou com Cliente");
                new Thread(new HandleTCPrequest(s,peers.get(r.nextInt()%peers.size()),socket,pedidos)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
