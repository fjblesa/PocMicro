package crud.crud_core.entities;

import javax.validation.constraints.NotNull;

import crud.crud_core.utils.validations.notblank.NotBlank;

public class PhoneNumber {

	@NotBlank
	@NotNull
	private String number;

	public PhoneNumber() {
	}
	
	public PhoneNumber(String phoneNumber) {
		this.number = phoneNumber;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
