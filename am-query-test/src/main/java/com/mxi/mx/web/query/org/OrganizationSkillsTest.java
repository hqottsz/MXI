
package com.mxi.mx.web.query.org;

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
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OrgSkillMapKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the com.mxi.mx.web.query.org.OrganizationSkills.qrx query.
 *
 * @author sarya
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OrganizationSkillsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), OrganizationSkillsTest.class );
   }


   private static final OrgKey ORG_KEY = new OrgKey( 4650, 100003 );
   private static final String EXPECTED_SKILL_CD_IN_POSITION1 = "CLN";
   private static final String EXPECTED_SKILL_CD_IN_POSITION2 = "LBR";
   private static final OrgSkillMapKey EXPECTED_SKILL_IN_POSITION1 =
         new OrgSkillMapKey( 4650, 100003, 4650, "CLN" );
   private static final OrgSkillMapKey EXPECTED_SKILL_IN_POSITION2 =
         new OrgSkillMapKey( 4650, 100003, 4650, "LBR" );


   /**
    * Test Case 1: Test that the query returns same skills which are assigned to the selected
    * organization
    */
   @Test
   public void testOrganizationSkills() {
      DataSet lDataSet = execute( ORG_KEY );

      // Assert number of rows returned
      assertEquals( 2, lDataSet.getRowCount() );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_SKILL_IN_POSITION1, EXPECTED_SKILL_CD_IN_POSITION1 );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_SKILL_IN_POSITION2, EXPECTED_SKILL_CD_IN_POSITION2 );
   }


   /**
    * Test row contents returned
    *
    * @param aDataSet
    *           the dataset
    * @param aExpectedOrgSkill
    *           the expected org skill map key
    * @param aExpectedSkillCd
    *           the expected skill code
    */
   private void assertRow( DataSet aDataSet, OrgSkillMapKey aExpectedOrgSkill,
         String aExpectedSkillCd ) {

      MxAssert.assertEquals( "org_labour_skill_key", aExpectedOrgSkill,
            aDataSet.getKey( OrgSkillMapKey.class, "org_labour_skill_key" ) );
      MxAssert.assertEquals( "labour_skill_cd", aExpectedSkillCd,
            aDataSet.getString( "labour_skill_cd" ) );
      MxAssert.assertEquals( "est_hourly_cost", 12.0, aDataSet.getDouble( "est_hourly_cost" ) );
      MxAssert.assertEquals( "esig_req_bool", true, aDataSet.getBoolean( "esig_req_bool" ) );
   }


   /**
    * Executes the query.
    *
    * @param aKey
    *           the organization key.
    *
    * @return {@link DataSet}
    */
   private DataSet execute( OrgKey aKey ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "aOrgDbId", "aOrgId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
