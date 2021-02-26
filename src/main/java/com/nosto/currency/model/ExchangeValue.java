package com.nosto.currency.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ExchangeValue implements Serializable {
	private static final long serialVersionUID = 8202409141762482495L;
	
	@NotNull(message="from is required")
	@Size(max = 3, message = "from must be 3 letters")
	private String from;
	@NotNull(message="to is required")
	@Size(max = 3, message = "to must be 3 letters")
	private String to;
	@NotNull(message="Amount is required") 
	@Min(value = 1,message = "amount must be greater than 0")
	private double amount;
	private double rate;
	private String value;
	private LocalDate date;
	public ExchangeValue() {}
	
	public ExchangeValue(String from, String to, String value) {
		this.from = from;	
		this.to = to;
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
