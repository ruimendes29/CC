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
            byte[] buf;
            while (true) {
                /*
                Esta thread é responsavel por de um tempo em um tempo percorrer os pedidos
                que ainda nao obtiveram resposta e reenvialos de forma a que caso uma confirmação se tenha perdido
                ou o pedido nao tenha chegado ao destino ambos possam ser reenviados
                 */
                Thread.sleep(1000);
                synchronized (pedidos) {
                    for (int chave : pedidos.keySet()) {
                        InetAddress peerAddress;
                        Map.Entry<InetAddress,String> par = pedidos.get(chave);
                        buf = Base64.getEncoder().encode(par.getValue().getBytes());
                        peerAddress = par.getKey();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, peerAddress, 6666);
                        socket.send(packet);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
