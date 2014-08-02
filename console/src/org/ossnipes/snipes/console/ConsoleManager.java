/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.ossnipes.snipes.console;

import java.util.Scanner;

public class ConsoleManager 
{
	
	public ConsoleManager(Scanner s)
	{
		_s = s;
	}
	// Depreciated so I don't not give messages.
	@Deprecated
	public String getConsoleLine()
	{
		return getConsoleLine("");
	}
	
	public String getConsoleLine(String msg)
	{
		System.out.print(msg);
		return _s.nextLine();
	}
	
	public void println(String msg)
	{
		System.out.println(msg);
	}
	Scanner _s;
}
