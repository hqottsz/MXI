
package com.mxi.mx.integration.query.ordering;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
 * This test class tests the ordering.GetLastMessageArrival query.
 *
 * @author rabson
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetLastMessageArrivalTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetLastMessageArrivalTest.class );
   }


   /**
    * This test case ensures that the GetLastMessageArrival query returns the proper value when
    * there's duplicate message id's used accross multiple stream codes.
    */
   @Test
   public void testDuplicateMessageIDs() {

      // Run the query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aStreamCd", "STREAM1" );

      QuerySet lDataSet = QueryExecutor.executeQuery( getClass(), lArgs );

      // Assert that only 1 row was returned
      Assert.assertEquals( 1, lDataSet.getRowCount() );

      // Check that we got the correct date from STREAM1
      lDataSet.next();
      Calendar lExpectedDate = GregorianCalendar.getInstance();
      lExpectedDate.set( 2011, Calendar.JANUARY, 15, 0, 0, 0 );
      lExpectedDate.set( Calendar.MILLISECOND, 0 );
      Assert.assertEquals( lExpectedDate.getTime(), lDataSet.getDate( "mx_msg_date" ) );
   }
}
