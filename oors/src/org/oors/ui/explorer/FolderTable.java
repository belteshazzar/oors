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

package org.oors.ui.explorer;

import java.util.logging.Logger;

import org.oors.Base;
import org.oors.Document;
import org.oors.Folder;
import org.oors.ProjectBranch;
import org.oors.ui.Actions;
import org.oors.ui.common.Table;

public class FolderTable extends Table
{
	public static final long serialVersionUID = -1L;
	private final static Logger LOG = Logger.getLogger(FolderTable.class
			.getName());

	private static final String CONTEXT = "folder table";
	private static final String HEADER_CONTEXT = "folder table header";

	@Override
	protected void openRow(Base rowObject)
	{
		if (rowObject instanceof Document)
		{
			org.oors.ui.Oors.getInstance().open((Document) rowObject);
		}
	}

	public FolderTable(ProjectBranchModel model)
	{
		super(model, CONTEXT, HEADER_CONTEXT);
	}

	@Override
	public void selectionChanged()
	{
		LOG.info("FolderTable selectionChanged");
		Object o = Actions.getInstance().getCurrentSelectionListener();
		if ( o==this ) 
		{
			LOG.info(" - event from myself");
			return;
		}
		else
		{
			LOG.info(" - not me: " +o);
		}
		Base[] bb = Actions.getInstance().getSelection();
		if (bb.length == 1 && ( bb[0] instanceof Folder || bb[0] instanceof ProjectBranch ) )
		{
			LOG.info(" - set table parent");
			model.setTableParent(bb[0]);
		}

	}
}