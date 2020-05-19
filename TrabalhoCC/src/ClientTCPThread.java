import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
            System.out.println("Ligação Estabelecida");
            PrintWriter out = new PrintWriter(System.out);
            BufferedReader inSocket = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter outSocket = new PrintWriter(s.getOutputStream());
            out.println("Entrou aqui!");
            out.flush();
            out.println("Teste - " + line);
            out.flush();
            outSocket.println(line);
            outSocket.flush();
            String response = inSocket.readLine();
            out.println("RESPOSTA: "+ response);
            out.flush();
            s.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
