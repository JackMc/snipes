package org.ossnipes.snipes.threads;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.ossnipes.snipes.bot.SnipesBot;

public class TimerThread implements Runnable {
	Timer errorPrintTimer;
	public TimerThread(){}
	public void run()
	{
		Thread t = Thread.currentThread();
		t.setName("Snipes-ErrorHandler");
		errorPrintTimer = new Timer();
		errorPrintTimer.schedule(new SnipesErrorTimerTask(), 0, 20000);
	}
	
	// Private class for Timer.
	private class SnipesErrorTimerTask extends TimerTask
	{
		@Override
		public void run() {
			Queue<String> errorQueue = SnipesBot.getErrorQueue();
			if (errorQueue.isEmpty())
                        {
                            return;
                        }
                        for (String s : errorQueue)
			{
				if (!s.equalsIgnoreCase(""))
				{
					System.err.println(s);
				}
			}
			errorQueue.clear();
                        // Set it as the error queue
                        SnipesBot.setErrorQueue(errorQueue);
		}
	}
}
