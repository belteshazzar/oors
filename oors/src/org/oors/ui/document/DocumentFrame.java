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

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultEditorKit;

import org.oors.Attribute;
import org.oors.AttributeFor;
import org.oors.AttributeType;
import org.oors.Document;
import org.oors.ui.common.Panel;

public class DocumentFrame extends JSplitPane
{
	public static final long serialVersionUID = -1L;

	public static DocumentFrame current = null;

	DocumentModel documentModel;
	DocumentTree documentTree;
	DocumentTable documentTable;

	public DocumentTree getDocumentTree()
	{
		return documentTree;
	}
	
	public DocumentTable getDocumentTable()
	{
		return documentTable;
	}

	public DocumentFrame(Document doc)
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
		// documentWindows.put(doc,new org.oors.ui.document.Window(doc));

		documentModel = new DocumentModel(doc);
		// documentModel.setAttributesForColumns( new String[] {
		// "body","created at","created by" } );

		List<Attribute> attributes;
		
		attributes = doc.getProjectBranch().getAttributesFor(
				AttributeFor.DOCUMENT);
		Attribute docAttributeToRender = null;
		for (int i = 0; i < attributes.size(); i++)
		{
			if (attributes.get(i).getName().equals("name"))
			{
				docAttributeToRender = attributes.get(i);
				List<Attribute> list = new LinkedList<Attribute>();
				list.add(docAttributeToRender);
				documentModel.setAttributesForColumns( list );
				break;
			}
		}
		if ( docAttributeToRender==null )
		{
			docAttributeToRender = doc.getProjectBranch().createAttribute(AttributeFor.DOCUMENT);
			docAttributeToRender.setValueType(AttributeType.STRING);
			docAttributeToRender.setName("Auto Generated Document Attribute");
		}
		
		attributes = doc.getProjectBranch().getAttributesFor(
				AttributeFor.OBJ);
		Attribute objAttributeToRender = null;
		for (int i = 0; i < attributes.size(); i++)
		{
			if (attributes.get(i).getName().equals("name"))
			{
				objAttributeToRender = attributes.get(i);
				List<Attribute> list = new LinkedList<Attribute>();
				list.add(objAttributeToRender);
				documentModel.setAttributesForColumns( list );
				break;
			}
		}
		if ( objAttributeToRender==null )
		{
			objAttributeToRender = doc.getProjectBranch().createAttribute(AttributeFor.OBJ);
			objAttributeToRender.setValueType(AttributeType.STRING);
			objAttributeToRender.setName("Auto Generated Obj Attribute");
		}

		try
		{
			documentTree = new DocumentTree(documentModel,docAttributeToRender,objAttributeToRender);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if ( documentModel.getRowCount()>0 )
		{
			documentTree.setSelectionRow(0);
		}
		// documentTree.setRootVisible(false);
		current = this;

		JScrollPane scroll = new JScrollPane(documentTree);
		scroll.setBorder(new LineBorder(Color.WHITE, 3));

		documentTable = new DocumentTable(documentModel);

		// documentTable.getColumnModel().getColumn(2).setCellEditor(new
		// StringTableCellEditor());
		// documentTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

		// split.setBorder( null );
		// split.setPreferredSize(new Dimension(200, 200));
		setDividerLocation(200);
		setDividerSize(5);
		setBackground(Color.WHITE);
		JScrollPane left = new JScrollPane(documentTree);
		left.setBorder(null);
		setLeftComponent(left);
		JScrollPane right = new JScrollPane(documentTable);
		right.getViewport().setBackground(Color.WHITE);
		right.setBorder(null);
		setRightComponent(right);

	}
	
	public void setPanel(Panel p)
	{
		// panelContainer.setPanel(p);
	}

	public void removePanel()
	{
		// panelContainer.removePanel();
	}

	public void previousPanel()
	{
		// panelContainer.previousPanel();
	}

}

abstract class JAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JAction(String name, char accelerator, char mnemonic)
	{
		super();
		putValue(JAction.NAME, name);
		putValue(JAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(JAction.MNEMONIC_KEY, new Integer(mnemonic));
	}
}

class Copy extends DefaultEditorKit.CopyAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9188206023063337546L;

	public Copy()
	{
		super();
		putValue(JAction.NAME, "Copy");
		putValue(JAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke('C', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	public void actionPerformed(ActionEvent ae)
	{
		super.actionPerformed(ae);
		// getSelectedFrame().getEditorPane().requestFocus();
	}

}

class Cut extends DefaultEditorKit.CutAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Cut()
	{
		super();
		putValue(JAction.NAME, "Cut");
		putValue(JAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke('X', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	public void actionPerformed(ActionEvent ae)
	{
		super.actionPerformed(ae);
		// getSelectedFrame().getEditorPane().requestFocus();
	}
}

class Goto extends JAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Goto()
	{
		super("Goto Line...", 'G', 'G');
	}

	public void actionPerformed(ActionEvent ae)
	{
	}
}
