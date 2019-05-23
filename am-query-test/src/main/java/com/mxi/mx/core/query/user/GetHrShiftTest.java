
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
 * This class tests the query com.mxi.mx.core.query.user.GetHrShift.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetHrShiftTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetHrShiftTest.class );
   }


   /**
    * Tests the retrieval of HR shifts.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHrUserShifts() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, 1, 4650, 1000, 0, "ENG", 1 );
   }


   /**
    * This method executes the query in GetHrShift.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aHrDbId", 4650 );
      lDataSetArgument.add( "aHrId", 6000007 );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aHrShiftId
    *           HR Shift Id.
    * @param aShiftDbId
    *           shift db id.
    * @param aShiftId
    *           shift id.
    * @param aLabourSkillDbId
    *           labour skill db id.
    * @param aLabourSkillCd
    *           labour skill code.
    * @param aLineCapacityBool
    *           line capacity bool value.
    */
   private void testRow( DataSet aDataSet, int aHrShiftId, int aShiftDbId, int aShiftId,
         int aLabourSkillDbId, String aLabourSkillCd, int aLineCapacityBool ) {

      MxAssert.assertEquals( "hr_shift_id", aHrShiftId, aDataSet.getString( "hr_shift_id" ) );
      MxAssert.assertEquals( "shift_db_id", aShiftDbId, aDataSet.getString( "shift_db_id" ) );
      MxAssert.assertEquals( "shift_id", aShiftId, aDataSet.getString( "shift_id" ) );
      MxAssert.assertEquals( "labour_skill_db_id", aLabourSkillDbId,
            aDataSet.getString( "labour_skill_db_id" ) );
      MxAssert.assertEquals( "labour_skill_cd", aLabourSkillCd,
            aDataSet.getString( "labour_skill_cd" ) );
      MxAssert.assertEquals( "line_capacity_bool", aLineCapacityBool,
            aDataSet.getInt( "line_capacity_bool" ) );
   }
}
