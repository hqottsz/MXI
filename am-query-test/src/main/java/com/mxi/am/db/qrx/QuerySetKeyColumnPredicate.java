
package com.mxi.am.db.qrx;

import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.key.MxKey;


/**
 * ColumnPredicate matches the query set with the value at the column equal to value provided.
 */
public final class QuerySetKeyColumnPredicate implements Predicate<QuerySet> {

   private final MxKey iKey;
   private final Class<? extends MxKey> iClass;
   private final String[] iColumnNames;


   /**
    * Creates a new {@linkplain QuerySetKeyColumnPredicate} object.
    */
   public QuerySetKeyColumnPredicate(MxKey aKey, String... aColumnNames) {
      iKey = aKey;
      iClass = aKey.getClass();
      iColumnNames = aColumnNames;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean test( QuerySet aQs ) {
      MxKey lQueryValue = aQs.getKey( iClass, iColumnNames );
      return lQueryValue == null ? false : lQueryValue.equals( iKey );
   }


   @Override
   public String toString() {
      StringBuilder lColumnsNames = new StringBuilder();
      boolean lFirst = true;
      for ( String lColumnName : iColumnNames ) {
         if ( !lFirst ) {
            lColumnsNames.append( ":" );
         }
         lColumnsNames.append( lColumnName );
         lFirst = false;
      }

      return String.format( "%s equal to %s", lColumnsNames.toString(), iKey.toString() );
   }
}
