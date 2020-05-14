Welcome to the Flag Picker Application.

### Flag Picker Service Instructions:

1. For the Flag Search Service, the API that's supported is at
   com.flagpicker.api.FlagSearchService.
   
2. For the Flag Search Controller, please see the comments in
   com.flagpicker.controller.FlagSearchRestController
   to understand the end-point variants.
   
3. Note that the tests call Flag Search service's init method with other 
   files (beyond the main one in src/main/resources/continents.json). 
   File handling is implemented in a flexible way to increase chances 
   for success with the tests.
   
4. In addition to the tests, you can also start up a server by running
   "mvn clean install" or by running
   com.flagpicker.SpringRestRunApplication in your IDE as an application. 
   Please note that this starts up Flag Search server at localhost:8080 
   by default (so you can test your code in the running server with localhost:8080/continent
   for example)

### Components:

1. API and Entity Objects(Continent and Country) are available at com.flagpicker.api
2. Spring Configuration is available at com.flagpicker.config
3. Service layer implementation is available at com.flagpicker.service
4. Util classes are in package com.flagpicker.util
5. Controller classes are in com.flagpicker.controller.
6. Test classes are in com.flagpicker.search

### Endpoints:

1. /continent?search=America - Get all continents or filter based on search string
2. /country/{continentName} - Get all countries in continent specified by continent name
3. /flag?continent=America&countriesSelected=USA - Get flags that matche the specified continent and countries selected string (comma separated countries)
    
### Swagger Enabled:

1. Use http://localhost:8080/swagger-ui.html#/ once the application is up and running to view the endpoints and to play around.

### Unit Tests:

1. Unit Tests at service and controller level are in src/test/java package.
2. Additional resources designed to test different scenarios are available at src/test/resources. 
3. Integration Tests are available at com.flagpicker.search.FlagSearchRestControllerIntegrationTests.

### Actuator:

1. Actuator is enabled to check whether the application is up using http://localhost:8080/actuator/health.
2. Metrics related information can be pulled using http://localhost:8080/actuator/metrics/http.server.requests.