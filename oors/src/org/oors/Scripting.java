package org.oors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Scripting {

	private ScriptEngineManager mgr;

	private Scripting()
	{
		mgr = new ScriptEngineManager();
	}
	
	private static Scripting instance = null;
	
	public static synchronized Scripting getInstance()
	{
		if ( instance==null ) instance = new Scripting();
		return instance;
	}
	
	public synchronized Invocable executeScript( String filename, Reader r, final ScriptLog log, Collection<ScriptingEnvironmentVariable> vars )
	{
		return executeScript(filename,null,r,log,vars);
	}

	public synchronized Invocable executeScript( Reader r, final ScriptLog log, Collection<ScriptingEnvironmentVariable> vars )
	{
		return executeScript(null,null,r,log,vars);
	}
	
	public synchronized Invocable executeScript( String filename, String script, final ScriptLog log, Collection<ScriptingEnvironmentVariable> vars )
	{
		return executeScript(filename,script,null,log,vars);
	}

	public synchronized Invocable executeScript( String script, final ScriptLog log, Collection<ScriptingEnvironmentVariable> vars )
	{
		return executeScript(null,script,null,log,vars);
	}
	
	private synchronized Invocable executeScript( String filename, String script, Reader r, final ScriptLog log, Collection<ScriptingEnvironmentVariable> vars )
	{
		ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
		jsEngine.put("datasource", DataSource.getInstance());
//		jsEngine.put("console", log);

		for ( ScriptingEnvironmentVariable v : vars )
		{
			jsEngine.put(v.name, v.var);
		}

		jsEngine.getContext().setWriter(new PrintWriter(new Writer()
		{
			String s = "";

			@Override
			public void write(char[] cbuf, int off, int len)
					throws IOException
			{
				for (int i = 0; i < len; i++)
				{
					s += cbuf[off + i];
				}
			}

			@Override
			public void flush() throws IOException
			{
				log.msg(s);
				s = "";
			}

			@Override
			public void close() throws IOException
			{
			}

		}));
		jsEngine.getContext().setErrorWriter( new PrintWriter(new Writer()
		{
			String s = "";

			@Override
			public void write(char[] cbuf, int off, int len)
					throws IOException
			{
				for (int i = 0; i < len; i++)
				{
					s += cbuf[off + i];
				}
			}

			@Override
			public void flush() throws IOException
			{
				log.err(s);
				s = "";
			}

			@Override
			public void close() throws IOException
			{
			}

		}));

		try
		{
			if ( script!=null ) jsEngine.eval(script);
			else jsEngine.eval(r);
		}
		catch (ScriptException ex)
		{
			log.err("Exception: " + ex.getMessage());
			log.err("Line : " + ex.getLineNumber());
		}
		
		return (Invocable)jsEngine;
	}
}
