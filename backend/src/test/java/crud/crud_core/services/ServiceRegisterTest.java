package crud.crud_core.services;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import crud.crud_core.entities.Contract;
import crud.crud_core.exceptions.NotFoundServiceException;
import crud.crud_core.services.ServiceRegister;

public class ServiceRegisterTest {

	@Test
	public void registerEntityAndConfirmExistInMap() {
		String entityName = "contracts";
		ServiceRegister.registerService(entityName,Contract.class);
		Class className = ServiceRegister.getEntityClass(entityName);
		assertEquals("Contract", className.getSimpleName());
	}
	
	@Test(expected=NotFoundServiceException.class)
	public void getEntityNotRegisterThowException(){
		String entityName = "entityNotFound";
		ServiceRegister.getEntityClass(entityName);
	}
	
	@Test
	public void getEntityRegisterListNotEmpty()
	{
		String entityName = "contracts";
		ServiceRegister.registerService(entityName,Contract.class);
		List<String> listEntity = ServiceRegister.getEntityRegisterList();
		assertNotNull(listEntity);
	}
	
	@Test
	public void whenCallRegisterServiceWithoutParamsAndCallGetEntityRegisterListRetrievedCompleteList(){
		ServiceRegister.registerService();
		List<String> listServices = ServiceRegister.getEntityRegisterList();
		for (String string : listServices) {
			assertTrue(string != "");
		}
	}
	@Test
	public void getEndpointRegisterInMapServices(){
		ServiceRegister.registerService();
		String endpoint = ServiceRegister.getEndPointRegister(Contract.class);
		
		assertEquals(endpoint, "contracts");
		
	}
}
