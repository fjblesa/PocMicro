package crud.crud_core.utils.validations.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import crud.crud_core.issues.Issue;
import crud.crud_core.issues.MandatoryFieldMissingIssue;

public class EntityValidator implements BaseValidator {


	@Override
	public List<Issue> validate(Object entity) {
		List<Issue> issues = new ArrayList<Issue>();
		
		ValidatorFactory validatorFactory = Validation
				.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator
				.validate(entity);

		for (ConstraintViolation<Object> violation : violations) {
			Issue issue = new MandatoryFieldMissingIssue(violation.getPropertyPath().toString());
			issues.add(issue);
		}
		return issues;
	}
}
