package com.mxi.mx.core.plsql.delegates;

import static com.mxi.am.domain.Domain.createWorkPackage;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.EventRelationshipBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.core.unittest.table.evt.EvtSchedDead;


/**
 * This class tests methods in the DeadlineProcedureDelegate.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DeadlineProcedureDelegateTest {

   private DeadlineProcedureDelegate iDeadlineProcedureDelegate;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that wen a deadline is requested that would go beyond the maximum date in Oracle
    * (9999/12/31), that the date returned is the second last day (due to rounding).
    *
    * @throws Exception
    *            If an error occurs
    */
   @SuppressWarnings( "deprecation" )
   @Test
   public void testFindForecastedDeadlineWithCrazyUsage() throws Exception {

      FcModelKey lForecastModel = Domain.createForecastModel();

      InventoryKey lAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT )
            .withForecastModel( lForecastModel ).build();

      Date lDeadline = iDeadlineProcedureDelegate.findForecastedDeadline(
            new AircraftKey( lAircraft ), DataTypeKey.CYCLES, 9999999d, new Date() );

      Calendar lCal = Calendar.getInstance();
      lCal.setTime( new Date() );
      lCal.add( Calendar.YEAR, 199 );
      Date lexpectedDate = lCal.getTime();

      // Expected date should be before lDeadline
      MxAssert.assertTrue( lexpectedDate.before( lDeadline ) );
   }


   /**
    * If an offwing task is created, the replacement task created to remove the component is
    * expected to have the same deadline as the offwing task. When the offwing task tracks a usage
    * parameter that the replacement tasks's inventory does not track, we must use a calendar
    * deadline to mimic the offwing task's deadline. Ensure the calendar deadline exists and that it
    * has the same deadline date.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatUntrackedUsageDeadlineOnOffwingTaskIsCopiedAsCalendarDeadline()
         throws Exception {
      Calendar lCal = Calendar.getInstance();
      lCal.set( 2013, 9, 29, 23, 59, 59 );

      final Date lDeadline = lCal.getTime();

      InventoryKey lAircraftKey = new InventoryBuilder().build();

      final InventoryKey lEngineKey =
            new InventoryBuilder().withParentInventory( lAircraftKey ).build();

      TaskKey lOffWingTask =
            new TaskBuilder().onInventory( lEngineKey ).withCalendarDeadline( lDeadline ).build();

      TaskKey lWorkPackageKey = createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lEngineKey );
         }
      } );

      TaskTaskKey lReplTaskDefnRevision = new TaskRevisionBuilder().build();
      TaskKey lReplTaskKey = new TaskBuilder().onInventory( lAircraftKey )
            .withTaskRevision( lReplTaskDefnRevision ).build();

      // build the WORMVL relationship
      new EventRelationshipBuilder().fromEvent( lReplTaskKey ).toEvent( lWorkPackageKey )
            .withType( RefRelationTypeKey.WORMVL ).build();

      // build the DRVTASK relationship
      new EventRelationshipBuilder().fromEvent( lWorkPackageKey ).toEvent( lOffWingTask )
            .withType( RefRelationTypeKey.DRVTASK ).build();

      iDeadlineProcedureDelegate.updateWormvlDeadline( lWorkPackageKey );

      EvtSchedDead lOffWingTaskDeadline =
            new EvtSchedDead( lOffWingTask.getEventKey(), DataTypeKey.HOURS );

      EvtSchedDead lReplCdyDeadline =
            new EvtSchedDead( lReplTaskKey.getEventKey(), DataTypeKey.CDY );
      lReplCdyDeadline.assertExist();
      lReplCdyDeadline.assertSchedDeadDt( lOffWingTaskDeadline.getDeadlineDate() );
   }


   /**
    * If an offwing task is created, the replacement task created to remove the component is
    * expected to have the same deadline as the offwing task. When the offwing task tracks a usage
    * parameter that the replacement tasks's inventory does not track, we must use a calendar
    * deadline to mimic the offwing task's deadline. If the deadline date does not exist, create a
    * new one dated Today at 23:59:59. Ensure the calendar deadline exists and that it has a
    * deadline date of Today at 23:59:59.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatUntrackedUsageDeadlineWithoutDateOnOffwingTaskIsCopiedAsCalendarDeadline()
         throws Exception {

      Calendar lExpectedDeadline = Calendar.getInstance();
      lExpectedDeadline.set( Calendar.HOUR_OF_DAY, 23 );
      lExpectedDeadline.set( Calendar.MINUTE, 59 );
      lExpectedDeadline.set( Calendar.SECOND, 59 );

      final Date lDeadline = lExpectedDeadline.getTime();

      InventoryKey lAircraftKey = new InventoryBuilder().build();

      final InventoryKey lEngineKey =
            new InventoryBuilder().withParentInventory( lAircraftKey ).build();

      TaskKey lOffWingTask =
            new TaskBuilder().onInventory( lEngineKey ).withCalendarDeadline( lDeadline ).build();

      // to mimic a task that has never had a deadline date set, let's remove
      // the deadline date
      EvtSchedDeadTable lTable =
            EvtSchedDeadTable.findByPrimaryKey( lOffWingTask.getEventKey(), DataTypeKey.HOURS );
      lTable.setDeadlineDate( null );
      lTable.update();

      TaskKey lWorkPackageKey = createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lEngineKey );
         }
      } );

      TaskTaskKey lReplTaskDefnRevision = new TaskRevisionBuilder().build();
      TaskKey lReplTaskKey = new TaskBuilder().onInventory( lAircraftKey )
            .withTaskRevision( lReplTaskDefnRevision ).build();

      // build the WORMVL relationship
      new EventRelationshipBuilder().fromEvent( lReplTaskKey ).toEvent( lWorkPackageKey )
            .withType( RefRelationTypeKey.WORMVL ).build();

      // build the DRVTASK relationship
      new EventRelationshipBuilder().fromEvent( lWorkPackageKey ).toEvent( lOffWingTask )
            .withType( RefRelationTypeKey.DRVTASK ).build();

      iDeadlineProcedureDelegate.updateWormvlDeadline( lWorkPackageKey );

      EvtSchedDead lReplCdyDeadline =
            new EvtSchedDead( lReplTaskKey.getEventKey(), DataTypeKey.CDY );
      lReplCdyDeadline.assertExist();
      lReplCdyDeadline.assertSchedDeadDt( lDeadline );
   }


   @Before
   public void loadData() throws Exception {
      iDeadlineProcedureDelegate = new DeadlineProcedureDelegate();
   }
}
