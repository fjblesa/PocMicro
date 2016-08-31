package crud.crud_core.entities;

import java.util.List;

import crud.crud_core.issues.Issue;

public class ContractState extends Entity{
	private Long statusId;
	private String statusDescription;
	private Entity reason;
	
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public Entity getReason() {
		return reason;
	}
	public void setReason(Entity reason) {
		this.reason = reason;
	}
	@Override
	public List<Issue> validate() {
		return null;
	}
	@Override
	public void copy(Entity entity) {
		
	}
	@Override
	public Entity clone() {
		return null;
	}
}
