package org.ossnipes.snipes.bot.tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ossnipes.snipes.bot.ArgumentParser;
import org.ossnipes.snipes.bot.Configuration;

public class ArgumentParserTest
{

	ArgumentParser inst;
	Configuration c;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		// None
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		// None
	}

	@Before
	public void setUp() throws Exception
	{
		// To make sure tests don't affect each
		// other.
		this.inst = ArgumentParser.getParser(true);
		this.c = new Configuration();
	}

	@After
	public void tearDown() throws Exception
	{
		this.inst = null;
		this.c = null;
	}

	@Test
	public void testParseArgs()
	{
		// Example arguments to Snipes
		this.inst.parseArgs(this.c, new String[]
		{ "--define", "test=testproperty", "-vD", "testprop=testpropval" });

		if (this.c.getProperty("test") == null
				|| !this.c.getProperty("test").equals("testproperty"))
		{
			fail("property \"test\" does not equal expected value \"testproperty\". ");
		}

		if (this.c.getProperty("testprop") == null
				|| !this.c.getProperty("testprop").equals("testpropval"))
		{
			fail("property \"testprop\" does not equal expected value \"testpropval\"");
		}

		if (this.c.getProperty("verbose") == null
				|| !this.c.getProperty("verbose").equalsIgnoreCase("TRUE"))
		{
			fail("property \"verbose\" does not equal expected value \"TRUE\" in any case.");
		}
	}
}
