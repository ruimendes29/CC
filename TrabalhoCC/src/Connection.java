import javax.security.auth.login.AccountNotFoundException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable {
    Socket s;
    String targetServer;
    int targetPort;
    ArrayList<AnonGW> peers;
    public Connection(Socket s, String targetServer, int targetPort, ArrayList<AnonGW> peers)
    {
        this.s=s;
        this.targetServer=targetServer;
        this.targetPort=targetPort;
        this.peers=peers;
    }
    @Override
    public void run() {
        String line = " ";
        System.out.println("Entrou aqui!");
        try {
            BufferedReader inCliente = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter outCliente = new PrintWriter(s.getOutputStream());
            Socket sServer = new Socket(targetServer,targetPort);
            BufferedReader inServer = new BufferedReader(new InputStreamReader(sServer.getInputStream()));
            PrintWriter outServer = new PrintWriter(s.getOutputStream());
        while(line!=null && !line.equals("quit"))
        {
            line = inCliente.readLine();
            System.out.println("Teste - "+line);
            outServer.println(line);
            outServer.flush();
            new Thread(new HandleResponse(inServer,outCliente)).start();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
