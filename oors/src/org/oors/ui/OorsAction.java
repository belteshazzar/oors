// Copyright (c) 2011, OORS contributors
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// * Neither the name of the OORS Project nor the
// names of its contributors may be used to endorse or promote products
// derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL CONTRIBUTORS OF THE OORS PROJECT BE LIABLE FOR
// ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.oors.ui;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.script.Invocable;
import javax.script.ScriptException;
import javax.swing.AbstractAction;

import org.oors.DataSource;
import org.oors.Scripting;
import org.oors.ScriptingEnvironmentVariable;

public class OorsAction extends AbstractAction
{
	private static final long	serialVersionUID	= 1376310550496698954L;
	private final static Logger	LOG			= Logger.getLogger(OorsAction.class
			.getName());

	private String scriptFilename;

	public OorsAction(String scriptFilename)
	{
		super();

		this.scriptFilename = scriptFilename;

		Reader in;
		try
		{
			in = new FileReader(scriptFilename);
		}
		catch (FileNotFoundException e)
		{
			LOG.severe("Script for action not found: " + scriptFilename);
			return;
		}

		Scripting.getInstance().executeScript(scriptFilename, in, null, createEnvironment());
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Reader in;
		try
		{
			in = new FileReader(scriptFilename);
		}
		catch (FileNotFoundException fnfex)
		{
			LOG.severe("Script for action not found: " + scriptFilename);
			return;
		}

		Invocable invocable = Scripting.getInstance().executeScript(scriptFilename, in, (Oors.getInstance()==null?null:Oors.getInstance().script), createEnvironment());
		if ( invocable==null ) return;

		try
		{
			invocable.invokeFunction("actionPerformed");
		}
		catch (NoSuchMethodException e1)
		{
			LOG.severe("Didn't find actionPerformed function in script file: " + scriptFilename);
		}
		catch (ScriptException e1)
		{
			LOG.severe("Script exception calling actionPerformed in action script: " + scriptFilename+", line: " +e1.getLineNumber());
		}
	}
	
	private List<ScriptingEnvironmentVariable> createEnvironment()
	{
		List<ScriptingEnvironmentVariable> env = new LinkedList<ScriptingEnvironmentVariable>();

		env.add( new ScriptingEnvironmentVariable("gui", Oors.getInstance()));
		env.add( new ScriptingEnvironmentVariable("datasource", DataSource.getInstance()));
		env.add( new ScriptingEnvironmentVariable("selection", Actions.getInstance().getSelection()));
		env.add( new ScriptingEnvironmentVariable("actions", Actions.getInstance()));

		return env;
	}

	@Override
	public boolean isEnabled()
	{
		Reader in;
		try
		{
			in = new FileReader(scriptFilename);
		}
		catch (FileNotFoundException fnfex)
		{
			LOG.severe("Script for action not found: " + scriptFilename);
			return false;
		}

		Invocable invocable = Scripting.getInstance().executeScript(scriptFilename, in, (Oors.getInstance()==null?null:Oors.getInstance().script), createEnvironment());
		if ( invocable==null ) return false;

		try
		{
			Object r = invocable.invokeFunction("isEnabled");

			if (r instanceof Boolean)
			{
				return (Boolean) r;
			}
			else
			{
				LOG.severe("isEnabled didn't return boolean, script file: " + scriptFilename);
				return false;
			}
		}
		catch (NoSuchMethodException e1)
		{
			LOG.severe("Didn't find isEnabled function in script file: " + scriptFilename);
			return false;
		}
		catch (ScriptException e1)
		{
			LOG.severe("Script exception calling isEnabled in action script: " + scriptFilename+", line: " +e1.getLineNumber());
			return false;
		}
	}

}
