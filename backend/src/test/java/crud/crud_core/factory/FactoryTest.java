package crud.crud_core.factory;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.factories.Factory;

public class FactoryTest {
	
	@Test
	public void checkCorrectlyInstanceOfTypeEntity(){
		Factory factory = new Factory();
		factory.setType(MoveOutReason.class);
		Entity entity = factory.create();
		
		assertTrue(entity instanceof MoveOutReason);
		
	}

}
