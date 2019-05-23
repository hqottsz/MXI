
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
 * This class tests the query com.mxi.mx.web.query.shift.UserShiftPattern.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UserShiftPatternTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UserShiftPatternTest.class );
   }


   /**
    * Tests the retrieval of user shift patterns.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testShiftLabel() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, "STDGRAVE", "Standard Week, Graveyard" );

      lDataSet.next();
      testRow( lDataSet, "STDLINE", "Standard Week, Line" );

      lDataSet.next();
      testRow( lDataSet, "STDNITE", "Standard Week, Night" );
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
    * @param aUserShiftCode
    *           the user shift pattern code.
    * @param aUserShiftName
    *           the user shift pattern name.
    */
   private void testRow( DataSet aDataSet, String aUserShiftCode, String aUserShiftName ) {

      MxAssert.assertEquals( "user_shift_pattern_cd", aUserShiftCode,
            aDataSet.getString( "user_shift_pattern_cd" ) );

      MxAssert.assertEquals( "user_shift_pattern_name", aUserShiftName,
            aDataSet.getString( "user_shift_pattern_name" ) );
   }
}
