package crud.crud_core.repositories;

import crud.crud_core.repositories.Repository;
import crud.crud_core.repositories.RepositoryListInMemory;

public class RepositoryListInMemoryTest extends AbstractRepositoryTest {

	@Override
	public Repository getRepository() {
		return new RepositoryListInMemory();
	}
}
