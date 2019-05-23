
package com.mxi.mx.web.query.fnc;

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
 * This class tests the query com.mxi.mx.web.query.fnc.AircraftAssignedToAccount.qrx
 *
 * @author sdevi
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AircraftAssignedToAccountTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            AircraftAssignedToAccountTest.class );
   }


   private static final String TOTAL_COUNT = "total_count";
   private static final String AIRCRAFT_NAME = "inv_no_sdesc";
   private static final String AIRCRAFT_KEY = "aircraft_key";


   /**
    * Tests the retrieval of aircraft associated to account.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAircraftAssignedToAccount() throws Exception {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aAccountDbId", 4650 );
      lArgs.add( "aAccountId", 1 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows", 3, lDs.getRowCount() );

      // now assert each row data
      lDs.next();
      MxAssert.assertEquals( TOTAL_COUNT, 3, lDs.getInt( TOTAL_COUNT ) );
      MxAssert.assertEquals( AIRCRAFT_KEY, "4650:2010", lDs.getString( AIRCRAFT_KEY ) );
      MxAssert.assertEquals( AIRCRAFT_NAME, "ACFT-100", lDs.getString( AIRCRAFT_NAME ) );

      lDs.next();
      MxAssert.assertEquals( TOTAL_COUNT, 3, lDs.getInt( TOTAL_COUNT ) );
      MxAssert.assertEquals( AIRCRAFT_KEY, "4650:2011", lDs.getString( AIRCRAFT_KEY ) );
      MxAssert.assertEquals( AIRCRAFT_NAME, "ACFT-101", lDs.getString( AIRCRAFT_NAME ) );

      lDs.next();
      MxAssert.assertEquals( TOTAL_COUNT, 3, lDs.getInt( TOTAL_COUNT ) );
      MxAssert.assertEquals( AIRCRAFT_KEY, "4650:2012", lDs.getString( AIRCRAFT_KEY ) );
      MxAssert.assertEquals( AIRCRAFT_NAME, "ACFT-102", lDs.getString( AIRCRAFT_NAME ) );
   }


   /**
    * Tests the retrieval of no aircraft associated to account.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoAircraftAssignedToAccount() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aAccountDbId", 4650 );
      lArgs.add( "aAccountId", 2 );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that No of Rows should be zero
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }
}
