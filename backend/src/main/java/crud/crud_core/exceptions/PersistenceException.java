package crud.crud_core.exceptions;

import java.util.List;

import crud.crud_core.issues.Issue;

public class PersistenceException extends BusinessException {
	
	private static final long serialVersionUID = 1L;

	public PersistenceException(List<Issue> issues) {
		super(issues);
	}
}
