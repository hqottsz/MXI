package com.mxi.am.stepdefn.persona.crewlead;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.runner.RunWith;

import com.mxi.am.driver.query.DepartmentQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.department.departmentDetails.DepartmentDetailsPageDriver;
import com.mxi.am.driver.web.department.search.DepartmentSearchPageDriver;
import com.mxi.am.driver.web.user.AddEditHrShiftPageDriver;
import com.mxi.am.driver.web.user.search.UserSearchPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.UserDetailsPageDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;


@RunWith( Cucumber.class )
public class CreateTemporaryCrewAssignmentStepDefinition {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private UserSearchPageDriver iUserSearchPageDriver;

   @Inject
   private DepartmentDetailsPageDriver iDepartmentDetailsPageDriver;

   @Inject
   private DepartmentSearchPageDriver iDepartmentSearchPageDriver;

   @Inject
   private UserDetailsPageDriver iUserDetailsPageDriver;

   @Inject
   private AddEditHrShiftPageDriver iAddEditHrShiftPageDriver;

   @Inject
   private DepartmentQueriesDriver iDepartmentQueriesDriver;

   private String iUserName = "user1";
   private String iUserName2 = "1, User";
   private String iUserCode = "1000091";

   private String iPermanentCrewCode = "CREW001";
   private String iPermanentCrewName = "crew-001";
   private String iTemporaryCrewName = "crew-002";
   private String iTemporaryCrewCode = "CREW002";

   static String[] iDateRange;
   // create temporary crew assignment for 3 days
   int iDateNum = 3;


   @When( "^I temporarily assign a technician to a crew for multiple days$" )
   public void iTemporarilyAssignATechnicianToACrewForMultipleDays() throws Throwable {
      // assert that user1 is assigned to the expected department before we start
      Assert.assertTrue(
            "Expecting department count of 1, but was " + iDepartmentQueriesDriver
                  .getDeptCountByUsername( iUserName, iPermanentCrewCode ).intValue(),
            ( iDepartmentQueriesDriver.getDeptCountByUsername( iUserName, iPermanentCrewCode )
                  .intValue() == 1 ) );

      iNavigationDriver.navigate( "Crew Lead", "User Search" );
      iUserSearchPageDriver.clickClearAll();
      iUserSearchPageDriver.setUserName( iUserName );
      iUserSearchPageDriver.clickSearch();
      iUserSearchPageDriver.clickTabUsersFound().clickUserNameInTable( iUserName2 );
      iUserDetailsPageDriver.clickTabSchedule().clickAddShift();
      iAddEditHrShiftPageDriver.setEndDate( iDateNum );
      iAddEditHrShiftPageDriver
            .setTemporaryCrew( iTemporaryCrewCode + " (" + iTemporaryCrewName + ")" );
      iAddEditHrShiftPageDriver.clickOk();
      iDateRange = iAddEditHrShiftPageDriver.getShiftAdjustmentsDates( iDateNum );

      // assert that user1 is assigned to the expected department at end of first step
      Assert.assertTrue(
            "Expecting department count of 1, but was " + iDepartmentQueriesDriver
                  .getDeptCountByUsername( iUserName, iPermanentCrewCode ).intValue(),
            ( iDepartmentQueriesDriver.getDeptCountByUsername( iUserName, iPermanentCrewCode )
                  .intValue() == 1 ) );
   }


   @When( "^unassign from crew for one day$" )
   public void unassignFromCrewForOneDay() throws Throwable {
      iUserDetailsPageDriver.clickTabSchedule().clickFirstShiftAdjustment();
      iUserDetailsPageDriver.getTabSchedule().clickRemoveShift();
      // assert that user1 is assigned to the expected department at end of second step
      Assert.assertTrue(
            "Expecting department count of 1, but was " + iDepartmentQueriesDriver
                  .getDeptCountByUsername( iUserName, iPermanentCrewCode ).intValue(),
            ( iDepartmentQueriesDriver.getDeptCountByUsername( iUserName, iPermanentCrewCode )
                  .intValue() == 1 ) );
   }


   @Then( "^shift adjustments and crew change schedules can be seen$" )
   public void shiftAdjustmentsAndCrewChangeSchedulesCanBeSeen() throws Throwable {
      Assert.assertTrue( ( iUserDetailsPageDriver.getTabSchedule()
            .isShiftAdjustmentsAddedAndRemoved( iTemporaryCrewCode, iDateNum, iDateRange ) ) );
      iUserDetailsPageDriver.getTabSchedule().clickCrewName( iTemporaryCrewCode );

      // assert that user1 is assigned to the expected department before we check Department Details
      // page
      Assert.assertTrue(
            "Expecting department count of 1, but was " + iDepartmentQueriesDriver
                  .getDeptCountByUsername( iUserName, iPermanentCrewCode ).intValue(),
            ( iDepartmentQueriesDriver.getDeptCountByUsername( iUserName, iPermanentCrewCode )
                  .intValue() == 1 ) );

      Assert.assertTrue(
            iDepartmentDetailsPageDriver.clickTabUsers().isCrewChangeScheduleCorrect( iDateRange,
                  iUserName2 + " (" + iUserCode + ")", iPermanentCrewCode, "From", iDateNum ) );
      iNavigationDriver.navigate( "Crew Lead", "Department Search" );
      iDepartmentSearchPageDriver.clickClearAll();
      iDepartmentSearchPageDriver.setDepartmentCode( iPermanentCrewCode );
      iDepartmentSearchPageDriver.clickSearch();
      iDepartmentSearchPageDriver.clickTabDepartmentsFound()
            .clickDepartmentNameInTable( iPermanentCrewCode + " (" + iPermanentCrewName + ")" );
      Assert.assertTrue(
            iDepartmentDetailsPageDriver.clickTabUsers().isCrewChangeScheduleCorrect( iDateRange,
                  iUserName2 + " (" + iUserCode + ")", iTemporaryCrewCode, "Joining", iDateNum ) );
   }
}
