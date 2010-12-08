package org.ossnipes.snipes.bot;

// Imports from the default class library
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// PircBot imports
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

// Snipes imports
import org.ossnipes.snipes.enums.PluginPassResponse;
import org.ossnipes.snipes.enums.SnipesEvent;
import org.ossnipes.snipes.exceptions.SnipesNotConfiguredException;
import org.ossnipes.snipes.exceptions.SnipesPluginException;
import org.ossnipes.snipes.misc.ErrorHandler;
import org.ossnipes.snipes.spf.Plugin;
import org.ossnipes.snipes.spf.PluginManager;
import org.ossnipes.snipes.spf.PluginType;
import org.ossnipes.snipes.spf.SnipesEventParams;
import org.ossnipes.snipes.spf.SuperPlugin;
import org.ossnipes.snipes.utils.Configuration;
import org.ossnipes.snipes.utils.Constants;
import org.ossnipes.snipes.utils.SerializableConfigReader;
import org.ossnipes.snipes.utils.SerializableConfigWriter;


/** Bot class for the open source Snipes project */
public class SnipesBot extends PircBot{
	private static ArrayList<SuperPlugin> sPlugins = new ArrayList<SuperPlugin>();
	private static ArrayList<Plugin> nPlugins = new ArrayList<Plugin>();
	private static ArrayList<PluginType> oPlugins = new ArrayList<PluginType>();
	private PluginManager defPluginManager = new PluginManager();
	private static List<Thread> threadRegister = new ArrayList<Thread>();
	private ArrayList<PluginType> allPlugins = new ArrayList<PluginType>();
	private static Configuration snipesConf;
	private static Queue<String> errorQueue = new LinkedList<String>();
	public SnipesBot(){this(true,false);}
	/** Constructor for SnipesBot. Initiates all needed 
	 * @param usePlugins
	 * @param testing
	 */
	public SnipesBot(boolean usePlugins, boolean testing)
	{
		Thread curr = Thread.currentThread();
		curr.setName("Snipes-Main");
		addThread(curr);
		addThread(new ErrorHandler()).start();
		try
		{
			readConfiguration();
			// If we get anything wrong, this is vital. Quit Snipes.
		} catch (Exception e) {
			errorQueue.add("Error reading Snipes "
		 	+ "configuration. If you have not already, please run Snipes with the"
		 	+ " --makeconf option to configure Snipes.\n For the more technical;"
		 	+ " the exception encountered was a " 
		 	+ e.getClass().getCanonicalName()
		 	+ " \nand its message was \"" 
		 	+ e.getMessage() 
			+ "\"");
		 exitSnipes(0);
		 }
		if (testing)
		{
				try {
					System.setOut(Constants.getDevNull());
				} catch (FileNotFoundException unhandled) {}
				System.out.println();
		}
		if (usePlugins)
		{
			if (!loadOnLoadPlugins())
			{
				System.err.println("Error loading vital Snipes plugins (or on-load ones, as specified in configuration.) Continuing in plugin-free mode (this may not go well.)");
				usePlugins = false;
				exitSnipes(1);
			}
		}
		this.setName("Snipes-Test");
		this.startIdentServer();
		this.setVerbose(true);
		try {
			this.connect("irc.geekshed.net");
			this.sendRawLine("NS IDENTIFY F1sh96");
		} catch (NickAlreadyInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IrcException e) {
			e.printStackTrace();
		}
		this.joinChannel("#snipes");
	}
	/** Load the on load plugins. Adds any problems to the error queue
	 * @return True if the loading did not encounter any serious errors
	 */
	private boolean loadOnLoadPlugins()
	{
			for (String c : snipesConf.plugins)
			{
				PluginType p = null;
				try {
					p = defPluginManager.loadPlugin(c);
				} catch (ClassNotFoundException e) {
					errorQueue.add("The plugin \"" + c + "\" could not be loaded because it could not be found.");
					continue;
				} catch (InstantiationException e) {
					errorQueue.add("The plugin \"" + c + "\" could not be loaded due to a error in instantiation. The error was: " + e.getMessage());
					continue;
				} catch (IllegalAccessException e) {
					errorQueue.add("The plugin \"" + c + "\" could not be loaded because the Snipes security manager forbids it.");
					continue;
				} catch (SnipesPluginException e) {
					errorQueue.add("The plugin \"" + c + "\" could not be loaded because it is not a instance of 'PluginType.' Contact the module's author, saying that the module must extend a dirivitive of PluginType.");
					continue;
				}
				if (p instanceof Plugin)
				{
					nPlugins.add((Plugin)p);
				}
				else if (p instanceof SuperPlugin)
				{
					sPlugins.add((SuperPlugin)p);
				}
				else if (p instanceof PluginType)
				{
					oPlugins.add(p);
				}
				allPlugins.add(p);
			}
			for (PluginType p : allPlugins)
			{
				p.snipesInit();
			}
			return true;
	}
	/** Method exposing event sending functions
	 * @param ev
	 * @param params
	 * @return
	 */
	public static PluginPassResponse sendEvent(SnipesEvent ev, SnipesEventParams params)
	{
		PluginPassResponse pr = PluginPassResponse.PLUGIN_PASSEVENT;
		if (ev.toString().startsWith("SNIPES_IRC"))
		{
			PluginPassResponse prr = runEvent(ev,params);
			if (prr != null)
				pr = prr;
		}
		else if (ev.toString().startsWith("SNIPES_INT"))
		{
			PluginPassResponse prr = runSuperEvent(ev,params);
			if (prr != null)
				pr = prr;
		}
		return pr;
	}
	/** Exit Snipes cleanly, calling snipesFini methods.
	 * @param statusCode The status code (0 means normal, 1 and more means abnormal)
	 */
	public void exitSnipes(int statusCode)
	{
		for (PluginType p : allPlugins)
		{
			p.snipesFini(statusCode);
		}
		System.exit(statusCode);
	}
	/** Run a event send, only to super events.
	 * This method will not show up in JavaDoc.
	 * @param ev The event to send
	 * @param params The params to use
	 * @return If the event should happen
	 */
	private static PluginPassResponse runSuperEvent(SnipesEvent ev, SnipesEventParams params)
	{
		for (SuperPlugin p : sPlugins)
		{
			PluginPassResponse pr = p.event(ev, params);
			if (pr == null || pr.equals(PluginPassResponse.PLUGIN_PASSEVENT))
				continue;
			if (pr.equals(PluginPassResponse.PLUGIN_CANCELEVENT))
			{
				if (p.canCancelEvents())
				{
					return pr;
				}
				else
				{
					continue;
				}
			}
		}
		return PluginPassResponse.PLUGIN_PASSEVENT;
	}
	/** Run a event send.
	 * This method will not show up in JavaDoc.
	 * @param ev The event to send
	 * @param params The params to use
	 * @return If the event should happen
	 */
	private static PluginPassResponse runEvent(SnipesEvent ev, SnipesEventParams params)
	{
		for (SuperPlugin p : sPlugins)
		{
			PluginPassResponse pr = p.event(ev, params);
			if (pr == null || pr.equals(PluginPassResponse.PLUGIN_PASSEVENT))
				continue;
			if (pr.equals(PluginPassResponse.PLUGIN_CANCELEVENT))
			{
				if (p.canCancelEvents())
				{
					return pr;
				}
				else
				{
					continue;
				}
			}
		}
		for (Plugin p : nPlugins)
		{
			PluginPassResponse pr = p.event(ev, params);
			if (pr == null || pr.equals(PluginPassResponse.PLUGIN_PASSEVENT))
				continue;
			if (pr.equals(PluginPassResponse.PLUGIN_CANCELEVENT))
			{
				if (p.canCancelEvents())
				{
					return pr;
				}
				else
				{
					continue;
				}
			}
		}
		for (PluginType p : oPlugins)
		{
			PluginPassResponse pr = p.event(ev, params);
			if (pr == null || pr.equals(PluginPassResponse.PLUGIN_PASSEVENT))
				continue;
			if (pr.equals(PluginPassResponse.PLUGIN_CANCELEVENT))
			{
				if (p.canCancelEvents())
				{
					return pr;
				}
				else
				{
					continue;
				}
				
			}
		}
		return PluginPassResponse.PLUGIN_PASSEVENT;
	}
	/** Prints a message to the console, allowing for SuperPlugins
	 *  to intervien.
	 * @param msg The message to send to the console
	 */
	public static void consolePrint(String msg)
	{
		consolePrint(msg,null);
	}
	/** Prints a message to the console, allowing for SuperPlugins
	 *  to intervien.
	 * @param msg The message to send to the console
	 * @param b The PircBot sending the console print.
	 */
	public static void consolePrint(String msg, PircBot b)
	{
		if (sendEvent(SnipesEvent.SNIPES_INT_CONSOLEOUT, new SnipesEventParams(b, new String[] {msg})) != PluginPassResponse.PLUGIN_CANCELEVENT)
		{
			System.out.println(msg);
		}
	}
	/** Gets a ArrayList of all currently loaded plugins (upcasted to
	 *  'PluginType.')
	 * @return The ArrayList of all plugins
	 */
	public ArrayList<PluginType> getPlugins()
	{
		return allPlugins;
	}
	/** Reads the configuration from disk, and sets it to the current
	 * Snipes configuration. Please note this will not show in JavaDocs
	 * because this method is private.
	 * @throws FileNotFoundException If we cannot access the file
	 * @throws IOException If an unknown error occured
	 * @throws SnipesNotConfiguredException If the Snipes configuration does not exist
	 * @throws ClassNotFoundException If the Snipes configuration Object cannot be found inside the file.
	 */
	private void readConfiguration() throws FileNotFoundException, IOException, SnipesNotConfiguredException, ClassNotFoundException
	{
		SerializableConfigReader scr = new SerializableConfigReader(Constants.CONFNAME);
		snipesConf = scr.readConf();
	}
	@Override
	protected final void finalize() throws Throwable
	{
		writeConfig();
		exitSnipes(0);
		super.finalize();
	}
	/** Writes the configuration to disk (please note that this 
	 * comment will not show up in JavaDocs, as this method is 
	 * private.
	 */
	private void writeConfig()
	{
		try {
			SerializableConfigWriter.writeConfiguration(snipesConf, Constants.CONFNAME);
		} catch (FileNotFoundException e) {errorQueue.add("Access error accessing " + Constants.CONFNAME + " to save Snipes' configuration.");} 
		catch (IOException e) {errorQueue.add("Unknown IO Exception accessing " + Constants.CONFNAME + " to save Snipes' configuration.");}
	}
	/** Write the specified configuration to disk. This method also
	 * sets that configuration as the current configuration that is
	 * loaded for convinence's sake.
	 * @param conf The configuration to write
	 */
	public static void writeConfig(Configuration conf)
	{
		try
		{
			snipesConf = conf;
			SerializableConfigWriter.writeConfiguration(conf, Constants.CONFNAME);
		} catch (FileNotFoundException e) {errorQueue.add("Access error accessing " + Constants.CONFNAME + " to save Snipes' configuration.");} 
		catch (IOException e) {errorQueue.add("Unknown IO Exception accessing " + Constants.CONFNAME + " to save Snipes' configuration.");}
	}
	/** Get the currently loaded configuration. If you wish to modify
	 *  or 'commit' this, use <code>
	 *  SnipesBot.writeConfig(Configuration conf)
	 *  </code> or <code>SnipesBot.setConfiguration(Configuration conf)
	 *  </code>. Both of these methods set the current configuration
	 *  to the specified one, the only difference is that the former
	 *  writes the configuration to disk (therefore assuring that
	 *  it gets there before Snipes shuts down in any circumstance)
	 *  and sets it, and the other only sets the configuration,
	 *  with the chance of it not being saved in the case of 
	 *  abnormal termination.
	 *  @return The currently loaded configuration
	 */
	public static Configuration getConfiguration() {
		
		return snipesConf;
	}
	/** Create a thread object of the specified Runnable, adding it 
	 * to the Snipes thread management register. Please note that 
	 * this method <b>DOES NOT</b> star the thread. You must use it
	 * like <code>SnipesBot.addThread(new ExampleRunnable()).start();
	 * </code> to start the thread. You could also store it in a Object
	 * like this: <code>Thread t = SnipesBot.addThread(new 
	 * ExampleRunnable); 
	 * //More code to do stuff before actually starting the
	 * thread
	 * t.start();
	 * </code>
	 * @param t The runnable to add and create a thread from.
	 * @return The Thread Object of the newly created Thread.
	 */
	public static Thread addThread(Runnable t)
	{
		if (t == null)
		{
			throw new NullPointerException("addThread(Runnable t) cannot take a null value.");
		}
		Thread newThread = new Thread(t);
		threadRegister.add(newThread);
		return newThread;
	}
	/** Add a pre-existing Thread to the Thread register.
	 * Only to be used by the main Thread (Snipes-Main,) because
	 * it is already created by the JVM.
	 * This will not show up in JavaDoc.
	 * @param t The Thread to add.
	 * @return t, for convinence
	 */
	private Thread addThread(Thread t)
	{
		if (t == null)
		{
			throw new NullPointerException("addThread(Thread t) cannot take a null value.");
		}
		threadRegister.add(t);
		return t;
	}
	/** Gets the current Snipes error Queue object. If you intend
	 * on modifying it, you must 'commit' your changes using 
	 * {@link setErrorQueue} so they are seen by other plugins/
	 * objects.
	 * @return
	 */
	public static Queue<String> getErrorQueue()
	{
		return errorQueue;
	}
	/** Set the value of the Snipes error queue. The intended
	 * purpose of this method is to remove or add errors to the
	 * current error Queue object (retrieved using 
	 * {@link getErrorQueue}.)
	 * @param value
	 * @return True if the value was set, false if it wasn't (value
	 * was null.)
	 */
	public static boolean setErrorQueue(Queue<String> value)
	{
		if (value != null)
		{
			errorQueue = value;
			return true;
		}
		return false;
	}
	/** Adds a item to the error message queue. These messages get
	 * printed out about every 20 seconds. This is so that they are
	 * not missed.
	 * @param error The error String to add
	 * @return True if the erorr was added. The error will be added as long as it doesn't equals null or empty.
	 */
	public static boolean addToErrorQueue(String error)
	{
		if (error != null && !error.equalsIgnoreCase(""))
		{
			errorQueue.add(error);
			return true;
		}
		return false;
	}
	/** Gets the Thread List Object
	 * @return The List Object, containing all currently running Threads
	 */
	public static List<Thread> getThreadCollection()
	{
		return threadRegister;
	}
}