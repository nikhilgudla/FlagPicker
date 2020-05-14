package com.flagpicker;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.flagpicker.api.FlagSearchService;

/**
 * Listener class to load the continents json for the endpoints to use.
 * 
 * @author nikhil
 *
 */
@Component
@PropertySource("classpath:search.properties")
public class InitSearchListener {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Value("${flagpicker.continents.resource.filename}")
	private String continentResourceName;

	@Autowired
	FlagSearchService flagSearchSvc;

	@Autowired
	ApplicationContext applicationContext;

	// Set order higher so this gets executed AFTER InitPriceAdjustListener in full
	// configuration
	@EventListener(ApplicationReadyEvent.class)
	@Order(10)
	public void initServices() throws IOException {

		File flagSearchInitFile;
		logger.info(">>>  Initializing Search Service...");
		flagSearchInitFile = applicationContext.getResource("classpath:" + continentResourceName).getFile();
		flagSearchSvc.init(flagSearchInitFile);
	}
}