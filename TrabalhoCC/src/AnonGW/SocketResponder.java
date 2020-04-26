import java.io.OutputStream;

public class SocketResponder implements Runnable {

    Session session;

    public SocketResponder(Session s)
    {   
        this.session=s;
    }
    public void run()
    {
        int lastPackSent=0;
        DataPack p;
        try {
        OutputStream outStream = this.session.getSocket().getOutputStream();

            while(true){
                this.session.getSessionLock().lock();
                this.session.getWakeUpCondition().await();

                while((p=session.getPacktoTCP(lastPackSent))!=null){
                    outStream.write(p.getData());
                    outStream.flush();
                    this.session.removePacktoTCP(lastPackSent);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
