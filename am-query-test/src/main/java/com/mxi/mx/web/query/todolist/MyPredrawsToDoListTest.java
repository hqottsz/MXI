
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
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class MyPredrawsToDoListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), MyPredrawsToDoListTest.class );
   }


   /**
    * Test the case to get all predraws.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testGetAllPredrawsToDoList() throws Exception {

      // Prepare the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( new HumanResourceKey( 4650, 90 ), new String[] { "aHrDbId", "aHrId" } );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.todolist.MyPreDrawsToDoList", lArgs );

      // There should be 2 rows
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      lDs.first();

      MxAssert.assertEquals( "part_request_key", "4650:50000",
            lDs.getString( "part_request_key" ) );
      MxAssert.assertEquals( "inv_loc_cd", "subloc1", lDs.getString( "inv_loc_cd" ) );
      MxAssert.assertEquals( "part_no_oem", "PART_5", lDs.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "req_loc_cd", "req_loc", lDs.getString( "req_loc_cd" ) );
      MxAssert.assertEquals( "req_priority_cd", "CRITICAL", lDs.getString( "req_priority_cd" ) );
      MxAssert.assertEquals( "req_qt", "4 EA", lDs.getString( "req_qt" ) );
      MxAssert.assertEquals( "wo_ref_sdesc", "TEST_WOREDESC", lDs.getString( "wo_ref_sdesc" ) );
      MxAssert.assertEquals( "req_type_cd", "TASK", lDs.getString( "req_type_cd" ) );

      lDs.next();

      MxAssert.assertEquals( "part_request_key", "4650:60000",
            lDs.getString( "part_request_key" ) );
      MxAssert.assertEquals( "inv_loc_cd", "subloc2", lDs.getString( "inv_loc_cd" ) );
      MxAssert.assertEquals( "part_no_oem", "PART_1", lDs.getString( "part_no_oem" ) );
      MxAssert.assertEquals( "req_loc_cd", "req_loc", lDs.getString( "req_loc_cd" ) );
      MxAssert.assertEquals( "req_priority_cd", "NORMAL", lDs.getString( "req_priority_cd" ) );
      MxAssert.assertEquals( "req_qt", "1 EA", lDs.getString( "req_qt" ) );
      MxAssert.assertEquals( "wo_ref_sdesc", null, lDs.getString( "wo_ref_sdesc" ) );
      MxAssert.assertEquals( "req_type_cd", "ADHOC", lDs.getString( "req_type_cd" ) );
   }
}
