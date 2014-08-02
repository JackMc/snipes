/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package plugins.noircman;

import org.ossnipes.snipes.bot.Configuration;
import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;
import org.ossnipes.snipes.lib.events.IRCEventListener;

public class NoIRCModule extends Module
{

	@Override
	protected ModuleReturn snipesInit()
	{
		new NoIRCMain(this);
		// No use wasting time on us when we don't handle any events.
		this.getParent().removeEventListener(this);
		return null;
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		// No code
	}

	public Configuration getConf()
	{
		return this.getConfiguration();
	}

	public SnipesBot getBot()
	{
		return this.getParent();
	}

	public boolean isConnected()
	{
		return this.getParent().isConnected();
	}

	public void addEventListener(IRCEventListener evl)
	{
		this.getParent().addEventListener(evl);
	}
}
