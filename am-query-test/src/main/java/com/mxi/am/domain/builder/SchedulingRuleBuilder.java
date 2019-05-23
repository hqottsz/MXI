package com.mxi.am.domain.builder;

import com.mxi.am.domain.SchedulingRule;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.core.key.TaskSchedRuleKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskSchedRule;


/**
 * Abstract builder for scheduling
 *
 */
public abstract class SchedulingRuleBuilder<T extends SchedulingRuleBuilder<T>> {

   /**
    * Build the common properties
    *
    * @param aSchedulingRule
    * @param aTaskRevision
    * @return Scheduling rule table object
    */
   protected TaskSchedRule build( SchedulingRule aSchedulingRule, TaskTaskKey aTaskRevision ) {
      if ( aSchedulingRule.getUsageParameter() == null ) {
         throw new MxRuntimeException( "Parameter date type may not be null" );
      }

      if ( aTaskRevision == null ) {
         throw new MxRuntimeException( "Task revision may not be null" );
      }

      TaskSchedRuleKey lTaskSchedRuleKey =
            new TaskSchedRuleKey( aTaskRevision, aSchedulingRule.getUsageParameter() );

      TaskSchedRule lTable = TaskSchedRule.create( lTaskSchedRuleKey );

      // The calculation for a deadline, which is based on a scheduling rule, is performed by plsql
      // and does not work when the deviation is null. Therefore, set to 0 when not provided.
      if ( aSchedulingRule.getDeviation() != null ) {
         lTable.setDeviationQt( aSchedulingRule.getDeviation().doubleValue() );
      } else {
         lTable.setDeviationQt( 0.0 );
      }

      if ( aSchedulingRule.getNotification() != null ) {
         lTable.setNotifyQt( aSchedulingRule.getNotification().doubleValue() );
      }

      if ( aSchedulingRule.getSchedToPlanLow() != null ) {
         lTable.setPrefixedQt( aSchedulingRule.getSchedToPlanLow().doubleValue() );
      }

      if ( aSchedulingRule.getSchedToPlanHigh() != null ) {
         lTable.setPostfixedQt( aSchedulingRule.getSchedToPlanHigh().doubleValue() );
      }

      return lTable;
   }

}
