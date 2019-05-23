package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Optional;

import com.mxi.mx.core.key.TaskTaskKey;


public class WeightAndBalanceImpact {

   private BigDecimal weight;
   private BigDecimal balance;
   private TaskTaskKey taskTaskKey;


   public Optional<BigDecimal> getWeight() {
      return Optional.ofNullable( weight );
   }


   public void setWeight( BigDecimal weight ) {
      this.weight = weight;
   }


   public Optional<BigDecimal> getBalance() {
      return Optional.ofNullable( balance );
   }


   public void setBalance( BigDecimal balance ) {
      this.balance = balance;
   }


   public Optional<TaskTaskKey> getTaskTaskKey() {
      return Optional.ofNullable( taskTaskKey );
   }


   public void setTaskTaskKey( TaskTaskKey taskTaskKey ) {
      this.taskTaskKey = taskTaskKey;
   }

}
