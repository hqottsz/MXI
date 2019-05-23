package com.mxi.am.stepdefn.persona.linetechnician;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.common.configurationParameters.ActionConfigurationParameterDriver;
import com.mxi.am.driver.query.HumanResourceQueriesDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


/**
 * Step definitions for assigning temporary roles.
 */
public class ActivateTemporaryRoleStepDefinitions {

   public static final String ROLE_ASSIGNEE_USER = "linetech";

   public static final String ROLE_ASSIGNMENT_CODE = "HVYLEAD";
   public static final int ROLE_ASSIGNMENT_ID = 10007;

   @Inject
   HumanResourceQueriesDriver iHRDriver;

   @Inject
   ActionConfigurationParameterDriver iActionParmDriver;


   @Given( "^Line technician has a temporary role assigned$" )
   public void lineTechnicianHasATemporaryRole() throws Throwable {

      Date lToday = new Date();
      Date lTomorrow = new Date( lToday.getTime() + 24 * 60 * 60 * 1000 );

      if ( !iActionParmDriver.isTempRoleAssignmentConfigured() ) {
         iActionParmDriver.setTempRoleAssignmentConfigured( true );
      }
      if ( !iActionParmDriver.isAccessTempRoleAssignmentConfigured() ) {
         iActionParmDriver.setAccessTempRoleAssignmentConfigured( true );
      }

      iHRDriver.clearTempRoles();
      iHRDriver.assignTempRole( ROLE_ASSIGNEE_USER, ROLE_ASSIGNMENT_ID, lToday, lTomorrow );

   }


   @Then( "^Line technician has the active role$" )
   public void lineTechnicianHasTheActiveRole() throws Throwable {

      // Log in to refresh the user's active roles and verify.

      List<String> lRoles = iHRDriver.getActiveRoles( ROLE_ASSIGNEE_USER );

      Assert.assertTrue( lRoles.contains( ROLE_ASSIGNMENT_CODE ) );
   }


   @Then( "^Line technician does not have the active role$" )
   public void theLineTechnicianNoLongerHasTheTemporaryRole() throws Throwable {

      // Log in to refresh the user's active roles and verify.

      List<String> lRoles = iHRDriver.getActiveRoles( ROLE_ASSIGNEE_USER );

      Assert.assertFalse( lRoles.contains( ROLE_ASSIGNMENT_CODE ) );
   }

}
