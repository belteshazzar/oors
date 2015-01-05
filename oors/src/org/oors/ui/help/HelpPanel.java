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

package org.oors.ui.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import org.oors.ui.Config;

public class HelpPanel extends JPanel implements HyperlinkListener
{
	private static final long serialVersionUID = -5947573916144567582L;
	private final static Logger LOG = Logger.getLogger(HelpPanel.class
			.getName());

	private JEditorPane pane;
	private JPopupMenu popup;
	
	public HelpPanel()
	{
		super();
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		pane = new JEditorPane();
		pane.setEditable(false);
		pane.addHyperlinkListener(this);
		java.net.URL helpURL;
		try
		{
			helpURL = (new File(Config.getInstance().getHelpIndex())).toURI().toURL();
			pane.setPage(helpURL);
		}
		catch ( NullPointerException npex )
		{
			LOG.info("Null Pointer Exception: " + Config.getInstance().getHelpIndex());			
		}
		catch (MalformedURLException e1)
		{
			LOG.info("Attempted to read a bad URL: " + Config.getInstance().getHelpIndex());
		}
		catch (IOException e)
		{
			LOG.info("Exception reading: " + Config.getInstance().getHelpIndex());				
		}
		
		popup = new JPopupMenu();
		popup.add( "Back" ).addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				popup.setVisible(false);
				LOG.info("Back is not implemented");
			}
		});
		popup.add("Forward").addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				popup.setVisible(false);
				LOG.info("Forward is not implemented");
			}
		});
		popup.addSeparator();
		popup.add("Find").addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				popup.setVisible(false);
				LOG.info("Find is not implemented");
			}
		});

		pane.addMouseListener( new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				testEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				testEvent(e);
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
			}
			
			private void testEvent( MouseEvent e )
			{
				if ( e.isPopupTrigger() )
				{
					popup.show(pane, e.getX(), e.getY() );
				}
			}
		});


		add(pane,BorderLayout.NORTH);
	}
	
	@Override
	public void hyperlinkUpdate(HyperlinkEvent he)
	{
		if ( he.getEventType()==EventType.ACTIVATED && he.getURL() != null)
		{
			try
			{
				pane.setPage(he.getURL());
			}
			catch (IOException e)
			{
				LOG.info("Attempted to read a bad URL: " + he.getURL());
			}
		}
	}
}
