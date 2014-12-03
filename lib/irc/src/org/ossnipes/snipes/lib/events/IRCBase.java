/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.ossnipes.snipes.lib.events;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.SocketFactory;

import org.ossnipes.snipes.lib.irc.BotConstants;
import org.ossnipes.snipes.lib.irc.IRCSocketManager;
import org.ossnipes.snipes.lib.irc.SnipesException;
import org.ossnipes.snipes.lib.irc.SnipesSSLSocketFactory;

/**
 * Main class for the Snipes IRC framework. The Snipes IRC framework is a
 * framework that is meant to replace the current implementation of IRC
 * communication in The Open Source Snipes Project (PircBot) so that the project
 * can "stand on it's own two feet" and be able to change from license to
 * license as the author sees fit. There was also a need for SSL support, so
 * that is built into the framework with the {@link SnipesSSLSocketFactory}
 * class.
 * 
 * @author Jack McCracken (<a
 *         href="http://ossnipes.org/">http://ossnipes.org</a>)
 * 
 * @since Snipes 0.6
 * 
 */
public abstract class IRCBase extends IRCSocketManager implements IRCEventListener
{
    // Default constructor
    /**
     * Creates a IRCBase. This constructor takes no parameters so that it is very easy to create a IRC bot 
     * using this framework.
     */
    public IRCBase()
    {
        if (CHANNEL_TRACKING)
            _channels = new ArrayList<Channel>();
        _eventcoll = new EventHandlerCollection();
        _eventcoll.addEventListener(this);

        _backupNicks = new LinkedBlockingQueue<String>();
    }

    /**
     * Registers this class for listening of events.
     * This does not apply to us, as we are a IRCBase,
     * given extra power by the event sending mechanism
     * to "enforce the rules" of who gets what events sent
     * to them.
     * 
     * This method is never called, and is thus empty.
     */
    public final Event[] getRegisteredEvents()
    {
        return new Event[] {};
    }

    /**
     * Adds a event listener to an IRCBase object.
     * @param h The even listener to add. The handleEvent method of this object will be called for every 
     * registered event.
     * @return The given event listener, for the convenience of the user.
     */
    public IRCEventListener addEventListener(IRCEventListener h)
    {
        return _eventcoll.addEventListener(h);
    }
	
    /**
     * Reverses the process preformed by the addEventListener function, removing an event listener.
     * @param h The event listener to be removed.
     */
    public void removeEventListener(IRCEventListener h)
    {
        _eventcoll.removeEventListener(h);
    }
	
    /**
     * Connects to the IRC server.
     * 
     * @param server
     *            The server IP/host to connect to. If null, a
     *            {@link IllegalArgumentException} is thrown.
     * @param port
     *            The port to connect on. If it is not between 1 and 65535,
     *            throws {@link IllegalArgumentException}.
     * @param factory
     *            The SocketFactory to use. If null, a SnipesSocketFactory is
     *            used.
     * @throws IOException
     *             If there is a unknown IO error while connecting to the
     *             server.
     * @throws UnknownHostException
     *             If the host specified by "server" doesn't exist.
     */
    public IRCSocketManager connect(String server, int port, String passwd, SocketFactory factory)
	throws IOException, UnknownHostException
    {
        return connect(new EventInputHandler(this), server, port, passwd, factory);
    }
	
    /**
     * Connects to an IRC server.
     * 
     * @param server
     *            The server IP/host to connect to. If null, a
     *            {@link IllegalArgumentException} is thrown.
     * @param port
     *            The port to connect on. If it is not between 1 and 65535,
     *            throws {@link IllegalArgumentException}.
     * @param factory
     *            The SocketFactory to use. If null, a SnipesSocketFactory is
     *            used.
     * @throws IOException
     *             If there is a unknown IO error while connecting to the
     *             server.
     * @throws UnknownHostException
     *             If the host specified by "server" doesn't exist.
     */
    public IRCSocketManager connect(String server, int port, SocketFactory factory)
	throws IOException, UnknownHostException
    {
        return connect(server, port, null, factory);
    }
	
    /**
     * Connects to the IRC server using a SnipesSocketFactory.
     * 
     * @param server
     *            The server IP/host to connect to. If null, a
     *            {@link IllegalArgumentException} is thrown.
     * @param port
     *            The port to connect on. If it is not between 1 and 65535,
     *            throws {@link IllegalArgumentException}.
     * @throws IOException
     *             If there is a unknown IO error while connecting to the
     *             server.
     * @throws UnknownHostException
     *             If the host specified by "server" doesn't exist.
     * @see #connect(String, int, SocketFactory)
     */
    public IRCSocketManager connect(String server, int port) throws IOException,
	UnknownHostException
    {
        // Defaults to a SnipesSocketFactory if third parameter is null.
        return connect(server, port, null);
    }
	
    /**
     * Connects to the IRC server using a SnipesSocketFactory and the default
     * port (see below.)
     * 
     * @param server
     *            The server IP/host to connect to. If null, a
     *            {@link IllegalArgumentException} is thrown.
     * @throws IOException
     *             If there is a unknown IO error while connecting to the
     *             server.
     * @throws UnknownHostException
     *             If the host specified by "server" doesn't exist.
     * @see #connect(String, int, SocketFactory)
     * @see BotConstants#IRC_DEFAULT_PORT for the port used by default by
     *      Snipes. I won't specify it here, as it may change.
     */
    public IRCSocketManager connect(String server) throws IOException, UnknownHostException
    {
        // Connect to the server with the default IRC port and 
        // SocketFactory.
        return connect(server, IRC_DEFAULT_PORT, null);
    }
	
    /**
     * Gets the list of objects representing channels.
     * 
     * @return A list of objects representing channels.
     */
    public Channel[] getJoinedChannels()
    {
        if (!CHANNEL_TRACKING)
        {
            throw new UnsupportedOperationException("The API was compiled with CHANNEL_TRACKING set to false. Channel tracking features are disabled.");
        }
        return _channels.toArray(new Channel[_channels.size()]);
    }
	
    /**
     * Gets a channel for the name given.
     * 
     * @param name The name of the channel to return.
     * @return The channel you requested, or null if the bot is not current in that channel.
     */
    public Channel getChannelForName(String name)
    {
        Channel result = null;
		
        for (Channel c : _channels)
        {
            // IRC's channel naming is case-insensitive.
            if (c.getName().equalsIgnoreCase(name))
            {
                return c;
            }
        }
		
        return result;
    }
	
    // Everyone ends up calling this, so we can implement generic functionality in here.
    /**
     * {@inheritDoc}
     * In IRCBase, this function is overwritten so that event handlers handling US events cannot send things to the IRC.
     */
    public void sendRaw(String line)
    {
        EventArgs args = getCurrentEvent();
        if (args == null || args.getEvent() == null || args.getEvent().getType() != Event.EventType.US)
        {
            super.sendRaw(line);
        }
        else
        {
            // We REALLY don't know how to deal with this...
            throw new IllegalStateException("Unable to send command to server from event handler because it was an US handler.");
        }
    }
	
    public EventArgs getCurrentEvent()
    {
        return _eventcoll.getCurrentEventTl().get();
    }
	
    /** Used to handle a event sent by {@link #sendEvent(Event, EventArgs)}.
     * @param ev The event that was sent.
     * @param args The arguments for the event.
     */
    public abstract void handleEvent(Event ev, EventArgs args);

    /** This method is called when a event in the {@link BotConstants#INTERNAL_EVENTS} array
     * is triggered.
     * @param ev The event that was sent.
     * @param args The arguments for the event.
     */
    public final void handleInternalEvent(Event ev, EventArgs args)
    {
        if (ev == Event.IRC_JOIN)
        {
            if (CHANNEL_TRACKING)
            {
                // We're only interested if the nick is us.
                if (args.getParamAsString("nick").equalsIgnoreCase(getNick()))
                {
                    _channels.add((Channel)addEventListener(new Channel(args.getParamAsString("channel"))));
                }
            }
        }
        else if (ev == Event.IRC_PART)
        {
            if (CHANNEL_TRACKING)
            {
                if (args.getParamAsString("nick").equalsIgnoreCase(getNick()))
                {
                    String chanName = args.getParamAsString("channel");
					
                    // Returns false if the Object did not exist in the first place.
                    if (!_channels.remove(getChannelForName(chanName)))
                    {
                        // We didn't have it in our lists? But we were parting it!
                        throw new SnipesException("Channel was not in the lists, yet we parted it.");
                    }
                }
            }
        }
        else if (ev == Event.IRC_NICKINUSE && (Boolean)args.getParam("fatal"))
        {
            String nextNick = _backupNicks.poll();
			
            if (nextNick == null)
            {
                System.err.println("Error. All nicknames provided already in use. Please make sure no other instances are running and restart this application.");
                System.exit(3);
            }
            else
            {
                // We attempt to change our nick:
                this.setNick(nextNick);
            }
        }
        else
        {
            System.err.println("Internal event handler: Unknown internal event " + ev + ".");
        }
    }
	
    /** Sends a event to the bot, checking if it is a internal one,
     * and if it is, it calls the appropriate method. Really just 
     * a alias for {@link BotUtils#sendEvent(Event, EventArgs, IRCBase)}
     * with <code>this</code> as the third argument :).
     * @param ev The event to send.
     * @param args The arguments to use.
     */
    public void sendEvent(Event ev, EventArgs args)
    {
        BotUtils.sendEvent(args, this.getEventHandlerColl());
    }
	
    /**
     * Sets the current level of threading.
     * @param level A object representing the level of threading to set.
     */
    public void setThreadLevel(ThreadLevel level)
    {
        _eventcoll.setThreadLevel(level);
    }
	
    /**
     * Queries the current thread level.
     * @return
     */
    public ThreadLevel getThreadLevel()
    {
        return _eventcoll.getThreadLevel();
    }
	
    /** Gets the list of {@link JavaEventHandlerManager}s that are assigned {@link IRCEventListener}s
     * that have subscribed to receive events from the bot.
     * @return The list of {@link JavaEventHandlerManager}s.
     */
    List<JavaEventHandlerManager> getListeners()
    {
        return _eventcoll.getListeners();
    }
	
    /**
     * Gets the current collection of event handlers. This is an internal object, so this function can only
     * be called from within this package. 
     * 
     * @return The event handler collection.
     */
    EventHandlerCollection getEventHandlerColl()
    {
        return _eventcoll;
    }

    /**
     * Sets the bot's nickname and backup nicks. This list will be exhausted before
     * disconnecting from the server.
     *
     * @param nicks The nicks the bot should attempt to use
     * @return This IRCBase object
     */
    public IRCBase setNicks(String[] nicks)
    {
        if (nicks.length == 0)
        {
            return this;
        }
		
        this.setNick(nicks[0]);
        if (nicks.length > 1)
        {
            for (int i = 1; i < nicks.length; i++)
            {
                _backupNicks.add(nicks[i]);
            }
        }
		
        return this;
    }
	
    private EventHandlerCollection _eventcoll;
    private List<Channel> _channels;
    private Queue<String> _backupNicks;
}
