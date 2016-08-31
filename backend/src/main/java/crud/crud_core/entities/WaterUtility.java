package crud.crud_core.entities;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;

import crud.crud_core.issues.Issue;
import crud.crud_core.utils.validations.notblank.NotBlank;
import crud.crud_core.views.Views;

@PersistenceCapable
public class WaterUtility extends Entity {

	@NotBlank
	@NotNull
	@JsonView({ Views.Detail.class, Views.Summary.class })
	@JsonProperty(required = true)
	@Size(max = 10)
	private String code;

	@NotBlank
	@NotNull
	@Size(max = 100)
	@JsonView({ Views.Detail.class, Views.Summary.class, Views.List.class })
	@JsonProperty(required = true)
	private String name;

	@NotBlank
	@NotNull
	@JsonView({ Views.Detail.class, Views.Summary.class })
	@JsonProperty(required = true)
	private String identifier;

	@NotNull
	@NotBlank
	@JsonView({ Views.Detail.class, Views.Summary.class })
	@JsonProperty(required = true, defaultValue = "true")
	private boolean active;

	@Override
	public List<Issue> validate() {
		issues = new ArrayList<Issue>();
		return issues;
	}

	@Override
	public void copy(Entity entity) {
		WaterUtility waterUtilities = (WaterUtility) entity;
		this.id = waterUtilities.getId();
		this.code = waterUtilities.getCode();
		this.name = waterUtilities.getName();
		this.identifier = waterUtilities.getIdentifier();
	}

	@Override
	public WaterUtility clone() {
		WaterUtility clone = new WaterUtility();
		clone.setId(this.id);
		clone.setCode(this.code);
		clone.setName(this.name);
		clone.setIdentifier(this.identifier);
		clone.setActive(this.active);
		return clone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
