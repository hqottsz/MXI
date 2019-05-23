
package com.mxi.am.stepdefn.persona.linetechnician.damagelocation;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.task.AddDamageRecordPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.DamageRecordPaneDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.stepdefn.persona.linetechnician.damagelocation.data.DamageRecordScenarioData;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class DamageRecordStepDefinitions {

   @Inject
   private ToDoListPageDriver iToDoListHeaderPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private AddDamageRecordPageDriver iAddDamageRecordPageDriver;


   @When( "^I add a damage record to a fault$" )
   public void iAddDamageLocationToFault() throws Throwable {
      iToDoListHeaderPageDriver.getTabFleetList()
            .clickAircraftInTable( DamageRecordScenarioData.REG_CODE );
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenFaults()
            .clickFaultInTable( DamageRecordScenarioData.FAULT_NAME );
      iTaskDetailsPageDriver.clickAddDamageButton();
      iAddDamageRecordPageDriver.chooseAircraft();
      iAddDamageRecordPageDriver.setLocation( DamageRecordScenarioData.LOCATION );
      iAddDamageRecordPageDriver.clickOk();

   }


   @Then( "^I can see the damage record is added to the fault" )
   public void seeTheDamageRecordAddedToFault() throws Throwable {
      DamageRecordPaneDriver lDamageRecordPaneDriver =
            iTaskDetailsPageDriver.clickDamageRecordTab();
      Assert.assertEquals( DamageRecordScenarioData.LOCATION,
            lDamageRecordPaneDriver.getLocation() );
   }

}
