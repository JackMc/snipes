package org.ossnipes.snipes.exceptions;

import org.ossnipes.snipes.bot.SnipesBot;

/**
 * org.ossnipes.snipes.exceptions.SnipesUExceptionHandler
 * This code is licensed under the GNU GPL.
 *
 * @author Jack McCracken (Auv5)
 */
public class SnipesUExceptionHandler implements Thread.UncaughtExceptionHandler {
    public SnipesUExceptionHandler()
    {}

    public void uncaughtException(Thread thread, Throwable throwable) {
        System.err.println("Uncaught exception in thread " + thread + ".");
        System.err.println("Type: " + throwable);
        System.err.println("Message: \"" + throwable.getMessage() + "\"");
        System.err.println();
        if (SnipesBot.DEBUG)
        {
            throwable.printStackTrace();
        }
    }
}
