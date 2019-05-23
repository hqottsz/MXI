
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
import com.mxi.mx.core.key.CapacityPatternKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.shift.GetCapacityPatternDaySkills.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCapacityPatternDaySkillsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetCapacityPatternDaySkillsTest.class );
   }


   /**
    * Tests the retrieval of skills associated to a capacity pattern.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCapacityPatternByCapacityKey() throws Exception {

      int lDbId = 4650;
      int lId = 3;

      DataSet lDataSet = this.execute( new CapacityPatternKey( lDbId, lId ) );

      while ( lDataSet.next() ) {

         MxAssert.assertEquals( "labour_skill_cd", "LBR", lDataSet.getString( "labour_skill_cd" ) );

         MxAssert.assertEquals( "lic_resource_qt", "10", lDataSet.getInteger( "lic_resource_qt" ) );

         MxAssert.assertEquals( "unlic_resource_qt", "15",
               lDataSet.getInteger( "unlic_resource_qt" ) );
      }
   }


   /**
    * This method executes the query in CapacityPattern.qrx
    *
    * @param aCapacityPatternKey
    *           the capacity pattern key.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( CapacityPatternKey aCapacityPatternKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aCapacityPatternDbId", aCapacityPatternKey.getCapacityPatternDbId() );
      lDataSetArgument.add( "aCapacityPatternId", aCapacityPatternKey.getCapacityPatternId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
