import java.net.InetAddress;

public class R3Package {
    public String tipo;
    public String data;
    public int numSeq;
    public int totalPacotes;
    public String clientAddress;
    public int id;
    public int udpID;
    public R3Package(String tipo, String data, int numSeq, int totalPacotes,String clientAddress,int id,int udpID) {
        this.tipo = tipo;
        this.data = data;
        this.numSeq = numSeq;
        this.totalPacotes = totalPacotes;
        this.clientAddress=clientAddress;
        this.id=id;
        this.udpID=udpID;
    }

    @Override
    public String toString() {
        return tipo+" "+data+" "+numSeq+" "+totalPacotes+" "+clientAddress+" "+id+" "+udpID;
    }
    public R3Package(String s)
    {
        String [] args=s.split(" ");
        this.tipo=args[0];
        this.data=args[1];
        this.numSeq=Integer.parseInt(args[2]);
        this.totalPacotes=Integer.parseInt(args[3]);
        this.clientAddress=args[4];
        this.id=Integer.parseInt(args[5]);
        this.udpID=Integer.parseInt(args[6]);
    }
}
