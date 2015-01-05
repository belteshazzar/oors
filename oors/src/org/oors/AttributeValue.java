package org.oors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
@IdClass(AttributeValueId.class)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class AttributeValue {

	@Id
	@Column(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	protected long projectBranchId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	ProjectBranch projectBranch;

	@Id
	@Column(name = "ATTRIBUTE_ID",insertable=false,updatable=false)
	protected long attributeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "PROJECT_BRANCH_ID",referencedColumnName="PROJECT_BRANCH_ID",insertable=false,updatable=false),
		@JoinColumn(name = "ATTRIBUTE_ID",referencedColumnName="ID",insertable=false,updatable=false)
	})
	Attribute attribute;

	@Id
	@Column(name = "VALUE_FOR_ID",insertable=false,updatable=false)
	protected long valueForId;
	
	protected AttributeValue() {} 
	
	protected AttributeValue( Attribute attribute, long forId )
	{
		this.attribute = attribute;
		this.attributeId = attribute.id;
		this.projectBranch = attribute.getProjectBranch();
		this.projectBranchId = this.projectBranch.id;
		this.valueForId = forId;
	}
	
	public Object getValue()
	{
		return null;
	}
	
	public void setValue( Object value )
	{
		
	}

}
