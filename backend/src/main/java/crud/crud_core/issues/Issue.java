package crud.crud_core.issues;

import java.util.ArrayList;
import java.util.List;

public class Issue {

	private String type;
	private List<String> vars;
	private String msg;	

	public Issue(String type) {
		this.vars = new ArrayList<>();
		this.type = type;
	}
	
	public List<String> getVars() {
		return vars;
	}

	public void addVars(String var) {
		this.vars.add(var);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getType() {
		return type;
	}
}
