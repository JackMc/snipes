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

public enum Exit
{
    /** Indicates that Snipes exited under normal conditions. */
    EXIT_NORMAL,
    /** Indicates that the argument syntax was incorrect, and Snipes is exitting. */
    EXIT_INCORRECTARGSSYN,
    /** Indicates that Snipes was unable to load the configuration file, and is
     * exiting. */
    EXIT_CONFIGNOLOAD,
    /** Indicates that the configuration provided to Snipes was invalid (missing  
     * or invalid property, ill-formated configuration file) */
    EXIT_INVALIDCONFIG,
    /** Indicates that Snipes could not connect to the specified server, and is
     * exiting. */
    EXIT_COULDNOTCONNECT,
    /** Indicates that this JVM's version is too low for Snipes to operate on. */
    EXIT_JVM_VERSION_TOO_LOW,
    /** Indicates that a copy of the Snipes IRC library could not be found on the
     * CLASSPATH, and that Snipes is exiting. */
    EXIT_NO_SNIRC
}
