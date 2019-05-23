
package com.mxi.mx.core.query.lrp.block;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;


/**
 * Tests the query GetBlocksForPlan.qrx.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetBlocksForPlanTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetBlocksForPlanTest.class );

   }


   /**
    * Test Case: test to retrieve all of blocks whose definitions are ACTV or SUPRSEDE in a plan.
    */
   @Test
   public void testBlocksWithSupersededDefinition() {
      // ARRANGE
      int lPlanDbId = 4650;
      int lPlanId = 1;

      // ACT
      QuerySet lQs = getBlocksForPlan( lPlanDbId, lPlanId );

      // ASSERT
      assertEquals( 2, lQs.getRowCount() );

   }


   /**
    * Test Case: retrieve all of blocks whose definitions are ACTV or SUPRSEDE in a plan.
    */
   private QuerySet getBlocksForPlan( int aPlanDbId, int aPlanId ) {
      // Build query arguments.
      DataSetArgument lArg = new DataSetArgument();
      lArg.add( "aPlanDbId", aPlanDbId );
      lArg.add( "aPlanId", aPlanId );

      // Execute the query.
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArg );
   }

}
