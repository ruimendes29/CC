import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HandleResponse implements Runnable {
    BufferedReader in;
    PrintWriter out;
    public HandleResponse(BufferedReader in,PrintWriter out)
    {
        this.in=in;
        this.out=out;
    }
    public void run()
    {
        String line;
        try {
            do {
                line = in.readLine();
                out.println(line);
            } while (line != null);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
