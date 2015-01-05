//	Copyright (c) 2011, OORS contributors
//	All rights reserved.
//	
//	Redistribution and use in source and binary forms, with or without
//	modification, are permitted provided that the following conditions are met:
//	    * Redistributions of source code must retain the above copyright
//	      notice, this list of conditions and the following disclaimer.
//	    * Redistributions in binary form must reproduce the above copyright
//	      notice, this list of conditions and the following disclaimer in the
//	      documentation and/or other materials provided with the distribution.
//	    * Neither the name of the OORS Project nor the
//	      names of its contributors may be used to endorse or promote products
//	      derived from this software without specific prior written permission.
//	
//	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
//	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//	DISCLAIMED. IN NO EVENT SHALL CONTRIBUTORS OF THE OORS PROJECT BE LIABLE FOR ANY
//	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
//	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
//	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
//	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
//	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
//	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.oors;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class DataSource {
	private EntityManagerFactory entityManagerFactory;
	 EntityManager entityManager;
	
	private static DataSource instance = null;
	
	public static synchronized DataSource getInstance()
	{
		if ( instance == null )
		{
			instance = new DataSource();
			instance.open();
		}
		return instance;
	}

	private DataSource() {}
	
	private void open() {
		entityManagerFactory = Persistence
				.createEntityManagerFactory("org.hibernate.tutorial.jpa");
		entityManager = entityManagerFactory.createEntityManager();
	}

	public synchronized void close() {
		instance.entityManager.close();
		instance.entityManagerFactory.close();
		instance = null;
	}

	@SuppressWarnings("unchecked")
	public Collection<Project> getProjects() {
		List<? extends Project> result = entityManager.createQuery(
				"from Project", Project.class).getResultList();
		return (Collection<Project>) result;
	}
	
	synchronized void persist( Object o )
	{
		entityManager.getTransaction().begin();
		entityManager.persist(o);
		entityManager.getTransaction().commit();		
	}
	
	public Project createProject( String name )
	{
		Project project = new Project(name);
		persist(project);
		return project;
	}
}
