package crud.crud_core.issues;

public class MandatoryFieldMissingIssue extends ErrorIssue {

	public MandatoryFieldMissingIssue(String vars) {
		super();
		this.addVars(vars);
		this.setMsg("El campo {0} es obligatorio.");
	}
}
