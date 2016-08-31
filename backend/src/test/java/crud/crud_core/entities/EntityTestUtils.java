package crud.crud_core.entities;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import crud.crud_core.entities.CommercialSite;
import crud.crud_core.entities.Contract;
import crud.crud_core.entities.PhoneNumber;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.enums.Status;

public class EntityTestUtils {

	
	public static Contract createContractDummy() {
		Contract entityContract = new Contract();
		entityContract.setDisconectionAllowed(true);
		entityContract.setExternalReference("RF654");
		entityContract.setActivationDate(new Date());
		entityContract.setMandatoryPhoneNumber(new PhoneNumber("9999999"));
		entityContract.setStatus(Status.PENDING);
		return entityContract;
	}
	
	public static void assertEqualContracts(Contract expected, Contract actual){
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getActivationDate(), actual.getActivationDate());
		assertEquals(expected.getExternalReference(), actual.getExternalReference());
		assertEquals(expected.getMandatoryPhoneNumber(), actual.getMandatoryPhoneNumber());
		assertEquals(expected.getStatus(), actual.getStatus());
	}

	public static WaterUtility createWaterUtilityDummy() {
		WaterUtility entity = new WaterUtility();
		entity.setCode("ABS"+new Date());
		entity.setIdentifier("A123456789");
		entity.setName("Sociedad Anonima, SA"+new Date());
		return entity;
	}

	public static CommercialSite createCommercialsiteDummy() {
		CommercialSite entity = new CommercialSite();
		entity.setCode("ABS");
		entity.setName("Sociedad Anonima, SA");
		return entity;
	}

	public static void assertEqualCommercialSite(CommercialSite expected, CommercialSite actual){
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getCode(), actual.getCode());
		assertEquals(expected.getWaterutilityid(), actual.getWaterutilityid());
		assertEquals(expected.isActive(), actual.isActive());
	}
}
