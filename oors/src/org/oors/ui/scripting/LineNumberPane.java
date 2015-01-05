/*
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

 * Created on 1/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.oors.ui.scripting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
/**
 * @author waltz
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LineNumberPane extends JPanel implements DocumentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7410213955062357196L;
	private Element root;
	private int lineWidth;
	private int lineHeight;
	public void changedUpdate( DocumentEvent de ) {
		renumber();
	}
	public void insertUpdate( DocumentEvent de ) {
		renumber();
	}
	public void removeUpdate( DocumentEvent de ) {
		renumber();
	}
	private int numLines = 0;
	private void renumber() {
		int lines = root.getElementCount();
		if ( lines==numLines ) return;
		numLines = lines;
		// for resizing .......
		/**
		//setPreferredSize(new Dimension(numLines,1000));
		// if width has changed
		//revalidate();
		// else
		**/
		repaint();
	}

	public void paintComponent(Graphics g) {
		Rectangle bounds = g.getClipBounds();

		// draw background
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(),getHeight());

		// draw numbers
		g.setColor(Color.BLACK);
		int startNumber = (bounds.y/lineHeight)+1;

		int endNumber = startNumber+(Math.abs(bounds.height)/lineHeight);
		if ( endNumber>numLines ) endNumber = numLines;
		//LOG.info("drawing line numbers : "+startNumber +" -> "+ endNumber +" of ("+numLines+") in "+bounds);
		int at = startNumber*lineHeight;
		for ( int i=startNumber ; i<=endNumber ; i++,at+=lineHeight ) {
			String num = String.valueOf(i);
			int numW = fontMetrics.stringWidth(num);
			g.drawString(num,lineWidth-numW,at);
		}
	}

	private FontMetrics fontMetrics;

	public LineNumberPane() {
		super();
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
	}

	public void setJTextComponent( JTextComponent text ) {
		javax.swing.text.Document doc = text.getDocument();
		root = doc.getDefaultRootElement();
		doc.addDocumentListener(this);
		numLines = root.getElementCount();
		if ( doc instanceof StyledDocument ) {
			fontMetrics = getFontMetrics( ((StyledDocument)doc).getFont(root.getAttributes()) );

		} else fontMetrics = getFontMetrics(text.getFont());
		//LOG.info("~~~~~~~~~~ font : "+text.getFont());
		//setFont(text.getFont());

		lineHeight = fontMetrics.getHeight();
		lineWidth = fontMetrics.stringWidth("000");
		// this is very significant, if not set then the getClipBounds returns some strange numbers, (-negative height)
		// this screws up the drawing ....
		setPreferredSize(new Dimension(lineWidth,100000));

		repaint();
	}
}

