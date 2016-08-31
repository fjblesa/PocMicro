package crud.crud_core.actions;

import java.util.ArrayList;
import java.util.List;

import crud.crud_core.entities.Entity;
import crud.crud_core.factories.Factory;
import crud.crud_core.issues.Issue;
import crud.crud_core.repositories.Repository;

public abstract class Action {
	
	protected Factory factory;
	protected Repository repository;
	protected List<Issue> issues;

	public Action(Factory factory, Repository repository) {
		this.factory = factory;
		this.repository = repository;
		this.issues = new ArrayList<Issue>();
	}

	public Action(Repository repository) {
		this.repository = repository;
		this.issues = new ArrayList<Issue>();
	}

	public abstract void execute() ;
	
	public abstract Object getResult();
	
	public abstract void setParams(Object params);

	public List<Issue> getIssues() {
		return this.issues;
	}

	public abstract void setAfectedClass(Entity entity);
}