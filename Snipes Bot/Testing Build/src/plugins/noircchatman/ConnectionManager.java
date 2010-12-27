package plugins.noircchatman;

import org.ossnipes.snipes.bot.SnipesBot;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * plugins.noircchatman.ConnectionManager
 * This code is licensed under the GNU GPL.
 *
 * @author Jack McCracken (Auv5)
 */
public class ConnectionManager implements Runnable {

    NoIRCMain parent;
    int num;
    NoIRCConnection currentJob;

    public ConnectionManager(NoIRCMain parent, int num)
    {
        this.parent = parent;
        this.num = num;
    }

    public void run()
    {
        if (SnipesBot.DEBUG)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Thread# " + num + " starting up...");
        }
        int curr;
        while (true)
        {
            System.err.println("Starting blocking call.");
            currentJob = popConnection();
            System.out.println("Got a connection from " + currentJob.getHost());
            handle();
        }
    }
    public NoIRCConnection popConnection()
    {
        try
        {
            return parent.getJob();
        }
        catch (InterruptedException e) {System.err.println("Interrupted while getting a job.");}
        return null;
    }
    public void handle()
    {
        boolean quit = false;
        while (!quit)
        {
            if (currentJob.isOpen())
            {
                currentJob.close();
                quit = true;
                break;
            }
            else
            {
                String s;
                while ((s = currentJob.recv()) != null)
                {

                }
            }
        }
    }
}
