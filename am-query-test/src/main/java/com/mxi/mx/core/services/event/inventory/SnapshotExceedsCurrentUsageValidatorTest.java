
package com.mxi.mx.core.services.event.inventory;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.validation.Messages;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvCurrUsage;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Tests the SnapshotExceedsCurrentUsageValidator
 *
 * @author asmolko
 */
@RunWith( Theories.class )
public final class SnapshotExceedsCurrentUsageValidatorTest {

   /** Task/event key to be used in the test */
   private static final TaskKey TASK = new TaskKey( 200, 1 );

   /** Inventory to be used in the test */
   private static final InventoryKey INVENTORY = new InventoryKey( 200, 10 );

   /** The user's id to be used in the test */
   private static final int USER_ID = 15001;

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "username" );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** Current usage value. The same for TSN, TSI and TSO */
   @DataPoint
   public static final Double CURRENT_VALUE = 5d;

   /** Provided usage value that exceeds the current value */
   @DataPoint
   public static final Double EXCEED_VALUE = 7d;

   /** Provided usage value that are below of the current value */
   @DataPoint
   public static final Double OK_VALUE = 3d;

   /** Exception expected value to increase readability of the tests */
   private static final boolean EXCEPTION_EXPECTED = true;

   /** Exception NOT expected value to increase readability of the tests */
   private static final boolean EXCEPTION_NOT_EXPECTED = false;


   /**
    * {@inheritDoc}
    *
    * @throws Exception
    *            the exception
    */
   @Before
   public void loadData() throws Exception {
      // Specify the current usage values
      InvCurrUsage lInvCurrUsage =
            InvCurrUsage.create( INVENTORY, DataTypeKey.CYCLES, CURRENT_VALUE, CURRENT_VALUE );
      lInvCurrUsage.setTsiQt( CURRENT_VALUE );
   }


   /**
    * This method ensures that faults do not have exceptions
    *
    * @param aValue
    *           the usage value
    */
   @Theory
   @Test
   public void testFaultThrowsNoException( Double aValue ) {
      setupAndAssert( RefEventTypeKey.FL, new FaultKey( 4650, 1 ), RefTaskClassKey.REQ, aValue,
            EXCEPTION_NOT_EXPECTED );
   }


   /**
    * Check to see that the validator catches the problem when the values exceed current values
    *
    * @param aValue
    *           the usage value
    */
   @Theory
   @Test
   public void testTaskExceedSnapshot( Double aValue ) {
      assumeThat( aValue, is( greaterThan( CURRENT_VALUE ) ) );

      setupAndAssert( RefEventTypeKey.TS, null, RefTaskClassKey.REQ, aValue, EXCEPTION_EXPECTED );
   }


   /**
    * Make sure the validator does not catch any warnings/errors if the provided values do not
    * exceed the current usage snapshot value
    *
    * @param aValue
    *           the usage value
    */
   @Theory
   @Test
   public void testTaskNotExceedSnapshot( Double aValue ) {
      assumeThat( aValue, is( lessThanOrEqualTo( CURRENT_VALUE ) ) );

      setupAndAssert( RefEventTypeKey.TS, null, RefTaskClassKey.REQ, aValue,
            EXCEPTION_NOT_EXPECTED );
   }


   /**
    * Make sure the validator does not throw any warnings/errors if the task is a check
    *
    * @param aValue
    *           the usage value
    */
   @Theory
   @Test
   public void testWorkPackageThrowsNoException( Double aValue ) {
      setupAndAssert( RefEventTypeKey.TS, null, RefTaskClassKey.CHECK, aValue,
            EXCEPTION_NOT_EXPECTED );
   }


   /**
    * The method simulate different conditions and asserts the validator output
    *
    * @param aEventType
    *           event type
    * @param aFault
    *           which fault the event is linked to
    * @param aTaskClass
    *           task class
    * @param aUsageValue
    *           usage value
    * @param aExpectException
    *           if true - it is expected that the validation will catch a problem, otherwise -
    *           success
    */
   private void setupAndAssert( RefEventTypeKey aEventType, FaultKey aFault,
         RefTaskClassKey aTaskClass, double aUsageValue, boolean aExpectException ) {
      InvInvTable lInvInvTable = InvInvTable.create( INVENTORY );
      lInvInvTable.insert();

      // Data set up according to the provided arguments
      EvtEventTable lEvtEventTable = EvtEventTable.create( TASK.getEventKey() );
      lEvtEventTable.setEventType( aEventType );
      lEvtEventTable.setHistBool( true );
      lEvtEventTable.insert();

      SchedStaskTable lSchedStaskTable = SchedStaskTable.create( TASK );
      lSchedStaskTable.setTaskClass( aTaskClass );
      lSchedStaskTable.setFault( aFault );
      lSchedStaskTable.setMainInventory( INVENTORY );
      lSchedStaskTable.insert();

      UsageSnapshot lSpecifiedSnapshot = new UsageSnapshot( INVENTORY, DataTypeKey.CYCLES,
            aUsageValue, aUsageValue, aUsageValue, null, null );

      // The functionality under the test
      Messages lMessages = new Messages();
      new SnapshotExceedsCurrentUsageValidator().check( lMessages, lSpecifiedSnapshot,
            TASK.getEventKey() );

      // Validate the result of the check and assert the outcome
      assertEquals( aExpectException, !lMessages.getMessages().isEmpty() );
   }

}
