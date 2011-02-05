package org.ossnipes.snipes.bot;

import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;
import org.ossnipes.snipes.lib.irc.IRCBase;

import java.io.IOException;
import java.net.UnknownHostException;

public class SnipesBot extends IRCBase
{
    public SnipesBot()
    {
        super();
        try {
            applyDirectives();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void applyDirectives() throws IOException {
        // Set the nick to the configuration directive "nick". (Use the default of the bot's current nick)
        setNick(Configuration.lookUp("nick", getNick()));
        try
        {
            connect(Configuration.lookUp("server", "irc.geekshed.net"));

        }
        // So that we can provide a better error message
        catch (IOException e)
        {
            throw new IOException("Encountered a IOException while loading or acting on the information provided by the 'server' directive." +
                    " Original message: \"" + e.getMessage() +
                "\". Snipes' message: \"unable to connect to server specified by 'server' directive\"");
        }

    }

    @Override
    public void handleEvent(Event ev, EventArgs args)
    {

    }
    String _server;
}
