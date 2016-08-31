package crud.crud_core.repositories;

import java.util.List;
import java.util.UUID;

import crud.crud_core.entities.Entity;

public interface Repository<T extends Entity> {
	
	public UUID store(T entity);

	public T read(UUID id, Class entity);

	public List<T> readAll(Class entity);

	public void delete(UUID id, Class entity);
	
	public List readListWaterUtilityFilterActive();
}
