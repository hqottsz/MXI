
package com.mxi.mx.core.query.fault;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FaultThresholdKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class ensures that the GetFaultThresholdForConfigSlot returns the proper fault thresholds.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetFaultThresholdsForConfigSlotTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetFaultThresholdsForConfigSlotTest.class );
   }


   private static final ConfigSlotKey PARENT_CONFIG_SLOT = new ConfigSlotKey( 4650, "TEST", 1 );

   /** Child config slot for ROOT_BOM_ITEM */
   private static final ConfigSlotKey CHILD_CONFIG_SLOT = new ConfigSlotKey( 4650, "TEST", 2 );

   private static final InventoryKey INVENTORY = new InventoryKey( 4650, 1 );

   private static final FaultThresholdKey THRESHOLD_ON_PARENT_CONFIG_SLOT =
         new FaultThresholdKey( 4650, 1 );

   private static final FaultThresholdKey THRESHOLD_ON_CHILD_CONFIG_SLOT =
         new FaultThresholdKey( 4650, 2 );

   private static final FaultThresholdKey THRESHOLD_ON_UNRELATED_CONFIG_SLOT =
         new FaultThresholdKey( 4650, 3 );


   /**
    * Ensures that child config slot's fault thresholds are not returned.
    */
   @Test
   public void testWillNotReturnChildConfigSlotThresholds() {
      Set<FaultThresholdKey> lFaultThresholds = executeQuery( PARENT_CONFIG_SLOT );

      assertThat( lFaultThresholds, not( hasItem( THRESHOLD_ON_CHILD_CONFIG_SLOT ) ) );
   }


   /**
    * Ensures that unrelated config slot's fault thresholds are not returned.
    */
   @Test
   public void testWillNotReturnUnrelatedConfigSlotThresholds() {
      Set<FaultThresholdKey> lFaultThresholds = executeQuery( CHILD_CONFIG_SLOT );

      assertThat( lFaultThresholds, not( hasItem( THRESHOLD_ON_UNRELATED_CONFIG_SLOT ) ) );
   }


   /**
    * Ensures that config slot's own fault thresholds will be returned.
    */
   @Test
   public void testWillReturnOwnConfigSlotThresholds() {
      Set<FaultThresholdKey> lFaultThresholds = executeQuery( CHILD_CONFIG_SLOT );

      assertThat( lFaultThresholds, hasItem( THRESHOLD_ON_CHILD_CONFIG_SLOT ) );
   }


   /**
    * Ensures that parent config slot's fault thresholds will be returned.
    */
   @Test
   public void testWillReturnParentConfigSlotThresholds() {
      Set<FaultThresholdKey> lFaultThresholds = executeQuery( CHILD_CONFIG_SLOT );

      assertThat( lFaultThresholds, hasItem( THRESHOLD_ON_PARENT_CONFIG_SLOT ) );
   }


   /**
    * Returns a set of fault thresholds applicable to the config slot
    *
    * @param aConfigSlot
    *           the config slot
    *
    * @return a set of applicable fault thresholds
    */
   private Set<FaultThresholdKey> executeQuery( ConfigSlotKey aConfigSlot ) {

      // Execute query with provided config slot and an applicable inventory
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( INVENTORY, "aInvDbId", "aInvId" );
      lArgs.add( aConfigSlot, "aAssmblDbId", "aAssmblCd", "aAssmblBomId" );

      QuerySet lQs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Convert result set into a set of fault thresholds
      Set<FaultThresholdKey> lFaultThresholds = new HashSet<FaultThresholdKey>();
      while ( lQs.next() ) {
         lFaultThresholds.add( lQs.getKey( FaultThresholdKey.class, "fault_threshold_key" ) );
      }

      return lFaultThresholds;
   }
}
