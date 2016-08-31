package crud.crud_core.issues;

public class InvalidFieldValueIssue extends ErrorIssue{
	
	public InvalidFieldValueIssue(String vars) {
		super();
		this.addVars(vars);
		this.setMsg("El campo {0} tiene un valor incorrecto.");
	}
}
