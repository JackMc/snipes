package org.ossnipes.snipes.threads;

import java.io.IOException;
import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.exceptions.NoSnipesInstanceException;
import org.ossnipes.snipes.utils.Configuration;

/** Snipes Shutdown Hooks
 * @author Jack McCracken
 */
public class Cleanup implements Runnable {

    public void run() {
        try
        {
            Configuration.finish();
            SnipesBot b = SnipesBot.getInst();
            b.exitSnipes(0);
        } catch (NoSnipesInstanceException e) { System.err.println("Bot ending without cleanup, this theoreticly shouldn't happen, so heres the message: " + e.getMessage()); }
        catch (IOException e) {System.err.println("Could not save configuration during shutdown.");}
    }

}
