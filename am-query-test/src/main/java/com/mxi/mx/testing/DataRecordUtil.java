
package com.mxi.mx.testing;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.dataset.SQLStatement;


/**
 * This class is used to debug result sets from tables and queries.
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class DataRecordUtil {

   Map<String, Object> iFields = new HashMap<>();
   private static OutputStream iStream = System.out;
   private static Connection iConnection = null;


   public void setField( String aKey, Object aValue ) {
      if ( aValue == null ) {
         iFields.remove( aKey );
      } else {
         iFields.put( aKey, aValue );
      }

   }


   public DataRecordUtil withConnection( Connection aConnection ) {
      setConnection( aConnection );
      return this;
   }


   public static void setConnection( Connection aConnection ) {
      iConnection = aConnection;
   }


   public static void setOutput( OutputStream aOutputStream ) {
      iStream = aOutputStream;
   }


   public DataRecordUtil outputTo( OutputStream aOutputStream ) {
      setOutput( aOutputStream );
      return this;
   }


   public Map<String, Object> getFields( String aKey ) {
      return iFields;
   }


   public DataRecordUtil() {
   }


   public DataRecordUtil(Connection aConnection) {
      iConnection = aConnection;
   }


   public DataRecordUtil(QuerySet aDataSet) {
      for ( String lColumn : aDataSet.getColumnNames() ) {
         iFields.put( lColumn, aDataSet.getObject( lColumn ) );
      }
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return iFields.toString();
   }


   /**
    * Return the named field
    *
    * @param aFieldName
    *           name of the field
    * @return
    */
   public Object getField( String aFieldName ) {
      return iFields.get( aFieldName );
   }


   public static void debugDataSet( DataSet aDs ) {
      do {
         System.out.println( new DataRecordUtil( aDs ) );
      } while ( aDs.next() );
   }


   public static void debugTable( String aTableName ) {
      debugQuery( "select * from " + aTableName, aTableName, 9 );
   }


   public static void debugQuery( String aQuery, String aLabel ) {
      debugQuery( aQuery, aLabel, 1 );
   }


   public static void debugQuery( String aQuery, String aLabel, int aRowCount ) {
      SQLStatement lStatement = new SQLStatement( aQuery );
      try {
         lStatement.prepare( iConnection );
         DataSet lDs = lStatement.executeQuery();
         for ( int x = 0; x < aRowCount && lDs.next(); x++ ) {
            DataRecordUtil lrec = new DataRecordUtil( lDs );
            System.out.println( aLabel + ": " + lrec );
         }
      } catch ( SQLException e ) {
         e.printStackTrace();
      }
   }

}
