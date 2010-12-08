package plugins;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jibble.pircbot.PircBot;
import org.ossnipes.snipes.enums.PluginConstructRet;
import org.ossnipes.snipes.enums.PluginDestructRet;
import org.ossnipes.snipes.enums.PluginPassResponse;
import org.ossnipes.snipes.enums.SnipesEvent;
import org.ossnipes.snipes.spf.Plugin;
import org.ossnipes.snipes.spf.SnipesEventParams;

public class JavaScriptExample extends Plugin {
    // Get the plugin's name

    @Override
    public String getName() {
        return "Snipes JavaScript parsing example";
    }
    // Called when a event is sent to us (in this case only SNIPES_IRC events)

    @Override
    protected PluginPassResponse handleEvent(SnipesEvent event,
            SnipesEventParams params) {
        // Get the PircBot object
        PircBot bot = params.getBot();
        // Get the event params in a String
        String[] eventParams = params.getParamsArr();
        switch (event) {
            // If the event is a channel message
            case SNIPES_IRC_CHANMSG: {
                // Split it into a string array by space, using the params' function
                String[] msgEx = params.getParamEx(3);
                // Check if the first word is !jseval
                if (msgEx[0].equalsIgnoreCase("!jseval")) {
                    // If the message length is one (we didn't get any args)
                    if (msgEx.length == 1) {
                        // Tell them off
                        params.getBot().sendMessage(eventParams[0], "Use like !jseval <script>");
                        // Break out of the switch
                        break;
                    }
                    // Will hold the script to evaluate
                    String script = "";
                    // Loop through the ex
                    for (String s : msgEx) {
                        // If it is the message, go to the next item
                        if (s.equalsIgnoreCase(msgEx[0])) {
                            continue;
                        }
                        // If not concat it into the string. This also checks if
                        // it is the first string in the evaluation, and if so,
                        // doesn't insert a space. (Note: We lost all spaces when
                        // we split the msgEx.)
                        script += (s.equalsIgnoreCase(msgEx[1]) ? "" : " ") + s;
                    }
                    // Initialize the ScriptEngineManager and get the JavaScript
                    // ScriptEngine.
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("js");
                    try {
                        // Tell the user what we are evaluating
                        bot.sendMessage(eventParams[0], "Evaluating: \"" + script + "\"");
                        // Actually evaluate the script
                        Object obj = engine.eval(script);
                        // Tell them what we got back from the JavaScript
                        bot.sendMessage(eventParams[0], "Function returned: " + obj);
                    } // If a ScriptException occurs (there was a error parsing their
                    // JavaScript.)
                    catch (ScriptException e) {
                        // Get the error message
                        String error = e.getMessage();
                        // Replace the <Unknown Source> from evaluating in Java
                        // with something more friendly (<Snipes JSEval>.)
                        error = error.replace("<Unknown source>", "<Snipes JSEval>");
                        // Tell them we are PMing them the error.
                        bot.sendMessage(eventParams[0], "I'm sorry " + eventParams[1] + " but there was  a "
                                + "error parsing your JavaScript. I'm PMing you the error to avoid flooding.");
                        bot.sendMessage(eventParams[1], "(This is a message about the JavaScript evaluation requested"
                                + " in " + eventParams[0] + ".) The JavaScript error was: \"" + error + "\".");
                    }
                }
                break;
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
