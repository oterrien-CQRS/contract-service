package com.ote.mandate.business;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/mandatesOperation.feature",
        tags = {"~@Ignore"},
        glue = "com.ote.mandate.business")
public class MandateServiceCucumberTest {

}