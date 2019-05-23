
package com.mxi.am.db.qrx;

import com.mxi.mx.common.dataset.QuerySet;


/**
 * Describes a Query Set
 */
public final class QuerySetDescriber {

   private QuerySetDescriber() {
      // utility class
   }


   public static String describe( QuerySet aQs ) {
      aQs.beforeFirst();
      StringBuilder lDescription = new StringBuilder();
      boolean lIsFirstRow = true;
      while ( aQs.next() ) {
         if ( !lIsFirstRow ) {
            lDescription.append( ",\n" );
         }

         boolean lIsFirstColumn = true;
         for ( String lColumnName : aQs.getColumnNames() ) {
            if ( !lIsFirstColumn ) {
               lDescription.append( ", " );
            }

            lDescription.append( lColumnName ).append( ": " );
            Object lValue = aQs.getObject( lColumnName );
            if ( lValue instanceof String ) {
               lDescription.append( "\"" + lValue + "\"" );
            } else {
               lDescription.append( lValue );
            }

            lIsFirstColumn = false;
         }

         lIsFirstRow = false;
      }
      return lDescription.toString();
   }
}
