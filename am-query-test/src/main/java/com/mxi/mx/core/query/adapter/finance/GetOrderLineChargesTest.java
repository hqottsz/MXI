
package com.mxi.mx.core.query.adapter.finance;

import static org.junit.Assert.assertEquals;

import java.util.Date;

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
public final class GetOrderLineChargesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetOrderLineChargesTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where POs-POLines-Charges are not in date range.
    *
    * <ol>
    * <li>Query for all Charges in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLineChargesBeforeDate() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-06-01 10:00:00" ) );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where POLines-Charges for POs in the date range are retrieved.
    *
    * <ol>
    * <li>Query for all Charges in date range.</li>
    * <li>Verify that the resuts are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testLineChargesRetrieval() throws Exception {
      execute( CoreAdapterUtils.toDate( "2007-01-01 10:00:00" ) );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "CUSTOMS", "2" );

      iDataSet.next();
      testRow( "SHIPPING", "3" );
   }


   /**
    * Execute the query.
    *
    * @param aSinceDate
    *           the date after which all purchase orders are required.
    */
   private void execute( Date aSinceDate ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSinceDate", aSinceDate );

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
