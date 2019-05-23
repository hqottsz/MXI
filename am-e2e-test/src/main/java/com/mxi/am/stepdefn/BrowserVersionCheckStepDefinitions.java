
package com.mxi.am.stepdefn;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.ChromeVersionPageDriver;
import com.mxi.am.driver.web.NavigationDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Browser version check step definition
 */
public class BrowserVersionCheckStepDefinitions {

   @Inject
   private ChromeVersionPageDriver iChromeVersionPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;


   @When( "^I check the Chrome browser version$" )
   public void iCheckTheChromeBrowserVersion() throws Throwable {
      iNavigationDriver.getPage( "chrome://version" );
   }


   @Then( "^the Chrome version should be supported$" )
   public void theChromeVersionShouldBeSupported() throws Throwable {
      // expected versions set a system property in am-e2e-test.gradle
      String lExpectedVersionString = System.getProperty( "expected.versions" );
      List<String> lExpectedVersionList = Arrays.asList( lExpectedVersionString.split( "," ) );

      String lVersion = iChromeVersionPageDriver.getVersion();
      boolean lMatchFound = false;
      for ( String lString : lExpectedVersionList ) {
         if ( lVersion.contains( lString.trim() ) ) {
            lMatchFound = true;
         }
      }
      Assert.assertTrue( "Unexpected browser verion. Expected Chrome versions "
            + lExpectedVersionList + ". Actual version is " + lVersion, lMatchFound );
   }
}
