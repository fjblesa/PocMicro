package crud.crud_core.services;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.UUID;

import crud.crud_core.actions.ActionFactory;
import crud.crud_core.actions.Create;
import crud.crud_core.actions.Delete;
import crud.crud_core.actions.ReadAll;
import crud.crud_core.actions.ReadList;
import crud.crud_core.actions.ReadOne;
import crud.crud_core.actions.UpdateOne;
import crud.crud_core.config.json.ParseObject;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.NullEntity;
import crud.crud_core.entities.ResponseObject;
import crud.crud_core.enums.HttpStatusCode;
import crud.crud_core.exceptions.NotFoundServiceException;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.issues.Issue;
import crud.crud_core.utils.validations.validator.BaseValidator;
import crud.crud_core.utils.validations.validator.EntityValidator;
import crud.crud_core.views.Views;

public class ControllerService {

	private static ActionFactory actionFactory = new ActionFactory();
	private static Class<?> entityClass;
	private static HttpServerResponse response;
	private static ParseObject parser;

	public static void verifyEntityExists(RoutingContext routingContext) {
		String entityName = routingContext.request().getParam("entity");

		try {
			entityClass = ServiceRegister.getEntityClass(entityName);
			routingContext.next();
		} catch (NotFoundServiceException e) {
			response = routingContext.response();
			responseNotFound();
		}
	}

	public static void readAllList(RoutingContext routingContext) {

		response = routingContext.response();

		ReadList<Entity> action = actionFactory.createReadListAction(entityClass);
		action.execute();

		parser = ParseObject.getInstance();

		response.putHeader("content-type", "application/json; charset=utf-8")
				.end(parser.convertToJson(action, Views.Summary.class));
	}
	
	
	public static void readAll(RoutingContext routingContext) {

		response = routingContext.response();

		ReadAll<Entity> action = actionFactory.createReadAllAction(entityClass);
		action.execute();

		parser = ParseObject.getInstance();

		response.putHeader("content-type", "application/json; charset=utf-8")
				.end(parser.convertToJson(action, Views.Summary.class));
	}

	public static void readOne(RoutingContext routingContext) {
		UUID idEntity = getContextId(routingContext);

		ReadOne<Entity> action = actionFactory.createReadOneAction(entityClass);
		action.setParams(idEntity);

		response = routingContext.response();
		parser = ParseObject.getInstance();
		try {
			action.execute();
		} catch (PersistenceException pe) {
			response.setStatusCode(HttpStatusCode.ERROR_NOT_FOUND
					.getStatusCode());
		}

		response.putHeader("content-type", "application/json; charset=utf-8")
				.end(parser.convertToJson(action, Views.Detail.class));
	}

	public static void delete(RoutingContext routingContext) {
		UUID idEntity = getContextId(routingContext);

		Delete<Entity> action = actionFactory.createDeleteAction(entityClass);
		action.setParams(idEntity);
		action.execute();

		response = routingContext.response();
		parser = ParseObject.getInstance();
		if (action.getIssues().isEmpty()) {
			response.setStatusCode(HttpStatusCode.OK_NO_CONTENT.getStatusCode());
		} else {
			response.setStatusCode(HttpStatusCode.ERROR_BAD_REQUEST
					.getStatusCode());
		}
		response.putHeader("content-type", "application/json; charset=utf-8")
				.end(parser.convertToJson(action));
	}

	public static void insert(RoutingContext routingContext) {
		parser = ParseObject.getInstance();
		Entity entityToInsert = (Entity) parser.convertToEntity(
				routingContext.getBodyAsString(), entityClass);

		Create<Entity> action = actionFactory.createCreateAction(entityClass);

		action.setParams(entityToInsert);
		action.execute();

		response = routingContext.response();
		if (action.getIssues().isEmpty()) {
			response.setStatusCode(HttpStatusCode.OK_CREATE.getStatusCode());
		} else {
			response.setStatusCode(HttpStatusCode.ERROR_BAD_REQUEST
					.getStatusCode());
		}
		response.putHeader("content-type", "application/json; charset=utf-8")
				.end(parser.convertToJson(action));
	}

	public static void update(RoutingContext routingContext) {
		response = routingContext.response();
		parser = ParseObject.getInstance();

		UUID idEntity = getContextId(routingContext);
		Entity entity = (Entity) parser.convertToEntity(
				routingContext.getBodyAsString(), entityClass);

		if (!entity.getId().equals(idEntity)) {
			responseNotFound();
		} else {
			UpdateOne<Entity> action = actionFactory
					.createUpdateAction(entityClass);

			action.setParams(entity);
			action.execute();
			
			response = routingContext.response();
			if (action.getIssues().isEmpty()) {
				response.setStatusCode(HttpStatusCode.OK_CREATE.getStatusCode());
			} else {
				response.setStatusCode(HttpStatusCode.ERROR_BAD_REQUEST
						.getStatusCode());
			}

			response.putHeader("content-type",
					"application/json; charset=utf-8").end(	parser.convertToJson(action));
		}
	}

	public static void schema(RoutingContext routingContext) {
		response = routingContext.response();

		parser = ParseObject.getInstance();
		String result =  parser.getJsonSchemaString(entityClass);

		response.putHeader("content-type", "application/json; charset=utf-8")
				.end( result);
	}

	public static void discovery(RoutingContext routingContext) {
		response = routingContext.response();

		List<String> listEntities = ServiceRegister.getEntityRegisterList();

		parser = ParseObject.getInstance();
		String result = parser.convertToJson(listEntities);
		response.end(result);
	}

	public static void verifyEntity(RoutingContext routingContext) {

		parser = ParseObject.getInstance();
		Entity entityToInsert = (Entity) parser.convertToEntity(
				routingContext.getBodyAsString(), entityClass);

		BaseValidator validator = new EntityValidator();

		response = routingContext.response();
		List<Issue> issues = validator.validate(entityToInsert);
		if (!issues.isEmpty()) {
			response.setStatusCode(HttpStatusCode.ERROR_BAD_REQUEST
					.getStatusCode());

			ResponseObject responseObject = new ResponseObject();
			responseObject.setData(new NullEntity());
			responseObject.setIssues(issues);

			response.putHeader("content-type",
					"application/json; charset=utf-8").end(
					parser.convertToJson(responseObject));
		} else
			routingContext.next();
	}

	private static void responseNotFound() {
		response.setStatusCode(HttpStatusCode.ERROR_NOT_FOUND.getStatusCode());
		response.end();
	}

	private static UUID getContextId(RoutingContext routingContext) {
		String idEntity = routingContext.request().getParam("id");
		return UUID.fromString(idEntity);
	}
}
