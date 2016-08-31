package crud.crud_core.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
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

public class WaterUtilitiesServicesTest {
	public static final String PATH_WITH_ID = "/crud/water_utilities/{id}";
	public static final String PATH = "/crud/water_utilities";
	private static final String PATHSCHEMA = "/schema/water_utilities";
	public static final UUID ID_NOT_EXIST = UUID.randomUUID();
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
				.body("properties.id.type", is("string"));
	}

	@Test
	public void whenInsertNewwaterUtilitiesThenReturnObjectWithId() {

		Map<String, Object> waterUtility = getWaterUtilityDummy();

		Response response = given().contentType(JSON).body(waterUtility).when()
				.post(PATH).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode()).extract()
				.response();

		JsonPath json = response.jsonPath();
		assertNotNull(json.getObject("data.id", UUID.class));
	}

	@Test
	public void whenUpdatewaterUtilitiesThenReturnOkHttpCode() {
		Map<String, Object> waterUtility = getWaterUtilityDummy();

		String id = insertwaterUtilityReturnId(waterUtility);

		waterUtility.put("id", id);
		waterUtility.put("name", "Sociedad Anonima, SA"+ new Date());
		waterUtility.put("code", random.nextInt(1000));

		given().contentType(JSON).body(waterUtility).when().put(PATH_WITH_ID, id)
				.then().statusCode(HttpStatusCode.OK_CREATE.getStatusCode());

		Response responseGetOne = when().get(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK.getStatusCode()).extract()
				.response();

		JsonPath jsonFromGetOne = responseGetOne.jsonPath();

		assertEquals(id, jsonFromGetOne.getString("data.id"));
	}

	@Test
	public void whenDeletewaterUtilityAndGetOneThenReturn404HttpCode() {
		Map<String, Object> waterUtility = getWaterUtilityDummy();

		String id = insertwaterUtilityReturnId(waterUtility);

		when().delete(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK_NO_CONTENT.getStatusCode());

		when().get(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.ERROR_NOT_FOUND.getStatusCode());
	}

	@Test
	public void whenInsertwater_utilitiesVoidThenReturnIssues() throws Exception {
		Map<String, Object> waterUtility = new HashMap<>();
		given().contentType(JSON).body(waterUtility).when().post(PATH).then()
				.statusCode(HttpStatusCode.ERROR_BAD_REQUEST.getStatusCode())
				.body(jsonArrayOjectSize("issues"), greaterThan(0))
				.body(jsonObjectContainsKey("issues", "type"), is(true))
				.body(jsonObjectContainsKey("issues", "vars"), is(true))
				.body(jsonObjectContainsKey("issues", "msg"), is(true));
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

	private String insertwaterUtilityReturnId(Map<String, Object> newEntity) {
		Response response = given().contentType(JSON).body(newEntity).when()
				.post(PATH).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode()).extract()
				.response();

		JsonPath json = response.jsonPath();
		return json.getString("data.id");
	}

	private String jsonArrayOjectSize(String object) {
		return object + ".size()";
	}

	private Map<String, Object> getWaterUtilityDummy() {
		Map<String, Object> entity = new HashMap<>();
		
		entity.put("name", "Sociedad de Agua, SA"+ new Date().getTime());
		entity.put("code", random.nextInt(1000));
		entity.put("identifier", "A123456789");

		return entity;
	}

	private String jsonObjectContainsKey(String object, String containsKey) {
		return object + ".any {it.containsKey('" + containsKey + "')}";
	}
}
