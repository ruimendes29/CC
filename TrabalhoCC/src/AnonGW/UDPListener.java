import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

public class UDPListener implements Runnable{

    Map<Integer,Session> sessions; 
    DatagramSocket socket;
    private byte[] buf = new byte[256];

    public UDPListener(Map<Integer,Session> sessions) {
        this.sessions = sessions;
        
        try{
            this.socket = new DatagramSocket(4445);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
    }

    void processPack(String packString){
        DataPack pack = new DataPack(packString);

        switch (pack.packType) {
            case "NEW":
                
            break;
            case "ACK":
                
            break;
            case "DATA":
                
            break;
            case "CLOSE":
                
            break;
        
            default:
                break;
        }
    }

    public void run(){
        try{
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String packReceived = new String(packet.getData(), 0, packet.getLength());
                this.processPack(packReceived);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
