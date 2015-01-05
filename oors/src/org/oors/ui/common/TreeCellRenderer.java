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

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.oors.Attribute;
import org.oors.AttributeException;
import org.oors.AttributeType;
import org.oors.Base;
import org.oors.ui.Config;

public class TreeCellRenderer extends DefaultTreeCellRenderer
{
	private ImageIcon folder;
	private Attribute rootAttribute;
	private Attribute attribute;

	public TreeCellRenderer( JTree tree, Attribute rootAttribute, Attribute attribute ) throws Exception
	{
		super();
		this.rootAttribute = rootAttribute;
		this.attribute = attribute;
		//if ( attribute.getValueClass() != String.class )
		if ( attribute.getValueType() != AttributeType.STRING )
		{
			throw new Exception("TreeCellRenderer only supports String attributes");
		}
		folder = Config.getInstance().getIcon("folder");
		setLeafIcon(folder);
		setOpenIcon(folder);
		setClosedIcon(folder);
//		setFont(new Font("calibri", Font.PLAIN, 18));
		setBorder(new EmptyBorder(4, 3, 4, 3));
		//setBorder( BorderFactory.createLineBorder(Color.RED));
	}
	
	public Attribute getRenderedAttribute()
	{
		return attribute;
	}
	public Attribute getRenderedRootAttribute()
	{
		return rootAttribute;
	}

	private static final long serialVersionUID = 1161080336368641930L;

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		Base b = ((TreeTableModel.TreeNode) value).getRepresents();
		Object toRender = null;
		try {
			toRender = b.getValue(attribute);
		} catch (AttributeException e) {
			try {
				toRender = b.getValue(rootAttribute);
			} catch (AttributeException e2) {
				toRender = "Exception";
			}
		}
		super.getTreeCellRendererComponent(tree, toRender, sel, expanded, leaf,
				row, hasFocus);

		return this;
	}
}
