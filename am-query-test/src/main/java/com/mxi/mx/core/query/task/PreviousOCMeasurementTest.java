
package com.mxi.mx.core.query.task;

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
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.core.query.task.PreviousMeasurement.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PreviousOCMeasurementTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), PreviousOCMeasurementTest.class );
   }


   /**
    * Tests the retrieval of previous measurements.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testPreviousMeasurement() throws Exception {

      int lInvNoDbId = 4650;
      int lInvNoId = 373504;
      int lDataTypeDbId = 4650;
      int lDataTypeId = 3007;
      int lRank = 10;

      DataSet lDataSet = this.execute( new InventoryKey( lInvNoDbId, lInvNoId ),
            new DataTypeKey( lDataTypeDbId, lDataTypeId ), lRank );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "MEASUREMENT_DATE", "25-Nov-2009 11:10:07",
               lDataSet.getString( "MEASUREMENT_DATE" ) );
         MxAssert.assertEquals( "UPTAKE_QT", "22.0", lDataSet.getString( "UPTAKE_QT" ) );
         MxAssert.assertEquals( "REC_ENG_UNIT_KEY", "4650:EA",
               lDataSet.getString( "REC_ENG_UNIT_KEY" ) );
         MxAssert.assertEquals( "ENGOIL_MEASUREMENT", "4650:3013",
               lDataSet.getString( "ENGOIL_MEASUREMENT" ) );
         MxAssert.assertEquals( "DELTA_EOT", "1", lDataSet.getString( "DELTA_EOT" ) );
         MxAssert.assertEquals( "RATE", "22", lDataSet.getString( "RATE" ) );
         MxAssert.assertEquals( "EOT", "3.0", lDataSet.getString( "EOT" ) );
         MxAssert.assertEquals( "REC_ENGOIL", "22.0", lDataSet.getString( "REC_ENGOIL" ) );
         MxAssert.assertEquals( "REC_CD", "EA", lDataSet.getString( "REC_CD" ) );
         MxAssert.assertEquals( "DATA_TYPE_CD", "EOT", lDataSet.getString( "DATA_TYPE_CD" ) );
         MxAssert.assertEquals( "MEASUREMENT_UNIT_CD", "EA",
               lDataSet.getString( "MEASUREMENT_UNIT_CD" ) );
         MxAssert.assertEquals( "ENTRY_PREC_QT", "2", lDataSet.getString( "ENTRY_PREC_QT" ) );
         MxAssert.assertEquals( "RANK", "1", lDataSet.getString( "RANK" ) );
      }
   }


   /**
    * This method executes the query in PreviousMeasurement.qrx
    *
    * @param aInventoryKey
    *           The inventory key.
    * @param aDataTypeKey
    *           The data type key.
    * @param aRank
    *           DOCUMENT_ME
    *
    * @return The dataset after execution.
    */
   private DataSet execute( InventoryKey aInventoryKey, DataTypeKey aDataTypeKey, int aRank ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInvNoDbId", aInventoryKey.getDbId() );
      lDataSetArgument.add( "aInvNoId", aInventoryKey.getId() );
      lDataSetArgument.add( "aDataTypeDbId", aDataTypeKey.getDbId() );
      lDataSetArgument.add( "aDataTypeId", aDataTypeKey.getId() );
      lDataSetArgument.add( "aRank", 10 );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
