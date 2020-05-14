package com.flagpicker.search;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flagpicker.api.Continent;
import com.flagpicker.api.FlagSearchService;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@WebMvcTest
public class FlagSearchRestControllerUnitTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ApplicationContext appContext;

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    // Overrides real bean in test or main
    @MockBean
    private FlagSearchService mockSearchSvc;

    private List<Continent> continents;


    @Test
    public void testGetAllContinents() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final String classPathAdjustedDataFileName = "classpath:continents.json";

        File jsonFile = appContext.getResource(classPathAdjustedDataFileName).getFile();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        continents = mapper.readValue(jsonFile, new TypeReference<List<Continent>>() {
        });

        Mockito.when(mockSearchSvc.getContinents()).thenReturn(continents);

        mvc.perform(get("/continent")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(continents)));
    }
    
    @Test
    public void testGet2Continents() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final String classPathAdjustedDataFileName = "classpath:2Continents.json";

        File jsonFile = appContext.getResource(classPathAdjustedDataFileName).getFile();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        continents = mapper.readValue(jsonFile, new TypeReference<List<Continent>>() {
        });

        Mockito.when(mockSearchSvc.getContinents()).thenReturn(continents);

        mvc.perform(get("/continent")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(continents)));
    }

    @Test
    public void testGetContinentBySearch() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final String classPathAdjustedDataFileName = "classpath:2Continents.json";
        final String classPathAdjustedAmerCont = "classpath:AmerContinent.json";

        List<Continent> amerContinent;
        final List<Continent> emptyList = new ArrayList<>();


        File jsonFile = appContext.getResource(classPathAdjustedDataFileName).getFile();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        continents = mapper.readValue(jsonFile, new TypeReference<List<Continent>>() {
        });

        File amerJsonFile = appContext.getResource(classPathAdjustedAmerCont).getFile();
        amerContinent = mapper.readValue(amerJsonFile, new TypeReference<List<Continent>>() {
        });

        // Check Amer - should return 1 element
        Mockito.when(mockSearchSvc.getContinents("Amer")).thenReturn(amerContinent);

        mvc.perform(get("/continent?search=Amer")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(amerContinent)));

        // Check Africa - should return 0 element
        Mockito.when(mockSearchSvc.getContinents("Africa")).thenReturn(emptyList);

        mvc.perform(get("/continent?search=Africa")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(emptyList)));

    }
}