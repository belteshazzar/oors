package org.oors;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
import javax.persistence.Transient;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name = "OBJS")
public class Obj extends Base {

	@Id
	@Column(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	protected long projectBranchId;

	@Id
	@Column(name = "ID",insertable=false,updatable=false)
	protected long id;
	
	@Column(name="TEXT")
	protected String text;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	ProjectBranch projectBranch;

	@Column(name="DOCUMENT_ID")
	protected long documentId;
	
	@Transient
	Document document;
	
	@Column(name="OBJ_ORDER")
	protected int order;

	@Transient
	protected List<Obj> children;

	@Column(name="PARENT_OBJ_ID")
	protected long parentObjId;

	@Transient
	protected Obj parent;
	
	@Transient
	protected List<Link> linksTo;
	
	@Transient
	protected List<Link> linksFrom;
	
	protected Obj() {}
	
	Obj(ProjectBranch projectBranch, Document d ) {
		this.projectBranchId = projectBranch.id;
		this.projectBranch = projectBranch;
		
		Query q = DataSource.getInstance()
				.entityManager.createQuery ("SELECT MAX(o.id) FROM Obj o");
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

		text = "";
		documentId = d.id;
		order = -1;
		parentObjId = -1;
		
		children = new LinkedList<Obj>();
		
		linksTo = new LinkedList<Link>();
		linksFrom = new LinkedList<Link>();
	}
	
	public String toString()
	{
		return "Obj[projectBranchId="+this.projectBranchId+",id="+this.id+",document="+this.documentId+",parent="+this.parentObjId+",order="+this.order+",text="+this.text+"]";
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText( String text )
	{
		this.text = text;
	}
	
	protected int getOrder() 
	{
		return order;
	}
	
	protected void setOrder( int order )
	{
		this.order = order;
	}
	
	public ProjectBranch getProjectBranch()
	{
		return projectBranch;
	}
	
	protected void setProjectBranch( ProjectBranch projectBranch )
	{
		this.projectBranch = projectBranch;
	}
	
	public Document getDocument() {
		if ( document == null )
		{
			Collection<Document> ds = DataSource.getInstance()
					.entityManager.createQuery("FROM Document d WHERE d.id = "+this.documentId+" AND d.projectBranchId = "+this.projectBranchId, Document.class)
					.getResultList();
			if ( !ds.isEmpty() ) document = ds.iterator().next(); 
		}
		return document;
	}
	
	protected void setDocument( Document document )
	{
		this.document = document;
	}

	
	public Obj getParent() {
		if ( parent == null )
		{
			Collection<Obj> os = DataSource.getInstance()
					.entityManager.createQuery("FROM Obj o WHERE o.id = "+this.parentObjId+" AND o.projectBranchId = "+this.projectBranchId, Obj.class)
					.getResultList();
			if ( !os.isEmpty() ) parent = os.iterator().next(); 
		}
		return parent;
	}
	
	protected void setParent( Obj parent )
	{
		this.parent = parent;
	}

	public List<Obj> getChildren() {
		if ( children == null )
		{
			children = DataSource.getInstance()
					.entityManager.createQuery("FROM Obj o WHERE DOCUMENT_ID = "+this.documentId+" AND PROJECT_BRANCH_ID = "+this.projectBranchId+" AND PARENT_OBJ_ID = "+this.id+" ORDER BY OBJ_ORDER", Obj.class)
					.getResultList();
		}
		return children;
	}

	public Obj next() {
		if ( this.getParent() != null )
		{
			parent.getChildren();
			if ( this.order+1 == parent.children.size() ) return null;
			return parent.children.get(this.order+1);
		}
		else
		{
			document.getObjs();
			if ( this.order+1 == document.objs.size() ) return null;
			return document.objs.get(this.order+1);			
		}
	}

	public Obj previous() {
		if ( this.order==0 ) return null;
		if ( this.getParent() != null )
		{
			parent.getChildren();
			return parent.children.get(this.order-1);
		}
		else
		{
			document.getObjs();
			return document.objs.get(this.order-1);			
		}
	}

	
	public Obj createBefore() {
		return createSiblingAt(this.order);
	}

	
	public Obj createAfter() {
		return createSiblingAt(this.order + 1);
	}

	
	public Obj appendChild() {
		Obj o = new Obj(this.getProjectBranch(),this.getDocument());
		o.order = getChildren().size();
		o.parentObjId = this.id;
		DataSource.getInstance().persist(o);
		children.add(o);
		fireCreatedEvent(o);
		return o;
	}
	
	private Obj createSiblingAt( int index )
	{
		Obj o = new Obj(this.getProjectBranch(),this.getDocument());
		o.order = index;
		o.parentObjId = this.id;
		List<Obj> os;
		if ( this.getParent()!=null )
		{
			parent.getChildren();
			os = parent.children;
		}
		else
		{
			this.getDocument().getObjs();
			os = document.objs;
		}
		os.add(index,o);
		DataSource.getInstance().persist(o);
		for ( int i=index+1 ; i<os.size() ; i++ )
		{
			Obj osi = os.get(i);
			osi.setOrder( osi.getOrder()+1);
		}
		fireCreatedEvent(o);
		return o;
	}
	
	public List<Link> getLinksTo()
	{
		if ( linksTo == null )
		{
			linksTo = DataSource.getInstance()
					.entityManager.createQuery("FROM Link l WHERE DEST_OBJ_ID = "+this.id+" AND PROJECT_BRANCH_ID = "+this.projectBranchId, Link.class)
					.getResultList();
		}
		return linksTo;		
	}
	
	public List<Link> getLinksFrom()
	{
		if ( linksFrom == null )
		{
			linksFrom = DataSource.getInstance()
					.entityManager.createQuery("FROM Link l WHERE SOURCE_OBJ_ID = "+this.id+" AND PROJECT_BRANCH_ID = "+this.projectBranchId, Link.class)
					.getResultList();
		}
		return linksFrom;			
	}

	public Link linkTo( Obj destination )
	{
		Link link = new Link(this.projectBranch,this,destination);
		DataSource.getInstance().persist(link);
		this.linksFrom.add(link);
		fireCreatedEvent(link);
		return link;
	}

	public Link linkFrom( Obj source )
	{
		Link link = new Link(this.projectBranch,source,this);
		DataSource.getInstance().persist(link);
		this.linksTo.add(link);
		fireCreatedEvent(link);
		return link;
	}

	@Override
	long getId() {
		return this.id;
	}

}
