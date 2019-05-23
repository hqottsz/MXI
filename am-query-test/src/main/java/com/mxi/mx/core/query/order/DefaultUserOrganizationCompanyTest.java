
package com.mxi.mx.core.query.order;

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
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class DefaultUserOrganizationCompanyTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            DefaultUserOrganizationCompanyTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * The query should return the default organization for the specified user. This test sets up a 2
    * organization, and associates with a user. Hence it attempts to confirm that only the default
    * orgnization is returned by the Query.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDefaultUserOrganizationCompany() throws Exception {

      // SETUP: Data Setup for running the Function - NA
      HumanResourceKey lHuman = new HumanResourceKey( 8888, 88888 );

      // ACTION: Execute the Query
      execute( lHuman );

      // TEST: Confirm the Data had 1 result
      assertEquals( 1, iDataSet.getRowCount() );

      // TEST: Confirm the company key returned is expected
      OrgKey lOrg = new OrgKey( 4650, 10000 );
      testRow( lOrg );
      // TEARDOWN: Undo the Setup Changes - NA
   }


   /**
    * Execute the query.
    *
    * @param aHuman
    *           The Start Date of the Scheduling Window
    */
   private void execute( HumanResourceKey aHuman ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHuman, "aHrDbId", "aHrId" );

      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row of the dataset
    *
    * @param aComapny
    *           Task PK
    */
   private void testRow( OrgKey aComapny ) {
      iDataSet.first();
      MxAssert.assertEquals( aComapny.toString(), iDataSet.getString( "company_org" ) );
   }
}
