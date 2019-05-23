
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
 * This class runs a unit test on the GetAircraftByGroup query file.
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftByGroupTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftByGroupTest.class );
   }


   /**
    * Test Case : retrieves the correct number of aircraft in a group.
    */
   @Test
   public void testGetAircraftInGroup() {

      // SETUP
      String lGroupId = "2001";
      String lUserId = "1";

      // ACT
      DataSet lResult = getAircraftByGroup( lGroupId, lUserId );

      // ASSERT
      assertNotNull( lResult );

      assertEquals( "Number of retrieved rows for Group Id: " + lGroupId, 4,
            lResult.getTotalRowCount() );
   }


   /**
    * Test Case : retrieves no aircraft in an empty group.
    */
   @Test
   public void testGetAircraftInEmptyGroup() {

      // SETUP
      String lGroupId = "2002";
      String lUserId = "1";

      // ACT
      DataSet lResult = getAircraftByGroup( lGroupId, lUserId );

      // ASSERT
      assertNotNull( lResult );

      assertEquals( "Number of retrieved rows for Group Id: " + lGroupId, 0,
            lResult.getTotalRowCount() );
   }


   /**
    * Retrieve the aircraft in the group.
    *
    */
   private DataSet getAircraftByGroup( String aGroupId, String aUserId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAcftGroupId", aGroupId );
      lArgs.add( "aUserId", aUserId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }
}
