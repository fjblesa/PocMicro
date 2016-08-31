package crud.crud_core.issues;

public class NotExistsEntityIssue extends ErrorIssue{
	
	public NotExistsEntityIssue(String vars) {
		super();
		this.addVars(vars);
		this.setMsg("El registro {0} no encontrado");
	}
}
