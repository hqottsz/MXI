
package com.mxi.am.stepdefn;

import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.AssetManagement;
import com.mxi.driver.api.ApiDriver;
import com.mxi.mx.core.api.utility.echo.v1.EchoParameter;
import com.mxi.mx.core.api.utility.echo.v1.EchoRequest;
import com.mxi.mx.core.api.utility.echo.v1.EchoResponse;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Step definitions that uses APIs
 */
@ScenarioScoped
public class ApiStepDefinitions {

   @Inject
   @AssetManagement
   private ApiDriver iApiDriver;

   private EchoResponse iResponse;

   private Throwable iFailedMessage;


   @Given( "^I am not authenticated for APIs$" )
   public void iAmNotAuthenticatedForAPIs() throws Throwable {
      // Do nothing!
   }


   @Then( "^the api response should be forbidden$" )
   public void assertApiResponseFailed() throws Throwable {
      Assert.assertNotNull( "The API message should have failed", iFailedMessage );
   }


   @Given( "^I am authenticated as \"([^\"]*)\" for APIs$" )
   public void authenticate( String aUsername ) throws Throwable {
      iApiDriver.login( aUsername, "password" );
   }


   @When( "^I send an api request$" )
   public void sendAnApiRequest() throws Throwable {
      EchoRequest lRequest = new EchoRequest();
      lRequest.setActionParameters(
            new HashSet<EchoParameter>( Arrays.asList( new EchoParameter( "Test" ) ) ) );
      try {
         iResponse = iApiDriver.execute( lRequest );
         iFailedMessage = null;
      } catch ( RuntimeException ex ) {
         iFailedMessage = ex;
      }
   }


   @Then( "^the api response should be received$" )
   public void assertApiResponseSucceeded() throws Throwable {
      Assert.assertNull( iFailedMessage );
      Assert.assertNotNull( iResponse );
   }
}
