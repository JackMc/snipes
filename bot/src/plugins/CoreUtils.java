/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package plugins;

import org.ossnipes.snipes.bot.CoreModule;
import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleInitException;
import org.ossnipes.snipes.bot.ModuleLoadException;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.bot.SnipesConstants;
import org.ossnipes.snipes.bot.ModuleEvent;
import org.ossnipes.snipes.lib.events.BotUtils;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;

import java.util.List;

public class CoreUtils extends CoreModule
{
    @Override
    protected ModuleReturn snipesInit()
    {
        return null;
    }
    
    public Event[] getRegisteredEvents()
    {
        return new Event[] {
            Event.IRC_PRIVMSG,
        };
    }

    @Override
    public void handleEvent(Event ev, EventArgs args)
    {
        if (ev == Event.IRC_PRIVMSG)
        {
            String sendTo = args.getParamAsString("sendto");
            String msg = args.getParamAsString("message");
            String[] msgSplit = msg.split(" ");

            if (msgSplit.length == 0)
            {
                return;
            }

            if (msgSplit[0].equalsIgnoreCase("!load"))
            {
                this.loadModuleCommand(sendTo, msgSplit);
            }
            else if (msgSplit[0].equalsIgnoreCase("!unload"))
            {
                this.unloadModuleCommand(sendTo, msgSplit);
            }
            else if (msgSplit[0].equalsIgnoreCase("!isloaded"))
            {
                this.isMLoadedCommand(sendTo, msgSplit);
            }

            else if (msgSplit[0].equalsIgnoreCase("!listmods"))
            {
                this.listModulesCommand(sendTo);
            }
        }
    }

    private void listModulesCommand(String sendTo)
    {
        List<Module> modules = this.getParent().getModules();
		
        StringBuilder message = new StringBuilder("Modules: ");

        Module lastModule = modules.get(modules.size()-1);

        for (Module m : modules)
        {
            message.append(m.getClass().getName() + (m != lastModule ? ", " : ""));
        }

        this.getParent().sendPrivMsg(sendTo, message.toString());
    }

    private void unloadModuleCommand(String sendTo, String[] msgSplit)
    {
        if (msgSplit.length != 2)
        {
            this.getParent().sendPrivMsg(sendTo,
                                         "Incorrect syntax. Use like !unload mypackage.mymodule");
            return;
        }
        else
        {
            if (msgSplit[1].equals(this.getClass().getName()))
            {
                this.getParent().sendPrivMsg(sendTo,
                                             "This plugin cannot unload itself.");
                return;
            }
            if (BotUtils.arrayContains(SnipesConstants.CORE_MODULES,
                                       msgSplit[1]))
            {
                this.getParent().sendPrivMsg(sendTo,
                                             "Cannot unload a core module.");
                return;
            }
            else if (!this.removeModule(msgSplit[1]))
            {
                this.getParent()
                    .sendPrivMsg(
                        sendTo,
                        "Bot could not unload the specified module because it is not loaded (at least not by this bot).");
                return;
            }
            else
            {
                this.getParent().sendPrivMsg(sendTo, "Module unloaded.");
            }
        }
    }

    private void loadModuleCommand(String sendTo, String[] msgSplit)
    {
        if (msgSplit.length != 2)
        {
            this.getParent().sendPrivMsg(sendTo,
                                         "Incorrect syntax. Use like !load mypackage.mymodule");
            return;
        }
        else if (msgSplit[1].equals(this.getClass().getName()))
        {
            this.getParent().sendPrivMsg(sendTo,
                                         "This plugin cannot load itself.");
        }
        else if (this.isModuleLoaded(msgSplit[1]))
        {
            this.getParent().sendPrivMsg(sendTo,
                                         "That module is already loaded.");
        }
        else
        {
            try
            {
                this.loadModule(msgSplit[1]);
                this.getParent().sendPrivMsg(sendTo, "Module loaded.");
            } catch (ModuleLoadException e)
            {
                this.getParent()
                    .sendPrivMsg(
                        sendTo,
                        "Error: Bot could not load the specified module because it is not a subclass of Module or CoreModule.");
            } catch (InstantiationException e)
            {
                this.getParent()
                    .sendPrivMsg(
                        sendTo,
                        "Error: Bot could not load the specified module because it is a abstract class or some other kind of non-instantiable class.");
            } catch (IllegalAccessException e)
            {
                this.getParent()
                    .sendPrivMsg(
                        sendTo,
                        "Error: Bot could not load module the specified because it's constructor could not be called.");
            } catch (ClassNotFoundException e)
            {
                this.getParent()
                    .sendPrivMsg(
                        sendTo,
                        "Error: Bot could not load the specified module because it could not be found in the bot's CLASSPATH.");
            } catch (ModuleInitException e)
            {
                this.getParent().sendPrivMsg(
                    sendTo,
                    "Error: Bot could not start up the specified module: "
                    + e.getModule().getLastError());
            }
        }
    }

    private void isMLoadedCommand(String sendTo, String[] msgSplit)
    {
        if (msgSplit.length <= 1)
        {
            this.getParent().sendPrivMsg(sendTo,
                                         "Incorrect syntax. Use like !isloaded mypackage.mymodule");
        }
        this.getParent().sendPrivMsg(sendTo,
                                     "Module loaded status: " + this.isModuleLoaded(msgSplit[1]));
    }
}
