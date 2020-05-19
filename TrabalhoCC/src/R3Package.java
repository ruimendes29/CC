
public class R3Package {
    public String tipo;
    public String data;
    public int numSeq;
    public int totalPacotes;
    public String clientAddress;
    public int id;
    public int udpID;
    private int authKey;
    public R3Package(String tipo, String data, int numSeq, int totalPacotes,String clientAddress,int id,int udpID) {
        this.tipo = tipo;
        this.data = data;
        this.numSeq = numSeq;
        this.totalPacotes = totalPacotes;
        this.clientAddress = clientAddress;
        this.id=id;
        this.udpID=udpID;
        this.authKey= numSeq*id-udpID*37+100;
    }

    @Override
    public String toString() {
        return tipo+" "+data+" "+numSeq+" "+totalPacotes+" "+clientAddress+" "+id+" "+udpID+" "+authKey;
    }

    public Boolean verifyKey(){
        System.out.println(this.authKey);
        System.out.println(this.numSeq*this.id-this.udpID*37+100);
        return (this.authKey == this.numSeq*this.id-this.udpID*37+100);
    }

    public R3Package(String s,Boolean missingKey)
    {
        String [] args=s.split(" ");
        this.tipo=args[0];
        this.data=args[1];
        this.numSeq=Integer.parseInt(args[2]);
        this.totalPacotes=Integer.parseInt(args[3]);
        this.clientAddress=args[4];
        this.id=Integer.parseInt(args[5]);
        this.udpID = Integer.parseInt(args[6]);

        if(missingKey)
            this.authKey = this.numSeq*this.id-this.udpID*37+100; 
    }


    public R3Package(String s)
    {
        System.out.println("ola");
        String [] args=s.split(" ");
        this.tipo=args[0];
        this.data=args[1];
        this.numSeq=Integer.parseInt(args[2]);
        this.totalPacotes=Integer.parseInt(args[3]);
        this.clientAddress=args[4];
        this.id=Integer.parseInt(args[5]);
        this.udpID = Integer.parseInt(args[6]);
        this.authKey = Integer.parseInt(args[7]); 
    }
}
