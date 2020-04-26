
public class DataPack {
    
    public String packType;
    public int sessionID;
    public int packID;
    public int timeToResend;
    public Long expireTime;
    public byte[] data;
    
    public DataPack (String packType,int sessionID ,int packID, int timeToResend, byte[] data){
        
        this.sessionID = sessionID;
        this.packID = packID;
        this.timeToResend = timeToResend;
        this.expireTime = System.currentTimeMillis()+this.timeToResend;
        this.data = data;

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


}