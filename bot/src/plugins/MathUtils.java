/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package plugins;

import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;

import java.util.Map;
import java.util.HashMap;

public class MathUtils extends Module
{	
	private interface MathOperation
	{
		public double calculate(double[] values);
		public int getNumArgs();
	}
	
	
	@Override
	protected ModuleReturn snipesInit()
	{
		_operations.put("add", new MathOperation() {
			public double calculate(double[] values)
			{
				return values[0] + values[1];
			}
			public int getNumArgs()
			{
				return 2;
			}});

		_operations.put("sub", new MathOperation() {
			public double calculate(double[] values)
			{
				return values[0] - values[1];
			}
			public int getNumArgs()
			{
				return 2;
			}});
		
		_operations.put("mul", new MathOperation() {
			public double calculate(double[] values)
			{
				return values[0] * values[1];
			}
			public int getNumArgs()
			{
				return 2;
			}});

		_operations.put("div", new MathOperation() {
			public double calculate(double[] values)
			{
				if (values[1] == 0)
				{
					throw new IllegalArgumentException("Cannot divide by zero.");
				}

				return values[0] / values[1];
			}

			public int getNumArgs()
			{
				return 2;
			}
		});

		_operations.put("pow", new MathOperation() {
			public double calculate(double[] values)
			{
				return Math.pow(values[0], values[1]);
			}
			public int getNumArgs()
			{
				return 2;
			}});

		_operations.put("sin", new MathOperation() {
			public double calculate(double[] values)
			{
				return Math.sin(values[0]);
			}

			public int getNumArgs()
			{
				return 1;
			}
		});

		_operations.put("cos", new MathOperation() {
			public double calculate(double[] values)
			{
				return Math.cos(values[0]);
			}

			public int getNumArgs()
			{
				return 1;
			}
		});

		_operations.put("sqrt", new MathOperation() {
			public double calculate(double[] values)
			{
				if (values[0] == 0)
				{
					throw new IllegalArgumentException("Can't square-root zero.");
				}
				else if (values[0] < 0)
				{
					throw new IllegalArgumentException("That's just too...complex ;)");
				}
				return Math.sqrt(values[0]);
			}

			public int getNumArgs()
			{
				return 1;
			}
		});

		return ModuleReturn.NORMAL;
	}

	private double parseMathExp(String exp)
	{
		if (Character.isDigit(exp.charAt(0)) || exp.charAt(0) == '-')
		{
			try
			{
				return Double.parseDouble(exp);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Ill-formatted double");
			}
		}
		else if (exp.contains("(") && exp.endsWith(")"))
		{
			String[] split = exp.split("\\(");
			// We interpret it as a function
			String name = split[0];
			String args = exp.split("^" + name + "\\(")[1].replaceAll("\\)$", "");

			System.err.println("Final split: " + args);
			
			if (!_operations.containsKey(name))
			{
				throw new IllegalArgumentException("Function " + name + " doesn't exist.");
			}
			
			StringBuilder buffer = new StringBuilder();

			boolean bracketBreak = false;
			
			for (char c : args.toCharArray())
			{
				if (c == '(')
				{
					bracketBreak = true;
				}
				if (c == ')')
				{
					bracketBreak = false;
				}
				if (c == ',' && !bracketBreak)
				{
					buffer.append("\n");
					continue;
				}
				if (c == ' ')
				{
					// Ignore all spaces...
					continue;
				}

				buffer.append(c);
			}

			System.err.println(buffer.toString());

			MathOperation mo = _operations.get(name);

			String[] argsToParse = buffer.toString().split("\n");

			if (argsToParse.length != mo.getNumArgs())
			{
				throw new IllegalArgumentException("Wrong number of arguments to func " + name);
			}

			double[] parsedArgs = new double[argsToParse.length];

			for (int i = 0; i < argsToParse.length; i ++)
			{
				parsedArgs[i] = parseMathExp(argsToParse[i]);
			}

			return mo.calculate(parsedArgs);
		}
		else if (exp.equalsIgnoreCase("pi"))
		{
			return Math.PI;
		}
		else if (exp.equalsIgnoreCase("e"))
		{
			return Math.E;
		}
		else
		{
			throw new IllegalArgumentException("Constant " + exp + " not found...");
		}
	}
	
	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		if (ev == Event.IRC_PRIVMSG &&
			args.getParamAsString("message").startsWith(
				this.getParent().getNick() + ": calc "))
		{
			// Remove the name at the start.
			String toParse = args.getParamAsString("message").replace(this.getParent().getNick() + ": calc ", "");
			try
			{
				this.getParent().sendPrivMsg(args.getParamAsString("sendto"), "Result: " + parseMathExp(toParse));
			} catch (IllegalArgumentException e)
			{
				this.getParent().sendPrivMsg(args.getParamAsString("sendto"), "Error while parsing/calculating: " + e.getMessage());
			}
		}
	}

	Map<String, MathOperation> _operations = new HashMap<>();
}
