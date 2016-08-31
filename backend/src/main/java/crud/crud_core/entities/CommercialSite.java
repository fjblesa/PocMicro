package crud.crud_core.entities;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import crud.crud_core.config.json.annotations.IdentityClass;
import crud.crud_core.issues.Issue;
import crud.crud_core.utils.validations.notblank.NotBlank;
import crud.crud_core.views.Views;

@PersistenceCapable
public class CommercialSite extends Entity {

	@NotBlank
	@NotNull
	@JsonProperty(required = true)
	@JsonView({ Views.Detail.class, Views.Summary.class })
	private String code;
	
	@NotBlank
	@NotNull
	@JsonProperty(required = true)
	@IdentityClass(WaterUtility.class)
	@JsonView({ Views.Detail.class, Views.Detail.class })
	private String waterutilityid;

	@NotBlank
	@NotNull
	@Size(max = 255)
	@JsonView({ Views.Detail.class, Views.Summary.class })
	@JsonProperty(required = true)
	private String name;

	@NotBlank
	@NotNull
	@JsonProperty(required = true, defaultValue = "false")
	@JsonView({ Views.Detail.class, Views.Summary.class })
	private boolean active;

	@Override
	public List<Issue> validate() {
		issues = new ArrayList<Issue>();
		return issues;
	}

	@Override
	public void copy(Entity entity) {
		CommercialSite clone = (CommercialSite) entity;
		this.id = clone.getId();
		this.code = clone.getCode();
		this.name = clone.getName();
		this.waterutilityid = clone.getWaterutilityid();
	}

	@Override
	public CommercialSite clone() {
		CommercialSite clone = new CommercialSite();
		clone.setId(id);
		clone.setCode(this.code);
		clone.setName(this.name);
		clone.setWaterutilityid(this.waterutilityid);
		return clone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWaterutilityid() {
		return waterutilityid;
	}

	public void setWaterutilityid(String waterutilityid) {
		this.waterutilityid = waterutilityid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
