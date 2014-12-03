/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package plugins.bouncer;

/** Contains a list of pseudo-clients the bot should query with lines (in order).
 * 
 * @author Jack McCracken (Auv5) */
public enum EnumPseudoClient
{
    // Order matters!
    BOT_CLIENT(BotClient.class), IRC_CLIENT(IRCClient.class);

    EnumPseudoClient(Class<? extends PseudoClient> clazz)
    {
        if (clazz == null)
        {
            throw new IllegalArgumentException("clazz cannot be null.");
        }

        this._clazz = clazz;
    }

    public Class<? extends PseudoClient> getClientClass()
    {
        return this._clazz;
    }

    private Class<? extends PseudoClient> _clazz;
}
