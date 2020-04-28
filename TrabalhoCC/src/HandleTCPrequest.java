import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class HandleTCPrequest implements Runnable{
    private Socket s;
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;


    public HandleTCPrequest(Socket s,InetAddress peer,DatagramSocket socket)
    {
        this.s=s;
            address = peer;
            this.socket=socket;
    }

    @Override
    public void run() {
        String line = " ";
        try {
            while(line!=null)
            {
                System.out.println("À espera de pedido TCP...");
                BufferedReader inCliente = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter outCliente = new PrintWriter(s.getOutputStream());
                line = inCliente.readLine();
                String [] args = line.split(" ");
                R3Package r3Packet = null;
                if (args.length==3 && args[0].equals("GET"))
                {
                    r3Packet = new R3Package(args[0],args[1],-1,-1,args[2]);
                    buf = r3Packet.toString().getBytes();
                    DatagramPacket packet
                            = new DatagramPacket(buf, buf.length, address, 6666);
                    socket.send(packet);
                    System.out.println("Envio pacote para o ANONGW peer: "+address);
                    //packet = new DatagramPacket(buf, buf.length);
                /*socket.receive(packet);
                System.out.println("Recebeu pacote do 2 ANONGW");
                r3Packet = new R3Package(new String(packet.getData(),0,packet.getLength()));*/
                }
                else if (args.length==3 && args[0].equals("DATA"))
                {
                    r3Packet = new R3Package("DATA",args[1],-1,-1,args[2]);
                    buf = r3Packet.toString().getBytes();
                    DatagramPacket packet
                            = new DatagramPacket(buf, buf.length, address, 6666);
                    socket.send(packet);
                    System.out.println("Envio pacote para o UDP do ANONGW peer: "+address);
                }
            /*if (r3Packet != null);
            {
                outCliente.println(r3Packet.data);
                outCliente.flush();
            }*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
