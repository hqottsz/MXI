
package com.mxi.mx.web.query.req;

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
import com.mxi.mx.core.key.PartRequestKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPartRequestPrefVendorTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPartRequestPrefVendorTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where preferred vendor exists for the part(which is removed part) in the Part
    * Request and the order type is EXCHANGE.
    *
    * <ol>
    * <li>Query by Part Request id and Order type.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testPrefExchangeVendorExists() throws Exception {
      execute( new PartRequestKey( 4650, 1001 ), "EXCHANGE" );
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      assertEquals( "4650:6001", iDataSet.getString( "vendor_key" ) );
   }


   /**
    * Test the case where preferred vendor exists for the part(which is removed part) in the Part
    * Request and the order type is PURCHASE.
    *
    * <ol>
    * <li>Query by Part Request id and Order type.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testPrefPurchaseVendorExists() throws Exception {
      execute( new PartRequestKey( 4650, 1000 ), "PURCHASE" );
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      assertEquals( "4650:6000", iDataSet.getString( "vendor_key" ) );
   }


   /**
    * Test the case where preferred vendor exists for the part(which is removed part) in the Part
    * Request and the order type is REPAIR.
    *
    * <ol>
    * <li>Query by Part Request id and Order type.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testPrefRepairVendorExists() throws Exception {
      execute( new PartRequestKey( 4650, 1002 ), "REPAIR" );
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      assertEquals( "4650:6002", iDataSet.getString( "vendor_key" ) );
   }


   /**
    * Test the case where preferred vendor does not exists for the part(which is removed part) in
    * the Part Request.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testPrefVendorDoesNotExists() throws Exception {
      execute( new PartRequestKey( 4650, 1000 ), "REPAIR" );
      assertEquals( 0, iDataSet.getRowCount() );

      execute( new PartRequestKey( 4650, 1001 ), "PURCHASE" );
      assertEquals( 0, iDataSet.getRowCount() );

      execute( new PartRequestKey( 4650, 1002 ), "EXCHANGE" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aPartReq
    *           the user id.
    * @param aOrderTypeCd
    *           the order type.
    */
   private void execute( PartRequestKey aPartReq, String aOrderTypeCd ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aPartReq, "aReqPartDbId", "aReqPartId" );
      lArgs.add( "aOrderTypeCd", aOrderTypeCd );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
