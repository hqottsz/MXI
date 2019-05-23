
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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOrderLineChargesByIdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetOrderLineChargesByIdTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where PO id does not exists.
    *
    * <ol>
    * <li>Query for all Charges by id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLineChargesNotExists() throws Exception {
      execute( 4650, 107108 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where POLines-Charges for POs are retrieved.
    *
    * <ol>
    * <li>Query for all Charges by id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLineChargesRetrievalById() throws Exception {
      execute( 4650, 107107 );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "CUSTOMS", "2" );

      iDataSet.next();
      testRow( "SHIPPING", "3" );
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
    * @param aChargeTypeCd
    *           the charge type code.
    * @param aChargeAmount
    *           the charge amount.
    */
   private void testRow( String aChargeTypeCd, String aChargeAmount ) {
      MxAssert.assertEquals( aChargeTypeCd, iDataSet.getString( "charge_code" ) );
      MxAssert.assertEquals( aChargeAmount, iDataSet.getString( "charge_amount" ) );
   }
}
