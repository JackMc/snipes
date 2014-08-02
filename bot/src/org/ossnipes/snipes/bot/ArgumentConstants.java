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

/** Contains the names and default values of several Snipes arguments.
 * 
 * @author Jack McCracken (Auv5)
 * 
 * @since Snipes 0.01 */
interface ArgumentConstants
{
	/** The prefix for short arguments (-D, -v, etc.) */
	public static final char ARG_PREFIX = '-';
	/** The prefix for all 'wordy' arguments (--define, --verbose, etc) */
	public static final String WORDY_ARG_PREFIX = ARG_PREFIX
			+ String.valueOf(ARG_PREFIX);

	public static final String DEFINE_L_ARG_NAME = WORDY_ARG_PREFIX + "define";
	public static final char DEFINE_S_ARG_NAME = 'D';
	public static final String VERBOSE_L_ARG_NAME = WORDY_ARG_PREFIX
			+ "verbose";
	public static final char VERBOSE_S_ARG_NAME = 'v';
	public static final String VERSION_L_ARG_NAME = WORDY_ARG_PREFIX
			+ "version";
	public static final String ALT_CONFIG_L_ARG_NAME = WORDY_ARG_PREFIX
			+ "config";
	public static final char ALT_CONFIG_S_ARG_NAME = 'c';
}
