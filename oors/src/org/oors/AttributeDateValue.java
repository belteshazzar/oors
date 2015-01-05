package org.oors;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name="ATTRIBUTE_DATE_VALUE")
public class AttributeDateValue extends AttributeValue {

	@Column(name="VALUE")
	protected Date value;
	
	protected AttributeDateValue() {}
	
	AttributeDateValue( Attribute attribute, long forId, Date value )
	{
		super(attribute,forId);
		this.value = value;
	}

	@Override
	public Date getValue()
	{
		return value;
	}
	
	@Override
	public void setValue( Object value )
	{
		this.value = (Date)value;
	}
}
