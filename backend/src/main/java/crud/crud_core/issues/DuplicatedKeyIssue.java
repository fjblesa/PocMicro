package crud.crud_core.issues;

public class DuplicatedKeyIssue extends ErrorIssue {

	public DuplicatedKeyIssue(String var) {
		super();
		this.addVars(var);
		this.setMsg("El valor indicado en el campo {0} se encuentra duplicado.");
	}
}
