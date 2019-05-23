
package com.mxi.mx.web.query.todolist;

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


/**
 * Tests the query com.mxi.mx.web.query.todolist.DeferralReferencesTab.qrx
 *
 * @author sdeshmukh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class DeferralReferencesTabTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), DeferralReferencesTabTest.class );
   }


   /**
    * Tests that the query returns the proper data.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testDeferralReferencesTab() throws Exception {
      // Execute the query to retrieve data
      DataSet lDs = execute( 4330, "36-150", 6000052 );

      // Ensure one row was returned. There are two that match the provided criteria, however, one
      // deferral reference is a rich content deferral reference and it should be filtered out by
      // the query
      assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // Ensure the first row's data is correct
      assertEquals( "36-150", lDs.getStringAt( 1, "assembly" ) );
      assertEquals( "Test Def Ref Query", lDs.getStringAt( 1, "defreference" ) );
      assertEquals( "MEL", lDs.getStringAt( 1, "faultseverity" ) );
      assertEquals( "MEL A", lDs.getStringAt( 1, "deferralclass" ) );
   }


   /**
    * Tests that the query returns the proper data .
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testDeferralReferencesTabWithNoAuthority() throws Exception {
      // Execute the query to retrieve data
      DataSet lDs = execute( 4330, "36-150", 6000053 );

      // Ensure one rows were returned
      assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aAssembDbId
    *           Assembly Db Id
    * @param aAssembCd
    *           Assembly Cd
    * @param aUserId
    *           User Id
    *
    * @return dataSet result.
    */
   private DataSet execute( Integer aAssembDbId, String aAssembCd, int aUserId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAssmblDbId", aAssembDbId );
      lArgs.add( "aAssmblCd", aAssembCd );
      lArgs.add( "aUserId", aUserId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
