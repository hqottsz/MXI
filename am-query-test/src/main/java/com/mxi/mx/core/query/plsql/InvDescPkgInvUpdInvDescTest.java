
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.InventoryKey;


/**
 * DOCUMENT ME!
 *
 * @author cbrouse
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class InvDescPkgInvUpdInvDescTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            InvDescPkgInvUpdInvDescTest.class );
   }


   /** The inventory pk. */
   private static final InventoryKey INV_NO_PK = new InventoryKey( "1000:15000" );

   /** The inventory serial number. */
   private static final String SER_NO_OEM = "1234ABCD";

   /** The inventory part number description. */
   private static final String PART_NO_SDESC = "Flux Capacitor";


   /**
    * Tests that the inventory description is properly updated for a serialized item.
    *
    * @throws Exception
    *            if an error occurs.
    */

   @Test
   public void testSerialized() throws Exception {

      // Execute the query to retrieve data for the prod plan
      DataSetArgument lArgs = execute( INV_NO_PK );

      final String lExpectedInvNoSdesc =
            PART_NO_SDESC + " " + "(" + "PN: , SN: " + SER_NO_OEM + ")";

      // Ensure the procedure returned the proper value
      assertEquals( lExpectedInvNoSdesc, lArgs.getString( "aInvNoSdesc" ) );

      // Ensure the procedure updated the database properly
      this.assertInvNoSdesc( lExpectedInvNoSdesc, INV_NO_PK );
   }


   /**
    * Asserts that the expected and actual inventory description values match.
    *
    * @param aExpectedValue
    *           the expected value.
    * @param aInvPk
    *           the inventory PK.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void assertInvNoSdesc( String aExpectedValue, InventoryKey aInvPk ) throws Exception {

      // Build the return columns
      String[] lReturnCols = { "inv_no_sdesc" };

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInvPk, new String[] { "inv_no_db_id", "inv_no_id" } );

      // Execute the query
      DataSet lReturn = MxDataAccess.getInstance().executeQuery( lReturnCols, "inv_inv", lArgs );

      // If no data was returned, assertion fails
      if ( !lReturn.next() ) {
         fail( "No data returned for inventory " + aInvPk );
      }

      // Get the return value
      String lActualValue = lReturn.getString( lReturnCols[0] );

      // Ensure the expected and actual values match
      assertEquals( aExpectedValue, lActualValue );
   }


   /**
    * Execute the query.
    *
    * @param aInventoryPk
    *           the inventory PK.
    *
    * @return the procedure result.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private DataSetArgument execute( InventoryKey aInventoryPk ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryPk, new String[] { "aInvNoDbId", "aInvNoId" } );

      // Execute the query
      return QueryExecutor.executePlsql( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
