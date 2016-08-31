package crud.crud_core.entities;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import crud.crud_core.config.json.annotations.ReadOnly;
import crud.crud_core.issues.Issue;
import crud.crud_core.views.Views;

public abstract class Entity {

	
	@JsonProperty
	@ReadOnly
	@JsonView({ Views.Detail.class, Views.Summary.class })
	protected UUID id;

	protected List<Issue> issues;

	public abstract List<Issue> validate();

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public abstract void copy(Entity entity);

	public abstract Entity clone();
}
