package crud.crud_core.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.AssertFalse;

import org.junit.Before;
import org.junit.Test;

import crud.crud_core.actions.Action;
import crud.crud_core.actions.Create;
import crud.crud_core.actions.Delete;
import crud.crud_core.actions.ReadAll;
import crud.crud_core.actions.ReadOne;
import crud.crud_core.actions.UpdateOne;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.factories.Factory;
import crud.crud_core.issues.DuplicatedKeyIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.issues.NotExistsEntityIssue;
import crud.crud_core.repositories.Repository;
import crud.crud_core.repositories.RepositoryListInMemory;

public class MoveOutReasonIntegrationListInMemoryTest {

	private static Factory factory;
	private static Repository repository;

	@Before
	public void setUp() {
		factory = new Factory();
		factory.setType(MoveOutReason.class);
		repository = new RepositoryListInMemory();
		Action insertAction = new Create(factory, repository);
		fixtureMoveOutReasosns(insertAction);
	}

	@Test
	public void allStoredEntitiesCanBeRetrieved() {

		ReadAll getAllAction = new ReadAll(factory, repository);
		getAllAction.execute();
		List<MoveOutReason> storedEntities = (List<MoveOutReason>) getAllAction
				.getResult();

		assertEquals(3, storedEntities.size());
	}

	@Test
	public void storedEntityCanBeRetrieved() {

		Action insertAction = new Create(factory, repository);

		Entity entity1 = createMoveOutReasonData("127", "name1", false);
		insertAction.setParams(entity1);
		insertAction.execute();
		Entity entity = (Entity) insertAction.getResult();
		UUID id = entity.getId();

		ReadOne getOneAction = new ReadOne(factory, repository);

		getOneAction.setParams(id);
		getOneAction.execute();
		MoveOutReason storedEntity = (MoveOutReason) getOneAction.getResult();
		assertEquals(id, storedEntity.getId());
		assertEquals("127", storedEntity.getCode());
		assertEquals("name1", storedEntity.getName());
		assertFalse(storedEntity.isActive());
		assertFalse(storedEntity.isDistributed());
	}

	@Test
	public void entitiesCanBeUpdated() {

		List<MoveOutReason> reasons = repository.readAll(MoveOutReason.class);
		MoveOutReason reason = reasons.get(0);

		UpdateOne updateOneAction = new UpdateOne(factory, repository);
		MoveOutReason values = new MoveOutReason();
		values.setId(reason.getId());
		values.setName("name2");
		values.setCode(reason.getCode());
		values.setActive(reason.isActive());
		updateOneAction.setParams(values);
		updateOneAction.execute();

		ReadOne getOneAction = new ReadOne(factory, repository);

		getOneAction.setParams(reason.getId());
		getOneAction.execute();

		MoveOutReason storedEntity = (MoveOutReason) getOneAction.getResult();
		assertEquals(reason.getId(), storedEntity.getId());
		assertEquals(reason.getCode(), storedEntity.getCode());
		assertEquals("name2", storedEntity.getName());
		assertFalse(storedEntity.isActive());
		assertFalse(storedEntity.isDistributed());
	}

	@Test
	public void invalidOnUpdateMoveOutReturnsIssues() {
		UpdateOne updateAction = new UpdateOne(factory, repository);
		MoveOutReason params = new MoveOutReason();
		params.setName("invalidOnUpdateMoveOutReturnsIssues");
		params.setCode("prueba test update");
		updateAction.setParams(params);
		updateAction.execute();

		List<Issue> issues = updateAction.getIssues();
		// no existe la entidada a updatear en la bbdd

		for (Issue issue : issues) {
			if (issue instanceof NotExistsEntityIssue) {
				NotExistsEntityIssue notExists = (NotExistsEntityIssue) issue;
				assertEquals("El registro {0} no encontrado",
						notExists.getMsg());
			}
		}
	}

	@Test
	public void invalidThreeTimesOnStoreMoveOutReasonReturnOnlyOneIssue() {
		Create<Entity> insertAction = new Create(factory, repository);

		Entity entity = createMoveOutReasonData("CODE-ENTITY", "NAME-ENTITY", false);
		insertAction.setParams(entity);
		// one
		insertAction.execute();
		List<Issue> issues = insertAction.getIssues();
		// two
		entity.setId(null);
		insertAction.setParams(entity);
		insertAction.execute();
		issues = insertAction.getIssues();
		// three
		entity.setId(null);
		insertAction.setParams(entity);
		insertAction.execute();
		issues = insertAction.getIssues();

		int numberIssusDuplicatedKeyIsOne = 0;
		for (Issue issue : issues) {

			if (issue instanceof DuplicatedKeyIssue) {
				numberIssusDuplicatedKeyIsOne++;
				DuplicatedKeyIssue duplicateKey = (DuplicatedKeyIssue) issue;
				assertEquals("El valor indicado en el campo {0} se encuentra duplicado.", duplicateKey.getMsg());
				assertTrue(duplicateKey.getVars().contains("code"));
			}
		}
		assertEquals(2, numberIssusDuplicatedKeyIsOne);
	}

	@Test
	public void deleteNotExistEntityReturnIssues() {
		Delete action = new Delete(factory, repository);

		action.setParams(UUID.randomUUID());
		action.setAfectedClass(new MoveOutReason());
		action.execute();
		List<Issue> issues = action.getIssues();

		assertTrue(issues.size() > 0);
	}

	@Test(expected = PersistenceException.class)
	public void readNotExistEntityTrowsException() {
		repository.read(UUID.randomUUID(), MoveOutReason.class);
	}

	private static void fixtureMoveOutReasosns(Action insertAction) {

		Entity entity1 = createMoveOutReasonData("123", "name1", false);
		insertAction.setParams(entity1);
		insertAction.execute();

		Entity entity2 = createMoveOutReasonData("124", "name1", false);
		insertAction.setParams(entity2);
		insertAction.execute();

		Entity entity3 = createMoveOutReasonData("125", "name1", false);
		insertAction.setParams(entity3);
		insertAction.execute();
	}

	private static Entity createMoveOutReasonData(String code, String name,
			boolean active) {
		MoveOutReason entity = new MoveOutReason();
		entity.setCode(code);
		entity.setName(name);
		entity.setActive(active);
		return entity;
	}
}
