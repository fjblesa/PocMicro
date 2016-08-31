package crud.crud_core.actions;

import java.util.List;

import crud.crud_core.entities.Entity;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;

public class ReadList<T extends Entity> extends Action {

	private List<T> allEntities;

	public ReadList(Factory factory, Repository repository) {
		super(factory, repository);
	}

	@Override
	public void execute() {
		this.allEntities = repository.readListWaterUtilityFilterActive();
	}

	@Override
	public Object getResult() {
		return this.allEntities;
	}

	@Override
	public void setParams(Object params) {
		throw new IllegalArgumentException("Can not configure Get All action");
	}

	@Override
	public void setAfectedClass(Entity entity) {
	}

}
