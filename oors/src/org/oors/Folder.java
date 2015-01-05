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
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@IdClass(ProjectBranchElementId.class)
@Table(name = "FOLDERS")
public class Folder extends Base {

	@Id
	@Column(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	protected long projectBranchId;

	@Id
	@Column(name = "FOLDER_ID",insertable=false,updatable=false)
	protected long id;
	
	@Column(name="FOLDER_NAME")
	protected String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_BRANCH_ID",insertable=false,updatable=false)
	ProjectBranch projectBranch;
		
	@Column(name="PARENT_FOLDER_ID")
	protected long parentFolderId;

	@Transient
	protected Folder parent;

	@Transient
	protected List<Folder> folders;

	@Transient
	protected List<Document> documents;

	public List<Folder> getFolders() {
		if ( folders == null )
		{
			folders = DataSource.getInstance()
					.entityManager.createQuery("FROM Folder f WHERE PARENT_FOLDER_ID = "+this.id+" AND PROJECT_BRANCH_ID = "+this.projectBranchId, Folder.class)
					.getResultList();
		}
		return folders;
	}

	public List<Document> getDocuments() {
		if ( documents == null )
		{
			documents = DataSource.getInstance()
					.entityManager.createQuery("FROM Document d WHERE FOLDER_ID = "+this.id+" AND PROJECT_BRANCH_ID = "+this.projectBranchId, Document.class)
					.getResultList();
		}
		return documents;
	}

	public Folder getParent() {
		if ( parent == null )
		{
			Collection<Folder> fs = DataSource
					.getInstance()
					.entityManager
					.createQuery("FROM Folder f WHERE f.id = "+this.parentFolderId+" AND f.projectBranchID = "+this.projectBranchId, Folder.class)
					.getResultList();
			if ( fs!=null ) parent = fs.iterator().next(); 
		}
		return parent;
	}

	public void setParent(Folder parent) {
		this.parent = parent;
		if ( parent==null ) parentFolderId=-1;
		else parentFolderId = parent.id;
	}

	protected Folder() {

	}

	Folder(ProjectBranch projectBranch, Folder parent, String name) {
		this.projectBranchId = projectBranch.id;
		this.projectBranch = projectBranch;
		
		Query q = DataSource.getInstance()
				.entityManager.createQuery ("SELECT MAX(f.id) FROM Folder f");
		Number result = (Number) q.getSingleResult ();

		if ( result==null ) this.id = 1;
		else this.id = result.longValue() + 1;
		
		this.parent = parent;
		if ( parent==null) this.parentFolderId=-1;
		else this.parentFolderId = parent.id;
		this.name = name;
		this.folders = new LinkedList<Folder>();
		this.documents = new LinkedList<Document>();
	}

	protected long getParentFolderId() {
		return parentFolderId;
	}

	protected void setParentFolderId(long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public String toString() {
		return "Folder[projectBranchId="+projectBranchId+",id=" + id + ",parentFolderId="+(parent==null?"null":parent.id)+",name="+name+"]";
	}

	public Folder createFolder(String name) {
		Folder folder = new Folder(this.getProjectBranch(), this,name);
		DataSource.getInstance().persist(folder);
		getFolders().add(folder);
		fireCreatedEvent(folder);
		return folder;
	}

	public Document createDocument( String name )
	{
		Document document = new Document(this.getProjectBranch(),this,name);
		DataSource.getInstance().persist(document);
		getDocuments().add(document);
		fireCreatedEvent(document);
		return document;
	}

	public long getProjectBranchId() {
		return projectBranchId;
	}

	protected void setProjectBranchId(long projectBranchId) {
		this.projectBranchId = projectBranchId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProjectBranch getProjectBranch() {
		return projectBranch;
	}

	protected void setProjectBranch(ProjectBranch projectBranch) {
		this.projectBranch = projectBranch;
	}

}