
package com.mxi.mx.db.plsql.eventpkg;

import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createForecastModel;
import static com.mxi.am.domain.Domain.createRepl;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.ibm.icu.util.Calendar;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.domain.Repl;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.plsql.delegates.DeadlineProcedureDelegate;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;


/**
 * Tests the
 * <code>EVENT_PKG.UpdateDrivingDeadline(an_SchedStaskDbId, an_SchedStaskId, on_Return)</code>
 * procedure
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateDrivingDeadlineTest {

   private static final Date DAY_BEFORE_DRIVING_DEADLINE;

   private static final Date DRIVING_DEADLINE;

   private static final double USAGE_RATE = 5d;

   private static final double CURRENT_USAGE = 1d;

   static {
      Calendar lCalendar = Calendar.getInstance();
      lCalendar.set( Calendar.HOUR_OF_DAY, 23 );
      lCalendar.set( Calendar.MINUTE, 59 );
      lCalendar.set( Calendar.SECOND, 59 );
      lCalendar.set( Calendar.MILLISECOND, 0 ); // Time granularity in database is seconds; strip
      DAY_BEFORE_DRIVING_DEADLINE = lCalendar.getTime();

      lCalendar.add( Calendar.DAY_OF_YEAR, +1 );
      DRIVING_DEADLINE = lCalendar.getTime();
   }

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public final FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private DeadlineProcedureDelegate iDeadlineProcedures;


   @Before
   public void setUp() {
      iDeadlineProcedures = new DeadlineProcedureDelegate();
   }


   @Test
   public void itCreatesREPLDeadlinesToComponentWorkPackageDeadline() {
      // Arrange
      final InventoryKey lMainInventory = createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft
                  .setForecastModel( createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, HOURS, USAGE_RATE );
                     }
                  } ) );
            aAircraft.addUsage( HOURS, new BigDecimal( CURRENT_USAGE ) );
         }
      } );

      final TaskKey lComponentWorkPackage =
            createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( lMainInventory );
               }
            } );
      TaskKey lDeadlineTask = new TaskBuilder().withParentTask( lComponentWorkPackage )
            .withUsageDeadline( HOURS, CURRENT_USAGE + USAGE_RATE, DRIVING_DEADLINE ).build();

      // Let's have multiple REPL tasks
      TaskKey lReplTask1 = createRepl( new DomainConfiguration<Repl>() {

         @Override
         public void configure( Repl aRepl ) {
            aRepl.setComponentWorkPackage( lComponentWorkPackage );
            aRepl.setMainInventory( lMainInventory );
         }
      } );
      TaskKey lReplTask2 = createRepl( new DomainConfiguration<Repl>() {

         @Override
         public void configure( Repl aRepl ) {
            aRepl.setComponentWorkPackage( lComponentWorkPackage );
            aRepl.setMainInventory( lMainInventory );
         }
      } );

      // Act
      iDeadlineProcedures.updatedDrivingDeadline( lComponentWorkPackage );

      // Assert
      // ... that the driving task is updated appropriately
      Assert.assertEquals( lDeadlineTask, getDrivingTask( lComponentWorkPackage ) );

      // ... that the REPL deadlines has been updated to the component work package's deadline
      Assert.assertEquals( "REPL Deadline (1)", DAY_BEFORE_DRIVING_DEADLINE,
            getTaskDeadline( lReplTask1, HOURS ) );
      Assert.assertEquals( "REPL Deadline (2)", DAY_BEFORE_DRIVING_DEADLINE,
            getTaskDeadline( lReplTask2, HOURS ) );
   }


   @Test
   public void itRemovesREPLDeadlinesWhenComponentWorkPackageHasNoDeadline() {
      // Arrange
      final InventoryKey lMainInventory = createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft
                  .setForecastModel( createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, HOURS, USAGE_RATE );
                     }
                  } ) );
            aAircraft.addUsage( HOURS, new BigDecimal( CURRENT_USAGE ) );
         }
      } );
      final TaskKey lComponentWorkPackage =
            createWorkPackage( new DomainConfiguration<WorkPackage>() {

               @Override
               public void configure( WorkPackage aWorkPackage ) {
                  aWorkPackage.setAircraft( lMainInventory );
               }
            } );

      // Let's have a repl task with a deadline
      TaskKey lReplTask = createRepl( new DomainConfiguration<Repl>() {

         @Override
         public void configure( Repl aRepl ) {
            aRepl.setComponentWorkPackage( lComponentWorkPackage );
            aRepl.setMainInventory( lMainInventory );
            aRepl.addUsageDeadline( HOURS, CURRENT_USAGE + USAGE_RATE, DRIVING_DEADLINE );
         }
      } );

      // Act
      iDeadlineProcedures.updatedDrivingDeadline( lComponentWorkPackage );

      // Assert
      // ... that the driving task is updated appropriately
      Assert.assertEquals( null, getDrivingTask( lComponentWorkPackage ) );

      // ... that the REPL deadlines has been removed
      Assert.assertEquals( "REPL Deadline", null, getTaskDeadline( lReplTask, HOURS ) );
   }


   /**
    * Retrieves the driving task
    *
    * @param aTask
    *           the task
    * @return the driving task
    */
   private TaskKey getDrivingTask( TaskKey aTask ) {
      List<EvtEventRel> lRelationshipTables =
            EvtEventRel.findByRelationshipType( aTask, RefRelationTypeKey.DRVTASK );
      if ( lRelationshipTables.isEmpty() ) {
         return null;
      }

      return new TaskKey( lRelationshipTables.get( 0 ).getRelEvent() );
   }


   /**
    * Finds the deadline for a task
    *
    * @param aTask
    *           the task
    * @return the task deadline
    */
   private Date getTaskDeadline( TaskKey aTask, DataTypeKey aDataType ) {
      EventDeadlineKey lDeadlineKey = new EventDeadlineKey( aTask.getEventKey(), aDataType );
      EvtSchedDeadTable lDeadlineTable = EvtSchedDeadTable.findByPrimaryKey( lDeadlineKey );
      return lDeadlineTable.exists() ? lDeadlineTable.getDeadlineDate() : null;
   }
}
