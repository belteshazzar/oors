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
import java.util.logging.Logger;

import org.oors.Attribute;
import org.oors.AttributeFor;
import org.oors.Document;
import org.oors.ProjectBranch;
import org.oors.ui.common.Panel;
import org.oors.ui.common.PanelContainer;

public class DocumentAttributesPanel extends Panel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7902161590040617308L;
	private final static Logger LOG = Logger.getLogger(DocumentAttributesPanel.class
			.getName());

	public DocumentAttributesPanel(PanelContainer pc, Document doc)
	{
		super(pc, "document title", "Document Attributes", "test");
		previous.setEnabled(false);
		next.setEnabled(false);
		cancel.setEnabled(false);

		ProjectBranch projectBranch = doc.getProjectBranch();
		List<Attribute> attributes = projectBranch.getAttributesFor(AttributeFor.DOCUMENT);
		for ( int i=0 ; i<attributes.size() ; i++ )
		{
			LOG.info(attributes.get(i).toString());
		}
	}
	
	@Override
	protected void finishPressed()
	{
		getPanelContainer().removePanel();
	}

}
