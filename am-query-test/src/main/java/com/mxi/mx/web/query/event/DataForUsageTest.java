package com.mxi.mx.web.query.event;

import static org.junit.Assert.assertEquals;

import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Test DataForUsage.qrx
 *
 */
public class DataForUsageTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), DataForUsageTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;

   private String USAGE_RECORD_ID = "8B83C41DF596410D94786A0924C905C6";


   /**
    * Test DataForUsage query selection.
    *
    * <ol>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDataForUsageSelection() throws Exception {
      // ARRANGE
      loadData();

      // ACT
      execute( USAGE_RECORD_ID );

      // ASSERT
      assertEquals( 1, iDataSet.getRowCount() );
      iDataSet.next();
      testRow( "4650:299865", "MXCORRECTION", "HOURS", 10, 11, 10, 12, 10, 13 );

   }


   /**
    * Execute the query.
    *
    * @param aUsageRecordId
    *           the usage record id
    */
   private void execute( String aUsageRecordId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUsageRecordId", aUsageRecordId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aInventoryKey
    *           the inventory key.
    * @param aUsageTypeCd
    *           the usage type code.
    * @param aDataTypeCd
    *           the data type code
    * @param aTsnBefore
    *           the TSN before value
    * @param aTsnAfter
    *           the TSN after value
    * @param aTsoBefore
    *           the TSO before value
    * @param aTsoAfter
    *           the TSO after value
    * @param aTsiBefore
    *           the TSI before value
    * @param aTsiAfter
    *           the TSI after value
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInventoryKey, String aUsageTypeCd, String aDataTypeCd,
         int aTsnBefore, int aTsnAfter, int aTsoBefore, int aTsoAfter, int aTsiBefore,
         int aTsiAfter ) throws Exception {

      MxAssert.assertEquals( aInventoryKey, iDataSet.getString( "inv_key" ) );
      MxAssert.assertEquals( aUsageTypeCd, iDataSet.getString( "usage_type_cd" ) );
      MxAssert.assertEquals( aDataTypeCd, iDataSet.getString( "data_type_cd" ) );
      MxAssert.assertEquals( aTsnBefore, iDataSet.getInt( "tsn_before" ) );
      MxAssert.assertEquals( aTsnAfter, iDataSet.getInt( "tsn_qt" ) );
      MxAssert.assertEquals( aTsnBefore, iDataSet.getInt( "tso_before" ) );
      MxAssert.assertEquals( aTsoAfter, iDataSet.getInt( "tso_qt" ) );
      MxAssert.assertEquals( aTsnBefore, iDataSet.getInt( "tsi_before" ) );
      MxAssert.assertEquals( aTsiAfter, iDataSet.getInt( "tsi_qt" ) );
   }
}
