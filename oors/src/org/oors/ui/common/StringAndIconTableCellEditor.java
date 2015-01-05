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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;

public class StringAndIconTableCellEditor extends AbstractCellEditor implements
		TableCellEditor
{
	public static final long serialVersionUID = -1L;

	private Panel panel;

	private JLabel label;

	public ResizingJTextArea textArea;

	public class Panel extends JPanel
	{
		public static final long serialVersionUID = -1L;

		public void requestFocus()
		{
			textArea.requestFocus();
		}
	}

	JTable table;

	int row;

	int column;

	public StringAndIconTableCellEditor()
	{
		GridBagConstraints gridBagConstraints;

		panel = new Panel();
		panel.setBackground(Color.WHITE);
		label = new JLabel();
		textArea = new ResizingJTextArea();
		panel.setLayout(new GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		panel.add(label, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panel.add(textArea, gridBagConstraints);

	}

	// Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue()
	{
		return textArea.getText();
	}

	// Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		this.table = table;
		this.row = row;
		this.column = column;
		label.setIcon(((StringAndIcon) value).getIcon());
		textArea.setText(((StringAndIcon) value).getString(), table
				.getRowHeight(row));
		return panel;
	}

	public boolean isCellEditable(EventObject ev)
	{
		return true;
	}

	public class ResizingJTextArea extends JTextArea implements DocumentListener
	{
		public static final long serialVersionUID = -1L;

		public ResizingJTextArea()
		{
			super();
		}

		public void setText(String text, int initialHeight)
		{
			setText(text);
			getDocument().addDocumentListener(this);
		}

		public void resizeRow()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					int currentHeight = table.getRowHeight(row);
					int preferredHeight = getPreferredSize().height;
					CellHeights.addSize(table, row, column, preferredHeight);
					int nextMaxHeight = CellHeights.getNextMaxCellHeight(table, row,
							column);
					if (preferredHeight >= nextMaxHeight
							&& preferredHeight != currentHeight)
					{
						table.setRowHeight(row, preferredHeight);
					}
				}
			});
		}

		public void removeUpdate(DocumentEvent ev)
		{
			resizeRow();
		}

		public void insertUpdate(DocumentEvent ev)
		{
			resizeRow();
		}

		public void changedUpdate(DocumentEvent ev)
		{
			resizeRow();
		}
	}
}