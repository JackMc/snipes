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

/** Just a simple class to test the IRCEventListener interface. */
class TestHandler implements IRCEventListener
{
    public TestHandler(IRCBase parent)
    {
        _parent = parent;
        _parent.addEventListener(this);
    }

    public Event[] getRegisteredEvents()
    {
        return new Event[] {Event.IRC_RESPONSE_CODE};
    }

    public void handleEvent(Event ev, EventArgs args)
    {
        System.out.println("BE SCARED! Text: " + args.getParamAsString("text"));
        if (ev == Event.IRC_RESPONSE_CODE)
        {
            System.out.println("Test: text=" + args.getParam("text") + "server=" + args.getParam("server") + "code=" + args.getParam("code"));
        }
    }

    private IRCBase _parent;
}
