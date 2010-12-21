package plugins.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WebServerWorker implements Runnable {
    public static String root;
    private Socket rawSock;
    private PrintStream pstream;
    private BufferedReader breader;
    public static final int TIMEOUT = 15;

    public WebServerWorker(Socket mahSocket) throws IOException {
        if (mahSocket == null) {
            throw new NullPointerException("Snipes web server: Socket cannot be null when passed to WebServerWorker.");
        }
        setVars(mahSocket);
    }

    public void service() {
        try {
            System.out.println("Snipes web server: Servicing request from " + rawSock.getInetAddress().getHostName());
            String s;
            List<String> request = new ArrayList<String>();
            while ((s = r()).equals(""))
            {
                request.add(s);
                s = null;
            }
            System.out.println("Finished loop");
            if (request.size() < 1)
                throw new IOException("Nothing received.");
            String fLine = request.get(0);
            String[] basicSplit = fLine.split(" ");
            if (basicSplit.length == 0)
            {
                throw new IOException("Invalid request.");
            }
            else
            {
                System.out.println("Req method: " + basicSplit[0]);
                if (!basicSplit[0].equals("GET"))
                {
                    System.out.println("Sending HTTP/1.0 405 Method Not Allowed.");
                    s("HTTP/1.0 405 Method Not Allowed");
                    s("Server:\tSnipes HTTP Server");
                    s("Content-length:\t0");
                    s("Allow:\tGET");
                    s("Keep-Alive:\ttimeout=" + TIMEOUT);
                    s("Connection:\tKeep-Alive");
                    s();
                }
            }
            } catch (IOException ex) {

        } finally {
            finish();
        }
    }

    /**
     * Alias for sending data to the client.
     *
     * @param s The String to send.
     */
    private void s(String s) {
        pstream.println(s);
    }
    private void s()
    {
        pstream.println();
    }
    /**
     * Alias for getting a line from the client.
     *
     * @return The line, read from the client if there is one. Null otherwise.
     * @throws IOException If readLine() throws a IOException.
     */
    private String r() throws IOException {
        return breader.readLine();
    }

    private void setVars(Socket s) throws IOException {
        rawSock = s;
        pstream = new PrintStream(s.getOutputStream());
        breader = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

    public void run() {
        service();
    }

    public void finish() {
        try {
            rawSock.close();
        } catch (IOException e) {/* Oh well, it'll close itself... */}
    }
}