package crud.crud_core.issues;

public class ForeignKeyIssue extends ErrorIssue {

	public ForeignKeyIssue() {
		super();
		this.addVars(null);
		this.setMsg("Acción no disponible el registro se encuentra en uso.");
	}
}
