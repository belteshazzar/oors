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

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class DocumentTableCellRenderer extends JTextPane implements
		TableCellRenderer
{
	public static final long serialVersionUID = -1L;
	private static TableCellRenderer theInstance;

	public static TableCellRenderer getInstance()
	{
		if (theInstance == null) theInstance = new DocumentTableCellRenderer();
		return theInstance;
	}

	DocumentTableCellRenderer()
	{
		super();
		this.setEditorKit(new HTMLEditorKit());
	}

	private Color unselectedForeground;

	private Color unselectedBackground;

	/**
	 * Overrides <code>JComponent.setForeground</code> to assign the
	 * unselected-foreground color to the specified color.
	 * 
	 * @param c
	 *            set the foreground color to this value
	 */
	public void setForeground(Color c)
	{
		super.setForeground(c);
		unselectedForeground = c;
	}

	/**
	 * Overrides <code>JComponent.setBackground</code> to assign the
	 * unselected-background color to the specified color.
	 * 
	 * @param c
	 *            set the background color to this value
	 */
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
		try
		{
			if (obj == null) this.setDocument(new HTMLDocument());
			else this.setDocument((HTMLDocument) obj);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		setSize(table.getColumnModel().getColumn(column).getWidth(), 10000);
		int currentHeight = table.getRowHeight(row);
		int preferredHeight = this.getPreferredSize().height;
		CellHeights.addSize(table, row, column, preferredHeight);
		int nextMaxHeight = CellHeights
				.getNextMaxCellHeight(table, row, column);
		if (preferredHeight >= nextMaxHeight
				&& preferredHeight != currentHeight)
		{
			table.setRowHeight(row, preferredHeight);
		}

		// ////////////////////////////////////////
		Color fg = null;
		Color bg = null;

		JTable.DropLocation dropLocation = table.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsertRow()
				&& !dropLocation.isInsertColumn()
				&& dropLocation.getRow() == row
				&& dropLocation.getColumn() == column)
		{

			fg = UIManager.getColor("Table.dropCellForeground");
			bg = UIManager.getColor("Table.dropCellBackground");

			isSelected = true;
		}

		if (isSelected)
		{
			super.setForeground(fg == null ? table.getSelectionForeground()
					: fg);
			super.setBackground(bg == null ? table.getSelectionBackground()
					: bg);
		}
		else
		{
			super.setForeground(unselectedForeground != null ? unselectedForeground
					: table.getForeground());
			super.setBackground(unselectedBackground != null ? unselectedBackground
					: table.getBackground());
		}
//LOG.info("table background " +table.getBackground());
//LOG.info("tabel selected background "+table.getSelectionBackground());
//		setFont(table.getFont());

		if (hasFocus)
		{
//			LOG.info("hasFocus");
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
				}
				col = UIManager.getColor("Table.focusCellBackground");
				if (col != null)
				{
//					LOG.info("background set to focusCellBackground");
					super.setBackground(col);
				}
			}
		}
		else
		{
			setBorder(getNoFocusBorder());
		}

		// /////////////////////////////////////////

		return this;
	}

	/**
	 * An empty <code>Border</code>. This field might not be used. To change the
	 * <code>Border</code> used by this renderer override the
	 * <code>getTableCellRendererComponent</code> method and set the border of
	 * the returned component directly.
	 */
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1,
			1);

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
}
