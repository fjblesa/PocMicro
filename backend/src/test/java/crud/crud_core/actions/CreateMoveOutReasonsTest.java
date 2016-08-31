package crud.crud_core.actions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import crud.crud_core.actions.Action;
import crud.crud_core.actions.Create;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.factories.Factory;
import crud.crud_core.issues.DuplicatedKeyIssue;
import crud.crud_core.issues.ErrorIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.repositories.Repository;

public class CreateMoveOutReasonsTest {

	@Mock
	private MoveOutReason entity;
	@Mock
	private Factory factory;
	@Mock
	private Repository repository;
	
	private Action action;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(factory.create()).thenReturn(entity);

		action = new Create(factory, repository);
	}

	@Test
	public void createIsStoredInRepositoryAndIssuesIsEqualZero() {
		action.setParams(entity);
		action.execute();

		List<Issue> issues = action.getIssues();

		assertEquals(0, issues.size());

//		verify(entity).validate(); TODO revisar valid 
		verify(repository).store(entity);
	}

	@Test
	public void invalidMoveOutReasonDataReturnsNullAndHasIssues() {

		List<Issue> issues = new ArrayList<Issue>();
		Issue issue = new DuplicatedKeyIssue("");
		issues.add(issue);
		PersistenceException pe = new PersistenceException(issues);
		
		List<Issue> validationIssues = new ArrayList<Issue>();
		validationIssues.add(new ErrorIssue());
		when(entity.validate()).thenReturn(validationIssues);
		Entity nullEntity = mock(Entity.class);
		when(factory.createNullEntity()).thenReturn(nullEntity);

		when(repository.store((Entity)entity)).thenThrow(pe);
		action.setParams(entity);
		action.execute();
		Entity entityTest = (Entity) action.getResult();

		verify(factory, atLeastOnce()).createNullEntity();
	}

	@Test
	public void invalidMoveOutResaonRepositoryHasIssues() {
		List<Issue> validationIssues = new ArrayList<Issue>();
		validationIssues.add(new ErrorIssue());
		when(repository.store(entity)).thenThrow(new PersistenceException(validationIssues));

		action.setParams(entity);
		action.execute();

		List<Issue> issues = action.getIssues();

		assertEquals(1, issues.size());

//		verify(entity).validate(); TODO revisar valid 
		verify(repository).store(entity);
	}
}
