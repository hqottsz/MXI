
package com.mxi.am;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;


/**
 * Runs all cucumber tests
 *
 * <pre>
 * Scenario: Running all tests
 *     Given unannotated scenarios
 *      When I run this test
 *      Then all scenarios should run
 *
 * &#64;Ignore
 * Scenario: Ignore some tests
 *     Given unannotated scenarios
 *      When I add the @Ignore annotation to scenarios
 *       And I run this test
 *      Then all other scenarios run
 * </pre>
 */
@RunWith( Cucumber.class )
@CucumberOptions(
      plugin = { "pretty", "html:build/reports/cucumber",
            "json:build/reports/cucumber/cucumber.json" },
      monochrome = true, tags = { "~@Ignore", "~@RequiresMule", "~@E2ESmokeBuild1", "~@JspSmokeTest" },
      snippets = SnippetType.CAMELCASE )
public class RunAllCucumberTests {
   // Annotated class will scan classpath for all features and run them
}
