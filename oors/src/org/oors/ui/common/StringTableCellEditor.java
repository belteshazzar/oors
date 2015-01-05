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

import org.oors.Attribute;
import org.oors.AttributeException;
import org.oors.Base;

public class StringTableCellEditor extends AbstractCellEditor implements
		TableCellEditor
{
	public static final long serialVersionUID = -1L;

	JTextField editComponent;

	JTable table;

	Base base;

	int row;

	int column;

	Attribute attributeToEdit = null;

	public StringTableCellEditor()
	{
		editComponent = new JTextField();
		editComponent.setBorder(null);
	}

	// Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue()
	{
		return editComponent.getText();
	}

	// Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		base = ((TreeTableModel) table.getModel()).getRow(row);
		attributeToEdit = ((TreeTableModel) table.getModel())
				.getAttributeForColumn(column);
		try {
			editComponent.setText((String) base.getValue(attributeToEdit));
		} catch (AttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return editComponent;
	}
}