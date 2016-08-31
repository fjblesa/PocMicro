package crud.crud_core.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
	PENDING(0), ACTIVE(1), CANCELLED(2), FINISHED(3);

	private int id;

	Status(int id) {
		this.id = id;
	}

	@JsonValue
	public int getId() {
		return id;
	}
}
