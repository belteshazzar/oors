package org.oors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name="ATTRIBUTE_USER_GROUP_VALUE")
public class AttributeUserGroupValue extends AttributeValue {

	@Column(name="VALUE")
	protected long value;
	
	protected AttributeUserGroupValue() {}
	
	AttributeUserGroupValue( Attribute attribute, long forId, long value )
	{
		super(attribute,forId);
		this.value = value;
	}

	@Override
	public Long getValue()
	{
		return value;
	}
	
	@Override
	public void setValue( Object value )
	{
		this.value = (Long)value;
	}
}
