package com.nosto.currency.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nosto.currency.model.ExchangeValue;
import com.nosto.currency.model.RateData;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)

class CurrencyConvertorServiceTest {
	@Autowired
	private CurrencyConvertorService service  ;

	@Autowired
	private RestTemplate restTemplate;

	
	
    @Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() throws JsonMappingException, JsonProcessingException {
    	//@given    	
    	ResponseEntity<RateData> response=restTemplate.getForEntity("https://api.exchangeratesapi.io/latest?base=EUR", RateData.class);
    	BigDecimal expected=new BigDecimal(10).multiply(BigDecimal.valueOf(response.getBody().getRates().get("USD")));
    	
    	//When
    	ExchangeValue acutualValue = service.convertCurrency(new BigDecimal(10), "Eur", "USD");
        //then
    	assertEquals(acutualValue.getValue(), service.formatNumber(expected));
    
    
    }
    
    
}
