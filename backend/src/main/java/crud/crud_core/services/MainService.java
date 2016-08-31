package crud.crud_core.services;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

//test
public class MainService extends AbstractVerticle {

	public static void main(String[] args) throws Exception {
		
		Vertx vertx = Vertx.vertx();
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);
		
		ServiceRegister.registerService();

		router.route().handler(CorsHandler.create("*")
				.allowedMethod(HttpMethod.GET)
				.allowedMethod(HttpMethod.POST)
				.allowedMethod(HttpMethod.PUT)
				.allowedMethod(HttpMethod.DELETE)
				.allowedHeader("Content-Type")
				.allowedMethod(HttpMethod.OPTIONS));

		router.route().handler(BodyHandler.create());

		router.get("/crud/:entity").handler(ControllerService::verifyEntityExists);
		router.get("/crud/:entity").handler(ControllerService::readAll);

		router.post("/crud/:entity").handler(ControllerService::verifyEntityExists);
		router.post("/crud/:entity").handler(ControllerService::verifyEntity);
		router.post("/crud/:entity").handler(ControllerService::insert);
				
		router.get("/crud/:entity/:id").handler(ControllerService::verifyEntityExists);
		router.get("/crud/:entity/:id").handler(ControllerService::readOne);

		router.delete("/crud/:entity/:id").handler(ControllerService::verifyEntityExists);
		router.delete("/crud/:entity/:id").handler(ControllerService::delete);

		router.put("/crud/:entity/:id").handler(ControllerService::verifyEntityExists);
		router.put("/crud/:entity/:id").handler(ControllerService::verifyEntity);
		router.put("/crud/:entity/:id").handler(ControllerService::update);
		
		router.get("/schema/:entity").handler(ControllerService::verifyEntityExists);
		router.get("/schema/:entity").handler(ControllerService::schema);
		
		router.get("/list/:entity").handler(ControllerService::verifyEntityExists);
		router.get("/list/:entity").handler(ControllerService::readAllList);
		
		router.get("/discovery/entities/").handler(ControllerService::discovery);
		
		server.requestHandler(router::accept).listen(8081);
		System.out.println("Start Server!");
	}
}