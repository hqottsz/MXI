package com.mxi.am.domain.builder;

import com.mxi.am.domain.RecurringSchedulingRule;
import com.mxi.mx.core.key.TaskSchedRuleKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskSchedRule;


/**
 * Builds a recurring <code>task_sched_rule</code> object
 *
 */
public class RecurringSchedulingRuleBuilder extends
      SchedulingRuleBuilder<RecurringSchedulingRuleBuilder> {

   /**
    * Build a recurring scheduling rule
    *
    * @param aSchedulingRule
    * @param aTaskRevision
    * @return scheduling rule key
    */
   public TaskSchedRuleKey build( RecurringSchedulingRule aRecurringSchedulingRule,
         TaskTaskKey aTaskRevision ) {
      TaskSchedRule lTable = super.build( aRecurringSchedulingRule, aTaskRevision );

      if ( aRecurringSchedulingRule.getInterval() != null ) {
         lTable.setIntervalQt( aRecurringSchedulingRule.getInterval().doubleValue() );
      }

      if ( aRecurringSchedulingRule.getInitialInterval() != null ) {
         lTable.setInitialQt( aRecurringSchedulingRule.getInitialInterval().doubleValue() );
      }

      return lTable.insert();
   }
}
