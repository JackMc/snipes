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

/** Plugin to manage the dynamic loading of Snipes plugins without 
 * shutting down Snipes.
 * @author Jack McCracken (Auv5)
 */
public class SPFManager extends SuperPlugin {

    @Override
    public String getName() {
        //TODO: Finish.
        return null;
    }

    @Override
    public PluginPassResponse handleEvent(SnipesEvent event,
            SnipesEventParams params) {
        String[] paramsArr = params.getParamsArr();
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
