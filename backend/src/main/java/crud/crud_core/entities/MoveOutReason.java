package crud.crud_core.entities;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import crud.crud_core.issues.Issue;
import crud.crud_core.utils.validations.notblank.NotBlank;
import crud.crud_core.utils.validations.validator.IsDistributedValidator;
import crud.crud_core.views.Views;

@PersistenceCapable
public class MoveOutReason extends Entity {

	@NotNull
	@NotBlank
	@Size(max = 10)
	@JsonView({ Views.Detail.class, Views.Summary.class })
	@JsonProperty(required = true)
	private String code;

	@NotNull
	@NotBlank
	@Size(max = 50)
	@JsonView({ Views.Detail.class, Views.Summary.class })
	@JsonProperty(required = true)
	private String name;

	@NotNull
	@NotBlank
	@JsonView({ Views.Detail.class, Views.Summary.class })
	@JsonProperty(required = true, defaultValue = "true")
	private boolean active;

	@JsonView({ Views.Detail.class, Views.Summary.class })
	@JsonProperty(defaultValue = "false")
	private boolean distributed;

	public boolean isActive() {
		return this.active;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isDistributed() {
		return distributed;
	}

	@Override
	public MoveOutReason clone() {
		MoveOutReason reasonCopy = new MoveOutReason();
		reasonCopy.setActive(this.isActive());
		reasonCopy.setCode(this.getCode());
		reasonCopy.setName(this.getName());
		reasonCopy.setId(this.getId());
		return reasonCopy;
	}

	@Override
	public void copy(Entity entity) {
		MoveOutReason reason = (MoveOutReason) entity;
		this.id = reason.getId();
		this.code = reason.getCode();
		this.name = reason.getName();
		this.active = reason.isActive();
	}

	@Override
	public List<Issue> validate() {
		issues = new ArrayList<Issue>();
		return issues;
	}
}
