package com.flagpicker.api;

import java.io.File;
import java.util.List;

/**
 * Flag Search Service Interface - Consists of abstract methods to be
 * implemented for flag search.
 * 
 * @author nikhil
 */
public interface FlagSearchService {

	/**
	 * Init method - Service reads this file to get the data.
	 * 
	 * @param continentJsonFileName - Fully qualified file name that contains the
	 *                              continents and flag information.
	 */
	public void init(String continentJsonFileName);

    /**
     * Variant that takes a file instead of a fully qualified path name.
     * 
     * @param continentJsonFile
     */
    public void init(File continentJsonFile);

    /**
     * Method to fetch all continents.
     * 
     * @return list of continents
     */
    public List<Continent> getContinents();

    /**
     * Method to get list of continents based on continent string
     * 
     * @param continent - pattern or continent name.
     * @return list of continents
     */
    public List<Continent> getContinents(String continent);


	/**
	 * Method to get countries based on continent
	 * 
	 * @param continent
	 * @return list of countries
	 */
	public List<Country> getCountries(String continent);
	
	/**
	 * Method to get flags based on continent and country selected.
	 * 
	 * @param continent
	 * @param countriesSelected
	 * @return list of flags
	 */
	public List<String> getFlags(String continent, String countriesSelected);
}