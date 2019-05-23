
package com.mxi.mx.core.adapter.flight.query.flight;

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
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * This tests the GetAircraftStatus test
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftCapabilitiesUpdatedTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetAircraftCapabilitiesUpdatedTest.class );
   }


   /**
    * Used to execute the query
    *
    * @returns DataSet with query results
    */
   private DataSet executeQuery( DataSetArgument aArgs ) {

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aArgs );
   }


   /**
    * Test Case: Get capabilities and capability levels by valid aircraft key
    */
   @Test
   public void testGetCapabilities() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAcftNoDbId", 4650 );
      lArgs.add( "aAcftNoId", 300785 );

      // Execute query!
      DataSet lResult = executeQuery( lArgs );

      assertEquals( "Number of retrieved rows", 3, lResult.getRowCount() );
   }


   /**
    * Test Case: Get empty capabilities and capability levels by invalid aircraft key
    */
   @Test
   public void testGetCapabilitiesButEmptyResult() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAcftNoDbId", 4650 );
      lArgs.add( "aAcftNoId", 5 );

      // Execute query!
      DataSet lResult = executeQuery( lArgs );

      assertEquals( "Number of retrieved rows", 0, lResult.getRowCount() );
   }

}
