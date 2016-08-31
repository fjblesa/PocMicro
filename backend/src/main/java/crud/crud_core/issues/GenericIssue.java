package crud.crud_core.issues;

public class GenericIssue extends ErrorIssue {

	public GenericIssue(String var) {
		super();
		this.addVars(var);
		this.setMsg("Se ha producido un error inesperado");
	}
}
