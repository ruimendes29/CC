import java.io.*;
import java.net.*;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.Map;

public class HandleTCPrequest implements Runnable{
    private Socket s;
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;
    Map<Map.Entry<String,Integer>,Socket> pedidos;

    public HandleTCPrequest(Socket s,InetAddress peer,DatagramSocket socket,Map<Map.Entry<String,Integer>,Socket> pedidos)
    {
        this.s=s;
        address = peer;
        this.socket=socket;
        this.pedidos=pedidos;
    }
    private synchronized void insertOnPedidos(String cliente,int numPedido)
    {
        Map.Entry<String,Integer> entry =
                new AbstractMap.SimpleEntry<>(cliente, numPedido);
        pedidos.put(entry,s);
    }
    @Override
    public void run() {
        String line = " ";
        try {
            PrintWriter out = new PrintWriter(new File("out.txt"));
            if(line!=null)
            {
                out.println("À espera de pedido TCP...");
                out.flush();
                BufferedReader inCliente = new BufferedReader(new InputStreamReader(s.getInputStream()));
                line = inCliente.readLine();
                String [] args = line.split(" ");
                R3Package r3Packet = null;
                if (args.length==4 && args[0].equals("wget"))
                {
                    insertOnPedidos(args[2],Integer.parseInt(args[3]));
                    r3Packet = new R3Package("GET",args[1],-1,-1,args[2],Integer.parseInt(args[3]),-1);
                    buf = Base64.getEncoder().encode(r3Packet.toString().getBytes());
                    DatagramPacket packet
                            = new DatagramPacket(buf, buf.length, address, 6666);
                    socket.send(packet);
                    out.println("Envio pacote para o ANONGW peer: "+address);
                    out.flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
