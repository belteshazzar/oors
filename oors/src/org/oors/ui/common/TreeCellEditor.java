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
import java.awt.event.ActionEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;

import org.oors.AttributeException;
import org.oors.Base;

public class TreeCellEditor extends DefaultTreeCellEditor
{

	public TreeCellEditor(Tree tree, TreeCellRenderer renderer)
	{
		super((JTree) tree, renderer);
	}

	private Base base;

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row)
	{
		base = ((TreeTableModel.TreeNode) value).getRepresents();
		Object toRender = null;
		try {
			toRender = base.getValue(((TreeCellRenderer) renderer)
					.getRenderedAttribute());
		} catch (AttributeException e) {
			try {
				toRender = base.getValue(((TreeCellRenderer) renderer)
						.getRenderedRootAttribute());
			} catch (AttributeException e2) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//setFont(new Font("tahoma", Font.PLAIN, 12));
		return super.getTreeCellEditorComponent(tree, toRender, isSelected,
				expanded, leaf, row);
	}

	@Override
	public void actionPerformed( ActionEvent ae )
	{
		super.actionPerformed(ae);
		
	}
	public Object getCellEditorValue()
	{
		try {
			base.setValue(((TreeCellRenderer) renderer).getRenderedAttribute(),
					super.getCellEditorValue().toString());
		} catch (AttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base;
	}

}