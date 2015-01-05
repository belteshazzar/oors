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

package org.oors.ui.document;

import org.oors.Base;
import org.oors.ui.common.Table;

public class DocumentTable extends Table
{
	public static final long serialVersionUID = -1L;

	private static final String context = "document tree";
	private static final String headerContext = "document tree header";

	@Override
	protected void openRow(Base rowObject)
	{
	}

	public DocumentTable(DocumentModel documentModel)
	{
		super(documentModel, context, headerContext);
	}

	@Override
	public void selectionChanged()
	{
		// LOG.info("||| Table selectionChanged");
		//
		// Base[] bb = Actions.getInstance().getSelection();
		//
		// if ( bb.length==1 && bb[0] instanceof )
		//
		// if ( model.flatNodes != null )
		// {
		// int index =
		// model.flatNodes.indexOf(Actions.getInstance().currentSelectionUI);
		// if ( index!=-1 )
		// {
		// this.getSelectionModel().setSelectionInterval(index, index);
		// }
		// else
		// {
		// this.clearSelection();
		// }
		// }
		// else
		// {
		// Object selection = Actions.getInstance().getSelectedUIComponent();
		// if ( selection!=null && selection instanceof TreeNode )
		// {
		// model.setTableParent((TreeNode)selection);
		// }
		// }
		//
	}

}