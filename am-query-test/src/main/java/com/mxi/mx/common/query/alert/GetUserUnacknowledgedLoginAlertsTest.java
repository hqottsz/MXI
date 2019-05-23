
package com.mxi.mx.common.query.alert;

import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author Libin Cai
 * @created October 10, 2007
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetUserUnacknowledgedLoginAlertsTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetUserUnacknowledgedLoginAlertsTest.class );
   }


   public static final String USERNAME = "lcai";
   public static final int ALERT_ID = 104604;
   public static final int ALERT_TYPE_ID = 90;
   public static final int PRIORITY = 0;
   public static final String ALERT_TIMESTAMP = "2007-10-05 16:51:39";
   public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat( "YYYY-MM-dd HH:mm:ss" );
   public static final String MESSAGE = "{0}";


   /**
    * Tests that the query returns the proper data for table with two Prod Plans
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {

      // Execute the query to retrieve data for the user name
      QuerySet lQs = execute( USERNAME );

      // Ensure only one unacknowledged login alert is returned
      Assert.assertEquals( "Number of retrieved rows", 1, lQs.getRowCount() );

      Assert.assertTrue( lQs.next() );
      Assert.assertEquals( "alert_id", ALERT_ID, lQs.getInt( "alert_id" ) );
      Assert.assertEquals( "alert_type_id", ALERT_TYPE_ID, lQs.getInt( "alert_type_id" ) );
      Assert.assertEquals( "priority", PRIORITY, lQs.getInt( "priority" ) );
      Assert.assertEquals( "alert_timestamp", ALERT_TIMESTAMP,
            TIME_FORMAT.format( lQs.getDate( "alert_timestamp" ) ) );
      Assert.assertEquals( "message", MESSAGE, lQs.getString( "message" ) );
   }


   /**
    * Execute the query.
    *
    * @param aUsername
    *           Query argument
    *
    * @return dataSet result.
    */
   private QuerySet execute( String aUsername ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUsername", aUsername );

      // Execute the query
      return QueryExecutor.executeQuery( getClass(), lArgs );
   }
}
