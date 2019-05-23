
package com.mxi.mx.web.query.dropdown;

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
 * This class tests the query com.mxi.mx.web.query.shift.CapacityPatternType.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CapacityPatternTypeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), CapacityPatternTypeTest.class );
   }


   /**
    * Tests the retrieval of capacity patterns.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCapacityPatternType() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, "DAILY" );

      lDataSet.next();
      testRow( lDataSet, "MONTHLY" );

      lDataSet.next();
      testRow( lDataSet, "WEEKLY" );
   }


   /**
    * This method executes the query in CapacityPatternType.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aCode
    *           the capacity pattern type code.
    */
   private void testRow( DataSet aDataSet, String aCode ) {

      MxAssert.assertEquals( "capacity_pattern_type_cd", aCode,
            aDataSet.getString( "capacity_pattern_type_cd" ) );
   }
}
