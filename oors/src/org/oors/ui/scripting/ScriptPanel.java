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

package org.oors.ui.scripting;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.text.Style;

import org.oors.ScriptLog;

public class ScriptPanel extends JSplitPane implements ScriptLog
{
	private static final long serialVersionUID = -2173551850310942465L;

	private LogPanel log;
	private EditPanel edit;
	private JFileChooser chooser;
	JPanel editCards;

	private static final String EDITOR = "editor";
	static final String CHOOSER = "chooser";
	
	public ScriptPanel()
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
		
		edit = new EditPanel( this , log );
				
		chooser = new JFileChooser();
		chooser.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ( e.getActionCommand()==JFileChooser.CANCEL_SELECTION )
				{
					((CardLayout)editCards.getLayout()).show(editCards, EDITOR);
				}
				else if ( e.getActionCommand()==JFileChooser.APPROVE_SELECTION )
				{
					((CardLayout)editCards.getLayout()).show(editCards, EDITOR);
					File f = chooser.getSelectedFile();
					
					try
					{
						String s = null;
						BufferedReader in;
						in = new BufferedReader( new FileReader(f));
						while ( in.ready() )
						{
							if ( s==null ) s = in.readLine();
							else s += "\n" + in.readLine();
						}
						in.close();
						edit.text.setText(s);
					}
					catch (FileNotFoundException e1)
					{
						err("File Not Found: "+f.getPath());
					}
					catch (IOException e2)
					{
						err("IO Error Reading: "+f.getPath());
					}
				}
			}
		});
		editCards = new JPanel();
		editCards.setLayout( new CardLayout() );
		editCards.add(edit,EDITOR);
		editCards.add(chooser,CHOOSER);
		
		this.setLeftComponent( editCards );		
		this.setRightComponent( log = new LogPanel() );

		this.setBorder(null);
		this.setDividerLocation(chooser.getPreferredSize().width);
		this.setDividerSize(4);
	}
	
	public void err( String msg )
	{
		log(msg+"\n",log.doc.getStyle("error"));
	}
	
	public void msg( String msg )
	{
		log(msg+"\n",log.doc.getStyle("regular"));
	}

	public void log( String msg, Style style )
	{
		try
		{
			log.doc.insertString(log.doc.getLength(), msg, style );
			log.requestFocus();
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
	}
}


