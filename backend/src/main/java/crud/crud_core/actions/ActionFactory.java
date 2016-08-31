package crud.crud_core.actions;

import crud.crud_core.entities.Entity;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;
import crud.crud_core.repositories.RepositorySQLite;

public class ActionFactory {

	private static Repository<Entity> repository = new RepositorySQLite<>();

	public Create<Entity> createCreateAction(Class entityClass) {
		Factory<Entity> factory = getEntityFactory(entityClass);

		Create<Entity> create = new Create<>(factory, repository);
		return create;
	}

	public UpdateOne<Entity> createUpdateAction(Class entityClass) {
		Factory<Entity> factory = getEntityFactory(entityClass);

		UpdateOne<Entity> action = new UpdateOne<>(factory, repository);
		return action;
	}

	public Delete<Entity> createDeleteAction(Class entityClass) {
		Factory<Entity> factory = getEntityFactory(entityClass);
		
		Delete<Entity> deleteEntity = new Delete<Entity>(factory, repository);
		try {
			Entity entity = (Entity) entityClass.newInstance();
			deleteEntity.setAfectedClass(entity);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deleteEntity;
	}

	public ReadOne<Entity> createReadOneAction(Class entityClass) {
		Factory<Entity> factory = getEntityFactory(entityClass);
		
		ReadOne<Entity> action = new ReadOne(factory,repository);
		return action;
	}

	public ReadAll<Entity> createReadAllAction(Class entityClass) {
		Factory<Entity> factory = getEntityFactory(entityClass);
		ReadAll<Entity> action = new ReadAll<>(factory,repository);
		return action;
	}
	
	public ReadList<Entity> createReadListAction(Class entityClass) {
		Factory<Entity> factory = getEntityFactory(entityClass);
		ReadList<Entity> action = new ReadList<>(factory,repository);
		return action;
	}
	
	private Factory<Entity> getEntityFactory(Class entity) {
		Factory<Entity> factory = new Factory<Entity>();
		factory.setType(entity);
		return factory;
	}
}
