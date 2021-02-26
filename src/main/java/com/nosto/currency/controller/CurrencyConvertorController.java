package com.nosto.currency.controller;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nosto.currency.exception.NotFoundException;
import com.nosto.currency.model.ExchangeValue;
import com.nosto.currency.service.CurrencyConvertorService;

@RestController
@RequestMapping("/api/")
@Validated
public class CurrencyConvertorController {
	
	@Autowired
	private CurrencyConvertorService currencyConvertorService;
	
	@GetMapping("/convert/{amount}/{from}/{to}")
	public ResponseEntity<ExchangeValue>  convertCurrency(
			@PathVariable("amount") @NotNull(message="Amount is required") @Min(value =1,message = "Amount must be greater than 0" )BigDecimal amount,
			@PathVariable("from") @NotNull(message="From is required") @Size(max = 3, message = "from must be 3 letters") String from,
			@PathVariable("to") @NotNull(message="To is required") @Size(max = 3,message = "to must be 3 letters")String to) throws NotFoundException, JsonMappingException, JsonProcessingException {
		ExchangeValue exchangeValue=currencyConvertorService.convertCurrency(amount, from.toUpperCase(), to.toUpperCase());
		return new ResponseEntity<ExchangeValue>(exchangeValue,HttpStatus.OK);				
	}
	
}
