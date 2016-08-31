package crud.crud_core.config.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;

public class CustomStringSchema extends StringSchema {

	@JsonProperty
	private String views;

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

}
