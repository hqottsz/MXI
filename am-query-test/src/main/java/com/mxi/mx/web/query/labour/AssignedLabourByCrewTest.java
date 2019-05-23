package com.mxi.mx.web.query.labour;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.DepartmentBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrgHrShiftBuilder;
import com.mxi.am.domain.builder.ShiftDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDeptTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UserKey;


/**
 * This class tests the query com.mxi.mx.web.query.labour.AssignedLabourByCrew.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AssignedLabourByCrewTest {

   private static final UserKey USER_ID = new UserKey( 100352 );

   private static final String TEMP_CREW_CODE = "TEMPCREWCODE";

   private static final String PERM_CREW_CODE = "PERMCREWCODE";

   private static final String LOC_CODE = "YYZ/LINE";

   private static final String TASK_NAME = "Task Name";

   private static final String TASK_BARCODE = "Task Barcode";

   private static final String AIRCRAFT = "Aircraft";

   private static final Double SCHED_HR = 2.0;

   private static final String WORK_PACKAGE_NAME = "Work Package Name";

   private Date TODAY = new Date();

   private HumanResourceKey iHr;

   private LocationKey iLocationKey;

   private ShiftKey iShiftKey;

   private InventoryKey iAircraft;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * Tests work package is assigned to a permanent crew
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testWorkPackageAssignedToPermCrew() throws Exception {

      // DATA SETUP: Create a crew and assign the user as a permanent crew member
      DepartmentKey lCrew = assignPermanentCrew();

      // DATA SETUP: Create a task with no crew assigned to it
      TaskKey lTask = createTask( null );

      // DATA SETUP: Create a work package and assign the user's permanent crew to it
      createWorkPackage( lTask, lCrew );

      DataSet lResult = execute();

      Assert.assertEquals( 1, lResult.getRowCount() );

      lResult.next();

      assertResult( lResult, true, lCrew, false );
   }


   /**
    * Tests work package is assigned to a temporary crew
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testWorkPackageAssignedToTempCrew() throws Exception {

      // DATA SETUP: Create a crew and assign the user as a temporary crew member
      DepartmentKey lCrew = assignTemporaryCrew( null );

      // DATA SETUP: Create a task with no crew assigned to it
      TaskKey lTask = createTask( null );

      // DATA SETUP: Create a work package and assign the user's temporary crew to it
      createWorkPackage( lTask, lCrew );

      DataSet lResult = execute();

      Assert.assertEquals( 1, lResult.getRowCount() );

      lResult.next();

      assertResult( lResult, true, lCrew, true );
   }


   /**
    * Tests task is assigned to a permanent crew
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testTaskAssignedToPermCrew() throws Exception {

      // DATA SETUP: Create a crew and assign the user as a permanent crew member
      DepartmentKey lCrew = assignPermanentCrew();

      // DATA SETUP: Create a task and assign the user's permanent crew to it
      TaskKey lTask = createTask( lCrew );

      // DATA SETUP: Create a work package with no crew assign to it
      createWorkPackage( lTask, null );

      DataSet lResult = execute();

      Assert.assertEquals( 1, lResult.getRowCount() );

      lResult.next();

      assertResult( lResult, false, lCrew, false );
   }


   /**
    * Tests task is assigned to a temporary crew
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testTaskAssignedToTempCrew() throws Exception {

      // DATA SETUP: Create a crew and assign the user as a temporary crew member
      DepartmentKey lCrew = assignTemporaryCrew( null );

      // DATA SETUP: Create a task and assign the user's temporary crew to it
      TaskKey lTask = createTask( lCrew );

      // DATA SETUP: Create a work package with no crew assigned
      createWorkPackage( lTask, null );

      DataSet lResult = execute();

      Assert.assertEquals( 1, lResult.getRowCount() );

      lResult.next();

      assertResult( lResult, false, lCrew, true );
   }


   /**
    * Tests work package is assigned to a crew and one of the tasks is assigned to another crew
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testTaskAssignedToDiffentCrewThanWorkPackage() throws Exception {

      // ***************************************************************************
      // Test 1:
      // Work package is assigned to the user's permanent crew
      // Task is assigned to another crew that user does not belong to
      // Result:
      // No row returns
      // ***************************************************************************

      // DATA SETUP: Create a crew and assign the user as a permanent crew member
      DepartmentKey lPermanentCrew = assignPermanentCrew();

      // DATA SETUP: Create another crew and assign no one to it
      DepartmentKey lAnotherCrew = createCrew( null, "ANOTHER CREW" );

      // DATA SETUP: Create a task and assign to a crew that the user does not belong to.
      TaskKey lTask = createTask( lAnotherCrew );

      // DATA SETUP: Create a work package and assign it to the user's permanent crew
      createWorkPackage( lTask, lPermanentCrew );

      DataSet lResult = execute();

      Assert.assertEquals( 0, lResult.getRowCount() );

      // ***************************************************************************
      // Test 2:
      // Work package is assigned to the user's permanent crew
      // Task is assigned to the user's other permanent crew
      // Result:
      // The crew that is assigned to the task takes precedence
      // ***************************************************************************

      // DATA SETUP: Create a crew and assign the user as a permanent crew member
      DepartmentKey lPermanentCrew2 = assignPermanentCrew();

      // DATA SETUP: Create a task and assign to the user's second permanent crew
      TaskKey lTask2 = createTask( lPermanentCrew2 );

      // DATA SETUP: Create a work package and assign it to the user's permanent crew
      createWorkPackage( lTask2, lPermanentCrew );

      lResult = execute();

      Assert.assertEquals( 1, lResult.getRowCount() );

      lResult.next();

      assertResult( lResult, false, lPermanentCrew2, false );
   }


   /**
    * Tests user is assigned to a temporary crew that is the same as their permanent crew and the
    * work package and task are assigned to the same crew.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testUserWithTheSamePermAndTempCrew() throws Exception {

      // DATA SETUP: Create a crew and assign the user as a permanent crew member
      DepartmentKey lPermanentCrew = assignPermanentCrew();

      // DATA SETUP: Create a temporary crew assignment for the user who has the same crew as their
      // permanent crew
      assignTemporaryCrew( lPermanentCrew );

      // DATA SETUP: Create a task and assign to a crew that the user does not belong to.
      TaskKey lTask = createTask( lPermanentCrew );

      // DATA SETUP: Create a work package and assign it to the user's permanent crew
      createWorkPackage( lTask, lPermanentCrew );

      DataSet lResult = execute();

      Assert.assertEquals( 1, lResult.getRowCount() );

      lResult.next();

      assertResult( lResult, false, lPermanentCrew, false );
   }


   private void assertResult( DataSet aResultDs, boolean aIsWpCrew, DepartmentKey aCrew,
         boolean aIsTempCrew ) {

      Assert.assertEquals( aIsWpCrew, aResultDs.getBoolean( "crew_assigned_to_wp" ) );

      String lCrewCode = PERM_CREW_CODE;

      if ( aIsTempCrew ) {
         lCrewCode = TEMP_CREW_CODE;
      }

      if ( aResultDs.getBoolean( "crew_assigned_to_wp" ) ) {
         Assert.assertEquals( lCrewCode + "," + aCrew.toString(),
               aResultDs.getString( "dept_key" ) );
      } else {
         Assert.assertEquals( aCrew, aResultDs.getKey( DepartmentKey.class, "dept_key" ) );
         Assert.assertEquals( lCrewCode, aResultDs.getString( "crew_cd" ) );
      }

      Assert.assertEquals( WORK_PACKAGE_NAME, aResultDs.getString( "wp_name" ) );
      Assert.assertEquals( RefLabourSkillKey.ENG, aResultDs.getString( "labour_skill_cd" ) );
      Assert.assertEquals( TASK_NAME, aResultDs.getString( "event_sdesc" ) );
      Assert.assertEquals( TASK_BARCODE, aResultDs.getString( "barcode_sdesc" ) );
      Assert.assertEquals( AIRCRAFT, aResultDs.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( LOC_CODE, aResultDs.getString( "loc_cd" ) );
      Assert.assertEquals( SCHED_HR, aResultDs.getDouble( "sched_hr" ), 0 );
   }


   private DepartmentKey assignPermanentCrew() {

      return createCrew( iHr, PERM_CREW_CODE );
   }


   private DepartmentKey assignTemporaryCrew( DepartmentKey aDepartment ) {

      // Create a crew department
      DepartmentKey lCrewKey = aDepartment;

      if ( lCrewKey == null ) {
         lCrewKey = createCrew( iHr, TEMP_CREW_CODE );
      }

      // Assign crew as a temporary crew for user
      new OrgHrShiftBuilder().withHumanResourceKey( iHr ).withDay( new Date() )
            .withShiftKey( iShiftKey ).withLabourSkill( RefLabourSkillKey.ENG )
            .withDepartmentKey( lCrewKey ).build();

      return lCrewKey;
   }


   private DepartmentKey createCrew( HumanResourceKey aHr, String aCrewCd ) {

      DepartmentBuilder lDepartmentBuilder = new DepartmentBuilder( aCrewCd )
            .withLocation( iLocationKey ).withType( RefDeptTypeKey.CREW );

      if ( aHr != null ) {
         lDepartmentBuilder.withUser( aHr );
      }

      return lDepartmentBuilder.build();
   }


   private TaskKey createWorkPackage( final TaskKey aTask, final DepartmentKey aCrew ) {

      TaskKey lWorkPackageKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( iAircraft );
            aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );
            aWorkPackage.setLocation( iLocationKey );
            aWorkPackage.addTask( aTask );
            aWorkPackage.setName( WORK_PACKAGE_NAME );

            if ( aCrew != null ) {
               aWorkPackage.addCrew( aCrew );
            }
         }
      } );

      return lWorkPackageKey;
   }


   private TaskKey createTask( DepartmentKey aCrew ) {

      TaskBuilder lTaskBuilder =
            new TaskBuilder().onInventory( iAircraft ).withLabour( RefLabourSkillKey.ENG, 2.0 )
                  .withScheduledStart( TODAY ).withBarcode( TASK_BARCODE ).withName( TASK_NAME );

      if ( aCrew != null ) {
         lTaskBuilder.withCrew( aCrew );
      }

      return lTaskBuilder.build();
   }


   /**
    * This method executes the query in AssignedLabourByCrew.qrx
    *
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iHr, "aHrDbId", "aHrId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   @Before
   public void setup() {

      // Create an Hr
      iHr = new HumanResourceDomainBuilder().withUserId( USER_ID.getId() ).build();

      // Create a shift
      iShiftKey = new ShiftDomainBuilder().withShiftCd( "Shift" ).build();

      // Create a location
      iLocationKey =
            new LocationDomainBuilder().withCode( LOC_CODE ).withType( RefLocTypeKey.LINE ).build();

      // Create an aircraft
      iAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setDescription( AIRCRAFT );
         }
      } );
   }
}
