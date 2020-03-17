import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection implements Runnable {
    Socket s;
    String targetServer;
    public Connection(Socket s,String targetServer)
    {
        this.s=s;
        this.targetServer=targetServer;
    }
    @Override
    public void run() {
        String line = " ";
        try {
            BufferedReader inCliente = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter outCliente = new PrintWriter(s.getOutputStream());
            Socket sServer = new Socket(targetServer,80);
            BufferedReader inServer = new BufferedReader(new InputStreamReader(sServer.getInputStream()));
            PrintWriter outServer = new PrintWriter(s.getOutputStream());
            new Thread(new HandleResponse(inServer,outCliente)).start();
        while(line!=null && !line.equals("quit"))
        {
            line = inCliente.readLine();
            outServer.println(line);
            outServer.flush();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
