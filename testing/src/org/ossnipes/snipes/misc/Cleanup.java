package org.ossnipes.snipes.misc;

import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.exceptions.NoSnipesInstanceException;

/** Snipes Shutdown Hooks
 * @author Jack McCracken
 */
public class Cleanup implements Runnable {

    public void run() {
        try
        {
            SnipesBot b = SnipesBot.getInst();
            b.exitSnipes(0);
        } catch (NoSnipesInstanceException e) { System.err.println("Bot ending without cleanup, this theoreticly shouldn't happen, so heres the message: " + e.getMessage()); }
    }

}
