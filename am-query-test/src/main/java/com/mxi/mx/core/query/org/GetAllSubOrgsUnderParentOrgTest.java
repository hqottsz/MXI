
package com.mxi.mx.core.query.org;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the GetAllSubOrgsUnderParentOrg query.
 *
 * @author sarya
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAllSubOrgsUnderParentOrgTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetAllSubOrgsUnderParentOrgTest.class );
   }


   private static final OrgKey ORG_MXI = new OrgKey( 0, 1 );
   private static final OrgKey SUB_ORG_MXI_R_D = new OrgKey( 4650, 1 );
   private static final OrgKey SUB_ORG_MXI_SERVICES = new OrgKey( 4650, 2 );
   private static final OrgKey SUB_ORG_MXI_R_D_TEAM = new OrgKey( 4650, 3 );

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Asserts the database row is equal to expected data
    *
    * @param aData
    *           The Data
    * @param aOrgKey
    *           The organization
    */
   public void assertRow( DataSet aData, OrgKey aOrgKey ) {

      // Assert Key
      MxAssert.assertEquals( aOrgKey.getDbId(), aData.getInt( "org_db_id" ) );
      MxAssert.assertEquals( aOrgKey.getId(), aData.getInt( "org_id" ) );
   }


   /**
    * Test the sub-organizations of the organization
    *
    * <ol>
    * <li>Query by organization key</li>
    * <li>Verify that the sub organizations are returned by the query.</li> *
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testBasicQuery() throws Exception {
      execute( ORG_MXI );

      // organization key
      MxAssert.assertTrue( iDataSet.next() );
      assertRow( iDataSet, ORG_MXI );

      // sub-organization key
      MxAssert.assertTrue( iDataSet.next() );
      assertRow( iDataSet, SUB_ORG_MXI_R_D );

      // sub-sub-organization key
      MxAssert.assertTrue( iDataSet.next() );
      assertRow( iDataSet, SUB_ORG_MXI_R_D_TEAM );

      MxAssert.assertTrue( iDataSet.next() );
      assertRow( iDataSet, SUB_ORG_MXI_SERVICES );

      MxAssert.assertFalse( iDataSet.next() );
   }


   /**
    * Execute the query.
    *
    * @param aOrg
    *           the organization key
    */
   private void execute( OrgKey aOrg ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aOrg, "aOrgDbId", "aOrgId" );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
