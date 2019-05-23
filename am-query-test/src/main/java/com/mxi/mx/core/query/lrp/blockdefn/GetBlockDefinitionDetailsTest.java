
package com.mxi.mx.core.query.lrp.blockdefn;

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
 * Tests the query GetBlockDefinitionDetails.qrx.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetBlockDefinitionDetailsTest {

   private static final String DEFINITION_STATUS_SUPRSEDE = "SUPRSEDE";
   private static final String DEFINITION_STATUS_ACTV = "ACTV";

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetBlockDefinitionDetailsTest.class );

   }


   /**
    * Test Case 1: test to retrieve the block whose definition is SUPRSEDE in a plan.
    */
   @Test
   public void testBlocksWithSupersededDefinition() {
      // ARRANGE
      int lTaskDefnDbId = 4650;
      int lTaskDefnId = 1;

      // ACT
      QuerySet lQs = getBlocksForPlan( lTaskDefnDbId, lTaskDefnId );

      // ASSERT
      assertEquals( 1, lQs.getRowCount() );

      lQs.first();

      assertEquals( DEFINITION_STATUS_SUPRSEDE, lQs.getString( "status" ) );

   }


   /**
    * Test Case 1: test to retrieve the block whose definition is ACTV in a plan.
    */
   @Test
   public void testBlocksWithActiveDefinition() {
      // ARRANGE
      int lTaskDefnDbId = 4650;
      int lTaskDefnId = 2;

      // ACT
      QuerySet lQs = getBlocksForPlan( lTaskDefnDbId, lTaskDefnId );

      // ASSERT
      assertEquals( 1, lQs.getRowCount() );

      lQs.first();

      assertEquals( DEFINITION_STATUS_ACTV, lQs.getString( "status" ) );

   }


   /**
    * Test Case: retrieve all of blocks whose definitions are ACTV and SUPRSEDE in a plan.
    */
   private QuerySet getBlocksForPlan( int aTaskDefnDbId, int aTaskDefnId ) {
      // Build query arguments.
      DataSetArgument lArg = new DataSetArgument();
      lArg.add( "aTaskDefnDbId", aTaskDefnDbId );
      lArg.add( "aTaskDefnId", aTaskDefnId );

      // Execute the query.
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArg );
   }

}
