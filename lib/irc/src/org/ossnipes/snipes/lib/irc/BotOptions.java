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

class BotOptions
{
    private BotOptions()
    {
		
    }
	
    static BotOptions getInst()
    {
        return new BotOptions();
    }
	
    boolean isVerbose()
    {
        return _verbose;
    }
	
    boolean isDebugging()
    {
        return _debugging;
    }
	
    void setDebugging(boolean val)
    {
        _debugging = val;
    }
	
    void setVerbose(boolean val)
    {
        _verbose = val;
    }
	
    private boolean _verbose;
    private boolean _debugging;
}
