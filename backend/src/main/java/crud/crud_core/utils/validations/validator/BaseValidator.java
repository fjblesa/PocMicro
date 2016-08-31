package crud.crud_core.utils.validations.validator;

import java.util.List;

import crud.crud_core.issues.Issue;

public interface BaseValidator {

	public List<Issue> validate(Object entity);
}
