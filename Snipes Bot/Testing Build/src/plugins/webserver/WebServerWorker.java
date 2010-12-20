package plugins.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class WebServerWorker implements Runnable {
    public static String root;
    private Socket rawSock;
    private PrintStream pstream;
    private BufferedReader breader;

    public WebServerWorker(Socket mahSocket) throws IOException {
      if (mahSocket == null)
        {
            throw new NullPointerException("Snipes web server: Socket cannot be null when passed to WebServerWorker.");
        }
        setVars(mahSocket);
    }

    public void begin()
    {
        try {
            String resp = r();
        } catch (IOException ex) {
            System.err.println("Recieved nothing from client at " + rawSock.getInetAddress() + ". This generally means someone is trying to connect with something other than a web browser. Disconnecting the client...");
        }
        finally
        {
            finish();
        }
    }

    /** Alias for sending data to the client.
     * @param s The String to send.
     */
    private void s(String s)
    {
        pstream.println(s);
    }

    /** Alias for getting a line from the client.
     * @return The line, read from the client if there is one. Null otherwise.
     * @throws IOException If readLine() throws a IOException.
     */
    private String r() throws IOException
    {
            return breader.readLine();
    }
    private void setVars(Socket s) throws IOException
    {
        rawSock = s;
        pstream = new PrintStream(s.getOutputStream());
        breader = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

    public void run() {
        begin();
    }
    public void finish()
    {
        try
        {
            rawSock.close();
        } catch (IOException e) {/* Oh well, it'll close itself... */}
    }
}
