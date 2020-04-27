import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ListenTCP implements Runnable{
    ArrayList<InetAddress> peers;
    int port;
    public ListenTCP(ArrayList<InetAddress> peers,int port)
    {
        this.peers=peers;
        this.port=port;
    }
    @Override
    public void run() {
        Random r = new Random();
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            while (true)
            {
                System.out.println("À espera...");
                Socket s = ss.accept();
                System.out.println("Ligou com anon");
                new Thread(new HandleTCPrequest(s,peers.get(r.nextInt()%peers.size()))).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
