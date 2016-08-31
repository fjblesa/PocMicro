package crud.crud_core.utils.validations.validator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import javax.jws.soap.InitParam;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import crud.crud_core.actions.Action;
import crud.crud_core.actions.ReadOne;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.factories.Factory;
import crud.crud_core.issues.DistributedIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.repositories.Repository;
import crud.crud_core.utils.validations.validator.IsDistributedValidator;

public class IsDistributedValidatorTest {
	
	@InjectMocks
	private IsDistributedValidator validator ;

	@Mock
	private Repository repository;

	@Mock
	private Factory factory;

	@Mock
	private Action action;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void whenCallValidatorWithDistributedEntityThrowException() {
		MoveOutReason entityRead = new MoveOutReason();
		Entity entity = new MoveOutReason();
		
		try {
			Field distributed = entityRead.getClass().getDeclaredField("distributed");
			distributed.setAccessible(true);
			distributed.setBoolean(entityRead, true);

		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		when(action.getResult()).thenReturn(entityRead);

		List<Issue> issues = validator.validate(entity);
			
		for (Issue issue : issues) {
			assertTrue(issue instanceof DistributedIssue);
		}
	}
}
