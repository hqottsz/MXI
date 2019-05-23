
package com.mxi.mx.core.query.adapter.finance;

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
import com.mxi.mx.core.adapter.CoreAdapterUtils;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOrderLineByIdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetOrderLineByIdTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where POLines for PO are retrieved by id.
    *
    * <ol>
    * <li>Query for all POLines of PO by id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOrderLineRetrievalById() throws Exception {
      execute( 4650, 107107 );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "1", "1 EA", "50000" );

      iDataSet.next();
      testRow( "2", "0 EA", "0" );
   }


   /**
    * Test the case where PO id does not exists.
    *
    * <ol>
    * <li>Query for all POLines of PO by id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOrderNotExists() throws Exception {
      execute( 4650, 107108 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aPoDbId
    *           the PO db id.
    * @param aPoId
    *           the PO id.
    */
   private void execute( int aPoDbId, int aPoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aPoDbId", aPoDbId );
      lArgs.add( "aPoId", aPoId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aPOLineId
    *           the po line id.
    * @param aOrderQty
    *           the order quantity.
    * @param aUnitStandardPrice
    *           the unit standard price.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aPOLineId, String aOrderQty, String aUnitStandardPrice )
         throws Exception {
      MxAssert.assertEquals( "4650", iDataSet.getString( "po_db_id" ) );
      MxAssert.assertEquals( "107107", iDataSet.getString( "po_id" ) );

      MxAssert.assertEquals( aPOLineId, iDataSet.getString( "po_line_id" ) );
      MxAssert.assertEquals( "1", iDataSet.getString( "line_number" ) );
      MxAssert.assertEquals( "POLINE-1", iDataSet.getString( "line_desc" ) );
      MxAssert.assertEquals( "321005656-01-M1", iDataSet.getString( "part_number" ) );
      MxAssert.assertEquals( aOrderQty, iDataSet.getString( "order_qty" ) );
      MxAssert.assertEquals( "0 EA", iDataSet.getString( "received_qty" ) );
      MxAssert.assertEquals( "10000", iDataSet.getString( "unit_price" ) );
      MxAssert.assertEquals( aUnitStandardPrice, iDataSet.getString( "unit_standard_price" ) );

      MxAssert.assertEquals( CoreAdapterUtils.toDate( "2007-02-06 13:58:00" ),
            iDataSet.getDate( "promise_by_dt" ) );
      MxAssert.assertEquals( true, iDataSet.getBoolean( "promised_dt_confirmed" ) );
      MxAssert.assertEquals( "TEST_EXPENSE", iDataSet.getString( "account_cd" ) );
      MxAssert.assertEquals( "EXPENSE", iDataSet.getString( "account_type_cd" ) );
      MxAssert.assertEquals( null, iDataSet.getString( "ext_key_sdesc" ) );
      MxAssert.assertEquals( "TCODE-01", iDataSet.getString( "tcode_cd" ) );
      MxAssert.assertEquals( "ext-ref_123", iDataSet.getString( "po_line_ext_sdesc" ) );
      MxAssert.assertEquals( "This is the vendor note.", iDataSet.getString( "vendor_note" ) );
      MxAssert.assertEquals( "A319/320", iDataSet.getString( "owner_cd" ) );
      MxAssert.assertEquals( "This is the receiver note.", iDataSet.getString( "receiver_note" ) );
   }
}
