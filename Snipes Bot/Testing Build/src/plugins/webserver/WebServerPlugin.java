package plugins.webserver;

import org.ossnipes.snipes.enums.PluginConstructRet;
import org.ossnipes.snipes.enums.PluginDestructRet;
import org.ossnipes.snipes.spf.NoEventPlugin;


public class WebServerPlugin extends NoEventPlugin {
    WebServerServer server;

    @Override
    public PluginConstructRet snipesInit() {
        server = new WebServerServer();
        server.begin();
        return PluginConstructRet.PLUGIN_LOADSUCCESS;
    }

    @Override
    public PluginDestructRet snipesFini(int status) {
        return null;
    }

    @Override
    public String getName() {
        return "Snipes web server";
    }
}
