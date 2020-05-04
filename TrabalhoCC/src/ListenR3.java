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
    DatagramSocket socket;
    String serverAddress;
    int serverPort;
    InetAddress peerAddress;
    private byte[] buf = new byte[256];
    public ListenR3(String serverAddress,int serverPort,DatagramSocket socket)
    {
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
        this.socket=socket;
    }
    @Override
    public void run() {

        try {
            PrintWriter out = new PrintWriter(System.out);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                new Thread(new HandleR3Response(serverAddress,serverPort,socket,packet)).start();
                }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
