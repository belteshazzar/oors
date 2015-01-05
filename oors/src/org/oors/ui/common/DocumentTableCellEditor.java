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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class DocumentTableCellEditor extends AbstractCellEditor implements
		TableCellEditor
{
	public static final long serialVersionUID = -1L;
	private final static Logger LOG = Logger.getLogger(DocumentTableCellEditor.class
			.getName());

	private static DocumentTableCellEditor me;
	private static ResizingJTextArea textArea;
	private static JTable table;
	private static int row;
	private static int column;
	public static Action boldAction;
	
	static
	{
		me = new DocumentTableCellEditor();
		textArea = new ResizingJTextArea();

	}
	
	public static DocumentTableCellEditor getInstance()
	{
		return me;
	}

	public DocumentTableCellEditor()
	{
	}

	// Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue()
	{
		textArea.getDocument().removeDocumentListener(textArea);
		LOG.info("getCellEditorValue: "+textArea.getDocument().hashCode());

		return textArea.getDocument();
	}

	// Implement the one method defined by TableCellEditor.
	@SuppressWarnings("unused")
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		DocumentTableCellEditor.table = table;
		DocumentTableCellEditor.row = row;
		DocumentTableCellEditor.column = column;
		
		LOG.info("getTableCellEditorComponent: "+value.hashCode());
		if ( value==null ) DocumentTableCellEditor.textArea.setDocument(new HTMLDocument());
		else DocumentTableCellEditor.textArea.setDocument((HTMLDocument)value);
				
		DocumentTableCellEditor.textArea.getDocument().addDocumentListener(textArea);
		return DocumentTableCellEditor.textArea;
	}

	private static class ResizingJTextArea extends JTextPane implements DocumentListener
	{
		public static final long serialVersionUID = -1L;

		public ResizingJTextArea()
		{
			super();
			this.setEditorKit(new HTMLEditorKit());
			this.setBorder(new EmptyBorder(1, 1, 1,1));

	      KeyStroke key;
	      key = KeyStroke.getKeyStroke(KeyEvent.VK_B,InputEvent.CTRL_DOWN_MASK);
	      this.getInputMap().put(key, "font-bold");
	      key = KeyStroke.getKeyStroke(KeyEvent.VK_I,InputEvent.CTRL_DOWN_MASK);
	      this.getInputMap().put(key, "font-italic");
	      key = KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_DOWN_MASK);
	      this.getInputMap().put(key, "font-underline");
		}

		public void resizeRow()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					int currentHeight = table.getRowHeight(row);
					int preferredHeight = getPreferredSize().height;
					CellHeights.addSize(table,row,column,preferredHeight);
					int nextMaxHeight = CellHeights.getNextMaxCellHeight(table,row,column);
					if ( preferredHeight>=nextMaxHeight && preferredHeight!=currentHeight )
					{
						table.setRowHeight(row, preferredHeight);
					}
				}
			});
		}

		public void removeUpdate(DocumentEvent ev)
		{
			resizeRow();
		}

		public void insertUpdate(DocumentEvent ev)
		{
			resizeRow();
		}

		public void changedUpdate(DocumentEvent ev)
		{
			resizeRow();
		}
		
//	    class MyPasteAction extends TextAction
//	    {
//	      public MyPasteAction()
//	      {
//	        super(DefaultEditorKit.pasteAction);
//	      }
//	     
//	      public void actionPerformed(ActionEvent e)
//	      {
//	        JTextComponent target = getTextComponent(e);
//	     
//	        Toolkit toolkit = Toolkit.getDefaultToolkit();
//	        Clipboard clipboard = toolkit.getSystemClipboard();
//	        Transferable clipData = clipboard.getContents(clipboard);
//	        if (clipData != null)
//	        {
//	          try
//	          {
//	            if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor))
//	            {
//	              target.replaceSelection(
//	                  (String)(clipData.getTransferData(DataFlavor.stringFlavor)));
//	            }
//	            else
//	            {
//	              toolkit.beep();
//	            }
//	          }
//	          catch (Exception exc)
//	          {
//	            LOG.info("Problems pasting data from clipboard: " + exc);
//	          }
//	        }
//	      }
//	    }
	}
}