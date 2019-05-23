
package com.mxi.mx.web.query.po;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetReceiptOrganizationsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetReceiptOrganizationsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * The query should return a row when the owner organization for the inventory is the same as
    * that for the Order.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetReceiptOrganization() throws Exception {

      // ACTION: Execute the Query
      execute();

      // TEST: Confirm the Data had 3 results
      assertEquals( 3, iDataSet.getRowCount() );

      // TEST: Confirm the ADMIN, OPER, MRO are returned
      validateTypes();
   }


   /**
    * Execute the query.
    */
   private void execute() {
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ) );
   }


   /**
    * Tests a row of the dataset
    */
   private void validateTypes() {
      String[] lOrg = new String[] { "MXI", "OPER_ORG", "MRO_ORG" };

      boolean lAssertionFailed = true;

      while ( iDataSet.next() ) {
         for ( String lString : lOrg ) {

            // iterate over each available type
            if ( lString.equals( iDataSet.getString( "org_cd" ) ) ) {
               lAssertionFailed = false;
            }
         }

         // should never come here
         if ( lAssertionFailed ) {
            MxAssert.fail( "The type " + iDataSet.getString( "org_cd" ) + "is invalid." );
         }
      }
   }
}
