package crud.crud_core.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;

import jdk.nashorn.internal.ir.annotations.Ignore;

import org.junit.Test;

import crud.crud_core.entities.Contract;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.issues.DuplicatedKeyIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.issues.NotExistsEntityIssue;
import crud.crud_core.repositories.Repository;

public abstract class AbstractRepositoryTest {
	
	public abstract Repository getRepository();
	
	@Test
	public void duplicatedCodeThrowsException() {
		Repository repository = getRepository();
		MoveOutReason reason1 = new MoveOutReason();
		reason1.setCode("codigo");

		repository.store(reason1);
		List<Issue> issues = null;
		try {
			MoveOutReason reason2 = new MoveOutReason();
			reason2.setCode("codigo");
			repository.store(reason2);
			fail("Should have thrown exception with duplication issues");
		} catch (PersistenceException e) {
			issues = e.getIssues();
		}
		assertEquals(1, issues.size());

		DuplicatedKeyIssue issue = (DuplicatedKeyIssue) issues.get(0);
		assertEquals("code", issue.getVars().get(0));
	}

	@Test
	public void codeIsNotDuplicate() {

		Repository repository = getRepository();
		MoveOutReason reason1 = new MoveOutReason();
		MoveOutReason reason2 = new MoveOutReason();

		reason1.setCode("test");
		reason1.setName("test");
		repository.store(reason1);
		
		reason2.setCode("test2");
		reason2.setName("test2");
		UUID id = repository.store(reason2);

		assertNotNull(id);
	}

	@Test
	public void modifyEntityNotExistsThrowsException() {
		Repository repository = getRepository();
		Entity reason1 = new MoveOutReason();

		try {
			reason1.setId(UUID.randomUUID());
			repository.store(reason1);
			fail("Should have thrown exception with not exists entity issues");
		} catch (PersistenceException e) {
			List<Issue> issues = e.getIssues();
			assertEquals(1, issues.size());
		}
	}

	@Test
	public void storeObjectIsRetrieved() {

		Repository repository = getRepository();

		MoveOutReason reasonToStore = new MoveOutReason();
		reasonToStore.setCode("storeObjectIsRetrieved");
		UUID id = repository.store(reasonToStore);

		Entity reason = (Entity) repository.read(id,reasonToStore.getClass());

		assertTrue(reason.getId().equals(id));
	}

	@Test
	public void modifyExistingObjectChangesItsValues() {
		Repository repository = getRepository();
		MoveOutReason reasonToStore = new MoveOutReason();

		reasonToStore.setCode("testModify");
		UUID id = repository.store(reasonToStore);

		MoveOutReason reasonToUpdate = new MoveOutReason();

		Entity reasonFromRepository = repository.read(id,reasonToUpdate.getClass());
		reasonToUpdate.setCode("testModify2");
		reasonToUpdate.setId(reasonFromRepository.getId());
		repository.store(reasonToUpdate);

		MoveOutReason reasonModified = (MoveOutReason)repository.read(id,reasonToUpdate.getClass());

		assertEquals(reasonFromRepository.getId(), reasonModified.getId());
		assertEquals(reasonToUpdate.getCode(), reasonModified.getCode());
	}

	@Test
	public void givenOneElementItCanRetrieved() {

		Repository repository = getRepository();

		MoveOutReason reasonToStore = new MoveOutReason();
		reasonToStore.setCode("code1");
		UUID id = repository.store(reasonToStore);
		
		reasonToStore.setId(id);
		Entity reason = repository.read(id,reasonToStore.getClass());
		assertEquals(id, reason.getId());
		assertTrue(reason.getId().equals(reasonToStore.getId()));
	}

	@Test
	public void dupplicatedCodeUpdateEntityThrowsException() {
		Repository repository = getRepository();

		MoveOutReason reason1 = new MoveOutReason();
		reason1.setCode("dupplicatedCodeUpdateEntityThrowsException");
		reason1.setName("dupplicatedCodeUpdateEntityThrowsException");
		repository.store(reason1);

		MoveOutReason reason2 = new MoveOutReason();
		reason2.setCode("dupplicatedCodeUpdateEntityThrowsException2");
		reason1.setName("dupplicatedCodeUpdateEntityThrowsException2");
		UUID idReason2 = repository.store(reason2);

		Entity reasonToUpdate = reason1;
		reasonToUpdate.setId(idReason2);

		try {
			repository.store(reasonToUpdate);
			fail("should throw exception");
		} catch (PersistenceException pe) {
			for (Issue issue : pe.getIssues()) {
				assertTrue(issue instanceof DuplicatedKeyIssue);
			}
		}
	}

	@Test(expected = PersistenceException.class)
	public void deleteObjectFromStoreItCanRetrieved() {
		Repository repository = getRepository();
		MoveOutReason reasonToStore = new MoveOutReason();
		reasonToStore.setCode("deleteObjectFromStoreItCanRetrieved");
		UUID id = repository.store(reasonToStore);

		repository.delete(id,reasonToStore.getClass());

		repository.read(id,reasonToStore.getClass());
	}

	@Test
	public void deleteEntityThrowsNotExistsEntityException() {

		Repository repository = getRepository();
		try {
			repository.delete(UUID.randomUUID(),MoveOutReason.class);
			fail("Should throw not exists entity exception");
		} catch (PersistenceException e) {
			e.getIssues().stream().forEach(exception -> assertTrue(exception instanceof NotExistsEntityIssue));
		}
	}
	
	@Test
	public void ModifingRetrievedAllElementNotModifyInRepository(){
		Repository repository = getRepository();
		
		MoveOutReason reason1 = new MoveOutReason();
		reason1.setCode("ModifingRetrievedAllElementNotModifyInRepository");
		repository.store(reason1);
		
		List<MoveOutReason> listForChange = repository.readAll(MoveOutReason.class);
		MoveOutReason entityToModify = listForChange.get(0);
		entityToModify.setCode("ModifingRetrievedAllElementNotModifyInRepository2");
		List<MoveOutReason> list = repository.readAll(MoveOutReason.class);
		Entity entity = list.get(0);
		
		assertFalse(entity.equals(entityToModify));
	}
	
	@Test
	public void ModifingRetrievedOneElementNotModifyInRepository(){
		Repository repository = getRepository();
		
		MoveOutReason reason1 = new MoveOutReason();
		reason1.setCode("ModifingRetrievedOneElementNotModifyInRepository");
		UUID id = repository.store(reason1);
		
		MoveOutReason entityToModify = (MoveOutReason)repository.read(id,reason1.getClass());
		entityToModify.setCode("ModifingRetrievedOneElementNotModifyInRepository2");
		Entity entity = repository.read(id,reason1.getClass());
		
		assertFalse(entity.equals(entityToModify));
	}
	@org.junit.Ignore
	public void insertAnyObjectAndReturnListForObject() {
		Repository repository = getRepository();
		
		MoveOutReason reason = new MoveOutReason();
		Contract contract = new Contract();
		
		repository.store(reason);
		repository.store(contract);
		
		List<MoveOutReason> listReason = repository.readAll(MoveOutReason.class);
		for (MoveOutReason moveOutReason : listReason) {
			assertTrue(moveOutReason instanceof MoveOutReason);
		}
		List<Contract> listContract = repository.readAll(Contract.class);
		for (Contract contract1 : listContract) {
			assertTrue(contract1 instanceof Contract);
		}
		assertTrue(listReason.size()==1);
		assertTrue(listContract.size()==1);
	}
}