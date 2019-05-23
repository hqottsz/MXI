
package com.mxi.am;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;


/**
 * Run feature tests in isolation to avoid running the whole test suite when working on specific
 * scenarios
 *
 * <pre>
 * Scenario: When no scenarios are annotated, do nothing
 *    Given unannotated scenarios
 *     When I execute this test
 *     Then it does not fail
 *      And it does not execute any scenarios
 * 
 * &#64;RunThis
 * Scenario: I want to run specific scenarios
 *    Given unannotated scenarios
 *     When I add the RunThis annotation to scenarios
 *      And I execute this test
 *     Then it executes only the scenarios that I've annotated
 * 
 * &#64;RunThis
 * &#64;Ignore
 * Scenario: I do not want to run ignored RunThis
 *    Given unannotated scenarios
 *     When I add the RunThis annotation to scenarios
 *      And I add the Ignore annotation to scenarios
 *      And I execute this test
 *     Then it does not execute these scenarios
 * </pre>
 */
@RunWith( Cucumber.class )
@CucumberOptions( plugin = { "pretty", "html:build/reports/cucumber" }, monochrome = true,
      tags = { "@SmokeTest", "~@RequiresMule"},
      snippets = SnippetType.CAMELCASE )
public class RunSmokeTestCucumberTests {
   // The test runner uses the annotations to drive this class; it must have an empty body.
}
