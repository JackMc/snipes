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

public class TrackerPlugin extends Module
{
	public static final int RPL_FREENODE_WHOIS_IDENTIFIED = 330;
	private static final String filename = "magicals.txt";
	private String report_to;
	
	@Override
	protected ModuleReturn snipesInit()
	{
		report_to = this.getConfiguration().getProperty("tracker_report");
		if (report_to == null) {
			this.setError("No reporting channel specified.");
			return ModuleReturn.ERROR;
		}

		if (report_to.startsWith("#")) {
			this.getParent().join(report_to);
		}

		return ModuleReturn.NORMAL;
	}

	@Override
	public Event[] getRegisteredEvents()
	{
		return new Event [] {Event.IRC_RESPONSE_CODE, Event.IRC_JOIN };
	}

	public void sendData(String message) {
		this.getParent().sendPrivMsg(this.report_to, message);
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{	
		if (ev == Event.IRC_JOIN) {
			String send_channel = args.getParamAsString("channel");
			String nick = args.getParamAsString("nick");
			String host = args.getParamAsString("host");

			// Check that it's in the channel and that it's not someone with a hostmask
			if (!host.contains("/")) {
				sendData("Identified join from " + nick + "@" + host);
				this.getParent().sendRaw("WHOIS " + nick);
			}
		}
		
		if (ev == Event.IRC_RESPONSE_CODE) {
			int code = (Integer)args.getParam("code");
			if (code == RPL_FREENODE_WHOIS_IDENTIFIED) {
				String[] msgSplit = args.getParamAsString("resp_text").split(" ");
				String nick = args.getParamAsString("line").split(" ")[3];
				sendData("- (" + nick + ") ID: " + msgSplit[1]);
			}
			if (code == IRCConstants.RPL_WHOISUSER) {
				String resp = args.getParamAsString("resp_text");
				String[] msgSplit = resp.split(" ");
				String nick = args.getParamAsString("line").split(" ")[3];
				sendData("- (" + nick + ") User: " + (msgSplit[1].startsWith("~") ? msgSplit[1].substring(1) : msgSplit[1]));
				sendData("- (" + nick + ") Realname: " + resp.substring(resp.indexOf(":") + 1));
				sendData("- (" + nick + ") Identd: " + (msgSplit[1].startsWith("~") ? "No" : "Yes"));
			}
		}
	}
}
