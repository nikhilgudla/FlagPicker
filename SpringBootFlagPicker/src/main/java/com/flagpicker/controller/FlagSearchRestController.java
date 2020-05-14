package com.flagpicker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flagpicker.api.Continent;
import com.flagpicker.api.Country;
import com.flagpicker.api.FlagSearchService;

/**
 * Controller to expose the following rest endpoints.
 *      /continent?search=America                           Get all continents or filter based on search string
 *      /country/{continentName}                            Get all countries in continent specified by continent name
 *      /flag?continent=America&countriesSelected=USA       Get flags that matches the specified continent and countries selected string
 *      
 * @author nikhil
 */
@Profile("default")
@RestController
public class FlagSearchRestController {
	
	@Autowired
	private FlagSearchService searchSvc;
	
	/**
	 * Method to provide list of continents based on search string if provided, else
	 * will return all the continents list.
	 * 
	 * @param search - Search string can be partial to be used in search box.
	 * @return list of continents
	 */
	@GetMapping("/continent")
	public List<Continent> getContinents(@RequestParam(required = false) String search){
		if(search == null || search.isEmpty())
			return searchSvc.getContinents();
		else
			return searchSvc.getContinents(search);
	}
	
	/**
	 * Method to provide the list of countries based on continent
	 * 
	 * @param continentName
	 * @return list of countries
	 */
	@GetMapping("/country/{continentName}")
	public List<Country> getCountries(@PathVariable String continentName){
		return searchSvc.getCountries(continentName);
	}
	
	/**
	 * Method that returns the list of flags based on continent and countries
	 * selected.
	 * 
	 * @param continent - continent name
	 * @param countriesSelected - countries selected comma separated
	 * @return list of flags
	 */
	@GetMapping("/flag")
	public List<String> getFlags(@RequestParam String continent, @RequestParam String countriesSelected) {
		return searchSvc.getFlags(continent, countriesSelected);		
	}
}