
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
 * This class tests the query com.mxi.mx.web.query.shift.GetHRUserShiftPattern.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetHRUserShiftPatternTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetHRUserShiftPatternTest.class );
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
      testRow( lDataSet, "ENG", 6000010, "Elise Do" );

      lDataSet.next();
      testRow( lDataSet, "ENG", 6000011, "Yungjae Cho" );
   }


   /**
    * This method executes the query in GetHRUserShiftPattern.qrx
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
    * @param aLabourSkillCode
    *           labour skill code.
    * @param aUserId
    *           User Id.
    * @param aFullName
    *           full name.
    */
   private void testRow( DataSet aDataSet, String aLabourSkillCode, int aUserId,
         String aFullName ) {

      MxAssert.assertEquals( "labour_skill_cd", aLabourSkillCode,
            aDataSet.getString( "labour_skill_cd" ) );

      MxAssert.assertEquals( "user_id", aUserId, aDataSet.getString( "user_id" ) );

      MxAssert.assertEquals( "full_name", aFullName, aDataSet.getString( "full_name" ) );
   }
}
