import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AnonGW {
    static String targetServer;
    static int targetPort;
    static String ownServer;
    static int ownPort;
    static ArrayList<InetAddress> peers;
    public static void main(String[] args) {
        try {
            targetServer=args[0];
            targetPort=Integer.parseInt(args[1]);
            ownServer= Inet4Address.getLocalHost().getHostAddress();
            ownPort=Integer.parseInt(args[2]);
            peers=new ArrayList<>();
            int r=0;
            for (int i=3;i<args.length;i++)
            {
                peers.add(InetAddress.getByName(args[i]));
                System.out.println("ADDED "+peers.get(r++));
            }
            DatagramSocket socket = new DatagramSocket(6666);
            ServerSocket ss = new ServerSocket(ownPort);
            System.out.println("Starting TCP Listener...");
            new Thread(new ListenTCP(peers,ownPort,socket,ss)).start();
            System.out.println("Starting R3 Listener...");
            new Thread(new ListenR3(targetServer,targetPort,socket)).start();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
