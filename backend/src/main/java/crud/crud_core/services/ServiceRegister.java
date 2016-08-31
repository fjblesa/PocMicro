package crud.crud_core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crud.crud_core.entities.CommercialSite;
import crud.crud_core.entities.Contract;
import crud.crud_core.entities.MoveOutReason;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.exceptions.NotFoundServiceException;

public class ServiceRegister {

	private static Map<String, Class<?>> entityRegisterMapper = new HashMap<String, Class<?>>();
	private static List<String> listRegisterEntities = new ArrayList<String>();

	public static void registerService() {
		registerService("move_out_reasons", MoveOutReason.class);
		registerService("contracts", Contract.class);
		registerService("water_utilities", WaterUtility.class);
		registerService("commercial_sites", CommercialSite.class);
	}

	public static Class<?> getEntityClass(String entityEndpoint) {
		if (entityRegisterMapper.containsKey(entityEndpoint)) {
			return entityRegisterMapper.get(entityEndpoint);
		}
		throw new NotFoundServiceException();
	}

	public static void registerService(String entityName, Class<?> entityClass) {
		entityRegisterMapper.put(entityName, entityClass);
		listRegisterEntities.add(entityName);
	}

	public static List<String> getEntityRegisterList() {
		return listRegisterEntities;
	}

	public static String getEndPointRegister(Class<?> entityRegistred) {
		for (String key : entityRegisterMapper.keySet()) {
			if (entityRegisterMapper.get(key).equals(entityRegistred)) {
				return key;
			}
		}
		return null;
	}
}
