
package com.mxi.am.db.qrx;

import com.mxi.mx.common.dataset.QuerySet;


/**
 * Selects a row from a {@link QuerySet}
 */
public final class QuerySetRowSelector {

   private QuerySetRowSelector() {
      // utility class
   }


   /**
    * Selects the first row that matches the specified predicate
    *
    * @param aQs
    *           the query set
    * @param aPredicate
    *           the predicate
    */
   public static void select( QuerySet aQs, Predicate<QuerySet> aPredicate ) {
      aQs.beforeFirst();
      while ( aQs.next() ) {
         if ( aPredicate.test( aQs ) ) {
            return;
         }
      }

      throw new IllegalArgumentException(
            "Could not find row: " + aPredicate + ".\n" + QuerySetDescriber.describe( aQs ) );
   }
}
