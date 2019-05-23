
package com.mxi.am.db.connection.format;

import com.mxi.mx.core.unittest.MxUnittestDao;


/**
 * This class escapes the column value for SQL.
 */
public final class ColumnValueEscaper {

   public enum ColumnType {
      DATE() {

         @Override
         public String escape( Object aValue ) {
            if ( aValue == null ) {
               return "NULL";
            }
            return "to_date('" + aValue + "', 'yyyy-mm-dd hh24:mi:ss')";
         }
      },
      STRING() {

         @Override
         public String escape( Object aValue ) {
            if ( aValue == null ) {
               return "NULL";
            }
            return "'" + aValue + "'";
         }
      };

      public abstract String escape( Object aValue );
   }


   public static ColumnType getInferredColumnType( String aTableName, String aColumnName ) {
      if ( MxUnittestDao.isDateColumn( aTableName, aColumnName ) ) {
         return ColumnType.DATE;
      }
      return ColumnType.STRING;
   }
}
