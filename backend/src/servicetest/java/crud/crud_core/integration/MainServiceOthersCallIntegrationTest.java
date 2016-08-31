package crud.crud_core.integration;

import static io.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;

import crud.crud_core.enums.HttpStatusCode;

public class MainServiceOthersCallIntegrationTest {
	
	public static final String PATH = "/discovery/entities/";
	@Before
	public void setupURL() {
		// here we setup the default URL and API base path to use throughout the
		// tests
		RestAssured.baseURI = "http://localhost:8081";
		RestAssured.basePath = "/";
		RestAssured.defaultParser = Parser.JSON;
	}
	@Test
	public void whenCallToDiscoveryThenReturnListOfRegisterEntities() {
		Response response = when().get(PATH).then().statusCode(HttpStatusCode.OK.getStatusCode()).extract().response();
		JsonPath jsonFromGetOne = response.jsonPath();

		assertEquals("move_out_reasons", jsonFromGetOne.getList("data").get(0));
	}
}
