
package com.mxi.am;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;


@RunWith( Cucumber.class )
@CucumberOptions( plugin = { "pretty", "html:build/reports/cucumber" }, monochrome = true,
      tags = { "@RunAMEngineering", "~@Ignore" }, snippets = SnippetType.CAMELCASE )
public class RunAMEngineeringCucumberTests {
   // The test runner uses the annotations to drive this class; it must have an empty body.
}
