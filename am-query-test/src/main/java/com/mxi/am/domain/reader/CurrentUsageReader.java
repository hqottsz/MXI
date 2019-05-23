package com.mxi.am.domain.reader;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Reader for retrieving current usage of an inventory.
 *
 */
public class CurrentUsageReader {

   private static final String INV_CURR_USAGE_TABLE = "inv_curr_usage";
   private static final String CURRENT_USAGE_TSN = "TSN_QT";
   private static final String COLUMN_DATA_TYPE_DB_ID = "DATA_TYPE_DB_ID";
   private static final String COLUMN_DATA_TYPE_ID = "DATA_TYPE_ID";


   /**
    * Retrieve a map of Usage Parameter and associated current usage for an inventory.
    *
    * @param aInventoryKey
    * @return
    */
   public static Map<DataTypeKey, BigDecimal> read( InventoryKey aInventoryKey ) {
      Map<DataTypeKey, BigDecimal> lUsageDeltaMap = new HashMap<>();
      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( INV_CURR_USAGE_TABLE,
                  aInventoryKey.getPKWhereArg() );
      while ( lQs.next() ) {
         BigDecimal lDelta = lQs.getBigDecimal( CURRENT_USAGE_TSN );
         lUsageDeltaMap
               .put( lQs.getKey( DataTypeKey.class, COLUMN_DATA_TYPE_DB_ID, COLUMN_DATA_TYPE_ID ),
                     lDelta );
      }
      return lUsageDeltaMap;
   }

}
