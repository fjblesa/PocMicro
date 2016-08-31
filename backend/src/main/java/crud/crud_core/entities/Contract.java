package crud.crud_core.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import crud.crud_core.enums.Status;
import crud.crud_core.exceptions.StatusException;
import crud.crud_core.issues.InvalidFieldValueIssue;
import crud.crud_core.issues.Issue;
import crud.crud_core.utils.validations.notblank.NotBlank;

public class Contract extends Entity {
	
	private boolean disconectionAllowed;
	private String externalReference;
	
	@NotBlank
	@NotNull
	private Date activationDate;
	
	@NotBlank
	@NotNull
	@Valid
	private PhoneNumber mandatoryPhoneNumber;
	
	@NotBlank
	@NotNull
	private Status status;

	@Override
	public List<Issue> validate() {
		issues = new ArrayList<Issue>();
		if (this.id == null && this.issues.isEmpty()) {
			//TODO ver como sacar las comprobaciones de negocio
			checkInsertStatus();
		}
		return issues;
	}

	private void checkInsertStatus() {
		if (this.status.equals(Status.FINISHED) || this.status.equals(Status.CANCELLED)) {
			Issue issue = new InvalidFieldValueIssue("estado");
			issues.add(issue);
			throw new StatusException(issues);
		}
	}

	@Override
	public void copy(Entity entity) {
		Contract contract = (Contract)entity;
		this.id = contract.getId();
		this.disconectionAllowed=contract.isDisconectionAllowed();
		this.externalReference=contract.getExternalReference();
		this.activationDate=contract.getActivationDate();
		this.mandatoryPhoneNumber=contract.getMandatoryPhoneNumber();
		this.status=contract.getStatus();
	}

	@Override
	public Contract clone() {
		Contract clone = new Contract();
		clone.setId(id);
		clone.setActivationDate(this.activationDate);
		clone.setDisconectionAllowed(disconectionAllowed);
		clone.setExternalReference(externalReference);
		clone.setMandatoryPhoneNumber(mandatoryPhoneNumber);
		clone.setStatus(status);
		return clone;
	}

	public boolean isDisconectionAllowed() {
		return disconectionAllowed;
	}

	public void setDisconectionAllowed(boolean disconectionAllowed) {
		this.disconectionAllowed = disconectionAllowed;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Status getStatus() {
		return status;
	}

	public PhoneNumber getMandatoryPhoneNumber() {
		return mandatoryPhoneNumber;
	}

	public void setMandatoryPhoneNumber(PhoneNumber mandatoryPhoneNumber) {
		this.mandatoryPhoneNumber = mandatoryPhoneNumber;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
