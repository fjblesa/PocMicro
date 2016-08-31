package crud.crud_core.exceptions;

import java.util.List;

import crud.crud_core.issues.Issue;

public class StatusException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public StatusException(List<Issue> issues) {
		super(issues);
	}
}
