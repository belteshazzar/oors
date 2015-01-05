package org.oors;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name = "LINKS")
public class Link extends Base {

	@Id
	@Column(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	protected long projectBranchId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	protected ProjectBranch projectBranch;

	@Id
	@Column(name = "ID",insertable=false,updatable=false)
	protected long id;

	@Column(name = "SOURCE_OBJ_ID")
	protected long sourceObjId;

	@Transient
	Obj source;

	@Column(name = "DEST_OBJ_ID")
	protected long destObjId;

	@Transient
	Obj destination;
	

	protected Link()
	{
		
	}
	
	Link( ProjectBranch branch, Obj source, Obj destination )
	{
		Query q = DataSource.getInstance()
				.entityManager.createQuery ("SELECT MAX(l.id) FROM Link l");
		Number result = (Number) q.getSingleResult ();

		if ( result==null ) this.id = 1;
		else this.id = result.longValue() + 1;
		
		this.projectBranch = branch;
		this.projectBranchId = branch.id;
		
		this.source = source;
		this.sourceObjId = source.id;
		
		this.destination = destination;
		this.destObjId = destination.id;
	}
	
	protected void setProjectBranchId( long id )
	{
		this.projectBranchId = id;
	}
	
	protected long getProjectBranchId()
	{
		return this.projectBranchId;
	}
	
	protected void setProjectBranch( ProjectBranch pb )
	{
		this.projectBranch = pb;
	}
	
	public ProjectBranch getProjectBranch()
	{
		return this.projectBranch;
	}
	
	protected long getId()
	{
		return this.id;
	}
	
	protected void setId( long id )
	{
		this.id = id;
	}
	
	protected long getDestObjId()
	{
		return this.destObjId;
	}
	
	protected void setDestObjId( long id )
	{
		this.destObjId = id;
	}
	
	protected long getSourceObjId()
	{
		return this.sourceObjId;
	}
	
	protected void setSourceObjId( long id )
	{
		this.sourceObjId = id;
	}
	
	public Obj getSource()
	{
		if ( source == null )
		{
			Collection<Obj> fs = DataSource
					.getInstance()
					.entityManager
					.createQuery("FROM Obj o WHERE o.id = "+this.sourceObjId+" AND o.projectBranchId = "+this.projectBranchId, Obj.class)
					.getResultList();
			if ( fs!=null ) source = fs.iterator().next(); 
		}
		return source;
		
	}
	
	public Obj getDestination()
	{
		if ( destination == null )
		{
			Collection<Obj> fs = DataSource
					.getInstance()
					.entityManager
					.createQuery("FROM Obj o WHERE o.id = "+this.destObjId+" AND o.projectBranchId = "+this.projectBranchId, Obj.class)
					.getResultList();
			if ( fs!=null ) destination = fs.iterator().next(); 
		}
		return destination;
	}
	
	public String toString()
	{
		return "Link[projectBranchId="+this.projectBranchId+",id="+this.id+",from="+this.getSource()+",to="+this.getDestination()+"]";
	}

}
