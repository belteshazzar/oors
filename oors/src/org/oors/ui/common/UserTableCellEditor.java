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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.oors.Attribute;
import org.oors.Base;
import org.oors.Project;
import org.oors.User;
import org.oors.UserGroup;

public class UserTableCellEditor extends AbstractCellEditor implements
		TableCellEditor
{
	public static final long serialVersionUID = -1L;

	JComboBox<User> editComponent;
	Attribute attributeToEdit;
	Base base;

	public UserTableCellEditor( Project project )
	{
		editComponent = new JComboBox<User>( );
		Collection<UserGroup> groups = project.getUserGroups();
		for ( UserGroup ug : groups )
		{
			Collection<User> users = ug.getUsers();
			for ( User u : users )
			{
				editComponent.addItem(u);
			}
		}
		editComponent.addKeyListener( new KeyAdapter(){
			@Override
			public void keyReleased( KeyEvent ev )
			{
				if ( ev.getKeyCode()==KeyEvent.VK_ENTER )
				{
					fireEditingStopped();
				}
			}
			
		});
		editComponent.setBorder(null);
	}

	public Object getCellEditorValue()
	{
		User value = (User)editComponent.getSelectedItem();
		return value;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{

		return editComponent;
	}
}