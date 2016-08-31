package crud.crud_core.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.issues.Issue;
import crud.crud_core.issues.MandatoryFieldMissingIssue;

public class MoveOutReasonsTest {

	private MoveOutReason reason;
	private MoveOutReason data;

	@Before
	public void setUp() {
		reason = new MoveOutReason();
		data = new MoveOutReason();
	}


	@Test
	public void loadDataCorrectly() {

		String codeValue = "noCode";
		String nameValue = "noName";

		data.setCode(codeValue);
		data.setName(nameValue);
		data.setActive(false);

		reason.setCode(codeValue);
		reason.setName(nameValue);
		reason.setActive(false);

		assertFalse(reason.isActive());
		assertEquals(codeValue, reason.getCode());
		assertEquals(nameValue, reason.getName());
		assertFalse(reason.isDistributed());
	}

	@Test
	public void loadDataCorrectlyFromAnotherEntity() {
			MoveOutReason entity = createMoveOutReason();
			
			MoveOutReason entityLoadData = new MoveOutReason();
			entityLoadData.copy(entity);
			
			assertsEquals(entity, entityLoadData);
	}

	private void assertsEquals(MoveOutReason entity, MoveOutReason entityLoadData) {
		assertEquals(entity.getId(), entityLoadData.getId());
		assertEquals(entity.getCode(), entityLoadData.getCode());
		assertEquals(entity.getName(), entityLoadData.getName());
		assertEquals(entity.isActive(), entityLoadData.isActive());
		assertEquals(entity.isDistributed(), entityLoadData.isDistributed());
	}

	private MoveOutReason createMoveOutReason() {
		MoveOutReason entity = new MoveOutReason();
		entity.setActive(true);
		entity.setCode("1");
		entity.setId(UUID.randomUUID());
		entity.setName("name");
		return entity;
	}
	
	@Test
	public void cloneAllFields() throws Exception {
		MoveOutReason entity = createMoveOutReason();
		
		MoveOutReason clonedEntity = entity.clone();
		
		assertFalse(entity == clonedEntity);
		assertsEquals(entity, clonedEntity);
		
	}
	
	private void checkMandatoryField(String notInformedField) {
		reason.copy(data);

		List<Issue> issues = reason.validate();

		assertEquals(1, issues.size());
		Issue issue = issues.get(0);
		assertTrue(issue instanceof MandatoryFieldMissingIssue);
		Issue mandatoryFieldMissingIssue = (Issue) issue;

		assertEquals(notInformedField, mandatoryFieldMissingIssue.getVars().get(0));
	}
}
