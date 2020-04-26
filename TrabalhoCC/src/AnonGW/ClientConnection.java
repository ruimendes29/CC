
public class ClientConnection implements Runnable {
    
    Session session;
    
    public ClientConnection(Session s)
    {
        this.session=s;
    }

    @Override
    public void run() {
            System.out.println("Entrou aqui!");
            new Thread(new SocketResponder(this.session)).start();
            new Thread(new SocketListener(this.session,3000)).start();
    }
}
