package com.nosto.currency.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nosto.currency.exception.UnSupportedCurrencyException;
import com.nosto.currency.model.ExchangeValue;
import com.nosto.currency.model.RateData;

@Service
public class CurrencyConvertorService {

	public static final Map<String, RateData>  CURRENCY_RATES_CACHE=new HashMap<String, RateData>();
	
	

	@Value("${exchange.rate.api.url}")
	private String exchangeRatesApi;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ExchangeValue convertCurrency(BigDecimal amount,String from,String to) {
		from=from.toUpperCase();
		to=to.toUpperCase();
		BigDecimal calculedValue=new BigDecimal(0);
		ExchangeValue exchangeVal=new ExchangeValue(from,to,calculedValue.toString());
		try {
			RateData rateData=getCurrenyRate(from);
			if(rateData!=null) {
				if(rateData.getRates().get(to)!=null) {
					calculedValue=amount.multiply(new BigDecimal(rateData.getRates().get(to)));
					exchangeVal.setValue(formatNumber(calculedValue));
					exchangeVal.setAmount(amount.doubleValue());
					exchangeVal.setRate(rateData.getRates().get(to));
					exchangeVal.setDate(rateData.getDate());
				}
			}
		} catch (Exception e ) {
			if(e instanceof HttpClientErrorException) {
				String msg=parseErrorMessge(e.getMessage());
				if(!msg.isEmpty()) {
					throw new UnSupportedCurrencyException(msg);
				}
			}else {
				throw e;
			}
		}
		return exchangeVal;				
	}
	
	private String parseErrorMessge(String rawMsg) {
		    ObjectMapper mapper = new ObjectMapper();
			String msgErrors=rawMsg.substring(rawMsg.indexOf(":")+1);
			String msg="";
			ArrayNode errorsList;
			try {
				errorsList = (ArrayNode)mapper.readTree(msgErrors);
				if(errorsList.size()>0) {
					JsonNode node=errorsList.get(0);
					msg=node.get("error").toString().replaceAll("\"", "");
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return msg;
	}
	
	public String formatNumber(BigDecimal calculedValue){
		DecimalFormat df = new DecimalFormat("#,###.0000");
		return df.format(calculedValue);
	}
	
	public RateData  getCurrenyRate(String base) {
		LocalDate today=LocalDate.now();
		ResponseEntity<RateData> response;
			if(CURRENCY_RATES_CACHE.get(base.toUpperCase())==null || today.isAfter(CURRENCY_RATES_CACHE.get(base.toUpperCase()).getDate())) {
				response=restTemplate.getForEntity(exchangeRatesApi+"latest?base="+base, RateData.class);
				CURRENCY_RATES_CACHE.put(base.toUpperCase(), response.getBody());
			}else {
				response= new ResponseEntity<RateData>(CURRENCY_RATES_CACHE.get(base.toUpperCase()),HttpStatus.OK);
			}
		return (response.getStatusCode()==HttpStatus.OK) ?response.getBody():null;
	}
}
