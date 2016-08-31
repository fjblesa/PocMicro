package crud.crud_core.exceptions;

import java.util.List;

import crud.crud_core.issues.Issue;

public class BusinessException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	List<Issue> issues;
	public BusinessException(List<Issue> issues){
		this.issues = issues;
	}
	
	public List<Issue> getIssues() {
		return this.issues;
	}
}
