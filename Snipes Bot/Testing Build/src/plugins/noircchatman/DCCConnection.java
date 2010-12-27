package plugins.noircchatman;

import org.jibble.pircbot.DccChat;
import org.ossnipes.snipes.enums.SnipesStaffType;
import org.ossnipes.snipes.exceptions.SnipesSecurityException;
import org.ossnipes.snipes.spf.PluginType;

import java.io.IOException;

public class DCCConnection implements NoIRCConnection {
    private DccChat rawChat;

    public DCCConnection(DccChat chat, SnipesStaffType acceptFrom, PluginType curr) throws IOException, SnipesSecurityException
    {
        if (chat == null)
        {
            throw new NullPointerException("rawChat cannot be null.");
        }
        rawChat = chat;
        switch (acceptFrom)
        {
            case SNIPES_IRC_ADMIN:
            {
                if (curr.isBotAdministrator(chat.getHostname()))
                {
                    chat.accept();
                }
                else
                {
                    throw new SnipesSecurityException("Could not accept client connection from " + chat.getHostname() + ". Did not have " + acceptFrom + " credentials.");
                }
                break;
            }
            case SNIPES_IRC_OWNER:
            {
                if (curr.isBotOwner(chat.getHostname()))
                {
                    chat.accept();
                }
                else
                {
                    throw new SnipesSecurityException("Could not accept client connection from " + chat.getHostname() + ". Did not have " + acceptFrom + " credentials.");
                }
                break;
            }
            case SNIPES_IRC_MODERATOR:
            {
                if (curr.isBotModerator(chat.getHostname()))
                {
                    chat.accept();
                }
                else
                {
                    throw new SnipesSecurityException("Could not accept DCC connection from " + chat.getHostname() + ". Did not have " + acceptFrom + " credentials.");
                }
                break;
            }
           default:
               throw new SnipesSecurityException("Could not accept DCC connection from " + chat.getHostname() + ", as you specified unknown credential types to the function.");
        }
        System.out.println("Chat authorized from " + chat.getHostname());
        chat.sendLine("Welcome to the Snipes IRC DCC chat system! Use this system for good, not evil :). Try \"help\" for some commands.");
        _finished = true;
    }

    public String recv() {
        try
        {
            return rawChat.readLine();
        } catch (IOException e) {return null;}
    }

    public void send(String s) {
        try
        {
            System.out.println(">> DCC >> " + rawChat.getNick() + " >> " + s);
            rawChat.sendLine(s);
        } catch (IOException e) {System.err.println("IOException while sending message to user.");}
    }

    public String getHost() {
        return rawChat.getHostname();
    }

    public void close() {
        try
        {
            rawChat.close();
        } catch (IOException e) {}
    }

    public boolean isOpen() {
        return _open;
    }

    private boolean _open = false;
    private boolean _finished = false;
}
