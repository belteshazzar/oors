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

import java.util.List;

import org.oors.Base;
import org.oors.Document;
import org.oors.Folder;
import org.oors.OorsEvent;
import org.oors.ProjectBranch;
import org.oors.ui.common.TreeTableModel;

public class ProjectBranchModel extends TreeTableModel
{
	public class ProjectBranchTreeNode extends TreeNode
	{
		public ProjectBranchTreeNode(TreeNode parent, Base base)
		{
			super(parent, (Base) base);

			if ( base instanceof Folder )
			{
				Folder folder = (Folder)base;
				contents = folder.getDocuments();

				List<Folder> folders = folder.getFolders();
				if (folders != null)
				{
					for (int i = 0; i < folders.size(); i++)
					{
						new ProjectBranchTreeNode(this, folders.get(i));
					}
				}
			}
			else if ( base instanceof ProjectBranch )
			{
				ProjectBranch folder = (ProjectBranch)base;
				contents = folder.getDocuments();

				List<Folder> folders = folder.getFolders();
				if (folders != null)
				{
					for (int i = 0; i < folders.size(); i++)
					{
						new ProjectBranchTreeNode(this, folders.get(i));
					}
				}
				
			}
		}

		@Override
		public void createdUpdate(OorsEvent event)
		{
			// Something has been created.
			// It will be either a Document or a Folder.
			//
			if (event.getAffected() instanceof Document)
			{
				if ( represents instanceof Folder )
				{
					setContents(((Folder)represents).getDocuments());
				}
				else if ( represents instanceof ProjectBranch )
				{
					setContents(((ProjectBranch)represents).getDocuments());
				}
			}
			else if (event.getAffected() instanceof Folder)
			{
				insertChild((Base)event.getAffected());
			}
			else
			{
				new Exception("createdUpdate not for Folder or Document?");
			}
		}

		@Override
		public void insertChild(Base represents)
		{
			ProjectBranchTreeNode tn = new ProjectBranchTreeNode(this,
					(Folder) represents);
			fireTreeNodeInserted(tn);
		}

		@Override
		public void deletedUpdate(OorsEvent event)
		{
		}

		@Override
		public void modifiedUpdate(OorsEvent event)
		{
			TreeNode node = find((Base)event.getAffected());
			fireTreeNodeChanged(node);
		}

	}

	public ProjectBranchModel(ProjectBranch projectBranch)
	{
		super(true);
		setRoot(new ProjectBranchTreeNode(null, projectBranch));
	}
}
