package com.mxi.mx.web.query.shift;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.ibm.icu.util.Calendar;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.CrewShiftScheduleBuilder;
import com.mxi.am.domain.builder.DepartmentBuilder;
import com.mxi.am.domain.builder.HrShiftScheduleBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.RoleBuilder;
import com.mxi.am.domain.builder.ShiftDomainBuilder;
import com.mxi.am.domain.builder.ShiftScheduleBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgCrewShiftPlanKey;
import com.mxi.mx.core.key.RefDeptTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RoleKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.utl.UtlUserTempRole.ColumnName;
import com.mxi.mx.testing.DataRecordUtil;


/**
 * This class tests the query com.mxi.mx.web.query.shift.CrewShiftDayCapacity.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CrewShiftDayCapacityTest {

   private static final Double SHIFT_DURATION = 8.0;

   private static final Double SHIFT_START_HOUR = 6.0;

   private static final Double SCHEDULE_WINDOW = 96.0;

   private static final String CREW_CODE = "EDCREW001";
   private static final String OK_CREW_CODE = "OKCREW001";

   private static final String LOC_CODE = "YYZ";

   private static final Double TASK_SCHED_HOURS = 5.0;

   private static final String SHIFT_CD = "EDCREW001DAY";

   private static final UserKey USER_ID = new UserKey( 100352 );
   private static final UserKey OUTGOING_USER_ID = new UserKey( 100353 );

   private static final String CONFIG_PARM = "INCLUDE_IN_WORKING_CAPACITY";

   private static final Double CREW_SHIFT_START_HOUR = 6.0;

   private static final Double CREW_SHIFT_DURATION = 8.0;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey iHr;
   private HumanResourceKey iHrTwo;

   private LocationKey iWorkLocation;

   private DepartmentKey iCrewKey;

   private TaskKey iTask;

   private DepartmentKey iOKCrewKey;


   /**
    *
    * Build the crew shift schedule.
    *
    * @param aStartHour
    *           shift start
    * @param aDurationHours
    *           shift duration
    * @param aStartDate
    *           schedule start date
    * @param aEndDate
    *           schedule end date
    */
   private CrewShiftScheduleBuilder getCrewShiftScheduleBuilder( double aStartHour,
         double aDurationHours, Date aStartDate, Date aEndDate ) {
      // Data Setup: Create Crew Shift
      ShiftDomainBuilder lCrewShiftDay = new ShiftDomainBuilder().withShiftCd( SHIFT_CD )
            .withStartHour( aStartHour ).withDurationInHours( aDurationHours );

      ShiftKey lCrewShift = lCrewShiftDay.build();

      ShiftScheduleBuilder lCrewScheduleBuilder = new CrewShiftScheduleBuilder()
            .withCrew( iCrewKey ).withShift( lCrewShift ).withStartHour( aStartHour )
            .withDurationInHours( aDurationHours ).forDateRange( aStartDate, aEndDate );

      return ( CrewShiftScheduleBuilder ) lCrewScheduleBuilder;
   }


   /**
    * Tests the calculation of capacity with INCLUDE_IN_WORKING_CAPACITY config parm
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testIncludeInWorkingCapacityConfigParm() throws Exception {

      Date lTempRoleStartDate = DateUtils.addDays( new Date(), -3 );
      Date lTempRoleEndDate = DateUtils.addDays( new Date(), 3 );

      // Data Setup: Add temporary role to user with config parm set to FALSE so that it is excluded
      // from capacity calculation
      RoleKey lTempRoleKey = new RoleBuilder().withUser( USER_ID ).isTemporary()
            .withConfigParm( CONFIG_PARM, false ).withTempRoleStartDate( lTempRoleStartDate )
            .withTempRoleEndDate( lTempRoleEndDate ).build();

      Date lCrewShiftDate = new Date();

      // Data Setup: Create Crew Shift which start: 6am and end: 2pm
      getCrewShiftScheduleBuilder( CREW_SHIFT_START_HOUR, CREW_SHIFT_DURATION, lCrewShiftDate,
            lCrewShiftDate ).build();

      // ***************************************************************************************************
      // Test 1: User has a temporary role that has config parm INCLUDE_IN_WORKING_CAPACITY set to
      // FALSE so that the capacity calculation does not include the HR work hours
      // ***************************************************************************************************

      Double lHrWorkHours = 8.0;

      // Data Setup: Create HR Shift which start: 6am and end:2pm
      new HrShiftScheduleBuilder().withHr( iHr ).withStartHour( CREW_SHIFT_START_HOUR )
            .withDurationInHours( CREW_SHIFT_DURATION ).withWorkHours( lHrWorkHours )
            .forDateRange( lCrewShiftDate, lCrewShiftDate ).build();

      // execute query
      DataSet lDs = this.execute( null );

      // assert that one row returns
      Assert.assertEquals( 1, lDs.getRowCount() );

      lDs.next();

      CrewShiftCapacityRecord lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iCrewKey, 1 ) )
            .withDeptCd( CREW_CODE ).withStartHour( CREW_SHIFT_START_HOUR ).withShiftCd( SHIFT_CD )
            .withEndHour( CREW_SHIFT_START_HOUR + CREW_SHIFT_DURATION ).withTotalCapacity( 0.0 )
            .withAvailCapacity( 0.0 );

      CrewShiftCapacityRecord lActualResults = new CrewShiftCapacityRecord( lDs );

      lExpectedResults.assertEquals( lActualResults );

      // ***************************************************************************************************
      // Test 2: Unassign the temporary role from the user so that the capacity calculation includes
      // the HR work hours
      // ***************************************************************************************************

      // DATA SETUP: Unassign the temporary role from user
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( ColumnName.UNASSIGNED_BY.name(), 111 );
      lSetArgs.add( ColumnName.UNASSIGNED_DT.name(), new Date() );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( "ROLE_ID", lTempRoleKey.getRoleId() );
      lWhereArgs.add( "USER_ID", USER_ID.getId() );

      MxDataAccess.getInstance().executeUpdate( "UTL_USER_TEMP_ROLE", lSetArgs, lWhereArgs );

      MxDataAccess.getInstance().executeDelete( "UTL_USER_ROLE", lWhereArgs );

      // execute query
      lDs = this.execute( null );

      // assert that one row returns
      Assert.assertEquals( 1, lDs.getRowCount() );

      lDs.next();

      lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iCrewKey, 1 ) )
            .withDeptCd( CREW_CODE ).withStartHour( CREW_SHIFT_START_HOUR ).withShiftCd( SHIFT_CD )
            .withEndHour( CREW_SHIFT_START_HOUR + CREW_SHIFT_DURATION )
            .withTotalCapacity( lHrWorkHours ).withAvailCapacity( lHrWorkHours );

      lActualResults = new CrewShiftCapacityRecord( lDs );

      lExpectedResults.assertEquals( lActualResults );

      // ***************************************************************************************************
      // Test 3: User has a temporary role that has config parm INCLUDE_IN_WORKING_CAPACITY set to
      // TRUE so that the capacity calculation includes the HR work hours
      // ***************************************************************************************************

      // Data Setup: Add temporary role to user with config parm set to FALSE so that it is excluded
      // from capacity calculation
      lTempRoleKey = new RoleBuilder().withUser( USER_ID ).isTemporary()
            .withConfigParm( CONFIG_PARM, true ).build();

      // execute query
      lDs = this.execute( null );

      // assert that one row returns
      Assert.assertEquals( 1, lDs.getRowCount() );

      lDs.next();

      lActualResults = new CrewShiftCapacityRecord( lDs );

      // should have the same results as the previous test
      lExpectedResults.assertEquals( lActualResults );

      // ***************************************************************************************************
      // Test 4: User has a permanent role that has config parm INCLUDE_IN_WORKING_CAPACITY set to
      // FALSE so that the capacity calculation does not include the HR work hours
      // ***************************************************************************************************

      // Data Setup: Add permanent role to user with config parm set to FALSE so that it is excluded
      // from capacity calculation
      new RoleBuilder().withUser( USER_ID ).withConfigParm( CONFIG_PARM, false ).build();

      // execute query
      lDs = this.execute( null );

      // assert that one row returns
      Assert.assertEquals( 1, lDs.getRowCount() );

      lDs.next();

      lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iCrewKey, 1 ) )
            .withDeptCd( CREW_CODE ).withStartHour( CREW_SHIFT_START_HOUR ).withShiftCd( SHIFT_CD )
            .withEndHour( CREW_SHIFT_START_HOUR + CREW_SHIFT_DURATION ).withTotalCapacity( 0.0 )
            .withAvailCapacity( 0.0 );

      lActualResults = new CrewShiftCapacityRecord( lDs );

      lExpectedResults.assertEquals( lActualResults );
   }


   /**
    * Tests the calculation of capacity for a crew shift day that is off
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCrewDayOff() throws Exception {

      // Data Setup: Create Crew Shift which start: 6am and end: 2pm
      Date lCrewShiftDate = new Date();

      Calendar lCalendar = Calendar.getInstance();
      lCalendar.setTime( lCrewShiftDate );

      getCrewShiftScheduleBuilder( CREW_SHIFT_START_HOUR, CREW_SHIFT_DURATION, lCrewShiftDate,
            lCrewShiftDate ).withTask( iTask, lCrewShiftDate )
                  .withDayOff( lCalendar.get( Calendar.DAY_OF_WEEK ) ).build();

      // execute query
      DataSet lDs = this.execute( null );

      // assert that no row returns
      Assert.assertEquals( 0, lDs.getRowCount() );
   }


   /**
    * Tests the calculation of capacity for a crew that has an HR that left for another crew.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCrewShiftDayCapacity_outgoingHR() throws Exception {

      // ********************************************************
      // Data Setup: Create a Crew Shift that
      // 1) does not have any capacity .
      // ********************************************************

      // Data Setup: Create Crew Shift which start: 6am and end: 2pm
      Date lCrewShiftDate = new Date();

      // Data Setup: Create Crew Shift
      ShiftDomainBuilder lCrewShiftDay = new ShiftDomainBuilder().withShiftCd( SHIFT_CD )
            .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION );

      ShiftKey lCrewShift = lCrewShiftDay.build();

      ShiftScheduleBuilder lCrewScheduleBuilder =
            new CrewShiftScheduleBuilder().withCrew( iOKCrewKey ).withShift( lCrewShift )
                  .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION )
                  .forDateRange( lCrewShiftDate, lCrewShiftDate );
      lCrewScheduleBuilder.build();

      // ********************************************************************************
      // Test Case 1: Outgoing HR - Crew Shift has an HR assigned who can not contribute to the crew
      // shift as he moved to a different crew
      // ********************************************************************************

      // Data Setup: Create a HR with the same shift , start: 6am and end: 2pm and assign to a
      // different crew: outgoing HR
      Double lHrWorkHours = 7.0;

      ShiftScheduleBuilder lWorkerScheduleBuilder = new HrShiftScheduleBuilder().withHr( iHrTwo )
            .withLocation( iWorkLocation ).withCrew( iCrewKey )
            .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION )
            .withWorkHours( lHrWorkHours ).forDateRange( lCrewShiftDate, lCrewShiftDate );
      lWorkerScheduleBuilder.build();

      // execute query
      DataSet lDs = this.execute( iHrTwo );
      lDs.next();

      Assert.assertEquals( 1, lDs.getRowCount() );

      CrewShiftCapacityRecord lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iOKCrewKey, 1 ) )
            .withDeptCd( OK_CREW_CODE ).withStartHour( CREW_SHIFT_START_HOUR )
            .withShiftCd( SHIFT_CD ).withEndHour( CREW_SHIFT_START_HOUR + CREW_SHIFT_DURATION )
            .withTotalCapacity( 0.0 ).withAvailCapacity( 0.0 );

      lDs.next();

      CrewShiftCapacityRecord lActualResults = new CrewShiftCapacityRecord( lDs );

      lExpectedResults.assertEquals( lActualResults );

   }


   /**
    * Tests the calculation of capacity for a crew that has an HR that joining the crew from another
    * crew.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCrewShiftDayCapacity_incomingHR() throws Exception {

      // ********************************************************
      // Data Setup: Create a Crew Shift that
      // 1) does not have any capacity .
      // ********************************************************

      // Data Setup: Create Crew Shift which start: 6am and end: 2pm
      Date lCrewShiftDate = new Date();

      // Data Setup: Create Crew Shift
      ShiftDomainBuilder lShiftDay = new ShiftDomainBuilder().withShiftCd( SHIFT_CD )
            .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION );

      ShiftKey lShift = lShiftDay.build();

      ShiftScheduleBuilder lCrewScheduleBuilder =
            new CrewShiftScheduleBuilder().withCrew( iOKCrewKey ).withShift( lShift )
                  .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION )
                  .forDateRange( lCrewShiftDate, lCrewShiftDate );
      lCrewScheduleBuilder.build();

      // ********************************************************************************
      // Test Case 1: Incoming HR - Crew Shift has an HR assigned who can contribute to the crew
      // shift as he joins the crew
      // ********************************************************************************

      // Data Setup: Create a HR with the same shift , start: 6am and end: 2pm and assign to the
      // crew: incoming HR
      Double lHrWorkHours = 3.0;

      ShiftScheduleBuilder lWorkerScheduleBuilder = new HrShiftScheduleBuilder().withHr( iHr )
            .withLocation( iWorkLocation ).withCrew( iOKCrewKey )
            .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION )
            .withWorkHours( lHrWorkHours ).forDateRange( lCrewShiftDate, lCrewShiftDate );
      lWorkerScheduleBuilder.build();

      // execute query
      DataSet lDs = this.execute( null );

      Assert.assertEquals( 1, lDs.getRowCount() );

      CrewShiftCapacityRecord lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iOKCrewKey, 1 ) )
            .withDeptCd( OK_CREW_CODE ).withStartHour( CREW_SHIFT_START_HOUR )
            .withShiftCd( SHIFT_CD ).withEndHour( CREW_SHIFT_START_HOUR + CREW_SHIFT_DURATION )
            .withTotalCapacity( lHrWorkHours ).withAvailCapacity( lHrWorkHours );
      lDs.next();

      CrewShiftCapacityRecord lActualResults = new CrewShiftCapacityRecord( lDs );
      lExpectedResults.assertEquals( lActualResults );

   }


   /**
    * Tests the calculation of capacity for a crew that has an HR that working just with home crew
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCrewShiftDayCapacity_HomeCrewHR() throws Exception {

      // ********************************************************
      // Data Setup: Create a Crew Shift that
      // 1) does not have any capacity .
      // ********************************************************

      // Data Setup: Create Crew Shift which start: 6am and end: 2pm
      Date lCrewShiftDate = new Date();

      // Data Setup: Create Crew Shift
      ShiftDomainBuilder lShiftDay = new ShiftDomainBuilder().withShiftCd( SHIFT_CD )
            .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION );

      ShiftKey lShift = lShiftDay.build();

      ShiftScheduleBuilder lCrewScheduleBuilder =
            new CrewShiftScheduleBuilder().withCrew( iCrewKey ).withShift( lShift )
                  .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION )
                  .forDateRange( lCrewShiftDate, lCrewShiftDate );
      lCrewScheduleBuilder.build();

      // ********************************************************************************
      // Test Case 1: Home Crew HR - Crew Shift has an HR scheduled for work at the home crew only
      // ********************************************************************************

      // Data Setup: Create a HR with the same shift , start: 6am and end: 2pm
      Double lHrWorkHours = 7.0;

      ShiftScheduleBuilder lWorkerScheduleBuilder =
            new HrShiftScheduleBuilder().withHr( iHr ).withLocation( iWorkLocation )
                  .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION )
                  .withWorkHours( lHrWorkHours ).forDateRange( lCrewShiftDate, lCrewShiftDate );
      lWorkerScheduleBuilder.build();

      // execute query
      DataSet lDs = this.execute( null );

      Assert.assertEquals( 1, lDs.getRowCount() );

      CrewShiftCapacityRecord lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iCrewKey, 1 ) )
            .withDeptCd( CREW_CODE ).withStartHour( CREW_SHIFT_START_HOUR ).withShiftCd( SHIFT_CD )
            .withEndHour( CREW_SHIFT_START_HOUR + CREW_SHIFT_DURATION )
            .withTotalCapacity( lHrWorkHours ).withAvailCapacity( lHrWorkHours );
      lDs.next();

      CrewShiftCapacityRecord lActualResults = new CrewShiftCapacityRecord( lDs );
      lExpectedResults.assertEquals( lActualResults );

   }


   /**
    * Tests the calculation of capacity for a normal shift.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCrewShiftDayCapacity() throws Exception {

      // ********************************************************
      // Test Case 1: Create a Crew Shift that
      // 1) does not have any capacity .
      // 2) has a task assigned to it
      // ********************************************************

      // Data Setup: Create Crew Shift which start: 6am and end: 2pm
      Date lCrewShiftDate = new Date();

      getCrewShiftScheduleBuilder( CREW_SHIFT_START_HOUR, CREW_SHIFT_DURATION, lCrewShiftDate,
            lCrewShiftDate ).withTask( iTask, lCrewShiftDate ).build();

      // execute query
      DataSet lDs = this.execute( null );

      // assert that one row returns
      Assert.assertEquals( 1, lDs.getRowCount() );

      lDs.next();

      CrewShiftCapacityRecord lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iCrewKey, 1 ) )
            .withDeptCd( CREW_CODE ).withStartHour( CREW_SHIFT_START_HOUR ).withShiftCd( SHIFT_CD )
            .withEndHour( CREW_SHIFT_START_HOUR + CREW_SHIFT_DURATION ).withTotalCapacity( 0.0 )
            .withAvailCapacity( -TASK_SCHED_HOURS );

      CrewShiftCapacityRecord lActualResults = new CrewShiftCapacityRecord( lDs );

      lExpectedResults.assertEquals( lActualResults );

      // ********************************************************************************
      // Test Case 2: Crew Shift has an HR assigned who can contribute to the crew shift
      // ********************************************************************************

      // Data Setup: Create a HR with the same shift as the crew, start: 6am and end: 2pm
      Double lHrWorkHours = 7.0;

      ShiftScheduleBuilder lWorkerScheduleBuilder = new HrShiftScheduleBuilder().withHr( iHr )
            .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION )
            .withWorkHours( lHrWorkHours ).forDateRange( lCrewShiftDate, lCrewShiftDate );
      lWorkerScheduleBuilder.build();

      // execute query
      lDs = this.execute( iHr );

      Assert.assertEquals( 1, lDs.getRowCount() );

      lDs.next();

      lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iCrewKey, 1 ) )
            .withDeptCd( CREW_CODE ).withStartHour( CREW_SHIFT_START_HOUR ).withShiftCd( SHIFT_CD )
            .withEndHour( CREW_SHIFT_START_HOUR + CREW_SHIFT_DURATION )
            .withTotalCapacity( lHrWorkHours ).withAvailCapacity( lHrWorkHours - TASK_SCHED_HOURS );

      lActualResults = new CrewShiftCapacityRecord( lDs );

      lExpectedResults.assertEquals( lActualResults );

      // *********************************************************************************
      // Test Case 3: Crew Shift has an HR assigned who is OFF during the crew shift time
      // *********************************************************************************

      Calendar lCalendar = Calendar.getInstance();
      lCalendar.setTime( lCrewShiftDate );

      // Data Setup: Create a HR who is OFF during crew shift time
      lWorkerScheduleBuilder = new HrShiftScheduleBuilder().withHr( iHr )
            .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( CREW_SHIFT_DURATION )
            .withWorkHours( lHrWorkHours ).withDayOff( lCalendar.get( Calendar.DAY_OF_WEEK ) )
            .forDateRange( lCrewShiftDate, lCrewShiftDate );
      lWorkerScheduleBuilder.build();

      // execute query
      lDs = this.execute( iHr );

      Assert.assertEquals( 1, lDs.getRowCount() );

      lDs.next();

      lActualResults = new CrewShiftCapacityRecord( lDs );

      // expected result should not change from previous test case since this user is off and
      // therefore cannot contribute anything to the crew shift
      lExpectedResults.assertEquals( lActualResults );

      // *********************************************************************************
      // Test Case 4: HR has a shift with 0 hour duration
      // *********************************************************************************

      // Data Setup: Create a HR with 0 hour duration shift
      lWorkerScheduleBuilder = new HrShiftScheduleBuilder().withHr( iHr )
            .withStartHour( CREW_SHIFT_START_HOUR ).withDurationInHours( 0.0 )
            .withWorkHours( lHrWorkHours ).forDateRange( lCrewShiftDate, lCrewShiftDate );
      lWorkerScheduleBuilder.build();

      // execute query
      lDs = this.execute( iHr );

      Assert.assertEquals( 1, lDs.getRowCount() );

      lDs.next();

      lActualResults = new CrewShiftCapacityRecord( lDs );

      // expected result should not change from previous test case since this HR shift has 0 hour
      // duration therefore cannot contribute anything to the crew shift
      lExpectedResults.assertEquals( lActualResults );
   }


   /**
    * Tests 'My Crews Only' filter
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testMyCrewsOnlyFilter() throws Exception {

      // Data Setup: Create Crew Shift which start: 6am and end: 2pm
      Date lCrewShiftDate = new Date();

      getCrewShiftScheduleBuilder( CREW_SHIFT_START_HOUR, CREW_SHIFT_DURATION, lCrewShiftDate,
            lCrewShiftDate ).build();

      // execute query
      DataSet lDs = this.execute( null );

      // assert that one row returns
      Assert.assertEquals( 1, lDs.getRowCount() );

      // execute query
      lDs = this.execute( new HumanResourceKey( 4650, -1 ) );

      // assert that no rows return
      Assert.assertEquals( 0, lDs.getRowCount() );

      // execute query
      lDs = this.execute( iHr );

      // assert that one row returns
      Assert.assertEquals( 1, lDs.getRowCount() );
   }


   /**
    * Tests the calculation of capacity for a shift with staggered worker work times.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCrewShiftDayCapacityWithOffsetWorker() throws Exception {

      /**
       * With one worker in the shift, the total capacity should be the worker's working hours per
       * shift.
       *
       * On worker days off, the total capacity should be zero.
       *
       * On crew shift days off, no result should be found for that day.
       */

      Date lStartDate = new Date();

      Date lEndDate = DateUtils.addDays( lStartDate, 2 );

      // Data Setup: Create Crew Shift which start: 6am and end: 2pm
      getCrewShiftScheduleBuilder( CREW_SHIFT_START_HOUR, CREW_SHIFT_DURATION, lStartDate,
            lEndDate ).build();

      // ********************************************************
      // Test Case: Crew Shift has a worker assigned one hour before start
      // ********************************************************

      // Data Setup: Create a worker with start: 5am and end: 1pm
      Double lWorkHours = 7.0;

      ShiftScheduleBuilder lWorkerScheduleBuilder = new HrShiftScheduleBuilder().withHr( iHr )
            .withStartHour( CREW_SHIFT_START_HOUR - 1 ).withDurationInHours( CREW_SHIFT_DURATION )
            .withWorkHours( lWorkHours ).forDateRange( lStartDate, lEndDate );
      lWorkerScheduleBuilder.build();

      DataSet lDataSet = this.execute( iHr );

      CrewShiftCapacityRecord lExpectedResults = new CrewShiftCapacityRecord()
            .withOrgCrewShiftPlanKey( new OrgCrewShiftPlanKey( iCrewKey, 1 ) )
            .withDeptCd( CREW_CODE ).withStartHour( SHIFT_START_HOUR ).withShiftCd( SHIFT_CD )
            .withEndHour( SHIFT_START_HOUR + SHIFT_DURATION ).withTotalCapacity( 6.125 )
            .withAvailCapacity( 6.125 );
      lDataSet.next();

      CrewShiftCapacityRecord lActualResults = new CrewShiftCapacityRecord( lDataSet );

      lExpectedResults.assertEquals( lActualResults );

   }


   /**
    * Tests the calculation of capacity with crew overnight shift
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Ignore
   @Test
   public void testCrewOvernightShift() throws Exception {

      Date lStartDate = new Date();

      lStartDate = DateUtils.addDays( lStartDate, 1 );

      Date lEndDate = DateUtils.addDays( lStartDate, 2 );

      // Data Setup: Create Crew Shift which start: 8pm and end: 8am
      Double lCrewShiftStartHour = 22.0;
      Double lCrewShiftDuration = 8.0;

      getCrewShiftScheduleBuilder( lCrewShiftStartHour, lCrewShiftDuration, lStartDate, lEndDate )
            .build();

      // ********************************************************
      // Test Case: Crew Shift has a worker assigned on next day
      // ********************************************************

      // Data Setup: Create a worker with start: 1am and end: 9am
      Double lWorkHours = 7.0;

      ShiftScheduleBuilder lWorkerScheduleBuilder = new HrShiftScheduleBuilder().withHr( iHr )
            .withStartHour( 3.0 ).withDurationInHours( lCrewShiftDuration )
            .withWorkHours( lWorkHours ).forDateRange( new Date(), lEndDate );
      lWorkerScheduleBuilder.build();

      DataSet lDataSet = this.execute( iHr );

      double lExpectedCapacity = 3 * ( lWorkHours / lCrewShiftDuration );

      CrewShiftCapacityRecord lExpectedResults =
            new CrewShiftCapacityRecord().withCrewKey( iCrewKey ).withDeptCd( CREW_CODE )
                  .withStartHour( lCrewShiftStartHour ).withShiftCd( SHIFT_CD )
                  .withEndHour( ( lCrewShiftStartHour + lCrewShiftDuration ) % 24 )
                  .withTotalCapacity( lExpectedCapacity ).withAvailCapacity( lExpectedCapacity );
      lDataSet.next();

      CrewShiftCapacityRecord lActualResults = new CrewShiftCapacityRecord( lDataSet );
      lExpectedResults.assertEquals( lActualResults );

   }


   /**
    * Tests the calculation of capacity with a hr overnight shift
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHrOvernightShift() throws Exception {

      Date lStartDate = new Date();

      lStartDate = DateUtils.addDays( lStartDate, 1 );

      Date lEndDate = DateUtils.addDays( lStartDate, 2 );

      // Data Setup: Create Crew Shift which start: 6am and end: 6pm
      Double lCrewShiftStartHour = 6.0;
      Double lCrewShiftDuration = 12.0;

      getCrewShiftScheduleBuilder( lCrewShiftStartHour, lCrewShiftDuration, lStartDate, lEndDate )
            .build();

      // ********************************************************
      // Test Case: Crew Shift has a worker assigned on previous day
      // ********************************************************

      // Data Setup: Create a worker with start: 11pm and end: 7am
      Double lWorkHours = 7.0;

      ShiftScheduleBuilder lWorkerScheduleBuilder = new HrShiftScheduleBuilder().withHr( iHr )
            .withStartHour( 21.0 ).withDurationInHours( lCrewShiftDuration )
            .withWorkHours( lWorkHours ).forDateRange( new Date(), lEndDate );
      lWorkerScheduleBuilder.build();

      DataSet lDataSet = this.execute( iHr );

      double lExpectedCapacity = 3.0 * ( lWorkHours / lCrewShiftDuration );

      CrewShiftCapacityRecord lExpectedResults = new CrewShiftCapacityRecord()
            .withCrewKey( iCrewKey ).withDeptCd( CREW_CODE ).withStartHour( lCrewShiftStartHour )
            .withShiftCd( SHIFT_CD ).withEndHour( lCrewShiftStartHour + lCrewShiftDuration )
            .withTotalCapacity( lExpectedCapacity ).withAvailCapacity( lExpectedCapacity );
      lDataSet.next();

      CrewShiftCapacityRecord lActualResults = new CrewShiftCapacityRecord( lDataSet );
      lExpectedResults.assertEquals( lActualResults );

   }


   /**
    * This method executes the query in CrewShiftDayCapacity.qrx
    *
    *
    * @return The dataset after execution.
    */
   private DataSet execute( HumanResourceKey aHr ) {

      DataSetArgument lArg = new DataSetArgument();
      lArg.add( iWorkLocation, "aLocDbId", "aLocId" );
      lArg.add( "aHours", SCHEDULE_WINDOW );
      lArg.add( aHr, "aHrDbId", "aHrId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArg );
   }


   @Before
   public void setup() {

      // Create a task
      iTask = new TaskBuilder().withLabour( RefLabourSkillKey.ENG, TASK_SCHED_HOURS ).build();

      // Create an Hr
      iHr = new HumanResourceDomainBuilder().withUserId( USER_ID.getId() ).build();
      iHrTwo = new HumanResourceDomainBuilder().withUserId( OUTGOING_USER_ID.getId() ).build();

      // Create work location
      iWorkLocation =
            new LocationDomainBuilder().withCode( LOC_CODE ).withType( RefLocTypeKey.LINE ).build();
      // Create a crew department at the work location and assign the user
      iCrewKey = new DepartmentBuilder( CREW_CODE ).withUser( iHr ).withLocation( iWorkLocation )
            .withType( RefDeptTypeKey.CREW ).build();

      iOKCrewKey = new DepartmentBuilder( OK_CREW_CODE ).withUser( iHrTwo )
            .withLocation( iWorkLocation ).withType( RefDeptTypeKey.CREW ).build();

   }


   /**
    *
    * Print out the current status of the tables involved.
    *
    * Add this function to a try catch block when AssertionError occurs.
    *
    * Do not leave calls in the tests when not debugging!
    *
    * @param aDs
    *           DataSet to print, can be null
    * @param ae
    *           AssertionError thrown, can be null
    * @throws AssertionError
    *            if an AssertionError was passed.
    */
   protected void debug( DataSet aDs, AssertionError ae ) throws AssertionError {
      DataRecordUtil.setConnection( iDatabaseConnectionRule.getConnection() );
      if ( aDs != null ) {
         DataRecordUtil.debugDataSet( aDs );
      }
      DataRecordUtil.debugTable( "org_dept_hr" );
      DataRecordUtil.debugTable( "shift_shift" );
      DataRecordUtil.debugTable( "inv_loc_dept" );
      DataRecordUtil.debugTable( "org_work_dept" );
      DataRecordUtil.debugTable( "org_crew_shift_plan" );
      DataRecordUtil.debugTable( "org_hr_shift_plan" );
      DataRecordUtil.debugTable( "utl_role" );
      DataRecordUtil.debugTable( "evt_event" );
      DataRecordUtil.debugTable( "org_crew_shift_task" );
      DataRecordUtil.debugTable( "UTL_USER_TEMP_ROLE" );
      if ( ae != null ) {
         throw ae;
      }
   }

}
