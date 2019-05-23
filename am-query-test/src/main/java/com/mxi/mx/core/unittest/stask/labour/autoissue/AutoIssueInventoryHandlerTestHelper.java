package com.mxi.mx.core.unittest.stask.labour.autoissue;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.FncXactionLogKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Helper class for Autoissue tests
 *
 */
public class AutoIssueInventoryHandlerTestHelper {

   /**
    * Get the financial transaction history for a given part no
    *
    * @param aPartNoKey
    *           part no key
    * @param aDays
    *           days in the past to search
    *
    * @return the financial transaction key
    */
   public static FncXactionLogKey getLatestTransactionByPart( PartNoKey aPartNoKey, int aDays ) {
      FncXactionLogKey lKey = null;
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNoKey, "aPartNoDbId", "aPartNoId" );
      lArgs.add( "aDayCount", aDays );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.part.PartTransactionHistory", lArgs );

      if ( lQs.next() ) {
         lKey = lQs.getKey( FncXactionLogKey.class, "trans_key" );
      }

      return lKey;
   }
}
