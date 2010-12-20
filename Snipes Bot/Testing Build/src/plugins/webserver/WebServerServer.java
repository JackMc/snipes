package plugins.webserver;
import org.ossnipes.snipes.bot.SnipesBot;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import static org.ossnipes.snipes.utils.Configuration.*;

public class WebServerServer {
    private static final int DEFAULT_PORT = 9001;
    public static final String DEFAULT_ROOT = "./wsres";
    public static double VERSION = 0.01;
    public void begin() {
        new Thread()
        {
            ServerSocket serverSocket;

            @Override
            public void run()
            {
                // Print the version and other things to STDOUT.
                System.out.println("Starting Snipes web server, version " + VERSION + ".");
                System.out.println("Snipes web server: Loading. Please wait...");

                // Get all the directives we need, passing errors to the Snipes
                // Error Queue

                String portString = lookUp("wsport");
                int portInt = DEFAULT_PORT;
                try
                {
                    portInt = Integer.parseInt(portString);
                } catch (NumberFormatException e) {
                    SnipesBot.addToErrorQueue("The directive \"wsport\" in the Snipes configuration file is not a number. Using default: " + portInt);
                }

                String wsroot = DEFAULT_ROOT;
                wsroot = lookUp("wsroot",DEFAULT_ROOT);
                File f = new File(wsroot);
                if (!f.exists())
                {
                    SnipesBot.addToErrorQueue("Snipes web server: File at " + f.getAbsolutePath() + " does not exist. Using default (\"" + DEFAULT_ROOT + "\".)");
                    wsroot = DEFAULT_ROOT;
                }
                else if (!f.isDirectory())
                {
                    SnipesBot.addToErrorQueue(f.getAbsolutePath() + " is not a directory, using default (\"" + DEFAULT_ROOT + "\".)");
                }
                System.out.println("Snipes web server: Attempting to bind to port " + portInt + ".");

                boolean bindSuccess = true;
                String msg = null;
                try
                {
                    serverSocket = new ServerSocket(portInt);
                } catch (IOException e) {
                    msg = e.getMessage();
                    bindSuccess = false;
                }
                if (!bindSuccess)
                {
                    SnipesBot.addToErrorQueue("Snipes web server: Could not bind to port " + portInt + ". The error message was: " + msg + ". Unloading...");
                    return;
                }
                else
                {
                    System.out.println("Snipes web server: Binding to port " + portInt + " was successful. Continuing.");
                }

                WebServerWorker.root = wsroot;

                Thread t = null;

                // Main loop
                while (true)
                {
                    try
                    {
                        t = new Thread(new WebServerWorker(serverSocket.accept()));
                        t.start();
                    } catch (IOException e) {SnipesBot.addToErrorQueue("Snipes web server: Client disconnected before we could initialize!");}
                    t = null;
                }
            }
        }.start();
    }
}
