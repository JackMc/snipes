package org.ossnipes.snipes.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.enums.PluginPassResponse;
import org.ossnipes.snipes.enums.SnipesEvent;
import org.ossnipes.snipes.spf.PluginType;
import org.ossnipes.snipes.spf.SnipesEventParams;
import org.ossnipes.snipes.spf.SuperPlugin;
import org.ossnipes.snipes.utils.Configuration;
import org.ossnipes.snipes.utils.Constants;
public class TestMain {
	Scanner defScan = new Scanner(System.in);
	public TestMain() throws IOException, ClassNotFoundException
	{
		printOptions();
	}
	
	public void printOptions() throws IOException, ClassNotFoundException
	{
		System.out.println("Snipes testing suite\n");
		System.out.println("There are various tests avaliable to you: ");
		System.out.println("Type 1 for a full event test (default params)");
		System.out.println("Type 2 for bot connection test without any plugins");
		System.out.println("Type 3 for bot connection test with plugins");
		System.out.println("Type 4 for a selective event test (your params)");
		System.out.println("Type 5 for a configuration test");
		System.out.println("Type 6 for a list of all on-load plugins.");
		System.out.println("Type 7 for all config directives.");
		
		System.out.println("Please type your selection:");
		int s = 0;
		try
		{
			s = Integer.parseInt(defScan.nextLine());
		} catch (NumberFormatException e) {System.out.println("Invalid option");printOptions();}
		switch (s)
		{
		case 1:
			eventTest();
			break;
		case 2:
			botConn(false);
			//printOptions();
			break;
		case 3:
			botConn(true);
			//printOptions();
			break;
		case 4:
			selectiveEventTest();
			break;
		case 5:
			printOptions();
			//configurationTest();
			break;
		case 6:
			listPlugins();
			break;
		case 7:
			getConfigDirectives();
			break;
		default:
			System.out.println("Invalid option.");
			printOptions();
		}
	}
	private void eventTest() throws FileNotFoundException
	{
		SnipesBot b = new SnipesBot(true,true);
		
		SnipesEvent[] events = SnipesEvent.values();
		for (SnipesEvent e : events)
		{
			String p = "PLUGIN_NOTIMPLEMENTED";
			switch (e)
			{
			case SNIPES_INT_CONSOLEOUT:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"Hello world!"})).toString();
				break;
			case SNIPES_INT_PLUGINLOAD:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"Test", SuperPlugin.class.getCanonicalName()})).toString();
				break;
			case SNIPES_INT_PLUGINUNLOAD:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"Test", SuperPlugin.class.getCanonicalName()})).toString();
				break;
			case SNIPES_IRC_AUTONICKCHANGE:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {Boolean.toString(true)})).toString();
				break;
			case SNIPES_IRC_CONNENCT:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b, new String [] {"irc.freenode.net"})).toString();
				break;
			case SNIPES_IRC_DISCONNECT:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b, new String [] {"irc.freenode.net"})).toString();
				break;
			case SNIPES_IRC_IDENTSTART:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
								format(Calendar.getInstance().getTime())})).toString();
				break;
			case SNIPES_IRC_IDENTSTOP:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
								format(Calendar.getInstance().getTime())})).toString();
				break;
			case SNIPES_IRC_JOIN:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b, new String [] {"#Snipes","SnipesKey"})).toString();
				break;
			case SNIPES_IRC_PART:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b, new String [] {"#Snipes", "baibai"})).toString();
				break;
			case SNIPES_IRC_PRIVMSG:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"#Snipes","Hello Snipes!"})).toString();
				break;
			case SNIPES_IRC_US_BAN:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"#Snipes","*!*@*"})).toString();
				break;
			case SNIPES_IRC_ACTION:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"Snipes","HW"})).toString();
				break;
			case SNIPES_IRC_US_CTCP_COMMAND:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"Snipes", "ACTION"})).toString();
				break;
			case SNIPES_IRC_US_INVITE:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b, new String [] {"#Snipes","Snipes"})).toString();
				break;
			case SNIPES_IRC_US_JOIN:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b, new String [] {"#Snipes"})).toString();
				break;
			case SNIPES_IRC_US_MODESET:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b, new String [] {"#Snipes","+m"})).toString();
				break;
			case SNIPES_IRC_US_NICK_CHANGE:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"Snipes","Snipes2"})).toString();
				break;
			case SNIPES_IRC_US_NOTICE:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"#Snipes","Test"})).toString();
				break;
			case SNIPES_IRC_US_PART:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"#Snipes","baibai"})).toString();
				break;
			case SNIPES_IRC_US_PRIVMSG:
				p = SnipesBot.sendEvent(e, new SnipesEventParams(b,new String [] {"#Snipes","Hello world!"})).toString();
				break;
			}
			System.setOut(Constants.systemOutDef);
			System.out.println("Event " + e + " returned " + p);
			System.setOut(new PrintStream(new FileOutputStream(new File("/dev/null"))));
		}
		b.exitSnipes(0);
	}
	
	private void botConn(boolean plugins)
	{
		System.out.println("Beginning bot connection test. Using plugins: " + plugins);
		SnipesBot b = new SnipesBot(plugins,true);
		System.setOut(Constants.systemOutDef);
		System.out.println("Bot connection: " + b.isConnected());
		try {
			System.setOut(Constants.getDevNull());
		} catch (FileNotFoundException unhandled) {}
		b.exitSnipes(0);
	}
	
	private void selectiveEventTest()
	{
		System.out.println();
		System.out.println("Snipes Selective Event Testing");
		SnipesEvent ev = askSnipesEvent();
		String[] paramList = checkSupplyArgs();
		SnipesBot b = new SnipesBot(true,true);
		PluginPassResponse p = SnipesBot.sendEvent(ev, new SnipesEventParams(b, paramList));
		System.setOut(Constants.systemOutDef);
		printReport(ev, paramList, b, p);
		try {
			System.setOut(Constants.getDevNull());
		} catch (FileNotFoundException unhandled) {}
		b.exitSnipes(0);
	}
	private void printReport(SnipesEvent ev, String[] paramList, SnipesBot b,
			PluginPassResponse p) {
		System.out.println();
		System.out.println();
		System.out.println("Snipes Event report!");
		System.out.println();
		System.out.println("Snipes Event Requested: " + ev.toString());
		StringBuilder sb = new StringBuilder("Snipes Params Sent: ");
		for (int i=0;i<paramList.length;i++)
		{
			sb.append(paramList[i] + (!(i==paramList.length-1) ? ", " : ""));
		}
		System.out.println(sb.toString());
		System.out.println("Recieved Pass Response: " + p.toString());
		System.out.println("Sent to:");
		listPlugins();
	}
	private String[] checkSupplyArgs()
	{
		ArrayList<String> args = new ArrayList<String>();
		System.out.println();
		boolean wantMoreArgs = askWantArgs(false);
		while (wantMoreArgs)
		{
			String arg = askArgs();
			if (!arg.equalsIgnoreCase(""))
			{
				args.add(arg);
				if (!askWantArgs(true))
				{
					wantMoreArgs = false;
				}
			}
			else {wantMoreArgs = false;}
		}
		return args.toArray(new String[] {});
	}
	private boolean askWantArgs(boolean askDuring)
	{
		System.out.print((askDuring ? "Do you want to supply any more arguments to this event? [Y/N, default N]: " : "Do you want to supply any arguments to this event? [Y/N, default N]: "));
		String s = defScan.nextLine();
		System.out.println();
		return (s.equalsIgnoreCase("Y") ? true : false);
	}
	private String askArgs()
	{
		System.out.print("Please enter the argument [blank means cancel]: ");
		String s = defScan.nextLine();
		System.out.println();
		return s;
	}
	private SnipesEvent askSnipesEvent()
	{
		System.out.println("Please type the full name of the event you wish to test or [EVLIST] for a list of events:");
		String tempResp = defScan.nextLine();
		if (tempResp.equalsIgnoreCase("[EVLIST]"))
		{
			for (int i = 0;i<SnipesEvent.values().length;i++)
			{
				System.out.println(i + " " + SnipesEvent.values()[i]);
			}
			System.out.println();
			return askSnipesEvent();
		}
		else
		{
			try
			{
				return SnipesEvent.valueOf(tempResp);
			} catch (IllegalArgumentException e) {System.out.println();System.out.println("Invalid event.");System.out.println();return askSnipesEvent();}
		}
	}
	private void listPlugins()
	{
		SnipesBot b = new SnipesBot(true,true);
		ArrayList<PluginType> plugins = b.getPlugins();
		for (int i=0;i<plugins.size();i++)
		{
			Class<?> plugClass = plugins.get(i).getClass();
			System.out.println((i+1) + " " + plugClass.getName());
		}
		try {
			System.setOut(Constants.getDevNull());
		} catch (FileNotFoundException e) {}
		b.exitSnipes(0);
	}
	private void getConfigDirectives() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream i = new ObjectInputStream(new FileInputStream(Constants.CONFNAME));
		System.out.println("Reading from " + Constants.CONFNAME);
		Configuration conf = (Configuration)i.readObject();
		System.out.println("Configuration.plugins=(");
		int count = 0;
		for (String s : conf.plugins)
		{
			System.out.println("'" + s + (count == (conf.plugins.size() - 1) ? "'," : ""));
			count++;
		}
		System.out.print("')");
	}
}
