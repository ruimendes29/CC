import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Session {
    
    private int sessionID;
    private Map <Integer,DataPack> toTCPpacks;
    private Map <Integer,DataPack> toAnonpacks;
    private ReentrantLock sessionLock;
    private Condition wakeUpCondition;
    private String targetIP;
    private int targetPort;
    private Socket socket;

    public Session(int sessionID, Socket socket,String targetIP,int targetPort){

        this.sessionID = sessionID;
        this.toTCPpacks = new HashMap <Integer,DataPack>();
        this.toAnonpacks = new HashMap <Integer,DataPack>();
        this.sessionLock = new ReentrantLock();
        this.wakeUpCondition = this.sessionLock.newCondition();
        this.targetIP=targetIP; 
        this.targetPort=targetPort;
        this.socket=socket;
    }


    public int getSessionID() {
        return this.sessionID;
    }

    public Map<Integer,DataPack> getToAnonpacks() {
        return this.toAnonpacks;
    }

    public ReentrantLock getSessionLock() {
        return this.sessionLock;
    }

    public Condition getWakeUpCondition() {
        return this.wakeUpCondition;
    }

    public String getTargetIP() {
        return this.targetIP;
    }

    public int getTargetPort() {
        return this.targetPort;
    }

    public Socket getSocket() {
        return this.socket;
    }    

    public synchronized DataPack getPacktoTCP(int packID){
        return this.toTCPpacks.get(packID);
    }

    public synchronized void addPacktoTCP(DataPack pack){
        this.toTCPpacks.put(pack.packID, pack);
    }

    public synchronized void addPacktoAnon(DataPack pack){
        this.toTCPpacks.put(pack.packID, pack);
    }

    public synchronized void removePacktoTCP(int packID){
        this.toTCPpacks.remove(packID);
    }

    public synchronized List<DataPack> getExpiredAnonPacks(){
            
        ArrayList<DataPack> expiredPacks = new ArrayList<>();
        for(DataPack p : this.toAnonpacks.values()){
            if(p.hasExpired()){
                expiredPacks.add(p);
            }
        }   
        return expiredPacks;
    }

    public synchronized void refreshAnonPacks(ArrayList<Integer> packs ){
        for(Integer i : packs){
            this.toTCPpacks.get(i).refreshTimer();
        }
    }

}    