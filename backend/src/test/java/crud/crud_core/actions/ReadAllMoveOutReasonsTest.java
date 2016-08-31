package crud.crud_core.actions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import crud.crud_core.actions.ReadAll;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.factories.Factory;
import crud.crud_core.issues.Issue;
import crud.crud_core.repositories.Repository;

public class ReadAllMoveOutReasonsTest {

	@Test
	public void executeReturnsAllMoveOutReasonsReturnByRepository() {
		Factory factory = mock(Factory.class);
		when(factory.getType()).thenReturn(MoveOutReason.class);
		Repository repository= mock(Repository.class);
		List<MoveOutReason> repositoryReasons = new ArrayList<MoveOutReason>();
		when(repository.readAll(MoveOutReason.class)).thenReturn(repositoryReasons);
		
		ReadAll action= new ReadAll(factory,repository);
		action.execute();
		
		List<Issue> issues = action.getIssues();
		assertEquals(0, issues.size());
		
		List<MoveOutReason> actionReasons = (List<MoveOutReason>) action.getResult();
		assertEquals(repositoryReasons, actionReasons);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenCallSetParamsThrowException(){
		Factory factory = mock(Factory.class);
		when(factory.getType()).thenReturn(MoveOutReason.class);
		Repository repository= mock(Repository.class);
		
		ReadAll action= new ReadAll(factory,repository);
		action.setParams(1L);
	}
}
