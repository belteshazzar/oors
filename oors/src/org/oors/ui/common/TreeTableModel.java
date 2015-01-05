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

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.oors.Attribute;
import org.oors.AttributeException;
import org.oors.AttributeType;
import org.oors.Base;
import org.oors.OorsEventGenerator;
import org.oors.OorsEvent;
import org.oors.OorsListener;

public abstract class TreeTableModel implements TreeModel, TableModel
{
	public TreeNode root;
	protected TreeNode tableParent;
	public Vector<TreeNode> flatNodes = null;
	private boolean showContents;

	public TreeTableModel( boolean showContents )
	{
		root = null;
		tableParent = null;
		this.showContents = showContents;
		if ( !showContents ) flatNodes = new Vector<TreeNode>();
		this.attributeTypeClasses  = new EnumMap<AttributeType,Class<?>>(AttributeType.class);
		this.attributeTypeClasses.put(AttributeType.BOOLEAN, Boolean.class);
		this.attributeTypeClasses.put(AttributeType.DATE, java.util.Date.class);
		this.attributeTypeClasses.put(AttributeType.HTML,javax.swing.text.html.HTMLDocument.class );
		this.attributeTypeClasses.put(AttributeType.STRING, String.class);
		this.attributeTypeClasses.put(AttributeType.NUMBER,Double.class);
		this.attributeTypeClasses.put(AttributeType.USER,null);
		this.attributeTypeClasses.put(AttributeType.USERGROUP,null);


	}

	protected void setRoot(TreeNode root)
	{
		this.root = root;
		tableParent = root;

		if ( !showContents )
		{
			flatNodes.clear();
			for ( int i=0 ; i<tableParent.children.size() ; i++ )
			{
				createFlatNodes(tableParent.children.get(i));
			}
		}
	}
	
	private void createFlatNodes( TreeNode tn )
	{
		flatNodes.add(tn);
		for ( int i=0 ; i<tn.children.size() ; i++ )
		{
			createFlatNodes(tn.children.get(i));
		}
	}

	private TreeNode[] getPathToRoot(TreeNode node)
	{
		Vector<TreeNode> tns = new Vector<TreeNode>();
		while (node != null)
		{
			tns.add(node);
			node = node.parent;
		}
		TreeNode[] path = new TreeNode[tns.size()];
		for (int i = 0; i < tns.size(); i++)
		{
			path[tns.size() - i - 1] = tns.get(i);
		}
		return path;
	}

	public class TreeNode implements OorsListener
	{
		TreeNode parent;

		public TreeNode getParent()
		{
			return parent;
		}

		public void setParent(TreeNode parent)
		{
			this.parent = parent;
		}

		protected Vector<TreeNode> children;

		public Vector<TreeNode> getChildren()
		{
			return children;
		}

		public void setChildren(Vector<TreeNode> children)
		{
			this.children = children;
		}

		public int indexOf(TreeNode child)
		{
			int result = -1;
			for (int i = 0; i < children.size(); i++)
			{
				if (children.get(i) == child)
				{
					result = i;
					break;
				}
			}
			return result;
		}

		public void insertChild(Base represents)
		{
			TreeNode tn = new TreeNode(this, represents);
			
			fireTreeNodeInserted(tn);
		}

		protected Base represents;

		public Base getRepresents()
		{
			return represents;
		}

		public void setRepresents(Base represents)
		{
			this.represents = represents;
		}

		protected List<? extends Base> contents;

		public List<? extends Base> getContents()
		{
			return contents;
		}

		public void setContents(List<? extends Base> contents)
		{
			this.contents = contents;
			if (tableParent == this)
			{
				fireTableDataChanged();
			}
		}

		public TreeNode()
		{
			parent = null;
			children = new Vector<TreeNode>();
			represents = null;
			contents = new LinkedList<Base>();
		}

		public TreeNode(TreeNode parent, Base represents)
		{
			this.parent = parent;
			children = new Vector<TreeNode>();
			if ( parent!=null )
			{
				parent.children.add(this);
			}
			this.represents = represents;
			represents.addOorsListener(this);
			this.contents = new LinkedList<Base>();
		}
		
		public TreeNode find( Base b )
		{
			if ( represents==b ) return this;
			for ( int i=0 ; i<children.size() ; i++ )
			{
				TreeNode r = children.get(i).find(b);
				if ( r!=null ) return r;
			}
			return null;
		}

		@Override
		public void createdUpdate(OorsEvent event)
		{
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

	public void dispose()
	{
		dispose(root);
	}

	private void dispose(TreeNode n)
	{
		n.represents.removeOorsListener(n);
		for (TreeNode ns : n.children)
		{
			dispose(ns);
		}
		n.children = null;
		n.parent = null;
		n.represents = null;
		n.contents = null;
	}

	/*
	 * ************************************************************
	 * 
	 * EVENT LISTENER METHODS
	 * 
	 * ************************************************************
	 */

	protected EventListenerList listeners = new EventListenerList();

	public void addTableModelListener(TableModelListener l)
	{
		listeners.add(TableModelListener.class, l);
	}

	public void removeTableModelListener(TableModelListener l)
	{
		listeners.remove(TableModelListener.class, l);
	}

	public void addTreeModelListener(TreeModelListener l)
	{
		listeners.add(TreeModelListener.class, l);
	}

	public void removeTreeModelListener(TreeModelListener l)
	{
		listeners.remove(TreeModelListener.class, l);
	}

	/*
	 * ************************************************************
	 * 
	 * TREE TABLE MODEL INTERFACE
	 * 
	 * ************************************************************
	 */

	public void setTableParent(TreeNode node)
	{
		tableParent = node;
		if ( ! showContents )
		{
			this.flatNodes.clear();
			for ( int i=0 ; i<tableParent.children.size() ; i++ )
			{
				createFlatNodes(tableParent.children.get(i));
			}			
		}
		fireTableDataChanged();
	}
	public void setTableParent(Base b)
	{
		TreeNode node = root.find( b );
		tableParent = node;
		if ( ! showContents )
		{
			this.flatNodes.clear();
			for ( int i=0 ; i<tableParent.children.size() ; i++ )
			{
				createFlatNodes(tableParent.children.get(i));
			}			
		}
		fireTableDataChanged();
	}


	public TreeNode getTableParent()
	{
		return tableParent;
	}

	public Base getRow(int index)
	{
		if (tableParent == null) return null;
		if ( showContents )
		{
			if (index < 0 || index >= tableParent.contents.size()) return null;
			return tableParent.contents.get(index);
		}
		else
		{
			if ( index<0 || index >= flatNodes.size() ) return null;
			return flatNodes.get(index).getRepresents();
		}
	}
	
	private List<Attribute> attributesForColumns = new LinkedList<Attribute>();

	public void setAttributesForColumns( List<Attribute> attributes )
	{
		attributesForColumns = attributes;
		fireTableDataChanged();
	}
	
	public Attribute getAttributeForColumn( int column )
	{
		return attributesForColumns.get(column);
	}

	/*
	 * **************************************************************************
	 * 
	 * TABLE MODEL INTERFACE
	 * 
	 * **************************************************************************
	 */

	private EnumMap<AttributeType,Class<?>> attributeTypeClasses;

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return attributeTypeClasses.get(attributesForColumns.get(columnIndex).getValueType());
	}

	@Override
	public int getColumnCount()
	{
		return attributesForColumns.size();
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return attributesForColumns.get(columnIndex).getName();
	}

	@Override
	public int getRowCount()
	{
		if ( showContents )
		{
			if ( tableParent.getContents()==null ) return 0;
			return tableParent.getContents().size();
		}
		else
		{
			return flatNodes.size();
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if ( showContents )
		{
			try {
				return tableParent.getContents().get(rowIndex)
					.getValue(attributesForColumns.get(columnIndex));
			} catch (AttributeException e) {
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			try {
				return flatNodes.get(rowIndex).getRepresents().getValue(attributesForColumns.get(columnIndex));
			} catch (AttributeException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		Attribute attr = attributesForColumns.get(columnIndex);
		Base b;
		if ( showContents )
		{
			b = tableParent.getContents().get(rowIndex);
		}
		else
		{
			b = flatNodes.get(rowIndex).getRepresents();
		}
		
		try {
			b.setValue(attr, aValue);
		} catch (AttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * ************************************************************
	 * 
	 * TREE INTERFACE
	 * 
	 * ************************************************************
	 */

	@Override
	public Object getChild(Object parent, int index)
	{
		TreeNode n = (TreeNode) parent;
		if (index < 0 || index >= n.getChildren().size())
		{
			return null;
		}
		else
		{
			return n.getChildren().get(index);
		}
	}

	@Override
	public int getChildCount(Object parent)
	{
		TreeNode n = (TreeNode) parent;
		return n.getChildren().size();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		int result = 0;
		TreeNode p = (TreeNode) parent;
		for (int i = 0; i < p.getChildren().size(); i++)
		{
			if (p.getChildren().get(i) == child)
			{
				result = i;
				break;
			}
		}
		return result;
	}

	@Override
	public Object getRoot()
	{
		return root;
	}

	@Override
	public boolean isLeaf(Object node)
	{
		TreeNode n = (TreeNode) node;
		return n.getChildren().size() == 0;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
	}

	/*
	 * ************************************************************
	 * 
	 * TABLE MODEL CHANGE METHODS
	 * 
	 * ************************************************************
	 */

	public void fireTableDataChanged()
	{
		fireTableChanged(new TableModelEvent(this));
	}

	public void fireTableStructureChanged()
	{
		fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
	}

	public void fireTableRowsInserted(int firstRow, int lastRow)
	{
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	public void fireTableRowsUpdated(int firstRow, int lastRow)
	{
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
	}

	public void fireTableRowsDeleted(int firstRow, int lastRow)
	{
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
	}

	public void fireTableCellUpdated(int row, int column)
	{
		fireTableChanged(new TableModelEvent(this, row, row, column));
	}

	public void fireTableChanged(TableModelEvent e)
	{
		Object[] ls = listeners.getListenerList();
		for (int i = ls.length - 2; i >= 0; i -= 2)
		{
			if (ls[i] == TableModelListener.class)
			{
				((TableModelListener) ls[i + 1]).tableChanged(e);
			}
		}
	}

	/*
	 * ************************************************************
	 * 
	 * TREE MODEL CHANGE METHODS
	 * 
	 * ************************************************************
	 */

	/**
	 * Invoked after a node (or a set of siblings) has changed in some way. The
	 * node(s) have not changed locations in the tree or altered their children
	 * arrays, but other attributes have changed and may affect presentation.
	 * Example: the name of a file has changed, but it is in the same location
	 * in the file system.
	 * 
	 * To indicate the root has changed, childIndices and children will be null.
	 */
	protected void fireTreeNodeChanged(TreeNode node)
	{
		// Guaranteed to return a non-null array
		Object[] ls = listeners.getListenerList();
		TreeModelEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = ls.length - 2; i >= 0; i -= 2)
		{
			if (ls[i] == TreeModelListener.class)
			{
				// Lazily create the event:
				if (e == null)
				{
					if (node == root)
					{
						e = new TreeModelEvent(this,
								getPathToRoot(node.getParent()), null, null);
					}
					else
					{
						e = new TreeModelEvent(this,
								getPathToRoot(node.getParent()),
								new int[] { node.getParent().indexOf(node) },
								new Object[] { node });
					}
				}
				((TreeModelListener) ls[i + 1]).treeNodesChanged(e);
			}
		}
	}

	/**
	 * Invoked after nodes have been inserted into the tree.
	 */
	protected void fireTreeNodeInserted(TreeNode insertedNode)
	{
		
		if ( !showContents )
		{
			// there has to be a much better way of
			// doing this but do it the inefficient way
			// for now.
			setRoot(root);
			int index = flatNodes.indexOf(insertedNode);
			fireTableRowsInserted(index,index);
		}
		
		// Guaranteed to return a non-null array
		Object[] ls = listeners.getListenerList();
		TreeModelEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = ls.length - 2; i >= 0; i -= 2)
		{
			if (ls[i] == TreeModelListener.class)
			{
				// Lazily create the event:
				if (e == null)
				{
					if (insertedNode == root)
					{
						e = new TreeModelEvent(this,
								getPathToRoot(insertedNode.getParent()), null,
								null);
					}
					else
					{
						e = new TreeModelEvent(this,
								getPathToRoot(insertedNode.getParent()),
								new int[] { insertedNode.getParent().indexOf(
										insertedNode) },
								new Object[] { insertedNode });
					}
				}
				((TreeModelListener) ls[i + 1]).treeNodesInserted(e);
			}
		}
	}

	/*
	 * Invoked after nodes have been removed from the tree. Note that if a
	 * subtree is removed from the tree, this method may only be invoked once
	 * for the root of the removed subtree, not once for each individual set of
	 * siblings removed.
	 */
	protected void fireTreeNodeRemoved(TreeNode parent, TreeNode child,
			int childIndex)
	{
		// Guaranteed to return a non-null array
		Object[] ls = listeners.getListenerList();
		TreeModelEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = ls.length - 2; i >= 0; i -= 2)
		{
			if (ls[i] == TreeModelListener.class)
			{
				// Lazily create the event:
				if (e == null)
				{
					e = new TreeModelEvent(this, getPathToRoot(parent),
							new int[] { childIndex }, new Object[] { child });
				}
				((TreeModelListener) ls[i + 1]).treeNodesRemoved(e);
			}
		}
	}

	protected void fireTreeStructureChanged(TreeNode node)
	{
		// Guaranteed to return a non-null array
		Object[] ls = listeners.getListenerList();
		TreeModelEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = ls.length - 2; i >= 0; i -= 2)
		{
			if (ls[i] == TreeModelListener.class)
			{
				// Lazily create the event:
				if (e == null)
				{
					e = new TreeModelEvent(this, getPathToRoot(node), null,
							null);
				}
				((TreeModelListener) ls[i + 1]).treeStructureChanged(e);
			}
		}
	}
}
