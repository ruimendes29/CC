import java.util.Map;

public class UDPSender {

    Map<Integer,Session> sessions; 

    public UDPSender(Map<Integer,Session> sessions) {
        this.sessions = sessions;
    }
}