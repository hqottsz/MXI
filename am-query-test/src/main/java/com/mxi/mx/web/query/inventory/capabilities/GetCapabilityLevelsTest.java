
package com.mxi.mx.web.query.inventory.capabilities;

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
 * This class runs unit tests on the GetCapabilityLevels query file.
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCapabilityLevelsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetCapabilityLevelsTest.class );
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
    * Test Case: Get capability levels by assembly and capability
    */
   @Test
   public void testGetCapabilityLevels() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 4650 );
      lArgs.add( "aAssmblCd", "A320" );
      lArgs.add( "aCapDbId", 10 );
      lArgs.add( "aCapCd", "ALAND" );

      // Execute query!
      DataSet lResult = executeQuery( lArgs );

      assertEquals( "Number of retrieved rows", 2, lResult.getRowCount() );
   }


   /**
    * Test Case: Get empty capability levels by assembly and capability
    */
   @Test
   public void testGetCapabilityLevelsButEmptyResult() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 4650 );
      lArgs.add( "aAssmblCd", "A320" );
      lArgs.add( "aCapDbId", 10 );
      lArgs.add( "aCapCd", "TEST" );

      // Execute query!
      DataSet lResult = executeQuery( lArgs );

      assertEquals( "Number of retrieved rows", 0, lResult.getRowCount() );
   }


   /**
    * Test Case: Check the order of the capability levels.
    */
   @Test
   public void testCapabilityLevelsOrder() {

      DataSetArgument lArgs = new DataSetArgument();
      String[] lCapLevelCdsInOrder = { "CATI", "CATIII" };
      int lIndex = 0;

      // Parameters required by the query.
      lArgs.add( "aAssmblDbId", 4650 );
      lArgs.add( "aAssmblCd", "A320" );
      lArgs.add( "aCapDbId", 10 );
      lArgs.add( "aCapCd", "ALAND" );

      // Execute query!
      DataSet lResult = executeQuery( lArgs );

      // Looping through the DataSet to ensure the order is correct
      while ( lResult.next() ) {
         assertEquals( "The level " + lCapLevelCdsInOrder[lIndex] + " is not in the expected order",
               lResult.getString( "acft_cap_level_cd" ), lCapLevelCdsInOrder[lIndex] );
         lIndex++;
      }
   }
}
