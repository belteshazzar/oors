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

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.oors.Attribute;
import org.oors.AttributeException;
import org.oors.Base;

public class NumberTableCellEditor extends AbstractCellEditor implements
		TableCellEditor
{
	public static final long serialVersionUID = -1L;

	JTextField editComponent;
	Attribute attributeToEdit;
	Base base;

	public class NumericDocument extends PlainDocument
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NumericDocument()
		{
			super();
		}

		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException
		{
			String current = super.getText(0, getLength());

			if (current.length() == 0 && str.length() == 1)
			{
				if (str.charAt(0) == '.' || str.charAt(0) == '-') super
						.insertString(offset, str, attr);
			}
			String front;
			String end;

			if (current.length() > 0 && offset > 0) front = current.substring(
					0, offset);
			else front = "";

			if (offset < current.length()) end = current.substring(offset);
			else end = "";

			String proposed = front + str + end;

			try
			{
				Double.parseDouble(proposed);
				super.insertString(offset, str, attr);
			}
			catch (NumberFormatException nfex)
			{
				return;
			}
		}
	}

	public NumberTableCellEditor()
	{
		editComponent = new JTextField();
		editComponent.setDocument(new NumericDocument());
		editComponent.setBorder(null);
	}

	public Object getCellEditorValue()
	{
		return new Double(Double.parseDouble(editComponent.getText()));
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		base = ((TreeTableModel) table.getModel()).getRow(row);
		attributeToEdit = ((TreeTableModel) table.getModel())
				.getAttributeForColumn(column);
		Object o = null;
		try {
			o = base.getValue(attributeToEdit);
		} catch (AttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (o == null) o = "";
		editComponent.setText(o.toString());

		return editComponent;
	}
}