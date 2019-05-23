
package com.mxi.mx.core.query.capability;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
 * This class tests the query com.mxi.mx.core.query.capability.EmptyCurrentCapabilityLevels.qrx
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class EmptyCurrentCapabilityLevelsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            EmptyCurrentCapabilityLevelsTest.class );
   }


   private static final InventoryKey AIRCRAFT__KEY = new InventoryKey( 4650, 300785 );


   /**
    * Tests the emptying of the capability levels for specified aircraft from baseline to actual.
    *
    * Preconditions:
    *
    * There is data setup needed in baseline and actuals. capability ALAND exists in baseline and
    * actuals but level CATI has been removed from capability ALAND in baseline.
    *
    * Action:
    *
    * Call the emptyCurrentCapabilityLevels method
    *
    * Expectation:
    *
    * A current level for existing capability ALAND is set to null
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testEmptyCurrentCapabilityLevels() throws Exception {

      DataSet lDataSet = getCapabilitiesDs( AIRCRAFT__KEY );

      assertEquals( "Number of retrieved rows", 2, lDataSet.getRowCount() );

      lDataSet.beforeFirst();
      while ( lDataSet.hasNext() ) {
         lDataSet.next();
         String lCapability = lDataSet.getString( "cap_cd" );
         if ( "ALAND".equals( lCapability ) ) {
            // assert that a current capability level has a value in actuals
            assertNotNull( lDataSet.getString( "level_cd" ) );
         }

      }

      // ACTION
      emptyCurrentCapabilityLevels( AIRCRAFT__KEY );

      // ASSERT
      lDataSet = getCapabilitiesDs( AIRCRAFT__KEY );

      assertEquals( "Number of retrieved rows", 2, lDataSet.getRowCount() );

      lDataSet.beforeFirst();
      while ( lDataSet.hasNext() ) {
         lDataSet.next();
         String lCapability = lDataSet.getString( "cap_cd" );
         if ( "ALAND".equals( lCapability ) ) {
            // assert that a current capability level is updated to empty value because it
            // has been removed from baseline
            assertNull( lDataSet.getString( "level_cd" ) );
         }

      }

   }


   /**
    * This method empties the current capability levels in actual .
    *
    * @param aAircraft
    *           The aircraft key.
    *
    */
   private void emptyCurrentCapabilityLevels( InventoryKey aAircraft ) {
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
