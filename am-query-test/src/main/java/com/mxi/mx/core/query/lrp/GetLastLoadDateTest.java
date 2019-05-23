
package com.mxi.mx.core.query.lrp;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
 * This class performs unit testing on the query file with the same name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetLastLoadDateTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetLastLoadDateTest.class );
   }


   /** The query execution data set. */
   private DataSet iDataSet = null;


   /**
    * Tests that query returns data set with unique rows.
    */
   @Test
   public void testLastLoadDate() {

      // Execute the query.
      execute();
      assertEquals( 1, iDataSet.getTotalRowCount() );
      while ( iDataSet.next() ) {
         assertRow();
      }
   }


   /**
    * Test the content of the record
    */
   private void assertRow() {
      Calendar lGCalendar = GregorianCalendar.getInstance();
      lGCalendar.set( 2009, 11, 31, 23, 59, 59 );
      MxAssert.assertEquals( lGCalendar.getTime(), iDataSet.getDate( "load_actual_dt" ) );
   }


   /**
    * Execute the query.
    */
   private void execute() {

      // Build query arguments.
      DataSetArgument lArgs = new DataSetArgument();

      // Execute the query.
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
