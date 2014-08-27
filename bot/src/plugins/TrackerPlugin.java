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

import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;
import org.ossnipes.snipes.lib.irc.IRCConstants;

import java.util.Map;

public class TrackerPlugin extends Module
{
	public static final int RPL_FREENODE_WHOIS_IDENTIFIED = 330;
	
	private Map<String, String> nicks;
	private String channel;
	private String owner;
	
	@Override
	protected ModuleReturn snipesInit()
	{
		channel = this.getConfiguration().getProperty("tracker_channel");
		owner = this.getConfiguration().getProperty("tracker_owner");

		return null;
	}

	@Override
	public Event[] getRegisteredEvents()
	{
		return new Event [] {Event.IRC_RESPONSE_CODE, Event.IRC_JOIN };
	}

	public void sendData(String message) {
		this.getParent().sendPrivMsg(this.owner, message);
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{	
		if (ev == Event.IRC_JOIN) {
			String send_channel = args.getParamAsString("channel");
			String nick = args.getParamAsString("nick");
			String host = args.getParamAsString("host");

			// Check that it's in the channel and that it's not someone with a hostmask
			if (send_channel.equalsIgnoreCase(this.channel) && !host.contains("/")) {
				sendData("Identified join from " + nick + "@" + host);
				this.getParent().sendRaw("WHOIS " + nick);
			}
		}
		
		if (ev == Event.IRC_RESPONSE_CODE) {
			int code = (Integer)args.getParam("code");
			if (code == RPL_FREENODE_WHOIS_IDENTIFIED) {
				String[] msgSplit = args.getParamAsString("resp_text").split(" ");
				sendData("- ID: " + msgSplit[1]);
			}
			if (code == IRCConstants.RPL_WHOISUSER) {
				String resp = args.getParamAsString("resp_text");
				String[] msgSplit = resp.split(" ");
				sendData("- User: " + (msgSplit[1].startsWith("~") ? msgSplit[1].substring(1) : msgSplit[1]));
				sendData("- Realname: " + resp.substring(resp.indexOf(":") + 1));
				sendData("- Identd: " + (msgSplit[1].startsWith("~") ? "No" : "Yes"));
			}
		}
	}
}
