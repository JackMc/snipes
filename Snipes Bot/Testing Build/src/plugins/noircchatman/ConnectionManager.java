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
            currentJob = popConnection();
            handle();
        }
    }
    public NoIRCConnection popConnection()
    {
        try
        {
            return parent.getJob();
        }
        catch (InterruptedException e) {System.err.println("Interrupted while getting a job for the Non-IRC chat manager..");}
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
                    String[] ex = s.split(" ");
                    if (ex.length == 0)
                    {
                        continue;
                    }
                    
                    if (ex[0].equalsIgnoreCase("die"))
                    {
                        int status = 0;
                        try
                        {
                            status = Integer.parseInt((ex.length > 1 ? 
                            ex[1] : "0"));
                        } catch (NumberFormatException e) {}
                        currentJob.send("Exiting with status " + 
                        status);
                        try
                        {
                            parent.exit(status,"Die command from a non-IRC source");
                        } catch 
                        (org.ossnipes.snipes.exceptions.NoSnipesInstanceException e)
                        {
                            currentJob.send("Somehow, you managed to tell me to die before I was done starting up! Please try again in a few seconds.");
                        }
                    }
                }
            }
        }
    }
}
