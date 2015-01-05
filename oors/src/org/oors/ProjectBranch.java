package org.oors;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

@Entity
@Table(name="PROJECT_BRANCHES")
public class ProjectBranch extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PROJECT_BRANCH_ID")
	protected long id;
	
	@Column(name="PROJECT_BRANCH_NAME")
	protected String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID")
	protected Project project;
	
	@Transient
	protected List<Folder> folders;
	
	@Transient
	protected List<Document> documents;

	public String toString()
	{
		return "ProjectBranch[id="+id+",name="+name+"]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected ProjectBranch()
	{
	}
	
	ProjectBranch( Project project, String name )
	{
		this.project = project;
		this.name = name;
		folders = new LinkedList<Folder>();
		documents = new LinkedList<Document>();
	}
	
	@Override
	public long getId()
	{
		return id;
	}
	
	protected void setId( long id )
	{
		this.id = id;
	}
	
	public Project getProject() {
		return project;
	}
	
	protected void setProject( Project project )
	{
		this.project = project;
	}
	
	public List<Folder> getFolders()
	{
		if ( folders == null )
		{
			folders = DataSource.getInstance()
					.entityManager.createQuery("FROM Folder f WHERE PARENT_FOLDER_ID = -1 AND PROJECT_BRANCH_ID = "+this.id, Folder.class)
					.getResultList();
		}
		return folders;
	}
	
	public List<Document> getDocuments() {
		if ( documents == null )
		{
			documents = DataSource.getInstance()
					.entityManager.createQuery("FROM Document d WHERE FOLDER_ID = -1 AND PROJECT_BRANCH_ID = "+this.id, Document.class)
					.getResultList();
		}
		return documents;
	}
	
	public Folder createFolder( String name )
	{
		Folder folder = new Folder(this,null,name);
		DataSource.getInstance().persist(folder);
		getFolders().add(folder);
		fireCreatedEvent(folder);
		return folder;
	}

	public Document createDocument( String name )
	{
		Document document = new Document(this,null,name);
		DataSource.getInstance().persist(document);
		getDocuments().add(document);
		fireCreatedEvent(document);
		return document;
	}

	public Collection<Attribute> getAttributes()
	{
		TypedQuery<Attribute> q = DataSource.getInstance()
				.entityManager.createQuery("FROM Attribute a WHERE a.projectBranchId = "+this.id, Attribute.class);
		Collection<Attribute> attribs = q.getResultList();
		return attribs;
	}

	public List<Attribute> getAttributesFor( AttributeFor attributesFor )
	{
		TypedQuery<Attribute> q = DataSource.getInstance()
				.entityManager.createQuery("FROM Attribute a WHERE a.forType = :forType AND a.projectBranchId = "+this.id, Attribute.class);
		q.setParameter("forType", attributesFor);
		return q.getResultList();
	}
	
	public Attribute createAttribute( AttributeFor attributeFor )
	{
		Attribute a = new Attribute(this,"New Attribute",attributeFor,AttributeType.STRING);
		DataSource.getInstance().persist(a);
		fireCreatedEvent(a);
		return a;
	}

	@Override
	ProjectBranch getProjectBranch() {
		return this;
	}


}
