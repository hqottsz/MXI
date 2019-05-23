
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
public final class GetPlansPublishedAfterLoadTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPlansPublishedAfterLoadTest.class );
   }


   /** The query execution data set. */
   private DataSet iDataSet = null;


   /**
    * Tests that query returns data set with unique rows.
    */
   @Test
   public void testPlansPublishedAfterLoad() {

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
      MxAssert.assertEquals( 4650, iDataSet.getInt( "lrp_db_id" ) );
      MxAssert.assertEquals( 1, iDataSet.getInt( "lrp_id" ) );
      MxAssert.assertEquals( 4650, iDataSet.getInt( "pub_hr_db_id" ) );
      MxAssert.assertEquals( 1, iDataSet.getInt( "pub_hr_id" ) );
      MxAssert.assertEquals( 4650, iDataSet.getInt( "created_hr_db_id" ) );
      MxAssert.assertEquals( 1, iDataSet.getInt( "created_hr_id" ) );
      MxAssert.assertEquals( 4650, iDataSet.getInt( "pub_hr_db_id" ) );
      MxAssert.assertEquals( 1, iDataSet.getInt( "pub_hr_id" ) );
      MxAssert.assertEquals( "Pln 1", iDataSet.getString( "desc_sdesc" ) );
      MxAssert.assertEquals( "First Plan for Test", iDataSet.getString( "desc_ldesc" ) );

      Calendar lGCalendar = GregorianCalendar.getInstance();
      lGCalendar.set( 2009, 11, 02, 10, 31 );
      MxAssert.assertEquals( lGCalendar.getTime(), iDataSet.getDate( "created_dt" ) );
      lGCalendar.set( 2009, 11, 03, 20, 31 );
      MxAssert.assertEquals( lGCalendar.getTime(), iDataSet.getDate( "updated_dt" ) );
      lGCalendar.set( 2009, 11, 29, 3, 31 );
      MxAssert.assertEquals( lGCalendar.getTime(), iDataSet.getDate( "published_dt" ) );
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
