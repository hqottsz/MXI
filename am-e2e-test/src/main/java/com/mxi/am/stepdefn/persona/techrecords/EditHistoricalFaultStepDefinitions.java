package com.mxi.am.stepdefn.persona.techrecords;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver.AircraftInfo;
import com.mxi.am.driver.query.FaultQueriesDriver;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.fault.RaiseFaultPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class EditHistoricalFaultStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private RaiseFaultPageDriver iRaiseFaultPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private FaultQueriesDriver iFaultQueriesDriver;

   @Inject
   private AircraftQueriesDriver iAircraftQueriesDriver;

   @Inject
   private InventoryQueriesDriver iInventoryQueriesDriver;

   private static final String UPDATED_RESOLUTION_CONFIG_SLOT = "SYS-2 - Aircraft System 2";

   private static final String FAULT_NAME = "Edit RCS-Fault-" + UUID.randomUUID().toString();

   private static final String AIRCRAFT_SNO = "TR-RCS-1";

   private static final String AIRCRAFT_ASSEMBLY_CD = "ACFT_TR1";

   private static final String AIRCRAFT_PART_NO = "ACFT_P_TR1";

   private static final String FAILED_SYSTEM_SDESC = "SYS-1 - Aircraft System 1";

   private static final String FAULT_SOURCE = "PILOT";

   private static final String FAULT_STATUS = "CFACTV";

   private static final String FOUND_BY_USER = "linetech";


   @Given( "^there is an existing historical fault that I would like to edit$" )
   public void thereExistsAHistoricalFault() {

      String lHistoricalFaultBarcode = iFaultQueriesDriver
            .getByOpenFaultNameAndSerialNo( AIRCRAFT_SNO, FAULT_NAME ).getBarcode();

      // certify the fault and place it into historical faults
      iFaultQueriesDriver.updateFaultToCompletedStatus( FAULT_NAME, new Date() );

      iNavigationDriver.barcodeSearch( lHistoricalFaultBarcode );
      iTaskDetailsPageDriver.clickTabFaultInformation();

   }


   @When( "^I change or enter a resolution config slot$" )
   public void iModifyAHistoricalFault() {
      iTaskDetailsPageDriver.getTabFaultInformation().clickEditFaultInformationButton();
      iRaiseFaultPageDriver.setResolutionConfigSlot( UPDATED_RESOLUTION_CONFIG_SLOT );
      iRaiseFaultPageDriver.clickOk();
   }


   @Then( "^the fault is updated with the changes$" )
   public void theFaultIsUpdated() {
      Assert.assertEquals( UPDATED_RESOLUTION_CONFIG_SLOT,
            iTaskDetailsPageDriver.clickTabFaultInformation().getResolutionConfigSlot() );
   }


   @Before( "@SetupHistoricalFault" )
   public void setupHistoricalFault() {

      // grab aircraft info
      AircraftInfo lAcInvNoId = iAircraftQueriesDriver
            .getAircraftInfoByPartAndSerialNo( AIRCRAFT_PART_NO, AIRCRAFT_SNO );

      // grab failed system info
      BigDecimal lFailedSysInvNoId = iInventoryQueriesDriver
            .getInvInfoByAircraftInvNoAndInvName( lAcInvNoId.getInvNoId(), FAILED_SYSTEM_SDESC )
            .getId();

      // create fault
      iFaultQueriesDriver.insertFault( FAULT_NAME, FAULT_NAME, FAULT_SOURCE,
            lAcInvNoId.getInvNoId(), FOUND_BY_USER, FAULT_STATUS, AIRCRAFT_ASSEMBLY_CD,
            lFailedSysInvNoId );

   }
}
