//	Copyright (c) 2011, OORS contributors
//	All rights reserved.
//	
//	Redistribution and use in source and binary forms, with or without
//	modification, are permitted provided that the following conditions are met:
//	    * Redistributions of source code must retain the above copyright
//	      notice, this list of conditions and the following disclaimer.
//	    * Redistributions in binary form must reproduce the above copyright
//	      notice, this list of conditions and the following disclaimer in the
//	      documentation and/or other materials provided with the distribution.
//	    * Neither the name of the OORS Project nor the
//	      names of its contributors may be used to endorse or promote products
//	      derived from this software without specific prior written permission.
//	
//	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
//	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//	DISCLAIMED. IN NO EVENT SHALL CONTRIBUTORS OF THE OORS PROJECT BE LIABLE FOR ANY
//	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
//	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
//	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
//	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
//	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
//	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.oors.ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.oors.DataSource;
import org.oors.Document;
import org.oors.ProjectBranch;
import org.oors.ScriptLog;
import org.oors.Scripting;
import org.oors.ScriptingEnvironmentVariable;
import org.oors.ui.document.DocumentFrame;
import org.oors.ui.explorer.ExplorerFrame;
import org.oors.ui.help.HelpPanel;
import org.oors.ui.scripting.ScriptPanel;

import com.javadocking.DockingExecutor;
import com.javadocking.DockingManager;
import com.javadocking.dock.Position;
import com.javadocking.dock.SplitDock;
import com.javadocking.dock.TabDock;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockableState;
import com.javadocking.dockable.DockingMode;
import com.javadocking.dockable.StateActionDockable;
import com.javadocking.dockable.action.DefaultDockableStateAction;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.event.DockingEvent;
import com.javadocking.event.DockingListener;
import com.javadocking.model.FloatDockModel;
import com.javadocking.visualizer.SingleMaximizer;

public class Oors extends JFrame
{
	private static final long serialVersionUID = -2329871747556767097L;
	private final static Logger LOG = Logger.getLogger(Oors.class
			.getName());
	
	public ProjectBranch getBranch()
	{
		return branch;
	}

	private ProjectBranch branch;
	private static Oors instance = null;
	private SplitDock splitDock;
	private TabDock tabDock;
	private Dockable expDock;
	private SingleMaximizer maximizePanel;

	 ScriptPanel script;

	private HelpPanel helpPane;
	
	public HelpPanel getHelp()
	{
		return helpPane;
	}

	private void quit()
	{
		DataSource.getInstance().close();
		System.exit(0);
	}

	public static Oors getInstance()
	{
		return instance;
	}

	public void open(Document document)
	{
		final DocumentFrame doc = new DocumentFrame(document);
		Dockable dock = addActions(new DefaultDockable("DOcument", doc, "Doc",
				null, DockingMode.ALL - DockingMode.FLOAT));
		dock.addDockingListener(new DockingListener()
		{

			@Override
			public void dockingChanged(DockingEvent de)
			{
				LOG.info("dockingChanged");
				LOG.info(" - " + de.getDestinationDock());
				if (de.getDestinationDock() == null)
				{
					Actions.getInstance().removeSelectionListener(
							doc.getDocumentTree());
					Actions.getInstance().removeSelectionListener(
							doc.getDocumentTable());
				}
			}

			@Override
			public void dockingWillChange(DockingEvent de)
			{
			}
		});

		DockingExecutor de = new DockingExecutor();
		de.changeDocking(dock, splitDock);

		if (expDock.getState() == DockableState.MAXIMIZED)
		{
			DefaultDockableStateAction restoreAction = new DefaultDockableStateAction(
					expDock, DockableState.NORMAL);
			restoreAction.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, "Restore"));
		}
	}

	private Oors()
	{
		super("Oors");
		
		Logger logger = Logger.getLogger("");
		ConsoleHandler logHandler = new ConsoleHandler();
		SimpleFormatter logFormatter = new SimpleFormatter();
		logHandler.setFormatter(logFormatter);
		logger.addHandler(logHandler);

		instance = this;
		this.setSize(800, 600);

		try
		{
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		}
		catch (UnsupportedLookAndFeelException e)
		{
			// handle exception
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// handle exception
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			// handle exception
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// handle exception
			e.printStackTrace();
		}
//		setUIFont (new javax.swing.plaf.FontUIResource
//			       ("Tahoma",Font.PLAIN,16));

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				quit();
			}
		});

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		splashMsg("loading data source");

		try
		{
			branch = DataSource
					.getInstance()
					.getProjects()
					.iterator()
					.next()
					.getProjectBranches()
					.iterator()
					.next();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		splashMsg("creating gui");

//		JPanel status = new JPanel();
//		status.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
//		status.setPreferredSize(new Dimension(100, 20));
//		this.getContentPane().add(status, BorderLayout.SOUTH);

		splashMsg("creating help");

		helpPane = new HelpPanel();
		Dockable helpDock = addExpActions(new DefaultDockable("Help", helpPane,
				"Help", null, DockingMode.ALL - DockingMode.FLOAT));

		splashMsg("creating scripting");

		script = new ScriptPanel();
		Dockable scriptDock = addExpActions(new DefaultDockable("Scripting", script,
				"Scripting", null, DockingMode.ALL - DockingMode.FLOAT));

		splashMsg("creating explorer");

		ExplorerFrame explorer = new ExplorerFrame(branch);
		expDock = addExpActions(new DefaultDockable("Explorer", explorer,
				"Project Branch Explorer", null, DockingMode.ALL
						- DockingMode.FLOAT));

		// Create the dock model for the docks.
		FloatDockModel dockModel = new FloatDockModel();
		dockModel.addOwner("frame0", this);

		// Give the dock model to the docking manager.
		DockingManager.setDockModel(dockModel);

		// Create the child tab dock.
		tabDock = new TabDock();

		// Add the dockables to the tab dock.
		tabDock.addDockable(helpDock, new Position(2));
		tabDock.addDockable(scriptDock, new Position(1));
		tabDock.addDockable(expDock, new Position(0));

		// Create the split dock.
		splitDock = new SplitDock();

		// Add the child dock in the center of the split dock.
		splitDock.addChildDock(tabDock, new Position(Position.CENTER));

		// Add the root dock to the dock model.
		dockModel.addRootDock("splitDock", splitDock, this);

		// Create a maximizer.
		maximizePanel = new SingleMaximizer(splitDock);
		dockModel.addVisualizer("maximizer", maximizePanel, this);

		// Add the split dock to the panel.
		add(maximizePanel, BorderLayout.CENTER);

//		DefaultDockableStateAction maxAction = new DefaultDockableStateAction(
//				expDock, DockableState.MAXIMIZED);
//		maxAction.actionPerformed(new ActionEvent(this,
//				ActionEvent.ACTION_PERFORMED, "init maximised"));

	}

	private Dockable addActions(Dockable dockable)
	{
		int[] states = { DockableState.CLOSED, DockableState.NORMAL,
				DockableState.MAXIMIZED };
		Dockable wrapper = new StateActionDockable(dockable,
				new DefaultDockableStateActionFactory(), states);
		return wrapper;
	}

	private Dockable addExpActions(Dockable dockable)
	{
		int[] states = { DockableState.NORMAL, DockableState.MAXIMIZED };
		Dockable wrapper = new StateActionDockable(dockable,
				new DefaultDockableStateActionFactory(), states);
		return wrapper;
	}

	static SplashScreen splash;
	static Graphics2D g;

	static void splashMsg( String msg )
	{
		if ( splash==null || g==null ) return;
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, 0, 200, 40);
		g.setPaintMode();
		g.setColor(Color.BLACK);
		g.drawString(msg, 2, 15);
		
		splash.update();
	}
	
	public void setUIFont (javax.swing.plaf.FontUIResource f){
	    //
	    // sets the default font for all Swing components.
	    // ex. 
	    //  setUIFont (new javax.swing.plaf.FontUIResource
	    //   ("Serif",Font.ITALIC,12));
	    //
	    java.util.Enumeration<?> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
	    }
	
	public static void main(String[] args)
	{
		LOG.info("starting ... ");
		
		Config config = Config.getInstance();

		splash = SplashScreen.getSplashScreen();
		if (splash == null)
		{
			LOG.info("splash==null");

		}
		else 
		{
			g = splash.createGraphics();
			if (g == null)
			{
				LOG.info("g==null");
			}

		}
		splashMsg("Loading");

		splashMsg("reading config");
		config.readConfiguration();
		splashMsg("creating oors");

		Oors o = new Oors();

		splashMsg("done");
		try
		{
			Thread.sleep(200);
		}
		catch ( Exception ex )
		{
			
		}
		o.setVisible(true);
		
		List<String> scripts = Config.getInstance().getStartupScriptFilenames();

		LinkedList<ScriptingEnvironmentVariable> vars = new LinkedList<ScriptingEnvironmentVariable>();
		vars.add( new ScriptingEnvironmentVariable("branch",Oors.getInstance().getBranch()));
		
		ScriptLog log = (ScriptLog)o.script;
		
		for ( String filename : scripts )
		{
			try
			{
				Reader in = new FileReader(filename);
				Scripting.getInstance().executeScript(filename,in, log, vars);
			}
			catch (FileNotFoundException e1)
			{
				log.err("File Not Found: "+filename);
			}
		}
	}

}
