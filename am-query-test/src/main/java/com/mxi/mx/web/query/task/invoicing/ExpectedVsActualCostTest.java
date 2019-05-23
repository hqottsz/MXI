
package com.mxi.mx.web.query.task.invoicing;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * DOCUMENT ME!
 *
 * @author rahulb
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ExpectedVsActualCostTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ExpectedVsActualCostTest.class );
   }


   /** DOCUMENT ME! */
   public static final int COST_LINE_ITEM_TYPE_DB_ID = 72485;

   /** DOCUMENT ME! */
   public static final String COST_LINE_ITEM_TYPE_CD = "rahul1";

   /** DOCUMENT ME! */
   public static final boolean PAYABLE_BOOL = true;

   /** DOCUMENT ME! */
   public static final String DESC_SDESC = "test rahul1";

   /** DOCUMENT ME! */
   public static final int SCHED_DB_ID = 4650;

   /** DOCUMENT ME! */
   public static final int SCHED_ID = 113987;

   /** DOCUMENT ME! */
   public static final int COST_LINE_ITEM_ID = 1;

   /** DOCUMENT ME! */
   public static final float INIT_EXP_COST = 100;

   /** DOCUMENT ME! */
   public static final float EXP_COST = 100;

   /** DOCUMENT ME! */
   public static final float ACTUAL_COST = 150;

   /** DOCUMENT ME! */
   public static final String ITEM_NOTE = "note";

   /** DOCUMENT ME! */
   public static final boolean INIT_EXP_COST_LOCKED_BOOL = true;

   /** DOCUMENT ME! */
   public static final boolean EXP_COST_LOCKED_BOOL = true;

   /** DOCUMENT ME! */
   public static final boolean ACTUAL_COST_LOCKED_BOOL = true;


   /**
    * Tests that the query returns the proper data when there are two users, each assigned to two
    * roles, with one role overlapping.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testAssignedRolesTwoUsers() throws Exception {

      //
      // Test User #1.
      //

      // Execute the query to retrieve data for user #1
      DataSet lDs = execute( SCHED_DB_ID, SCHED_ID );

      // Ensure two rows were returned
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // Ensure the first row's data is correct
      MxAssert.assertEquals( "event_key", SCHED_DB_ID + ":" + SCHED_ID,
            lDs.getStringAt( 1, "event_key" ) );
      MxAssert.assertEquals( "cost_line_item_key",
            SCHED_DB_ID + ":" + SCHED_ID + ":" + COST_LINE_ITEM_ID,
            lDs.getStringAt( 1, "cost_line_item_key" ) );
      MxAssert.assertEquals( "init_exp_cost", INIT_EXP_COST, lDs.getFloatAt( 1, "init_exp_cost" ) );
      MxAssert.assertEquals( "exp_cost", EXP_COST, lDs.getFloatAt( 1, "exp_cost" ) );
      MxAssert.assertEquals( "actual_cost", ACTUAL_COST, lDs.getFloatAt( 1, "actual_cost" ) );
      MxAssert.assertEquals( "item_note", ITEM_NOTE, lDs.getStringAt( 1, "item_note" ) );
      MxAssert.assertEquals( "payable_bool", PAYABLE_BOOL, lDs.getBooleanAt( 1, "payable_bool" ) );
      MxAssert.assertEquals( "desc_sdesc", DESC_SDESC, lDs.getStringAt( 1, "desc_sdesc" ) );
      MxAssert.assertEquals( "init_exp_cost_locked_bool", INIT_EXP_COST_LOCKED_BOOL,
            lDs.getBooleanAt( 1, "init_exp_cost_locked_bool" ) );
      MxAssert.assertEquals( "exp_cost_locked_bool", EXP_COST_LOCKED_BOOL,
            lDs.getBooleanAt( 1, "exp_cost_locked_bool" ) );
      MxAssert.assertEquals( "actual_cost_locked_bool", ACTUAL_COST_LOCKED_BOOL,
            lDs.getBooleanAt( 1, "actual_cost_locked_bool" ) );
   }


   /**
    * Execute the query.
    *
    * @param aSchedDbId
    *           sched_db_id.
    * @param aSchedId
    *           sched_id!
    *
    * @return dataSet result.
    */
   private DataSet execute( int aSchedDbId, int aSchedId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aSchedDbId", aSchedDbId );
      lArgs.add( "aSchedId", aSchedId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
