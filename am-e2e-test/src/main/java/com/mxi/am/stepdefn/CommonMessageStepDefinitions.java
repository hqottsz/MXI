
package com.mxi.am.stepdefn;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.CommonMessagePageDriver;

import cucumber.api.java.en.Then;


/**
 * Step Definitions for navigating Maintenix
 */
public class CommonMessageStepDefinitions {

   @Inject
   private CommonMessagePageDriver iCommonMessagePageDriver;


   @Then( "^I should receive a message \"([^\"]*)\" could not be found$" )
   public void receiveMessageBarcodeNotFound( String aBarcode ) throws Throwable {
      Assert.assertEquals( "Message", iCommonMessagePageDriver.getTitle() );
      Assert.assertEquals( "The barcode '" + aBarcode + "' could not be found in the system.",
            iCommonMessagePageDriver.getMessage() );
   }
}
