package com.nosto.currency.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class RateData implements Serializable {
	private static final long serialVersionUID = -4024953524264565522L;
	private Map<String, Double> rates;
	private String base;
	private LocalDate date;
	
	public Map<String, Double> getRates() {
		return rates;
	}
	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
