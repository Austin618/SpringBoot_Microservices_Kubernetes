package com.micro.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
    @Autowired
    private CurrencyExchangeRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from,
                                                 @PathVariable String to){
//        CurrencyExchange currencyExchange = new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(50));

        // INFO [currency-exchange,db0feb616caece49183fe7683c24caff,fea4baac1b5673ce] 22516 --- [nio-8000-exec-1] c.m.m.c.CurrencyExchangeController       : receiveExchangeValue called with USD to INR
        // Trace ID: db0feb616caece49183fe7683c24caff
        // Span ID: fea4baac1b5673ce
        logger.info("receiveExchangeValue called with {} to {}", from, to);

        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);
        if (currencyExchange == null) {
            throw new RuntimeException("Unable to find data for " + from +  " to " + to);
        }
        String port = environment.getProperty("local.server.port");

//        currencyExchange.setEnvironment(port);

        // Kubernetes
        String host = environment.getProperty("HOSTNAME");
        String version = "v12";
        currencyExchange.setEnvironment(port + " " + version + " " + host);

        return currencyExchange;
    }
}
