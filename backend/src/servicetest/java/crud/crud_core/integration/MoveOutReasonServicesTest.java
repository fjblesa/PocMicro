package crud.crud_core.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import crud.crud_core.enums.HttpStatusCode;

public class MoveOutReasonServicesTest {

	public static final String PATH = "/crud/move_out_reasons";
	public static final String PATH_WITH_ID = "/crud/move_out_reasons/{id}";
	private static final String PATHSCHEMA = "/schema/move_out_reasons";
	Random random = new Random();

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
				.body("properties.id.type", is("string"))
				.body("properties.code.required", is(true))
				.body("properties.name.required", is(true));
	}

	@Test
	public void whenInsertNewMoveOutReasonThenReturnObjectWithId() {

		Map<String, Object> newMoveOutReason = getMoveOutReason();

		Response response = given().contentType(JSON).body(newMoveOutReason)
				.when().post(PATH).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode()).extract()
				.response();

		JsonPath json = response.jsonPath();
		UUID idMoveOutReasonInserted = json.getObject("data.id", UUID.class);
		Response responseGetOne = when()
				.get(PATH_WITH_ID, idMoveOutReasonInserted).then()
				.statusCode(HttpStatusCode.OK.getStatusCode()).extract()
				.response();

		assertEquals(idMoveOutReasonInserted, responseGetOne.jsonPath()
				.getObject("data.id", UUID.class));

	}

	@Test
	public void whenUpdateMoveOutReasonThenReturnOkHttpCode() {
		Map<String, Object> newMoveOutReason = getMoveOutReason();
		
		UUID id = insertMoveOutReasonReturnId(newMoveOutReason);
		
		newMoveOutReason.put("id", id);
		newMoveOutReason.put("code", random.nextInt(1000));
		newMoveOutReason.put("name", "TestNameEdit"+new Date());

		given().contentType(JSON).body(newMoveOutReason).when()
				.put(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode());

		Response responseGetOne = when().get(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK.getStatusCode()).extract()
				.response();

		JsonPath jsonFromGetOne = responseGetOne.jsonPath();

		assertEquals(id, jsonFromGetOne.getObject("data.id", UUID.class));
	}

	@Test
	public void whenDeleteMoveOutReasonAndGetOneThenReturn404HttpCode() {
		Map<String, Object> newMoveOutReason = getMoveOutReason();

		UUID id = insertMoveOutReasonReturnId(newMoveOutReason);

		when().delete(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK_NO_CONTENT.getStatusCode());

		when().get(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.ERROR_NOT_FOUND.getStatusCode());
	}

	@Test
	public void whenInsertMoveOutReasonVoidThenReturnIssues() throws Exception {
		Map<String, Object> newMoveOutReason = new HashMap<>();
		given().contentType(JSON).body(newMoveOutReason).when().post(PATH)
				.then()
				.statusCode(HttpStatusCode.ERROR_BAD_REQUEST.getStatusCode())
				.body("issues.size()", greaterThan(0))
				.body("issues.any {it.containsKey('type')}", is(true))
				.body("issues.any {it.containsKey('vars')}", is(true))
				.body("issues.any {it.containsKey('msg')}", is(true));
	}

	@Test
	public void whenDeleteWithoutIdParamsThenReturnNotFound() throws Exception {
		when().delete("/move_out_reasons/").then()
				.statusCode(HttpStatusCode.ERROR_NOT_FOUND.getStatusCode());
	}

	@Test
	public void whenDeleteWithoutNotExistIdThenReturnBadRequest()
			throws Exception {
		when().delete(PATH_WITH_ID, UUID.randomUUID()).then()
				.statusCode(HttpStatusCode.ERROR_BAD_REQUEST.getStatusCode())
				.body("issues.size()", greaterThan(0));
	}

	@Test
	public void whenUpdateWithoutNotExistIdThenReturnBadRequest()
			throws Exception {
		when().delete(PATH_WITH_ID, UUID.randomUUID()).then()
				.statusCode(HttpStatusCode.ERROR_BAD_REQUEST.getStatusCode())
				.body("issues.size()", greaterThan(0));
	}

	private UUID insertMoveOutReasonReturnId(
			Map<String, Object> newMoveOutReason) {
		Response response = given().contentType(JSON).body(newMoveOutReason)
				.when().post(PATH).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode()).extract()
				.response();

		JsonPath json = response.jsonPath();
		return json.getObject("data.id", UUID.class);
	}

	private Map<String, Object> getMoveOutReason() {
		Map<String, Object> newMoveOutReason = new HashMap<>();
		newMoveOutReason.put("code",random.nextInt(1000));
		newMoveOutReason.put("name", "TestName"+ (new Date().getTime()));
		return newMoveOutReason;
	}
}
