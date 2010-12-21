package plugins;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.enums.PluginConstructRet;
import org.ossnipes.snipes.enums.PluginDestructRet;
import org.ossnipes.snipes.enums.PluginPassResponse;
import org.ossnipes.snipes.enums.SnipesEvent;
import org.ossnipes.snipes.spf.SnipesEventParams;
import org.ossnipes.snipes.spf.SuperPlugin;
import org.ossnipes.snipes.exceptions.NoSnipesInstanceException;

/**
 * Plugin to manage the dynamic loading of Snipes plugins without
 * shutting down Snipes.
 *
 * @author Jack McCracken (Auv5)
 */
public class DebugUtils extends SuperPlugin {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public PluginPassResponse handleEvent(SnipesEvent event,
                                          SnipesEventParams params) {
        String[] paramsArr = params.getParamsArr();
        switch (event) {
            case SNIPES_IRC_CHANMSG: {
                String[] msgEx = params.getParamEx(3);
                if (msgEx[0].equalsIgnoreCase("!die")) {
                    String dieName = null;
                    if (msgEx.length == 2 && msgEx[1].equalsIgnoreCase(getNick())) {
                        try {
                            this.exit(0, "Die command");
                        } catch (NoSnipesInstanceException e) {
                        }
                    } else if (msgEx.length == 1) {
                        try {
                            this.exit(0, "Die command");
                        } catch (NoSnipesInstanceException e) {
                        }
                    }
                }
            }
            break;
        }
        return null;
    }

    @Override
    public PluginDestructRet snipesFini(int status) {
        return null;
    }

    @Override
    public PluginConstructRet snipesInit() {
        return null;
    }
}