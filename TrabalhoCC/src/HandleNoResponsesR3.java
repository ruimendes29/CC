import java.io.File;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Base64;
import java.util.Map;

public class HandleNoResponsesR3 implements Runnable{
    /*Classe responsavel por reenviar pedidos que nao tenham recebido o respetivo ack */
    final Map<Integer,Map.Entry<InetAddress,String>> pedidos;
    DatagramSocket socket;
    public HandleNoResponsesR3(Map<Integer,Map.Entry<InetAddress,String>> pedidos,DatagramSocket socket)
    {
        this.socket=socket;
        this.pedidos=pedidos;
    }
    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(new File("non.txt"));
            byte[] buf;
            while (true) {
                Thread.sleep(1000);
                synchronized (pedidos) {
                    for (int chave : pedidos.keySet()) {
                        InetAddress peerAddress;
                        Map.Entry<InetAddress,String> par = pedidos.get(chave);
                        buf = Base64.getEncoder().encode(par.getValue().getBytes());
                        peerAddress = par.getKey();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, peerAddress, 6666);
                        socket.send(packet);
                        out.println("ENVIOU PEDIDO QUE NAO TEVE RESPOSTA!! - "+chave);
                        out.flush();
                    }
                    out.println("FIM DESTA!!");
                    out.flush();
                    out.close();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
