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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.oors.ScriptLog;
import org.oors.Scripting;
import org.oors.ScriptingEnvironmentVariable;
import org.oors.ui.Oors;

class EditPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(EditPanel.class
			.getName());

	JTextPane lines;
	JTextPane text;
	JPanel menu;
	JButton exe;
	private JPopupMenu popup;

	// references passed in to constructor
	ScriptPanel scriptPanel;
	LogPanel logPanel;

	public EditPanel( final ScriptPanel scriptPanel, final LogPanel logPanel )
	{
		super();
		this.scriptPanel = scriptPanel;
		this.logPanel = logPanel;
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));

		menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.LINE_AXIS));
		menu.setPreferredSize(new Dimension(100, 40));
		menu.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		menu.add(Box.createHorizontalGlue());
		menu.add(exe = new JButton("Execute"));
		exe.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				LinkedList<ScriptingEnvironmentVariable> env = new LinkedList<ScriptingEnvironmentVariable>();
				env.add( new ScriptingEnvironmentVariable("branch",Oors.getInstance().getBranch()));
				
				Scripting.getInstance().executeScript(null,text.getText(), (ScriptLog) scriptPanel, env);

//				jsEngine.getContext().setWriter(new PrintWriter(new Writer()
//				{
//					String s = "";
//
//					@Override
//					public void write(char[] cbuf, int off, int len)
//							throws IOException
//					{
//						for (int i = 0; i < len; i++)
//						{
//							s += cbuf[off + i];
//						}
//					}
//
//					@Override
//					public void flush() throws IOException
//					{
//						scriptPanel.log(s, logPanel.doc.getStyle("regular"));
//						s = "";
//					}
//
//					@Override
//					public void close() throws IOException
//					{
//					}
//
//				}));
//				jsEngine.getContext().setErrorWriter( new PrintWriter(new Writer()
//				{
//					String s = "";
//
//					@Override
//					public void write(char[] cbuf, int off, int len)
//							throws IOException
//					{
//						for (int i = 0; i < len; i++)
//						{
//							s += cbuf[off + i];
//						}
//					}
//
//					@Override
//					public void flush() throws IOException
//					{
//						scriptPanel.log(s, logPanel.doc.getStyle("error"));
//						s = "";
//					}
//
//					@Override
//					public void close() throws IOException
//					{
//					}
//
//				}));
//
//				try
//				{
//					jsEngine.eval(text.getText());
//				}
//				catch (ScriptException ex)
//				{
//					scriptPanel.err(ex.getMessage());
//					ex.printStackTrace();
//				}
			}
		});
		menu.add(Box.createRigidArea(new Dimension(10, 0)));
		this.add(menu, BorderLayout.SOUTH);

		text = new JTextPane();
		text.setBorder(null);

		JScrollPane scroll = new JScrollPane();
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(text, BorderLayout.CENTER);
		scroll.setViewportView(jp);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		lines = new JTextPane();
		lines.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
				Color.LIGHT_GRAY));
		lines.setEditable(false);
		lines.setPreferredSize(new Dimension(30, 30));

//		final StyledDocument doc = (StyledDocument) lines.getDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		text.setLogicalStyle(def);

		final Style regular = ((StyledDocument) lines.getDocument()).addStyle(
				"regular", def);
		StyleConstants.setAlignment(regular, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(regular, Color.LIGHT_GRAY);

		lines.setLogicalStyle(regular);

		// ((StyledDocument)text.getDocument()).addStyle("regular", def);

		lines.setText("1");
		text.getDocument().addDocumentListener(new DocumentListener()
		{
			public String getText()
			{
				int caretPosition = text.getDocument().getLength();
				Element root = text.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++)
				{
					text += i + System.getProperty("line.separator");
				}
				return text;
			}

			@Override
			public void changedUpdate(DocumentEvent de)
			{
				lines.setText(getText());
			}

			@Override
			public void insertUpdate(DocumentEvent de)
			{
				lines.setText(getText());
			}

			@Override
			public void removeUpdate(DocumentEvent de)
			{
				lines.setText(getText());
			}

		});

		scroll.setRowHeaderView(lines);
		scroll.setBorder(null);
		this.add(scroll, BorderLayout.CENTER);
		
		popup = new JPopupMenu();
		popup.add( "New" ).addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				popup.setVisible(false);
				text.setText("");
			}
		});
		popup.add("Load").addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				popup.setVisible(false);
				((CardLayout) scriptPanel.editCards.getLayout()).show(scriptPanel.editCards, ScriptPanel.CHOOSER);
			}
		});
		popup.add("Save").addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				popup.setVisible(false);
				LOG.info("Save is not implemented");
			}
		});
		popup.add("Save As").addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				popup.setVisible(false);
				LOG.info("Save As is not implemented");
			}
		});

		text.addMouseListener( new MouseListener()
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
					popup.show(EditPanel.this, e.getX(), e.getY() );
				}
			}
		});

	}
}
