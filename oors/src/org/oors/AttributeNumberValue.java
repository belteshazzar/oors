package org.oors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name="ATTRIBUTE_NUMBER_VALUE")
public class AttributeNumberValue extends AttributeValue {

	@Column(name="VALUE")
	protected double value;
	
	protected AttributeNumberValue() {}
	
	AttributeNumberValue( Attribute attribute, long forId, double value )
	{
		super(attribute,forId);
		this.value = value;
	}

	@Override
	public Double getValue()
	{
		return value;
	}
	
	@Override
	public void setValue( Object value )
	{
		this.value = (Double)value;
	}
}
