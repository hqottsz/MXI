
package com.mxi.mx.core.services.bsync.synchronization.logic.update;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.enumeration.task.ClassMode;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.plsql.delegates.DeadlineProcedures;
import com.mxi.mx.core.services.bsync.synchronization.model.update.SchedulingRulesDetails;
import com.mxi.mx.core.services.bsync.synchronization.model.update.SchedulingRulesTO;
import com.mxi.mx.core.services.inventory.oper.InventoryOperationalInterface;


/**
 * Tests {@link SchedulingRulesDelegate}
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SchedulingRulesDelegateTest {

   private Mockery iContext = new Mockery();

   private InventoryKey iHighestInv;
   private InventoryKey iInv;
   private DeadlineProcedures iMockDeadlineProcedures;
   private InventoryOperationalInterface iMockInventoryOperationalInterface;
   private SchedulingRulesDelegate iSchedulingRulesDelegate;

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
   public void setUp() throws Exception {
      // Setup some mock objects.
      iMockDeadlineProcedures = iContext.mock( DeadlineProcedures.class );
      StoredProcedureCall.getInstance().setDelegate( iMockDeadlineProcedures );

      iMockInventoryOperationalInterface = iContext.mock( InventoryOperationalInterface.class );

      iSchedulingRulesDelegate =
            new SchedulingRulesDelegate( null, null, iMockInventoryOperationalInterface );

      // Build some test objects in the database.
      iHighestInv = new InventoryBuilder().withClass( RefInvClassKey.ASSY ).build();
      iInv = new InventoryBuilder().withHighestInventory( iHighestInv ).build();
   }


   /**
    * Tests that when the prevent_deadline_sync bool is enabled for a task, it's deadlines are not
    * synchronized to the baseline.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testPreventDeadlineSync() throws Exception {

      final TaskKey lTask = new TaskBuilder().onInventory( iInv )
            .withTaskClass( RefTaskClassKey.REQ ).withManualSchedulingEnabled().build();

      iContext.checking( new Expectations() {

         {
            one( iMockDeadlineProcedures ).prepareSchedDeadline( with( equal( lTask ) ),
                  with( any( TaskTaskKey.class ) ), with( equal( false ) ) );

            ignoring( iMockInventoryOperationalInterface );
         }
      } );

      SchedulingRulesDetails lTransferObject = new SchedulingRulesTO( lTask, null, null, null,
            iHighestInv, RefEventStatusKey.ACTV, true, true, ClassMode.REQ, false, false, null );

      iSchedulingRulesDelegate.synchronize( lTransferObject, null );

      iContext.assertIsSatisfied();
   }


   /**
    * Test the scheduling rules synchronization of a REPL task. Ensure the REPL task's scheduling
    * information does not change during schronization (as REPL tasks do not manage their own
    * scheduling info).
    *
    * @throws Exception
    */
   @Test
   public void testSynchronizeReplTask() throws Exception {

      // Ensure the current and next revisions are not marked as manual.
      final boolean lCurrentManualScheduling = false;
      final boolean lNewManualScheduling = false;

      // REPL tasks are not recurring (but not really relevent to this test).
      final boolean lCurrentRecuring = false;
      final boolean lNewRecuring = false;

      final TaskKey lTask =
            new TaskBuilder().onInventory( iInv ).withTaskClass( RefTaskClassKey.REPL ).build();

      iContext.checking( new Expectations() {

         {

            // Mock out this method call.
            one( iMockInventoryOperationalInterface ).calculateOperatingStatus(
                  with( equal( iHighestInv ) ), with( any( Date.class ) ),
                  with( any( Date.class ) ) );

            // Verify that the REPL task's scheduling information is NOT updated.
            // (updates performed by prepareSchedDeadline )
            never( iMockDeadlineProcedures ).prepareSchedDeadline( with( equal( lTask ) ),
                  with( any( TaskTaskKey.class ) ), with( any( boolean.class ) ) );
         }
      } );

      // The null arguments are not relevent to this test.
      SchedulingRulesTO lTO = new SchedulingRulesTO( lTask, null, null, null, iHighestInv,
            RefEventStatusKey.ACTV, lCurrentRecuring, lNewRecuring, ClassMode.REQ,
            lCurrentManualScheduling, lNewManualScheduling, null );

      // Call method under test.
      iSchedulingRulesDelegate.synchronize( lTO, null );

      // The verification is performed within the context expectations.
      iContext.assertIsSatisfied();
   }
}
