
package com.mxi.mx.web.query.user;

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
 * This class tests the query com.mxi.mx.web.query.shift.GetShiftDetails.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetShiftDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetShiftDetailsTest.class );
   }


   /**
    * Tests the retrieval of shift details.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testShiftDetails() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, "ABQ/LINE", "NIGHT", 6000011 );
   }


   /**
    * This method executes the query in GetShiftDetails.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aHrDbId", 4650 );
      lDataSetArgument.add( "aHrId", 6000008 );
      lDataSetArgument.add( "aHrShiftId", 2 );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset.
    * @param aLocCode
    *           location code.
    * @param aShiftCode
    *           shift code.
    * @param aUserId
    *           user Id.
    */
   private void testRow( DataSet aDataSet, String aLocCode, String aShiftCode, int aUserId ) {

      MxAssert.assertEquals( "loc_cd", aLocCode, aDataSet.getString( "loc_cd" ) );

      MxAssert.assertEquals( "shift_cd", aShiftCode, aDataSet.getString( "shift_cd" ) );

      MxAssert.assertEquals( "user_id", aUserId, aDataSet.getString( "user_id" ) );
   }
}
