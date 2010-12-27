package plugins.noircchatman;

import org.jibble.pircbot.DccChat;
import org.ossnipes.snipes.enums.*;
import org.ossnipes.snipes.exceptions.SnipesSecurityException;
import org.ossnipes.snipes.spf.SnipesEventParams;
import org.ossnipes.snipes.spf.SuperPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class NoIRCMain extends SuperPlugin {
    BlockingQueue<NoIRCConnection> q = new LinkedBlockingQueue<NoIRCConnection>();
    Thread[] workers = new Thread[5];
    @Override
    protected PluginPassResponse handleEvent(SnipesEvent event, SnipesEventParams params) {
        switch (event)
        {
            case SNIPES_IRC_DCCCHAT:
            {
                System.out.println("SNIRCCM: Found a chat");
                DccChat c = (DccChat) params.getObjectParamsArr()[0];
                try
                {
                    NoIRCConnection conn = new DCCConnection(c, SnipesStaffType.SNIPES_IRC_MODERATOR, this);
                    // This doesn't need multi-threading, as the listen/response system does, as all
                    // it does is check the credentials of the person, and accept the chat and send
                    // the welcome if they are allowed.

                        try
                        {
                            q.put(conn);
                        } catch (InterruptedException e) {}
                } catch (IOException e) {

                }
                catch (SnipesSecurityException e) {
                    try
                    {
                        c.accept();
                        c.sendLine("Sorry, but you do not have the required credentials to request a connection with this bot. " +
                                "Try and get " + SnipesStaffType.SNIPES_IRC_MODERATOR + " rank, and maybe I'll think about it.");
                        c.close();
                    } catch (IOException owell) {}
                }
                break;
            }
        }
        return null;
    }

    @Override
    public PluginConstructRet snipesInit() {
        System.out.println("Snipes non-IRC chat moderation system: Starting up!");
        for (int i=0;i<workers.length;i++)
        {
            workers[i] = new Thread(new ConnectionManager(this,i));
            workers[i].start();
        }
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
    public NoIRCConnection getJob() throws InterruptedException {
        NoIRCConnection qq = q.take();
        return qq;
    }
}
