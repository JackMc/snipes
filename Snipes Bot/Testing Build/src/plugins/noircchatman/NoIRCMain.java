package plugins.noircchatman;

import org.jibble.pircbot.DccChat;
import org.ossnipes.snipes.enums.*;
import org.ossnipes.snipes.exceptions.SnipesSecurityException;
import org.ossnipes.snipes.spf.SnipesEventParams;
import org.ossnipes.snipes.spf.SuperPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoIRCMain extends SuperPlugin {
    List<NoIRCConnection> connectionList = new ArrayList<NoIRCConnection>();
    @Override
    protected PluginPassResponse handleEvent(SnipesEvent event, SnipesEventParams params) {
        switch (event)
        {
            case SNIPES_IRC_DCCCHAT:
            {
                DccChat c = (DccChat) params.getObjectParamsArr()[0];
                try
                {
                    DCCConnection conn = new DCCConnection(c, SnipesStaffType.SNIPES_IRC_MODERATOR, this);
                    // This doesn't need multi-threading, as the listen/response system does, as all
                    // it does is check the credentials of the person, and accept the chat and send
                    // the welcome if they are allowed.
                } catch (IOException e) {

                }
                catch (SnipesSecurityException e) {
                    try
                    {
                        c.accept();
                        c.sendLine("Sorry, but you do not have the required credentials to request a connection with this bot. " +
                                "Try and get " + SnipesStaffType.SNIPES_IRC_MODERATOR + " rank, and maybe I'll think about it.");
                    } catch (IOException owell) {}
                }
                break;
            }
        }
        return null;
    }

    @Override
    public PluginConstructRet snipesInit() {
        //TODO: Implement threaded listener/responder system
        return null;
    }

    @Override
    public PluginDestructRet snipesFini(int status) {
        return null;
    }

    @Override
    public String getName() {
        return "Non-IRC connection moderator.";
    }
}
