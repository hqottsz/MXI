
package com.mxi.mx.web.query.claim;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

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
import com.mxi.mx.core.key.ClaimKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the ClaimLaborLines query file with the same package and
 * name.
 *
 * @author akash
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ClaimLaborLinesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ClaimLaborLinesTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Results of ClaimLaborLines query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testClaimLaborLines() throws Exception {
      execute( new ClaimKey( "4650:1" ) );

      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();

      MxAssert.assertEquals( "4650:1:1", iDataSet.getString( "claim_labour_line_key" ) );
      MxAssert.assertEquals( "Test Inv (Serial No)", iDataSet.getString( "line_ldesc" ) );
      MxAssert.assertEquals( 10.0, iDataSet.getDouble( "claim_qt" ) );
      MxAssert.assertEquals( new BigDecimal( "5.0" ), iDataSet.getBigDecimal( "unit_price" ) );
      MxAssert.assertEquals( new BigDecimal( "50.0" ), iDataSet.getBigDecimal( "line_price" ) );
      MxAssert.assertEquals( "Test Task", iDataSet.getString( "event_sdesc" ) );
      MxAssert.assertEquals( "Test Note", iDataSet.getString( "note" ) );
   }


   /**
    * Execute the query.
    *
    * @param aClaim
    *           Claim Key
    */
   private void execute( ClaimKey aClaim ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aClaim, new String[] { "aClaimDbId", "aClaimId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
