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
import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import crud.crud_core.enums.HttpStatusCode;

public class CommercialSitesServicesTest {
	public static final String PATH_WITH_ID = "/crud/commercial_sites/{id}";
	public static final String PATH = "/crud/commercial_sites";
	public static final String PATHWATERUTILITY = "/crud/water_utilities";
	private static final String PATHSCHEMA = "/schema/commercial_sites";
	public static final UUID ID_NOT_EXIST = UUID.randomUUID();
	private UUID idWaterUtility;
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
	public void whenInsertNewCommercialSitesThenReturnObjectWithId() {

		Map<String, Object> commercialSites = getCommercialSitesDummy();

		Response response = given().contentType(JSON).body(commercialSites)
				.when().post(PATH).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode()).extract()
				.response();

		JsonPath json = response.jsonPath();
		assertNotNull(json.getObject("data.id", UUID.class));

	}

	@Test
	public void whenUpdateCommercialSitesThenReturnOkHttpCode() {
		
		Map<String, Object> commercialSites = getCommercialSitesDummy();

		UUID id = insertCommercialSitesReturnId(commercialSites);
		commercialSites.put("id", id);
		commercialSites.put("name", "Sociedad Anonima update, SA" + new Date().getTime());
		commercialSites.put("code", random.nextInt(1000));
		commercialSites.put("waterutilityid", idWaterUtility);

		given().contentType(JSON).body(commercialSites).when()
				.put(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode());

		Response responseGetOne = when().get(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK.getStatusCode()).extract()
				.response();

		JsonPath jsonFromGetOne = responseGetOne.jsonPath();

		assertEquals(id, jsonFromGetOne.getObject("data.id", UUID.class));
	}
	
	private UUID insertWaterUtilityReturnId() {
		Map<String, Object> waterUtility = getWaterUtilityDummy();

		Response response = given().contentType(JSON).body(waterUtility).when()
				.post(PATHWATERUTILITY).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode()).extract()
				.response();

		JsonPath json = response.jsonPath();
		return json.getObject("data.id", UUID.class);
	}

	@Test
	public void whenDeleteCommercialSitesAndGetOneThenReturn404HttpCode() {
		Map<String, Object> commercialSites = getCommercialSitesDummy();

		UUID id = insertCommercialSitesReturnId(commercialSites);

		when().delete(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.OK_NO_CONTENT.getStatusCode());

		when().get(PATH_WITH_ID, id).then()
				.statusCode(HttpStatusCode.ERROR_NOT_FOUND.getStatusCode());
	}

	@Test
	public void whenInsertCommercialSitesVoidThenReturnIssues()
			throws Exception {
		Map<String, Object> commercialSites = new HashMap<>();
		given().contentType(JSON).body(commercialSites).when().post(PATH)
				.then()
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

	private UUID insertCommercialSitesReturnId(Map<String, Object> newEntity) {
		Response response = given().contentType(JSON).body(newEntity).when()
				.post(PATH).then()
				.statusCode(HttpStatusCode.OK_CREATE.getStatusCode())
				.extract()
				.response();

		JsonPath json = response.jsonPath();
		return json.getObject("data.id", UUID.class);
	}

	private String jsonArrayOjectSize(String object) {
		return object + ".size()";
	}

	private Map<String, Object> getCommercialSitesDummy() {
		idWaterUtility = insertWaterUtilityReturnId();
		
		Map<String, Object> entity = new HashMap<>();

		entity.put("name", "Sociedad de Agua, SA"+ new Date().getTime());
		entity.put("code", random.nextInt(1000));
		entity.put("waterutilityid", idWaterUtility);

		return entity;
	}

	private String jsonObjectContainsKey(String object, String containsKey) {
		return object + ".any {it.containsKey('" + containsKey + "')}";
	}
	
	private Map<String, Object> getWaterUtilityDummy() {
		Map<String, Object> entity = new HashMap<>();
		entity.put("name", "Sociedad de Agua, SA"+ new Date().getTime());
		entity.put("code", random.nextInt(1000));
		entity.put("identifier", "A123456789");

		return entity;
	}
}
