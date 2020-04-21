import java.time.LocalDateTime;

public class DataPack {
    
    public String packType;
    public int sessionID;
    public int packID;
    public Long timeToResend;
    public Long expireTime;
    public Byte[] data;
    
    public DataPack (int sessionID ,int packID, Long timeToResend, Byte[] data){
        
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


}