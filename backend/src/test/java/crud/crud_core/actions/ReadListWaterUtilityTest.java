package crud.crud_core.actions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import crud.crud_core.actions.ReadList;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.factories.Factory;
import crud.crud_core.repositories.Repository;

public class ReadListWaterUtilityTest {
	
	@Test
	public void fetchesFromRepository() {
		Repository repository = mock(Repository.class);
		Factory factory = mock(Factory.class);
		
		List mochResultDB = new ArrayList<WaterUtility>();
		when(repository.readListWaterUtilityFilterActive()).thenReturn(mochResultDB);
		ReadList<WaterUtility> action = new ReadList<WaterUtility>(factory, repository);
		
		action.execute();
		
		List<WaterUtility> result = (List<WaterUtility>) action.getResult();
		
		assertEquals(result ,mochResultDB);
	}
}
