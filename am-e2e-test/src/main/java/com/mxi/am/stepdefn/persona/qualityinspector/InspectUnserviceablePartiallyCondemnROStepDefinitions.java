package com.mxi.am.stepdefn.persona.qualityinspector;

import static com.mxi.am.helper.Selector.selectFirst;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.MessagePageDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.CondemnInventoryPageDriver;
import com.mxi.am.driver.web.inventory.InspectAsServiceablePageDriver;
import com.mxi.am.driver.web.inventory.InspectAsUnserviceablePageDriver;
import com.mxi.am.driver.web.inventory.MarkInventoryAsInspRequiredPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.InventorySearchPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.inventorysearchpanes.InventoryFoundPaneDriver.InventorySearchResult;
import com.mxi.am.driver.web.po.AOGAuthorizeOrderPageDriver;
import com.mxi.am.driver.web.po.EditPurchaseOrderLineReceiverNotePageDriver;
import com.mxi.am.driver.web.po.IssueOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.RequestAuthorizationPageDriver;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver.InventoriesInInventoryReceiptsRO;
import com.mxi.am.driver.web.po.podetails.ReceiptReturnsPaneDriver.OutboundShipmentLine;
import com.mxi.am.driver.web.shipment.ReceiveShipmentPageDriver;
import com.mxi.am.driver.web.shipment.SendShipmentPageDriver;
import com.mxi.am.driver.web.shipment.shipmentDetails.ShipmentDetailsPageDriver;
import com.mxi.am.driver.web.task.CreateOrEditCheckPageDriver;
import com.mxi.am.driver.web.task.ScheduleCheckPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.QuarantinePaneDriver.QuaratineInventories;
import com.mxi.am.driver.web.vendor.VendorSearchPageDriver;
import com.mxi.am.helper.FilterCriteria;
import com.mxi.am.stepdefn.persona.purchasingagent.DateUtil;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class InspectUnserviceablePartiallyCondemnROStepDefinitions {

   private static final String ORI_INVENTORY = "INSP-UNSER-BATCH";
   private static final String BATCH_PART = "B000001";
   private static final String WORK_PACKAGE = "WORKPACKAGE";
   private static final String PROMISED_BY_DATE = "28-FEB-2023";
   private static final String RETURN_TO_LOC = "AIRPORT1/DOCK";
   private static final String VENDOR = "10004";
   private static final String RFI = "RFI (Ready for Issue)";
   private static final String RECEIVER_NOTE = "testreceivernote";
   private static final String USER_1 = "user1";
   private static final String QC_INSPECTOR = "qcinsp1";
   private static final String PASSWORD = "password";

   @Inject
   private LoginPageDriver iLoginDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private OrderDetailsPageDriver iOrderDetailsPageDriver;

   @Inject
   private InventorySearchPageDriver iInventorySearchPageDriver;

   @Inject
   private CreateOrEditCheckPageDriver iCreateOrEditCheckPageDriver;

   @Inject
   private VendorSearchPageDriver iVendorSearchPageDriver;

   @Inject
   private ReceiveShipmentPageDriver iReceiveShipmentPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private ScheduleCheckPageDriver iScheduleCheckPageDriver;

   @Inject
   private ShipmentDetailsPageDriver iShipmentDetailsPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private SendShipmentPageDriver iSendShipmentPageDriver;

   @Inject
   private AOGAuthorizeOrderPageDriver iAOGAuthorizeOrderDriver;

   @Inject
   private IssueOrderPageDriver iIssueOrderDriver;

   @Inject
   private CondemnInventoryPageDriver iCondemnInventoryPageDriver;

   @Inject
   private InspectAsUnserviceablePageDriver iInspectAsUnserviceablePageDriver;

   @Inject
   private InspectAsServiceablePageDriver iInspectAsServiceablePageDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private RequestAuthorizationPageDriver iRequestAuthorizationPageDriver;

   @Inject
   private MarkInventoryAsInspRequiredPageDriver iMarkInventoryAsInspRequiredPageDriver;

   @Inject
   private EditPurchaseOrderLineReceiverNotePageDriver iEditPurchaseOrderLineReceiverNotePageDriver;

   @Inject
   private MessagePageDriver iMessagePageDriver;


   @Given( "^the inventories received from a repair order are partially condemned and partially serviceable$" )
   public void iRepairOrderHasPartiallyCondemnedAndServiceableInventories() throws Throwable {

      // login
      iLoginDriver.setUserName( USER_1 ).setPassword( PASSWORD ).login();

      iNavigationDriver.navigate( "Material Controller", "Inventory Search" );
      iInventorySearchPageDriver.clearAll();
      iInventorySearchPageDriver.setSerialNoBatchNo( ORI_INVENTORY );
      iInventorySearchPageDriver.clickSearch();

      List<InventorySearchResult> lResults =
            iInventorySearchPageDriver.clickTabInventoryFound().getResults();
      for ( InventorySearchResult lResult : lResults ) {
         lResult.clickSerialNoBatchNo();
      }

      // create a work package for the batch inventory
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenWorkPackages()
            .clickCreateWorkPackage();

      iCreateOrEditCheckPageDriver.setName( WORK_PACKAGE );
      iCreateOrEditCheckPageDriver.setIssueToAccount( "5" );
      iCreateOrEditCheckPageDriver.clickOK();

      // schedule the work package externally to a repair vendor
      iCheckDetailsPageDriver.clickScheduleWorkPackage();
      iScheduleCheckPageDriver.clickWorkDoneByExternalVendor();
      iScheduleCheckPageDriver.searchRepairVendor();

      iVendorSearchPageDriver.setVendorCode( VENDOR );
      iVendorSearchPageDriver.search();
      iVendorSearchPageDriver.clickAssignVendor();

      iScheduleCheckPageDriver.setPromisedBy( DateUtil.getDate( "dd-MMM-yyyy", PROMISED_BY_DATE ) );
      iScheduleCheckPageDriver.setReturnToLocation( RETURN_TO_LOC );
      iScheduleCheckPageDriver.clickOK();

      // go to the repair order
      iNavigationDriver.barcodeSearch( iCheckDetailsPageDriver.getRepairOrderNumber() );

      // add a receiver note
      iOrderDetailsPageDriver.clickEditReceiverNote();
      iEditPurchaseOrderLineReceiverNotePageDriver.editReceiverNote( RECEIVER_NOTE );
      iEditPurchaseOrderLineReceiverNotePageDriver.clickOk();

      ReceiptReturnsPaneDriver lReceiptReturnsPaneDriver =
            iOrderDetailsPageDriver.clickTabReceiptReturns();

      List<OutboundShipmentLine> lOutboundShipmentLine =
            lReceiptReturnsPaneDriver.getOutboundShipmentLines();
      OutboundShipmentLine lOutboundShipmentLineRow = lOutboundShipmentLine.get( 0 );
      lOutboundShipmentLineRow.clickOutboundShipment();

      iShipmentDetailsPageDriver.clickSendShipment();
      iSendShipmentPageDriver.clickOK();
      iShipmentDetailsPageDriver.clickClose();

      // authorize and issue the repair order
      iOrderDetailsPageDriver.clickAOGAuthorizationOverride();
      iAOGAuthorizeOrderDriver.clickOK();
      iAOGAuthorizeOrderDriver.setPassword( PASSWORD );
      iAOGAuthorizeOrderDriver.clickAuthenticate();

      iOrderDetailsPageDriver.clickIssuePurchaseOrder();
      iIssueOrderDriver.clickOk();

      // receive all of the parts
      iOrderDetailsPageDriver.clickReceiveShipment();
      iReceiveShipmentPageDriver.setQty( "5" );
      iReceiveShipmentPageDriver.clickOk();
      iShipmentDetailsPageDriver.clickClose();

      iOrderDetailsPageDriver.clickTabReceiptReturns();

      List<InventoriesInInventoryReceiptsRO> lInventoriesInInventoryReceipts =
            lReceiptReturnsPaneDriver.getInventoriesInInventoryReceiptsRO();
      InventoriesInInventoryReceiptsRO lInventoriesInInventoryReceiptsRow =
            lInventoriesInInventoryReceipts.get( 0 );
      lInventoriesInInventoryReceiptsRow.clickFirstRowSerialBatchNo();

      // condemn 3 of 5, so 3 will become Condemn, 2 will become InspReq
      iInventoryDetailsPageDriver.clickCondemnInventory();
      iCondemnInventoryPageDriver.setQty( "3" );
      iCondemnInventoryPageDriver.clickOk();
      iCondemnInventoryPageDriver.setPassword( PASSWORD );
      iCondemnInventoryPageDriver.authenticate();

      iInventoryDetailsPageDriver.clickClose();

      // get the new inventory serial/batch no
      iOrderDetailsPageDriver.clickTabReceiptReturns();
      List<InventoriesInInventoryReceiptsRO> lInventoriesInInventoryReceiptsAgain =
            lReceiptReturnsPaneDriver.getInventoriesInInventoryReceiptsRO();
      String lNewInventory =
            lInventoriesInInventoryReceiptsAgain.get( 0 ).getFirstRowSerialBatchNo();

      // log out after
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();

      // Login as QC Inspector
      iLoginDriver.setUserName( QC_INSPECTOR ).setPassword( PASSWORD ).login();

      // go to Inspector To Do List
      iNavigationDriver.navigate( "Quality Control Inspector",
            "To Do List (Quality Control Inspector)" );

      // check the specified inventory
      iToDoListPageDriver.clickTabInspectionRequired()
            .clickCheckboxfromSerialBatchNo( lNewInventory );

      // inspect this inventory with quantity 2 as serviceable
      iToDoListPageDriver.getTabInspectionRequired().clickInspectServiceableButton();
      iInspectAsServiceablePageDriver.printServiceablepartTag();
      iInspectAsServiceablePageDriver.clickOk();

      iRequestAuthorizationPageDriver.setPassword( PASSWORD ).authenticate();

      // wait for the page refresh
      Wait.pause( 2000 );

      // search for this inventory
      iNavigationDriver.navigate( "Quality Control Inspector", "Inventory Search" );

      iInventorySearchPageDriver.clearAll();
      iInventorySearchPageDriver.setSerialNoBatchNo( lNewInventory );
      iInventorySearchPageDriver.setRfiCondition();

   }


   @And( "^the serviceable item is RFI$" )
   public void theItemIsRFI() throws Throwable {

      iInventorySearchPageDriver.clickSearch();

      InventorySearchResult lInventorySearchResult =
            iInventorySearchPageDriver.clickTabInventoryFound().getResults().get( 0 );
      // verify the quantity is 2
      Assert.assertEquals( "2 EA", lInventorySearchResult.getQty() );

      lInventorySearchResult.clickSerialNoBatchNo();

      // verify the inventory is RFI now
      Assert.assertEquals( RFI, iInventoryDetailsPageDriver.clickTabDetails().getCondition() );
      // verify the receiver note is there
      Assert.assertEquals( RECEIVER_NOTE, iInventoryDetailsPageDriver.getReceiverNote() );
   }


   @When( "^inspector is blocked for marking the condemned inventory as repair required$" )
   public void iTryToMarkCondemnedInventoryAsRepairRequired() throws Throwable {

      iNavigationDriver.navigate( "Quality Control Inspector", "Inventory Search" );

      // search for the condemned inventory
      iInventorySearchPageDriver.clearAll();
      iInventorySearchPageDriver.setOEMPartNo( BATCH_PART );
      iInventorySearchPageDriver.setCondemnCondition();

      iInventorySearchPageDriver.clickSearch();

      InventorySearchResult lInventorySearchResult =
            iInventorySearchPageDriver.clickTabInventoryFound().getResults().get( 0 );

      String lCondemnedInventory = lInventorySearchResult.getSerialNoBatchNo();
      lInventorySearchResult.clickSerialNoBatchNo();

      // try to mark this inventory as repair required
      iInventoryDetailsPageDriver.clickMarkAsRepairRequired();

      // an error message shows up to block this behavior
      Assert.assertTrue( iMessagePageDriver.getCellMessage().contains( "'" + lCondemnedInventory
            + "' was received from a vendor and hasn't been inspected yet. Instead of marking as repair required, please inspect it first." ) );
      iMessagePageDriver.clickOk();

   }


   @And( "^he clicks Mark as Inspection Required$" )
   public void iClickMarkAsInspectionRequired() throws Throwable {

      // mark this inventory as inspection required
      iInventoryDetailsPageDriver.clickMarkAsInspectionRequired();
      iMarkInventoryAsInspRequiredPageDriver.clickOk();
      iMarkInventoryAsInspRequiredPageDriver.setPassword( PASSWORD );
      iMarkInventoryAsInspRequiredPageDriver.authenticate();

   }


   @Then( "^the inventory is Inspection Required$" )
   public void theInventoryIsInspectionRequired() throws Throwable {

      // verify the inventory is INSPREQ now
      Assert.assertEquals( iInventoryDetailsPageDriver.clickTabDetails().getCondition(),
            "INSPREQ (Inspection Required)" );
   }


   @Then( "^an Inspector goes to Inspection Required To Do List again and inspects the condemned inventory as unserviceable$" )
   public void iInspectCondemnedInventoryAsUnserviceable() throws Throwable {

      String lCondemnedInventory = iInventoryDetailsPageDriver.getSerialNumber();

      // go to Inspector To Do List
      iNavigationDriver.navigate( "Quality Control Inspector",
            "To Do List (Quality Control Inspector)" );

      // check the specified inventory
      iToDoListPageDriver.clickTabInspectionRequired()
            .clickCheckboxfromSerialBatchNo( lCondemnedInventory );
      iToDoListPageDriver.getTabInspectionRequired().clickInspectUnserviceableButton();
      iInspectAsUnserviceablePageDriver.clickPrintUnServiceablepartTag();
      iInspectAsUnserviceablePageDriver.clickOk();
      iInspectAsUnserviceablePageDriver.setPassword( PASSWORD ).clickAuthenticationOkButton();
      // need to check that inventory is NOT found on quarantine tab, and we should see an Illegal
      // Argument exception
      if ( iToDoListPageDriver.clickTabQuarantine().isQuarantineListEmpty() ) {
         Assert.assertTrue( true );
      } else {
         try {
            selectFirst( iToDoListPageDriver.getTabQuarantine().getQuaratineInventories(),
                  withQuarantinedSerialNo( lCondemnedInventory ) );
            Assert.assertTrue( "Inventory was unexpectedly found on Quarantine list.", false );
         } catch ( IllegalArgumentException e ) {
            // as expected, the inventory could not be found in the table and an exception was
            // thrown, so let's declare it as a pass
            boolean lInventoryNotFound = true;
            Assert.assertTrue( lInventoryNotFound );
         }
      }

      // search for the repair required inventory
      iNavigationDriver.navigate( "Quality Control Inspector", "Inventory Search" );
      iInventorySearchPageDriver.clearAll();
      iInventorySearchPageDriver.setOEMPartNo( BATCH_PART );
      iInventorySearchPageDriver.setSerialNoBatchNo( lCondemnedInventory );
      iInventorySearchPageDriver.setRepReqCondition();
      iInventorySearchPageDriver.clickSearch();
      iInventorySearchPageDriver.clickTabInventoryFound().getResults().get( 0 )
            .clickSerialNoBatchNo();

      // mark this inventory as inspection required
      iInventoryDetailsPageDriver.clickMarkAsInspectionRequired();
      iMarkInventoryAsInspRequiredPageDriver.clickOk();
      iMarkInventoryAsInspRequiredPageDriver.setPassword( PASSWORD );
      iMarkInventoryAsInspRequiredPageDriver.authenticate();

      // wait for the page refresh
      Wait.pause( 2000 );

      // go to Inspector To Do List
      iNavigationDriver.navigate( "Quality Control Inspector",
            "To Do List (Quality Control Inspector)" );

      // check the specified inventory
      iToDoListPageDriver.clickTabInspectionRequired()
            .clickCheckboxfromSerialBatchNo( lCondemnedInventory );

      // verify the inspect as unserviceable button is not enabled
      Assert.assertTrue( iToDoListPageDriver.getTabInspectionRequired().UnserviceableEnabled()
            .contains( "disabled" ) );

      // log out after
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();
   }


   private FilterCriteria<QuaratineInventories> withQuarantinedSerialNo( final String aSerialNo ) {
      return new FilterCriteria<QuaratineInventories>() {

         @Override
         public boolean test( QuaratineInventories aQuaratineInventories ) {
            if ( aQuaratineInventories.getSerialNo().equals( aSerialNo ) )

               return true;
            else
               return false;

         }
      };
   }
}
