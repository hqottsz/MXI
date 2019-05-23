
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
public final class AircraftPopUpTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AircraftPopUpTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where aircraft inventories are retrieved for an assembly inventory.
    *
    * <ol>
    * <li>Query for all aircraft inventories.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAircraftForAssembly() throws Exception {
      execute( new InventoryKey( "4650:1233" ), 1 );
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( "4650:1234", "A320", "A320-200", "Airbus 320" );
   }


   /**
    * Test the case where aircraft inventories are not there for an assembly inventory.
    *
    * <ol>
    * <li>Query for all aircraft inventories.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNoAircraftForAssembly() throws Exception {
      execute( new InventoryKey( "4650:1232" ), 1 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aAssembly
    *           the assembly inventory for which all the aircraft inventories are required.
    * @param aUserId
    *           the user id.
    */
   private void execute( InventoryKey aAssembly, int aUserId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAssembly, new String[] { "aInvInvDbId", "aInvInvId" } );
      lArgs.add( "aUserId", aUserId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aAircraftKey
    *           the aircraft inventory key.
    * @param aRegCd
    *           the registration code.
    * @param aSerialNo
    *           the aircraft serial no.
    * @param aName
    *           the aircraft name.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aAircraftKey, String aRegCd, String aSerialNo, String aName )
         throws Exception {

      MxAssert.assertEquals( aAircraftKey, iDataSet.getString( "aircraft_key" ) );
      MxAssert.assertEquals( aRegCd, iDataSet.getString( "ac_reg_cd" ) );
      MxAssert.assertEquals( aSerialNo, iDataSet.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( aName, iDataSet.getString( "inv_no_sdesc" ) );
   }
}
