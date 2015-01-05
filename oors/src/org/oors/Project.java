package org.oors;

import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.*;

@Entity
@Table(
        name="PROJECTS", 
        uniqueConstraints=
            @UniqueConstraint(columnNames={"PROJECT_NAME"}))
public class Project extends OorsEventGenerator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name="PROJECT_ID")
	protected long id;
	
	@Column( name="PROJECT_NAME")
	protected String name;
	
	@OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL,mappedBy="project")
	protected Collection<ProjectBranch> projectBranches;
	
	public String toString()
	{
		return "Project[id="+id+",name="+name+"]";
	}
	
	protected Project()
	{
		
	}
	
	Project( String name )
	{
		this.name = name;
		projectBranches = new LinkedList<ProjectBranch>();
	}
	
	public long getId()
	{
		return id;
	}
	
	protected void setId( long id )
	{
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<ProjectBranch> getProjectBranches() {
		return projectBranches;
	}

	public void setProjectBranches(Collection<ProjectBranch> projectBranches) {
		this.projectBranches = projectBranches;
	}
	
	public ProjectBranch createProjectBranch( String name )
	{
		ProjectBranch projectBranch = new ProjectBranch(this,name);
		DataSource.getInstance().persist(projectBranch);
		getProjectBranches().add(projectBranch);
		fireCreatedEvent(projectBranch);
		return projectBranch;
	}

	public Collection<UserGroup> getUserGroups() {
		// TODO Auto-generated method stub
		return null;
	}

}
