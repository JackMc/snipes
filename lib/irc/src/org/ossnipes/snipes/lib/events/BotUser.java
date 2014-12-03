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

import org.ossnipes.snipes.lib.irc.BotConstants;
import org.ossnipes.snipes.lib.irc.SnipesException;

public class BotUser implements BotConstants
{
    public BotUser(String nick)
    {
        if (nick == null)
        {
            throw new IllegalArgumentException("Nick cannot be null.");
        }
        _nick = nick;
    }
	
    public String getNick(boolean withPrefix)
    {
        // Final result
        String nick = _nick;
        if (nick.length() == 0)
        {
            throw new SnipesException("User's nick is empty.");
        }
        //TODO: More checking for user nicks and other things, this includes a handler for the mode event.
        else if (!withPrefix && BotUtils.arrayContains(IRC_NICKPREFIXES, nick.charAt(0)))
        {
            nick = nick.substring(1);
        }
        return nick;
    }
	
	
    // Cannot have a String version of this method. This class does not get the IRCBase instance which manages the channels.
    public boolean isInChannel(Channel channel)
    {
        return channel.isUserInChannel(this);
    }
	
    public String getNick()
    {
        return getNick(false);
    }
	
    private String _nick;
}
