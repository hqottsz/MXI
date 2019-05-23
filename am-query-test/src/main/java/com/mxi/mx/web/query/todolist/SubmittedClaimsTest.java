
package com.mxi.mx.web.query.todolist;

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
 * Data class for testing query SubmittedClaims.qrx
 *
 * @author rahulb
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SubmittedClaimsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), SubmittedClaimsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Results of SubmittedClaims query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testSubmittedClaims() throws Exception {
      execute();
      iDataSet.next();
      MxAssert.assertEquals( "4650:1", iDataSet.getString( "claim_pk" ) );
      MxAssert.assertEquals( "Test Task", iDataSet.getString( "event_sdesc" ) );
      MxAssert.assertEquals( "4650:1", iDataSet.getString( "sched_stask_pk" ) );
      MxAssert.assertEquals( "TEST WARRANTY SDESC", iDataSet.getString( "warranty_sdesc" ) );
      MxAssert.assertEquals( "4650:3", iDataSet.getString( "warranty_defn_pk" ) );
      MxAssert.assertEquals( "ACE(ACE ELECTRONICS )", iDataSet.getString( "vendor" ) );
      MxAssert.assertEquals( "4650:6", iDataSet.getString( "vendor_pk" ) );
      MxAssert.assertEquals( "TEST INV DESC", iDataSet.getString( "inv_no_sdesc" ) );
      MxAssert.assertEquals( "4650:2", iDataSet.getString( "inv_inv_pk" ) );
      MxAssert.assertEquals( "100", iDataSet.getString( "total_price" ) );
      MxAssert.assertEquals( "USD", iDataSet.getString( "default_currency" ) );
   }


   /**
    * Execute the query.
    */
   private void execute() {

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ) );
   }
}
