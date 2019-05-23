package com.mxi.am.stepdefn.persona.linetechnician.recordoiluptake;

import org.junit.Assert;

import com.google.inject.Inject;
import com.mxi.am.driver.web.lmoc.recordoiluptake.RecordOilUptakePageDriver;
import com.mxi.am.stepdefn.persona.linetechnician.recordoiluptake.data.RecordOilUptakeScenarioData;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class RecordOilUptakeStepDefinitions {

   @Inject
   private RecordOilUptakePageDriver iRecordOilUptakePageDriver;


   @Given( "^I want to record oil uptake for an aircraft$" )
   public void iWantToRecordOilUptakeForAnAircraft() throws Throwable {
      iRecordOilUptakePageDriver.goTo();
   }


   @When( "^I submit the oil uptake values for the assemblies$" )
   public void iSubmitTheOilUptakeValuesForTheAssemblies() throws Throwable {
      iRecordOilUptakePageDriver.fillInForm( RecordOilUptakeScenarioData.FORM_DATA );
   }


   @Then( "^the oil uptake records are saved$" )
   public void theOilUptakeRecordsAreSaved() throws Throwable {
      iRecordOilUptakePageDriver.clickSave();
      String lSuccessMessage = iRecordOilUptakePageDriver.getSuccessMessage();
      Assert.assertEquals( RecordOilUptakeScenarioData.SUCCESS_MESSAGE, lSuccessMessage );
      iRecordOilUptakePageDriver.clickOK();
   }
}
