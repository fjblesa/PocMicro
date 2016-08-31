package crud.crud_core.issues;

public class DistributedIssue extends ErrorIssue{

	public DistributedIssue(){
		super();
		this.setMsg("No se permite eliminar, registro distribuido.");
	}
}
