package crud.crud_core.utils.validations.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import crud.crud_core.actions.Action;
import crud.crud_core.actions.ReadOne;
import crud.crud_core.entities.Entity;
import crud.crud_core.exceptions.BusinessException;
import crud.crud_core.factories.Factory;
import crud.crud_core.issues.DistributedIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.repositories.Repository;

public class IsDistributedValidator {

	Repository repository;
	Factory factory;
	Action action;
	
	public List<Issue> validate(Entity entity){
		List<Issue> issues = new ArrayList<Issue>();
		boolean isDistributed = false;
		Field[] fields = entity.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			if(field.getName().equals("distributed")){
				action.setParams(entity.getId());
				action.execute();
				Entity entityDistributed = (Entity) action.getResult();

				try {
					field.setAccessible(true);
					isDistributed = field.getBoolean(entityDistributed);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new BusinessException(null);
				}
			}
		}
		if(isDistributed){
			issues.add(new DistributedIssue());
		}
		return issues;
	}
	
	public void setParams(Factory factory, Repository repository){
		this.factory = factory;
		this.repository = repository;
		action = new ReadOne<Entity>(factory, repository);
	}
}
