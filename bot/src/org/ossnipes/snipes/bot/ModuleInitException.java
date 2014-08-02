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

public class ModuleInitException extends ModuleException
{
	private static final long serialVersionUID = 5671671541122272824L;

	public ModuleInitException(Module erroredModule)
	{
		this.setErroredModule(erroredModule);
	}

	public ModuleInitException(String message, Module erroredModule)
	{
		super(message);
		this.setErroredModule(erroredModule);
	}

	public ModuleInitException(Throwable cause, Module erroredModule)
	{
		super(cause);
		this.setErroredModule(erroredModule);
	}

	public ModuleInitException(String message, Throwable cause,
			Module erroredModule)
	{
		super(message, cause);
		this.setErroredModule(erroredModule);
	}

	public Module getModule()
	{
		return this._erroredModule;
	}

	private void setErroredModule(Module erroredModule)
	{
		this._erroredModule = erroredModule;
	}

	private Module _erroredModule;

}
