import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnonGW {
    static String targetServer;
    static int targetPort;
    static String ownServer;
    static int ownPort;
    static ArrayList<String> peers;
    static Map<Integer,Session> sendSessions;
    static Map<Integer,Session> receiveSessions;

    
    public static void main(String[] args) {

    
        try {
            targetServer=args[0];
            targetPort=Integer.parseInt(args[1]);
            ownServer= Inet4Address.getLocalHost().getHostAddress();
            ownPort=Integer.parseInt(args[2]);

            Map<Integer,Session> Sessions=new HashMap<Integer,Session>();
            Session currentSession;
            
            int sessionID = Integer.parseInt(args[3])*1000000;

            System.out.println(ownServer);

            //Falta iniciat os peers
            peers=new ArrayList<String>();
            ServerSocket ss = new ServerSocket(ownPort);
            
            //Turn on the UDP receiver
            //new Thread(new ClientConnection(session,targetServer,targetPort)).start();
           
            //Start Listening for client connections
            while(true)
            {
                Socket socket = ss.accept();
                System.out.println("Inicio Conexão");

                int random = (int)(Math.random()*(peers.size()-1));
                currentSession= new Session(sessionID,socket,peers.get(random),targetPort);
                Sessions.put(sessionID,currentSession);
                sessionID++;
                //
                new Thread(new ClientConnection(currentSession)).start();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
