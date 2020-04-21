import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Session {
    
    private int sessionID;
    private Map <Integer,DataPack> packs;
    private ReentrantLock sessionLock;
    private Condition wakeUpCondition;
    private String targetIP;

    public Session(int sessionID){

        this.sessionID = sessionID;
        this.packs = new HashMap <Integer,DataPack>();
        this.sessionLock = new ReentrantLock();
        this.wakeUpCondition = this.sessionLock.newCondition(); 
    }

    public DataPack getPack(int packID){
        return this.packs.get(packID);
    }

    public void addPack(DataPack pack){
        this.packs.put(pack.packID, pack);
    }

    public void removePack(int packID){
        this.packs.remove(packID);
    }

    public List<DataPack> getExpiredPacks(){
            
        ArrayList<DataPack> expiredPacks = new ArrayList<>();
        for(DataPack p : this.packs.values()){
            if(p.hasExpired()){
                expiredPacks.add(p);
            }
        }   
        return expiredPacks;
    }

    public void refreshPacks(ArrayList<Integer> packs ){
        for(Integer i : packs){
            this.packs.get(i).refreshTimer();
        }
    }

}    