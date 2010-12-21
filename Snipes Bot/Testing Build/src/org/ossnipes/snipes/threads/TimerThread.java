package org.ossnipes.snipes.threads;

import org.ossnipes.snipes.utils.Constants;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.ossnipes.snipes.bot.SnipesBot;

import static org.ossnipes.snipes.utils.Configuration.*;

public class TimerThread implements Runnable {
    Timer errorPrintTimer;

    public TimerThread() {
    }

    public void run() {
        Thread t = Thread.currentThread();
        t.setName("Snipes-ErrorHandler");
        errorPrintTimer = new Timer();
        errorPrintTimer.schedule(new SnipesErrorTimerTask(), 0, (Boolean.parseBoolean(lookUp("verbose", "FALSE")) ? Constants.VERBOSE_ERRT : Constants.NON_VERBOSE_ERRT));
    }

    // Private class for Timer.
    private class SnipesErrorTimerTask extends TimerTask {
        @Override
        public void run() {
            Queue<String> errorQueue = SnipesBot.getErrorQueue();
            if (errorQueue.isEmpty()) {
                return;
            }
            for (String s : errorQueue) {
                if (!s.equalsIgnoreCase("")) {
                    System.err.println(s);
                }
            }
            errorQueue.clear();
            // Set it as the error queue
            SnipesBot.setErrorQueue(errorQueue);
        }
    }
}
