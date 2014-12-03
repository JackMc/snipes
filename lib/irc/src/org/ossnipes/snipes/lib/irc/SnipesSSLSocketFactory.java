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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.ossnipes.snipes.lib.events.IRCBase;

/**
 * The default socket factory for all secure IRC operations within the bot.
 * 
 * The most common use for this class would probably be as a argument to
 * {@link IRCBase#connect(String, int, SocketFactory)}with the third argument
 * being SnipesSSLSocketFactory.getDefault(). The port would generally be 6697
 * (the default IRC SSL port).
 * 
 * The reasoning for using a factory and not a straight new Socket(host,port) is
 * because with this approach, we can have certain options set on by default
 * with the #createSocket method (also to move more towards the "singleton"
 * method).
 * 
 * If you wish to use SSL when connecting to a server, try
 * {@link SnipesSSLSocketFactory}
 * 
 * @since Snipes 0.6 Jack McCracken (<a
 *        href="http://ossnipes.org/">http://ossnipes.org</a>)
 * @see SnipesSSLSocketFactory
 */

public class SnipesSSLSocketFactory extends SocketFactory implements
                                                          BotConstants
{
    // This class might be considered a hack for SSL compatibility, as there's
    // no documentation on
    // how to actually *use* SSLSockets.
    // So, we use a SSLSocketFactory to create our sockets, and just set the
    // timeout on that.

    private static final SocketFactory parent = SSLSocketFactory.getDefault();

    // The default, returned by getDefault()
    private static final SnipesSSLSocketFactory def = new SnipesSSLSocketFactory();



    private SnipesSSLSocketFactory()
    {

    }

    @Override
    public Socket createSocket(String host, int port) throws IOException,
	UnknownHostException
    {
        Socket s = parent.createSocket(host, port);
        s.setSoTimeout(IRC_TIMEOUT);
        return s;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException
    {
        Socket s = parent.createSocket(host, port);
        s.setSoTimeout(IRC_TIMEOUT);
        return s;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost,
                               int localPort) throws IOException, UnknownHostException
    {
        Socket s = parent.createSocket(host, port, localHost, localPort);
        s.setSoTimeout(IRC_TIMEOUT);
        return s;
    }

    @Override
    public Socket createSocket(InetAddress address, int port,
                               InetAddress localAddress, int localPort) throws IOException
    {
        Socket s = parent.createSocket(address, port, localAddress, localPort);
        s.setSoTimeout(IRC_TIMEOUT);
        return s;
    }

    public static SocketFactory getDefault()
    {
        return def;
    }

}
