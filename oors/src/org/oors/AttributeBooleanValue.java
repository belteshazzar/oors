package org.oors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name="ATTRIBUTE_BOOLEAN_VALUE")
public class AttributeBooleanValue extends AttributeValue {

	@Column(name="VALUE")
	protected boolean value;
	
	protected AttributeBooleanValue() {}
	
	AttributeBooleanValue( Attribute attribute, long forId, boolean value )
	{
		super(attribute,forId);
		this.value = value;
	}

	@Override
	public Boolean getValue()
	{
		return value;
	}
	
	@Override
	public void setValue( Object value )
	{
		this.value = (Boolean)value;
	}
}
