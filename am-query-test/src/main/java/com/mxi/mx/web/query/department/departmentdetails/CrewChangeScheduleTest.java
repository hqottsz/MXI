package com.mxi.mx.web.query.department.departmentdetails;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.DepartmentBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.OrgHrShiftBuilder;
import com.mxi.am.domain.builder.ShiftDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDeptTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query
 * com.mxi.mx.web.query.department.departmentdetails.CrewChangeSchedule.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CrewChangeScheduleTest {

   private static final String ANOTHER_CREW = "ANOTHER_CREW";

   private static final UserKey PERM_CREW_USER = new UserKey( 100352 );

   private static final UserKey TEMP_CREW_USER = new UserKey( 100353 );

   private static final String TEMP_FIRST_NAME = "Temporary";

   private static final String LAST_NAME = "Crew";

   private static final String PERM_FIRST_NAME = "Permanent";

   private static final String SHIFT_NAME = "Shift";

   private static DepartmentKey iAnotherCrewDept;

   private static ShiftKey iShift;

   private static DepartmentKey iHomeCrewDept;

   private static HumanResourceKey iTempCrewHr;

   private static HumanResourceKey iPermCrewHr;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Test
   public void testCrewChangeSchedule() throws Exception {

      dataSetup();

      Date lNow = new Date();

      // Assign a temporary crew member
      new OrgHrShiftBuilder().withHumanResourceKey( iTempCrewHr ).withDay( lNow )
            .withShiftKey( iShift ).withLabourSkill( RefLabourSkillKey.ENG )
            .withDepartmentKey( iHomeCrewDept ).build();

      // Assign a permanent crew member to another crew department
      new OrgHrShiftBuilder().withHumanResourceKey( iPermCrewHr ).withDay( lNow )
            .withShiftKey( iShift ).withLabourSkill( RefLabourSkillKey.PILOT )
            .withDepartmentKey( iAnotherCrewDept ).build();

      DataSet lResult = execute( false );

      Assert.assertEquals( 2, lResult.getRowCount() );

      lResult.next();

      // Assert that a temporary user has been assigned to the crew
      Assert.assertEquals( "FROM", lResult.getString( "direction" ) );
      MxAssert.assertEquals( lNow, lResult.getDate( "day_dt" ) );
      Assert.assertEquals( LAST_NAME + ", " + TEMP_FIRST_NAME, lResult.getString( "user_name" ) );
      Assert.assertEquals( SHIFT_NAME, lResult.getString( "shift_name" ) );
      Assert.assertEquals( iAnotherCrewDept, lResult.getKey( DepartmentKey.class, "crew_key" ) );
      Assert.assertEquals( RefLabourSkillKey.ENG.getCd(), lResult.getString( "labour_skill_cd" ) );

      lResult.next();

      // Assert the permanent crew member has been assigned to another crew
      Assert.assertEquals( "JOINING", lResult.getString( "direction" ) );
      MxAssert.assertEquals( lNow, lResult.getDate( "day_dt" ) );
      Assert.assertEquals( LAST_NAME + ", " + PERM_FIRST_NAME, lResult.getString( "user_name" ) );
      Assert.assertEquals( SHIFT_NAME, lResult.getString( "shift_name" ) );
      Assert.assertEquals( iAnotherCrewDept, lResult.getKey( DepartmentKey.class, "crew_key" ) );
      Assert.assertEquals( RefLabourSkillKey.PILOT.getCd(),
            lResult.getString( "labour_skill_cd" ) );

   }


   @Test
   public void testCrewChangeScheduleFilter() throws Exception {

      dataSetup();

      Date lNow = new Date();

      // Assign a temporary crew member for three days
      new OrgHrShiftBuilder().withHumanResourceKey( iTempCrewHr ).withDay( lNow )
            .withShiftKey( iShift ).withLabourSkill( RefLabourSkillKey.ENG )
            .withDepartmentKey( iHomeCrewDept ).build();

      new OrgHrShiftBuilder().withHumanResourceKey( iTempCrewHr )
            .withDay( DateUtils.addDays( lNow, 1 ) ).withShiftKey( iShift )
            .withLabourSkill( RefLabourSkillKey.ENG ).withDepartmentKey( iHomeCrewDept ).build();

      new OrgHrShiftBuilder().withHumanResourceKey( iTempCrewHr )
            .withDay( DateUtils.addDays( lNow, 2 ) ).withShiftKey( iShift )
            .withLabourSkill( RefLabourSkillKey.ENG ).withDepartmentKey( iHomeCrewDept ).build();

      // No filter
      DataSet lResult = execute( false );

      Assert.assertEquals( 3, lResult.getRowCount() );

      // Apply Filter
      lResult = execute( true );

      Assert.assertEquals( 1, lResult.getRowCount() );
   }


   @Test
   public void testUserTempAssignedToTheirPermCrew() throws Exception {

      dataSetup();

      // Assign user as a temporary crew member to their permanent crew
      new OrgHrShiftBuilder().withHumanResourceKey( iPermCrewHr ).withDay( new Date() )
            .withShiftKey( iShift ).withLabourSkill( RefLabourSkillKey.ENG )
            .withDepartmentKey( iHomeCrewDept ).build();

      DataSet lResult = execute( false );

      Assert.assertEquals( 0, lResult.getRowCount() );
   }


   private void dataSetup() {

      // Create Hr
      iPermCrewHr = new HumanResourceDomainBuilder().withUserId( PERM_CREW_USER.getId() )
            .withFirstName( PERM_FIRST_NAME ).withLastName( LAST_NAME ).build();

      iTempCrewHr = new HumanResourceDomainBuilder().withUserId( TEMP_CREW_USER.getId() )
            .withFirstName( TEMP_FIRST_NAME ).withLastName( LAST_NAME ).build();

      // Create location
      LocationKey lLocation = new LocationKey( 4650, 1 );

      // Create a home crew department
      iHomeCrewDept = new DepartmentBuilder( "HOME CREW" ).withLocation( lLocation )
            .withUser( iPermCrewHr ).withType( RefDeptTypeKey.CREW ).build();

      // Create another crew department
      iAnotherCrewDept = new DepartmentBuilder( ANOTHER_CREW ).withLocation( lLocation )
            .withUser( iTempCrewHr ).withType( RefDeptTypeKey.CREW ).build();

      iShift = new ShiftDomainBuilder().withShiftCd( SHIFT_NAME ).build();
   }


   /**
    * This method executes the query in CrewChangeSchedule.qrx
    *
    *
    * @return The dataset after execution.
    */
   private DataSet execute( boolean aApplyFilter ) {

      Date lToday = new Date();
      Date lEndDate = null;

      if ( aApplyFilter ) {
         lEndDate = lToday;
      }

      DataSetArgument lArg = new DataSetArgument();
      lArg.add( iHomeCrewDept, "aDeptDbId", "aDeptId" );
      lArg.add( "aStartDate", lToday );
      lArg.add( "aEndDate", lEndDate );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArg );
   }

}
