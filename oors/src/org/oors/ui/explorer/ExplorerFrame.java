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

import java.awt.Color;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.oors.Attribute;
import org.oors.AttributeFor;
import org.oors.AttributeType;
import org.oors.Document;
import org.oors.Obj;
import org.oors.ProjectBranch;

public class ExplorerFrame extends JSplitPane 
{
	public static final long serialVersionUID = -1L;
	private final static Logger LOG = Logger.getLogger(ExplorerFrame.class
			.getName());

	ProjectBranchModel projectBranchModel;
	ProjectBranchTree projectTree;

	public ProjectBranchTree getProjectTree()
	{
		return projectTree;
	}

	public FolderTable getFolderTable()
	{
		return folderTable;
	}

	FolderTable folderTable;

	private Obj linkStart = null;

	// public Folder getSelectedFolder()
	// {
	// return selectedFolder;
	// }

	public void openDocument(Document doc)
	{
		// documentWindows.put(doc,new org.oors.ui.document.Window(doc));
	}

	public void setLinkStart(Obj mySQLObj)
	{
		this.linkStart = mySQLObj;
	}

	public void setLinkEnd(Obj mySQLObj)
	{
		// notify link start document of link creation
	}

	public Obj getLinkStart()
	{
		return this.linkStart;
	}

	public ExplorerFrame(ProjectBranch projectBranch)
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
//		super("Project Branch",
//				true, //resizable
//		          false, //closable
//		          true, //maximizable
//		          false);//iconifiable);
		// documentWindows = new
		// Hashtable<Document,org.oors.ui.document.Window>();


		// panelContainer = new DialogGlassPane();
		// this.setGlassPane((JComponent) panelContainer);
		try
		{
			List<Attribute> attributes;
			
			attributes = projectBranch
					.getAttributesFor(AttributeFor.PROJECT_BRANCH);
			Attribute pbAttributeToRender = null;

			for (int i = 0; i < attributes.size(); i++)
			{
				LOG.info(attributes.get(i).toString());
				if (attributes.get(i).getValueType() == AttributeType.STRING)
				{
					pbAttributeToRender = attributes.get(i);
				}
			}
			if ( pbAttributeToRender==null )
			{
				pbAttributeToRender = projectBranch.createAttribute(AttributeFor.PROJECT_BRANCH);
				pbAttributeToRender.setValueType(AttributeType.STRING);
				pbAttributeToRender.setName("Auto Generated ProjectBranch Attribute");
			}

			attributes = projectBranch
					.getAttributesFor(AttributeFor.FOLDER);
			Attribute fAttributeToRender = null;
			LOG.info("FOLDER Attributes: "+attributes.size());
			for (int i = 0; i < attributes.size(); i++)
			{
				LOG.info(attributes.get(i).toString());
				if (attributes.get(i).getValueType() == AttributeType.STRING)
				{
					fAttributeToRender = attributes.get(i);
				}
			}
			
			if ( fAttributeToRender==null )
			{
				fAttributeToRender = projectBranch.createAttribute(AttributeFor.FOLDER);
				fAttributeToRender.setValueType(AttributeType.STRING);
				fAttributeToRender.setName("Auto Generated Folder Attribute");
			}

			attributes = projectBranch
					.getAttributesFor(AttributeFor.DOCUMENT);
			
			if ( attributes.size()==0 )
			{
				Attribute dAttributeToRender = projectBranch.createAttribute(AttributeFor.DOCUMENT);
				dAttributeToRender.setValueType(AttributeType.STRING);
				dAttributeToRender.setName("Auto Generated Document Attribute");
			}
			
			projectBranchModel = new ProjectBranchModel(projectBranch);
			projectBranchModel.setAttributesForColumns(projectBranch
					.getAttributesFor(AttributeFor.DOCUMENT));

			projectTree = new ProjectBranchTree(projectBranchModel, pbAttributeToRender,fAttributeToRender);
			projectTree.setSelectionRow(0);
			folderTable = new FolderTable(projectBranchModel);

			// projectTree.getSelectionModel().clearSelection();
			// projectTree.setRootVisible(true);
			// projectTree.setBorder(new LineBorder(Color.WHITE,5));

//			JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			/*split.*/setBorder(null);
			// split.setPreferredSize(new Dimension(200, 200));
			/*split.*/setDividerLocation(200);
			/*split.*/setDividerSize(5);
			/*split.*/setBackground(Color.WHITE);
			JScrollPane left = new JScrollPane(projectTree);
			left.setBorder(null);
			/*split.*/setLeftComponent(left);
			JScrollPane right = new JScrollPane(folderTable);
			right.getViewport().setBackground(Color.WHITE);
			right.setBorder(null);
			/*split.*/setRightComponent(right);

			//this.getContentPane().add(split, BorderLayout.CENTER);
			//this.setSize(800, 600);
			//this.setVisible(true);
//			this.setExtendedState(this.getExtendedState()
//					| JFrame.MAXIMIZED_BOTH);

			// projectTree.setSelectionRow(0);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
