package com.mxi.am.stepdefn.persona.storeroomclerk;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterDriver;
import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterWorkflow;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.LoginPageDriver;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.ToBeShelvedPaneDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.ToBeShelvedPaneDriver.ToBeShelvedTable;
import com.mxi.am.driver.web.transfer.CompletePutAwayPageDriver;
import com.mxi.am.driver.web.transfer.CompletePutAwayPageDriver.PutAwayTable;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CompletePutAwayInventoryStepDefinitions {

   private static final String STOREROOM_CLERK_USERNAME = "storeroomclerk";
   private static final String SUPERUSER_USERNAME = "mxi";
   private static final String PASSWORD = "password";
   private static final String AUTO_COMPLETE_PUTAWAY = "AUTO_COMPLETE_PUTAWAY";
   private static final String AUTO_PRINT_PUTAWAY_TICKET = "AUTO_PRINT_PUTAWAY_TICKET";
   private static final String PART_NUMBER = "PART4188";
   private static final String SERIAL_NUMBER = "OPER-4188";
   private static final String STOREROOM_CLERK = "Storeroom Clerk";
   private static final String TO_DO_LIST_STOREROOM_CLERK = "To Do List (Storeroom Clerk)";
   private static final String TRUE = "true";
   private static final String FALSE = "FALSE";

   @Inject
   private ConfigurationParameterDriver iConfigurationParameterDriver;

   @Inject
   private ConfigurationParameterWorkflow iConfigurationParameterWorkflow;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private LogoutPageDriver iLogoutPageDriver;

   @Inject
   private LoginPageDriver iLoginPageDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private CompletePutAwayPageDriver iCompletePutAwayPageDriver;

   private final String iBarcode;
   private String iOriginalAutoCompletePutAwayParm;
   private String iOriginalAutoPrintPutAwayTicketParm;


   @Inject
   public CompletePutAwayInventoryStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      iBarcode = aInventoryQueriesDriver.getBarcodeBySerialPartNo( PART_NUMBER, SERIAL_NUMBER );
   }


   @When( "^I enter an inventory barcode trying to validate for completing a put away$" )
   public void iEnterTheInventoryBarcodeTryingToValidate() {
      updateConfigParmForCompletePutawayButtonToShowUp();

      iNavigationDriver.navigate( STOREROOM_CLERK, TO_DO_LIST_STOREROOM_CLERK );
      ToBeShelvedPaneDriver lToBeShelvedPaneDriver = iToDoListPageDriver.clickTabToBeShelved();

      for ( ToBeShelvedTable ToBeShelvedTable : lToBeShelvedPaneDriver.getRows() ) {
         if ( SERIAL_NUMBER.equals( ToBeShelvedTable.getSerialNumber() ) ) {
            ToBeShelvedTable.clickCheckBox();
            lToBeShelvedPaneDriver.clickCompletePutAwayButton();
            iCompletePutAwayPageDriver.setBarcode( iBarcode );
            iCompletePutAwayPageDriver.clickValidate();
            break;
         }
      }

      revertConfigParmChanges();
   }


   @Then( "^The barcode is properly validated$" )
   public void theBarcodeIsValidated() {
      List<PutAwayTable> lRows = iCompletePutAwayPageDriver.getRows();
      Assert.assertEquals( 1, lRows.size() );
      for ( PutAwayTable lPutAwayTable : lRows ) {
         Assert.assertEquals( TRUE, lPutAwayTable.getValidatedValue() );
      }

   }


   private void updateConfigParmForCompletePutawayButtonToShowUp() {
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();
      iLoginPageDriver.login( SUPERUSER_USERNAME, PASSWORD );
      iOriginalAutoCompletePutAwayParm = iConfigurationParameterWorkflow
            .temporaryParameterToggleAndRefresh( AUTO_COMPLETE_PUTAWAY, FALSE );
      iOriginalAutoPrintPutAwayTicketParm = iConfigurationParameterWorkflow
            .temporaryParameterToggleAndRefresh( AUTO_PRINT_PUTAWAY_TICKET, FALSE );
      iNavigationDriver.clickLogout();
      iLogoutPageDriver.clickOK();
      iLoginPageDriver.login( STOREROOM_CLERK_USERNAME, PASSWORD );
   }


   private void revertConfigParmChanges() {
      iConfigurationParameterDriver.update( AUTO_COMPLETE_PUTAWAY,
            iOriginalAutoCompletePutAwayParm );
      iConfigurationParameterDriver.update( AUTO_PRINT_PUTAWAY_TICKET,
            iOriginalAutoPrintPutAwayTicketParm );
   }
}
