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
 * A exception thrown when a operation is attempted that would require
 * connection to a IRC server, and we are not connected.
 * 
 * @author jack
 * 
 */
public class NotConnectedException extends RuntimeException
{
    private static final long serialVersionUID = 7694269847054717555L;

    public NotConnectedException()
    {
        super();
    }

    public NotConnectedException(String message)
    {
        super(message);
    }

    public NotConnectedException(Throwable cause)
    {
        super(cause);
    }

    public NotConnectedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
