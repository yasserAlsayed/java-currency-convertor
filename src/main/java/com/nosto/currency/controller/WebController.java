package com.nosto.currency.controller;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nosto.currency.exception.UnSupportedCurrencyException;
import com.nosto.currency.model.ExchangeValue;
import com.nosto.currency.model.RateData;
import com.nosto.currency.service.CurrencyConvertorService;

@Controller
@RequestMapping("/")
@Validated
public class WebController {
	
	@Autowired
	private CurrencyConvertorService currencyConvertorService;
	
	@GetMapping("list/{base}")
    public String showListForm(@PathVariable("base") @NotNull(message="base is required")
	@Size(max = 3, message = "base must be 3 letters") String base,Model model) {
		RateData data = currencyConvertorService.getCurrenyRate(base);
		model.addAttribute("rateData", data.getRates());
        return "index";
    }
	@GetMapping("/")
    public String showIndex(ExchangeValue exchangeValue) {
		//model.addAttribute("rateData", data.getRates());
        return "convert";
    }
	
	@GetMapping("view-convert")
    public String showViewConvert(ExchangeValue exchangeValue) {
        return "convert";
    }
	
    @PostMapping("doConvert")
    public String convert(@Validated ExchangeValue exchangeValue, BindingResult result, Model model) {
    	if(!result.hasFieldErrors()) {
    		try {
    			ExchangeValue data=currencyConvertorService.convertCurrency(new BigDecimal(exchangeValue.getAmount()), exchangeValue.getFrom(), exchangeValue.getTo());
    			model.addAttribute("result",data);
			} catch (UnSupportedCurrencyException e) {
				model.addAttribute("not_spported",new ExchangeValue());
			}
		}
		return "convert";
    }
	
   
}
