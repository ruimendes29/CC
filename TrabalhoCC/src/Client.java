import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception{
        String line = " ";
        new Thread(new ClientTCPListen()).start();
        while (line!=null && !line.equals("exit"))
        {
            Socket s = new Socket(args[0],Integer.parseInt(args[1]));
            System.out.println("Ligação Estabelecida");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(System.out);
            PrintWriter outSocket = new PrintWriter(s.getOutputStream());
            out.println("Entrou aqui!");
            out.flush();
            line = in.readLine();
            out.println("Teste - "+line);
            out.flush();
            outSocket.println(line+" "+InetAddress.getLocalHost().getHostAddress());
            outSocket.flush();
            s.close();
            //response=inSocket.readLine();
            //out.println(response);
            //out.flush();
        }
    }
}
