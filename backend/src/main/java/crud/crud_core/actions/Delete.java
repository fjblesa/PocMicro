package crud.crud_core.actions;

import java.util.UUID;

import crud.crud_core.entities.Entity;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;
import crud.crud_core.utils.validations.validator.IsDistributedValidator;

public class Delete<T extends Entity> extends Action {

	private UUID id;
	private Entity entity;
	private IsDistributedValidator validator;

	public Delete(Factory factory, Repository repository) {
		super(factory, repository);
	}

	@Override
	public void execute() {
		validator.setParams(factory, repository);
		try {
			entity.setId(id);
			issues.addAll(validator.validate(entity));
			if (issues.isEmpty()) {
				repository.delete(this.id, factory.getType());
			}
		} catch (PersistenceException e) {
			issues.addAll(e.getIssues());
		}
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public void setParams(Object params) {
		this.id = (UUID) params;

	}

	@Override
	public void setAfectedClass(Entity entity) {
		validator = new IsDistributedValidator();
		this.entity = entity;
	}
}
