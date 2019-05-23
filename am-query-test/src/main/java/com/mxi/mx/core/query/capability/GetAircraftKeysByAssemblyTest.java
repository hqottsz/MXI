
package com.mxi.mx.core.query.capability;

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
 * This class tests the query com.mxi.mx.core.query.capability.GetAircraftKeysByAssembly.qrx
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftKeysByAssemblyTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetAircraftKeysByAssemblyTest.class );
   }


   /**
    * Tests the retrieval of the aircraft keys of a giving assembly.
    *
    * Preconditions:
    *
    * Data is set up such that there are 7 aircraft under an assembly with 6 of them not locked.
    *
    * Action:
    *
    * Run the GetAircraftKeysByAssembly query
    *
    * Expectation:
    *
    * For the data set to contain 6 rows
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetAircraftKeysByAssembly() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 5000000 );
      lArgs.add( "aAssmblCd", "A320" );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      // ASSERT
      assertEquals( "Number of retrieved rows", 6, lResult.getRowCount() );

   }

}
