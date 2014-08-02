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

/** Signals that there was a error when loading a Snipes module.
 * 
 * @author Jack McCracken (Auv5)
 * @since Snipes 0.01 */
public class ModuleLoadException extends ModuleException
{
	private static final long serialVersionUID = -3410734904488958352L;

	public ModuleLoadException()
	{
		// Redundant
	}

	public ModuleLoadException(String message)
	{
		super(message);
	}

	public ModuleLoadException(Throwable cause)
	{
		super(cause);
	}

	public ModuleLoadException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
