package crud.crud_core.datasource;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.sqlite.SQLiteConfig;

import crud.crud_core.exceptions.BusinessException;
import crud.crud_core.exceptions.PersistenceException;
import crud.crud_core.issues.DuplicatedKeyIssue;
import crud.crud_core.issues.ForeignKeyIssue;
import crud.crud_core.issues.GenericIssue;
import crud.crud_core.issues.Issue;

public class UtilsSQLite {

	private static Connection connection;

	public static Integer executeUpdateOrInsertOrDelete(String sql) {
		init();
		try {
			Statement stmt = connection.createStatement();
			return stmt.executeUpdate(sql);// devuelve las filas afectadas
		} catch (SQLException e) {
			if (e.getMessage().contains("UNIQUE")) {
				String[] message = e.getMessage().split(":");
				String entityField = message[1];
				String field = entityField.split("\\.")[1];
				List<Issue> issues = new ArrayList<Issue>();
				Issue issue = new DuplicatedKeyIssue(field);
				issues.add(issue);
				throw new PersistenceException(issues);
			}else if(e.getMessage().contains("FOREIGN KEY")){
				List<Issue> issues = new ArrayList<Issue>();
				Issue issue = new ForeignKeyIssue();
				issues.add(issue);
				throw new PersistenceException(issues);
			}
			List<Issue> issues = new ArrayList<Issue>();
			Issue issue = new GenericIssue(null);
			issues.add(issue);
			throw new BusinessException(issues);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				List<Issue> issues = new ArrayList<Issue>();
				Issue issue = null;
				issues.add(issue);
				throw new PersistenceException(issues);
			}
		}
	}

	public static JsonArray executeQuery(String sql) {
		init();
		try {
			Statement stmt = connection.createStatement();
			ResultSet resultDB = stmt.executeQuery(sql);
			JsonArray jsonDB = new JsonArray();
			while (resultDB.next()) {
				JsonObject jsonElement = new JsonObject();
				for (int i = 1; i <= resultDB.getMetaData().getColumnCount(); i++) {
					jsonElement.put(resultDB.getMetaData().getColumnName(i)
							.toLowerCase(), resultDB.getObject(i));
				}
				jsonDB.add(jsonElement);
			}
			return jsonDB;
		} catch (SQLException e) {
			e.printStackTrace();
			List<Issue> issues = new ArrayList<Issue>();
			throw new PersistenceException(issues);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				List<Issue> issues = new ArrayList<Issue>();
				throw new PersistenceException(issues);
			}
		}
	}

	public static JsonArray executeQuery(String sql, Object... vars) {
		init();
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			for (int i = 0; i < vars.length; i++) {
				if (vars[i] instanceof String) {
					stmt.setString(i + 1, (String) vars[i]);
				} else if (vars[i] instanceof Integer) {
					stmt.setInt(i + 1, (int) vars[i]);
				} else if (vars[i] instanceof UUID) {
					stmt.setString(i + 1, vars[i].toString());
				} else if (vars[i] instanceof Boolean) {
					stmt.setString(i + 1, String.valueOf(vars[i]));
				} else {
					stmt.setObject(i + 1, vars[i]);
				}
			}
			ResultSet resultDB = stmt.executeQuery();
			JsonArray jsonDB = new JsonArray();
			while (resultDB.next()) {
				JsonObject jsonElement = new JsonObject();
				for (int i = 1; i <= resultDB.getMetaData().getColumnCount(); i++) {
					jsonElement.put(resultDB.getMetaData().getColumnName(i)
							.toLowerCase(), resultDB.getObject(i));
				}
				jsonDB.add(jsonElement);
			}
			return jsonDB;
		} catch (SQLException e) {
			e.printStackTrace();
			List<Issue> issues = new ArrayList<Issue>();
			throw new PersistenceException(issues);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				List<Issue> issues = new ArrayList<Issue>();
				throw new PersistenceException(issues);
			}
		}
	}

	private static void init() {
		try {
			Class.forName("org.sqlite.JDBC");
			Properties connectionProperties = new Properties();
			SQLiteConfig config = new SQLiteConfig();
			config.enforceForeignKeys(true);
			connectionProperties = config.toProperties();
			connection = DriverManager.getConnection("jdbc:sqlite:test.db",
					connectionProperties);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
}
