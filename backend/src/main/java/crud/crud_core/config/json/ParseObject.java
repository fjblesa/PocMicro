package crud.crud_core.config.json;

import io.vertx.core.json.JsonArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

import crud.crud_core.actions.Action;
import crud.crud_core.entities.ResponseObject;
import crud.crud_core.exceptions.BusinessException;
import crud.crud_core.issues.GenericIssue;
import crud.crud_core.issues.Issue;

public class ParseObject {

	private static ParseObject uniqueInstance;
	private static ObjectMapper mapper;

	private ParseObject() {
		// private constructor
	}

	public static synchronized ParseObject getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ParseObject();
			mapper = new ObjectMapper();			
			
		}
		return uniqueInstance;
	}

	public String convertToJson(Action action) {
		return convertToJson(action, null);
	}

	public String convertToJson(Action action, Class<?> view) {

		Object entity = action.getResult();
		List<Issue> issues = action.getIssues();

		ResponseObject responseObject = new ResponseObject();
		responseObject.setData(entity);
		responseObject.setIssues(issues);

		String responseJsonValue = "";

		try {
			if (view != null) {
				responseJsonValue = mapper.writerWithView(view)
						.writeValueAsString(responseObject);
			} else {
				responseJsonValue = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(responseObject);
			}

		} catch (JsonProcessingException e) {
			Issue issue = new GenericIssue(null);
			issues.add(issue);
			throw new BusinessException(issues);
		}

		return responseJsonValue;
	}

	public String convertToJson(List<String> objectToConvert) {

		ResponseObject responseObject = new ResponseObject();
		responseObject.setData(objectToConvert);
		responseObject.setIssues(new ArrayList<Issue>());// TODO tratar issues
		String responseJsonValue = "";
		try {
			responseJsonValue = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(responseObject);
		} catch (JsonProcessingException e) {
			List<Issue> issues = new ArrayList<Issue>();
			Issue issue = new GenericIssue(null);
			issues.add(issue);
			throw new BusinessException(issues);
		}

		return responseJsonValue;
	}

	public String convertToJson(ResponseObject objectToConvert) {

		String responseJsonValue = "";
		try {
			responseJsonValue = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(objectToConvert);
		} catch (JsonProcessingException e) {
			List<Issue> issues = new ArrayList<Issue>();
			Issue issue = new GenericIssue(null);
			issues.add(issue);
			throw new BusinessException(issues);
		}
		return responseJsonValue;
	}

	public Object convertToEntity(String string, Class<?> entityClass) {

		Object pojo = null;
		try {
			pojo = mapper.readValue(string, entityClass);
		} catch (IOException e) {
			List<Issue> issues = new ArrayList<Issue>();
			Issue issue = new GenericIssue(null);
			issues.add(issue);
			throw new BusinessException(issues);
		}
		return pojo;
	}

	public String getJsonSchemaString(Class<?> entityClass) {

		String schemaString = "{}";

		JsonSchema schema = getJsonSchema(entityClass);

		try {
			schemaString = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(schema);
		} catch (JsonProcessingException e) {
			List<Issue> issues = new ArrayList<Issue>();
			Issue issue = new GenericIssue(null);
			issues.add(issue);
			throw new BusinessException(issues);
		}
		return schemaString;

	}

	public JsonSchema getJsonSchema(Class<?> entityClass) {

		CustomSchema visitor = new CustomSchema();
		try {
			mapper.acceptJsonFormatVisitor(entityClass, visitor);
		} catch (JsonMappingException e) {
			List<Issue> issues = new ArrayList<Issue>();
			Issue issue = new GenericIssue(null);
			issues.add(issue);
			throw new BusinessException(issues);
		}
		JsonSchema schema = visitor.finalSchema();
		return schema;

	}

	public List<Object> convertToListEntity(JsonArray resultQuery,
			Class<?> entityClass) {
		List<Object> resultList = new ArrayList<>();
		for (Object object : resultQuery) {
			resultList.add(convertToEntity(object.toString(), entityClass));
		}
		return resultList;
	}
}
