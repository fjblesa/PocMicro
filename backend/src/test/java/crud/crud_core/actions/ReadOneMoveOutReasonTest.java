package crud.crud_core.actions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import crud.crud_core.actions.ReadOne;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.factories.Factory;
import crud.crud_core.issues.Issue;
import crud.crud_core.repositories.Repository;

public class ReadOneMoveOutReasonTest {

	@Test
	public void fetchesFromRepository() {
		Repository repository = mock(Repository.class);
		Factory factory = mock(Factory.class);
		
		Entity repositoryEntity = new MoveOutReason();
		UUID id = UUID.randomUUID();
		when(repository.read(id,null)).thenReturn(repositoryEntity);
		ReadOne action = new ReadOne(factory,repository);
		action.setParams(id);
		action.execute();

		List<Issue> issues = action.getIssues();

		assertEquals(0, issues.size());

		Entity moveOutReason = (Entity) action.getResult();
		assertEquals(repositoryEntity, moveOutReason);
	}
}
