
package com.mxi.mx.core.query.inventory;

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
 * This class tests the query com.mxi.mx.core.query.inventory.GetInventoryFromPartRequestBarcode.qrx
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetInventoryFromPartRequestBarcodeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetInventoryFromPartRequestBarcodeTest.class );
   }


   /**
    * Tests the retrieval of the measurements item.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetMeasurementItems() throws Exception {

      String lBarcode = "R48U00025NZ";

      DataSet lDataSet = this.execute( lBarcode );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDataSet.getRowCount() );
      lDataSet.next();
      MxAssert.assertEquals( "req_part", "4650:138326", lDataSet.getString( "req_part" ) );
      MxAssert.assertEquals( "req_qt", "1", lDataSet.getInt( "req_qt" ) );
      MxAssert.assertEquals( "issue_to_account", "4650:112",
            lDataSet.getString( "issue_to_account" ) );
      MxAssert.assertEquals( "serial_no_oem", "SN000281", lDataSet.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "lot_oem_tag", null, lDataSet.getString( "lot_oem_tag" ) );
      MxAssert.assertEquals( "batch_no_oem", null, lDataSet.getString( "batch_no_oem" ) );
      MxAssert.assertEquals( "barcode_sdesc", "I40S000TEGG",
            lDataSet.getString( "barcode_sdesc" ) );
      MxAssert.assertEquals( "location_key", "4650:1011", lDataSet.getString( "location_key" ) );

      MxAssert.assertEquals( "part_no_oem", "211001802-UNK", lDataSet.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "manufact_cd", "1000", lDataSet.getString( "manufact_cd" ) );
   }


   /**
    * This method executes the query in GetInventoryFromPartRequestBarcode.qrx
    *
    * @param aBarcode
    *           The barcode.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( String aBarcode ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aPartRequestBarcode", aBarcode );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
