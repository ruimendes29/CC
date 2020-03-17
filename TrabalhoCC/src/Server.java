import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static String targetServer;
    public Server(String targetServer)
    {
        this.targetServer=targetServer;
    }
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(80);
            while(true)
            {
                Socket s = ss.accept();
                new Thread(new Connection(s,targetServer)).start();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
