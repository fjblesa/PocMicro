package crud.crud_core.entities;

import com.fasterxml.jackson.annotation.JsonView;

import crud.crud_core.views.Views;

public class ResponseObject {
	
	@JsonView({ Views.Detail.class, Views.Summary.class })
	private Object data;
	
	@JsonView({ Views.Detail.class, Views.Summary.class })
	private Object issues;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Object getIssues() {
		return issues;
	}
	public void setIssues(Object issues) {
		this.issues = issues;
	}
}
