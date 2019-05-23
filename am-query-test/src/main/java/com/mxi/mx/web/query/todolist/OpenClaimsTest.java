
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
import com.mxi.mx.core.key.ClaimKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Data class for testing query OpenClaims.qrx
 *
 * @author rahulb
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OpenClaimsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), OpenClaimsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Results of OpenClaims query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testOpenClaims() throws Exception {
      execute( new ClaimKey( "4650:1" ) );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();

      MxAssert.assertEquals( "4650:1", iDataSet.getString( "claim_pk" ) );
      MxAssert.assertEquals( "TEST EVENT DESC", iDataSet.getString( "event_sdesc" ) );
      MxAssert.assertEquals( "TEST WARRANTY SDESC", iDataSet.getString( "warranty_sdesc" ) );
      MxAssert.assertEquals( "TEST INV SDESC", iDataSet.getString( "inv_no_sdesc" ) );
      MxAssert.assertEquals( "4650:1", iDataSet.getString( "sched_stask_pk" ) );
      MxAssert.assertEquals( "22", iDataSet.getString( "total_price" ) );
      MxAssert.assertEquals( "USD", iDataSet.getString( "default_currency" ) );
      MxAssert.assertEquals( "4650:4", iDataSet.getString( "warranty_defn_pk" ) );
      MxAssert.assertEquals( "ACE(ACE ELECTRONICS )", iDataSet.getString( "vendor" ) );
      MxAssert.assertEquals( "4650:6", iDataSet.getString( "vendor_pk" ) );
      MxAssert.assertEquals( "4650:5", iDataSet.getString( "inv_inv_pk" ) );
   }


   /**
    * Execute the query.
    *
    * @param aClaim
    *           Claim Key
    */
   private void execute( ClaimKey aClaim ) {

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ) );
   }
}
