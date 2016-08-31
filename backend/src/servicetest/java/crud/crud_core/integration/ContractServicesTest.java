package crud.crud_core.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import crud.crud_core.entities.PhoneNumber;
import crud.crud_core.enums.HttpStatusCode;
import crud.crud_core.enums.Status;
@Ignore
public class ContractServicesTest {
	public static final String PATH_WITH_ID = "/crud/contracts/{id}";
	public static final String PATH = "/crud/contracts";
	private static final String PATHSCHEMA = "/schema/contracts";
	public static final UUID ID_NOT_EXIST = UUID.randomUUID();

	@Before
	public void setupURL() {
		// here we setup the default URL and API base path to use throughout the
		// tests
		RestAssured.baseURI = "http://localhost:8081";
		RestAssured.basePath = "/";
		RestAssured.defaultParser = Parser.JSON;
	}

	@Test
	public void whenCallSchemaThenReturnJsonSchema() {
		when().get(PATHSCHEMA).then()
				.statusCode(HttpStatusCode.OK.getStatusCode())
				.body("properties.id.type", is("string"));
	}

	@Test
	public void whenInsertNewContractThenReturnObjectWithId() {

		Map<String, Object> contract = getContractDummy();

		Response response = given().contentType(JSON).body(contract).when()
				.post(PATH).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode()).extract()
				.response();

		JsonPath json = response.jsonPath();
		assertNotNull(json.getObject("data.id", UUID.class));

	}

	@Test
	public void whenUpdateContractThenReturnOkHttpCode() {
		Map<String, Object> contract = getContractDummy();

		String id = insertContractReturnId(contract);

		contract.put("id", id);
		contract.put("externalReference", "RF-TEST");

		given().contentType(JSON).body(contract).when().put(PATH_WITH_ID, id)
				.then().statusCode(HttpStatusCode.OK.getStatusCode());

		Response responseGetOne = when().get(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK.getStatusCode()).extract()
				.response();

		JsonPath jsonFromGetOne = responseGetOne.jsonPath();

		assertEquals(id, jsonFromGetOne.getString("data.id"));
	}

	@Test
	public void whenDeleteContractAndGetOneThenReturn404HttpCode() {
		Map<String, Object> newContract = getContractDummy();

		String id = insertContractReturnId(newContract);

		when().delete(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK_NO_CONTENT.getStatusCode());

		when().get(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.ERROR_NOT_FOUND.getStatusCode());
	}

	@Test
	public void whenInsertContractVoidThenReturnIssues() throws Exception {
		Map<String, Object> newContract = new HashMap<>();
		given().contentType(JSON).body(newContract).when().post(PATH).then()
				.statusCode(HttpStatusCode.ERROR_BAD_REQUEST.getStatusCode())
				.body(jsonArrayOjectSize("issues"), greaterThan(0))
				.body(jsonObjectContainsKey("issues", "type"), is(true))
				.body(jsonObjectContainsKey("issues", "vars"), is(true))
				.body(jsonObjectContainsKey("issues", "msg"), is(true));
	}

	private String jsonObjectContainsKey(String object, String containsKey) {
		return object + ".any {it.containsKey('" + containsKey + "')}";
	}

	@Test
	public void whenDeleteWithoutIdParamsThenReturnNotFound() throws Exception {
		when().delete(PATH + "/").then()
				.statusCode(HttpStatusCode.ERROR_NOT_FOUND.getStatusCode());
	}

	@Test
	public void whenDeleteWithoutNotExistIdThenReturnBadRequest()
			throws Exception {
		when().delete(PATH_WITH_ID, ID_NOT_EXIST).then()
				.statusCode(HttpStatusCode.ERROR_BAD_REQUEST.getStatusCode())
				.body(jsonArrayOjectSize("issues"), greaterThan(0));
	}

	@Test
	public void whenUpdateWithoutNotExistIdThenReturnBadRequest()
			throws Exception {
		when().delete(PATH_WITH_ID, ID_NOT_EXIST).then()
				.statusCode(HttpStatusCode.ERROR_BAD_REQUEST.getStatusCode())
				.body(jsonArrayOjectSize("issues"), greaterThan(0));
	}

	private String insertContractReturnId(Map<String, Object> newContract) {
		Response response = given().contentType(JSON).body(newContract).when()
				.post(PATH).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode()).extract()
				.response();

		JsonPath json = response.jsonPath();
		return json.getString("data.id");
	}

	private String jsonArrayOjectSize(String object) {
		return object + ".size()";
	}

	private Map<String, Object> getContractDummy() {
		Map<String, Object> contract = new HashMap<>();

		contract.put("disconectionAllowed", true);
		contract.put("externalReference", "RF654");
		contract.put("activationDate", new Date());
		contract.put("mandatoryPhoneNumber", new PhoneNumber("9999999"));
		contract.put("status", Status.PENDING);

		return contract;
	}
}
