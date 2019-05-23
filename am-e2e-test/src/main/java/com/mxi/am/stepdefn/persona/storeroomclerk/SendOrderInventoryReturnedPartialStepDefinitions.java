package com.mxi.am.stepdefn.persona.storeroomclerk;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.shipment.ReturnToVendorPageDriver;

import cucumber.api.java.en.Given;


public class SendOrderInventoryReturnedPartialStepDefinitions {

   @Inject
   public InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   public ReturnToVendorPageDriver iReturnToVendorPageDriver;

   private final static String RTV_QTY = "RTV Quantity";


   @Given( "^that I have created a partial Return to Vendor shipment$" )
   public void thatIHaveCreatedAPartialReturnToVendorShipment(
         List<Map<String, String>> aDataTable ) throws Throwable {
      iInventoryDetailsPageDriver.clickReturnToVendor();
      iReturnToVendorPageDriver.setBatchReturnQuantity( aDataTable.get( 0 ).get( RTV_QTY ) );
      iReturnToVendorPageDriver.ok();
   }
}
