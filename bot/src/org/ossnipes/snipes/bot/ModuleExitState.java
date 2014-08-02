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

/** Signals to a module under what circumstances it is being unloaded.
 * 
 * @author Jack McCracken (Auv5) */
public enum ModuleExitState
{
	/** Signals that the module was unloaded with a command or automatically by
	 * the bot (not at shutdown). */
	EXIT_UNLOAD,
	/** Signals that the module was unloaded because the bot is quitting. */
	EXIT_QUITTING,
	/** Signals that the bot does not know why the module was unloaded. */
	EXIT_UNKNOWN
}
