
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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.todolist.FaultThresholdsTab.qrx
 *
 * @author sdeshmukh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FaultThresholdsTabTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FaultThresholdsTabTest.class );
   }


   /**
    * Tests that the query returns the proper data .
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testFaulthresholdTab() throws Exception {
      try {

         // Execute the query to retrieve data
         DataSet lDs = execute( 5000000, "A320", 6000052 );

         // Ensure one rows were returned
         MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

         // Ensure the first row's data is correct
         MxAssert.assertEquals( "A320", lDs.getStringAt( 1, "assembly" ) );

         MxAssert.assertEquals( "05-50-00", lDs.getStringAt( 1, "thsystem" ) );

         MxAssert.assertEquals( "Test Fault Query", lDs.getStringAt( 1, "thname" ) );

         MxAssert.assertEquals( 12, lDs.getIntAt( 1, "thqt" ) );

         MxAssert.assertEquals( "12-32", lDs.getStringAt( 1, "applicablityrage" ) );

         MxAssert.assertEquals( "Mon May 05 01:00:00 EDT 2008",
               lDs.getDateAt( 1, "effective" ).toString() );
      } catch ( Exception e ) {

         e.printStackTrace();
      }
   }


   /**
    * Tests that the query returns the proper data .
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testFaulthresholdTabWithNoAuthority() throws Exception {
      try {

         // Execute the query to retrieve data
         DataSet lDs = execute( 5000000, "A320", 6000053 );

         // Ensure one rows were returned
         MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
      } catch ( Exception e ) {

         e.printStackTrace();
      }
   }


   /**
    * Execute the query.
    *
    * @param aAssembDbId
    *           Assembly Db Id
    * @param aAssembCd
    *           Assembly Cd
    * @param aUserId
    *           User Id
    *
    * @return dataSet result.
    */
   private DataSet execute( Integer aAssembDbId, String aAssembCd, int aUserId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAssmblDbId", aAssembDbId );
      lArgs.add( "aAssmblCd", aAssembCd );
      lArgs.add( "aUserId", aUserId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
