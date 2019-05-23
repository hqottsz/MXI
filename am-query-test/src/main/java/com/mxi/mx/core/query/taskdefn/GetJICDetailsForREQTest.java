
package com.mxi.mx.core.query.taskdefn;

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
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.core.query.taskdefn.GetJICDetailsForREQ.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetJICDetailsForREQTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetJICDetailsForREQTest.class );
   }


   /**
    * Tests the retrieval of JIC details for a given requirement.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetJICDetails() throws Exception {

      int lDbId = 4650;
      int lId = 238646;

      DataSet lDataSet = this.execute( new TaskKey( lDbId, lId ) );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "SUBTASK_KEY", "4650:238647", lDataSet.getString( "SUBTASK_KEY" ) );
         MxAssert.assertEquals( "JIC_CD_NAME", "JC1 (JN1)", lDataSet.getString( "JIC_CD_NAME" ) );
         MxAssert.assertEquals( "TASK_DEF_STATUS_CD", "BUILD",
               lDataSet.getString( "TASK_DEF_STATUS_CD" ) );
      }
   }


   /**
    * This method executes the query in GetJICDetailsForREQ.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aTaskDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aTaskId", aTaskKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
