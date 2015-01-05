package org.oors;

public enum AttributeFor {
	DOCUMENT(Document.class),FOLDER(Folder.class),LINK(Link.class),OBJ(Obj.class),PROJECT_BRANCH(ProjectBranch.class);
	
	private int hash;
	
	public int getHash() { return hash; }

	private AttributeFor( Class<?> clazz )
	{
		hash = clazz.hashCode();
	}
}
