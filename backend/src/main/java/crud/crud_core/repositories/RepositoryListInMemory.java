package crud.crud_core.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.issues.DuplicatedKeyIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.issues.NotExistsEntityIssue;

public class RepositoryListInMemory<T extends Entity> implements Repository<T> {
	List<Issue> issues;
	List<T> entities = new ArrayList<T>();

	@Override
	public UUID store(T entity) {
		issues = new ArrayList<>();
		if (entity.getId() != null) {
			updateEntity(entity);
		} else {
			insertEntity(entity);
		}
		return entity.getId();
	}

	@Override
	public T read(UUID id, Class entity) {
		return (T) readWithoutClone(id).clone();
	}

	@Override
	public List<T> readAll(Class getClass) {
		List<T> entityCloned = new ArrayList<T>();
		for (T entity : entities) {
			if (getClass.isInstance(entity)) {
				entityCloned.add((T) entity.clone());
			}
		}
		return entityCloned;
	}

	@Override
	public void delete(UUID id, Class entity) {
		Entity reasonDelete = readWithoutClone(id);
		entities.remove(reasonDelete);
	}

	private void insertEntity(T entity) {
		verifyRepositoryIntegrity(entity);
		entity.setId(UUID.randomUUID());
		entities.add((T) entity.clone());
	}

	private void verifyRepositoryIntegrity(T entity) {

		if (entity instanceof MoveOutReason) {
			MoveOutReason reason = (MoveOutReason) entity;
			verifyDuplicateCode(reason);
		}
	}

	private void verifyDuplicateCode(MoveOutReason reason) {
		for (Entity entity : entities) {
			if (entity instanceof MoveOutReason) {
				MoveOutReason moveOutReason = (MoveOutReason) entity;
				if (moveOutReason.getCode().equals(reason.getCode())
						&& moveOutReason.getId() != reason.getId()) {
					DuplicatedKeyIssue issue = new DuplicatedKeyIssue("code");
					issues.add(issue);
					throw new PersistenceException(issues);
				}
			}
		}
	}

	private void updateEntity(T reason) {
		T reasonToUpdate = readWithoutClone(reason.getId());
		if (reasonToUpdate.getId() == null) {
			NotExistsEntityIssue issue = new NotExistsEntityIssue(reason
					.getId().toString());
			issues.add(issue);
			throw new PersistenceException(issues);
		}
		verifyRepositoryIntegrity(reason);

		reasonToUpdate.copy(reason);
	}

	private T readWithoutClone(UUID id) {
		Optional<T> entity = (Optional<T>) entities.stream()
				.filter(item -> item.getId().equals(id)).findFirst();
		if (entity.isPresent()) {
			return entity.get();
		}
		List<Issue> issues = new ArrayList<>();
		NotExistsEntityIssue issue = new NotExistsEntityIssue("Id " + id);
		issues.add(issue);
		throw new PersistenceException(issues);
	}

	@Override
	public List readListWaterUtilityFilterActive() {
		return null;
	}
}
