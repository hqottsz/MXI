
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
 * This class tests the query com.mxi.mx.web.query.shift.GetShiftPatternDetail.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetShiftPatternDetailTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetShiftPatternDetailTest.class );
   }


   /**
    * Tests the retrieval of HR user shift patterns.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testShiftLabel() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, "MSP/LINE", "STDNITE", "ENG", 6000011 );
   }


   /**
    * This method executes the query in GetShiftPatternDetail.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aHrDbId", 4650 );
      lDataSetArgument.add( "aHrId", 6000008 );
      lDataSetArgument.add( "aHrScheduleId", 2 );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aLocCode
    *           localtion code.
    * @param aShiftCode
    *           shift code.
    * @param aLabourSkillCode
    *           labour skill code.
    * @param aUserId
    *           User Id.
    */
   private void testRow( DataSet aDataSet, String aLocCode, String aShiftCode,
         String aLabourSkillCode, int aUserId ) {

      MxAssert.assertEquals( "loc_cd", aLocCode, aDataSet.getString( "loc_cd" ) );

      MxAssert.assertEquals( "user_shift_pattern_cd", aShiftCode,
            aDataSet.getString( "user_shift_pattern_cd" ) );

      MxAssert.assertEquals( "labour_skill_cd", aLabourSkillCode,
            aDataSet.getString( "labour_skill_cd" ) );

      MxAssert.assertEquals( "user_id", aUserId, aDataSet.getString( "user_id" ) );
   }
}
