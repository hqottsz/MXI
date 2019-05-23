
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
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests the query com.mxi.mx.core.query.capability.PopulateCapabilities.qrx
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PopulateCapabilitiesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), PopulateCapabilitiesTest.class );
   }


   private static final AssemblyKey ASSEMBLY__KEY = new AssemblyKey( 5000000, "A320" );
   private static final InventoryKey AIRCRAFT__KEY_1 = new InventoryKey( 4650, 1 );
   private static final InventoryKey AIRCRAFT__KEY_2 = new InventoryKey( 4650, 2 );


   /**
    * Tests the populating of the capabilities across multiple aircrafts from baseline to actual.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testPopulateCapabilities() throws Exception {

      // ACTION
      populateCapabilities( ASSEMBLY__KEY );

      // ASSERT
      DataSet lDataSet = getCapabilitiesDs( AIRCRAFT__KEY_1 );

      assertEquals( "Number of retrieved rows", 3, lDataSet.getRowCount() );

      lDataSet = getCapabilitiesDs( AIRCRAFT__KEY_2 );

      assertEquals( "Number of retrieved rows", 3, lDataSet.getRowCount() );

   }


   /**
    * This method populates the capabilities from baseline to actual.
    *
    * @param aAssembly
    *           The assembly key.
    *
    */
   private void populateCapabilities( AssemblyKey aAssembly ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAssembly, new String[] { "aAssmblDbId", "aAssmblCd" } );

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
