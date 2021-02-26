package com.nosto.currency.exception;

public class NotFoundException extends Exception {
	private static final long serialVersionUID = -4185036715383803193L;
	private String message;
	
	public NotFoundException(String message) {
		this.message=message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
