/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.ossnipes.snipes.lib.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Wrapper class for arguments to Snipes events. 
 * @author jack 
 */
public class EventArgs
{
	private Map<String,Object> _params = null;
	private Event _ev;
	/** Constructs a event arguments object with no keys.
	 */
	public EventArgs(Event ev, String line)
	{
		_params = new HashMap<String,Object>();
		_ev = ev;
		_params.put("line", line);
	}
	
	public Event getEvent()
	{
		return _ev;
	}
	
	/** Converts the two specified arrays and uses the keys array as the keys for the parameters, and 
	 * uses the values array as the values of the Strings of the keys array with the same index.
	 * @param keys An array of keys, must be the same size as values.
	 * @param values An array of values, must be the same size as keys.
	 * @throws IllegalArgumentException If keys.length != values.length or if keys ==null || values == null.
	 */
	public EventArgs(Event ev, String line, String[] keys, String[] values)
	{
		if (keys == null || values == null)
			throw new IllegalArgumentException("Keys/values cannot be null.");
		_params = BotUtils.stringObjectArraysToStringObjectMap(keys, values);
		_params.put("line", line);
		_ev = ev;
	}
	
	public boolean containsKey(String key)
	{
		return _params.containsKey(key);
	}
	
	/** Creates a EventArgs object with the specified arguments as it's parameters.
	 * @param args The String Object map of the arguments.
	 */
	public EventArgs(Event ev, String line, Map<String, Object> args)
	{
		if (args == null)
		{
			throw new IllegalArgumentException("args cannot be null.");
		}
		this._params = args;
		_params.put("line", line);
		_ev = ev;
	}
	/** Gets the parameter with the specified key.
	 * @return The value of the parameter. Null if there's no mapping.
	 */
	public Object getParam(String key)
	{
		return _params.get(key);
	}
	/** Gets all the keys in this args set.<BR/>
	 * A common use of this would be: <BR/><BR/>
	 * <code>for (String s : args.getKeySet()) {<BR/>
	 * someList.add(args.getParam(s));<BR/>
	 * }<BR/><BR/>
	 * </code>
	 * Which would add all the values in the args set to someList (or whatever list
	 *  you replaced it with.)
	 * @return A set of all the keys in the args set.
	 */
	public Set<String> getKeySet()
	{
		return _params.keySet();
	}

	/** Retrieves a event parameter as a String, using the {@link Object#toString()} method (so that it is more typesafe than
	 * casting to a String). This is just a convenience method, and is the same as {@link #getParam(String)}.toString().
	 * 
	 * @param key The key to retrieve the value of.
	 * @return The value of the parameter with the specified key.
	 */
	public String getParamAsString(String key)
	{
		Object o = getParam(key);
		if (o == null)
		{
			return null;
		}
		return o.toString();
	}
}
