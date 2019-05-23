
package com.mxi.mx.web.query.dropdown;

import static org.junit.Assert.assertEquals;

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
 * This class tests the query com.mxi.mx.web.query.dropdown.LabourSkillByUserOrg.qrx
 *
 * @author sarya
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class LabourSkillByUserOrgTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), LabourSkillByUserOrgTest.class );
   }


   private static final String EXPECTED_SKILL_CD_IN_POSITION1 = "CLN";
   private static final String EXPECTED_SKILL_CD_IN_POSITION2 = "ENG";
   private static final String EXPECTED_SKILL_CD_IN_POSITION3 = "LBR";
   private static final String EXPECTED_SKILL_CD_IN_POSITION4 = "PILOT";


   /**
    * Tests the labour skills applicable to the user's primary organization
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetLabourSkillByUserOrg() throws Exception {

      int lUserId = 106;

      // Retrieves labour skills applicable to the user's primary organization
      DataSet lDataSet = this.execute( lUserId );

      // Assert number of rows returned
      assertEquals( 4, lDataSet.getRowCount() );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_SKILL_CD_IN_POSITION1 );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_SKILL_CD_IN_POSITION2 );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_SKILL_CD_IN_POSITION3 );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_SKILL_CD_IN_POSITION4 );
   }


   /**
    * Test row contents returned
    *
    * @param aDataSet
    *           the dataset
    * @param aExpectedSkillCd
    *           the expected skill code
    */
   private void assertRow( DataSet aDataSet, String aExpectedSkillCd ) {

      MxAssert.assertEquals( "labour_skill_cd", aExpectedSkillCd,
            aDataSet.getString( "labour_skill_cd" ) );
   }


   /**
    * This method executes the query LabourSkillByUserOrg.qrx
    *
    * @param aUserId
    *           The user id
    *
    * @return The dataset after execution.
    */
   private DataSet execute( int aUserId ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aUserId", aUserId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
