package crud.crud_core.integration;

import static crud.crud_core.entities.EntityTestUtils.assertEqualCommercialSite;
import static crud.crud_core.entities.EntityTestUtils.createCommercialsiteDummy;
import static crud.crud_core.entities.EntityTestUtils.createWaterUtilityDummy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import crud.crud_core.actions.Action;
import crud.crud_core.actions.Create;
import crud.crud_core.actions.Delete;
import crud.crud_core.actions.ReadAll;
import crud.crud_core.actions.ReadOne;
import crud.crud_core.actions.UpdateOne;
import crud.crud_core.entities.CommercialSite;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;
import crud.crud_core.repositories.RepositoryListInMemory;

public class CommercialSitesIntegrationTest {

	Factory<CommercialSite> factory;
	Repository<CommercialSite> repository;

	@Before
	public void setUp() {
		factory = new Factory<CommercialSite>();
		factory.setType(CommercialSite.class);
		repository = new RepositoryListInMemory<CommercialSite>();
	}

	private UUID createWaterUtility() {
		Action action = new Create<>(factory, repository);

		WaterUtility entityEntreprise = createWaterUtilityDummy();

		action.setParams(entityEntreprise);
		action.execute();
		Entity insertedEntity = (Entity) action.getResult();
		return insertedEntity.getId();
	}
	
	@Test
	public void createCommercialSite() {
		
		UUID waterUtilityId = createWaterUtility();
		Action action = new Create<>(factory, repository);
		
		CommercialSite entity = createCommercialsiteDummy();
		entity.setWaterutilityid(waterUtilityId.toString());

		action.setParams(entity);
		action.execute();
		Entity insertedEntity = (Entity) action.getResult();

		assertNotNull(insertedEntity.getId());
	}

	@Test
	public void readOneEntreprise() {
		Action action = new ReadOne<>(factory,repository);
		CommercialSite commercialSiteDummy = createCommercialsiteDummy();

		UUID id = repository.store(commercialSiteDummy);

		action.setParams(id);
		action.execute();

		CommercialSite retrievedEntity = (CommercialSite) action.getResult();

		assertNotNull(retrievedEntity);
		assertEquals(id, retrievedEntity.getId());

		commercialSiteDummy.setId(id);
		assertNotEquals(commercialSiteDummy, retrievedEntity);
		assertEqualCommercialSite(commercialSiteDummy, retrievedEntity);
	}

	@Test
	public void readAllEntreprise() {
		Action action = new ReadAll<>(factory, repository);
		CommercialSite commercialSiteDummy = createCommercialsiteDummy();
		repository.store(commercialSiteDummy);
		action.execute();

		List<CommercialSite> retrievedEntities = (List<CommercialSite>) action
				.getResult();

		assertNotNull(retrievedEntities);
		assertTrue(retrievedEntities.size() > 0);
	}

	@Test
	public void modifyEntity() {
		Action actionToInsert = new Create<>(factory, repository);
		CommercialSite commercialSiteToInsert = createCommercialsiteDummy();

		actionToInsert.setParams(commercialSiteToInsert);
		actionToInsert.execute();

		Entity retrievedEntity = (Entity) actionToInsert.getResult();

		CommercialSite commercialSiteToUpdate = createCommercialsiteDummy();
		commercialSiteToUpdate.setId(retrievedEntity.getId());

		String newName = "Empresa de Aguas, SA";
		commercialSiteToUpdate.setName(newName);

		Action actionToUpdate = new UpdateOne<>(factory, repository);
		actionToUpdate.setParams(commercialSiteToUpdate);
		actionToUpdate.execute();

		CommercialSite modifyEntreprise = repository
				.read(commercialSiteToUpdate.getId(),commercialSiteToUpdate.getClass());
		assertEquals(modifyEntreprise.getName(), newName);
	}

	@Test(expected = PersistenceException.class)
	public void deleteEntity() {
		Action actionDelete = new Delete<>(factory,repository);
		CommercialSite commercialSiteToDelete = createCommercialsiteDummy();

		UUID entityId = repository.store(commercialSiteToDelete);

		actionDelete.setParams(entityId);
		actionDelete.setAfectedClass(commercialSiteToDelete);
		actionDelete.execute();

		Action actionRead = new ReadOne<>(factory,repository);
		actionRead.setParams(entityId);
		actionRead.execute();
	}
}
