/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package plugins.irchook;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class SocketHook extends Hook
{

	public SocketHook(Socket s)
	{
		this._s = s;
		try
		{
			this._ps = new PrintStream(this._s.getOutputStream());
		} catch (IOException e)
		{
			System.err
					.println("IRCHook: Unable to initialise socket PrintStream: "
							+ e.getMessage());
		}
	}

	@Override
	public boolean init()
	{
		return true;
		//
	}

	@Override
	public void line(String line)
	{
		this._ps.println(line);
	}

	@Override
	public void fini()
	{
		try
		{
			this._s.close();
		} catch (IOException e)
		{
			// We don't care.
		}
	}

	Socket _s;
	PrintStream _ps;
}
