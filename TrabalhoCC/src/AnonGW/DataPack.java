
public class DataPack {
    
    public String packType;
    public int sessionID;
    public int packID;
    public int timeToResend;
    public Long expireTime;
    public byte[] data;
    
    public DataPack (String packType,int sessionID ,int packID, int timeToResend, byte[] data){
        
        this.packType=packType;
        this.sessionID = sessionID;
        this.packID = packID;
        this.timeToResend = timeToResend;
        this.expireTime = System.currentTimeMillis()+this.timeToResend;
        this.data = data;

    }

    public DataPack (String packString){
        
        String[] fields = packString.split(" ");

        this.packType = fields[0];
        this.sessionID = Integer.parseInt(fields[1]);
        this.packID = Integer.parseInt(fields[2]);
        this.timeToResend = Integer.parseInt(fields[3]);
        this.expireTime = System.currentTimeMillis()+this.timeToResend;
        this.data = fields[4].getBytes();
    }
    
    public boolean hasExpired(){
        return (System.currentTimeMillis()>this.expireTime);
    }

    public void refreshTimer(){
        this.expireTime=System.currentTimeMillis()+this.timeToResend;
    }

    public byte[] getData(){
        return this.data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.packType+" "+this.sessionID+" "+this.packID+" "+this.timeToResend+" "+this.data);
        return sb.toString(); 
    }



}