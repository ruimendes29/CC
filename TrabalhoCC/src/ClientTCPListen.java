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
                PrintWriter outCliente = new PrintWriter(s.getOutputStream());
                line = inCliente.readLine();
                System.out.println("RESPOSTA - " + line);
                s.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
