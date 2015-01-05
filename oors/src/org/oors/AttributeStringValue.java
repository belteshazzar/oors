package org.oors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name="ATTRIBUTE_STRING_VALUE")
public class AttributeStringValue extends AttributeValue {

	@Column(name="VALUE")
	protected String value;
	
	protected AttributeStringValue() {}
	
	AttributeStringValue( Attribute attribute, long forId, String value )
	{
		super(attribute,forId);
		this.value = value;
	}

	@Override
	public String getValue()
	{
		return value;
	}
	
	@Override
	public void setValue( Object value )
	{
		this.value = (String)value;
	}
}
