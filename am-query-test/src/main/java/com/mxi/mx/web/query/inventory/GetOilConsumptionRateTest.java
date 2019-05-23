
package com.mxi.mx.web.query.inventory;

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
 * This class tests the query com.mxi.mx.web.query.dropdown.UnitOfMeasure.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetOilConsumptionRateTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetOilConsumptionRateTest.class );
   }


   /** The UOM_KEY */
   private static final String OIL_CONSUMPTION_RATE = "5";


   /**
    * Tests the retrieval of unit of measure on the edit measurements screen..
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetUnitOfMeasure() throws Exception {

      int lInvNoDbId = 4650;
      int lInvNoId = 373781;

      // Retrieves the set of data for UOM.
      DataSet lDataSet = this.execute( new InventoryKey( lInvNoDbId, lInvNoId ) );

      lDataSet.next();

      this.testRow( lDataSet );
   }


   /**
    * This method executes the query in UnitOfMeasure.qrx
    *
    * @param aInventoryKey
    *           The data type key.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( InventoryKey aInventoryKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInvNoDbId", aInventoryKey.getDbId() );
      lDataSetArgument.add( "aInvNoId", aInventoryKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Verify the content of the row returned.
    *
    * @param aDataSet
    *           the dataset.
    */
   private void testRow( DataSet aDataSet ) {

      // Verify if the following are returned:
      MxAssert.assertEquals( "oil_consumption_rate", OIL_CONSUMPTION_RATE,
            aDataSet.getString( "oil_consumption_rate" ) );
   }
}
