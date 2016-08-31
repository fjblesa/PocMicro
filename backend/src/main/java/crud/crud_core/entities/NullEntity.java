package crud.crud_core.entities;

import java.util.List;

import crud.crud_core.issues.Issue;

public class NullEntity extends Entity {

	@Override
	public List<Issue> validate() {
		throw new UnsupportedOperationException("Call not suported");
	}

	@Override
	public void copy(Entity entity) {
	}

	@Override
	public Entity clone() {
		return this;
	}
}
