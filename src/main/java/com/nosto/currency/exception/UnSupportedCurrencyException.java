package com.nosto.currency.exception;

public class UnSupportedCurrencyException extends RuntimeException{
	private static final long serialVersionUID = 2243250903285212308L;

	public UnSupportedCurrencyException(String key) {
        super(key);
    }
}
