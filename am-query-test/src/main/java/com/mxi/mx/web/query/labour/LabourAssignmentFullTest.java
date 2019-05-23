package com.mxi.mx.web.query.labour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgHrShiftKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.ShiftPlanKey;
import com.mxi.mx.core.table.org.OrgHrShift;
import com.mxi.mx.core.table.org.OrgHrShiftPlan;
import com.mxi.mx.core.table.org.OrgHrShiftPlanDao;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

public final class LabourAssignmentFullTest {

   private static final SchedLabourKey LABOUR_1 = new SchedLabourKey( 4650, 1 );
   private static final SchedLabourKey LABOUR_2 = new SchedLabourKey( 4650, 2 );
   private static final HumanResourceKey USER_1 = new HumanResourceKey( 4650, 1 );
   private static final HumanResourceKey USER_2 = new HumanResourceKey( 4650, 2 );
   private static final HumanResourceKey USER_3 = new HumanResourceKey( 4650, 3 );
   private static final HumanResourceKey USER_FOR_TEMP_CREW = new HumanResourceKey( 4650, 123 );
   private static final LocationKey LOCATION_1 = new LocationKey( 4650, 1 );
   private static final LocationKey LOCATION_2 = new LocationKey( 4650, 2 );
   private static final LocationKey STANDARD_CREW_LOCATION = new LocationKey( 4650, 999 );

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), LabourAssignmentFullTest.class );
   }


   /**
    * Tests the assigned labour requirement configurations
    */
   @Test
   public void testAssignedLabourRequirements() throws Exception {
      DataSet lData = execute( LABOUR_1, RefLabourSkillKey.LBR, LOCATION_1, 24, false, false );

      assertTrue( lData.next() );
      assertRow( lData, USER_1, false, true, 0, 0.0, null, 0.0, 0.0 );

      assertTrue( lData.next() );
      assertRow( lData, USER_2, false, false, 1, 1.0, null, 0.0, 0.0 );

      assertFalse( lData.next() );

      lData = execute( LABOUR_2, RefLabourSkillKey.LBR, LOCATION_2, 24, false, false );

      assertTrue( lData.next() );
      assertRow( lData, USER_1, false, true, 0, 0.0, null, 0.0, 0.0 );

      assertTrue( lData.next() );
      assertRow( lData, USER_3, false, false, 0, 0.0, null, 0.0, 0.0 );

      assertFalse( lData.next() );
   }


   /**
    * Tests the query has not returned the locked user (4650,106)
    */
   @Test
   public void testLockedUserNotListed() throws Exception {
      DataSet lData = execute( LABOUR_2, RefLabourSkillKey.LBR, LOCATION_2, 24, false, false );

      // Assert for No of Rows
      assertEquals( "Number of retrieved rows", 2, lData.getRowCount() );

      // row 1 with a active user
      assertTrue( lData.next() );
      assertEquals( USER_1, lData.getKey( HumanResourceKey.class, 1 ) );

      // row 2 with a active user
      assertTrue( lData.next() );
      assertEquals( USER_3, lData.getKey( HumanResourceKey.class, 1 ) );

      // the row with the locked user (4650,106) is not selected by the query
      assertFalse( lData.next() );
   }


   /**
    * Tests the user qualification configurations
    */
   @Test
   public void testQualification() throws Exception {
      DataSet lData = execute( LABOUR_1, RefLabourSkillKey.LBR, LOCATION_1, 24, false, true );

      assertTrue( lData.next() );
      assertRow( lData, USER_1, false, true, 0, 0.0, null, 0.0, 0.0 );

      assertFalse( lData.next() );

      lData = execute( LABOUR_1, RefLabourSkillKey.LBR, LOCATION_2, 24, false, true );

      assertTrue( lData.next() );
      assertRow( lData, USER_1, false, true, 0, 0.0, null, 0.0, 0.0 );

      assertFalse( lData.next() );
   }


   /**
    * Tests when user has a standard crew, then this user should show in the user to assign list of
    * the user's standard crew location
    */
   @Test
   public void userHasStandardCrew_showAsScheduledResources() {
      Date systemDate = new Date();
      OrgHrShiftPlanDao orgHrShiftPlanDao =
            InjectorContainer.get().getInstance( OrgHrShiftPlanDao.class );

      OrgHrShiftPlan orgHrShiftPlanTable =
            orgHrShiftPlanDao.findByPrimaryKey( new ShiftPlanKey( 4650, 123, 1 ) );
      orgHrShiftPlanTable.setDayDt( systemDate );
      orgHrShiftPlanDao.update( orgHrShiftPlanTable );

      DataSet lData =
            execute( LABOUR_1, RefLabourSkillKey.LBR, STANDARD_CREW_LOCATION, 9, true, false );

      assertTrue( lData.next() );
      assertRow( lData, USER_FOR_TEMP_CREW, false, false, 0, 0.0, systemDate, 8.0, 32.0 );
   }


   /**
    * Tests when user who has the labour requirement skill and and was added a shift adjustment with
    * a temporary crew:<br>
    * - This temporary crew has the location where the work package was scheduled to;</br>
    * - The shift adjustment has valid date, shift start hour and duration:</br>
    * 1> date + start hour + duration >= system date (for this test: today+8h+8h> today)</br>
    * 2> date + start hour <= system date + Show Assignment Details for the Next X hours user input
    * from UI (for this test: today+8h<= today+9h)
    *
    * This user will show in the user to assign list when user clicked "Show Only Scheduled
    * Resources at this Location" and should not show in the list of the standard crew location
    */
   @Test
   public void userHasValidShiftAdjustmentWithValidTemporaryCrew_showAsScheduledResources() {
      // Add shift adjustment with a temporary crew in LOCATION_1
      OrgHrShift lOrgHrShift = OrgHrShift.findByPrimaryKey( new OrgHrShiftKey( 4650, 123, 1 ) );
      lOrgHrShift.setCrew( new DepartmentKey( 4650, 321 ) );
      lOrgHrShift.setLocationKey( LOCATION_1 );
      lOrgHrShift.update();

      // Add system date to the org_hr_shift_plan table to satisfy the window of "Show Only
      // Scheduled Resources at this Location"
      Date systemDate = new Date();
      OrgHrShiftPlanDao orgHrShiftPlanDao =
            InjectorContainer.get().getInstance( OrgHrShiftPlanDao.class );
      OrgHrShiftPlan orgHrShiftPlanTable =
            orgHrShiftPlanDao.findByPrimaryKey( new ShiftPlanKey( 4650, 123, 1 ) );
      orgHrShiftPlanTable.setDayDt( systemDate );
      orgHrShiftPlanDao.update( orgHrShiftPlanTable );

      // Assert that the user is in the list of temporary crew location
      DataSet lTemporaryCrewLocationData =
            execute( LABOUR_1, RefLabourSkillKey.LBR, LOCATION_1, 9, true, false );
      assertTrue( lTemporaryCrewLocationData.next() );
      assertRow( lTemporaryCrewLocationData, USER_FOR_TEMP_CREW, false, false, 0, 0.0, systemDate,
            8.0, 32.0 );

      // Assert that the user is not in the list of standard crew location
      DataSet lStandardCrewLocationData =
            execute( LABOUR_1, RefLabourSkillKey.LBR, STANDARD_CREW_LOCATION, 9, true, false );
      assertFalse( lStandardCrewLocationData.next() );
   }


   /**
    * Asserts the database row is equal to expected data
    *
    * @param aData
    *           The Data
    * @param aHumanResourceKey
    *           The human resource key
    * @param aLicensed
    *           TRUE is licensed
    * @param aQualified
    *           TRUE if qualified
    * @param aTasksCurrentlyAssignedComp
    *           Amount of labour requirements assigned in time interval
    * @param aSchedHrComp
    *           Amount of hours from the labour requirements assigned
    * @param aDay
    *           the shift date
    * @param aStartTime
    *           the shift start time
    * @param aEndTime
    *           the shift end time
    */
   private void assertRow( DataSet aData, HumanResourceKey aHumanResourceKey, boolean aLicensed,
         boolean aQualified, int aTasksCurrentlyAssignedComp, double aSchedHrComp, Date aDay,
         double aStartTime, double aEndTime ) {
      assertEquals( aHumanResourceKey, aData.getKey( HumanResourceKey.class, "hr_key" ) );
      assertEquals( aLicensed, aData.getBoolean( "licensed" ) );
      assertEquals( aQualified, aData.getBoolean( "qualified" ) );
      assertEquals( aTasksCurrentlyAssignedComp, aData.getInt( "tasks_currently_assigned_comp" ) );
      assertEquals( aSchedHrComp, aData.getDouble( "sched_hr_comp" ), 0 );
      if ( aDay != null ) {
         assertTrue( DateUtils.truncatedEquals( aDay, aData.getDate( "day_dt" ), 0 ) );
      }
      assertEquals( aStartTime, aData.getDouble( "start_time" ), 0 );
      assertEquals( aEndTime, aData.getDouble( "end_time" ), 0 );
   }


   private DataSet execute( SchedLabourKey aSchedLabourKey, RefLabourSkillKey aSkillKey,
         LocationKey aLocation, int aHours, boolean aShowOnlySchedHr,
         boolean aShowOnlyQualifiedHr ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSkillCd", aSkillKey.getCd() );
      lArgs.add( aLocation, "aLocDbId", "aLocId" );
      lArgs.add( "aHours", aHours );
      lArgs.add( aSchedLabourKey, "aSchedLaborDbId", "aSchedLaborId" );

      if ( aShowOnlySchedHr ) {
         lArgs.addWhere( "SHOW_ONLY_SCHED_HR_CLAUSE", "valid_shifts.hr_db_id IS NOT NULL" );
      } else {
         lArgs.addWhere( "SHOW_ONLY_SCHED_HR_CLAUSE", "1 = 1" );
      }

      if ( aShowOnlyQualifiedHr ) {
         lArgs.addWhere( "SHOW_ONLY_QUALIFIED_HR_CLAUSE", "qualified_hr.hr_db_id IS NOT NULL" );
      } else {
         lArgs.addWhere( "SHOW_ONLY_QUALIFIED_HR_CLAUSE", "1 = 1" );
      }

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
