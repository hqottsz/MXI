
package com.mxi.mx.web.query.shift;

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
 * This class tests the query com.mxi.mx.web.query.shift.CapacityPattern.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CapacityPatternTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), CapacityPatternTest.class );
   }


   /**
    * Tests the retrieval of capacity patterns.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCapacityPattern() throws Exception {

      DataSet lDataSet = this.execute();

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "capacity_pattern_cd", "CP01",
               lDataSet.getString( "capacity_pattern_cd" ) );

         MxAssert.assertEquals( "capacity_pattern_name", "Capacity Pattern 1",
               lDataSet.getString( "capacity_pattern_name" ) );

         MxAssert.assertEquals( "capacity_pattern_ldesc", "7 day pattern",
               lDataSet.getString( "capacity_pattern_ldesc" ) );
      }
   }


   /**
    * This method executes the query in CapacityPattern.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
