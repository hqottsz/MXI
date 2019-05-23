
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
 * This class runs a unit test on the GetAircraftGroups query file.
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftGroupsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftGroupsTest.class );
   }


   /**
    * Test Case : retrieves aircraft groups correctly.
    */
   @Test
   public void testGetAircraftGroups() {

      // ACT
      DataSet lResult = GetAircraftGroups();

      // ASSERT
      assertNotNull( lResult );

      assertEquals( "Number of retrieved rows", 4, lResult.getTotalRowCount() );
   }


   /**
    * Retrieve the aircraft groups.
    *
    */
   private DataSet GetAircraftGroups() {

      DataSetArgument lArgs = new DataSetArgument();

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }
}
