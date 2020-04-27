import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
        while (true)
        {
            System.out.println("À espera...");
            Socket s = ss.accept();
            System.out.println("Ligou com anon");
            new Thread(new ServerWorker(s)).start();
        }
    }
}
