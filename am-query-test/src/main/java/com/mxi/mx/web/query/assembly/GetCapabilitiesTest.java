
package com.mxi.mx.web.query.assembly;

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
 * This class runs unit tests on the GetCapabilities query file.
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCapabilitiesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetCapabilitiesTest.class );
   }


   /**
    * Test Case: Get capabilities and capability levels by assembly
    */
   @Test
   public void testgGetCapabilities() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 5000000 );
      lArgs.add( "aAssmblCd", "A320" );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Number of retrieved rows", 6, lResult.getRowCount() );
   }


   /**
    * Test Case: Get empty capabilities and capability levels by assembly
    */
   @Test
   public void testgGetCapabilitiesButEmptyResult() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 4650 );
      lArgs.add( "aAssmblCd", "GENERIC" );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Number of retrieved rows", 0, lResult.getRowCount() );
   }


   /**
    * Test Case: Check the order of the capabilities to ensure they are following the CAP_ORDER and
    * the LEVEL_ORDER
    */
   @Test
   public void testgCapabilitiesOrder() {

      DataSetArgument lArgs = new DataSetArgument();
      String[] lCapLevelCdsInOrder = { "NO_ETOPS", "ETOPS_90", "ETOPS120", "CATI", "CATII", "YES" };
      int lIndex = 0;

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 5000000 );
      lArgs.add( "aAssmblCd", "A320" );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Looping through the DataSet to ensure the order is correct
      while ( lResult.next() ) {
         assertEquals( "Row " + ( lIndex + 1 ) + " is not in the expected order",
               lResult.getStringAt( lIndex + 1, "ACFT_CAP_LEVEL_CD" ),
               lCapLevelCdsInOrder[lIndex] );
         lIndex++;
      }
   }
}
