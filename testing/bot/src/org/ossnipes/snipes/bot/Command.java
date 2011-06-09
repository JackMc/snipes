package org.ossnipes.snipes.bot;

public interface Command
{
	public String getCommand();

	public int getNumberOfArgs();

	public void handleCommand(String command, String from, String[] spaceSplit);
}
