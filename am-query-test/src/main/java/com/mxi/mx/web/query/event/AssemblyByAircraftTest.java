
package com.mxi.mx.web.query.event;

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
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AssemblyByAircraftTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AssemblyByAircraftTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where assembly inventories are retrieved for an aircraft inventory.
    *
    * <ol>
    * <li>Query for all assembly inventories.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAssemblyForAircraft() throws Exception {
      execute( new InventoryKey( "4650:1111" ) );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:1234", "Engine-1" );
   }


   /**
    * Test the case where assembly inventories are not there for an aircraft inventory.
    *
    * <ol>
    * <li>Query for all assembly inventories.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoAssemblyForAircraft() throws Exception {
      execute( new InventoryKey( "4650:1112" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aAircraft
    *           the aircraft inventory for which all the assembly inventories are required.
    */
   private void execute( InventoryKey aAircraft ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAircraft, new String[] { "aInvDbId", "aInvId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aAssemblyKey
    *           the assembly inventory key.
    * @param aSerialNo
    *           the assembly serial no.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aAssemblyKey, String aSerialNo ) throws Exception {
      MxAssert.assertEquals( aAssemblyKey, iDataSet.getString( "assembly_inv_key" ) );
      MxAssert.assertEquals( aSerialNo, iDataSet.getString( "serial_no_oem" ) );
   }
}
