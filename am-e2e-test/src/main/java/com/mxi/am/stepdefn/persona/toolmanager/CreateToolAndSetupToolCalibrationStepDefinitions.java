package com.mxi.am.stepdefn.persona.toolmanager;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.MessagePageDriver;
import com.mxi.am.driver.common.configurationParameters.ConfigurationParameterDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.CreateInventoryPageDriver;
import com.mxi.am.driver.web.inventory.editinventorypage.EditInventoryPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.InventorySearchPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.inventorysearchpanes.InventoryFoundPaneDriver.InventorySearchResult;
import com.mxi.am.driver.web.location.locationsearchpage.LocationSearchPageDriver;
import com.mxi.am.driver.web.part.partdetailspage.PartDetailsPageDriver;
import com.mxi.am.driver.web.part.partsearchpage.PartSearchPageDriver;
import com.mxi.am.driver.web.taskdefn.ActivateTaskDefinitionPageDriver;
import com.mxi.am.driver.web.taskdefn.AddSchedulingRulePageDriver;
import com.mxi.am.driver.web.taskdefn.CreateRequirementPageDriver;
import com.mxi.am.driver.web.taskdefn.CreateRequirementPageDriver.WorkscopeRadioButtons;
import com.mxi.am.driver.web.taskdefn.InitializeTaskPageDriver;
import com.mxi.am.driver.web.taskdefn.InitializeTaskPageDriver.SelectAllRadioButtons;
import com.mxi.am.driver.web.taskdefn.reqdetailspage.ReqDetailsPageDriver;
import com.mxi.am.driver.web.taskdefn.taskdefinitionsearchpage.TaskDefinitionSearchPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;

import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class CreateToolAndSetupToolCalibrationStepDefinitions {

   @Inject
   private ActivateTaskDefinitionPageDriver iActivateTaskDefinitionPageDriver;

   @Inject
   private AddSchedulingRulePageDriver iAddSchedulingRulePageDriver;

   @Inject
   private ConfigurationParameterDriver iConfigurationParameter;

   @Inject
   private CreateInventoryPageDriver iCreateInventoryPageDriver;

   @Inject
   private CreateRequirementPageDriver iCreateRequirementPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private EditInventoryPageDriver iEditInventoryPageDriver;

   @Inject
   private InitializeTaskPageDriver iInitializeTaskPageDriver;

   @Inject
   private InventorySearchPageDriver iInventorySearchPageDriver;

   @Inject
   private LocationSearchPageDriver iLocationSearchPageDriver;

   @Inject
   private MessagePageDriver iMessagePageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private PartDetailsPageDriver iPartDetailsPageDriver;

   @Inject
   private PartSearchPageDriver iPartSearchPageDriver;

   @Inject
   private ReqDetailsPageDriver iReqDetailsPageDriver;

   @Inject
   private TaskDefinitionSearchPageDriver iTaskDefinitionSearchPageDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   private final String iTaskDefnClass = "REQ (Requirement)";
   private final String iEffectiveDate = "Effective Date";
   private final String iInstructions = "Test instructions for tool calibration task def\'n";
   private final String iManufacturedDate = "01-OCT-2016";


   @When( "^I define a calibration requirement \"([^\"]*)\" for part number \"([^\"]*)\"$" )
   public void iDefineCalibrationRequirementForPart( String aRequirementName, String aPartNo )
         throws Throwable {
      iConfigurationParameter.update( "ENFORCE_SIGNATURES", "FALSE" );
      iNavigationDriver.navigate( "Administrator", "To Do List (Administration)" );
      iToDoListPageDriver.clickRefreshParameters();

      // Navigate to the Part Search page and search for the appropriate part number
      iNavigationDriver.navigate( "Material Controller", "Part Search" );
      iPartSearchPageDriver.clickClearAll();
      iPartSearchPageDriver.setOemPartNo( aPartNo );
      iPartSearchPageDriver.clickSearch();

      // Create a new requirement for that part number
      iPartSearchPageDriver.clickTabPartsFound().getResults().get( 0 ).clickOemPartNo( aPartNo );
      iPartDetailsPageDriver.clickTabTaskDefinitions().clickTabRequirements()
            .clickCreateRequirement();

      // Enter and submit all details for the calibration requirement
      iCreateRequirementPageDriver.setGeneratedCode();
      iCreateRequirementPageDriver.setName( aRequirementName );
      iCreateRequirementPageDriver.setClass( iTaskDefnClass );
      iCreateRequirementPageDriver.setWorkscope( WorkscopeRadioButtons.WITHOUT_JIC );
      iCreateRequirementPageDriver.setRecurring( true );
      iCreateRequirementPageDriver.setScheduleFrom( iEffectiveDate );
      iCreateRequirementPageDriver.setEffectiveFromDate( iManufacturedDate );
      iCreateRequirementPageDriver.setOnCondition( true );
      iCreateRequirementPageDriver.setInstructions( iInstructions );
      iCreateRequirementPageDriver.clickOkButton();

   }


   @When( "^the requirement \"([^\"]*)\" recurs at an interval of \"([^\"]*)\" \"([^\"]*)\"s$" )
   public void theRequirementRecursAtIntervalParameter( String aRequirementName, String aInterval,
         String aParameter ) throws Throwable {
      iConfigurationParameter.update( "ENFORCE_SIGNATURES", "FALSE" );
      iNavigationDriver.navigate( "Administrator", "To Do List (Administration)" );
      iToDoListPageDriver.clickRefreshParameters();

      // Navigate to the Task Definition Search page and search for the appropriate task definition
      iNavigationDriver.navigate( "Engineer", "Task Definition Search" );
      iTaskDefinitionSearchPageDriver.clickClearAll();
      iTaskDefinitionSearchPageDriver.setTaskDefinitionName( aRequirementName );
      iTaskDefinitionSearchPageDriver.clickSearch();
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinition( aRequirementName );

      // Add the scheduling rule
      iReqDetailsPageDriver.clickTabScheduling().clickAddSchedulingRule();
      iAddSchedulingRulePageDriver.setUsageParm( aParameter );
      iAddSchedulingRulePageDriver.setInitialInterval( aInterval );
      iAddSchedulingRulePageDriver.setRepeatInterval( aInterval );
      iAddSchedulingRulePageDriver.clickOk();

   }


   @When( "^I activate and initialize the requirement \"([^\"]*)\" on all applicable tools$" )
   public void iActivateAndInitializeTheRequirement( String aRequirementName ) throws Throwable {
      iConfigurationParameter.update( "ENFORCE_SIGNATURES", "FALSE" );
      iNavigationDriver.navigate( "Administrator", "To Do List (Administration)" );
      iToDoListPageDriver.clickRefreshParameters();

      // Navigate to the Task Definition Search page and search for the appropriate task definition
      iNavigationDriver.navigate( "Engineer", "Task Definition Search" );
      iTaskDefinitionSearchPageDriver.clickClearAll();
      iTaskDefinitionSearchPageDriver.setTaskDefinitionName( aRequirementName );
      iTaskDefinitionSearchPageDriver.clickSearch();
      iTaskDefinitionSearchPageDriver.clickTabTaskDefinitionsFound()
            .clickTableTaskDefinition( aRequirementName );

      // Activate the requirement
      iReqDetailsPageDriver.clickActivateRequirement();
      iActivateTaskDefinitionPageDriver.clickOk();

      // Initialize the requirement
      iReqDetailsPageDriver.clickInitializeRequirement();

      // If there is only one inventory on which the requirement can be initialized, initialize the
      // requirement on that inventory only
      // Otherwise, attempt to initialize the requirement on all inventory
      if ( iInitializeTaskPageDriver.getInventoryTableRows().size() == 1 ) {
         iInitializeTaskPageDriver.getInventoryTableRows().get( 0 ).clickCheckbox();
      } else {
         iInitializeTaskPageDriver.setSelectAll( SelectAllRadioButtons.SELECT_ALL_APPLICABLE );
      }

      iInitializeTaskPageDriver.clickCreateTasks();
      iInitializeTaskPageDriver.clickClose();

   }


   @When( "^I create a tracked tool with part number \"([^\"]*)\" at location \"([^\"]*)\"$" )
   public void createToolAtLocation( String aPartNo, String aLocationCode ) throws Throwable {
      // Navigate to Inventory Search page and create a new inventory
      iNavigationDriver.navigate( "Material Controller", "Inventory Search" );
      iInventorySearchPageDriver.clickTabInventoryFound().clickCreateInventory();

      // Set the part number
      iCreateInventoryPageDriver.clickSelectPart();
      iPartSearchPageDriver.setOemPartNo( aPartNo );
      iPartSearchPageDriver.clickSearch();
      iPartSearchPageDriver.clickTabPartsFound().getResults().get( 0 ).clickPartNo();
      iPartSearchPageDriver.clickTabPartsFound().clickAssignPart();

      // Complete and submit the Create Inventory page form
      iCreateInventoryPageDriver.generateSerialNo();
      iCreateInventoryPageDriver.setLocation( aLocationCode );
      iCreateInventoryPageDriver.setManufacturedDate( iManufacturedDate );
      iCreateInventoryPageDriver.setCondition( "RFI (Ready for Issue)" );
      iCreateInventoryPageDriver.clickOk();
      iEditInventoryPageDriver.clickFinish();

   }


   @When( "^I check the upcoming tasks for a tool with part number \"([^\"]*)\"$" )
   public void iCheckTheUpcomingTasksForAToolWithPartNumber( String aPartNo ) throws Throwable {

      // Navigate to Inventory Search page and search for a tool inventory
      iNavigationDriver.navigate( "Material Controller", "Inventory Search" );
      iInventorySearchPageDriver.clearAll();
      iInventorySearchPageDriver.setOEMPartNo( aPartNo );
      iInventorySearchPageDriver.clickSearch();

      List<InventorySearchResult> lInventorySearchResults =
            iInventorySearchPageDriver.clickTabInventoryFound().getResults();

      lInventorySearchResults.get( lInventorySearchResults.size() - 1 ).clickSerialNoBatchNo();

      // Check the open tasks for that tool
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenTasks();

   }


   @When( "^I define a repair shop \"([^\"]*)\" to have repair capabilities for part number \"([^\"]*)\"$" )
   public void iDefineShopCanRepairPartNumber( String aLocationCode, String aPartNo )
         throws Throwable {
      // Navigate to the Part Search page and search for the appropriate part number
      iNavigationDriver.navigate( "Material Controller", "Part Search" );
      iPartSearchPageDriver.setOemPartNo( aPartNo );
      iPartSearchPageDriver.clickSearch();
      iPartSearchPageDriver.clickTabPartsFound().getResults().get( 0 ).clickOemPartNo( aPartNo );

      // Add the repair location
      iPartDetailsPageDriver.clickTabDetails().clickAssignRepairLocation();
      iLocationSearchPageDriver.setLocationCode( aLocationCode );
      iLocationSearchPageDriver.clickSearch();
      iLocationSearchPageDriver.clickTabLocationsFound().getResults().get( 0 ).select();
      iLocationSearchPageDriver.clickAssignLocation();
   }


   @Then( "^the inventory is created as a tool$" )
   public void inventoryIsCreatedAsTool() throws Throwable {
      Assert.assertTrue(
            "Issued field label on inventory details page does not print \"Checked Out\" for new tool.",
            iInventoryDetailsPageDriver.clickTabDetails().getIssuedLabel()
                  .contains( "Checked Out" ) );
   }


   @Then( "^the tool is checked in to the crib$" )
   public void toolIsCheckedIn() throws Throwable {
      Assert.assertFalse( "New tool is not created as checked in.",
            iInventoryDetailsPageDriver.clickTabDetails().isInventoryIssued() );
   }


   @Then( "^the tool is ready to be checked out$" )
   public void iCanCheckOutTool() throws Throwable {
      Assert.assertTrue(
            "Check Out Tool button is not visible on inventory details page for new tool.",
            iInventoryDetailsPageDriver.hasCheckOutTool() );
   }


   @Then( "^an actual task instance of \"([^\"]*)\" is scheduled for the tool$" )
   public void anActualTaskInstanceIsScheduledForTheTool( String aTaskName ) throws Throwable {
      Assert.assertNotNull( "Actual task is not created for the tool", iInventoryDetailsPageDriver
            .getTabOpen().getTabOpenTasks().getTaskTableRowContainingTaskName( aTaskName ) );
   }


   @Then( "^the repair shop \"([^\"]*)\" appears as a repair location for part number \"([^\"]*)\"$" )
   public void theRepairShopAppearsAsARepairLocation( String aLocationCode, String aPartNo )
         throws Throwable {
      iNavigationDriver.navigate( "Material Controller", "Part Search" );
      iPartSearchPageDriver.setOemPartNo( aPartNo );
      iPartSearchPageDriver.clickSearch();
      iPartSearchPageDriver.clickTabPartsFound().getResults().get( 0 ).clickOemPartNo( aPartNo );
      iPartDetailsPageDriver.clickTabDetails();

      Assert.assertNotNull( "Repair shop does not appear as repair location for part number",
            iPartDetailsPageDriver.getTabDetails()
                  .getRepairLocationsRowContainingLocationCode( aLocationCode ) );
   }


   @After( "@RunSCMaterials" )
   public void afterSCMaterials() {

      if ( iNavigationDriver.getTitle().equals( "Message" ) ) {
         iMessagePageDriver.clickOk();
      }

      iConfigurationParameter.update( "ENFORCE_SIGNATURES", "TRUE" );
      iNavigationDriver.navigate( "Administrator", "To Do List (Administration)" );
      iToDoListPageDriver.clickRefreshParameters();

   }

}
