package org.ossnipes.snipes.console;

import java.util.Scanner;

public class ConsoleManager 
{
	
	public ConsoleManager(Scanner s)
	{
		_s = s;
	}
	// Depreciated so I don't not give messages.
	@Deprecated
	public String getConsoleLine()
	{
		return getConsoleLine("");
	}
	
	public String getConsoleLine(String msg)
	{
		System.out.print(msg);
		return _s.nextLine();
	}
	
	public void println(String msg)
	{
		System.out.println(msg);
	}
	Scanner _s;
}
