/*
 * \ * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2019 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */
package com.mxi.am.stepdefn.persona.shippingclerk;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.InventorySearchPageDriver;
import com.mxi.am.driver.web.shipment.CreateEditShipmentPageDriver;
import com.mxi.am.driver.web.shipment.SendShipmentPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class SendShipmentStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private InventorySearchPageDriver iInventorySearchPageDriver;

   @Inject
   private CreateEditShipmentPageDriver iCreateEditShipmentPageDriver;

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private SendShipmentPageDriver iSendShipmentPageDriver;

   private final String iShipmentID = "SCMATSHPMNT00001";
   public String iInventorySerialNumber = "SCMATSHP1";
   public String iInventoryPartNumber = "A0000001";
   private final String iShipToLocation = "AIRPORT2/DOCK";
   private final String iShipByDate = "01-SEP-2020";

   private final String iInventoryBarcode;


   // get the barcode of inventory
   @Inject
   public SendShipmentStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iInventoryBarcode = aInventoryQueriesDriver.getBarcodeBySerialPartNo( iInventoryPartNumber,
            iInventorySerialNumber );
   }


   @Given( "^that an inventory is to be shipped from my dock$" )
   public void thatInventoryIsToBeShipped() throws Throwable {
      // Navigate to inventory details page
      iNavigationDriver.barcodeSearch( iInventoryBarcode );

      iInventoryDetailsPageDriver.clickCreateShipment();

      iCreateEditShipmentPageDriver.setShipmentID( iShipmentID );
      iCreateEditShipmentPageDriver.setShipToLocation( iShipToLocation );
      iCreateEditShipmentPageDriver.setShipByDate( iShipByDate );
      iCreateEditShipmentPageDriver.clickOK();
   }


   @When( "^I ship the inventory$" )
   public void iShipTheInventory() throws Throwable {
      iNavigationDriver.barcodeSearch( iShipmentID );
      iShipmentDetailsPageDriver.clickSendShipment();
      iSendShipmentPageDriver.clickOK();
   }


   @Then( "^the inventory is in transit$" )
   public void theInventoryIsInTransit() throws Throwable {
      iNavigationDriver.barcodeSearch( iShipmentID );

      Assert.assertTrue( iShipmentDetailsPageDriver.getRoutingStatus()
            .contentEquals( "IN-TRANS (In Transit)" ) );
   }

}
