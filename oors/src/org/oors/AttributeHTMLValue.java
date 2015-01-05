package org.oors;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name="ATTRIBUTE_HTML_VALUE")
public class AttributeHTMLValue extends AttributeValue {

	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="VALUE")
	protected javax.swing.text.html.HTMLDocument value;
	
	protected AttributeHTMLValue() {}
	
	AttributeHTMLValue( Attribute attribute, long forId, javax.swing.text.html.HTMLDocument value )
	{
		super(attribute,forId);
		this.value = value;
	}

	@Override
	public javax.swing.text.html.HTMLDocument getValue()
	{
		return value;
	}
	
	@Override
	public void setValue( Object value )
	{
		this.value = (javax.swing.text.html.HTMLDocument)value;
	}
}
