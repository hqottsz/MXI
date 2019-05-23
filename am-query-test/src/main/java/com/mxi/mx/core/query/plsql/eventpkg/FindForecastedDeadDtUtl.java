package com.mxi.mx.core.query.plsql.eventpkg;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.SqlLoader;


/**
 * This class Runs the SQL queries and calls the database for event_pkg.FindForecastedDeadDt
 * 
 * @author gehyca
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class FindForecastedDeadDtUtl {

   @ClassRule
   public static final DatabaseConnectionRule iDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   // This runs the data preparation SQL for the tests
   public static void loadData() {
      SqlLoader.load( iDatabaseConnectionRule.getConnection(), FindForecastedDeadDtTest.class,
            "FindForecastedDeadDtTest.sql" );
   }


   /**
    * This method will calculate the difference in days for current and future dates.
    *
    * @param aCurrentDate
    *           - today's date
    * @param aForecastDate
    *           - generated forecasted date
    * @return Number of days
    */
   protected long DateDiffInDays( Date aCurrentDate, Date aForecastDate ) {
      long diffDays = 0;

      ResultSet ResultSetRecords;

      String Strquery = "SELECT TO_DATE('" + aForecastDate + "', 'YYYY-MM-DD') - TO_DATE('"
            + aCurrentDate + "', 'YYYY-MM-DD') AS DateDiff FROM dual";

      try {
         ResultSetRecords = runQuery( Strquery.toString() );
         ResultSetRecords.next();
         diffDays = ResultSetRecords.getLong( "DateDiff" );
      } catch ( SQLException e ) {
         e.printStackTrace();

      }

      return diffDays;
   }


   /**
    * Runs the provided query on the database
    *
    * @param aQuery
    *           Query to be run
    *
    * @return Returns the string query result
    *
    * @throws SQLException
    */
   protected ResultSet runQuery( String aQuery ) throws SQLException {

      System.out.println( "Executing Query: " + aQuery );

      PreparedStatement lStatement = iDatabaseConnectionRule.getConnection().prepareStatement(
            aQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

      ResultSet lResultSet = lStatement.executeQuery( aQuery );

      return lResultSet;
   }


   protected String execute( String aQuery, String aResultfield ) {

      PreparedStatement lStatement;
      String value = null;

      try {
         lStatement = iDatabaseConnectionRule.getConnection().prepareStatement( aQuery,
               ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );

         ResultSet lResultSet = lStatement.executeQuery( aQuery );
         while ( lResultSet.next() ) {
            value = lResultSet.getString( aResultfield );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }

      return value;

   }


   /**
    * call event_pkg.findforecasteddeaddt
    *
    * @parameters: aAircraftDbId, aAircraftId, datatypeDBid, aDataTypeId, remaining Usage and start
    *              Date
    * @return: Forecast date
    *
    */
   protected Date runValidateFindForecast( int aAircraftDbId, int aAircraftId, int aDataTypeDbId,
         int aDataTypeId, long aRemainingUsageQt, Date aStartDt ) {

      Date lForcastedDate = null;
      CallableStatement lPrepareCallFindForecast;
      try {
         lPrepareCallFindForecast = iDatabaseConnectionRule.getConnection()
               .prepareCall( "BEGIN   event_pkg.findforecasteddeaddt(aAircraftDbId => ?,"
                     + "aAircraftId =>?, aDataTypeDbId => ?,"
                     + "aDataTypeId =>?,aRemainingUsageQt =>?," + "aStartDt => ?,aforecastdt =>?,"
                     + "ol_return => ?); END;" );

         lPrepareCallFindForecast.setInt( 1, aAircraftDbId );
         lPrepareCallFindForecast.setInt( 2, aAircraftId );
         lPrepareCallFindForecast.setInt( 3, aDataTypeDbId );
         lPrepareCallFindForecast.setInt( 4, aDataTypeId );
         lPrepareCallFindForecast.setLong( 5, aRemainingUsageQt );
         lPrepareCallFindForecast.setDate( 6, aStartDt );
         lPrepareCallFindForecast.registerOutParameter( 7, Types.DATE );
         lPrepareCallFindForecast.registerOutParameter( 8, Types.INTEGER );

         lPrepareCallFindForecast.execute();
         lForcastedDate = lPrepareCallFindForecast.getDate( 7 );
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return lForcastedDate;
   }
}
