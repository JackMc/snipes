/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.ossnipes.snipes.bot;

/** An abstract superclass that represents that a problem has occurred with the
 * operation of the Snipes Module system or a Snipes Module; or in the loading
 * of one.
 * 
 * @author Jack McCracken (Auv5) */
public abstract class ModuleException extends Exception
{

	private static final long serialVersionUID = -5654904272122942983L;

	public ModuleException()
	{
		// Redundant
	}

	public ModuleException(String message)
	{
		super(message);
	}

	public ModuleException(Throwable cause)
	{
		super(cause);
	}

	public ModuleException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
