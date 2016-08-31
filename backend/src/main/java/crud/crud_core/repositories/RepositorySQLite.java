package crud.crud_core.repositories;

import io.vertx.core.json.JsonArray;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.querydsl.sql.SQLQuery;

import crud.crud_core.config.json.ParseObject;
import crud.crud_core.datasource.UtilsSQLite;
import crud.crud_core.entities.Entity;
import crud.crud_core.entities.QWaterUtility;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.issues.DuplicatedKeyIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.issues.NotExistsEntityIssue;

public class RepositorySQLite<T extends Entity> implements Repository<T> {

	@Override
	public UUID store(Entity entity) {
		UUID result = null;
		if (entity.getId() != null) {
			updateEntity(entity);
		} else {
			result = insertEntity(entity);
		}
		return result;
	}

	@Override
	public T read(UUID id, Class entity) {
		return readOneEntity(id, entity);
	}

	@Override
	public List readAll(Class entity) {
		return readAllExecuteQuery(entity);
	}

	@Override
	public void delete(UUID id, Class entity) {
		Entity entityDB = readOneEntity(id, entity);
		if (entityDB.getId() == null) {
			NotExistsEntityIssue issue = new NotExistsEntityIssue(id.toString());
			List issues = new ArrayList<Issue>();
			issues.add(issue);
			throw new PersistenceException(issues);
		}
		deleteEntity(id,entity);
	}
	
	public List readListWaterUtilityFilterActive(){
		
		QWaterUtility waterUtility = QWaterUtility.waterUtility;
		SQLQuery<QWaterUtility> sql =new SQLQuery<QWaterUtility>();
		sql.select(waterUtility.id,waterUtility.code,waterUtility.name,waterUtility.identifier,waterUtility.active);
		sql.from(waterUtility);
		sql.where(waterUtility.active.eq(true));
		
		JsonArray resultDB = UtilsSQLite.executeQuery(sql.toString(),true);
		
		return ParseObject.getInstance().convertToListEntity(resultDB, WaterUtility.class);
		
	}

	private void deleteEntity(UUID id,Class entity) {
		String sql = sqlDeleteEntity(id,entity);
		UtilsSQLite.executeUpdateOrInsertOrDelete(sql);
	}

	private UUID insertEntity(Entity entity) {
		entity.setId(UUID.randomUUID());
		String sql = sqlInsert(entity);
		Integer numRowsAfected = UtilsSQLite.executeUpdateOrInsertOrDelete(sql);

		if (numRowsAfected > 0) {// si ha insertado ok devolvemos el id
			return entity.getId();
		}
		return null;
	}

	private void updateEntity(Entity entity) {
		Entity entityDB = readOneEntity(entity.getId(), entity.getClass());
		if (entityDB.getId() != null) {
			String sql = sqlUpdate(entity);
			UtilsSQLite.executeUpdateOrInsertOrDelete(sql);
		} else {
			NotExistsEntityIssue issue = new NotExistsEntityIssue(entity
					.getId().toString());
			List issues = new ArrayList<Issue>();
			issues.add(issue);
			throw new PersistenceException(issues);
		}
	}

	private boolean codeExistsInDatabase(String code) {
		JsonArray resultQuery = UtilsSQLite
				.executeQuery(sqlCodeDuplicate(code));
		if (!resultQuery.isEmpty()) {
			List<Issue> issues = new ArrayList<Issue>();
			issues.add(new DuplicatedKeyIssue("code"));
			throw new PersistenceException(issues);
		}
		return true;
	}

	private T readOneEntity(UUID id, Class entity) {

		JsonArray resultQuery = UtilsSQLite.executeQuery(sqlRead(id, entity));

		if (resultQuery.isEmpty()) {
			List<Issue> issues = new ArrayList<>();
			NotExistsEntityIssue issue = new NotExistsEntityIssue("Id " + id);
			issues.add(issue);
			throw new PersistenceException(issues);
		}
		ParseObject parseObject = ParseObject.getInstance();
		Object entityDB = parseObject.convertToEntity(resultQuery.getValue(0)
				.toString(), entity);
		return (T) entityDB;
	}

	private List<Object> readAllExecuteQuery(Class entity) {
		JsonArray resultQuery = UtilsSQLite.executeQuery(sqlRead(null, entity));
		ParseObject parseObject = ParseObject.getInstance();
		List<Object> resultList = parseObject.convertToListEntity(resultQuery,entity);
		return resultList;
	}

	private String sqlRead(UUID id, Class entity) {
		String sql = "SELECT * FROM " + entity.getSimpleName();
		if (id != null) {
			sql = sql + " WHERE ID= '" + id + "'";
		}
		return sql;
	}

	private String sqlCodeDuplicate(String code) {
		String sql = "SELECT * FROM MOVEOUTREASON WHERE CODE='" + code + "'";
		return sql;
	}

	private String sqlInsert(Entity entityToInsert) {
		Field[] fieldsEntity = entityToInsert.getClass().getDeclaredFields();
		String sql = "INSERT INTO "+entityToInsert.getClass().getSimpleName()+"(";
		
		for (Field field : fieldsEntity) {
			sql += field.getName()+",";
		}
		sql+="ID) VALUES ('";
		for (Field field : fieldsEntity) {
			field.setAccessible(true);
			try {
				sql += field.get(entityToInsert) + "','";
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sql+=entityToInsert.getId()+"')";
		return sql;
	}

	private String sqlUpdate(Entity entityToInsert) {
		Field[] fieldsEntity = entityToInsert.getClass().getDeclaredFields();
		String sql = "UPDATE "+entityToInsert.getClass().getSimpleName()+" SET ";
		for (Field field : fieldsEntity) {
			field.setAccessible(true);
			sql += field.getName();
			sql += "='";
			try {
				sql += field.get(entityToInsert) + "',";
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sql=sql.substring(0, sql.length()-1);
		sql += " WHERE ID='"+entityToInsert.getId()+"'";
		return sql;
	}

	private String sqlDeleteEntity(UUID id,Class entity) {
		return "DELETE FROM "+entity.getSimpleName()+" WHERE ID = '" + id + "'";
	}
}
