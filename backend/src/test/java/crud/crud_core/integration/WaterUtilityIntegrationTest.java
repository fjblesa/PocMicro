package crud.crud_core.integration;

import org.junit.Before;
import org.junit.Test;









import crud.crud_core.actions.*;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.EntityTestUtils;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;
import crud.crud_core.repositories.RepositoryListInMemory;

import java.util.List;
import java.util.UUID;

import static crud.crud_core.entities.EntityTestUtils.createWaterUtilityDummy;
import static org.junit.Assert.*;

public class WaterUtilityIntegrationTest {

	Factory<WaterUtility> factory;
	Repository<WaterUtility> repository;

	@Before
	public void setUp() {
		factory = new Factory<WaterUtility>();
		factory.setType(WaterUtility.class);
		repository = new RepositoryListInMemory<WaterUtility>();
	}

	@Test
	public void createEntreprise() {
		Action action = new Create<>(factory, repository);

		WaterUtility entityEntreprise = createWaterUtilityDummy();

		action.setParams(entityEntreprise);
		action.execute();
		Entity insertedEntity = (Entity) action.getResult();

		assertNotNull(insertedEntity.getId() != null);
	}

	@Test
	public void readOneEntreprise() {
		Action action = new ReadOne<>(factory,repository);
		WaterUtility entrepriseDummy = createWaterUtilityDummy();
		UUID id = repository.store(entrepriseDummy);

		action.setParams(id);
		action.execute();

		WaterUtility retrievedEntity = (WaterUtility) action.getResult();

		assertNotNull(retrievedEntity);
		assertEquals(id, retrievedEntity.getId());

		entrepriseDummy.setId(id);
		assertNotEquals(entrepriseDummy, retrievedEntity);
		assertEqualEntreprises(entrepriseDummy, retrievedEntity);
	}

	@Test
	public void readAllEntreprise() {
		Action action = new ReadAll<>(factory,repository);
		WaterUtility EntrepriseDummy = createWaterUtilityDummy();
		repository.store(EntrepriseDummy);
		action.execute();

		List<WaterUtility> retrievedEntities = (List<WaterUtility>) action.getResult();

		assertNotNull(retrievedEntities);
		assertTrue(retrievedEntities.size() > 0);
	}

	@Test
	public void modifyEntity() {
		Action actionToInsert = new Create<>(factory, repository);
		WaterUtility EntrepriseToInsert = createWaterUtilityDummy();

		actionToInsert.setParams(EntrepriseToInsert);
		actionToInsert.execute();

		Entity retrievedEntity = (Entity) actionToInsert.getResult();

		WaterUtility entrepriseToUpdate = createWaterUtilityDummy();
		entrepriseToUpdate.setId(retrievedEntity.getId());

		String newName = "Empresa de Aguas, SA";
		entrepriseToUpdate.setName(newName);

		Action actionToUpdate = new UpdateOne<>(factory, repository);
		actionToUpdate.setParams(entrepriseToUpdate);
		actionToUpdate.execute();

		WaterUtility modifyEntreprise = repository.read(entrepriseToUpdate.getId(),entrepriseToUpdate.getClass());
		assertEquals(modifyEntreprise.getName(), newName);
	}

	@Test
	public void deleteEntity() {
		Action actionDelete = new Delete(factory,repository);
		WaterUtility EntrepriseEntityToDelete = createWaterUtilityDummy();

		UUID entityId = repository.store(EntrepriseEntityToDelete);

		actionDelete.setParams(entityId);
		actionDelete.setAfectedClass(new WaterUtility());
		actionDelete.execute();

		Action actionRead = new ReadOne<>(factory,repository);
		actionRead.setParams(entityId);
		try {
			actionRead.execute();
		} catch (PersistenceException pe) {
			assertTrue(pe.getIssues().size() > 0);
		}
	}


	private void assertEqualEntreprises(WaterUtility expected, WaterUtility actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getCode(), actual.getCode());
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
		assertEquals(expected.getName(), actual.getName());
	}
}
