import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class ServerWorker implements Runnable{
    private Socket s;
    public ServerWorker(Socket s)
    {
        this.s=s;
    }
    @Override
    public void run() {
        String line = " ";
        try {
            BufferedReader inCliente = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter outCliente = new PrintWriter(s.getOutputStream());
                line = inCliente.readLine();
                System.out.println("Teste - "+line);
                String [] args = line.split(" ");
                //byte [] content;
                if (args[0].equals("GET"))
                {
                    System.out.println(args[1]);
                    try {
                        String content = new String(Files.readAllBytes(Paths.get("/etc/"+args[1])));
                        String coded = Base64.getEncoder().encodeToString(content.getBytes());
                        outCliente.println(coded);
                        outCliente.flush();
                    }
                    catch (IOException e)
                    {
                        String content = "O ficheiro que está a tentar aceder não existe na diretoria /etc!!";
                        String coded = Base64.getEncoder().encodeToString(content.getBytes());
                        outCliente.println(coded);
                        outCliente.flush();
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}