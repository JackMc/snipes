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

import java.io.IOException;

/**
 * Encapsulates the receiving of data from the IRC server.
 * 
 * @since Snipes 0.6
 * @author Jack McCracken
 */

class IRCReceiver implements Runnable
{
	// Lock for usage of the handler.
	private Object lock = new Object();
	
	/**
	 * Instantiates a object of the class.
	 * 
	 * @param manager
	 *            The socket manager that this thread should be watching for
	 *            messages
	 * @param initialHandler
	 *            The "parent" IRCBase of this object. In other words: The
	 *            object it will notify if it gets a message.
	 */
	IRCReceiver(IRCSocketManager manager, InputHandler initialHandler)
	{
		_manager = manager;
		_handler = initialHandler;
	}

	public void run()
	{
		try
		{
			// Loop until the bot disconnects from the server.
			while (isConnected())
			{
				String s = _manager.recvRaw();
				if (s != null)
				{
					String[] split = s.split(" ");
					
					if (split[0].equalsIgnoreCase("PING"))
					{
                                                String pongMsg = split[1].contains(":") ? split[1].substring(1) : split[1];
                                                System.out.println("PONG: " + pongMsg);
						_manager.sendPong(pongMsg);
					}
					
					synchronized (lock) {
						_handler.handle(s);
					}
				}
				else
				{
					System.out.println("Disconnected from server.");
					break;
				}
			}
		} catch (IOException e)
		{/*
		 * :( we've Disconnected, or we've been disconnected. Just return.
		 */
			System.err.println("Disconnected unexpectedly: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public void setHandler(InputHandler newIn)
	{
		synchronized (lock) 
		{
			_handler = newIn;
		}
	}

	public boolean isConnected()
	{
		return _manager.isConnected();
	}

	private IRCSocketManager _manager;
	private InputHandler _handler;

}
