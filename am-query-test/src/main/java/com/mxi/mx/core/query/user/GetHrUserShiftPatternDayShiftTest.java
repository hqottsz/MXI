
package com.mxi.mx.core.query.user;

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
 * This class tests the query com.mxi.mx.core.query.user.GetHrUserShiftPatternDayShift.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetHrUserShiftPatternDayShiftTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetHrUserShiftPatternDayShiftTest.class );
   }


   /**
    * Tests the retrieval of HR user shift pattern day shifts.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHrUserShifts() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, 4650, 1000, 1, 6, 8 );

      lDataSet.next();
      testRow( lDataSet, 4650, 1000, 2, 6, 8 );

      lDataSet.next();
      testRow( lDataSet, 4650, 1000, 3, 6, 8 );

      lDataSet.next();
      testRow( lDataSet, 4650, 1000, 4, 6, 8 );

      lDataSet.next();
      testRow( lDataSet, 4650, 1000, 5, 6, 8 );
   }


   /**
    * This method executes the query in GetHrUserShiftPatternDayShift.qrx
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
    * @param aShiftDbId
    *           shift db id.
    * @param aShiftId
    *           shift id.
    * @param aUserShiftPatternDayOrd
    *           user shift pattern day order.
    * @param aDurationQt
    *           duration property.
    * @param aWorkHoursQt
    *           work hour property.
    */
   private void testRow( DataSet aDataSet, int aShiftDbId, int aShiftId,
         int aUserShiftPatternDayOrd, int aDurationQt, int aWorkHoursQt ) {

      MxAssert.assertEquals( "shift_db_id", aShiftDbId, aDataSet.getString( "shift_db_id" ) );

      MxAssert.assertEquals( "shift_id", aShiftId, aDataSet.getString( "shift_id" ) );

      MxAssert.assertEquals( "user_shift_pattern_day_ord", aUserShiftPatternDayOrd,
            aDataSet.getString( "user_shift_pattern_day_ord" ) );

      MxAssert.assertEquals( "duration_qt", aDurationQt, aDataSet.getInt( "duration_qt" ) );

      MxAssert.assertEquals( "work_hours_qt", aWorkHoursQt, aDataSet.getInt( "work_hours_qt" ) );
   }
}
