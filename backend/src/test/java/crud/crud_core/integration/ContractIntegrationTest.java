package crud.crud_core.integration;

import static crud.crud_core.entities.EntityTestUtils.assertEqualContracts;
import static crud.crud_core.entities.EntityTestUtils.createContractDummy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import crud.crud_core.actions.Action;
import crud.crud_core.actions.Create;
import crud.crud_core.actions.Delete;
import crud.crud_core.actions.ReadAll;
import crud.crud_core.actions.ReadOne;
import crud.crud_core.actions.UpdateOne;
import crud.crud_core.entities.Contract;
import crud.crud_core.entities.Entity;
import crud.crud_core.enums.Status;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.exceptions.StatusException;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;
import crud.crud_core.repositories.RepositoryListInMemory;

public class ContractIntegrationTest {

	Factory<Contract> factory;
	Repository<Contract> repository;

	@Before
	public void setUp() {
		factory = new Factory<Contract>();
		factory.setType(Contract.class);
		repository = new RepositoryListInMemory<Contract>();
	}

	@Test
	public void createContract() {
		Action action = new Create<>(factory, repository);

		Contract entityContract = createContractDummy();

		action.setParams(entityContract);
		action.execute();
		Entity insertedEntity = (Entity) action.getResult();

		assertNotNull(insertedEntity.getId());
	}

	@Test
	public void readOneContract() {
		Action action = new ReadOne<>(factory, repository);
		Contract contractDummy = createContractDummy();
		UUID id = repository.store(contractDummy);

		action.setParams(id);
		action.execute();

		Contract retrievedEntity = (Contract) action.getResult();

		assertNotNull(retrievedEntity);
		assertEquals(id, retrievedEntity.getId());

		contractDummy.setId(id);
		assertNotEquals(contractDummy, retrievedEntity);
		assertEqualContracts(contractDummy, retrievedEntity);
	}

	// TODO activar cuando se implemente la loogica de contratos
	@Ignore
	@Test(expected = StatusException.class)
	public void insertWithCanceledStateReturnIssues() {
		Contract contract = createContractDummy();
		contract.setStatus(Status.CANCELLED);
		Action action = new Create<>(factory, repository);

		action.setParams(contract);
		action.execute();
	}

	// TODO activar cuando se implemente la loogica de contratos
	@Test(expected = StatusException.class)
	@Ignore
	public void insertWithFinishedStateReturnIssues() {
		Contract contract = createContractDummy();
		contract.setStatus(Status.FINISHED);
		Action action = new Create<>(factory, repository);

		action.setParams(contract);
		action.execute();
	}

	@Test
	public void realAllContract() {
		Action action = new ReadAll<>(factory, repository);
		Contract contractDummy = createContractDummy();
		repository.store(contractDummy);
		action.execute();

		List<Contract> retrievedEntities = (List<Contract>) action.getResult();

		assertNotNull(retrievedEntities);
		assertTrue(retrievedEntities.size() > 0);
	}

	@Test
	public void modifyEntity() {
		Action actionToInsert = new Create<>(factory, repository);
		Contract contractToInsert = createContractDummy();

		actionToInsert.setParams(contractToInsert);
		actionToInsert.execute();

		Entity retrievedEntity = (Entity) actionToInsert.getResult();

		Contract contractToUpdate = createContractDummy();
		contractToUpdate.setId(retrievedEntity.getId());

		String externalReference = "REF-UPD";
		contractToUpdate.setExternalReference(externalReference);

		Action actionToUpdate = new UpdateOne<>(factory, repository);
		actionToUpdate.setParams(contractToUpdate);
		actionToUpdate.execute();

		Contract modifyContract = repository.read(contractToUpdate.getId(),
				contractToUpdate.getClass());
		assertEquals(modifyContract.getExternalReference(), externalReference);
	}

	@Test
	public void modifyEntityInStatusPendingToStatusFinishedRetrievedIssues() {
		Action actionToInsert = new Create<>(factory, repository);
		Contract contractToInsert = createContractDummy();

		actionToInsert.setParams(contractToInsert);
		actionToInsert.execute();

		Entity retrievedEntity = (Entity) actionToInsert.getResult();

		Contract contractToUpdate = createContractDummy();
		contractToUpdate.setStatus(Status.FINISHED);
		contractToUpdate.setId(retrievedEntity.getId());

		Action actionToUpdate = new UpdateOne<>(factory, repository);
		actionToUpdate.setParams(contractToUpdate);
		try {
			actionToUpdate.execute();
		} catch (PersistenceException pe) {
			assertTrue(pe.getIssues().size() > 0);
		}
	}

	@Test
	public void deleteEntity() {
		Action actionDelete = new Delete<>(factory, repository);
		Contract contractEntityToDelete = createContractDummy();

		UUID entityId = repository.store(contractEntityToDelete);

		actionDelete.setParams(entityId);
		actionDelete.setAfectedClass(contractEntityToDelete);
		actionDelete.execute();

		Action actionRead = new ReadOne<>(factory, repository);
		actionRead.setParams(entityId);
		try {
			actionRead.execute();
			actionRead.getResult();
		} catch (PersistenceException pe) {
			assertTrue(pe.getIssues().size() > 0);
		}
	}
}
