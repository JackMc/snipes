package org.ossnipes.snipes.spf;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

// PircBot imports
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.DccChat;
import org.jibble.pircbot.DccFileTransfer;

// SnipesBot imports
import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.enums.PluginConstructRet;
import org.ossnipes.snipes.enums.PluginDestructRet;
import org.ossnipes.snipes.enums.PluginPassResponse;
import org.ossnipes.snipes.enums.SnipesEvent;
import org.ossnipes.snipes.exceptions.NoSnipesInstanceException;
import org.ossnipes.snipes.interfaces.SnipesLogger;
import org.ossnipes.snipes.utils.Log;

/**
 * Base class used to represent a Snipes plugin type (a base
 * class used to define the rough outline of a plugin, and what rights it should
 * have within the bot All <b>plugin types</b> should inherit from bot type.
 * If you create a Snipes plugin, it shouldn't inherit from PluginType class,
 * rather something such as Snipes Super Plugin ({@link SuperPlugin},)
 * or Snipes Plugin ({@link Plugin}.)
 *
 * @author Jack McCracken (Auv5)
 */
public abstract class PluginType implements SnipesLogger {

    private SnipesBot inst;

    /**
     * Log a line to the default Snipes Logger.
     * bot method assumes level {@link Level}.CONFIG.
     *
     * @param line The line to log.
     */
    public final void log(String line) {
        log(line, Level.CONFIG);
    }

    /**
     * Log a line to the default Snipes Logger.
     *
     * @param line  The line to log.
     * @param level The level to log at.
     */
    public final void log(String line, Level level) {
        Log.log(line, level);
    }

    /**
     * Called when a Snipes event (internal or external) that you have the rights to handle happens.
     *
     * @param event  The event, from the SnipesEvent enum
     * @param params The paramaters, works just like a ArrayList.
     * @return If you are able to cancel events (determined by your plugin type's canCancelEvents() method,) you may return PLUGIN_CANCELEVENT, if not, you may only return PLUGIN_PASSEVENT or null (does the same)
     */
    protected abstract PluginPassResponse handleEvent(SnipesEvent event, SnipesEventParams params);

    public PluginPassResponse event(SnipesEvent event, SnipesEventParams params) {
        params.setBot(null);
        return handleEvent(event, params);
    }

    /**
     * Signifies the beginning of a Plugin's lifecycle.
     *
     * @return If the plugin initialized successfully. Null assumes yes.
     */
    public abstract PluginConstructRet snipesInit();

    /**
     * Signifies the end of a Plugin's lifecycle.
     *
     * @return If the plugin ended normally. Null assumes yes.
     */
    public abstract PluginDestructRet snipesFini(int status);

    /**
     * Determines if a bot Plugin can hook into events beginning with
     * "SNIPES_INT".
     *
     * @return If the Plugin can hook into internal events.
     */
    public abstract boolean canHookInternalEvents();

    /**
     * Determines if the Plugin can cancel events for plugins below
     * it.
     *
     * @return If the Plugin can cancel events.
     */
    public abstract boolean canCancelEvents();

    /**
     * Gets the name of the Plugin.
     *
     * @return The name of the Plugin.
     */
    public abstract String getName();

    /**
     * Joins a channel.
     *
     * @param channel The name of the channel to join (eg "#cs").
     */
    protected final void joinChannel(String channel) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("JOIN " + channel);
        } catch (Exception e) {
        }
    }

    /**
     * Joins a channel with a key.
     *
     * @param channel The name of the channel to join (eg "#cs").
     * @param key     The key that will be used to join the channel.
     */
    protected final void joinChannel(String channel, String key) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).joinChannel(channel + " " + key);
        } catch (Exception e) {
        }
    }

    /**
     * Parts a channel.
     *
     * @param channel The name of the channel to leave.
     */
    protected final void partChannel(String channel) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("PART " + channel);

        } catch (Exception e) {
        }
    }

    /**
     * Parts a channel, giving a reason.
     *
     * @param channel The name of the channel to leave.
     * @param reason  The reason for parting the channel.
     */
    protected final void partChannel(String channel, String reason) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("PART " + channel + " :" + reason);
        } catch (Exception e) {
        }
    }

    /**
     * Quits from the IRC server.
     * Providing we are actually connected to an IRC server, the
     * onDisconnect() method will be called as soon as the IRC server
     * disconnects us.
     */
    protected final void quitServer() {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).quitServer("");
        } catch (Exception e) {
        }
    }

    /**
     * Quits from the IRC server with a reason.
     * Providing we are actually connected to an IRC server, the
     * onDisconnect() method will be called as soon as the IRC server
     * disconnects us.
     *
     * @param reason The reason for quitting the server.
     */
    protected final void quitServer(String reason) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("QUIT :" + reason);
            (inst != null ? inst : (inst = SnipesBot.getInst())).getInCue().quitServer();
        } catch (Exception ex) {
        }
    }

    /**
     * Sends a raw line to the IRC server as soon as possible, bypassing the
     * outgoing message queue.
     *
     * @param line The raw line to send to the IRC server.
     */
    protected final synchronized void sendRawLine(String line) {
        try {
            if ((inst != null ? inst : (inst = SnipesBot.getInst())).isConnected()) {
                (inst != null ? inst : (inst = SnipesBot.getInst())).getInCue().sendRawLine(line);
            }
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Sends a raw line through the outgoing message queue.
     *
     * @param line The raw line to send to the IRC server.
     */
    protected final synchronized void sendRawLineViaQueue(String line) {
        if (line == null) {
            throw new NullPointerException("Cannot send null messages to server");
        }
        try {
            if ((inst != null ? inst : (inst = SnipesBot.getInst())).isConnected()) {
                try {
                    (inst != null ? inst : (inst = SnipesBot.getInst())).getOutCue().add(line);
                } catch (NoSnipesInstanceException ex) {
                }
            }
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Sends a message to a channel or a private message to a user.  These
     * messages are added to the outgoing message queue and sent at the
     * earliest possible opportunity.
     * <p/>
     * Some examples: -
     * <pre>    // Send the message "Hello!" to the channel #cs.
     *    sendMessage("#cs", "Hello!");
     * <p/>
     *    // Send a private message to Paul that says "Hi".
     *    sendMessage("Paul", "Hi");</pre>
     * <p/>
     * You may optionally apply colours, boldness, underlining, etc to
     * the message by using the <code>Colors</code> class.
     *
     * @param target  The name of the channel or user nick to send to.
     * @param message The message to send.
     * @see Colors
     */
    protected final void sendMessage(String target, String message) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).getOutCue().add("PRIVMSG " + target + " :" + message);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Sends an action to the channel or to a user.
     *
     * @param target The name of the channel or user nick to send to.
     * @param action The action to send.
     * @see Colors
     */
    protected final void sendAction(String target, String action) {
        sendCTCPCommand(target, "ACTION " + action);
    }

    /**
     * Sends a notice to the channel or to a user.
     *
     * @param target The name of the channel or user nick to send to.
     * @param notice The notice to send.
     */
    protected final void sendNotice(String target, String notice) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).getOutCue().add("NOTICE " + target + " :" + notice);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Sends a CTCP command to a channel or user.  (Client to client protocol).
     * Examples of such commands are "PING <number>", "FINGER", "VERSION", etc.
     * For example, if you wish to request the version of a user called "Dave",
     * then you would call <code>sendCTCPCommand("Dave", "VERSION");</code>.
     * The type of response to such commands is largely dependant on the target
     * client software.
     *
     * @param target  The name of the channel or user to send the CTCP message to.
     * @param command The CTCP command to send.
     */
    protected final void sendCTCPCommand(String target, String command) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).getOutCue().add("PRIVMSG " + target + " :\u0001" + command + "\u0001");
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Attempt to change the current nick (nickname) of the bot when it
     * is connected to an IRC server.
     * After confirmation of a successful nick change, the getNick method
     * will return the new nick.
     *
     * @param newNick The new nick to use.
     */
    protected final void changeNick(String newNick) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("NICK " + newNick);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Set the mode of a channel.
     * this method attempts to set the mode of a channel.  this
     * may require the this to have operator status on the channel.
     * For example, if the bot has operator status, we can grant
     * operator status to "Dave" on the #cs channel
     * by calling setMode("#cs", "+o Dave");
     * An alternative way of doing this would be to use the op method.
     *
     * @param channel The channel on which to perform the mode change.
     * @param mode    The new mode to apply to the channel.  This may include
     *                zero or more arguments if necessary.
     * @see #op(String, String) op
     */
    protected final void setMode(String channel, String mode) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("MODE " + channel + " " + mode);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Sends an invitation to join a channel.  Some channels can be marked
     * as "invite-only", so it may be useful to allow a bot to invite people
     * into it.
     *
     * @param nick    The nick of the user to invite
     * @param channel The channel you are inviting the user to join.
     */
    protected final void sendInvite(String nick, String channel) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("INVITE " + nick + " :" + channel);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Bans a user from a channel.  An example of a valid hostmask is
     * "*!*compu@*.18hp.net".  bot may be used in conjunction with the
     * kick method to permanently remove a user from a channel.
     * Successful use of bot method may require the bot to have operator
     * status itself.
     *
     * @param channel  The channel to ban the user from.
     * @param hostmask A hostmask representing the user we're banning.
     */
    protected final void ban(String channel, String hostmask) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("MODE " + channel + " +b " + hostmask);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Unbans a user from a channel.  An example of a valid hostmask is
     * "*!*compu@*.18hp.net".
     * Successful use of bot method may require the bot to have operator
     * status itself.
     *
     * @param channel  The channel to unban the user from.
     * @param hostmask A hostmask representing the user we're unbanning.
     */
    protected final void unBan(String channel, String hostmask) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("MODE " + channel + " -b " + hostmask);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Grants operator privilidges to a user on a channel.
     * Successful use of bot method may require the bot to have operator
     * status itself.
     *
     * @param channel The channel we're opping the user on.
     * @param nick    The nick of the user we are opping.
     */
    protected final void op(String channel, String nick) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).setMode(channel, "+o " + nick);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Removes operator privilidges from a user on a channel.
     * Successful use of bot method may require the bot to have operator
     * status itself.
     *
     * @param channel The channel we're deopping the user on.
     * @param nick    The nick of the user we are deopping.
     */
    protected final void deOp(String channel, String nick) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).setMode(channel, "-o " + nick);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Grants voice privilidges to a user on a channel.
     * Successful use of bot method may require the bot to have operator
     * status itself.
     *
     * @param channel The channel we're voicing the user on.
     * @param nick    The nick of the user we are voicing.
     */
    protected final void voice(String channel, String nick) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).setMode(channel, "+v " + nick);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Removes voice privilidges from a user on a channel.
     * Successful use of bot method may require the bot to have operator
     * status itself.
     *
     * @param channel The channel we're devoicing the user on.
     * @param nick    The nick of the user we are devoicing.
     */
    protected final void deVoice(String channel, String nick) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).setMode(channel, "-v " + nick);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Set the topic for a channel.
     * bot method attempts to set the topic of a channel.  bot
     * may require the bot to have operator status if the topic
     * is protected.
     *
     * @param channel The channel on which to perform the mode change.
     * @param topic   The new topic for the channel.
     */
    protected final void setTopic(String channel, String topic) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("TOPIC " + channel + " :" + topic);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Kicks a user from a channel.
     * bot method attempts to kick a user from a channel and
     * may require the bot to have operator status in the channel.
     *
     * @param channel The channel to kick the user from.
     * @param nick    The nick of the user to kick.
     */
    protected final void kick(String channel, String nick) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).kick(channel, nick, "");
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Kicks a user from a channel, giving a reason.
     * bot method attempts to kick a user from a channel and
     * may require the bot to have operator status in the channel.
     *
     * @param channel The channel to kick the user from.
     * @param nick    The nick of the user to kick.
     * @param reason  A description of the reason for kicking a user.
     */
    protected final void kick(String channel, String nick, String reason) {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("KICK " + channel + " " + nick + " :" + reason);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Issues a request for a list of all channels on the IRC server.
     * When the Pircbot receives information for each channel, it will
     * call the onChannelInfo method, which you will need to override
     * if you want it to do anything useful.
     */
    protected final void listChannels() {
        try {
            (inst != null ? inst : (inst = SnipesBot.getInst())).listChannels(null);
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Issues a request for a list of all channels on the IRC server.
     * When the Pircbot receives information for each channel, it will
     * call the onChannelInfo method, which you will need to override
     * if you want it to do anything useful.
     * <p/>
     * Some IRC servers support certain parameters for LIST requests.
     * One example is a parameter of ">10" to list only those channels
     * that have more than 10 users in them.  Whether these parameters
     * are supported or not will depend on the IRC server software.
     *
     * @param parameters The parameters to supply when requesting the
     *                   list.
     */
    protected final void listChannels(String parameters) {
        try {
            if (parameters == null) {
                (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("LIST");
            } else {
                (inst != null ? inst : (inst = SnipesBot.getInst())).sendRawLine("LIST " + parameters);
            }
        } catch (NoSnipesInstanceException ex) {
        }
    }

    /**
     * Sends a file to another user.  Resuming is supported.
     * The other user must be able to connect directly to your this to be
     * able to receive the file.
     * <p/>
     * You may throttle the speed of this file transfer by calling the
     * setPacketDelay method on the DccFileTransfer that is returned.
     * <p/>
     * this method may not be overridden.
     *
     * @param file    The file to send.
     * @param nick    The user to whom the file is to be sent.
     * @param timeout The number of milliseconds to wait for the recipient to
     *                acccept the file (we recommend about 120000).
     * @return The DccFileTransfer that can be used to monitor bot transfer. Null if the Snipes instance cannot be found.
     * @see DccFileTransfer
     */
    protected final DccFileTransfer dccSendFile(File file, String nick, int timeout) {
        try {
            DccFileTransfer transfer;
            transfer = new DccFileTransfer((inst != null ? inst : (inst = SnipesBot.getInst())), (inst != null ? inst : (inst = SnipesBot.getInst())).getManager(), file, nick, timeout);
            transfer.doSend(true);
            return transfer;
        } catch (NoSnipesInstanceException ex) {
            Logger.getLogger(PluginType.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Attempts to establish a DCC CHAT session with a client.  bot method
     * issues the connection request to the client and then waits for the
     * client to respond.  If the connection is successfully made, then a
     * DccChat object is returned by bot method.  If the connection is not
     * made within the time limit specified by the timeout value, then null
     * is returned.
     * <p/>
     * It is <b>strongly recommended</b> that you call bot method within a new
     * Thread, as it may take a long time to return.
     * <p/>
     * bot method may not be overridden.
     *
     * @param nick    The nick of the user we are trying to establish a chat with.
     * @param timeout The number of milliseconds to wait for the recipient to
     *                accept the chat connection (we recommend about 120000).
     * @return a DccChat object that can be used to send and recieve lines of
     *         text.  Returns <b>null</b> if the connection could not be made.
     * @see DccChat
     */
    protected final DccChat dccSendChatRequest(String nick, int timeout) {
        DccChat chat = null;
        try {
            ServerSocket ss = null;

            int[] ports = (inst != null ? inst : (inst = SnipesBot.getInst())).getDccPorts();
            if (ports == null) {
                // Use any free port.
                ss = new ServerSocket(0);
            } else {
                for (int i = 0; i < ports.length; i++) {
                    try {
                        ss = new ServerSocket(ports[i]);
                        // Found a port number we could use.
                        break;
                    } catch (Exception e) {
                        // Do nothing; go round and try another port.
                    }
                }
                if (ss == null) {
                    // No ports could be used.
                    throw new IOException("All ports returned by getDccPorts() are in use.");
                }
            }

            ss.setSoTimeout(timeout);
            int port = ss.getLocalPort();

            InetAddress inetAddress = (inst != null ? inst : (inst = SnipesBot.getInst())).getDccInetAddress();
            if (inetAddress == null) {
                inetAddress = (inst != null ? inst : (inst = SnipesBot.getInst())).getInetAddress();
            }
            byte[] ip = inetAddress.getAddress();
            long ipNum = ipToLong(ip);

            sendCTCPCommand(nick, "DCC CHAT chat " + ipNum + " " + port);

            // The client may now connect to us to chat.
            Socket socket = ss.accept();

            // Close the server socket now that we've finished with it.
            ss.close();

            chat = new DccChat((inst != null ? inst : (inst = SnipesBot.getInst())), nick, socket);
        } catch (Exception e) {
            // Do nothing.
        }
        return chat;
    }

    /**
     * A convenient method that accepts an IP address represented by a byte[]
     * of size 4 and returns this as a long representation of the same IP
     * address.
     *
     * @param address the byte[] of size 4 representing the IP address.
     * @return a long representation of the IP address.
     */
    protected long ipToLong(byte[] address) {
        if (address.length != 4) {
            throw new IllegalArgumentException("byte array must be of length 4");
        }
        long ipNum = 0;
        long multiplier = 1;
        for (int i = 3; i >= 0; i--) {
            int byteVal = (address[i] + 256) % 256;
            ipNum += byteVal * multiplier;
            multiplier *= 256;
        }
        return ipNum;
    }

    /**
     * @return The current nick of the bot
     */
    protected String getNick() {
        try {
            return (inst != null ? inst : (inst = SnipesBot.getInst())).getNick();
        } catch (NoSnipesInstanceException ex) {
        }
        return null;
    }

    /**
     * Exits Snipes, with the given reason and the given status
     */
    protected void exit(int status, String reason) throws NoSnipesInstanceException {
        System.err.println(reason);
        (inst != null ? inst : (inst = SnipesBot.getInst())).exitSnipes(status);
    }
    public boolean isBotAdministrator(String hostname)
    {
        try
        {
            return inst != null ? inst.isBotAdministrator(hostname) : (inst = SnipesBot.getInst()).isBotAdministrator(hostname);
        } catch (NoSnipesInstanceException e) {}
        return false;
    }
    public boolean isBotModerator(String hostname)
    {
        try
        {
            return (inst != null ? inst.isBotAdministrator(hostname) : (inst = SnipesBot.getInst()).isBotAdministrator(hostname));
        } catch (NoSnipesInstanceException e) {}
        return false;
    }
    public boolean isBotOwner(String hostname)
    {
        try
        {
            return (inst != null ? inst.isBotOwner(hostname) : (inst = SnipesBot.getInst()).isBotOwner(hostname));
        } catch (NoSnipesInstanceException e) {}
        return false;
    }
}
