
package com.mxi.am.stepdefn.persona.lineplanner;

import static com.mxi.am.helper.Selector.selectFirst;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.integration.IntegrationMessageDriver;
import com.mxi.am.driver.integrationtesting.AbstractMessageDriverXmlBeans;
import com.mxi.am.driver.query.PartRequestQueriesDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.part.addpartrequirementpage.AddPartRequirementPageDriver;
import com.mxi.am.driver.web.part.addpartrequirementpage.addpartrequirementpane.PartsFoundPaneDriver.TablePartsFoundResults;
import com.mxi.am.driver.web.task.TaskSelectionPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.helper.FilterCriteria;
import com.mxi.driver.api.ApiDriver;
import com.mxi.driver.standard.Wait;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.Manufacturer;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.Part;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus.AuthorizationInfo;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus.RequestedPart;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus.RequestedPart.PartRequest;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus.RequestedPart.Status;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus.RequestedPart.Status.Inventory;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus.RequestedPart.Status.Inventory.Batch;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus.RequestedPart.Status.Inventory.Serialized;
import com.mxi.xml.xsd.core.matadapter.partRequestStatus.x13.PartRequestStatusDocument.PartRequestStatus.RequestedPart.Status.RequestStatus.Enum;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for navigating Maintenix
 */
public class MaterialsAPIStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectPageDriver;

   @Inject
   private TaskSelectionPageDriver iTaskSelectionPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private AddPartRequirementPageDriver iAddPartRequirementPageDriver;

   @Inject
   private PartRequestQueriesDriver iPartRequestQueriesDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   @AssetManagement
   private ApiDriver iApiDriver;

   private String iAircraftRegCode = "102";
   private String iTargetConfigSlot = "ACFT_CD1";
   private String iTaskName = "Test Task";
   private static List<PartReservation> iPartReservations = new ArrayList<PartReservation>();

   private static final String INT_USERNAME = "mxintegration";
   private static final String INT_PASSWORD = "password";


   @When( "^I create part requests$" )
   public void iCreatePartRequests() throws Throwable {

      // stores reservation lines
      iPartReservations.add( new PartReservation( "A0000001", 1, Arrays.asList( "EXT-001" ),
            Arrays.asList( "A0000001" ), Arrays.asList( "00001" ), "TESTSN-001", "TRK",
            "Tracked Part", Arrays.asList( 1 ), Arrays.asList( "PRRESERVE" ),
            Arrays.asList( "2016-05-16 10:00:00" ) ) );
      iPartReservations.add( new PartReservation( "A0000002", 1, Arrays.asList( "EXT-002" ),
            Arrays.asList( "A0000002" ), Arrays.asList( "11111" ), "TESTSN-002", "TRK",
            "2 Position Tracked Part", Arrays.asList( 1 ), Arrays.asList( "PROPEN" ),
            Arrays.asList( "2016-05-16 11:00:00" ) ) );
      iPartReservations.add( new PartReservation( "A0000003", 1, Arrays.asList( "EXT-003" ),
            Arrays.asList( "A0000003" ), Arrays.asList( "ABC11" ), "TESTSN-003", "TRK",
            "3 Position Tracked Part", Arrays.asList( 1 ), Arrays.asList( "PRRESERVE" ),
            Arrays.asList( "2016-05-16 12:00:00" ) ) );
      iPartReservations.add( new PartReservation( "A0000011", 10,
            Arrays.asList( "EXT-004", "EXT-005" ), Arrays.asList( "A0000011", "A0000011" ),
            Arrays.asList( "ABC11", "ABC11" ), "", "BATCH", "BATCH-on-TRK Child",
            Arrays.asList( 5, 5 ), Arrays.asList( "PRRESERVE", "PROPEN" ),
            Arrays.asList( "2016-05-16 13:00:00", "2016-05-16 14:00:00" ) ) );
      iPartReservations.add( new PartReservation( "A0000011", 10,
            Arrays.asList( "EXT-006", "EXT-007" ), Arrays.asList( "A0000011", "A0000011" ),
            Arrays.asList( "ABC11", "ABC11" ), "", "BATCH", "BATCH-on-TRK Child",
            Arrays.asList( 5, 5 ), Arrays.asList( "PRRESERVE", "PRRESERVE" ),
            Arrays.asList( "2016-05-16 15:00:00", "2016-05-17 16:00:00" ) ) );

      // select line planner
      iNavigationDriver.navigate( "Line Planner", "To Do List (Line Planner)" );
      iToDoListPageDriver.clickTabFleetList().clickRadioButtonForAircraft( iAircraftRegCode );
      iToDoListPageDriver.getTabFleetList().clickCreateTaskButton();
      iInventorySelectPageDriver.selectInventoryFromTree( iTargetConfigSlot );
      iTaskSelectionPageDriver.clickCreateAdHoc();
      iTaskSelectionPageDriver.setTaskName( iTaskName );
      iTaskSelectionPageDriver.clickOkForCreateTask();
      iTaskDetailsPageDriver.clickTabTaskExecution().clickAddPartRequirement();

      for ( int i = 0; i < 5; i++ ) {
         PartReservation lPartReservation = iPartReservations.get( i );
         iAddPartRequirementPageDriver.clickClearAll();
         iAddPartRequirementPageDriver.setPartNoOEMField( lPartReservation.getReqPartNo() );
         iAddPartRequirementPageDriver.clickSearch();

         selectFirst( iAddPartRequirementPageDriver.clickTabPartsFound().getPartsFoundRows(),
               withPartGroupName( lPartReservation.getPartGroupName() ) ).clickPartGroup();

         // if quantity =1 then we don't need to update
         if ( lPartReservation.getReqQty() > 1 ) {
            // temporary alternative to TableDriver for this column
            iAddPartRequirementPageDriver.setRequestQuantity( lPartReservation.getReqPartNo(),
                  lPartReservation.getReqQty() );
         }

         iAddPartRequirementPageDriver.getTabPartsFound()
               .clickAssignSelectedPartsToTaskSearchAgain();

         // pause to allow Mx to write to the db before querying
         Wait.pause( 500 );

         // collect request IDs to be used in messages
         lPartReservation.setMasterRequestId( iPartRequestQueriesDriver
               .getRequestIdByPartQtyAcRegCd( lPartReservation.getReqPartNo(),
                     lPartReservation.getReqQty(), iAircraftRegCode ) );

      }

      iAddPartRequirementPageDriver.getTabPartsFound().clickCancel();

   }


   // to be used once dual header tables can be supported by TableDriver

   private FilterCriteria<TablePartsFoundResults> withPartGroupName( final String aPartGroupName ) {
      return new FilterCriteria<TablePartsFoundResults>() {

         @Override
         public boolean test( TablePartsFoundResults aPartsFoundRow ) {
            return aPartsFoundRow.getPartGroupName().startsWith( aPartGroupName );
         }
      };
   }


   @Then( "^the request responses update the request statuses$" )
   public void theRequestResponsesUpdateTheRequestStatuses() throws Throwable {

      // Send a create vendor message into Maintenix
      AbstractMessageDriverXmlBeans lMessageDriver =
            new IntegrationMessageDriver( INT_USERNAME, INT_PASSWORD );

      // creates and sends message for each loop
      for ( int i = 0; i < 5; i++ ) {

         PartReservation lPartReservation = iPartReservations.get( i );
         PartRequestStatusDocument lRequestDoc = PartRequestStatusDocument.Factory.newInstance();

         PartRequestStatus lPartRequestStatus = lRequestDoc.addNewPartRequestStatus();
         RequestedPart lRequestedPart = lPartRequestStatus.addNewRequestedPart();
         PartRequest lPartRequest = lRequestedPart.addNewPartRequest();
         lPartRequest.setMasterReqId( lPartReservation.getMasterRequestId() );
         lPartRequest.setRequestId( lPartReservation.getMasterRequestId() );

         for ( ReservationLine lReservationLine : lPartReservation.getReservationLine() ) {

            Status lStatus = lRequestedPart.addNewStatus();

            Inventory lInventory = lStatus.addNewInventory();
            lStatus.setRequestStatus( Enum.forString( lReservationLine.getStatus() ) );
            lStatus.setEstimatedArrival( lReservationLine.getETA() );
            lStatus.setExternalId( lReservationLine.getExternalID() );

            if ( lPartReservation.getInventoryClass() == "TRK" ) {

               Serialized lSerialized = lInventory.addNewSerialized();
               lSerialized.setSerialNoOem( lReservationLine.getSerialNumber() );
               Part lPart = lSerialized.addNewPart();
               lPart.setPartNoOem( lReservationLine.getPartNumber() );
               Manufacturer lManufacturer = lPart.addNewManufacturer();
               lManufacturer.setManufacturerCode( lReservationLine.getManufacturer() );

            } else {

               Batch lBatch = lInventory.addNewBatch();
               lBatch.setQuantity( lReservationLine.getQuantity().floatValue() );
               Part lPart = lBatch.addNewPart();
               lPart.setPartNoOem( lReservationLine.getPartNumber() );
               Manufacturer lManufacturer = lPart.addNewManufacturer();
               lManufacturer.setManufacturerCode( lReservationLine.getManufacturer() );
            }
         }
         String lReason = "Test";
         String lAuthority = "mxintegration";
         String lDate = "2015-06-30 17:00:00";
         AuthorizationInfo lAuthorizationInfo = lPartRequestStatus.addNewAuthorizationInfo();
         lAuthorizationInfo.setAuthority( lAuthority );
         lAuthorizationInfo.setDatetime( lDate );
         lAuthorizationInfo.setReason( lReason );

         // Send the request to the API
         lMessageDriver.sendMessage( lRequestDoc );
         checkPartRequestDetails( lPartReservation.getReservationLine(),
               lPartReservation.getMasterRequestId() );

      }

   }


   private void checkPartRequestDetails( List<ReservationLine> aReservationLine,
         String aMasterRequestId ) throws InterruptedException {
      for ( ReservationLine lReservationLine : aReservationLine ) {

         int lLoopCounter = 0;
         while ( iPartRequestQueriesDriver.getCountByExtId( lReservationLine.getExternalID() )
               .intValue() == 0 ) {
            assertThat( "Message failure or message timeout - on request: "
                  + lReservationLine.getExternalID(), ( lLoopCounter < 50 ) );
            lLoopCounter++;
            Thread.sleep( 500 );
         }

         Assert.assertEquals( lReservationLine.getStatus(),
               iPartRequestQueriesDriver.getStatusByExtId( lReservationLine.getExternalID() ) );
         Assert.assertEquals( lReservationLine.getQuantity(),
               iPartRequestQueriesDriver.getQuantityByExtId( lReservationLine.getExternalID() ) );
         Assert.assertEquals( lReservationLine.getPartNumber(),
               iPartRequestQueriesDriver.getPartNoByExtId( lReservationLine.getExternalID() ) );
      }

   }
}
