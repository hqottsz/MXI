package com.mxi.mx.core.query.task;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * This UT test the validateReleaseOverdueFault query
 *
 * @author okamenskova
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ValidateReleaseOverdueFaultTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The query set */
   private QuerySet iQs;

   private static final String INV_DESCRIPTION = "INV_DESCRIPTION";


   /**
    * The subtask for the fault is COMPLETED and Overdue
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testValidateReleaseOverdueFaultWithCompletedOverdueSubtask() throws Exception {
      LocationKey lLocation = new LocationDomainBuilder().build();
      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      // Fault set up:
      FaultKey lFault = new SdFaultBuilder().onInventory( lTaskInventory ).build();
      TaskKey lRootTaskKey = new TaskBuilder().withTaskClass( RefTaskClassKey.CORR )
            .withStatus( RefEventStatusKey.ACTV ).build();

      // subtask for the fault ACTV ( not historic ):
      TaskKey lTaskKey = new TaskBuilder().onInventory( lTaskInventory )
            .withTaskClass( RefTaskClassKey.ADHOC ).withFault( lFault ).asOverdue()
            .withParentTask( lRootTaskKey ).asHistoric().build();

      // Build query arguments and execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lAircraft, "aHighestInvDbId", "aHighestInvId" );

      iQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.validateReleaseOverdueFault", lArgs );

      // EXPECTED RESULT: no overdue faults.
      // Assert that 0 rows returned from the query
      assertEquals( "Number of retrieved rows", 0, iQs.getRowCount() );

   }


   /**
    * The subtask for the fault is ACTV and Overdue
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testValidateReleaseOverdueFaultWithACTVOverdueSubtask() throws Exception {
      LocationKey lLocation = new LocationDomainBuilder().build();
      InventoryKey lAircraft = new InventoryBuilder().build();

      InventoryKey lTaskInventory = new InventoryBuilder().withHighestInventory( lAircraft )
            .atLocation( lLocation ).withDescription( INV_DESCRIPTION ).build();

      // Fault set up:
      FaultKey lFault = new SdFaultBuilder().onInventory( lTaskInventory ).build();
      TaskKey lRootTaskKey = new TaskBuilder().withTaskClass( RefTaskClassKey.CORR )
            .withStatus( RefEventStatusKey.ACTV ).build();

      // subtask for the fault ACTV ( not historic ):
      TaskKey lTaskKey =
            new TaskBuilder().onInventory( lTaskInventory ).withTaskClass( RefTaskClassKey.ADHOC )
                  .withFault( lFault ).asOverdue().withParentTask( lRootTaskKey ).build();

      // Build query arguments and execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lAircraft, "aHighestInvDbId", "aHighestInvId" );

      iQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.validateReleaseOverdueFault", lArgs );

      // EXPECTED RESULT: 1 overdue fault.
      // Assert that 1 row is returned from the query
      assertEquals( "Number of retrieved rows", 1, iQs.getRowCount() );

   }

}
