
package com.mxi.mx.core.query.capability;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests the query com.mxi.mx.core.query.capability.PopulateCapabilities.qrx
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PullCapabilitiesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), PullCapabilitiesTest.class );
   }


   private static final InventoryKey AIRCRAFT__KEY = new InventoryKey( 4650, 300785 );


   /**
    * Tests the pulling of the capabilities for specified aircraft from baseline to actual.
    *
    * Preconditions:
    *
    * There is data setup needed in baseline and actuals. capability ETOPS exists in baseline not
    * actuals. capability ALAND exists in baseline and actuals but level CATIII has been removed
    * from capability ALAND in baseline.
    *
    * Action:
    *
    * Call the pullCapabilities method
    *
    * Expectation:
    *
    * A new capability ETOPS is pulled into actuals from baseline. A configured level for existing
    * capability ALAND is set to null
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testPullCapabilities() throws Exception {
      // boolean lHasWifi = false;
      boolean lHasEtops = false;
      DataSet lDataSet = getCapabilitiesDs( AIRCRAFT__KEY );

      assertEquals( "Number of retrieved rows", 2, lDataSet.getRowCount() );

      lDataSet.beforeFirst();
      while ( lDataSet.hasNext() ) {
         lDataSet.next();
         String lCapability = lDataSet.getString( "cap_cd" );
         if ( "ALAND".equals( lCapability ) ) {
            // assert that a configured capability level has a value in actuals
            assertNotNull( lDataSet.getString( "config_level_cd" ) );
         }
         if ( "ETOPS".equals( lCapability ) ) {
            lHasEtops = true;
         }

      }

      // assert that a capability ETOPS doesn't exist in actuals
      assertFalse( lHasEtops );

      // ACTION
      pullCapabilities( AIRCRAFT__KEY );

      // ASSERT
      lDataSet = getCapabilitiesDs( AIRCRAFT__KEY );

      assertEquals( "Number of retrieved rows", 3, lDataSet.getRowCount() );

      lHasEtops = false;
      lDataSet.beforeFirst();
      while ( lDataSet.hasNext() ) {
         lDataSet.next();
         String lCapability = lDataSet.getString( "cap_cd" );
         if ( "ALAND".equals( lCapability ) ) {
            // assert that a configured capability level is updated to empty value because it
            // has been removed from baseline
            assertNull( lDataSet.getString( "config_level_cd" ) );
         }
         if ( "ETOPS".equals( lCapability ) ) {
            lHasEtops = true;
         }

      }

      // assert that a new capability ETOPS is inserted into actuals
      assertTrue( lHasEtops );

   }


   /**
    * This method populates the capabilities from baseline to actual.
    *
    * @param aAircraft
    *           The aircraft key.
    *
    */
   private void pullCapabilities( InventoryKey aAircraft ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, new String[] { "aAcftNoDbId", "aAcftNoId" } );

      QueryExecutor.executeUpdate( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Returns the value of the capability ds property.
    *
    * @param aInventory
    *           The aircraft key.
    *
    * @return the value of the capability ds property.
    */
   private DataSet getCapabilitiesDs( InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, new String[] { "aAcftNoDbId", "aAcftNoId" } );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.capability.GetCapabilities", lArgs );

      return lDs;
   }
}
