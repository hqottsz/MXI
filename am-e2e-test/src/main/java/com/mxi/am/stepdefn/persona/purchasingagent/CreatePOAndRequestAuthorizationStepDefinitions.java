package com.mxi.am.stepdefn.persona.purchasingagent;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.POLinesQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.part.PartRequestDetailsPageDriver;
import com.mxi.am.driver.web.part.PartRequestSearchPageDriver;
import com.mxi.am.driver.web.part.PartRequestSearchPageDriver.ReqPartSearchResult;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CreatePOAndRequestAuthorizationStepDefinitions {

   @Inject
   private PartRequestDetailsPageDriver iPartRequestDetailsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private PartRequestSearchPageDriver iPartRequestSearchPageDriver;

   @Inject
   private CreateEditOrderPageDriver iCreateEditOrderPageDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private POLinesQueriesDriver iPOLinesQueriesDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsPageDriver;

   private final String REQUEST_ID = "PR1";
   private final String VENDOR_ID = "10002";
   private final String SHIPTO_LOCATION = "AIRPORT1/DOCK";
   private final String UNIT_PRICE = "1500";
   private final String LINE_PRICE = "1500";
   private final String PASSWORD = "password";
   private final String PART_NUMBER = "CHW000023";
   private final String MANUFACTURER = "ABC11";


   @And( "^I have an adhoc part request$" )
   public void iHaveAnAdhocPartRequest() throws Throwable {

      iNavigationDriver.navigate( "Purchasing Agent", "Part Request Search" );

      iPartRequestSearchPageDriver.setRequestedID( REQUEST_ID );
      iPartRequestSearchPageDriver.clickSearch();

      List<ReqPartSearchResult> lList = iPartRequestSearchPageDriver.getResults();

      // Get the first row and click on the Request ID hyperlink
      ReqPartSearchResult lResultRow = lList.get( 0 );
      lResultRow.clickRequestIDSearchResultRow();

   }


   @When( "^I create a purchase order$" )
   public void iCreatePO() throws Throwable {

      iPartRequestDetailsPageDriver.clickCreatePO();

      iCreateEditOrderPageDriver.setVendor( VENDOR_ID );
      iCreateEditOrderPageDriver.setShipTo( SHIPTO_LOCATION );
      iCreateEditOrderPageDriver.clickOK();
   }


   @Then( "^the purchase order is created$" )
   public void thePOisCreated() throws Throwable {

      Assert.assertEquals( "OPEN (The order is new and/or being prepared.)",
            iOrderDetailsPageDriver.getStatus() );
      Assert.assertEquals( "PENDING", iOrderDetailsPageDriver.getAuthorizationStatus() );
   }


   @When( "^I request authorization for the purchase order$" )
   public void iClickRequestAuthorizationAndThenClickOK() throws Throwable {

      // edit the line to increase the line price over $1500 (over Purchase Agent approval level)
      // update unit and line price value in database
      iPOLinesQueriesDriver.setPOLineUnitAndLinePriceByREQNumberPartNumberManufacturer( UNIT_PRICE,
            LINE_PRICE, REQUEST_ID, PART_NUMBER, MANUFACTURER );
      iOrderDetailsPageDriver.clickRequestAuthorization();
      iRequestAuthorizationPageDriver.clickOK();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
   }


   @Then( "^the Authorization Status of the PO changes to REQUESTED$" )
   public void thePOisAuthorizationRequest() throws Throwable {

      Assert.assertEquals( "REQUESTED", iOrderDetailsPageDriver.getAuthorizationStatus() );
   }

}
