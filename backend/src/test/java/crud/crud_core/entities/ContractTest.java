package crud.crud_core.entities;

import static crud.crud_core.entities.EntityTestUtils.assertEqualContracts;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import crud.crud_core.entities.Contract;

public class ContractTest {

	Contract entity;
	
	@Before
	public void setUp(){
		entity = new Contract();
		entity.setId(UUID.randomUUID());
	}

	@Test
	public void canCloneItself()   {
		Contract dummyContract = EntityTestUtils.createContractDummy();
		
		Contract clonedContract = dummyContract.clone();
		
		assertNotNull(clonedContract);
		assertEqualContracts(dummyContract, clonedContract);
	}
	
	@Test
	public void canCopyItself()   {
		Contract dummyContract = EntityTestUtils.createContractDummy();		
		Contract copyContract = new Contract();		
		copyContract.copy(dummyContract);
		
		assertEqualContracts(copyContract, dummyContract);		
	}
}
