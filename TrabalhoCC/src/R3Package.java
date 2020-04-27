public class R3Package {
    public String tipo;
    public String data;
    public int numSeq;
    public int totalPacotes;
    public R3Package(String tipo, String data, int numSeq, int totalPacotes) {
        this.tipo = tipo;
        this.data = data;
        this.numSeq = numSeq;
        this.totalPacotes = totalPacotes;
    }

    @Override
    public String toString() {
        return tipo+" "+data+" "+numSeq+" "+totalPacotes;
    }
    public R3Package(String s)
    {
        String [] args=s.split(" ");
        this.tipo=args[0];
        this.data=args[1];
        this.numSeq=Integer.parseInt(args[2]);
        this.totalPacotes=Integer.parseInt(args[3]);
    }
}
