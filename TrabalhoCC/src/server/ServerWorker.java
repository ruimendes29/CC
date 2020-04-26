import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable{
    private Socket s;
    public ServerWorker(Socket s)
    {
        this.s=s;
    }
    @Override
    public void run() {
        String line = " ";
        try {
            BufferedReader inCliente = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter outCliente = new PrintWriter(s.getOutputStream());
            while(line!=null && !line.equals("quit"))
            {
                line = inCliente.readLine();
                System.out.println("Teste - "+line);
                outCliente.println(line);
                outCliente.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
