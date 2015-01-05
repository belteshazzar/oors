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

import java.util.List;

import org.oors.Base;
import org.oors.OorsEventGenerator;
import org.oors.Document;
import org.oors.Obj;
import org.oors.OorsEvent;
import org.oors.ui.common.TreeTableModel;

public class DocumentModel extends TreeTableModel
{

	public class DocumentTreeNode extends TreeNode
	{

		// private void addChildrenOfTo( Obj of, Vector<Obj> to )
		// {
		// Obj[] children = of.getChildren();
		// for ( int i=0 ; i<children.length ; i++ )
		// {
		// to.add(children[i]);
		// addChildrenOfTo(children[i],to);
		// }
		// }

		public DocumentTreeNode(Document document)
		{
			super(null, document);

			contents = document.getObjs();
			// if ( contents!=null )
			// {
			// Vector<Obj> contentV = new Vector<Obj>();
			//
			for (int i = 0; i < contents.size(); i++)
			{
				new ObjTreeNode(this, (Obj) contents.get(i));
				// contentV.add((Obj)contents[i]);
				// addChildrenOfTo((Obj)contents[i],contentV);
			}
			//
			// contents = new Obj[contentV.size()];
			// for ( int i=0 ; i<contents.length ; i++ )
			// {
			// contents[i] = contentV.get(i);
			// }
			// }
		}

		@Override
		public void createdUpdate(OorsEvent event)
		{
			// Something has been created.
			// It will be an Obj.
			//
			if (event.getAffected() instanceof Obj)
			{
				insertChild((Base)event.getAffected());
				// setContents( ((Document)represents).getObjs());
			}
			else
			{
				new Exception("createdUpdate not for Obj?");
			}
		}

		@Override
		public void insertChild(Base represents)
		{
			ObjTreeNode tn = new ObjTreeNode(this, (Obj) represents);
			fireTreeNodeInserted(tn);
		}

		@Override
		public void deletedUpdate(OorsEvent event)
		{
		}

		@Override
		public void modifiedUpdate(OorsEvent event)
		{
		}

	}

	public class ObjTreeNode extends TreeNode
	{
		public ObjTreeNode(TreeNode parent, Obj obj)
		{
			super(parent, obj);

			// TreeNode constructor will add node to the end
			// move it to first

			if (parent != null)
			{

				TreeNode tn = parent.getChildren().remove(
						parent.getChildren().size() - 1);
				List<Obj> parentObjs = null;
				if (parent.getRepresents() instanceof Document)
				{
					Document d = (Document) parent.getRepresents();
					parentObjs = d.getObjs();

				}
				else if (parent.getRepresents() instanceof Obj)
				{
					Obj o = (Obj) parent.getRepresents();
					parentObjs = o.getChildren();
				}
				if ( parentObjs!=null )
				{
					int index = 0;
					while (index < parentObjs.size())
					{
						if (parentObjs.get(index) == represents)
						{
							parent.getChildren().insertElementAt(tn, index);
							break;
						}
						index++;
					}
				}
			}

			List<Obj> objs = obj.getChildren();
			if (objs != null)
			{
				for (int i = 0; i < objs.size(); i++)
				{
					new ObjTreeNode(this, objs.get(i));
				}
			}
		}

		@Override
		public void insertChild(Base represents)
		{
			ObjTreeNode tn = new ObjTreeNode(this, (Obj) represents);
			fireTreeNodeInserted(tn);
		}

		@Override
		public void createdUpdate(OorsEvent event)
		{
			// Something has been created.
			// It will be an Obj.
			//
			if (event.getAffected() instanceof Obj)
			{
				insertChild((Base)event.getAffected());
			}
			else
			{
				new Exception("createdUpdate not for Obj?");
			}
		}

		@Override
		public void deletedUpdate(OorsEvent event)
		{
		}

		@Override
		public void modifiedUpdate(OorsEvent event)
		{
			fireTreeNodeChanged(this);
		}
	}

	public DocumentModel(Document document)
	{
		super(false);
		setRoot(new DocumentTreeNode(document));
	}
}
