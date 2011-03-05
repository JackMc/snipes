package org.ossnipes.snipes.console;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SpamConsole 
{
	private static final class SpamConsoleConnector implements Runnable {
		public static volatile int failures;
		@Override
		public void run() {
			SocketManager sm = null;
			try {
				sm = new SocketManager(new Socket("localhost", 9001));
			} catch (UnknownHostException e) {
				fail();
				return;
			} catch (IOException e) {
				fail();
				return;
			}
			
			sm.sendln("HELLO");
			sm.sendln("example");
			sm.sendln("password");
			try {
				sm.recv();
				for (int i = 1; i <= 5; i++)
				{
					sm.sendln("SAYHELLO");
					sm.recv();
					sm.recv();
				}
			} catch (IOException e) {
				fail();
				return;
			}
		}
		private void fail() {
			failures++;
			return;
		}
	}

	// Stress testing the server
	public static void main(String args[])
	{
		final int TOTAL = 10000;
		int i = 0;
		
		
		while (i < TOTAL)
		{
			new Thread(new SpamConsoleConnector()).start();
			i++;
		}
		
		System.err.println("Failures: " + SpamConsoleConnector.failures);
	}
}
