package com.mxi.mx.core.adapter.diagnostic.query;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;


public class GetInWorkWorkOrderTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey iAircraft;


   @Test
   public void execute_taskClassCodeCHECKAndStatusINWORK() {
      TaskKey lTask = createTask( RefTaskClassKey.CHECK, RefEventStatusKey.IN_WORK );

      QuerySet lQs = execute( iAircraft );

      assertTaskKey( lTask, lQs );
   }


   @Test
   public void execute_taskClassCodeROAndStatusINWORK() {
      TaskKey lTask = createTask( RefTaskClassKey.RO, RefEventStatusKey.IN_WORK );

      QuerySet lQs = execute( iAircraft );

      assertTaskKey( lTask, lQs );
   }


   @Test
   public void execute_taskClassCodeNeitherROorCHECKAndStatusINWORK() {
      createTask( RefTaskClassKey.JIC, RefEventStatusKey.IN_WORK );

      QuerySet lQs = execute( iAircraft );

      assertEquals( 0, lQs.getRowCount() );
   }


   @Test
   public void execute_taskClassCodeCHECKButStatusNotINWORK() {
      createTask( RefTaskClassKey.CHECK, RefEventStatusKey.COMPLETE );

      QuerySet lQs = execute( iAircraft );

      assertEquals( 0, lQs.getRowCount() );
   }


   private void assertTaskKey( TaskKey aTask, QuerySet aQs ) {
      assertEquals( 1, aQs.getRowCount() );
      aQs.next();
      assertEquals( aTask, aQs.getKey( TaskKey.class, "task_key" ) );
   }


   private TaskKey createTask( RefTaskClassKey aTaskClassKey, RefEventStatusKey aTaskStatus ) {
      return Domain.createRequirement( aRequirement -> {
         aRequirement.setTaskClass( aTaskClassKey );
         aRequirement.setStatus( aTaskStatus );
         aRequirement.setInventory( iAircraft );
      } );

   }


   private QuerySet execute( InventoryKey aInventoryKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, "aHighestInventoryDbId", "aHighestInventoryId" );
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   @Before
   public void dataSetup() {
      iAircraft = Domain.createAircraft();
   }

}
