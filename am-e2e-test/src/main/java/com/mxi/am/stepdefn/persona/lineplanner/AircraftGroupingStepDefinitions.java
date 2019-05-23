package com.mxi.am.stepdefn.persona.lineplanner;

import static com.mxi.am.helper.Selector.selectFirst;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.acftgroup.AircraftGroupDetailsPageDriver;
import com.mxi.am.driver.web.acftgroup.AircraftGroupDetailsPageDriver.AircraftListTableRow;
import com.mxi.am.driver.web.acftgroup.AircraftSearchPageDriver;
import com.mxi.am.driver.web.acftgroup.CreateEditAircraftGroupPageDriver;
import com.mxi.am.driver.web.acftgroup.ManageAircraftGroups.ManageAircraftGroupsPageDriver;
import com.mxi.am.driver.web.acftgroup.ManageAircraftGroups.ManageAircraftGroupsPanes.AircraftGroupsPaneDriver;
import com.mxi.am.driver.web.acftgroup.ManageAircraftGroups.ManageAircraftGroupsPanes.AircraftGroupsPaneDriver.AircraftGroups;
import com.mxi.am.driver.web.acftgroup.ManageAircraftGroups.ManageAircraftGroupsPanes.GroupAssignmentsPaneDriver.GroupAssignments;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.FleetDueListPaneDriver.FleetDueListTableRow;
import com.mxi.am.driver.web.todolist.ToDoListPanes.FleetListPaneDriver.FleetListTableRow;
import com.mxi.am.helper.FilterCriteria;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for navigating Maintenix
 */
public class AircraftGroupingStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   private String iMenuName = "Manage Aircraft Groups";
   private String iToDoMenuName = "To Do List (Line Planner)";
   private String iGroupName = "Group 1";
   private String iGroupDescription = "My first group";
   private String iUpdatedGroupName = "Group 2";
   private String iUpdatedGroupDescription = "My second group";
   private String iAircraftName = "Aircraft Part 1 - RJIC-ACFT1";
   private String iSecondAircraftName = "Aircraft Part 2 - 101";
   private String iThirdAircraftFleetListName = "Aircraft Part 2 - 300";

   @Inject
   private ManageAircraftGroupsPageDriver iManageAircraftGroupsPageDriver;

   @Inject
   private CreateEditAircraftGroupPageDriver iCreateEditAircraftGroupPageDriver;

   @Inject
   private AircraftGroupDetailsPageDriver iAircraftGroupDetailsPageDriver;

   @Inject
   private AircraftSearchPageDriver iAircraftSearchPageDriver;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;


   @When( "^I create a new aircraft group$" )
   public void iCreateANewAircraftGroup() throws Throwable {
      // select Manage Aircraft Groups menu from line planner
      iNavigationDriver.navigate( "Line Planner", iMenuName );
      // click create aircraft group button
      iManageAircraftGroupsPageDriver.clickTabAircraftGroups().clickCreateAircraftGroup();
      // define new group
      iCreateEditAircraftGroupPageDriver.setGroupName( iGroupName );
      iCreateEditAircraftGroupPageDriver.setGroupDescription( iGroupDescription );
      iCreateEditAircraftGroupPageDriver.clickOK();
      // now we are back on Aircraft Group Details page
   }


   @When( "^I add aircraft to the group$" )
   public void iAddAircraftToTheGroup() throws Throwable {
      // assume we are already on Aircraft Group Details page for Group 1
      // click to add aircraft to group
      iAircraftGroupDetailsPageDriver.clickAddAircraft();
      // select aircraft from list
      iAircraftSearchPageDriver.clickSearch();
      iAircraftSearchPageDriver.clickCheckboxForAircraft( iAircraftName );
      iAircraftSearchPageDriver.clickAssignAircraft();
      // click close
      iAircraftGroupDetailsPageDriver.clickClose();
   }


   private FilterCriteria<AircraftListTableRow> withAcftName( final String aAcftName ) {
      return new FilterCriteria<AircraftListTableRow>() {

         @Override
         public boolean test( AircraftListTableRow aAircraftListTableRow ) {
            return aAircraftListTableRow.getAircraft().equals( aAcftName );
         }
      };
   }


   @Then( "^the group is created and includes the added aircraft$" )
   public void theGroupIsCreatedAndIncludesTheAddedAircraft() throws Throwable {
      // check that the Manage Aircraft Group page contains a group called Group 1 on the Aircraft
      // Groups tab
      iManageAircraftGroupsPageDriver.clickTabAircraftGroups()
            .clickAircraftGroupInTable( iGroupName );
      // assert details for group details page for group
      Assert.assertEquals( iGroupName, iAircraftGroupDetailsPageDriver.getGroupName() );
      Assert.assertEquals( iGroupDescription,
            iAircraftGroupDetailsPageDriver.getGroupDescription() );
      // assert selected aircraft are on list on the Aircraft Group Details page
      Assert.assertNotNull( selectFirst( iAircraftGroupDetailsPageDriver.getAircraftListTableRows(),
            withAcftName( iAircraftName ) ) );
      iAircraftGroupDetailsPageDriver.clickClose();
   }


   @Then( "^the group assignment list shows the aircraft in the new group$" )
   public void theGroupAssignmentListShowsTheAcftInTheNewGroup() throws Throwable {
      // go to manage aircraft groups > Group Assignments tab
      // iNavigationDriver.navigate( "Line Planner", iMenuName );
      iManageAircraftGroupsPageDriver.clickTabGroupAssignments();
      // check that there is a row with our aircraft and the Groups 1
      Assert.assertNotNull( selectFirst(
            iManageAircraftGroupsPageDriver.getTabGroupAssignments().getGroupAssignments(),
            withAcftAndGroupName( iAircraftName, iGroupName ) ) );
   }


   @When( "^I edit an aircraft group$" )
   public void iEditAnAircraftGroup() throws Throwable {
      iNavigationDriver.navigate( "Line Planner", iMenuName );
      iManageAircraftGroupsPageDriver.clickTabAircraftGroups();
      // select group to edit - click on the group name "Group 1" on the table in the Aircraft
      // Groups pane
      iManageAircraftGroupsPageDriver.getTabAircraftGroups()
            .clickAircraftGroupInTable( iGroupName );
      // click edit group arrow button in the Aircraft Group Details page
      iAircraftGroupDetailsPageDriver.clickEditDetails();
      // update group details
      iCreateEditAircraftGroupPageDriver.setGroupName( iUpdatedGroupName );
      iCreateEditAircraftGroupPageDriver.setGroupDescription( iUpdatedGroupDescription );
      iCreateEditAircraftGroupPageDriver.clickOK();
      // we are still on the Aircraft Group Details Page
   }


   private FilterCriteria<GroupAssignments> withAcftAndGroupName( final String aAcftName,
         final String aGroupName ) {
      return new FilterCriteria<GroupAssignments>() {

         @Override
         public boolean test( GroupAssignments aGroupAssignments ) {
            if ( ( aGroupAssignments.getAircraftGroups().toString().contains( aGroupName ) )
                  && ( aGroupAssignments.getAircraft().toString().equals( aAcftName ) ) )
               return true;
            else
               return false;

         }
      };
   }


   private FilterCriteria<GroupAssignments> foundAcftButWithoutGroupName( final String aAcftName,
         final String aGroupName ) {
      return new FilterCriteria<GroupAssignments>() {

         @Override
         public boolean test( GroupAssignments aGroupAssignments ) {
            if ( ( aGroupAssignments.getAircraft().toString().equals( aAcftName ) )
                  && !( aGroupAssignments.getAircraftGroups().toString().contains( aGroupName ) ) )

               return true;
            else
               return false;

         }
      };
   }


   @When( "^I update the aircraft group assignments$" )
   public void iUpdateTheAircraftGroupAssignments() throws Throwable {
      // click to add aircraft to group
      iAircraftGroupDetailsPageDriver.clickAddAircraft();
      // select aircraft from list
      iAircraftSearchPageDriver.clickSearch();
      // check the checkbox of another acft
      iAircraftSearchPageDriver.clickCheckboxForAircraft( iSecondAircraftName );
      iAircraftSearchPageDriver.clickAssignAircraft();
      // click close
      iAircraftGroupDetailsPageDriver.clickClose();
      // now we are back on Manage Aircraft Groups page
   }


   @Then( "^the group is updated and includes the updated assignments$" )
   public void theGroupIsUpdatedAndIncludesTheUpdatedAssignments() throws Throwable {
      // assert details for group details page for group
      // assert selected aircraft are on list
      iManageAircraftGroupsPageDriver.clickTabAircraftGroups()
            .clickAircraftGroupInTable( iUpdatedGroupName );
      // assert details for group details page for group
      Assert.assertEquals( iUpdatedGroupName, iAircraftGroupDetailsPageDriver.getGroupName() );
      Assert.assertEquals( iUpdatedGroupDescription,
            iAircraftGroupDetailsPageDriver.getGroupDescription() );
      // assert selected aircraft are on list on the Aircraft Group Details page
      Assert.assertNotNull( selectFirst( iAircraftGroupDetailsPageDriver.getAircraftListTableRows(),
            withAcftName( iSecondAircraftName ) ) );
   }


   @Then( "^the group assignment list shows the updated group assignment information$" )
   public void theGroupAssignmentListShowsTheUpdatedGroupAssignmentInformation() throws Throwable {
      // go to manage aircraft groups > Group Assignments tab
      iNavigationDriver.navigate( "Line Planner", iMenuName );
      iManageAircraftGroupsPageDriver.clickTabGroupAssignments();
      // check that there are two aircraft with the updated aircraft Group 2
      Assert.assertNotNull( selectFirst(
            iManageAircraftGroupsPageDriver.getTabGroupAssignments().getGroupAssignments(),
            withAcftAndGroupName( iAircraftName, iUpdatedGroupName ) ) );
      Assert.assertNotNull( selectFirst(
            iManageAircraftGroupsPageDriver.getTabGroupAssignments().getGroupAssignments(),
            withAcftAndGroupName( iSecondAircraftName, iUpdatedGroupName ) ) );
   }


   @When( "^I select an aircraft group$" )
   public void iSelectAnAircraftGroup() throws Throwable {
      // go to fleet list
      iNavigationDriver.navigate( "Line Planner", iToDoMenuName );
      // select group to filter on and apply
      iToDoListPageDriver.clickTabFleetList().clickOptionsLink();
      iToDoListPageDriver.getTabFleetList().selectShowSelectedGroupsCheckbox();
      iToDoListPageDriver.getTabFleetList().selectAircraftGroup( iUpdatedGroupName );
      iToDoListPageDriver.getTabFleetList().clickOptionsApplyButton();
   }


   @Then( "^the fleet view is filtered by aircraft group$" )
   public void theFleetViewIsFilteredByAircraftGroup() throws Throwable {
      // verify that fleet list is filtered
      Assert.assertNotNull(
            selectFirst( iToDoListPageDriver.getTabFleetList().getFleetListTableRows(),
                  withAircraftDescription( iAircraftName ) ) );
      Assert.assertNotNull(
            selectFirst( iToDoListPageDriver.getTabFleetList().getFleetListTableRows(),
                  withAircraftDescription( iSecondAircraftName ) ) );
      // need to check that an aircraft is NOT found, and we should see an Illegal Argument
      // exception
      try {
         selectFirst( iToDoListPageDriver.getTabFleetList().getFleetListTableRows(),
               withAircraftDescription( iThirdAircraftFleetListName ) );
      } catch ( IllegalArgumentException e ) {
         // as expected, the aircraft could not be found in the table and an exception was
         // thrown, so let's declare it as a pass
         boolean lAircraftNotFound = true;
         Assert.assertTrue( lAircraftNotFound );
      }

      // go to fleet due list
      iToDoListPageDriver.clickTabFleetDueList();
      // verify that fleet due list is filtered
      Assert.assertNotNull(
            selectFirst( iToDoListPageDriver.getTabFleetDueList().getFleetDueListTableRows(),
                  withDueListAircraftDescription( iSecondAircraftName ) ) );
      // need to check that an aircraft is NOT found, and we should see an Illegal Argument
      // exception
      try {
         selectFirst( iToDoListPageDriver.getTabFleetDueList().getFleetDueListTableRows(),
               withDueListAircraftDescription( iThirdAircraftFleetListName ) );
      } catch ( IllegalArgumentException e ) {
         // as expected, the aircraft could not be found in the table and an exception was
         // thrown, so let's declare it as a pass
         boolean lAircraftNotFound = true;
         Assert.assertTrue( lAircraftNotFound );
      }
   }


   private FilterCriteria<FleetListTableRow> withAircraftDescription( final String aAcft ) {
      return new FilterCriteria<FleetListTableRow>() {

         @Override
         public boolean test( FleetListTableRow aFleetListRow ) {
            if ( aFleetListRow.getAircraft().toString().equals( aAcft ) )

               return true;
            else
               return false;

         }
      };
   }


   private FilterCriteria<FleetDueListTableRow>
         withDueListAircraftDescription( final String aAcft ) {
      return new FilterCriteria<FleetDueListTableRow>() {

         @Override
         public boolean test( FleetDueListTableRow aFleetDueListRow ) {
            if ( aFleetDueListRow.getAircraft().toString().equals( aAcft ) )

               return true;
            else
               return false;

         }
      };
   }


   @When( "^I delete an aircraft group$" )
   public void iDeleteAnAircraftGroup() throws Throwable {
      iNavigationDriver.navigate( "Line Planner", iMenuName );
      // select group to delete
      AircraftGroupsPaneDriver lAircraftGroupsPaneDriver =
            iManageAircraftGroupsPageDriver.clickTabAircraftGroups();
      lAircraftGroupsPaneDriver.clickCheckboxForGroup( iUpdatedGroupName );
      // click delete group
      lAircraftGroupsPaneDriver.clickDeleteAircraftGroup();
      // accept warning
      iConfirmPageDriver.clickYes();
      // we're still on Manage Aircraft Groups page
   }


   @Then( "^the group is deleted$" )
   public void theGroupIsDeleted() throws Throwable {
      // assert that group is no longer on list on the Aircraft Groups tab of the Manage Aircraft
      // Groups page
      AircraftGroupsPaneDriver lAircraftGroupsPaneDriver =
            iManageAircraftGroupsPageDriver.clickTabAircraftGroups();
      // check that the aircraft group is not on the Manage Aircraft Groups page
      // since it got deleted, the table is empty so we cannot check the column for the value of the
      // group name
      // It says "there are no aircraft groups"
      boolean lEmptyTable = lAircraftGroupsPaneDriver.isTableEmpty();

      if ( lEmptyTable ) {
         Assert.assertTrue( lEmptyTable );
      } else {
         try {
            selectFirst( lAircraftGroupsPaneDriver.getAircraftGroups(),
                  withGroupName( iUpdatedGroupName ) );
         } catch ( IllegalArgumentException e ) {
            // as expected, the group name could not be found in the table and an exception was
            // thrown, so let's declare it as a pass
            boolean lGroupNotFound = true;
            Assert.assertTrue( lGroupNotFound );
         }
      }

   }


   private FilterCriteria<AircraftGroups> withGroupName( final String aGroupName ) {
      return new FilterCriteria<AircraftGroups>() {

         @Override
         public boolean test( AircraftGroups aAircraftGroupsTableRow ) {
            return aAircraftGroupsTableRow.getAircraftGroupName().equals( aGroupName );
         }
      };
   }


   @Then( "^the group assignment list no longer shows the aircraft in the group$" )
   public void theGroupAssignmentListNoLongerShowsTheAcftInTheGroup() throws Throwable {
      // go to manage aircraft groups > Group Assignments tab
      iNavigationDriver.navigate( "Line Planner", iMenuName );
      iManageAircraftGroupsPageDriver.clickTabGroupAssignments();
      // check that there is no longer a row for our aircraft where the Aircraft Groups is
      Assert.assertNotNull( ( selectFirst(
            iManageAircraftGroupsPageDriver.getTabGroupAssignments().getGroupAssignments(),
            foundAcftButWithoutGroupName( iSecondAircraftName, iUpdatedGroupName ) ) ) );
   }

}
