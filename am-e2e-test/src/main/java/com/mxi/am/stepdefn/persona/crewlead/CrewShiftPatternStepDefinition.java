package com.mxi.am.stepdefn.persona.crewlead;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.runner.RunWith;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.department.AddShiftPatternToDepartmentPageDriver;
import com.mxi.am.driver.web.department.departmentDetails.DepartmentDetailsPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.UserDetailsPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;


@RunWith( Cucumber.class )
public class CrewShiftPatternStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private DepartmentDetailsPageDriver iDepartmentDetailsPageDriver;

   @Inject
   private UserDetailsPageDriver iUserDetailsPageDriver;

   @Inject
   private AddShiftPatternToDepartmentPageDriver iAddShiftPatternToDepartmentPageDriver;

   static String iSelectedShiftPattern;


   @When( "^I locate my crew$" )
   public void iLocateMyCrew() throws Throwable {
      iNavigationDriver.navigate( "References", "My User Details" );
      iUserDetailsPageDriver.clickTabDepartments().clickDepartmentNameInTable( "crew" );
   }


   @When( "^I assign shift pattern to crew$" )
   public void iAssignShiftPatternToCrew() throws Throwable {
      iDepartmentDetailsPageDriver.clickTabSchedule().clickAddCrewSchedule();
      iSelectedShiftPattern = iAddShiftPatternToDepartmentPageDriver.clickFirstValidShiftPattern();
      if ( iSelectedShiftPattern.contains( " (" ) )
         iSelectedShiftPattern =
               iSelectedShiftPattern.substring( 0, iSelectedShiftPattern.indexOf( " (" ) );
      iAddShiftPatternToDepartmentPageDriver.setStartDate();
      iAddShiftPatternToDepartmentPageDriver.setEndDate();
      iAddShiftPatternToDepartmentPageDriver.clickOk();

   }


   @Then( "^shift pattern is assigned to crew$" )
   public void shiftPatternIsAssignedToCrew() throws Throwable {
      Assert.assertTrue( iDepartmentDetailsPageDriver.clickTabSchedule()
            .isShiftPatternAdded( iSelectedShiftPattern ) );
   }

}
