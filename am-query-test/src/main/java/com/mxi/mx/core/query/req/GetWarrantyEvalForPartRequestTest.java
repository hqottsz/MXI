
package com.mxi.mx.core.query.req;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

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
public final class GetWarrantyEvalForPartRequestTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetWarrantyEvalForPartRequestTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where the Part Request is not having warranty evaluation results.
    *
    * <ol>
    * <li>Query by Part Request id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testWarrantyEvalForPartReqDoesNotExists() throws Exception {
      execute( 4650, 1001 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where the Part Request is having warranty evaluation results.
    *
    * <ol>
    * <li>Query by Part Request id.</li>
    * <li>Verify that the resuts are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the resuts are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testWarrantyEvalForPartReqExists() throws Exception {
      execute( 4650, 1000 );
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();

      testRow( "4650:3000:300:30", new BigDecimal( "11.0" ), new BigDecimal( "12.0" ) );
   }


   /**
    * Execute the query.
    *
    * @param aReqPartDbId
    *           the Req Part db id.
    * @param aReqPartId
    *           the Req Part id.
    */
   private void execute( int aReqPartDbId, int aReqPartId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aReqPartDbId", aReqPartDbId );
      lArgs.add( "aReqPartId", aReqPartId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row of the dataset.
    *
    * @param aEvalPartKey
    *           the eval part key value.
    * @param aUnitPriceThresholdQt
    *           the unit price threshold value.
    * @param aTurnInPriceThresholdQt
    *           the turn-in price threshold value.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aEvalPartKey, BigDecimal aUnitPriceThresholdQt,
         BigDecimal aTurnInPriceThresholdQt ) throws Exception {
      MxAssert.assertEquals( aEvalPartKey, iDataSet.getString( "eval_part_key" ) );
      MxAssert.assertEquals( aUnitPriceThresholdQt,
            iDataSet.getBigDecimal( "unit_price_threshold_qt" ) );
      MxAssert.assertEquals( aTurnInPriceThresholdQt,
            iDataSet.getBigDecimal( "turn_in_price_threshold_qt" ) );
   }
}
