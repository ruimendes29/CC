import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ListenR3 implements Runnable{
    ArrayList<InetAddress> peers;
    Lock l ;
    int port;
    DatagramSocket socket;
    String serverAddress;
    int serverPort;
    private byte[] buf = new byte[256];
    public ListenR3(ArrayList<InetAddress> peers,int port,String serverAddress,int serverPort,DatagramSocket socket)
    {
        this.peers=peers;
        l = new ReentrantLock(true);
        this.port=port;
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
        this.socket=socket;
    }
    @Override
    public void run() {

        try {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received=new String(packet.getData(), 0, packet.getLength());
            R3Package r3Package = new R3Package(received);
            switch (r3Package.tipo)
            {
                case "GET":
                    Socket s = new Socket(serverAddress,serverPort);
                    System.out.println("Ligação Estabelecida");
                    String line = " ";
                    String response;
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    PrintWriter out = new PrintWriter(System.out);
                    BufferedReader inSocket = new BufferedReader((new InputStreamReader(s.getInputStream())));
                    PrintWriter outSocket = new PrintWriter(s.getOutputStream());
                        out.println("Entrou aqui!");
                        out.flush();
                        out.println("Teste - "+r3Package.toString());
                        out.flush();
                        outSocket.println(r3Package.toString());
                        outSocket.flush();
                        response=inSocket.readLine();
                        out.println(response);
                        out.flush();
                        s.close();
                        buf = response.getBytes();
                        break;
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
    }

}
