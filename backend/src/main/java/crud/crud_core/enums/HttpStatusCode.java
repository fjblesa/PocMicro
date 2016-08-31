package crud.crud_core.enums;

public enum HttpStatusCode {
	ERROR_NOT_FOUND(404),
	ERROR_BAD_REQUEST(400),
	OK(200),
	OK_CREATE (201),
	OK_NO_CONTENT(204);
	private int statusCode;
	
	HttpStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
