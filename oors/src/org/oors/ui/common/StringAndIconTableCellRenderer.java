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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class StringAndIconTableCellRenderer extends JPanel implements
		TableCellRenderer
{
	public static final long serialVersionUID = -1L;

	private Color unselectedForeground;

	private Color unselectedBackground;

	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1,
			1);

	private JLabel label;

	private JTextPane textPane;

	public StringAndIconTableCellRenderer()
	{
		super();

		GridBagConstraints gridBagConstraints;

		setBackground(Color.WHITE);
		label = new JLabel();
		textPane = new JTextPane();
		setLayout(new GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		add(label, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(textPane, gridBagConstraints);
	}

	private static Border getNoFocusBorder()
	{
		if (System.getSecurityManager() != null)
		{
			return SAFE_NO_FOCUS_BORDER;
		}
		else
		{
			return noFocusBorder;
		}
	}

	public void setForeground(Color c)
	{
		super.setForeground(c);
		unselectedForeground = c;
	}

	public void setBackground(Color c)
	{
		super.setBackground(c);
		unselectedBackground = c;
	}

	public void updateUI()
	{
		super.updateUI();
		setForeground(null);
		setBackground(null);
	}

	public Component getTableCellRendererComponent(
			//
			JTable table, Object obj, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		Color fg = null;
		Color bg = null;

		if (isSelected)
		{
			super.setForeground(fg == null ? table.getSelectionForeground() : fg);
			super.setBackground(bg == null ? table.getSelectionBackground() : bg);
			textPane.setForeground(fg == null ? table.getSelectionForeground()
					: fg);
			textPane.setBackground(bg == null ? table.getSelectionBackground()
					: bg);
		}
		else
		{
			super
					.setForeground(unselectedForeground != null ? unselectedForeground
							: table.getForeground());
			super
					.setBackground(unselectedBackground != null ? unselectedBackground
							: table.getBackground());
			textPane
					.setForeground(unselectedForeground != null ? unselectedForeground
							: table.getForeground());
			textPane
					.setBackground(unselectedBackground != null ? unselectedBackground
							: table.getBackground());
		}

		//setFont(table.getFont());

		if (hasFocus)
		{
			Border border = null;
			if (isSelected)
			{
				border = UIManager
						.getBorder("Table.focusSelectedCellHighlightBorder");
			}
			if (border == null)
			{
				border = UIManager.getBorder("Table.focusCellHighlightBorder");
			}
			setBorder(border);

			if (!isSelected && table.isCellEditable(row, column))
			{
				Color col;
				col = UIManager.getColor("Table.focusCellForeground");
				if (col != null)
				{
					super.setForeground(col);
					textPane.setForeground(col);
				}
				col = UIManager.getColor("Table.focusCellBackground");
				if (col != null)
				{
					super.setBackground(col);
					textPane.setBackground(col);
				}
			}
		}
		else
		{
			setBorder(getNoFocusBorder());
		}

		label.setIcon(((StringAndIcon) obj).getIcon());
		textPane.setText(((StringAndIcon) obj).getString());
		setSize(table.getColumnModel().getColumn(column).getWidth(), 10000);
		int currentHeight = table.getRowHeight(row);
		int preferredHeight = getPreferredSize().height;
		CellHeights.addSize(table, row, column, preferredHeight);
		int nextMaxHeight = CellHeights.getNextMaxCellHeight(table, row, column);
		if (preferredHeight >= nextMaxHeight && preferredHeight != currentHeight)
		{
			table.setRowHeight(row, preferredHeight);
		}

		return this;
	}
}