import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception{
        Socket s = new Socket(args[0],Integer.parseInt(args[1]));
        System.out.println("Ligação Estabelecida");
        String line = " ";
        String response;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        BufferedReader inSocket = new BufferedReader((new InputStreamReader(s.getInputStream())));
        PrintWriter outSocket = new PrintWriter(s.getOutputStream());
        while (line!=null && !line.equals("exit"))
        {
            out.println("Entrou aqui!");
            out.flush();
            line = in.readLine();
            out.println("Teste - "+line);
            out.flush();
            outSocket.println(line);
            outSocket.flush();
            response=inSocket.readLine();
            out.println(response);
            out.flush();
        }
    }
}
