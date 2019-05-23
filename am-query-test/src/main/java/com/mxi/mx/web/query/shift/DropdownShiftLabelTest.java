
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
 * This class tests the query com.mxi.mx.web.query.shift.DropdownShiftLabel.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class DropdownShiftLabelTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), DropdownShiftLabelTest.class );
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
      testRow( lDataSet, "DAY", "Day Shift" );

      lDataSet.next();
      testRow( lDataSet, "GRAVE", "Graveyard Shift" );

      lDataSet.next();
      testRow( lDataSet, "NIGHT", "Night Shift" );

      lDataSet.next();
      testRow( lDataSet, "OFF", null );
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
    *           the shift code.
    * @param aShiftName
    *           the shift name.
    */
   private void testRow( DataSet aDataSet, String aShiftCode, String aShiftName ) {

      MxAssert.assertEquals( "shift_cd", aShiftCode, aDataSet.getString( "shift_cd" ) );

      MxAssert.assertEquals( "shift_name", aShiftName, aDataSet.getString( "shift_name" ) );
   }
}
