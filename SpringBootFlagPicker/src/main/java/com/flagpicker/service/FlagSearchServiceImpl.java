package com.flagpicker.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.flagpicker.api.Continent;
import com.flagpicker.api.Country;
import com.flagpicker.api.FlagSearchService;
import com.flagpicker.util.FlagSearchUtil;

/**
 * Implementation for FlagSearchService - Methods to include implementation
 * logic for flag search.
 * 
 * @author nikhil
 */
public class FlagSearchServiceImpl implements FlagSearchService {
	List<Continent> continents = new ArrayList<Continent>();
	
    @Override
    public void init(String continentJsonFileName) {
    	try {
    		continents = FlagSearchUtil.jsonArrayToObjectList(continentJsonFileName,  Continent.class);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
    }

    @Override
    public void init(File continentJsonFile) {
    	ObjectMapper mapper = new ObjectMapper();
        try {
			CollectionType listType = mapper.getTypeFactory()
			    .constructCollectionType(ArrayList.class, Continent.class);
			List<Continent> ts = mapper.readValue(continentJsonFile, listType);
			continents = ts;
		} catch (JsonParseException jsonParseException) {
			jsonParseException.printStackTrace();
		} catch (JsonMappingException jsonMappingException) {
			jsonMappingException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
    }

	@Override
	public List<Continent> getContinents() {
		return continents;
	}

	@Override
	public List<Continent> getContinents(String continent) {
		List<Continent> filteredList = continents.stream()
				.filter(cont -> cont.getContinent().toLowerCase().contains(continent.toLowerCase()))
				.collect(Collectors.toList());
		return filteredList;
	}
	
	@Override
	public List<Country> getCountries(String continent) {
		List<Continent> filteredList = continents.stream()
				.filter(cont -> cont.getContinent().contains(continent))
				.collect(Collectors.toList());
		if(filteredList.isEmpty())
			return new ArrayList<Country>();
		else
			return filteredList.get(0).getCountries();
	}

	@Override
	public List<String> getFlags(String continent, String countriesSelected) {
		List<String> flags = new ArrayList<>();
		List<Continent> filteredList = continents.stream()
				.filter(cont -> cont.getContinent().equalsIgnoreCase(continent))
				.collect(Collectors.toList());
		if(!filteredList.isEmpty()) {
			List<Country> countries = filteredList.get(0).getCountries();
			for(Country c:countries) {
				if(countriesSelected.contains(c.getName())) {
					flags.add(c.getFlag());
				}
			}
		}
		return flags;
	}
}