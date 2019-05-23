package com.mxi.am.domain.reader;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.TaskWeightAndBalanceKey;


public class WeightAndBalanceImpactReader {

   public static List<TaskWeightAndBalanceKey> read( TaskTaskKey taskTasKKey ) {

      DataSetArgument args = new DataSetArgument();
      args.add( taskTasKKey, "task_db_id", "task_id" );

      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "TASK_WEIGHT_BAlANCE", args );

      List<TaskWeightAndBalanceKey> list = new ArrayList<>();
      while ( qs.next() ) {
         list.add( qs.getKey( TaskWeightAndBalanceKey.class, "task_weight_balance_db_id",
               "task_weight_balance_id" ) );

      }
      return list;
   }

}
