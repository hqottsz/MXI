package com.mxi.am.stepdefn.persona.crewlead;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.department.departmentDetails.DepartmentDetailsPageDriver;
import com.mxi.am.driver.web.labour.AssignCrewPageDriver;
import com.mxi.am.driver.web.labour.LabourAssignmentPageDriver;
import com.mxi.am.driver.web.location.locationdetailspage.LocationDetailsPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.planshift.PlanShiftPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.user.AddEditHrUserShiftPatternPageDriver;
import com.mxi.am.driver.web.user.userdetailspage.UserDetailsPageDriver;
import com.mxi.am.stepdefn.persona.crewlead.data.IdentifyAvailResAndAssignWorkToTechScenarioData;
import com.mxi.am.stepdefn.persona.crewlead.data.ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class AssignWorkToTechnicianStepDefinition {

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
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private AddEditHrUserShiftPatternPageDriver iAddEditHrUserShiftPatternPageDriver;

   @Inject
   private PlanShiftPageDriver iPlanShiftPageDriver;

   @Inject
   private AssignCrewPageDriver iAssignCrewPageDriver;

   @Inject
   private LabourAssignmentPageDriver iLabourAssignmentPageDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   @Inject
   private WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   private static int iWaitingTime;
   private static String iProductionController;
   private static String iProductionControllerToDoList;
   private static String iHeavyTechnician;
   private static String iHeavyTechnicianToDoList;
   private static String iWorkPackage;
   private static String iWorkPackageNo;
   private static String iAssignedTask;
   private static String iLaborSkill;
   private static String iDepartment;
   private static String iCrewCode;
   private static String iTechnician;
   private static String iLocation;
   private static String iUserShiftPattern;
   private static String iStartDate;
   private static String iEndDate;
   private static String iPrimarySkill;
   private static String iNextHours;


   @Before( "@ReviewWorkAssignedToCrewAndSkillsRequiredScenarioDataSetup" )
   public void assignWorkToTechnicianScenarioDataSetup() {
      iProductionController =
            ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.PRODUCTION_CONTROLLER;
      iProductionControllerToDoList =
            ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.PRODUCTION_CONTROLLER_TO_DO_LIST;
      iHeavyTechnician = ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.HEAVY_TECHNICIAN;
      iHeavyTechnicianToDoList =
            ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.HEAVY_TECHNICIAN_TO_DO_LIST;
      iWorkPackage = ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.WORK_PACKAGE;
      iDepartment = ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.DEPARTMENT;
      iCrewCode = ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.CREW_CODE;
      iAssignedTask = ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.TASK_TO_ASSIGN;
      iLaborSkill = ReviewWorkAssignedToCrewAndSkillsRequiredScenarioData.LABOR_SKILL;
      iWorkPackageNo = iWorkPackageQueriesDriver.getWorkPackageNoByWorkPackageName( iWorkPackage );
   }


   @Before( "@IdentifyAvailResAndAssignWorkToTechScenarioDataSetup" )
   public void identifyAvailResAndAssignWorkToTechScenarioDataSetup() {
      iProductionController = IdentifyAvailResAndAssignWorkToTechScenarioData.PRODUCTION_CONTROLLER;
      iProductionControllerToDoList =
            IdentifyAvailResAndAssignWorkToTechScenarioData.PRODUCTION_CONTROLLER_TO_DO_LIST;
      iHeavyTechnician = IdentifyAvailResAndAssignWorkToTechScenarioData.HEAVY_TECHNICIAN;
      iHeavyTechnicianToDoList =
            IdentifyAvailResAndAssignWorkToTechScenarioData.HEAVY_TECHNICIAN_TO_DO_LIST;
      iWorkPackage = IdentifyAvailResAndAssignWorkToTechScenarioData.WORK_PACKAGE;
      iDepartment = IdentifyAvailResAndAssignWorkToTechScenarioData.DEPARTMENT;
      iCrewCode = IdentifyAvailResAndAssignWorkToTechScenarioData.CREW_CODE;
      iAssignedTask = IdentifyAvailResAndAssignWorkToTechScenarioData.ASSIGNED_TASK;
      iLaborSkill = IdentifyAvailResAndAssignWorkToTechScenarioData.LABOR_SKILL;
      iTechnician = IdentifyAvailResAndAssignWorkToTechScenarioData.USER_NAME;
      iLocation = IdentifyAvailResAndAssignWorkToTechScenarioData.LOCATION;
      iStartDate = IdentifyAvailResAndAssignWorkToTechScenarioData.START_DATE;
      iEndDate = IdentifyAvailResAndAssignWorkToTechScenarioData.END_DATE;
      iUserShiftPattern = IdentifyAvailResAndAssignWorkToTechScenarioData.USER_SHIFT_PATTERN;
      iNextHours = IdentifyAvailResAndAssignWorkToTechScenarioData.FOURTY_EIGHT_HOURS;
      iWaitingTime = IdentifyAvailResAndAssignWorkToTechScenarioData.MAX_WAIT_TIME_IN_MS;
      iWorkPackageNo = iWorkPackageQueriesDriver.getWorkPackageNoByWorkPackageName( iWorkPackage );
   }


   @Given( "^I have Work Packages$" )
   public void iHaveWorkPackages() throws Throwable {
      iNavigationDriver.navigate( iProductionController, iProductionControllerToDoList );
      iToDoListPageDriver.clickTabAssignedWorkList();
      iToDoListPageDriver.getTabAssignedWorkList().clickWorkPackageInTable( iWorkPackage );
   }


   @Given( "^the Tasks in the Work Packages have Skills$" )
   public void theTasksInTheWorkPackagesHaveSkills() throws Throwable {
      iCheckDetailsPageDriver.clickTabLabor();
      assertEquals( iLaborSkill,
            iCheckDetailsPageDriver.getTabLabor().getLaborSkill( iAssignedTask ) );

   }


   @Given( "^I have a Crew$" )
   public void iHaveACrew() throws Throwable {
      iCheckDetailsPageDriver.clickTabDetails().clickWorkLocation();
      iLocationDetailsPageDriver.clickTabDepartments();
      iLocationDetailsPageDriver.getTabDepartments().clickDepartment( iDepartment );
   }


   @Given( "^I have a Crew which has a Technician$" )
   public void iHaveACrewWhichHasATechnician() throws Throwable {
      iCheckDetailsPageDriver.clickTabDetails().clickWorkLocation();
      iLocationDetailsPageDriver.clickTabDepartments();
      iLocationDetailsPageDriver.getTabDepartments().clickDepartment( iDepartment );
      iDepartmentDetailsPageDriver.clickTabUsers();
      iDepartmentDetailsPageDriver.getTabUsers().clickMember( iTechnician );
      iUserDetailsPageDriver.clickTabSchedule();
      iUserDetailsPageDriver.getTabSchedule().clickAddShiftPattern();
      iAddEditHrUserShiftPatternPageDriver.setLocation( iLocation );
      iAddEditHrUserShiftPatternPageDriver.setUserShiftPattern( iUserShiftPattern );
      iAddEditHrUserShiftPatternPageDriver.setPrimarySkill( iLaborSkill );
      iAddEditHrUserShiftPatternPageDriver.setStartDate( iStartDate );
      iAddEditHrUserShiftPatternPageDriver.setEndDate( iEndDate );
      iAddEditHrUserShiftPatternPageDriver.clickOk();
      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( iWaitingTime );
      iUserDetailsPageDriver.clickOK();
   }


   @Given( "^the Work Packages are Assigned to my Crew$" )
   public void theWorkPackagesAreAssignedToMyCrew() throws Throwable {

      iDepartmentDetailsPageDriver.clickOkButton();
      iLocationDetailsPageDriver.clickOK();

      iCheckDetailsPageDriver.clickPlanShift();
      iPlanShiftPageDriver.clickSearch();
      iPlanShiftPageDriver.getTabSearchResults().clickSelectAll();
      iPlanShiftPageDriver.getTabSearchResults().clickAssignCrew();
      iAssignCrewPageDriver.clickCheckBoxForCrew( iCrewCode );

      iPlanShiftPageDriver.clickTabSearchResults();
      assertTrue( "Task " + iAssignedTask + " is not assigned to Crew " + iCrewCode,
            iPlanShiftPageDriver.getTabSearchResults().isTaskAssignedToCrew( iAssignedTask,
                  iCrewCode ) );
   }


   @When( "^I review the list of work assigned to my Crew$" )
   public void iReviewTheListOfWorkAssignedToMyCrew() throws Throwable {
      iNavigationDriver.navigate( iHeavyTechnician, iHeavyTechnicianToDoList );
      iToDoListPageDriver.clickTabAssignedToYourCrew();
   }


   @Then( "^the list of Tasks is visible$" )
   public void theListOfTasksIsVisible() throws Throwable {
      assertTrue(
            "Task " + iAssignedTask + " is not assigned to your crew (" + iCrewCode
                  + ") in Work Package " + iWorkPackageNo + ".",
            iToDoListPageDriver.getTabAssignedToYourCrew()
                  .isAssignedToYourCrewInWorkPackage( iCrewCode, iAssignedTask, iWorkPackageNo ) );
   }


   @Then( "^the Labor Skills are visible$" )
   public void theLaborSkillsAreVisible() throws Throwable {
      assertTrue( "Labor Skill " + iLaborSkill + " is not assigned to your crew (" + iCrewCode
            + ") for the Task (" + iAssignedTask + ") in Work Package " + iWorkPackageNo + ".",
            iToDoListPageDriver.getTabAssignedToYourCrew()
                  .isLaborSkillAssignedToYourCrewInWorkPackage( iLaborSkill, iCrewCode,
                        iAssignedTask, iWorkPackageNo ) );
   }


   @When( "^I view the details of an Assigned Task$" )
   public void iViewTheDetailsOfAnAssignedTask() throws Throwable {
      iPlanShiftPageDriver.clickOk();
      iCheckDetailsPageDriver.clickTabAssignedTasks();
      iCheckDetailsPageDriver.getTabAssignedTasks().clickTaskInAssignedTasksTable( iAssignedTask );
      iTaskDetailsPageDriver.clickTabTaskExecution();
      iTaskDetailsPageDriver.getTabTaskExecution().getLaborRow( iLaborSkill ).clickTechnician();

   }


   @And( "^I Assign the work to a Technician$" )
   public void iAssignTheWorkToATechnician() throws Throwable {
      iLabourAssignmentPageDriver.setHourNumber( iNextHours );
      iLabourAssignmentPageDriver.clickSearch();
      iLabourAssignmentPageDriver.clickTechnicianRadioButton( iTechnician );
   }


   @Then( "^the work is assigned to the Technician$" )
   public void theWorkIsAssogmedToTheTechnician() throws Throwable {
      assertTrue(
            "Skill(" + iPrimarySkill + ") and Technician(" + iTechnician + ") NOT in Labor Table.",
            iTaskDetailsPageDriver.clickTabTaskExecution().hasLaborRow( iLaborSkill,
                  iTechnician ) );
      iTaskDetailsPageDriver.clickClose();
      iCheckDetailsPageDriver.clickTabLabor();
      assertTrue(
            "Technician(" + iTechnician + ") with Skill(" + iLaborSkill
                  + ") is NOT assigned to Task(" + iAssignedTask + ").",
            iCheckDetailsPageDriver.getTabLabor().hasTask( iAssignedTask, iLaborSkill,
                  iTechnician ) );
   }
}
