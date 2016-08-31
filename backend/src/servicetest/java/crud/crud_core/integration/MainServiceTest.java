package crud.crud_core.integration;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import crud.crud_core.services.MainService;

@RunWith(VertxUnitRunner.class)
public class MainServiceTest {

	Vertx vertx;
	int port = 8081;

	@Before
	public void setUp(TestContext context) throws IOException {
		vertx = Vertx.vertx();
		ServerSocket socket = new ServerSocket(0);
		socket.close();
		DeploymentOptions options = new DeploymentOptions()
				.setConfig(new JsonObject().put("http.port", port));
		vertx.deployVerticle(MainService.class.getName(), options,
				context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void callAsyncToGetMethodRetrievedData(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(port, "localhost",
				"/crud/move_out_reasons", response -> {
					response.handler(body -> {
						context.assertEquals(response.statusCode(), 200);
						async.complete();
					});
				});
	}
}
