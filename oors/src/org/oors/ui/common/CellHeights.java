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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JTable;

class CellHeights
{
	private static HashMap<JTable,HashMap<Integer,HashMap<Integer,Integer>>> tables;
	static
	{
		tables = new HashMap<JTable,HashMap<Integer,HashMap<Integer,Integer>>>();
	}
	
	public static void addSize( JTable table, int row, int column, int height )
	{
		HashMap<Integer,HashMap<Integer,Integer>> tableHeights = tables.get(table);
		if ( tableHeights==null )
		{
			tableHeights = new HashMap<Integer,HashMap<Integer,Integer>>();
			tables.put(table,tableHeights);
		}

		HashMap<Integer,Integer> rowHeights = tableHeights.get(row);
		if ( rowHeights==null )
		{
			rowHeights = new HashMap<Integer,Integer>();
			tableHeights.put(row,rowHeights);
		}
		rowHeights.put(column, height);
	}
	
	public static int getNextMaxCellHeight( JTable table, int row, int column )
	{
		HashMap<Integer,HashMap<Integer,Integer>> tableHeights = tables.get(table);
		if ( tableHeights==null )
		{
			return table.getRowHeight();
		}
		HashMap<Integer,Integer> rowHeights = tableHeights.get(row);
		if ( rowHeights==null )
		{
			return table.getRowHeight();
		}
		int result = table.getRowHeight();
		Iterator<Entry<Integer,Integer>> iter = rowHeights.entrySet().iterator();
		while ( iter.hasNext() )
		{
			Entry<Integer,Integer> e = iter.next();
			if ( e.getKey()!=column )
			{
				result = Math.max(result, e.getValue());
			}
		}
		
		return result;
	}
}