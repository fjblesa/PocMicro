package crud.crud_core.config.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;

import crud.crud_core.actions.Action;
import crud.crud_core.actions.Create;
import crud.crud_core.config.json.ParseObject;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.entities.PhoneNumber;
import crud.crud_core.entities.ResponseObject;
import crud.crud_core.factories.Factory;
import crud.crud_core.issues.InvalidFieldValueIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.repositories.Repository;

public class ParseObjectTest {

	private static Class entityClass = PhoneNumber.class;
	private ParseObject parse;
	@Mock
	private MoveOutReason entity;
	@Mock
	private Factory factory;
	@Mock
	private Repository repository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		parse = ParseObject.getInstance();
		when(factory.create()).thenReturn(entity);
	}

	@Test
	public void whenCallSomeTimesParseObjectReturnSameObject() throws Exception {
		ParseObject sameParseObject = ParseObject.getInstance();
		assertEquals(parse, sameParseObject);
	}

	@Test
	public void whenParseStringThenReturnObject() {
		Object objectFromJson = parse.convertToEntity("{}", entityClass);
		assertTrue(entityClass.isInstance(objectFromJson));
	}

	@Test
	public void whenCallJsonSchemaThenReturnJsonSchema() {

		String jsonSchemaString = parse.getJsonSchemaString(PhoneNumber.class);
		JsonObject jsonSchema = new JsonObject(jsonSchemaString);
		JsonObject properties = jsonSchema.getJsonObject("properties");
		JsonObject number = properties.getJsonObject("number");

		assertTrue(number.getString("type").equals("string"));
	}
	
	@Test
	public void whenCallConvertToJsonWithStringThenReturnJsonObjectWithData(){
		List<String> listEntity = new ArrayList<String>();
		listEntity.add("contracts");
		String jsonObject = parse.convertToJson(listEntity);		
		assertTrue(jsonObject.contains("contracts"));
	}
	
	@Test
	public void whenCallWithResponseObjectThenReturnConvertedJson(){
		ResponseObject responseObject = new ResponseObject();
		List<Issue> issues = new ArrayList<Issue>();
		Issue issue = new InvalidFieldValueIssue(null);
		issues.add(issue);
		responseObject.setIssues(issues);
		String responseRetrieved = parse.convertToJson(responseObject);
		assertNotEquals("",responseRetrieved);
	}
	
	@Test
	public void whenCallWithActionsThenReturnConvertedJson() throws JsonProcessingException{
		MoveOutReason entityMOR = new MoveOutReason();
		Action action = new Create<Entity>(factory, repository);
		action.setParams(entityMOR);
		
		String responseRetrieved = parse.convertToJson(action, null);
		assertNotEquals("",responseRetrieved);
	}
}
