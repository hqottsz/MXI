
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
import com.mxi.mx.core.key.CapacityPatternKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.shift.CapacityPatternByCapacityKey.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CapacityPatternByCapacityKeyTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            CapacityPatternByCapacityKeyTest.class );
   }


   /**
    * Tests the retrieval of a capacity patterns using the CapacityPatternKey.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCapacityPatternByCapacityKey() throws Exception {

      int lDbId = 4650;
      int lId = 2;

      DataSet lDataSet = this.execute( new CapacityPatternKey( lDbId, lId ) );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "capacity_pattern_cd", "CP02",
               lDataSet.getString( "capacity_pattern_cd" ) );

         MxAssert.assertEquals( "capacity_pattern_name", "Capacity Pattern 2",
               lDataSet.getString( "capacity_pattern_name" ) );

         MxAssert.assertEquals( "capacity_pattern_ldesc", "7 days pattern",
               lDataSet.getString( "capacity_pattern_ldesc" ) );
      }
   }


   /**
    * This method executes the query in CapacityPatternByCapacityKey.qrx
    *
    * @param aCapacityPatternKey
    *           the CapacityPatternKey object.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( CapacityPatternKey aCapacityPatternKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aCapacityPatternDbId", aCapacityPatternKey.getCapacityPatternDbId() );
      lDataSetArgument.add( "aCapacityPatternId", aCapacityPatternKey.getCapacityPatternId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
