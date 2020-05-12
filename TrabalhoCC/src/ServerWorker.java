import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
                if (args[0].equals("GET"))
                {
                    outCliente.println("1 " +
                            "As armas e os barões assinalados, " +
                            "Que da ocidental praia Lusitana, " +
                            "Por mares nunca de antes navegados, " +
                            "Passaram ainda além da Taprobana, " +
                            "Em perigos e guerras esforçados, " +
                            "Mais do que prometia a força humana, " +
                            "E entre gente remota edificaram " +
                            "Novo Reino, que tanto sublimaram; " +
                            " " +
                            "2 " +
                            "E também as memórias gloriosas " +
                            "Daqueles Reis, que foram dilatando " +
                            "A Fé, o Império, e as terras viciosas " +
                            "De África e de Ásia andaram devastando; " +
                            "E aqueles, que por obras valerosas " +
                            "Se vão da lei da morte libertando; " +
                            "Cantando espalharei por toda parte, " +
                            "Se a tanto me ajudar o engenho e arte. " +
                            " " +
                            "3 " +
                            "Cessem do sábio Grego e do Troiano " +
                            "As navegações grandes que fizeram; " +
                            "Cale-se de Alexandro e de Trajano " +
                            "A fama das vitórias que tiveram; " +
                            "Que eu canto o peito ilustre Lusitano, " +
                            "A quem Neptuno e Marte obedeceram: " +
                            "Cesse tudo o que a Musa antígua canta, " +
                            "Que outro valor mais alto se alevanta. " +
                            "\n");
                    outCliente.flush();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}