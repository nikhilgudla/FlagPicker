package com.flagpicker.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flagpicker.api.Continent;
import com.flagpicker.api.FlagSearchService;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlagSearchRestControllerIntegrationTests {

     Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private FlagSearchService searchSvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    ApplicationContext appContext;


    private final String continentsJsonFileName = "classpath:continents.json";
    private File continentFile;
    private List<Continent> continentsFromContinentsFile;
    private ObjectMapper mapper = new ObjectMapper();
    private List<Continent> continentsFromRestEndpoint;

    // See  https://github.com/Pragmatists/junitparams-spring-integration-example/blob/master/README.md
    // for an explanation on running this with JunitParams under spring

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();


    @Before
    public void init() {

        try {
        	continentFile = appContext.getResource(continentsJsonFileName).getFile();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            continentsFromContinentsFile = mapper.readValue(continentFile, new TypeReference<List<Continent>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Exception in init", false);
        }
    }

    @Test
    public void testGetAllContinents() throws Exception {

        searchSvc.init(continentFile);

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/continent", String.class);

        assertEquals(String.format("URI: %s, Status Code: %s", testRestTemplate.getRootUri(), responseEntity.getStatusCode()), HttpStatus.OK,
                responseEntity.getStatusCode());

        continentsFromRestEndpoint = mapper.readValue(responseEntity.getBody(), new TypeReference<List<Continent>>() {
        });



        assertTrue("Continents not equal", areListsIdentical(continentsFromContinentsFile, continentsFromRestEndpoint));
    }

	/**
	 * Method to check differences between continents from file and endpoint
	 * 
	 * @param expectedContinents
	 * @param actualContinents
	 * @return boolean
	 */
    private boolean areListsIdentical(List<Continent> expectedContinents, List<Continent> actualContinents) {

        boolean isIdentical = true;

        if (expectedContinents.size() != actualContinents.size()) {
            logger.info(String.format("Expected size: %d; Actual size: %d", expectedContinents.size(), actualContinents.size()));
            return false;
        }

        for (int i=0; i<expectedContinents.size(); i++) {
            if (!expectedContinents.get(i).getContinent().matches(actualContinents.get(i).getContinent()) ||
                expectedContinents.get(i).getCountries().size() != actualContinents.get(i).getCountries().size()) {
                logger.info(String.format("Continents differ at index: %d Countries: %s", i, expectedContinents.get(i).getCountries().size()));
                return false;
            }
        }
        return  isIdentical;
    }
}