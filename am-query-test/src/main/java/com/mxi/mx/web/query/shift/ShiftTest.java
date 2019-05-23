
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
 * This class tests the query com.mxi.mx.web.query.shift.Shift.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ShiftTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ShiftTest.class );
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
      testRow( lDataSet, "DAY", 6, 8.0, 6.0 );

      lDataSet.next();
      testRow( lDataSet, "NIGHT", 16, 8.0, 6.0 );

      lDataSet.next();
      testRow( lDataSet, "GRAVE", 22, 8.0, 6.0 );
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


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aShiftCode
    *           the user shift pattern code.
    * @param aStartHour
    *           the user shift pattern name.
    * @param aShiftDuration
    *           shift duration.
    * @param aWorkHours
    *           working hours for the shift
    */
   private void testRow( DataSet aDataSet, String aShiftCode, int aStartHour, double aShiftDuration,
         double aWorkHours ) {

      MxAssert.assertEquals( "shift_cd", aShiftCode, aDataSet.getString( "shift_cd" ) );

      MxAssert.assertEquals( "start_hour", aStartHour, aDataSet.getString( "start_hour" ) );

      MxAssert.assertEquals( "duration_qt", aShiftDuration, aDataSet.getString( "duration_qt" ) );

      MxAssert.assertEquals( "work_hours_qt", aWorkHours, aDataSet.getString( "work_hours_qt" ) );
   }
}
