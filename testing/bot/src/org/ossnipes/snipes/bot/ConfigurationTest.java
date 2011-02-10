package org.ossnipes.snipes.bot;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigurationTest
{
	Configuration c;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
		this.c = new Configuration();
	}

	@After
	public void tearDown() throws Exception
	{
		this.c = null;
	}

	@Test
	public void testConfigurationProperties()
	{
		// Use a java.utils.Properties Object, as to not rely on the
		// Configuration version of the method.
		Properties p = new Properties();
		p.setProperty("testprop", "testpropval");
		this.c = new Configuration(p);
		String prop = this.c.getProperty("testprop");
		if (prop == null || !prop.equals("testpropval"))
		{
			fail(prop + " != " + "testpropval");
		}
	}

	/* @Test public void testLoadInputStream() { // Snipes should be able to
	 * load normal java.utils.Properties store() // generated lists, so we can
	 * just load it using that. Properties testProps = new Properties();
	 * testProps.setProperty("testprop", "testpropvalue");
	 * 
	 * InputStream is = null; OutputStream os = null; try { // Create a "mock"
	 * enviroment for the two to store/load on. is = new PipedInputStream(); os
	 * = new PipedOutputStream((PipedInputStream) is); } catch (IOException e) {
	 * fail("Could not connect PipedOutputStream and PipedInputStream."); }
	 * 
	 * try { // No need for null checks, the // test will fail.
	 * System.out.println("Storing..."); testProps.store(os, ""); } catch
	 * (IOException e) { fail("Could not store Properties in OutputStream."); }
	 * 
	 * try { System.out.println("Loading..."); this.c.load(is); } catch
	 * (IOException e) { fail("Could not load configuration properties. Error: "
	 * + e.getMessage() + "."); } System.out.println("Done.");
	 * 
	 * } */

	@Test
	public void testGetPropertyString()
	{
		this.c.setProperty("pie", "cheezburger");

		String s = this.c.getProperty("pie");

		if (s == null || !s.equals("cheezburger"))
		{
			fail("property \"pie\" was not set or was not set to \"cheezburger\".");
		}
	}

	@Test
	public void testGetPropertyStringString()
	{

		String s = this.c.getProperty("pie", "cheezburger");

		if (s == null || !s.equals("cheezburger"))
		{
			fail("Unset property \"pie\"'s default value \"cheezburger\" did not return.");
		}
	}

	@Test
	public void testStoreOutputStreamString()
	{
		this.c.setProperty("prop", "No");
		this.c.setProperty("pizza", "KFC");
		this.c.setProperty("s", "SS");

		PipedInputStream in = new PipedInputStream();
		PipedOutputStream out = null;
		try
		{
			out = new PipedOutputStream(in);
		} catch (IOException e)
		{
			fail("Could not connect PipedInputStream to PipedOutputStream.");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try
		{
			this.c.store(out, null);
			String s;
			while ((s = br.readLine()) != null)
			{
				System.out.println(s);
			}
		} catch (IOException e)
		{
			fail("Could not store properties");
		}
	}

	@Test
	public void testSetTempProperty()
	{
		this.c.setTempProperty("pie", "cheezburger");

		String s = this.c.getProperty("pie");

		if (s == null || !s.equals("cheezburger"))
		{
			fail("Temporary property \"pie\" was not set or was not set to \"cheezburger\"");
		}
	}

	@Test
	public void testClearTemps()
	{
		this.c.setTempProperty("pie", "cheezburger");
		this.c.clearTemps();
		String s = this.c.getProperty("pie");
		if (s != null)
		{
			fail("Temporary \"pie\" property persists after clearTemps() call.");
		}
	}

}
