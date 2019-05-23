package com.mxi.am.domain.builder;

import com.mxi.am.domain.WeightAndBalanceImpact;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.TaskWeightAndBalanceKey;
import com.mxi.mx.core.table.task.TaskWeightAndBalanceTable;


public class WeightAndBalanceImpactBuilder {

   public static TaskWeightAndBalanceKey build( WeightAndBalanceImpact weightAndBalanceImpact ) {

      TaskTaskKey taskTaskKey =
            weightAndBalanceImpact.getTaskTaskKey().orElseThrow( () -> new IllegalStateException(
                  "Unable to build weight and balance.  Mandatory TaskTaskKey is missing." ) );

      TaskWeightAndBalanceKey weightAndBalanceKey =
            TaskWeightAndBalanceTable.generatePrimaryKey( taskTaskKey );

      TaskWeightAndBalanceTable weightAndBalanceTable =
            TaskWeightAndBalanceTable.create( weightAndBalanceKey );

      if ( weightAndBalanceImpact.getBalance().isPresent() ) {
         weightAndBalanceTable
               .setBalance( weightAndBalanceImpact.getBalance().get().doubleValue() );
      }

      if ( weightAndBalanceImpact.getWeight().isPresent() ) {
         weightAndBalanceTable.setWeight( weightAndBalanceImpact.getWeight().get().doubleValue() );
      }

      weightAndBalanceTable.setTaskKey( taskTaskKey );

      return weightAndBalanceTable.insert();

   }

}
