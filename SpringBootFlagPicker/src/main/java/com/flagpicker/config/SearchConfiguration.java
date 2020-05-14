package com.flagpicker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.flagpicker.api.FlagSearchService;
import com.flagpicker.service.FlagSearchServiceImpl;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan("com.flagpicker")
@Profile("default")
public class SearchConfiguration {
    
    @Bean
    public FlagSearchService flagSearch() {
        return new FlagSearchServiceImpl();
    }
    
    @Bean
    public Docket productApi() {
       return new Docket(DocumentationType.SWAGGER_2).select()
          .apis(RequestHandlerSelectors.basePackage("com.flagpicker")).build();
    }
}