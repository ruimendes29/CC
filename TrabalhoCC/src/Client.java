import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws Exception{
        String line = " ";
        String own = InetAddress.getLocalHost().getHostAddress();
        int idPedido=1;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (line!=null && !line.equals("exit"))
        {
            line = in.readLine();
            line = line+" "+own+" "+idPedido;
            new Thread(new ClientTCPThread(line,args[0],Integer.parseInt(args[1]))).start();
            idPedido++;
            //response=inSocket.readLine();
            //out.println(response);
            //out.flush();
        }
    }
}
