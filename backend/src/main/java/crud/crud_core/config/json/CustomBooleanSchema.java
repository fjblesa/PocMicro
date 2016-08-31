package crud.crud_core.config.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.module.jsonSchema.types.BooleanSchema;

public class CustomBooleanSchema extends BooleanSchema {
	
	@JsonProperty
	private String views;

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}
}
