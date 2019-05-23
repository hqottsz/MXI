
package com.mxi.am.stepdefn;

import javax.inject.Inject;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.mxi.am.driver.web.AssetManagement;

import cucumber.api.Scenario;
import cucumber.api.java.After;


/**
 * The web driver harness
 */
public class WebDriverHarness {

   private final WebDriver iDriver;


   @Inject
   public WebDriverHarness(@AssetManagement WebDriver aDriver) {
      iDriver = aDriver;
   }


   //
   // Re: @After( order = 5000 ); this value is set less than the default (10000) to give other
   // After hooks a
   // chance to execute prior to taking a screen shot of the scenario's final page.
   //
   @After( order = 5000 )
   public void takeScreenShot( Scenario aScenario ) {
      if ( iDriver instanceof TakesScreenshot ) {
         aScenario.embed( ( ( TakesScreenshot ) iDriver ).getScreenshotAs( OutputType.BYTES ),
               "image/png" );
      }
   }


   // Re: @After( order = 1000 ); this value is set less than the order value of takeScreenShot() to
   // ensure the screen shot is take prior to the driver shutting down.
   @After( order = 1000 )
   public void finalizeWebDriver( Scenario aScenario ) {

      iDriver.quit();
   }
}
