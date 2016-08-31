package crud.crud_core.actions;

import java.util.UUID;

import crud.crud_core.entities.Entity;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;

public class ReadOne<T extends Entity> extends Action {
	
	private UUID id;
	private T entity;

	public ReadOne(Factory factory ,Repository<T> repository) {
		super(factory,repository);
	}

	@Override
	public void execute() {
//		TODO al tratarse de un readOne se deberia de controlar que el id venga con identificador no nulo
		entity = (T)repository.read(id,factory.getType());
	}

	@Override
	public Object getResult() {
		return entity;
	}

	@Override
	public void setParams(Object params) {
		id = (UUID) params;
	}

	@Override
	public void setAfectedClass(Entity entity) {
	}
}
