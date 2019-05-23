package com.mxi.mx.web.query.user;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * Test UserHistory.qrx
 */

public final class UserHistoryTest {

   public static final int EMPLOYEE_USER_ID = 1111111;
   public static final int REPORTED_BY_USER_ID = 99999;
   public static final String REPORTED_BY_USER_FIRSTNAME = "Jason";
   public static final String REPORTED_BY_USER_LASTNAME = "Smith";
   private static final String EVENT_KEY = "4650:1";
   private static final String EVENT_SDESC = "some notes";
   private static final String USER_REASON_CD = "REASONCD";
   private static final String USER_STATUS_CD = "SUSPEND";
   private static final Date EVENT_DATE =
         new GregorianCalendar( 2017, GregorianCalendar.DECEMBER, 14, 12, 0, 0 ).getTime();

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UserHistoryTest.class );
   }


   /**
    * Tests that when one license of a user was suspended, the UserHistory query can retrieve info
    * of that event.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void executeQuery_userWithSuspendedLicense() throws Exception {

      DataSet lDs = executeQuery( EMPLOYEE_USER_ID );

      assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
      lDs.next();
      assertEquals( "The date of the license was suspended", EVENT_DATE,
            lDs.getDate( "event_dt" ) );
      assertEquals( "Reported by user name",
            REPORTED_BY_USER_LASTNAME + ", " + REPORTED_BY_USER_FIRSTNAME,
            lDs.getString( "username" ) );
      assertEquals( "user_status_cd", USER_STATUS_CD, lDs.getString( "user_status_cd" ) );
      assertEquals( "user_reason_cd", USER_REASON_CD, lDs.getString( "user_reason_cd" ) );
      assertEquals( "event_sdesc", EVENT_SDESC, lDs.getString( "event_sdesc" ) );
      assertEquals( "event_key", EVENT_KEY, lDs.getString( "event_key" ) );
      assertEquals( "Reported by user ID", REPORTED_BY_USER_ID, lDs.getInt( "user_id" ) );
   }


   private DataSet executeQuery( int aUserId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUserId", aUserId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
