
package com.mxi.mx.core.query.shift;

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
 * This class tests the query com.mxi.mx.core.query.shift.FindDuplicateUserShiftPatternCode.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FindDuplicateUserShiftPatternCodeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            FindDuplicateUserShiftPatternCodeTest.class );
   }


   /**
    * Tests the retrieval of duplicate user shift pattern codes.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testUserShiftCode() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, 1000 );
   }


   /**
    * This method executes the query in FindDuplicateUserShiftPatternCode.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aUserShiftPatternCd", "ABQ/LINE-STDDAY" );
      lDataSetArgument.add( "aUserShiftPatternDbId", 4650 );
      lDataSetArgument.add( "aUserShiftPatternId", 1055 );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aUserShiftPatternId
    *           user shift pattern id.
    */
   private void testRow( DataSet aDataSet, int aUserShiftPatternId ) {

      MxAssert.assertEquals( "user_shift_pattern_id", aUserShiftPatternId,
            aDataSet.getString( "user_shift_pattern_id" ) );
   }
}
