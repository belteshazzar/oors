package org.oors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Table;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name = "ATTRIBUTES")
public class Attribute extends OorsEventGenerator {

	@Id
	@Column(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	protected long projectBranchId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	protected ProjectBranch projectBranch;

	@Id
	@Column(name = "ID",insertable=false,updatable=false)
	protected long id;
	
	@Column(name="NAME")
	protected String name;
	
	@Column(name="FOR_TYPE")
	protected AttributeFor forType;
	
	@Column(name="VALUE_TYPE")
	protected AttributeType valueType;
	
	public String toString()
	{
		return "Attribute[pb="+this.projectBranchId+",id="+this.id+",name="+this.name+",for="+this.forType+",type="+valueType+"]";
	}

	protected Attribute()
	{
		
	}
	
	Attribute( ProjectBranch branch, String name, AttributeFor forType, AttributeType valueType )
	{
		this.projectBranch = branch;
		this.projectBranchId = branch.id;
		
		Query q = DataSource.getInstance()
				.entityManager.createQuery ("SELECT MAX(a.id) FROM Attribute a");
		try
		{
			Number result = (Number) q.getSingleResult ();
			if ( result==null ) this.id = 1;
			else this.id = result.longValue() + 1;
		}
		catch ( NoResultException nrex )
		{
			this.id = 1;
		}

		this.name = name;
		this.forType = forType;
		this.valueType = valueType;
	}
	
	public ProjectBranch getProjectBranch()
	{
		return this.projectBranch;
	}
	
	protected void setProjectBranch( ProjectBranch projectBranch )
	{
		this.projectBranch = projectBranch;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName( String name )
	{
		this.name = name;
	}
	
	public AttributeFor getForType()
	{
		return this.forType;
	}
	
	public void setForType( AttributeFor forType )
	{
		this.forType = forType;
	}
	
	public AttributeType getValueType()
	{
		return this.valueType;
	}
	
	public void setValueType( AttributeType valueType )
	{
		this.valueType = valueType;
	}
	
}
