/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package plugins.noircman;

import java.io.IOException;

public interface Connection
{
    public void send(String data);

    public void sendln(String data);

    public void send(int data) throws IOException;

    public void send(double data) throws IOException;

    public void send(char data) throws IOException;

    public void send(float data) throws IOException;

    public void send(Object o) throws IOException;

    public String recv() throws IOException;

    public int recvInt() throws IOException;

    public double recvDouble() throws IOException;

    public char recvChar() throws IOException;

    public float recvFloat() throws IOException;

    public Object recvObject() throws IOException, ClassNotFoundException;

    public void close();
}
