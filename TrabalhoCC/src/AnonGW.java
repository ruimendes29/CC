import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AnonGW {
    static String targetServer;
    static int targetPort;
    static String ownServer;
    static int ownPort;
    static ArrayList<AnonGW> peers;
    public static void main(String[] args) {
        try {
            targetServer=args[0];
            targetPort=Integer.parseInt(args[1]);
            ownServer= Inet4Address.getLocalHost().getHostAddress();
            ownPort=Integer.parseInt(args[2]);
            System.out.println(ownServer);
            peers=new ArrayList<>();
            ServerSocket ss = new ServerSocket(ownPort);
            while(true)
            {
                Socket s = ss.accept();
                System.out.println("Inicio Conexão");
                new Thread(new Connection(s,targetServer,targetPort,peers)).start();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
