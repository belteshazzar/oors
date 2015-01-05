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

package org.oors.ui.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * Redesign Panel to only create one of each type? singleton per derived class
 * or factory of some sort so that we're not enlessly creating panel objects?
 * 
 */
public class Panel extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4580943122967240164L;

	private JPanel content;

	private GridBagConstraints gbcon;

	private GridBagLayout gb;

	//private Font font;

	protected JButton previous, next, finish, cancel;

	private PanelContainer panelContainer;

	public void actionPerformed(ActionEvent ev)
	{
		Object o = ev.getSource();
		if (o == previous) previousPressed();
		else if (o == next) nextPressed();
		else if (o == finish) finishPressed();
		else cancelPressed();
	}

	protected void previousPressed()
	{
	}

	protected void nextPressed()
	{
	}

	protected void finishPressed()
	{
	}
	
	protected PanelContainer getPanelContainer()
	{
		return panelContainer;
	}

	private void cancelPressed()
	{
		panelContainer.removePanel();
	}

	public Panel( PanelContainer pc, String title, String description,
			String imageFilename)
	{
		super();

		panelContainer = pc;
		
		this.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

		Dimension d = new Dimension(600,400);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		setLayout(new BorderLayout());

		JPanel header = new JPanel();
		header.setLayout(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(new CompoundBorder(new BottomEtchedBorder(),
				new EmptyBorder(5, 5, 5, 5)));
		Box b = Box.createVerticalBox();
		JLabel titleLabel = new JLabel(title);
		//titleLabel.setFont(new Font("tahoma", Font.BOLD, 16));
		b.add(titleLabel);
		JLabel text = new JLabel("   " + description);

		//font = new Font("tahoma", Font.PLAIN, 12);
		//text.setFont(font);
		b.add(text);
		header.add(b, BorderLayout.WEST);

		ImageIcon img = new ImageIcon("org/oors/ui/images" + File.separatorChar
				+ imageFilename);
		JLabel image = new JLabel(img);
		header.add(image, BorderLayout.EAST);

		add(header, BorderLayout.NORTH);

		content = new JPanel();
		content.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 20));
		gb = new GridBagLayout();
		gbcon = new GridBagConstraints();
		gbcon.insets = new Insets(10, 10, 10, 10);
		content.setLayout(gb);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new TopEtchedBorder());
		buttonPanel.setLayout(new BorderLayout());
		JPanel buttons = new JPanel();
		previous = new JButton("< Previous");
		//previous.setFont(font);
		previous.addActionListener(this);
		buttons.add(previous);
		next = new JButton("Next >");
		//next.setFont(font);
		next.addActionListener(this);
		buttons.add(next);
		finish = new JButton("Finish");
		//finish.setFont(font);
		finish.addActionListener(this);
		buttons.add(finish);
		cancel = new JButton("Cancel");
		//cancel.setFont(font);
		cancel.addActionListener(this);
		buttons.add(cancel);
		buttonPanel.add(buttons, BorderLayout.EAST);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(content, BorderLayout.NORTH);

		add(contentPanel, BorderLayout.WEST);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	protected JButton addButton(String name, String text)
	{
		JButton b = new JButton(name);
		//b.setFont(font);
		gbcon.gridwidth = 1;
		gb.setConstraints(b, gbcon);
		content.add(b);

		JLabel l = new JLabel(text);
		//l.setFont(font);
		gbcon.gridwidth = GridBagConstraints.REMAINDER;
		gb.setConstraints(l, gbcon);
		content.add(l);

		return b;
	}

	protected JTextField addField(String name, final PanelFieldValidator pfv)
	{
		return addField(name,"",pfv);
	}
	
	protected JTextField addField(String name, String value, final PanelFieldValidator pfv)
	{
		gbcon.weightx = 1.0;
		gbcon.gridwidth = 1;
		gbcon.anchor = GridBagConstraints.WEST;
		JLabel nameLabel = new JLabel(name + ":");
		//nameLabel.setFont(font);
		gb.setConstraints(nameLabel, gbcon);
		content.add(nameLabel);

		JTextField f = new JTextField(15);
		f.setText(value);
		//f.setFont(font);
		gb.setConstraints(f, gbcon);
		content.add(f);

		gbcon.gridwidth = GridBagConstraints.REMAINDER;
		final JLabel msg = new JLabel();
		msg.setText(pfv.check(value));
		//msg.setFont(font);
		msg.setForeground(Color.RED);
		gb.setConstraints(msg, gbcon);
		content.add(msg);

		f.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
			}

			public void insertUpdate(DocumentEvent e)
			{
				changed(e);
			}

			public void removeUpdate(DocumentEvent e)
			{
				changed(e);
			}

			private void changed(DocumentEvent e)
			{
				try
				{
					msg.setText(pfv.check(e.getDocument().getText(0,
							e.getDocument().getLength())));

				}
				catch (Exception ex)
				{
				}
			}
		});

		return f;
	}
}