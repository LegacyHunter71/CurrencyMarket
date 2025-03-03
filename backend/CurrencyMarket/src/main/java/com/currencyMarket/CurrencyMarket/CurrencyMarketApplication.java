package com.currencyMarket.CurrencyMarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = {"com.currencyMarket.CurrencyMarket", "com.currencyMarket.CurrencyMarket.utils"})
public class CurrencyMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyMarketApplication.class, args);
	}

}
