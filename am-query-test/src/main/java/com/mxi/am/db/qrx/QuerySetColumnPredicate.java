
package com.mxi.am.db.qrx;

import com.mxi.mx.common.dataset.QuerySet;


/**
 * QuerySetColumnPredicate matches rows with the value matching the provided value.
 */
public final class QuerySetColumnPredicate implements Predicate<QuerySet> {

   private final Object iValue;
   private final String iColumnName;


   public QuerySetColumnPredicate(Object aValue, String aColumnName) {
      iValue = aValue;
      iColumnName = aColumnName;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean test( QuerySet aQs ) {
      Object lQuerySetValue = aQs.getObject( iColumnName );

      return lQuerySetValue == null ? false : lQuerySetValue.equals( iValue );
   }


   @Override
   public String toString() {
      return String.format( "%s equal to %s", iColumnName, iValue.toString() );
   }
}
