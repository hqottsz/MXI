package com.mxi.am.stepdefn.persona.engineering.systemeng;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.LogoutPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.assembly.details.AssemblyDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.usagedefn.CreateUsageDefinitionPageDriver;
import com.mxi.am.driver.web.usagedefn.UsageDefinitionDetailsPageDriver;
import com.mxi.am.stepdefn.persona.engineering.systemeng.data.ResetUsageDefinitionData;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Assembly Usage Definition Step Definitions
 */

@ScenarioScoped
public class AssemblyUsageDefinitionStepDefinitions {

   @Inject
   private ToDoListPageDriver iTodoListPageDriver;

   @Inject
   private AssemblyDetailsPageDriver iAssemblyDetailsPageDriver;

   @Inject
   private CreateUsageDefinitionPageDriver iCreateUsageDefinitionPageDriver;

   @Inject
   private UsageDefinitionDetailsPageDriver iUsageDefinitionDetialsPageDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private LogoutPageDriver iLogoutDriver;

   @Inject
   @AssetManagement
   public DatabaseDriver iDatabaseDriver;

   // Test data
   private final String iAssembly = "ACFTENG4 (Aircraft ENG 4)";
   private final String iType = "MXFL (Maintenix Flight Log)";
   private final String iUsageDefinitionCreate = "MXFL (MXFL - ACFTENG4)";
   private final String iUsageDefinitionDelete = "BULK (BULK - ACFTENG4)";
   private final String iPageTitle = "Assembly Details";


   @Given( "^an aircraft assembly$" )
   public void anAircraftAssembly() throws Throwable {
      /*
       * Assembly set up in C_ASSMBL_LIST.csv and C_ATA_SYS.csv from
       * am-e2e-test\src\main\data\baseline\persona\engineering
       */
   }


   @When( "^I create a new usage definition for the assembly$" )
   public void iCreateANewUsageDefinitionForTheAssembly() throws Throwable {
      iTodoListPageDriver.clickTabAssemblyList().clickAssembly( iAssembly );
      iAssemblyDetailsPageDriver.clickTabUsage().clickCreateUsageDefinition();
      iCreateUsageDefinitionPageDriver.setType( iType );
      iCreateUsageDefinitionPageDriver.clickOK();
      iUsageDefinitionDetialsPageDriver.clickClose();
   }


   @Then( "^the new usage definition is created$" )
   public void theNewUsageDefinitionIsCreated() throws Throwable {
      Assert.assertTrue( iAssemblyDetailsPageDriver.getPageTitle().equals( iPageTitle ) );
      Assert.assertTrue( iAssemblyDetailsPageDriver.clickTabUsage()
            .isUsageDefinitionInTable( iUsageDefinitionCreate ) );
      iNavigationDriver.clickLogout();
      iLogoutDriver.clickOK();
   }


   @After( "@Usage-Definition-Delete" )
   public void teardownDelete() {
      // Reset data created during this scenario
      Assert.assertTrue( iDatabaseDriver.update( ResetUsageDefinitionData.DELETE_USAGE_DEFN,
            ResetUsageDefinitionData.ASSEMBLY_NAME,
            ResetUsageDefinitionData.USAGE_DEFINITION_MXFL ) == 1 );
   }


   @Given( "^an aircraft assembly with a usage definition$" )
   public void anAssemblyWithAUsageDefinition() throws Throwable {
      /*
       * Assembly set up in C_ASSMBL_LIST.csv and C_ATA_SYS.csv from
       * am-e2e-test\src\main\data\baseline\persona\engineering
       */
   }


   @When( "^I delete the usage definition$" )
   public void iDeleteTheUsageDefinition() throws Throwable {
      iTodoListPageDriver.clickTabAssemblyList().clickAssembly( iAssembly );
      iAssemblyDetailsPageDriver.clickTabUsage().selectUsageDefinition( iUsageDefinitionDelete );
      iAssemblyDetailsPageDriver.clickTabUsage().clickDeleteUsageDefinition();
   }


   @Then( "^the usage definition is deleted$" )
   public void theUsageDefinitionIsDeleted() throws Throwable {
      Assert.assertTrue( iAssemblyDetailsPageDriver.getPageTitle().equals( iPageTitle ) );
      Assert.assertTrue( !iAssemblyDetailsPageDriver.clickTabUsage()
            .isUsageDefinitionInTable( iUsageDefinitionDelete ) );
      iNavigationDriver.clickLogout();
      iLogoutDriver.clickOK();
   }


   @After( "@Usage-Definition-Create" )
   public void teardownCreate() {
      // Reset data deleted during this scenario
      Assert.assertTrue( iDatabaseDriver.update( ResetUsageDefinitionData.CREATE_USAGE_DEFN,
            ResetUsageDefinitionData.ASSEMBLY_NAME,
            ResetUsageDefinitionData.USAGE_DEFINITION_BULK ) == 1 );
   }
}
