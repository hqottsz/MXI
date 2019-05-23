
package com.mxi.mx.web.query.task;

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
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.task.GetCurrUsageDetailsForFault.qrx
 *
 * @author twu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCurrUsageDetailsForFaultTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetCurrUsageDetailsForFaultTest.class );
   }


   /**
    * Tests the retrieval of fault start usage data.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetStartUsage() throws Exception {

      int lSchedDbId = 4650;
      int lSchedId = 105360;

      int lDataTypeDbId = 0;
      int lDataTypeId = 30;
      DataSet lDataSet = execute( new TaskKey( lSchedDbId, lSchedId ),
            new DataTypeKey( lDataTypeDbId, lDataTypeId ) );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "comp_curr_usage_tsn", 1302,
               lDataSet.getInt( "comp_curr_usage_tsn" ) );
         MxAssert.assertEquals( "mim_data_type_cd", "LANDING",
               lDataSet.getString( "mim_data_type_cd" ) );
      }
   }


   /**
    * This method executes the query in GetCurrUsageDetailsForFault.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @param aDataTypeKey
    *           the DataTypeKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey, DataTypeKey aDataTypeKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aSchedDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aSchedId", aTaskKey.getId() );
      lDataSetArgument.add( "aDataTypeDbId", aDataTypeKey.getDbId() );
      lDataSetArgument.add( "aDataTypeId", aDataTypeKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
