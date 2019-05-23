
package com.mxi.mx.core.query.capability;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.CapabilityKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests the query com.mxi.mx.core.query.capability.UpdateCapabilityLevel.qrx
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateCapabilityLevelTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UpdateCapabilityLevelTest.class );
   }


   private static final InventoryKey AIRCRAFT__KEY = new InventoryKey( 4650, 300785 );
   private static final CapabilityKey CAPABILITY__KEY = new CapabilityKey( 10, "ETOPS" );
   private static final CapabilityLevelKey CAPABILITY__LEVEL_KEY =
         new CapabilityLevelKey( 10, "ETOPS_90", CAPABILITY__KEY );


   /**
    * Tests the updating of the configured capability level.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testUpdateConfiguredCapabilityLevel() {

      // ACTION
      updateConfiguredCapabilityLevel( AIRCRAFT__KEY, CAPABILITY__LEVEL_KEY );

      // ASSERT
      DataSet lDataSet = getCapabilityLevelDs( AIRCRAFT__KEY, CAPABILITY__KEY );

      assertEquals( "Number of retrieved rows", 1, lDataSet.getRowCount() );

      lDataSet.first();

      assertEquals( "ETOPS_90", lDataSet.getString( "config_level_cd" ) );

   }


   /**
    * This method updates the configured capability level.
    *
    * @param aInventory
    *           The aircraft key.
    *
    */
   private void updateConfiguredCapabilityLevel( InventoryKey aAircraft,
         CapabilityLevelKey aConfiguredCapabilityLevel ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, new String[] { "aAcftNoDbId", "aAcftNoId" } );
      lArgs.add( aConfiguredCapabilityLevel,
            new String[] { "aConfigLevelDbId", "aConfigLevelCd", "aCapDbId", "aCapCd" } );

      QueryExecutor.executeUpdate( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Returns the value of the configured capability level ds property.
    *
    * @param aInventory
    *           The aircraft key.
    *
    * @return the value of the configured capability ds property.
    */
   private DataSet getCapabilityLevelDs( InventoryKey aAircraft, CapabilityKey aCapability ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, new String[] { "aAcftNoDbId", "aAcftNoId" } );
      lArgs.add( aCapability, new String[] { "aCapDbId", "aCapCd" } );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.capability.GetCapabilityLevel", lArgs );

      return lDs;
   }
}
