package com.mxi.mx.core.ejb.flighthist;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


/**
 * Holds the snapshot usage for an inventory against a usage record
 *
 */
public class SnapshotUsage {

   private static final String TSN = "TSN";
   private static final String TSO = "TSO";
   private static final String TSI = "TSI";

   private Map<DataTypeKey, Map<String, BigDecimal>> lUsages =
         new HashMap<DataTypeKey, Map<String, BigDecimal>>();


   public SnapshotUsage(InventoryKey aInventoryKey, UsageAdjustmentId aUsageRecordId) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, "inv_no_db_id", "inv_no_id" );
      lArgs.add( "USAGE_RECORD_ID", aUsageRecordId.toString() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "usg_usage_data", lArgs );
      while ( lQs.next() ) {
         DataTypeKey lDataType = lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" );

         if ( !lUsages.containsKey( lDataType ) ) {
            lUsages.put( lDataType, new HashMap<String, BigDecimal>() );
         }
         Map<String, BigDecimal> lValues = lUsages.get( lDataType );
         lValues.put( TSN, lQs.getBigDecimal( "tsn_qt" ) );
         lValues.put( TSO, lQs.getBigDecimal( "tso_qt" ) );
         lValues.put( TSI, lQs.getBigDecimal( "tsi_qt" ) );
      }
   }


   public BigDecimal getTsn( DataTypeKey lDataTypeKey ) {
      return lUsages.get( lDataTypeKey ).get( TSN );
   }


   public BigDecimal getTso( DataTypeKey lDataTypeKey ) {
      return lUsages.get( lDataTypeKey ).get( TSO );
   }


   public BigDecimal getTsi( DataTypeKey lDataTypeKey ) {
      return lUsages.get( lDataTypeKey ).get( TSI );
   }
}
