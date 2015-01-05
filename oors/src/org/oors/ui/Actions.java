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

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.EventListenerList;

import org.oors.Base;
import org.oors.ui.common.SelectionListener;

public class Actions
{
	private final static Logger LOG = Logger.getLogger(Actions.class
			.getName());

	private static Actions instance = null;

	public static Actions getInstance()
	{
		if (instance == null)
		{
			instance = new Actions();
		}
		return instance;
	}

	private Base[] currentSelection;
	private Object currentSelectionListener;
	
	private Base actionStart;
	public void setActionStart( Base b )
	{
		actionStart = b;
	}
	public Base getActionStart()
	{
		return actionStart;
	}

	public Base[] getSelection()
	{
		return currentSelection;
	}

	public Object getCurrentSelectionListener()
	{
		return currentSelectionListener;
	}

	public void setSelection(Base[] bb, Object sl)
	{
		LOG.info("Actions.setSelection");
		if ( bb.length!=1 )
		{
			LOG.info(" - bb.length != 1");
			return;
		}
		LOG.info(" - "+ bb[0].getClass() + "-"+sl.getClass());
		if ( currentSelection!=null && currentSelection[0]==bb[0] )
		{
			LOG.info(" - current selection already selected");
			return;
		}
		currentSelection = bb;
		currentSelectionListener = sl;

		fireSelectionChanged();
	}

	protected EventListenerList listeners = new EventListenerList();

	public void addSelectionListener(SelectionListener l)
	{
		listeners.add(SelectionListener.class, l);
	}

	public void removeSelectionListener(SelectionListener l)
	{
		listeners.remove(SelectionListener.class, l);
	}

	private void fireSelectionChanged()
	{
		// Guaranteed to return a non-null array
		Object[] ls = listeners.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = ls.length - 2; i >= 0; i -= 2)
		{
			if (ls[i] == SelectionListener.class && ls[i+1]!=currentSelectionListener)
			{
				LOG.info("Actions fireSelectionChanged "+ls[i+1]+" - "+currentSelectionListener);
				((SelectionListener) ls[i + 1]).selectionChanged();
			}
		}
	}

	private Actions()
	{
		currentSelection = null;
	}
	
	public void show( Component c, int x, int y)
	{
		if ( this.currentSelection.length != 1 ) return;
		
		String selectedUI = c.getClass().getSimpleName();
		String selectedClassName = this.currentSelection[0].getClass().getSimpleName();
		
		JPopupMenu popup = Config.getInstance().getMenu(selectedUI+"-"+selectedClassName);
		if (popup==null)
		{
			LOG.info("Actions.show - null toolbar: "+selectedUI+"-"+selectedClassName);
			return;
		}
		
		for ( int i=0 ; i<popup.getComponentCount() ; i++ )
		{
			Component cc = popup.getComponent(i);
			if ( cc instanceof JMenuItem )
			{
				JMenuItem jb = (JMenuItem)cc;
				jb.setEnabled(jb.getAction()==null || jb.getAction().isEnabled());
			}
			else LOG.info(cc.getClass().toString());
		}

		popup.show(c,x,y);
	}
	
//	public JToolBar getToolBarFor(String context)
//	{
//		Log.trace("context", context);
//		toolbar.removeAll();
//
//		Vector<OorsAction> v = contextActions.get(context);
//		if (v == null)
//		{
//			Log.error("Action context NOT FOUND");
//			return null;
//		}
//		Log.trace("found actions");
//		for (OorsAction a : v)
//		{
//			toolbar.add(new JButton(a));
//		}
//
//		return toolbar;
//	}

}
