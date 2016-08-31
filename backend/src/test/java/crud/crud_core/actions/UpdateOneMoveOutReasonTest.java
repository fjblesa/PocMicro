package crud.crud_core.actions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import crud.crud_core.actions.UpdateOne;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;

public class UpdateOneMoveOutReasonTest {

	@Test
	public void modifyMoveOutReasonIsStoredInRepository() {
		Repository repository = mock(Repository.class);
		Factory factory = mock(Factory.class);
		MoveOutReason reason = mock(MoveOutReason.class);
		when(factory.create()).thenReturn(reason);

		UpdateOne action = new UpdateOne(factory, repository);
		action.setParams(reason);
		action.execute();

		verify(factory).create();
//		verify(reason).validate();
		verify(repository).store(reason);
	}
}