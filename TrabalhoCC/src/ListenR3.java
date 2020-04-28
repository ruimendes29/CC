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
    InetAddress peerAddress;
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
            Socket s;
            switch (r3Package.tipo)
            {
                case "GET":
                    s = new Socket(serverAddress,serverPort);
                    String response;
                    PrintWriter out = new PrintWriter(System.out);
                    BufferedReader inSocket = new BufferedReader((new InputStreamReader(s.getInputStream())));
                    PrintWriter outSocket = new PrintWriter(s.getOutputStream());
                        out.println("Entrou no ANONGW peer!");
                        out.flush();
                        out.println("String Recebida:"+r3Package.toString());
                        out.flush();
                        outSocket.println(r3Package.toString());
                        outSocket.flush();
                        response=inSocket.readLine();
                        out.println("Resposta a enviar ANONGW peer: "+response);
                        out.flush();
                        s.close();
                        buf = response.getBytes();
                    peerAddress = packet.getAddress();
                    packet = new DatagramPacket(buf, buf.length, peerAddress, 6666);
                    socket.send(packet);
                    out.println("Saiu pacote para o ANONGW peer");
                    out.flush();
                        break;
                case "DATA":
                    s = new Socket(r3Package.clientAddress,12345);
                    PrintWriter out2 = new PrintWriter(System.out);
                    PrintWriter outSocket2 = new PrintWriter(s.getOutputStream());
                    out2.println("Pronto para Ligar ao TCP do Client");
                    out2.flush();
                    out2.println("String Recebida:"+r3Package.toString());
                    out2.flush();
                    outSocket2.println(r3Package.toString());
                    outSocket2.flush();
                    s.close();
                    break;

            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
