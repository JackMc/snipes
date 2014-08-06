/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.ossnipes.snipes.lib.irc;

/**
 * Interface for all bot constants (bot constants are constants that are not IRC
 * response codes)
 * 
 * @since Snipes 0.6
 * @author Jack McCracken
 */
public interface BotConstants
{
	/** The float-represented version of the Snipes IRC library */
	static final float SNIRC_VERSION = 1.01f;
	/** The String representation of {@link #SNIRC_VERSION} */
	static final String SNIRC_VERSION_STRING = Float.toString(SNIRC_VERSION);
	/** The default port for IRC servers */
	static final int IRC_DEFAULT_PORT = 6667;
	/** The IRC server timeout in milliseconds */
	static final int IRC_TIMEOUT = 120 * 1000;
	static final Character[] IRC_CHANPREFIXES = {'#', '&', '+'};
	static final Character[] IRC_NICKPREFIXES = {'!', '~', '&', '@', '%', '+'};
	/**
	 * The default username (before the @ and after the ! in the hostname) if it
	 * has not been set.
	 */
	static final String DEFAULT_USER = "SnipesBot";
	/**
	 * The default nick (you know what a nick is :P) if it has not been set
	 */
	static final String DEFAULT_NICK = "Snipes-RunSetNick";
	/**
	 * The default realname for the bot if it has not been set
	 */
	static final String DEFAULT_REALNAME = "Snipes IRC bot, version " + SNIRC_VERSION_STRING;
}
