package test;

import java.util.Collection;

import javax.swing.text.html.HTMLDocument;

import org.oors.Attribute;
import org.oors.AttributeFor;
import org.oors.AttributeType;
import org.oors.DataSource;
import org.oors.Document;
import org.oors.Folder;
import org.oors.Link;
import org.oors.Obj;
import org.oors.OorsException;
import org.oors.Project;
import org.oors.ProjectBranch;

public class TestDB01 {

	private static DataSource ds;

	public static void main(String[] args) {

		ds = DataSource.getInstance();
		dump();
		Project p;
		ProjectBranch pb;
		Folder f;
		Document d;
		Obj o;
		Obj oo;
		//Link l;
		Attribute a;
		
		try
		{
			p = ds.createProject("Test Project 01");
			pb = p.createProjectBranch("Test Project 01 - Branch 1");
			a = pb.createAttribute(AttributeFor.FOLDER);
			a.setValueType(AttributeType.STRING);
			a.setName("Folder Name");
			a = pb.createAttribute(AttributeFor.DOCUMENT);
			a.setValueType(AttributeType.STRING);
			a.setName("Document Name");
			a = pb.createAttribute(AttributeFor.PROJECT_BRANCH);
			a.setName("my attribute !!!!");
			pb.getStringValue(a);
			pb.setStringValue(a,"my value");
			a = pb.createAttribute(AttributeFor.OBJ);
			a.setForType(AttributeFor.DOCUMENT);
			a.setForType(AttributeFor.PROJECT_BRANCH);
			a.setValueType(AttributeType.HTML);
			System.out.println("===========================");
			System.out.println(pb.getHTMLValue(a));
			pb.setHTMLValue(a, new HTMLDocument());
			System.out.println(pb.getHTMLValue(a));
			System.out.println("===========================");
			
			
			f = pb.createFolder("P1, B1, F1");
			d = f.createDocument("Doc 1");
			f = f.createFolder("sub folder");
			d = pb.createDocument("Doc 2");
			o = d.appendObj();
			o.setText("1111");
			o.createBefore().setText("0000");
			o.createAfter().setText("2222");
			o = o.appendChild();
			o.setText("1111.1111");
			o = d.appendObj();
			o.setText("bbbb");
			oo = o.appendChild();
			oo.setText("dddd");
			oo.setText("##############");
			
			o.linkTo(oo);
			o.linkFrom(oo);
			
			pb = p.createProjectBranch("Test Project 01 - Branch 2");
			p = ds.createProject("Test Project 02");
			pb = p.createProjectBranch("Test Project 01 - Branch 1");
		}
		catch ( OorsException oex )
		{
			oex.printStackTrace();
		}
		dump();
		ds.close();

		ds = DataSource.getInstance();
		dump();
		ds.close();
}
	
	private static void dump()
	{
		System.out.println("--- DUMP -------------------------");
		Collection<Project> projects = ds.getProjects();
		for ( Project p : projects )
		{
			System.out.println(p);
			Collection<ProjectBranch> branches = p.getProjectBranches();
			for ( ProjectBranch pb : branches )
			{
				indent(1);
				System.out.println(pb);

				indent(2);
				System.out.println("( "+pb.getProject()+" )");

				Collection<Attribute> attribs = pb.getAttributes();
				for ( Attribute a : attribs )
				{
					indent(2);
					System.out.println(a);
				}
				
				dumpFolders(pb.getFolders(),2);
				dumpDocuments(pb.getDocuments(),2);
			}	
		}
		System.out.println("--- /DUMP -------------------------");
	}
	
	private static void indent(int indent )
	{
		for ( int i=0 ;i<indent;i++) System.out.print("   ");
	}
	
	private static void dumpFolders(Collection<Folder> folders,int indent)
	{
		for ( Folder f : folders )
		{
			indent(indent);
			System.out.println(f);
			dumpFolders(f.getFolders(),indent+1);
			dumpDocuments(f.getDocuments(),indent+1);
		}

	}
	private static void dumpDocuments(Collection<Document> documents,int indent)
	{
		for ( Document d : documents )
		{
			indent(indent);
			System.out.println(d);
			dumpObjs(d.getObjs(),indent+1);
		}

	}
	private static void dumpObjs(Collection<Obj> objs,int indent)
	{
		for ( Obj o : objs )
		{
			indent(indent);
			System.out.println(o);
			for ( Link l : o.getLinksFrom() )
			{
				indent(indent+1);
				System.out.println(l);
				indent(indent+1);
				System.out.println("-> " + l.getDestination());
			}
			for ( Link l : o.getLinksTo() )
			{
				indent(indent+1);
				System.out.println(l);
				indent(indent+1);
				System.out.println("<- "+l.getSource());
			}
			dumpObjs(o.getChildren(),indent+1);
		}

	}

}
