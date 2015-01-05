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

import org.oors.Attribute;
import org.oors.Base;
import org.oors.Folder;
import org.oors.ProjectBranch;
import org.oors.ui.Actions;
import org.oors.ui.common.Tree;
import org.oors.ui.common.TreeTableModel.TreeNode;

public class ProjectBranchTree extends Tree
{
	public static final long serialVersionUID = -1L;
	private final static Logger LOG = Logger.getLogger(ProjectBranchTree.class
			.getName());

	private static final String context = "project tree";

	public ProjectBranchTree(ProjectBranchModel model, Attribute rootAttribute, Attribute attributeToRender)
			throws Exception
	{
		super(model, context, rootAttribute, attributeToRender);
	}
	
	@Override
	public void selectionChanged()
	{
		Object o = Actions.getInstance().getCurrentSelectionListener();
		Base[] bb = Actions.getInstance().getSelection();
		LOG.info("Tree selectionChanged");
		if ( o==this )
		{
			LOG.info(" - own event");
			return;
		}
		else if ( bb.length==0 )
		{
			LOG.info(" - nothing selected, clear");
			this.clearSelection();
			return;
		}
		else if ( bb[0] instanceof ProjectBranch )
		{
			LOG.info(" - project branch");
			if ( this.getModel().getRoot()==bb[0] )
			{
				LOG.info(" - this project, select root");
				this.setSelectionRow(0);
				return;
			}
			else
			{
				LOG.info(" - not this project, clear");
				this.clearSelection();
			}
		}
		else if ( bb[0] instanceof Folder )
		{
			LOG.info(" - folder");
			Folder f = (Folder)bb[0];
			if ( f.getProjectBranch()==((TreeNode)this.getModel().getRoot()).getRepresents() )
			{
				LOG.info(" - folder in this project");
				return;
			}
			else
			{
				LOG.info(" - folder NOT in this project");
				this.clearSelection();
				return;
			}
		}
		else
		{
			LOG.info(" - not interested in project");
			return;
		}
		
	}

}