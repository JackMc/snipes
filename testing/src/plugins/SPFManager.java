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
        switch (event) {
            case SNIPES_IRC_CHANMSG: {
                String[] msgEx = params.getParamEx(3);
                if (msgEx[0].equalsIgnoreCase("!time")) {
                    if (msgEx.length < 1) {
                        sendMessage("#Snipes", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + " is the current date/time.");
                    } else {
                        sendMessage("#Snipes", "Silly! !time doesn't need params!");
                    }
                }
                if (msgEx[0].equalsIgnoreCase("!printthreads")) {
                    params.getBot().sendMessage(paramsArr[0], "Listing Snipes thread names!");
                    for (Thread t : SnipesBot.getThreadCollection()) {
                        sendMessage(paramsArr[0], t.getName());
                    }
                }
                if (msgEx[0].startsWith("!listex")) {
                    sendMessage(paramsArr[0], "Printing ex");
                    for (String s : msgEx) {
                        sendMessage(paramsArr[0], s);
                    }
                }
            }
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
