import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;

public class ClientTCPThread implements Runnable{
    private String ipAnon;
    private int portAnon;
    private String line;
    public ClientTCPThread(String line, String ipAnon, int portAnon) {
        this.ipAnon = ipAnon;
        this.portAnon = portAnon;
        this.line=line;
    }

    @Override
    public void run() {
        try {
            Socket s = new Socket(ipAnon, portAnon);
            PrintWriter out = new PrintWriter(System.out);
            BufferedReader inSocket = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter outSocket = new PrintWriter(s.getOutputStream());
            // Envia para o anon o pedido feito pelo cliente
            outSocket.println(line);
            outSocket.flush();
            // Resposta dada pelo anon é processada
            String response = inSocket.readLine();
            out.println("RESPOSTA: \n"+ new String (Base64.getDecoder().decode(response)));
            out.flush();
            s.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
