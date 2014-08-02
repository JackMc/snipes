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

/** Represents a permission that it is possible for a module to be loaded with.
 * 
 * @author Jack McCracken (Auv5) */
public enum ModulePermission
{
	/** Represents the ability to hook (subscribe to) events other than the
	 * Events in the default {@link Event} enumeration. */
	CAN_HOOK_INTERNAL,
	/** Represents the ability to intercept events before they get to the events
	 * loaded after it. */
	CAN_CANCEL_EVENTS
}
