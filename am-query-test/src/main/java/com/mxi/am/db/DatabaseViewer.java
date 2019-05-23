package com.mxi.am.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mxi.am.db.connection.DatabaseConnectionRule;


/**
 * A Utility class to help developer to debug a test. Since test is run in a transaction, it is
 * impossible to see the content of the table from external application such as PLSQL developer/SQL
 * Developer. This utility class allows developer to run query within the transaction of the test.
 *
 * <code>
 *    DatabaseViewer.runQuery("select * from utl_work_item");
 * </code>
 */
public final class DatabaseViewer {

   /**
    * Execute the provided query
    * 
    * @param query
    * @return
    * @throws SQLException
    * @throws JSONException
    */
   public static JSONArray runQuery( String query ) throws SQLException, JSONException {
      JSONArray json;
      try ( Statement stmt = new DatabaseConnectionRule().getConnection().createStatement() ) {
         ResultSet rs = stmt.executeQuery( query );
         json = new JSONArray();
         ResultSetMetaData rsmd = rs.getMetaData();
         while ( rs.next() ) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for ( int i = 1; i <= numColumns; i++ ) {
               String column_name = rsmd.getColumnName( i );
               obj.put( column_name, rs.getObject( column_name ) );
            }
            json.put( obj );
         }
      }
      return json;
   }


   /**
    *
    * Execute the provided query and output the result to console
    *
    * @param query
    * @throws SQLException
    */
   public static void runQueryToConsole( String query ) throws SQLException {
      try ( Statement stmt = new DatabaseConnectionRule().getConnection().createStatement() ) {
         ResultSet rs = stmt.executeQuery( query );
         ResultSetMetaData rsmd = rs.getMetaData();
         System.out.println();
         for ( int i = 1; i <= rsmd.getColumnCount(); i++ ) {
            String column_name = rsmd.getColumnName( i );
            System.out.print( column_name + " \t " );
         }
         System.out.println();
         while ( rs.next() ) {
            int numColumns = rsmd.getColumnCount();
            for ( int i = 1; i <= numColumns; i++ ) {
               String column_name = rsmd.getColumnName( i );
               System.out.print( rs.getObject( column_name ) + " \t " );
            }
            System.out.println();
         }
      }
      return;
   }

}
