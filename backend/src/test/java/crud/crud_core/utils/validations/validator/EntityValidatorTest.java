package crud.crud_core.utils.validations.validator;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import crud.crud_core.entities.Contract;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.entities.PhoneNumber;
import crud.crud_core.enums.Status;
import crud.crud_core.issues.Issue;
import crud.crud_core.issues.MandatoryFieldMissingIssue;
import crud.crud_core.utils.validations.validator.BaseValidator;
import crud.crud_core.utils.validations.validator.EntityValidator;

public class EntityValidatorTest {

	@Test
	public void whenValidateWrongEntityRetrievedIssues() {
		Entity entity = new MoveOutReason();
		BaseValidator validator = new EntityValidator();
		List<Issue> issues = validator.validate(entity);
		
		assertTrue(issues.size()>0);
		for (Issue issue : issues) {
			assertTrue(issue instanceof MandatoryFieldMissingIssue);
		}
	}
	
	@Test
	public void whenValidateEntityWithoutPhoneNumberRetrievedIssue()
	{
		Contract entity = new Contract();
		entity.setMandatoryPhoneNumber(new PhoneNumber());
		entity.setActivationDate(new Date());
		entity.setStatus(Status.ACTIVE);
		
		BaseValidator validator = new EntityValidator();
		List<Issue> issues = validator.validate(entity);
		
		assertTrue(issues.size()>0);
		for (Issue issue : issues) {
			assertTrue(issue instanceof MandatoryFieldMissingIssue);
			assertEquals("mandatoryPhoneNumber.number",issue.getVars().get(0));
		}
	}
	
	@Test
	public void whenValidateEmptyPhoneNumberRetrievedIssue()
	{
		PhoneNumber phoneNumber = new PhoneNumber();
		
		BaseValidator validator = new EntityValidator();
		List<Issue> issues = validator.validate(phoneNumber);
		
		assertTrue(issues.size()>0);
		for (Issue issue : issues) {
			assertTrue(issue instanceof MandatoryFieldMissingIssue);
		}
	}
}
