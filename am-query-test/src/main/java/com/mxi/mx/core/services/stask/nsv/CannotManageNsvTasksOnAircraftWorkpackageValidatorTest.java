
package com.mxi.mx.core.services.stask.nsv;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.message.MxCoreParameterFactoryStub;
import com.mxi.mx.core.services.stask.nsv.ManageNsvTasksTO.Mode;
import com.mxi.mx.core.services.stask.status.AnotherActiveTaskAlreadyExistsValidator;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Ensures {@link AnotherActiveTaskAlreadyExistsValidator} throws an exception when there is a
 * location conflict
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class CannotManageNsvTasksOnAircraftWorkpackageValidatorTest {

   private static final InventoryKey AIRCRAFT_INVENTORY = new InventoryKey( 4650, 1 );
   private static final InventoryKey COMPONENT_INVENTORY = new InventoryKey( 4650, 2 );

   private static final TaskKey AIRCRAFT_WORK_PACKAGE = new TaskKey( 4650, 3 );
   private static final TaskKey COMPONENT_WORK_PACKAGE = new TaskKey( 4650, 4 );

   private CannotManageNsvTasksOnAircraftWorkpackageValidator iValidator =
         new CannotManageNsvTasksOnAircraftWorkpackageValidator( new MxCoreParameterFactoryStub() );
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      InvInvTable lInventoryTable;
      SchedStaskTable lTaskTable;

      lInventoryTable = InvInvTable.create( AIRCRAFT_INVENTORY );
      lInventoryTable.setInvClass( RefInvClassKey.ACFT );
      lInventoryTable.insert();

      lTaskTable = SchedStaskTable.create( AIRCRAFT_WORK_PACKAGE );
      lTaskTable.setMainInventory( AIRCRAFT_INVENTORY );
      lTaskTable.insert();

      lInventoryTable = InvInvTable.create( COMPONENT_INVENTORY );
      lInventoryTable.setInvClass( RefInvClassKey.TRK );
      lInventoryTable.insert();

      lTaskTable = SchedStaskTable.create( COMPONENT_WORK_PACKAGE );
      lTaskTable.setMainInventory( COMPONENT_INVENTORY );
      lTaskTable.insert();
   }


   /**
    * Ensures that no exceptions are thrown when NSV tasks are attempted to be enforced on a
    * component work package.
    */
   @Test
   public void testExceptionNotThrownForComponentWpWithEnforceMode() {

      try {
         iValidator.validate( COMPONENT_WORK_PACKAGE, Mode.ENFORCE_NSV_TASKS );
      } catch ( CannotEnforceNsvTasksOnAircraftWorkpackageException e ) {
         fail( "not expecting CannotEnforceNsvTasksOnAircraftWorkpackageException" );
      } catch ( CannotIgnoreNsvTasksOnAircraftWorkpackageException e ) {
         fail( "not expecting CannotIgnoreNsvTasksOnAircraftWorkpackageException" );
      } catch ( MxException e ) {
         fail( "not expecting MxException" );
      }
   }


   /**
    * Ensures that no exceptions are thrown when NSV tasks are attempted to be ignored on a
    * component work package.
    */
   @Test
   public void testExceptionNotThrownForComponentWpWithIgnoreMode() {

      try {
         iValidator.validate( COMPONENT_WORK_PACKAGE, Mode.IGNORE_NSV_TASKS );
      } catch ( CannotEnforceNsvTasksOnAircraftWorkpackageException e ) {
         fail( "not expecting CannotEnforceNsvTasksOnAircraftWorkpackageException" );
      } catch ( CannotIgnoreNsvTasksOnAircraftWorkpackageException e ) {
         fail( "not expecting CannotIgnoreNsvTasksOnAircraftWorkpackageException" );
      } catch ( MxException e ) {
         fail( "not expecting MxException" );
      }
   }


   /**
    * Ensures that the exception is thrown when NSV tasks are attempted to be enforced on an
    * aircraft work package.
    */
   @Test
   public void testExceptionThrownForAircraftWpWithEnforceMode() {

      try {
         iValidator.validate( AIRCRAFT_WORK_PACKAGE, Mode.ENFORCE_NSV_TASKS );
         fail( "expected CannotEnforceNsvTasksOnAircraftWorkpackageException" );
      } catch ( CannotEnforceNsvTasksOnAircraftWorkpackageException e ) {
         // expected
      } catch ( CannotIgnoreNsvTasksOnAircraftWorkpackageException e ) {
         fail( "expected CannotEnforceNsvTasksOnAircraftWorkpackageException" );
      } catch ( MxException e ) {
         fail( "expected CannotEnforceNsvTasksOnAircraftWorkpackageException" );
      }
   }


   /**
    * Ensures that the exception is thrown when NSV tasks are attempted to be ignored on an aircraft
    * work package.
    */
   @Test
   public void testExceptionThrownForAircraftWpWithIgnoreMode() {

      try {
         iValidator.validate( AIRCRAFT_WORK_PACKAGE, Mode.IGNORE_NSV_TASKS );
         fail( "expected CannotIgnoreNsvTasksOnAircraftWorkpackageException" );
      } catch ( CannotIgnoreNsvTasksOnAircraftWorkpackageException e ) {
         // expected
      } catch ( CannotEnforceNsvTasksOnAircraftWorkpackageException e ) {
         fail( "expected CannotIgnoreNsvTasksOnAircraftWorkpackageException" );
      } catch ( MxException e ) {
         fail( "expected CannotIgnoreNsvTasksOnAircraftWorkpackageException" );
      }
   }
}
