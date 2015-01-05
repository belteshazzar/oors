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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;

import org.oors.Base;
import org.oors.ui.Actions;

public abstract class Table extends JTable implements SelectionListener, FocusListener
{
	public static final long serialVersionUID = -1L;
	private final static Logger LOG = Logger.getLogger(Table.class
			.getName());

	protected TreeTableModel model;
	private Timer editTimer;
	private boolean canEditCell = false;
	private int row = -1;
	private int col = -1;
	StringTableCellEditor stringEditor = null;

	@Override
	public void focusGained(FocusEvent e)
	{
		LOG.info("Table FocusEvent");
		setSelection();
	}

	@Override
	public void focusLost(FocusEvent e)
	{
	}
	
	private void setSelection()
	{
		LOG.info("Table setSelection");
		int[] selectedRows = Table.this.getSelectedRows();
		if ( selectedRows.length==0 )
		{
			Base[] b = new Base[1];
			b[0] = Table.this.model.tableParent.represents;
			Actions.getInstance().setSelection(b, Table.this);
		}
		else 
		{
			Base[] selections = new Base[selectedRows.length];
			for ( int i=0 ; i<selections.length ; i++ )
			{
				selections[i] = model.getRow(selectedRows[i]);
			}
			Actions.getInstance().setSelection(selections, Table.this);
		}

	}
	protected void openRow(Base rowObject)
	{
	}

	public final boolean isCellEditable(int row, int column)
	{
		return canEditCell;
	}
//    public TableCellEditor getCellEditor(int row, int column)
//    {
//    	LOG.info("Table getCellEditor");
//    	int col = convertColumnIndexToModel(column);
//    	Attribute attr = model.getAttributeForColumn(col);
//        	
//    	if ( attr.getValueClass()==java.util.Date.class )
//    	{
//    		return new DateTableCellEditor();
//    	}
//    	else if ( attr.getValueClass()==DerbyUser.class )
//    	{
//    		return new UserTableCellEditor();
//    	}
//    	else if ( attr.getValueClass()==Boolean.class)
//    	{
//    		return new BooleanTableCellEditor();
//    	}
//    	else if ( attr.getValueClass()==String.class )
//    	{
//    		return new StringTableCellEditor();
//    	}
//    	else if ( attr.getValueClass()==Double.class )
//    	{
//    		return new NumberTableCellEditor();
//    	}
//    	else if ( attr.getValueClass()==javax.swing.text.html.HTMLDocument.class)
//    	{
//    		return new DocumentTableCellEditor();
//    	}
//    	
//    	throw new Exception("unreckognised attribute value class");
//    }
  
//    public TableCellRenderer getCellRenderer(int row, int column)
//    {
//    	int col = convertColumnIndexToModel(column);
//    	Attribute attr = model.getAttributeForColumn(col);
//
//    	if ( attr.getValueClass()==Document.class)
//    	{
//    		return new DocumentTableCellRenderer();
//    	}
//    	else// if ( attr.getValueClass()==String.class )
//    	{
//    		return new StringTableCellRenderer();
//    	}
//    }

	protected JTableHeader createDefaultTableHeader()
	{
		JTableHeader header = new JTableHeader(columnModel);
		header.addMouseListener(new HeaderMouseListener());
		return header;
	}

	class HeaderMouseListener extends MouseAdapter
	{		
		private void rightHeaderClick(MouseEvent me, int col)
		{
			Actions.getInstance().show(me.getComponent(), me.getX()-5, me.getY()-5);
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			int _col = convertColumnIndexToModel(columnAtPoint(e.getPoint()));
			if (e.getButton() == MouseEvent.BUTTON3)
			{
				rightHeaderClick(e, _col);
			}

		}
	}
	
	private class SelectionListener implements ListSelectionListener
	{

		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			LOG.info("Table ListSelectionEvent");
			if ( !e.getValueIsAdjusting() ) setSelection();
		}
	}
		
	private class ClickListener extends MouseAdapter
	{

		@Override
		public void mouseClicked(MouseEvent ev)
		{
			LOG.info("Table MouseEvent");

			int _row = rowAtPoint(ev.getPoint());
			int _col = columnAtPoint(ev.getPoint());

			if (ev.getButton()==MouseEvent.BUTTON1 && _row==-1 )
			{
				if ( Table.this.getSelectedRowCount()!=0 )
				{
					Table.this.getSelectionModel().clearSelection();
					Base[] b = new Base[1];
					b[0] = model.getTableParent().getRepresents();
					//Actions.getInstance().setSelection(b,Table.this);
					openRow(model.getTableParent().getRepresents());

					if ( editTimer.isRunning() )
					{
						editTimer.stop();
					}
				}
				row = _row;
				col = _col;

			}
			else if ( ev.getButton()==MouseEvent.BUTTON3)
			{
				if ( editTimer.isRunning() )
				{
					editTimer.stop();
				}
				Table.this.getSelectionModel().clearSelection();
				if ( _row!=-1 )
					Table.this.getSelectionModel().setSelectionInterval(_row, _row);
				Actions.getInstance().show(ev.getComponent(), ev.getX()-5, ev.getY()-5);
			}
			else if ( ev.getButton()==MouseEvent.BUTTON1 )
			{
				_row = convertRowIndexToModel(_row);
				_col = convertColumnIndexToModel(_col);
				if ( ev.getClickCount()==1 )
				{
					if ( _row==row && _col==col )
					{
						if ( editTimer.isRunning() )
						{
							editTimer.stop();
						}
						else
						{
							editTimer.start();
						}
					}
				}
				else if ( ev.getClickCount()==2 )
				{
					if ( editTimer.isRunning() )
					{
						editTimer.stop();
					}
					openRow(model.getRow(_row));					
				}
				row = _row;
				col = _col;

			}
			else
			{
				if ( editTimer.isRunning() )
				{
					editTimer.stop();
				}			
			}
		}
	}
	
	private class ModelListener implements TableModelListener
	{

		@Override
		public void tableChanged(TableModelEvent e)
		{
			removeEditor();
			editTimer.stop();
		}
		
	}
	
	private class TimerListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			canEditCell = true;
			editCellAt(row, col);
			if (editorComp != null)
			{
				editorComp.requestFocus();
			}
			canEditCell = false;	
		}
		
	}

	public Table(TreeTableModel model, String context, String headerContext )
	{
		super(model);
		
		this.model = model;
		
		addMouseListener( new ClickListener());
		model.addTableModelListener( new ModelListener());
		this.getSelectionModel().addListSelectionListener(new SelectionListener());
		Actions.getInstance().addSelectionListener(this);
		
		editTimer = new Timer(350, new TimerListener());
		editTimer.setRepeats(false);

		setAutoCreateRowSorter(true);
		setRowHeight(36);
		configureEnclosingScrollPane();
		setFillsViewportHeight(true);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setAutoscrolls(true);
//		this.addFocusListener(this);
		
		this.setDefaultEditor(java.util.Date.class,new net.sf.nachocalendar.table.DateFieldTableEditor());
//    	this.setDefaultEditor(DerbyUser.class,new UserTableCellEditor(model.root.getRepresents().getProjectBranch().getProject()));
    	this.setDefaultEditor(Boolean.class,new BooleanTableCellEditor());
    	this.setDefaultEditor(String.class,new StringTableCellEditor());
    	this.setDefaultEditor(Double.class,new NumberTableCellEditor());
    	this.setDefaultEditor(javax.swing.text.html.HTMLDocument.class,new DocumentTableCellEditor());
    	
    	this.setDefaultRenderer(java.util.Date.class, new StringTableCellRenderer());//new net.sf.nachocalendar.table.DateRendererDecorator());
  //  	this.setDefaultRenderer(DerbyUser.class, new StringTableCellRenderer());
    	this.setDefaultRenderer(Boolean.class,new StringTableCellRenderer());
    	this.setDefaultRenderer(String.class,new StringTableCellRenderer());
    	this.setDefaultRenderer(Double.class,new StringTableCellRenderer());
    	this.setDefaultRenderer(javax.swing.text.html.HTMLDocument.class,new DocumentTableCellRenderer());
	}

}