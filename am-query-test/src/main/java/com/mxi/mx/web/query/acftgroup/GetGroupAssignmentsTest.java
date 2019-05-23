
package com.mxi.mx.web.query.acftgroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
 * This class runs a unit test on the GetGroupAssignments query file.
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetGroupAssignmentsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetGroupAssignmentsTest.class );
   }


   /**
    * Test Case : retrieves the correct number of aircraft taking authority into account.
    */
   @Test
   public void testGetGroupAssignmentsWithAuthorities() {

      // SETUP
      String lUserId = "1";

      // ACT
      DataSet lResult = getGroupAssignments( lUserId );

      // ASSERT
      assertNotNull( lResult );

      assertEquals( "Number of retrieved rows for User Id: " + lUserId, 3,
            lResult.getTotalRowCount() );
   }


   /**
    * Test Case : multiple aircrafts can be assigned to a group.
    */
   @Test
   public void testMultipleAircraftsAssignedToAGroup() {

      // SETUP
      String lUserId = "1";

      // ACT
      DataSet lResult = getGroupAssignments( lUserId );

      String lList = lResult.getStringAt( 2, 2 );

      // ASSERT
      assertNotNull( lList );

      assertEquals( lList, "plan-1, plan-2" );
   }


   /**
    * Retrieve the aircraft group assignments.
    *
    */
   private DataSet getGroupAssignments( String aUserId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUserId", aUserId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }
}
