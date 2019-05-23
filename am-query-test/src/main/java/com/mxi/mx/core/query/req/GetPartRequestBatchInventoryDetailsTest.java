
package com.mxi.mx.core.query.req;

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
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;


/**
 * Tests the GetPartRequestBatchInventoryDetails query.
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPartRequestBatchInventoryDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPartRequestBatchInventoryDetailsTest.class );
   }


   private static final PartRequestKey PART_REQUEST = new PartRequestKey( 4650, 1000 );
   private static final PartRequestKey PART_REQUEST_SER = new PartRequestKey( 4650, 1001 );
   private static final PartRequestKey PART_REQUEST_NULL_INV = new PartRequestKey( 4650, 1002 );


   /**
    * Tests that the proper data is returned for a valid part request.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lDS = executeQuery( PART_REQUEST );

      assertEquals( 1, lDS.getRowCount() );

      lDS.first();

      // assert values
      assertEquals( "RFI", lDS.getString( "inv_cond_cd" ) );
      assertEquals( new LocationKey( 4650, 4000 ),
            lDS.getKey( LocationKey.class, "loc_db_id", "loc_id" ) );
      assertEquals( new PartNoKey( 4650, 2000 ),
            lDS.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" ) );
      assertEquals( "0serial_no_test", lDS.getString( "serial_no_oem" ) );
      assertEquals( "0barcode_test", lDS.getString( "barcode_sdesc" ) );
      assertEquals( 5.0, lDS.getDouble( "bin_qt" ), 0f );
   }


   /**
    * Test that no result is returned when there is no inventory.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryNullInv() throws Exception {
      DataSet lDS = executeQuery( PART_REQUEST_NULL_INV );

      assertEquals( 0, lDS.getRowCount() );
   }


   /**
    * Test that no result is returned when inventory is not BATCH
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQuerySerialized() throws Exception {
      DataSet lDS = executeQuery( PART_REQUEST_SER );

      assertEquals( 0, lDS.getRowCount() );
   }


   /**
    * Executes the query with the given part request as the argument.
    *
    * @param aPartRequest
    *           The part request.
    *
    * @return The results.
    */
   private DataSet executeQuery( PartRequestKey aPartRequest ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartRequest, "aPartReqDbId", "aPartReqId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
