package crud.crud_core.actions;

import crud.crud_core.entities.Entity;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;

public class UpdateOne<T extends Entity> extends Action {

	private T entity;

	public UpdateOne(Factory factory, Repository repository) {
		super(factory, repository);
	}

	@Override
	public void execute() {
		try {
			repository.store(entity);
		} catch (PersistenceException e) {
			this.issues.addAll(e.getIssues());
		}
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public void setParams(Object params) {
		this.entity = (T) this.factory.create();
		this.entity = (T) params;
	}

	@Override
	public void setAfectedClass(Entity entity) {
	}
}
