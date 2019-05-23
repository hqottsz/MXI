
package com.mxi.am.stepdefn;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.AssemblyListPaneDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step Definitions for navigating Maintenix
 */
public class AssemblyListStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   @Inject
   private AssemblyListPaneDriver iAssemblyListPaneDriver;


   @When( "^I go to Assembly List tab$" )
   public void i_go_to_Assembly_List_tab() throws Throwable {
      iToDoListPageDriver.clickTabAssemblyList();
   }


   @When( "^I click assembly \"([^\"]*)\"$" )
   public void selectAssembly( String aAssemblyCode ) throws Throwable {
      iAssemblyListPaneDriver.clickAssembly( aAssemblyCode );
   }


   @Then( "^I should be on page \"([^\"]*)\" for assembly \"([^\"]*)\"$" )
   public void i_should_be_on_page_for_assembly( String aTitle, String aSubTitle )
         throws Throwable {
      Assert.assertEquals( aTitle, iNavigationDriver.getTitle() );
      Assert.assertEquals( aSubTitle, iNavigationDriver.getSubTitle() );
   }

}
