package plugins.bouncer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.lib.events.Channel;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;
import org.ossnipes.snipes.lib.events.IRCEventListener;

public class BouncerConnection extends Thread implements IRCEventListener
{
	public BouncerConnection(SnipesBot bot, Socket s)
	{
		this._bot = bot;
		this._isAuthed = false;
		this._sock = s;

		this.start();
	}

	@Override
	public void run()
	{
		// Doesn't really need to be done after starting up, but speeds things
		// up getting back to the caller.
		this.populatePseudoClientList();

		try
		{
			this._in = new BufferedReader(new InputStreamReader(this._sock
					.getInputStream()));
			this._out = new PrintStream(this._sock.getOutputStream());
		} catch (IOException e)
		{
			System.err
					.println("Snipes bouncer: Could not get socket I/O streams.");
			return;
		}

		// TODO: Some I/O to connect to the client (accept user (throw away),
		// nick (throw away) and send the startup packets (look at GS's
		// connection using ExampleBot (fix that too)

		try
		{
			this.loop();
		} catch (IOException e)
		{
			System.err
					.println("Snipes bouncer: unable to read line from server.");
		}
	}

	private void loop() throws IOException
	{
		String nick = this._in.readLine().split(" ")[1];
		String line;
		this._out.println(":" + nick + " NICK " + this._bot.getNick());
		for (Channel chan : this._bot.getJoinedChannels())
		{
			this.sendRawLineToClient(":" + this._bot.getNick() + " JOIN "
					+ chan.getName());

		}
		while ((line = this._in.readLine()) != null)
		{
			System.err.println(line);
			for (PseudoClient pc : this._clients)
			{
				if (pc.isLineTo(line, this))
				{
					pc.performCommand(line, this);
					// Lines can only go to one client.
					break;
				}
			}
		}
	}

	private void populatePseudoClientList()
	{
		OUTER: for (EnumPseudoClient epc : EnumPseudoClient.values())
		{
			Class<? extends PseudoClient> clazz = epc.getClientClass();

			Constructor<?>[] constructors = clazz.getDeclaredConstructors();

			// Impossible case.
			if (constructors.length == 0)
			{
				// I saw ArrayList do this for an impossible case, so I'm going
				// to assume it's correct.
				throw new InternalError();
			}

			Constructor<?> constructor = null;

			INNER: for (Constructor<?> c : constructors)
			{
				Class<?>[] paramTypes = c.getParameterTypes();

				if (paramTypes.length > 1 ||
				// Nice impossible case you have there.
						paramTypes.length < 0)
				{
					continue INNER;
				}

				if (paramTypes.length == 0 || paramTypes[0] == SnipesBot.class)
				{
					constructor = c;
					break INNER;
				}
			}

			if (constructor == null)
			{
				// *This is not the class you are looking for* :)
				System.err
						.println("Snipes bouncer: Could not reflectively instantiate pseudo-client "
								+ clazz.getName()
								+ ". It does not have a constructor taking a SnipesBot as it's only argument or no argument.");
				continue OUTER;
			}
			else
			{
				// Initialise the Object and stick it in the list.
				try
				{
					int modifiers = constructor.getModifiers();
					if (Modifier.isPublic(modifiers)
							|| (this.getClass().getPackage().equals(
									this.getClass().getPackage()) && !Modifier
									.isPrivate(modifiers)))
					{
						// No need to check cast, it came from a ? extends
						// PsuedoClient Class object.
						this._clients
								.add(constructor.getParameterTypes().length == 0 ? (PseudoClient) constructor
										.newInstance()
										: (PseudoClient) constructor
												.newInstance(new Object[]
												{ this._bot }));
					}
					else
					{
						System.err
								.println("Snipes Bouncer: Bouncer client class "
										+ clazz.getName()
										+ "'"
										+ (clazz.getName().endsWith("s") ? ""
												: "s")
										+ " has a constructor that is inaccessable via normal access. Fortunately, we can bypass this using reflection. Please make the constructor accessible to this class ("
										+ this.getClass().getName()
										+ ") to stop this message from appearing.");
						constructor.setAccessible(true);
					}
				} catch (IllegalArgumentException e)
				{
					// We already checked the arguments.
					System.err
							.println("Snipes bouncer: Imposible case. Please check what universe you are in.");
					throw new InternalError();
				} catch (InstantiationException e)
				{
					System.err
							.println("Invalid class. It is either an array class, an abstract class or something that cannot be initialised by us mere mortals (bow down to the almighty JVM!)");
				} catch (IllegalAccessException e)
				{
					System.err
							.println("Snipes bouncer: Constructor is private, protected or package-private. Cannot call constructor.");
				} catch (InvocationTargetException e)
				{
					System.err
							.println("Snipes bouncer: Constructor throws a exception. Cannot load PseudoClient "
									+ clazz.getName() + ".");
				}
			}
		}
	}

	public boolean isAuthed()
	{
		return this._isAuthed;
	}

	public void setAuthed(boolean val)
	{
		this._isAuthed = val;
	}

	public void sendRawLineToClient(String line)
	{
		this._out.println(line);
	}

	public void sendRawLineToServer(String line)
	{
		this._bot.sendRaw(line);
	}

	@Override
	public Event[] getRegisteredEvents()
	{
		return Event.values();
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		if (ev == Event.IRC_RESPONSE_CODE)
		{
			// System.err.println("Response code!! "
			// + args.getParamAsString("line"));
		}
		System.err.println("Line!: " + args.getParamAsString("line"));
		// We don't want to double-pong.
		if (ev != Event.IRC_PING)
		{
			// System.err.println("? " + ev);
			this.sendRawLineToClient(args.getParamAsString("line"));
		}
	}

	private boolean _isAuthed;
	private final SnipesBot _bot;
	private final List<PseudoClient> _clients = new ArrayList<PseudoClient>();
	private final Socket _sock;
	private PrintStream _out;
	private BufferedReader _in;
}
