
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
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.shift.GetLabourSkills.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetLabourSkillsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetLabourSkillsTest.class );
   }


   /**
    * Tests the retrieval of labour skills available for the user's primary organization
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetLabourSkills() throws Exception {

      DataSet lDataSet =
            this.execute( new CapacityPatternKey( 4650, 40 ), new OrgKey( 4650, 100003 ) );

      lDataSet.next();
      testRow( lDataSet, "ENG", "Engineering", "Engineering" );

      lDataSet.next();
      testRow( lDataSet, "INSP", "Inspector", "Inspector" );

      lDataSet.next();
      testRow( lDataSet, "LBR", "Laborer", "Laborer" );

      lDataSet.next();
      testRow( lDataSet, "PILOT", "Pilot", "Pilot" );
   }


   /**
    * This method executes the query in GetLabourSkills.qrx
    *
    * @param aCapacityPatternKey
    *           the capacity pattern key.
    * @param aOrg
    *           the user's primary organization key
    *
    * @return The dataset after execution.
    */
   private DataSet execute( CapacityPatternKey aCapacityPatternKey, OrgKey aOrg ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aCapacityPatternKey, "aCapacityPatternDbId", "aCapacityPatternId" );
      lDataSetArgument.add( aOrg, "aOrgDbId", "aOrgId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    * Tests a row in the dataset.
    *
    * @param aDataSet
    *           the dataset
    * @param aCode
    *           the capacity pattern type code.
    * @param aShortDesc
    *           short description
    * @param aLongDesc
    *           long description
    */
   private void testRow( DataSet aDataSet, String aCode, String aShortDesc, String aLongDesc ) {

      MxAssert.assertEquals( "labour_skill_cd", aCode, aDataSet.getString( "labour_skill_cd" ) );

      MxAssert.assertEquals( "desc_sdesc", aShortDesc, aDataSet.getString( "class_name" ) );

      MxAssert.assertEquals( "desc_ldesc", aLongDesc, aDataSet.getString( "class_desc" ) );
   }
}
