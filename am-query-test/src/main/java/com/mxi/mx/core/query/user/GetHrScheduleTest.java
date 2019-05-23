
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
 * This class tests the query com.mxi.mx.core.query.user.GetHrSchedule.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetHrScheduleTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetHrScheduleTest.class );
   }


   /**
    * Tests the retrieval of HR schedules.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHrUserSchedules() throws Exception {

      DataSet lDataSet = this.execute();

      lDataSet.next();
      testRow( lDataSet, 1004, 10, "QA", 1 );
   }


   /**
    * This method executes the query in GetHrSchedule.qrx
    *
    * @return The dataset after execution.
    */
   private DataSet execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aHrDbId", 4650 );
      lDataSetArgument.add( "aHrId", 107 );

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
    * @param aLabourSkillDbId
    *           labour skill db id.
    * @param aLabourSkillCd
    *           labour skill code.
    * @param aLineCapacityBool
    *           line capacity bool value.
    */
   private void testRow( DataSet aDataSet, int aUserShiftPatternId, int aLabourSkillDbId,
         String aLabourSkillCd, int aLineCapacityBool ) {

      MxAssert.assertEquals( "user_shift_pattern_id", aUserShiftPatternId,
            aDataSet.getString( "user_shift_pattern_id" ) );
      MxAssert.assertEquals( "labour_skill_db_id", aLabourSkillDbId,
            aDataSet.getString( "labour_skill_db_id" ) );
      MxAssert.assertEquals( "labour_skill_cd", aLabourSkillCd,
            aDataSet.getString( "labour_skill_cd" ) );

      MxAssert.assertEquals( "line_capacity_bool", aLineCapacityBool,
            aDataSet.getInt( "line_capacity_bool" ) );
   }
}
