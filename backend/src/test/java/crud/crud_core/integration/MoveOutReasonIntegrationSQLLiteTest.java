package crud.crud_core.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.junit.After;
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
import crud.crud_core.issues.Issue;
import crud.crud_core.issues.NotExistsEntityIssue;
import crud.crud_core.repositories.Repository;
import crud.crud_core.repositories.RepositorySQLite;

public class MoveOutReasonIntegrationSQLLiteTest {

	private static Factory factory;
	private static Repository repository;

	@Before
	public void setUp() {
		factory = new Factory();
		factory.setType(MoveOutReason.class);
		repository = new RepositorySQLite();
		Action insertAction = new Create(factory, repository);
		fixtureMoveOutReasosns(insertAction);
	}
	
	
	@After
	public void tearDawn()
	{
		ReadAll getAllAction = new ReadAll(factory, repository);
		getAllAction.execute();
		List<MoveOutReason> storedEntities = (List<MoveOutReason>) getAllAction
				.getResult();
		
		Delete delete = new Delete(factory, repository);
		for (MoveOutReason moveOutReason : storedEntities) {
			if(!moveOutReason.isDistributed())
			{
				delete.setAfectedClass(moveOutReason);
				delete.setParams(moveOutReason.getId());
				delete.execute();
			}
		}
	}

	@Test
	public void allStoredEntitiesCanBeRetrieved() {

		ReadAll getAllAction = new ReadAll(factory, repository);
		getAllAction.execute();
		List<MoveOutReason> storedEntities = (List<MoveOutReason>) getAllAction
				.getResult();

		
		assertNotNull(storedEntities);
	}

	@Test
	public void storedEntityCanBeRetrieved() {

		Action insertAction = new Create(factory, repository);

		Entity entity1 = createMoveOutReasonData("127", "storedEntityCanBeRetrieved", false);
		insertAction.setParams(entity1);
		insertAction.execute();
		Entity entity = (Entity) insertAction.getResult();
		UUID id = entity.getId();

		ReadOne getOneAction = new ReadOne(factory,repository);

		getOneAction.setParams(id);
		getOneAction.execute();
		MoveOutReason storedEntity = (MoveOutReason) getOneAction.getResult();
		assertEquals(id, storedEntity.getId());
		assertEquals("127", storedEntity.getCode());
		assertEquals("storedEntityCanBeRetrieved", storedEntity.getName());
		assertFalse(storedEntity.isActive());
	}

	@Test
	public void entitiesCanBeUpdated() {
		
		UUID id = repository.store(createMoveOutReasonData("1234", "test", false));
		MoveOutReason reason = (MoveOutReason) repository.read(id, MoveOutReason.class);

		UpdateOne updateOneAction = new UpdateOne(factory, repository);
		MoveOutReason values = new MoveOutReason();
		values.setId(reason.getId());
		values.setName("entitiesCanBeUpdated");
		values.setCode(reason.getCode());
		values.setActive(reason.isActive());
		updateOneAction.setParams(values);
		updateOneAction.execute();

		ReadOne getOneAction = new ReadOne(factory,repository);

		getOneAction.setParams(reason.getId());
		getOneAction.execute();

		MoveOutReason storedEntity = (MoveOutReason) getOneAction.getResult();
		assertEquals(reason.getId(), storedEntity.getId());
		assertEquals(reason.getCode(), storedEntity.getCode());
		assertEquals("entitiesCanBeUpdated", storedEntity.getName());
		assertFalse(storedEntity.isActive());
	}

	@Test
	public void invalidOnUpdateMoveOutReturnsIssues() {
		UpdateOne updateAction = new UpdateOne(factory, repository);
		MoveOutReason params = new MoveOutReason();
		params.setCode("prueba test update");
		params.setName("invalidOnUpdateMoveOutReturnsIssues");
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
	public void deleteNotExistEntityReturnIssues() {
		Delete action = new Delete(factory,repository);
		action.setAfectedClass(new MoveOutReason());
		action.setParams(UUID.randomUUID());
		action.execute();
		List<Issue> issues = action.getIssues();

		assertTrue(issues.size() > 0);
	}

	@Test(expected = PersistenceException.class)
	public void readNotExistEntityTrowsException() {
		repository.read(UUID.randomUUID(),MoveOutReason.class);		
	}
	
	@Test
	public void storeEntityWithSameNameRetrievedIssues()
	{
		Action insertAction = new Create(factory, repository);

		Entity entity1 = createMoveOutReasonData("127", "storeEntityWithSameNameRetrievedIssues", false);
		insertAction.setParams(entity1);
		insertAction.execute();
		
		Entity entity = (Entity) insertAction.getResult();
		entity.setId(null);
		
		insertAction.setParams(entity);
		insertAction.execute();
		assertFalse(insertAction.getIssues().isEmpty());
	}
	
	@Test
	public void whenDeleteDistributedThenReturnIssues()
	{
		Entity entity = createMoveOutReasonData("whenDeleteDistributedThenReturnIssues", "whenDeleteDistributedThenReturnIssues", false);
		try {
			Field distributed = entity.getClass().getDeclaredField("distributed");
			distributed.setAccessible(true);
			distributed.setBoolean(entity, true);

		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		Action insertAction = new Create(factory, repository);

		insertAction.setParams(entity);
		insertAction.execute();
		
		Entity entityInserted = (Entity) insertAction.getResult();
		
		Action action = new Delete(factory,repository);
		action.setAfectedClass(entityInserted);
		

		action.setParams(entityInserted.getId());
		action.execute();
		List<Issue> issues = action.getIssues();

		assertTrue(issues.size() > 0);
	}

	private static void fixtureMoveOutReasosns(Action insertAction) {

		Entity entity1 = createMoveOutReasonData("123", "name1", false);
		insertAction.setParams(entity1);
		insertAction.execute();

		Entity entity2 = createMoveOutReasonData("124", "name2", false);
		insertAction.setParams(entity2);
		insertAction.execute();

		Entity entity3 = createMoveOutReasonData("125", "name3", false);
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
