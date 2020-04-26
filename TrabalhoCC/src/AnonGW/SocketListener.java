import java.io.InputStream;

public class SocketListener implements Runnable {

    Session session;
    int resendTimer;

    public SocketListener(Session s,int rs) 
    {   
        this.session=s;
        this.resendTimer=rs;
    }

    public void run()
    {
        int lastPackAdded=0;
        DataPack p=null;
        byte[] b=null;
        try {
 
            InputStream inputStream = this.session.getSocket().getInputStream();
    
                while(true){
                    inputStream.read(b);

                    if(b!=null){
                        p = new DataPack(this.session.getSessionID() ,lastPackAdded, this.resendTimer, b);
                        this.session.addPacktoAnon(p);
                    }
                }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}