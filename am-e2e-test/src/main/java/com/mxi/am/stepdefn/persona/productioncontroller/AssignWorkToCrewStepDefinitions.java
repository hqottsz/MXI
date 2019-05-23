package com.mxi.am.stepdefn.persona.productioncontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import com.mxi.am.driver.query.CrewShiftPlanQueriesDriver;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.common.SubmitReasonAndNotesPageDriver;
import com.mxi.am.driver.web.department.departmentDetails.DepartmentDetailsPageDriver;
import com.mxi.am.driver.web.labour.AssignCrewPageDriver;
import com.mxi.am.driver.web.labour.AssignTasksToCrewShiftDayPageDriver;
import com.mxi.am.driver.web.location.locationdetailspage.LocationDetailsPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.planshift.PlanShiftPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.user.AddEditHrUserShiftPatternPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.UserDetailsPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.userdetailspanes.UserDetailsSchedulePaneDriver.ScheduleTableRow;
import com.mxi.am.stepdefn.persona.productioncontroller.data.AssignWorkPackageToCrewScenarioData;
import com.mxi.am.stepdefn.persona.productioncontroller.data.AssignWorkToCrewScenarioData;
import com.mxi.am.stepdefn.persona.productioncontroller.data.AssignWorkToCrewShiftDayScenarioData;
import com.mxi.am.stepdefn.persona.productioncontroller.data.IdentifyAvailableResourcesScenarioData;
import com.mxi.am.stepdefn.persona.productioncontroller.data.IdentifyAvailableResourcesScenarioData.Shift;
import com.mxi.am.stepdefn.persona.productioncontroller.data.ReviewInProgressWorkScenarioData;
import com.mxi.am.stepdefn.persona.productioncontroller.data.ReviewUnassignedWorkScenarioData;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class AssignWorkToCrewStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private LocationDetailsPageDriver iLocationDetailsPageDriver;

   @Inject
   private DepartmentDetailsPageDriver iDepartmentDetailsPageDriver;

   @Inject
   private UserDetailsPageDriver iUserDetailsPageDriver;

   @Inject
   private AddEditHrUserShiftPatternPageDriver iAddEditHrUserShiftPatternPageDriver;

   @Inject
   private PlanShiftPageDriver iPlanShiftPageDriver;

   @Inject
   private AssignCrewPageDriver iAssignCrewPageDriver;

   @Inject
   private AssignTasksToCrewShiftDayPageDriver iAssignTasksToCrewShiftDayPageDriver;

   @Inject
   private SubmitReasonAndNotesPageDriver iSubmitReasonAndNotesPageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private CrewShiftPlanQueriesDriver iCrewShiftPlanQueriesDriver;

   private static int iWaitingTime;
   private static int iCrewShiftPlanId;
   private static String iSearchHours;
   private static String iProductionController;
   private static String iProductionControllerToDoList;
   private static String iHeavyTechnician;
   private static String iHeavyTechnicianToDoList;
   private static String iWorkPackage;
   private static String iWorkPackageNo;
   private static String iAssignedTask;
   private static String iUnAssignedTask;
   private static String iLaborSkill;
   private static String iDepartment;
   private static String iCrewCode;
   private static String iUserName;
   private static String iLocation;
   private static String iStartDate;
   private static String iEndDate;
   private static String iUserShiftPattern;
   private static String iPrimarySkill;
   private static String iShiftDay;
   private static String iShiftCode;
   private static String iShiftPattern;
   private static String iPassword;


   @Before( "@ReviewInProgressWorkScenarioDataSetup " )
   public void reviewInProgressWorkScenarioDataSetup() {
      iProductionController = ReviewInProgressWorkScenarioData.PRODUCTION_CONTROLLER;
      iProductionControllerToDoList =
            ReviewInProgressWorkScenarioData.PRODUCTION_CONTROLLER_TO_DO_LIST;
      iWorkPackage = ReviewInProgressWorkScenarioData.WORK_PACKAGE;
      iAssignedTask = ReviewInProgressWorkScenarioData.ASSIGNED_TASK;
      iPassword = ReviewInProgressWorkScenarioData.PASSWORD;
      iWaitingTime = ReviewInProgressWorkScenarioData.MAX_WAIT_TIME_IN_MS;
   }


   @Before( "@ReviewUnassignedWorkScenarioDataSetup" )
   public void reviewUnassignedWorkScenarioDataSetup() {
      iProductionController = ReviewUnassignedWorkScenarioData.PRODUCTION_CONTROLLER;
      iProductionControllerToDoList =
            ReviewUnassignedWorkScenarioData.PRODUCTION_CONTROLLER_TO_DO_LIST;
      iWorkPackage = ReviewUnassignedWorkScenarioData.WORK_PACKAGE;
      iAssignedTask = ReviewUnassignedWorkScenarioData.ASSIGNED_TASK;
      iLaborSkill = ReviewUnassignedWorkScenarioData.ENG_LABOR_SKILL;
      iPassword = ReviewUnassignedWorkScenarioData.PASSWORD;
      iWaitingTime = ReviewUnassignedWorkScenarioData.MAX_WAIT_TIME_IN_MS;
   }


   @Before( "@IdentifyAvailableResourcesScenarioDataSetup" )
   public void identifyAvailableResourcesScenarioDataSetup() {
      iProductionController = IdentifyAvailableResourcesScenarioData.PRODUCTION_CONTROLLER;
      iProductionControllerToDoList =
            IdentifyAvailableResourcesScenarioData.PRODUCTION_CONTROLLER_TO_DO_LIST;
      iWorkPackage = IdentifyAvailableResourcesScenarioData.WORK_PACKAGE;
      iDepartment = IdentifyAvailableResourcesScenarioData.DEPARTMENT;
      iUserName = IdentifyAvailableResourcesScenarioData.USER_NAME;
      iLocation = IdentifyAvailableResourcesScenarioData.LOCATION;
      iStartDate = IdentifyAvailableResourcesScenarioData.START_DATE;
      iEndDate = IdentifyAvailableResourcesScenarioData.END_DATE;
      iUserShiftPattern = IdentifyAvailableResourcesScenarioData.USER_SHIFT_PATTERN;
      iPrimarySkill = IdentifyAvailableResourcesScenarioData.ENG_LABOR_SKILL;
      iWaitingTime = IdentifyAvailableResourcesScenarioData.MAX_WAIT_TIME_IN_MS;
   }


   @Before( "@AssignWorkPackageToCrewScenarioDataSetup" )
   public void assignWorkPackageToCrewScenarioDataSetup() {
      iProductionController = AssignWorkPackageToCrewScenarioData.PRODUCTION_CONTROLLER;
      iProductionControllerToDoList =
            AssignWorkPackageToCrewScenarioData.PRODUCTION_CONTROLLER_TO_DO_LIST;
      iHeavyTechnician = AssignWorkPackageToCrewScenarioData.HEAVY_TECHNICIAN;
      iHeavyTechnicianToDoList = AssignWorkPackageToCrewScenarioData.HEAVY_TECHNICIAN_TO_DO_LIST;
      iWorkPackage = AssignWorkPackageToCrewScenarioData.WORK_PACKAGE;
      iDepartment = AssignWorkPackageToCrewScenarioData.DEPARTMENT;
      iCrewCode = AssignWorkPackageToCrewScenarioData.CREW_CODE;
      iUserName = AssignWorkPackageToCrewScenarioData.USER_NAME;
      iLocation = AssignWorkPackageToCrewScenarioData.LOCATION;
      iAssignedTask = AssignWorkPackageToCrewScenarioData.ASSIGNED_TASK;
      iWaitingTime = AssignWorkPackageToCrewScenarioData.MAX_WAIT_TIME_IN_MS;
      iWorkPackageNo = iWorkPackageQueriesDriver.getWorkPackageNoByWorkPackageName( iWorkPackage );
   }


   @Before( "@AssignWorkToCrewScenarioDataSetup" )
   public void assignWorkToCrewScenarioDataSetup() {
      iProductionController = AssignWorkToCrewScenarioData.PRODUCTION_CONTROLLER;
      iProductionControllerToDoList = AssignWorkToCrewScenarioData.PRODUCTION_CONTROLLER_TO_DO_LIST;
      iHeavyTechnician = AssignWorkToCrewScenarioData.HEAVY_TECHNICIAN;
      iHeavyTechnicianToDoList = AssignWorkToCrewScenarioData.HEAVY_TECHNICIAN_TO_DO_LIST;
      iWorkPackage = AssignWorkToCrewScenarioData.WORK_PACKAGE;
      iDepartment = AssignWorkToCrewScenarioData.DEPARTMENT;
      iCrewCode = AssignWorkToCrewScenarioData.CREW_CODE;
      iUserName = AssignWorkToCrewScenarioData.USER_NAME;
      iLocation = AssignWorkToCrewScenarioData.LOCATION;
      iAssignedTask = AssignWorkToCrewScenarioData.TASK_TO_ASSIGN;
      iUnAssignedTask = AssignWorkToCrewScenarioData.TASK_TO_UNASSIGN;
      iWaitingTime = AssignWorkToCrewScenarioData.MAX_WAIT_TIME_IN_MS;
      iWorkPackageNo = iWorkPackageQueriesDriver.getWorkPackageNoByWorkPackageName( iWorkPackage );
   }


   @Before( "@AssignWorkToCrewShiftDayScenarioDataSetup" )
   public void assignWorkToCrewShiftDayScenarioDataSetup() {
      iProductionController = AssignWorkToCrewShiftDayScenarioData.PRODUCTION_CONTROLLER;
      iProductionControllerToDoList =
            AssignWorkToCrewShiftDayScenarioData.PRODUCTION_CONTROLLER_TO_DO_LIST;
      iHeavyTechnician = AssignWorkToCrewShiftDayScenarioData.HEAVY_TECHNICIAN;
      iHeavyTechnicianToDoList = AssignWorkToCrewShiftDayScenarioData.HEAVY_TECHNICIAN_TO_DO_LIST;
      iWorkPackage = AssignWorkToCrewShiftDayScenarioData.WORK_PACKAGE;
      iDepartment = AssignWorkToCrewShiftDayScenarioData.DEPARTMENT;
      iCrewCode = AssignWorkToCrewShiftDayScenarioData.CREW_CODE;
      iUserName = AssignWorkToCrewShiftDayScenarioData.USER_NAME;
      iLocation = AssignWorkToCrewShiftDayScenarioData.LOCATION;
      iAssignedTask = AssignWorkToCrewShiftDayScenarioData.TASK_TO_ASSIGN;
      iUnAssignedTask = AssignWorkToCrewShiftDayScenarioData.TASK_TO_UNASSIGN;
      iWaitingTime = AssignWorkToCrewShiftDayScenarioData.MAX_WAIT_TIME_IN_MS;
      iSearchHours = AssignWorkToCrewShiftDayScenarioData.FIVE_HUNDREDS_HOURS;
      iCrewShiftPlanId = AssignWorkToCrewShiftDayScenarioData.CREW_SHIFT_PLAN_ID;
      iShiftPattern = AssignWorkToCrewShiftDayScenarioData.SHIFT_PATTERN;
      iPassword = AssignWorkToCrewShiftDayScenarioData.PASSWORD;
      iWorkPackageNo = iWorkPackageQueriesDriver.getWorkPackageNoByWorkPackageName( iWorkPackage );
      iShiftDay = iCrewShiftPlanQueriesDriver.getShiftDayByCrewCodeAndCrewShiftPlanId( iCrewCode,
            iCrewShiftPlanId );
      iShiftCode = iCrewShiftPlanQueriesDriver.getShiftCodeByCrewCodeAndCrewShiftPlanId( iCrewCode,
            iCrewShiftPlanId );
   }


   @Given( "^I have a Work Package$" )
   public void iHaveAWorkPackage() throws Throwable {
      iNavigationDriver.navigate( iProductionController, iProductionControllerToDoList );
      iToDoListPageDriver.clickTabAssignedWorkList();
      iToDoListPageDriver.getTabAssignedWorkList().clickWorkPackageInTable( iWorkPackage );
   }


   @Given( "^the Work Package has Assigned Tasks$" )
   public void theWorkPackageHasAssignedTasks() throws Throwable {
      iCheckDetailsPageDriver.clickTabAssignedTasks();
      assertTrue(
            iCheckDetailsPageDriver.getTabAssignedTasks().assignedTaskExist( iAssignedTask ) );
   }


   @When( "^the Workscope is Committed$" )
   public void theWorkscopeIsCommitted() throws Throwable {
      iCheckDetailsPageDriver.clickCommitScopeButton();
      iSubmitReasonAndNotesPageDriver.clickOk();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( iPassword );
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( iWaitingTime );
   }


   @When( "^I review the WorkScope$" )
   public void iReviewTheWorkScope() throws Throwable {
      iCheckDetailsPageDriver.clickTabWorkscope();
   }


   @Then( "^the WorkScope exists$" )
   public void theWorkScopeExists() throws Throwable {
      assertTrue( iCheckDetailsPageDriver.getTabWorkscope().taskExistInWorkScope( iAssignedTask ) );
   }


   @When( "^I review the Work Package Labor Tab$" )
   public void iReviewTheWorkPackageLaborTab() throws Throwable {
      iCheckDetailsPageDriver.clickTabLabor();
   }


   @Then( "^there are Tasks that are not Assigned to Technicians$" )
   public void thereAreTasksThatAreNotAssignedToTechnicians() throws Throwable {
      assertTrue( iCheckDetailsPageDriver.getTabLabor().technicianUnassigned( iAssignedTask ) );
   }


   @And( "^the Tasks have Labor Labor Skills$" )
   public void theTaskshaveLaborLaborSkills() throws Throwable {
      assertEquals( iLaborSkill,
            iCheckDetailsPageDriver.getTabLabor().getLaborSkill( iAssignedTask ) );
   }


   @Given( "^the Work Package is Scheduled to a Location$" )
   public void theWorkPackageIsScheduledToALocation() throws Throwable {
      iCheckDetailsPageDriver.clickTabDetails();
   }


   @Given( "^the Location has a Crew$" )
   public void theLocationHasACrew() throws Throwable {
      iCheckDetailsPageDriver.getTabDetails().clickWorkLocation();
      iLocationDetailsPageDriver.clickTabDepartments();
   }


   @Given( "^the Crew has a Schedule$" )
   public void theCrewHasASchedule() throws Throwable {
      iLocationDetailsPageDriver.getTabDepartments().clickDepartment( iDepartment );

      iDepartmentDetailsPageDriver.clickTabSchedule();
      assertTrue( iShiftPattern + " is not in the Schedule",
            iDepartmentDetailsPageDriver.getTabSchedule().isShiftPatternAdded( iShiftPattern ) );
   }


   @Given( "^the Crew has a User$" )
   public void theCrewHasAUser() throws Throwable {
      iLocationDetailsPageDriver.getTabDepartments().clickDepartment( iDepartment );
      iDepartmentDetailsPageDriver.clickTabUsers();
      assertTrue( iDepartmentDetailsPageDriver.getTabUsers().memberExists( iUserName ) );
   }


   @Given( "^the Crew has a user$" )
   public void theCrewHasAuser() throws Throwable {
      iDepartmentDetailsPageDriver.clickTabUsers();
      assertTrue( iDepartmentDetailsPageDriver.getTabUsers().memberExists( iUserName ) );
      iDepartmentDetailsPageDriver.clickOkButton();
      iLocationDetailsPageDriver.clickOK();
   }


   @Given( "^the User has a Schedule$" )
   public void theUserHasASchedule() throws Throwable {
      iDepartmentDetailsPageDriver.getTabUsers().clickMember( iUserName );
      iUserDetailsPageDriver.clickTabSchedule();
      iUserDetailsPageDriver.getTabSchedule().clickAddShiftPattern();
      iAddEditHrUserShiftPatternPageDriver.setLocation( iLocation );
      iAddEditHrUserShiftPatternPageDriver.setUserShiftPattern( iUserShiftPattern );
      iAddEditHrUserShiftPatternPageDriver.setPrimarySkill( iPrimarySkill );
      iAddEditHrUserShiftPatternPageDriver.setStartDate( iStartDate );
      iAddEditHrUserShiftPatternPageDriver.setEndDate( iEndDate );
      iAddEditHrUserShiftPatternPageDriver.clickOk();
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( iWaitingTime );
   }


   @When( "^I view the User Schedule$" )
   public void iViewTheUserSchedule() throws Throwable {
      iUserDetailsPageDriver.clickTabSchedule();
   }


   @Then( "^the Schedule exists$" )
   public void theScheduleExists() throws Throwable {
      assertEquals( IdentifyAvailableResourcesScenarioData.TWO_SCHEDULES,
            iUserDetailsPageDriver.getTabSchedule().getSchedules().size() );
      assertTrue( scheduleExists( iUserDetailsPageDriver.getTabSchedule().getSchedules(),
            IdentifyAvailableResourcesScenarioData.SCHEDULE_ONE ) );
      assertTrue( scheduleExists( iUserDetailsPageDriver.getTabSchedule().getSchedules(),
            IdentifyAvailableResourcesScenarioData.SCHEDULE_TWO ) );
   }


   @And( "^the Work Package has Task$" )
   public void theWorkPackageHasTask() throws Throwable {
      iCheckDetailsPageDriver.clickTabAssignedTasks();
      assertTrue( "Task " + iAssignedTask + " is not assigned to Work Package" + iWorkPackage,
            iCheckDetailsPageDriver.getAssignedTasksTab().assignedTaskExist( iAssignedTask ) );
   }


   @When( "^I Assign the Work Package to a Crew$" )
   public void iAssignTheWorkPackageToACrew() throws Throwable {
      iDepartmentDetailsPageDriver.clickOkButton();
      iLocationDetailsPageDriver.clickOK();
      iCheckDetailsPageDriver.clickPlanShift();
      iPlanShiftPageDriver.clickSearch();
      iPlanShiftPageDriver.getTabSearchResults().clickSelectAll();
      iPlanShiftPageDriver.getTabSearchResults().clickAssignCrew();
      iAssignCrewPageDriver.clickCheckBoxForCrew( iCrewCode );
   }


   @And( "^the work package is Committed$" )
   public void theWorkPackageIsCommited() throws Throwable {
      iCheckDetailsPageDriver.clickCommitScopeButton();
      iSubmitReasonAndNotesPageDriver.clickOk();
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( iPassword );
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( iWaitingTime );
   }


   @And( "^the Work Package has Tasks$" )
   public void theWorkPackageHasTasks() throws Throwable {
      iCheckDetailsPageDriver.clickTabAssignedTasks();
      assertTrue( "Task " + iAssignedTask + " is not assigned to Work Package" + iWorkPackage,
            iCheckDetailsPageDriver.getAssignedTasksTab().assignedTaskExist( iAssignedTask ) );
      assertTrue( "Task " + iUnAssignedTask + " is not assigned to Work Package" + iWorkPackage,
            iCheckDetailsPageDriver.getAssignedTasksTab().assignedTaskExist( iUnAssignedTask ) );
   }


   @When( "^I Plan Shift for the Work Package$" )
   public void iPlanShiftForTheWorkPackage() throws Throwable {
      iDepartmentDetailsPageDriver.clickOkButton();
      iLocationDetailsPageDriver.clickOK();
      iCheckDetailsPageDriver.clickPlanShift();
   }


   @When( "^I Plan Shift for this Work Package$" )
   public void iPlanShiftForThisWorkPackage() throws Throwable {
      iCheckDetailsPageDriver.clickPlanShift();
   }


   @Then( "^I can assign a Tasks to a Crew Shift Day$" )
   public void iCanAssignATasksToACrewShiftDay() throws Throwable {
      iPlanShiftPageDriver.clickSearch();
      iPlanShiftPageDriver.getTabSearchResults().clickSelectAll();
      iPlanShiftPageDriver.getTabSearchResults().clickAssignCrewShiftDay();
      iAssignTasksToCrewShiftDayPageDriver.setHourNumber( iSearchHours );
      iAssignTasksToCrewShiftDayPageDriver.clickSearch();
      iAssignTasksToCrewShiftDayPageDriver.clickCrewShiftDayCode( iDepartment, iShiftDay,
            iShiftCode );
      iAssignTasksToCrewShiftDayPageDriver.clickOK();
   }


   @When( "^I can assign a Tasks to a Crew$" )
   public void iCanAssignATasksToACrew() throws Throwable {
      iPlanShiftPageDriver.clickSearch();
      iPlanShiftPageDriver.getTabSearchResults().clickSelectAll();
      iPlanShiftPageDriver.getTabSearchResults().clickAssignCrew();
      iAssignCrewPageDriver.clickCheckBoxForCrew( iCrewCode );
   }


   @Then( "^the Work Package is Assigned to the Crew$" )
   public void theWorkPackageIsAssignedToTheCrew() throws Throwable {
      iPlanShiftPageDriver.clickTabSearchResults();
      assertTrue( "Task " + iAssignedTask + " is not assigned to Crew " + iCrewCode,
            iPlanShiftPageDriver.getTabSearchResults().isTaskAssignedToCrew( iAssignedTask,
                  iCrewCode ) );
   }


   @And( "^I can unassign a Task from a Crew$" )
   public void iCanUnassignATaskFromACrew() throws Throwable {
      iPlanShiftPageDriver.getTabSearchResults().clickCheckboxForTask( iUnAssignedTask );
      iPlanShiftPageDriver.getTabSearchResults().clickUnassignCrew();
      assertTrue( "Task " + iUnAssignedTask + " is still assigned to Crew " + iCrewCode,
            iPlanShiftPageDriver.getTabSearchResults().isTaskUnassigned( iUnAssignedTask ) );
   }


   @And( "^I can unassign a Task from a Crew Shift Day$" )
   public void iCanUnassignATaskFromACrewShiftDay() throws Throwable {
      iPlanShiftPageDriver.clickTabSearchResults();
      iPlanShiftPageDriver.getTabSearchResults().clickCheckboxForTask( iUnAssignedTask );
      iPlanShiftPageDriver.getTabSearchResults().clickUnassignCrew();
      assertTrue( "Task " + iUnAssignedTask + " is still assigned to Crew " + iCrewCode,
            iPlanShiftPageDriver.getTabSearchResults().isTaskUnassigned( iUnAssignedTask ) );
   }


   @Then( "^I can see the Tasks on the Users Assigned to My Crew tab$" )
   public void iCanSeeTheTasksOnTheUsersAssignedToMyCrewTab() throws Throwable {
      iNavigationDriver.navigate( iHeavyTechnician, iHeavyTechnicianToDoList );
      iToDoListPageDriver.clickTabAssignedToYourCrew();
      assertTrue(
            "Task " + iAssignedTask + " is not assigned to your crew (" + iCrewCode
                  + ") in Work Package " + iWorkPackageNo + ".",
            iToDoListPageDriver.getTabAssignedToYourCrew()
                  .isAssignedToYourCrewInWorkPackage( iCrewCode, iAssignedTask, iWorkPackageNo ) );
   }


   private boolean scheduleExists( List<ScheduleTableRow> aScheduleList, Shift aSchedule ) {
      int lCount = 0;
      for ( ScheduleTableRow lSchedule : aScheduleList ) {
         if ( ( lSchedule.getLocation().equals( aSchedule.iLocation.trim() ) )
               && ( lSchedule.getDate().equals( aSchedule.iDate ) )
               && ( lSchedule.getShift().equals( aSchedule.iShift.trim() ) )
               && ( lSchedule.getPrimarySkill().equals( aSchedule.iPrimarySkill.trim() ) )
               && ( lSchedule.getTemporaryCrew().equals( aSchedule.iTemporaryCrew.trim() ) ) ) {
            lCount++;
         }
      }
      if ( lCount == 1 ) {
         return true;
      }
      System.out.println( aSchedule );
      System.out.println( "The Schedule above could not been found in the Schedule Table." );
      return false;
   }
}
