package com.flagpicker.search;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.flagpicker.api.Continent;
import com.flagpicker.api.Country;
import com.flagpicker.api.FlagSearchService;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class FlagSearchServiceUnitTests {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FlagSearchService flagSearchService;

    // See  https://github.com/Pragmatists/junitparams-spring-integration-example/blob/master/README.md
    // for an explanation on running this with JunitParams under spring

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();


    // 0 Continents file
    @Test
    public void testSearchSvcOContinents() throws Exception {
        final String continentFileName = "classpath:0Continent.json";

        final File continentFile = applicationContext.getResource(continentFileName).getFile();
        flagSearchService.init(continentFile);

        List<Continent> allContinents = flagSearchService.getContinents();
        List<Continent> continentsWithSearchStr = flagSearchService.getContinents("Asia");
        List<Country> countriesWithContinentName = flagSearchService.getCountries("America");
        List<String> flags = flagSearchService.getFlags("Oceania", "Australia");

		assertEquals(String.format("allContinents list size: %d", allContinents.size()), allContinents.size(), 0);

		assertEquals(String.format("getContinents(\"Asia\") list size: %d", continentsWithSearchStr.size()),
				continentsWithSearchStr.size(), 0);

		assertEquals(String.format("getCountries(\"America\") list size: %d", countriesWithContinentName.size()),
				countriesWithContinentName.size(), 0);
		
		assertEquals(String.format("getFlags(\"Oceania\", \"Australia\") list size: %d", flags.size()), flags.size(),
				0);
    }

    // 2 Continents file
    @Test
    public void testSearchSvc2Continents() throws Exception {
        final String continentFileName = "classpath:2Continents.json";

        final File continentFile = applicationContext.getResource(continentFileName).getFile();
        flagSearchService.init(continentFile);

        List<Continent> allContinents = flagSearchService.getContinents();
        List<Continent> continentsWithSearchFullStr = flagSearchService.getContinents("America"); // should find one match
        List<Continent> continentsWithSearchStr = flagSearchService.getContinents("ca"); // should find two match
        List<Continent> continentsWithSearch = flagSearchService.getContinents("Oceania"); // should find two match
        
        List<Country> countriesWithContinentName = flagSearchService.getCountries("America");
        List<Country> countriesWithInvalidContinent = flagSearchService.getCountries("India");
        
        List<String> flags = flagSearchService.getFlags("America", "USA");
        List<String> flagsMultiple = flagSearchService.getFlags("America", "USA,Mexico,Colombia");
        List<String> flagsInvalid = flagSearchService.getFlags("Oceania", "Australia");

		// All Continents
		assertEquals(String.format("getContinents list size: %d", allContinents.size()), 2, allContinents.size());

		// Search by continent - positives
		assertEquals(
				String.format("searchSvc.getContinents(\"America\") list size: %d", continentsWithSearchFullStr.size()),
				continentsWithSearchFullStr.size(), 1);

		assertEquals(String.format("getContinents(\"ca\") list size: %d", continentsWithSearchStr.size()),
				continentsWithSearchStr.size(), 2);

		// Search by continent - should not find any matches
		assertEquals(String.format("getContinents(\"Oceania\") list size: %d", continentsWithSearch.size()),
				continentsWithSearch.size(), 0);

		// Search by continent - positives
		assertEquals(String.format("getCountries(\"America\") list size: %d", countriesWithContinentName.size()),
				countriesWithContinentName.size(), 5);
		
		// Search by continent - should not find any matches
		assertEquals(String.format("getCountries(\"India\") list size: %d", countriesWithInvalidContinent.size()),
				countriesWithInvalidContinent.size(), 0);
		
		// Search flags by continent and country - positives
		assertEquals(String.format("getFlags(\"America\", \"USA\") list size: %d", flags.size()), flags.size(), 1);
		assertEquals(String.format("getFlags(\"America\", \"USA\") list size: %d", flagsMultiple.size()), 3, flagsMultiple.size());

		// Search flags by continent and country - should not find any matches
		assertEquals(String.format("getFlags(\"Oceania\", \"Australia\") list size: %d", flagsInvalid.size()),
				flagsInvalid.size(),		0);
    }
}