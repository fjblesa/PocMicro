package crud.crud_core.actions;

import crud.crud_core.entities.Entity;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;

public class Create<T extends Entity> extends Action {
	
	private T entity;

	public Create(Factory factory, Repository repository) {
		super(factory,repository);
	}

	@Override
	public void execute() {
		try{
			this.entity.setId(repository.store((Entity)entity));			
		}catch(PersistenceException e){
			entity = (T) factory.createNullEntity();
			this.issues.addAll(e.getIssues());
		}
	}

	@Override
	public Object getResult() {
		return this.entity;
	}

	@Override
	public void setParams(Object params) {
		this.entity = (T) params;
	}

	@Override
	public void setAfectedClass(Entity entity) {
	}
}
