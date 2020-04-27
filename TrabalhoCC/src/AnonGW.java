import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
            new Thread(new ListenTCP(peers,ownPort)).start();
            new Thread(new ListenR3(peers,ownPort,targetServer,targetPort)).start();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
