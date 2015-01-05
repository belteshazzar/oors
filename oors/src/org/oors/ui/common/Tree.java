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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.oors.Attribute;
import org.oors.Base;
import org.oors.ui.Actions;
import org.oors.ui.common.TreeTableModel.TreeNode;

public abstract class Tree extends JTree implements FocusListener,
		org.oors.ui.common.SelectionListener {

	public static final long serialVersionUID = -1L;
	private final static Logger LOG = Logger.getLogger(Tree.class
			.getName());

	public void setAttributesToRender(Attribute rootAttribute,
			Attribute otherAttributes) throws Exception {
		TreeCellRenderer renderer = new TreeCellRenderer(this, rootAttribute,
				otherAttributes);
		setCellRenderer(renderer);
		setCellEditor(new TreeCellEditor(this, renderer));
	}

	private void setSelection() {
		LOG.info("Tree.setSelection()");
		TreePath[] selections = Tree.this.getSelectionPaths();

		if (selections == null) {
			Base[] b = new Base[1];
			b[0] = ((TreeNode) Tree.this.getModel().getRoot()).getRepresents();
			Actions.getInstance().setSelection(b, Tree.this);
		} else {
			Base[] selected = new Base[selections.length];
			for (int i = 0; i < selections.length; i++) {
				selected[i] = ((TreeTableModel.TreeNode) selections[i]
						.getLastPathComponent()).getRepresents();
			}

			Actions.getInstance().setSelection(selected, this);
		}

	}

	public Tree(TreeTableModel model, String context,
			Attribute rootsAttributeToRender, Attribute attributeToRender)
			throws Exception {
		super(model);

		// Log.trace("setting selection from Tree constructor");
		Actions.getInstance().addSelectionListener(this);

		setAttributesToRender(rootsAttributeToRender, attributeToRender);
		setEditable(true);
		addTreeSelectionListener(new SelectionListener());

		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		addMouseListener(new MouseListener());
		addFocusListener(this);
		putClientProperty("JTree.lineStyle", "Angled");
		putClientProperty("Tree.drawsFocusBorderAroundIcon", true);

		// Tree.actionMap ActionMap
		// Tree.ancestorInputMap InputMap
		// Tree.background Color
		// Tree.changeSelectionWithFocus Boolean
		// Tree.closedIcon Icon
		// Tree.collapsedIcon Icon
		putClientProperty("Tree.drawDashedFocusIndicator", true);
		putClientProperty("Tree.drawHorizontalLines", true);
		// Tree.drawsFocusBorderAroundIcon Boolean
		putClientProperty("Tree.drawVerticalLines", true);
		// Tree.editorBorder Border
		// Tree.editorBorderSelectionColor Color
		// Tree.expandedIcon Icon
		// Tree.expanderSize Integer
		// Tree.focusInputMap InputMap
		// Tree.focusInputMap.RightToLeft InputMap
		// Tree.font Font
		// Tree.foreground Color
		// Tree.hash Color
		// Tree.iconBackground Color
		// Tree.iconForeground Color
		// Tree.iconHighlight Color
		// Tree.iconShadow Color
		// Tree.leafIcon Icon
		// Tree.leftChildIndent Integer
		// Tree.line Color
		// Tree.lineTypeDashed Boolean
		// Tree.openIcon Icon
		// Tree.padding Integer
		putClientProperty("Tree.paintLines", true);
		// Tree.rendererUseTreeColors Boolean
		// Tree.rightChildIndent Integer
		// Tree.rowHeight Integer
		// Tree.scrollsHorizontallyAndVertically Boolean
		// Tree.scrollsOnExpand Boolean
		// Tree.selectionBackground Color
		// Tree.selectionBorderColor Color
		// Tree.selectionForeground Color
		setShowsRootHandles(true);
		// Tree.textBackground Color
		// Tree.textForeground Color
		// Tree.timeFactor

	}

	// public void setUI( TreeUI ui )
	// {
	// LOG.info("setUI: "+ui.getClass());
	// super.setUI( new StoneTreeUI() );
	// //super.setUI( ui );
	// }

	private class SelectionListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			LOG.info("tree selection changed");
			setSelection();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		LOG.info("Tree focusGained");
		setSelection();
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

	private class MouseListener extends MouseAdapter {
		/**
		 * The tree selection event occurs before the mouseClick event.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			LOG.info("Tree mouseClicked");
			LOG.info(" - closest: "
					+ Tree.this.getClosestPathForLocation(e.getX(), e.getY()));
			LOG.info(" - path: "
					+ Tree.this.getPathForLocation(e.getX(), e.getY()));

			TreePath path = Tree.this.getClosestPathForLocation(e.getX(),
					e.getY());

			if (path == null) {
				Tree.this.setSelectionPath(new TreePath(
						new Object[] { Tree.this.getModel().getRoot() }));
			} else {
				Tree.this.setSelectionPath(path);
			}
			LOG.info("Path: " + path);

			if (e.getButton() == MouseEvent.BUTTON3) {
				Actions.getInstance().show(e.getComponent(), e.getX() - 5,
						e.getY() - 5);
			}
		}
	}
}

// class CustomTreeUI extends BasicTreeUI {
//
// JScrollPane fScrollPane;
//
// public CustomT
// @Override
// protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
// return new NodeDimensionsHandler() {
// @Override
// public Rectangle getNodeDimensions(
// Object value, int row, int depth, boolean expanded,
// Rectangle size) {
// Rectangle dimensions = super.getNodeDimensions(value, row,
// depth, expanded, size);
// dimensions.width =
// fScrollPane.getWidth() - getRowX(row, depth);
// return dimensions;
// }
// };
// }
//
// @Override
// protected void paintHorizontalLine(Graphics g, JComponent c,
// int y, int left, int right) {
// // do nothing.
// }
//
// @Override
// protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds,
// Insets insets, TreePath path) {
// // do nothing.
// }
// }

// class StoneTreeUI extends BasicTreeUI {
//
// public static ComponentUI createUI(JComponent c) {
// return new StoneTreeUI();
// }
//
// @Override
// protected void installListeners() {
// super.installListeners();
//
// tree.addComponentListener(componentListener);
// }
//
// @Override
// protected void uninstallListeners() {
// tree.removeComponentListener(componentListener);
//
// super.uninstallListeners();
// }
//
// @Override
// protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
// return new NodeDimensionsHandler() {
// @Override
// public Rectangle getNodeDimensions(Object value, int row, int depth, boolean
// expanded, Rectangle size) {
// Rectangle dimensions = super.getNodeDimensions(value, row, depth, expanded,
// size);
// Insets insets = tree.getInsets();
// dimensions.width = tree.getWidth()-getRowX(row,
// depth)-insets.left-insets.right;
// return dimensions;
// }
// };
// }
//
// private final ComponentListener componentListener = new ComponentAdapter() {
// @Override
// public void componentResized(ComponentEvent e) {
// treeState.invalidateSizes();
// tree.repaint();
// };
// };
//
// }

