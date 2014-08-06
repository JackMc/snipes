/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.ossnipes.snipes.lib.events;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;


/**
 * A very simple IRC bot that just prints out information about
 * the events it receives.
 * @author Jack
 *
 */
public class ExampleBot extends IRCBase
{
	public static void main(String args[])
	{
		new ExampleBot().run();
	}
	
	public void run()
	{
		try
		{
			setRealname("Snipes IRC Framework example bot.");
			setVerbose(true);
			setNick("Snipes-TestBot");
			connect("irc.geekshed.net");
			join("#Snipes");
			join("#Snipes-Testing");
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public synchronized void handleEvent(Event ev, EventArgs args)
	{
		Set<String> keys = args.getKeySet();
		StringBuilder stringBuilder = new StringBuilder("Got an event: " + ev.toString() + " ");
		int counter = 0;
		for (String s : keys)
		{
			stringBuilder.append((counter != 0 ? " " : "") + s + "=\"" + args.getParam(s) + "\"");
			counter ++;
		}

		System.err.println(stringBuilder.toString());
		if (ev == Event.IRC_PRIVMSG)
		{
			String msg = (String)args.getParam("message");
			String[] msgSplit = msg.split(" ");
			if (msgSplit.length == 2 && msgSplit[0].equalsIgnoreCase("!nick"))
			{
				setNick(msgSplit[1]);
			}
		}
	}
}
