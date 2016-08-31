package crud.crud_core.repositories;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Test;

import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.repositories.Repository;
import crud.crud_core.repositories.RepositorySQLite;

public class RepositorySQLiteTest extends AbstractRepositoryTest {

	@After
	public void setDown() {
		Repository repository = new RepositorySQLite();
		List<MoveOutReason> entityDB = repository.readAll(MoveOutReason.class);

		for (MoveOutReason moveOutReason : entityDB) {
			if (!moveOutReason.isDistributed()) {
				repository.delete(moveOutReason.getId(), MoveOutReason.class);
			}
		}

		List<WaterUtility> entityDBWaterUtilities = repository
				.readAll(WaterUtility.class);

		for (WaterUtility entityDBWaterUtility : entityDBWaterUtilities) {
			repository.delete(entityDBWaterUtility.getId(), WaterUtility.class);
		}

	}

	@Override
	public Repository<MoveOutReason> getRepository() {
		return new RepositorySQLite<MoveOutReason>();
	}

	@Test
	public void insertEntityAndReturnIDInserted() throws Exception {
		MoveOutReason entity = new MoveOutReason();

		entity.setActive(true);
		entity.setName("fran");
		entity.setCode("12");

		Repository<MoveOutReason> repository = new RepositorySQLite<MoveOutReason>();

		UUID entityResultid = repository.store(entity);

		MoveOutReason entityDB = repository.read(entityResultid,
				entity.getClass());
		assertTrue(entityDB.getId().equals(entityResultid));
	}

	@Test
	public void readListWaterUtilityFilterActiveReturnOk() {
		RepositorySQLite<Entity> repository = new RepositorySQLite<Entity>();

		WaterUtility entityWaterUtility = new WaterUtility();
		entityWaterUtility.setActive(true);
		entityWaterUtility.setCode("readListWaterUtilityFilterActiveReturnOk");
		entityWaterUtility.setIdentifier("identifier");
		entityWaterUtility.setName("teeest");
		repository.store(entityWaterUtility);

		List resultDB = repository.readListWaterUtilityFilterActive();

		assertNotNull(resultDB);

	}
}
