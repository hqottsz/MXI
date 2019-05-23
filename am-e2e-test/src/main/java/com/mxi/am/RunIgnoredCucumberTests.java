
package com.mxi.am;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;


/**
 * Runs all ignored cucumber tests
 *
 * <pre>
 * &#64;Ignore
 * Scenario: Run ignored tests
 *     Given unannotated scenarios
 *      When I add the @Ignore annotation to scenarios
 *       And I run this test
 *      Then only ignored scenarios run
 * </pre>
 */
@RunWith( Cucumber.class )
@CucumberOptions( plugin = { "pretty", "html:build/reports/cucumber" }, monochrome = true,
      tags = { "@Ignore" }, snippets = SnippetType.CAMELCASE )
public class RunIgnoredCucumberTests {
   // Annotated class will scan classpath for all features and run them
}
