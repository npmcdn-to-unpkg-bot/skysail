package io.skysail.server;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "json:generated/cucumber.json"})
public class RunCucumberTests { // NOSONAR

}
