/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.ossnipes.snipes.console;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SpamConsole 
{
	private static final class SpamConsoleConnector implements Runnable {
		public static volatile int failures;
		@Override
		public void run() {
			SocketManager sm = null;
			try {
				sm = new SocketManager(new Socket("localhost", 9001));
			} catch (UnknownHostException e) {
				fail();
				return;
			} catch (IOException e) {
				fail();
				return;
			}
			
			sm.sendln("HELLO");
			sm.sendln("example");
			sm.sendln("password");
			try {
				sm.recv();
				for (int i = 1; i <= 5; i++)
				{
					sm.sendln("SAYHELLO");
					sm.recv();
					sm.recv();
				}
			} catch (IOException e) {
				fail();
				return;
			}
		}
		private void fail() {
			failures++;
			return;
		}
	}

	// Stress testing the server
	public static void main(String args[])
	{
		final int TOTAL = 10000;
		int i = 0;
		
		
		while (i < TOTAL)
		{
			new Thread(new SpamConsoleConnector()).start();
			i++;
		}
		
		System.err.println("Failures: " + SpamConsoleConnector.failures);
	}
}
