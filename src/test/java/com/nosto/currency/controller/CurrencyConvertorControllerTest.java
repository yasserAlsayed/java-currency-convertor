package com.nosto.currency.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nosto.currency.model.ExchangeValue;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
class CurrencyConvertorControllerTest {
 
    @Autowired
    private TestRestTemplate testRestTemplate;
 
    @Test
    public void when_call_with_valid_params() 
    {
    	ResponseEntity<ExchangeValue>  result=testRestTemplate
        .getForEntity("/api/convert/1/USD/CAD",ExchangeValue.class);
            assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    
    @Test
    public void when_call_with_invalid_amount_then_return_bad_request() 
    {
    	ResponseEntity<ExchangeValue>  result=testRestTemplate
        .getForEntity("/api/convert/-1/USD/CAD",ExchangeValue.class);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    
    @Test
    public void when_call_with_invalid_from_then_return_bad_request() 
    {
    	ResponseEntity<ExchangeValue>  result=testRestTemplate
        .getForEntity("/api/convert/12/U2SD/CAD",ExchangeValue.class);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    
    @Test
    public void when_call_with_invalid_to_then_return_bad_request() 
    {
    	ResponseEntity<ExchangeValue>  result=testRestTemplate
        .getForEntity("/api/convert/12/USD/CeeAD",ExchangeValue.class);
            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
    
}
