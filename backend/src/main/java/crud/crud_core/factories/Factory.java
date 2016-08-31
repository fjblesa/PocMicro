package crud.crud_core.factories;

import crud.crud_core.entities.Entity;
import crud.crud_core.entities.NullEntity;

public class Factory<T extends Entity>{
	
	private Class<T> type;
	
	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type){
		this.type = type;
	}

	public Entity create(){
		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Entity createNullEntity(){
		return new NullEntity();
	}
}
