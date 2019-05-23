
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
import com.mxi.mx.core.key.CapacityPatternDayKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.shift.GetShiftsForDay.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetShiftsForDayTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetShiftsForDayTest.class );
   }


   /**
    * Tests the retrieval of shifts for the day.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetShiftsForDay() throws Exception {

      int lDbId = 4650;
      int lId = 50;
      int lDayOrd = 1;

      DataSet lDataSet = this.execute( new CapacityPatternDayKey( lDbId, lId, lDayOrd ) );

      lDataSet.next();
      testRow( lDataSet, 8.00, 8.00, 6.00 );
   }


   /**
    * This method executes the query in CapacityPattern.qrx
    *
    * @param aCapacityPatternDayKey
    *           the capacity pattern key.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( CapacityPatternDayKey aCapacityPatternDayKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aCapacityPatternDbId",
            aCapacityPatternDayKey.getCapacityPatternDbId() );
      lDataSetArgument.add( "aCapacityPatternId", aCapacityPatternDayKey.getCapacityPatternId() );
      lDataSetArgument.add( "aCapacityPatternDayOrd",
            aCapacityPatternDayKey.getCapacityPatternDayOrd() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aStartHour
    *           start hour for a shift.
    * @param aDuration
    *           duration of the shift
    * @param aWorkHours
    *           work hours for the shift
    */
   private void testRow( DataSet aDataSet, double aStartHour, double aDuration,
         double aWorkHours ) {

      MxAssert.assertEquals( "start_hour", aStartHour, aDataSet.getDouble( "start_hour" ) );
      MxAssert.assertEquals( "duration_qt", aDuration, aDataSet.getDouble( "duration_qt" ) );
      MxAssert.assertEquals( "work_hours_qt", aWorkHours, aDataSet.getDouble( "work_hours_qt" ) );
   }
}
