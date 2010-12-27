package org.ossnipes.snipes.bot;

/*
 * JOBS:
 * - Add in security, methods such as isBotAdministrator(String channel, String nick)
 * - Work on changing hardcoded values to configuration directives
 * - Work on transition from accessing actual PircBot methods, to using
 * SPF methods in plugins.
 */

// Imports from the default class library

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

// PircBot imports
import org.jibble.pircbot.DccManager;
import org.jibble.pircbot.InputThread;
import org.jibble.pircbot.PircBot;

// Snipes imports
import org.ossnipes.snipes.enums.PluginPassResponse;
import org.ossnipes.snipes.enums.SnipesEvent;
import org.ossnipes.snipes.exceptions.NoSnipesInstanceException;
import org.ossnipes.snipes.exceptions.SnipesPluginException;
import org.ossnipes.snipes.exceptions.SnipesSecurityException;
import org.ossnipes.snipes.threads.Cleanup;
import org.ossnipes.snipes.threads.TimerThread;
import org.ossnipes.snipes.spf.Plugin;
import org.ossnipes.snipes.spf.PluginManager;
import org.ossnipes.snipes.spf.PluginType;
import org.ossnipes.snipes.spf.SnipesEventParams;
import org.ossnipes.snipes.spf.SuperPlugin;
import org.ossnipes.snipes.utils.Configuration;
import org.ossnipes.snipes.utils.Constants;
import org.ossnipes.snipes.utils.Expensives;

/**
 * Bot class for the open source Snipes project
 */
public class SnipesBot extends PircBot {

    private static SnipesBot inst;
    private static ArrayList<SuperPlugin> sPlugins = new ArrayList<SuperPlugin>();
    private static ArrayList<Plugin> nPlugins = new ArrayList<Plugin>();
    private static ArrayList<PluginType> oPlugins = new ArrayList<PluginType>();
    private PluginManager defPluginManager = new PluginManager();
    private static List<Thread> threadRegister = new ArrayList<Thread>();
    private ArrayList<PluginType> allPlugins = new ArrayList<PluginType>();
    private static Queue<String> errorQueue = new LinkedList<String>();
    Logger log = Logger.getLogger(this.getClass().getName());
    public static boolean DEBUG = false;


    public org.jibble.pircbot.Queue getOutCue() {
        return _outQueue;
    }

    public InputThread getInCue() {
        return _inputThread;
    }

    public void setOutQueue(org.jibble.pircbot.Queue val) {
        if (val != null)
            _outQueue = val;
    }

    public DccManager getManager() {
        return _dccManager;
    }

    public SnipesBot() {
        this(true, false);
    }

    /**
     * Constructor for SnipesBot. Initiates all needed variables
     *
     * @param usePlugins If we should be using plugins
     * @param quiet      No output (same as executing with a "> /dev/null")
     */
    public SnipesBot(boolean usePlugins, boolean quiet) {
        if (inst != null)
            throw new SnipesSecurityException("Only one instance of Snipes may be created.");

        if (Boolean.parseBoolean(Configuration.lookUp("debug")))
        {
            DEBUG = true;
        }
        debug("Snipes bot: Version " + Constants.VERSION_STRING + " starting...");
        debug("Options: usePlugins: " + usePlugins + " quiet: " + quiet);

        debug("Adding runtime hooks...");
        Runtime.getRuntime().addShutdownHook(new Thread(new Cleanup()));
        Thread curr = Thread.currentThread();
        curr.setName("Snipes-Main");
        addThread(curr);
        debug("Starting error thread...");
        startErrorThread();
        if (quiet) {
            try {
                System.setOut(Constants.getDevNull());
            } catch (FileNotFoundException unhandled) {
            }
            System.out.println();
        }
        debug("Loading Snipes plugins...");
        if (usePlugins) {
            if (!loadOnLoadPlugins()) {
                System.err.println("Error loading vital Snipes plugins (or on-load ones, as specified in configuration.) Continuing in plugin-free mode (this may not go well.)");
                usePlugins = false;
                exitSnipes(1);
            }
        }
        debug("Parsing opening configuration options (nick, channels, etc)...");
        this.setName(Configuration.lookUp("nick", "Snipes-unconf"));
        if (Boolean.parseBoolean(Configuration.lookUp("ident", "FALSE"))) {
            this.startIdentServer();
        }
        this.setVerbose(Boolean.parseBoolean(Configuration.lookUp("verbose", "FALSE")));
        try {
            this.connect(Configuration.lookUp("server", "irc.geekshed.net"));
            System.out.println("Connected to server at " + Configuration.lookUp("server", "irc.geekshed.net"));
        } catch (Exception e) {
            System.err.println("Unable to connect to server at " + Configuration.lookUp("server", "irc.geekshed.net (default server, configure me!)") + ": " + e.getMessage());
            if (Boolean.parseBoolean(Configuration.lookUp("verbose", "FALSE"))) {
                System.err.println("Regarding the above, the error type was a: \"" + e.getClass().getCanonicalName() + "\" and it's message was: \"" + e.getMessage() + "\"");
            }
            this.exitSnipes(1);
        }
        String password;
        if ((password = Configuration.lookUp("nspass")) != null) {
            this.sendRawLine("NS IDENTIFY " + password);
        }
        String[] rawLines = Configuration.getSplitProperty("rawlines", "", ",");
        if (rawLines.length != 0 && rawLines[0].equalsIgnoreCase("")) {
            for (String s : rawLines) {
                if (!s.equals("")) {
                    this.sendRawLine(s);
                }
            }
        }
        String[] channels = Configuration.getSplitProperty(
                "channels",
                "#Snipes-Testing",
                ",",
                true);
        for (String channel : channels) {
            if (channel != null && !channel.equals("")) {
                System.out.println("Joining channel " + channel);
                this.joinChannel(channel);
            }
        }
        debug("Done loading!");
    }

    /**
     * Load the on load plugins. Adds any problems to the error queue
     *
     * @return True if the loading did not encounter any serious errors
     */
    private boolean loadOnLoadPlugins() {
        inst = this;
        ArrayList<String> plugins = new ArrayList();
        String[] s = Configuration.getSplitProperty(
                "plugins",
                "",
                ",",
                true);
        plugins.addAll(Arrays.asList(s));
        plugins.addAll(0, Arrays.asList(Constants.CORE_PLUGINS));
        if (!(s instanceof String[])) {
            System.err.println("This *really* shouldn't happen! Something we thought was a String array was actually another type! Configuration.getSplitProperty() tricked us!");
            System.exit(1);
        } else {
            s = plugins.toArray(new String[plugins.size()]);
        }
        for (String c : s) {
            PluginType p;
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
            addToLists(p);
        }
        callConstructors();
        return true;
    }

    /**
     * Method exposing event sending functions
     *
     * @param ev     The event to send
     * @param params The params to use
     * @return The response, events may honour that or not.
     */
    public static PluginPassResponse sendEvent(SnipesEvent ev, SnipesEventParams params) {
        PluginPassResponse pr = PluginPassResponse.PLUGIN_PASSEVENT;
        if (ev.toString().startsWith("SNIPES_IRC")) {
            PluginPassResponse prr = runEvent(ev, params);
            if (prr != null) {
                pr = prr;
            }
        } else if (ev.toString().startsWith("SNIPES_INT")) {
            PluginPassResponse prr = runSuperEvent(ev, params);
            if (prr != null) {
                pr = prr;
            }
        }
        return pr;
    }

    /**
     * Exit Snipes cleanly, calling snipesFini methods.
     *
     * @param statusCode The status code (0 means normal, 1 and more means abnormal)
     */
    public final void exitSnipes(int statusCode) {
        for (PluginType p : allPlugins) {
            p.snipesFini(statusCode);
        }
        if (this.isConnected()) {
            this.disconnect();
        }
        // Let the plugins finish
        try {
            Thread.sleep(500);
        } catch (InterruptedException owell) {
        }
        System.exit(statusCode);
    }

    /**
     * Run a event send, only to super events.
     * This method will not show up in JavaDoc.
     *
     * @param ev     The event to send
     * @param params The params to use
     * @return If the event should happen
     */
    private static PluginPassResponse runSuperEvent(SnipesEvent ev, SnipesEventParams params) {
        for (SuperPlugin p : sPlugins) {
            PluginPassResponse pr = p.event(ev, params);
            if (pr == null || pr.equals(PluginPassResponse.PLUGIN_PASSEVENT)) {
                continue;
            }
            if (pr.equals(PluginPassResponse.PLUGIN_CANCELEVENT)) {
                if (p.canCancelEvents()) {
                    return pr;
                } else {
                    continue;
                }
            }
        }
        return PluginPassResponse.PLUGIN_PASSEVENT;
    }

    /**
     * Run a event send.
     * This method will not show up in JavaDoc.
     *
     * @param ev     The event to send
     * @param params The params to use
     * @return If the event should continue
     */
    private static PluginPassResponse runEvent(SnipesEvent ev, SnipesEventParams params) {
        for (SuperPlugin p : sPlugins) {
            PluginPassResponse pr = p.event(ev, params);
            if (pr == null || pr.equals(PluginPassResponse.PLUGIN_PASSEVENT)) {
                continue;
            }
            if (pr.equals(PluginPassResponse.PLUGIN_CANCELEVENT)) {
                if (p.canCancelEvents()) {
                    return pr;
                } else {
                    continue;
                }
            }
        }
        for (Plugin p : nPlugins) {
            PluginPassResponse pr = p.event(ev, params);
            if (pr == null || pr.equals(PluginPassResponse.PLUGIN_PASSEVENT)) {
                continue;
            }
            if (pr.equals(PluginPassResponse.PLUGIN_CANCELEVENT)) {
                if (p.canCancelEvents()) {
                    return pr;
                } else {
                    continue;
                }
            }
        }
        for (PluginType p : oPlugins) {
            PluginPassResponse pr = p.event(ev, params);
            if (pr == null || pr.equals(PluginPassResponse.PLUGIN_PASSEVENT)) {
                continue;
            }
            if (pr.equals(PluginPassResponse.PLUGIN_CANCELEVENT)) {
                if (p.canCancelEvents()) {
                    return pr;
                } else {
                    continue;
                }

            }
        }
        return PluginPassResponse.PLUGIN_PASSEVENT;
    }

    /**
     * Prints a message to the console, allowing for SuperPlugins
     * to intervien.
     *
     * @param msg The message to send to the console
     */
    public static void consolePrint(String msg) {
        consolePrint(msg, null);
    }

    /**
     * Prints a message to the console, allowing for SuperPlugins
     * to intervien.
     *
     * @param msg The message to send to the console
     * @param b   The PircBot sending the console print.
     */
    public static void consolePrint(String msg, PircBot b) {
        if (sendEvent(SnipesEvent.SNIPES_INT_CONSOLEOUT, new SnipesEventParams(b, new String[]{msg})) != PluginPassResponse.PLUGIN_CANCELEVENT) {
            System.out.println(msg);
        }
    }

    /**
     * Gets a ArrayList of all currently loaded plugins (upcasted to
     * 'PluginType.')
     *
     * @return The ArrayList of all plugins
     */
    public ArrayList<PluginType> getPlugins() {
        return allPlugins;
    }

    /**
     * Create a thread object of the specified Runnable, adding it
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
     *
     * @param t The runnable to add and create a thread from.
     * @return The Thread Object of the newly created Thread.
     */
    public static Thread addThread(Runnable t) {
        if (t == null) {
            throw new NullPointerException("addThread(Runnable t) cannot take a null value.");
        }
        Thread newThread = new Thread(t);
        if (threadRegister.contains(newThread)) {
            return newThread;
        }
        threadRegister.add(newThread);
        return newThread;
    }

    /**
     * Add a pre-existing Thread to the Thread register.
     * Only to be used by the main Thread (Snipes-Main,) because
     * it is already created by the JVM.
     * This will not show up in JavaDoc.
     *
     * @param t The Thread to add.
     * @return t, for convinence
     */
    private Thread addThread(Thread t) {
        if (t == null) {
            throw new NullPointerException("addThread(Thread t) cannot take a null value.");
        }
        threadRegister.add(t);
        return t;
    }

    /**
     * Gets the current Snipes error Queue object. If you intend
     * on modifying it, you must 'commit' your changes using
     * {#setErrorQueue} so they are seen by other plugins/
     * objects.
     *
     * @return The error queue
     */
    public static Queue<String> getErrorQueue() {
        return errorQueue;
    }

    /**
     * Set the value of the Snipes error queue. The intended
     * purpose of this method is to remove or add errors to the
     * current error Queue object (retrieved using
     * {#getErrorQueue}.)
     *
     * @param value
     * @return True if the value was set, false if it wasn't (value
     *         was null.)
     */
    public static boolean setErrorQueue(Queue<String> value) {
        if (value != null) {
            errorQueue = value;
            return true;
        }
        return false;
    }

    /**
     * Adds a item to the error message queue. These messages get
     * printed out about every 20 seconds. This is so that they are
     * not missed.
     *
     * @param error The error String to add
     * @return True if the erorr was added. The error will be added as long as it doesn't equals null or empty.
     */
    public static boolean addToErrorQueue(String error) {
        if (error != null && !error.equalsIgnoreCase("")) {
            errorQueue.add(error);
            return true;
        }
        return false;
    }

    /**
     * Gets the Thread List Object
     *
     * @return The List Object, containing all currently running Threads
     */
    public static List<Thread> getThreadCollection() {
        return threadRegister;
    }

    /**
     * Gets the currently running Snipes instance.
     * This method is expensive, and should not be called often (or at all by plugins).
     *
     * @return The currently running Snipes instance
     * @throws NoSnipesInstanceException If the Snipes instance has not been created yet.
     * @throws SnipesPluginException     If a plugin attempts to call this method.
     */
    public static SnipesBot getInst() throws NoSnipesInstanceException {
        if (inst == null) {
            throw new NoSnipesInstanceException("Trying to get instance before Snipes starts.");
        }
        if (Expensives.getCallingMethodInfo().equals("handleEvent")) {
            throw new SnipesSecurityException("Plugins shouldn't be touching me!");
        }
        return inst;
    }

    /**
     * Start the Snipes error handling Thread.
     * This will not appear in JavaDocs, as it is private.
     */
    private void startErrorThread() {
        addThread(new TimerThread()).start();
    }

    /**
     * Checks if the given hostname belongs to a bot administrator or higher.
     *
     * @param hostname
     * @return
     */
    public boolean isBotAdministrator(String hostname) {
        List<String> ranks = Arrays.asList(Configuration.getSplitProperty("admins", "", ","));
        ranks.addAll(Arrays.asList(Configuration.getSplitProperty("owners", "", ",")));
        if (ranks.size() == 1 && ranks.get(0).equalsIgnoreCase("")) {
            return false;
        }
        for (String s : ranks) {
            if (s.equalsIgnoreCase(hostname)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBotOwner(String hostname) {
        List<String> ranks = Arrays.asList(Configuration.getSplitProperty("owners", "", ","));
        if (ranks.size() == 1 && ranks.get(0).equalsIgnoreCase("")) {
            return false;
        }
        for (String s : ranks) {
            if (s.equalsIgnoreCase(hostname)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBotModerator(String hostname) {
        List<String> ranks = Arrays.asList(Configuration.getSplitProperty("admins", "", ","));
        ranks.addAll(Arrays.asList(Configuration.getSplitProperty("owners", "", ",")));
        ranks.addAll(Arrays.asList(Configuration.getSplitProperty("mods", "", ",")));
        if (ranks.size() == 1 && ranks.get(0).equalsIgnoreCase("")) {
            return false;
        }
        for (String s : ranks) {
            if (s.equalsIgnoreCase(hostname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Notifies Snipes that a plugin has been added and needs to be added to the lists.
     * There is no guarantee that this plugin will be accepted and added, but if
     * it is, it's constructor will be called. A exception will
     * be thrown if it cannot or will not be added.
     *
     * @param p The PluginType to add.
     * @throws SnipesPluginException If Snipes cannot or refuses to load the specified plugin.
     */
    public boolean notifyExternalPluginLoad(PluginType p) throws SnipesPluginException {
        if (p == null) {
            throw new SnipesPluginException("p cannot be null.");
        }
        return addToLists(p);
    }

    /**
     * Adds the specified PluginType to the lists, making sure it is not null.
     *
     * @param p The PluginType to add.
     */
    private boolean addToLists(PluginType p) {
        if (p instanceof Plugin) {
            nPlugins.add((Plugin) p);
        } else if (p instanceof SuperPlugin) {
            sPlugins.add((SuperPlugin) p);
        } else if (p != null) {
            oPlugins.add(p);
        }
        allPlugins.add(p);
        return true;
    }

    /**
     * Calls the constructors of all the currently loaded plugins
     */
    private void callConstructors() {
        for (PluginType p : allPlugins) {
            p.snipesInit();
        }
    }
    private void debug(String s)
    {
        debug(s,Level.INFO);
    }
    private void debug(String s, Level l)
    {
        if (DEBUG)
        {
            log.log(l, s);
        }
    }
}
