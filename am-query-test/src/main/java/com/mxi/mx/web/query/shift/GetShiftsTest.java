
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
 * This class tests the query com.mxi.mx.web.query.shift.GetShifts.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetShiftsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetShiftsTest.class );
   }


   /**
    * Tests the retrieval of shifts.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testShiftLabel() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, 1, "DAY (Day Shift)", 1000 );
   }


   /**
    * This method executes the query in CapacityPattern.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aUserShiftPatternDbId", 4650 );
      lDataSetArgument.add( "aUserShiftPatternId", 1000 );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aDayOrd
    *           day order.
    * @param aShiftDesc
    *           shift description
    * @param aShiftId
    *           shift id
    */
   private void testRow( DataSet aDataSet, int aDayOrd, String aShiftDesc, int aShiftId ) {

      MxAssert.assertEquals( "user_shift_pattern_day_ord", aDayOrd,
            aDataSet.getString( "user_shift_pattern_day_ord" ) );

      MxAssert.assertEquals( "shift_desc", aShiftDesc, aDataSet.getString( "shift_desc" ) );

      MxAssert.assertEquals( "shift_id", aShiftId, aDataSet.getString( "shift_id" ) );
   }
}
