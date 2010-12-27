package org.ossnipes.snipes.threads;

import java.io.IOException;

import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.exceptions.NoSnipesInstanceException;
import org.ossnipes.snipes.utils.Configuration;

/**
 * Snipes Shutdown Hooks
 *
 * @author Jack McCracken
 */
public class Cleanup implements Runnable {

    public void run() {
        try {
            Configuration.finish();
            SnipesBot b = SnipesBot.getInst();
            b.exitSnipes(0);
        } catch (NoSnipesInstanceException e) {
            System.err.println("The bot seems to have crashed or have been shut down before the bot even had a chance to load it's \nplugins! This just means that there was nothing to end, so we didn't end it. \nJust thought you should know...");
        } catch (IOException e) {
            System.err.println("Could not save configuration during shutdown.");
        }
    }

}
