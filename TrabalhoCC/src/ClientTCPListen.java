import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientTCPListen implements Runnable{
    @Override
    public void run() {
        try {
            String line = " ";
            ServerSocket ss = new ServerSocket(12345);
            while (true) {
                Socket s = ss.accept();
                BufferedReader inCliente = new BufferedReader(new InputStreamReader(s.getInputStream()));
                line = inCliente.readLine();
                PrintWriter out = new PrintWriter(System.out);
                out.println("RESPOSTA - " + line);
                out.flush();
                s.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
