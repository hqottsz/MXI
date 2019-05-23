package com.mxi.am.domain.builder;

import com.mxi.am.domain.OneTimeSchedulingRule;
import com.mxi.mx.core.key.TaskSchedRuleKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskSchedRule;


/**
 * Builds a one time scheduling rule <code>task_sched_rule</code> object
 */
public class OneTimeSchedulingRuleBuilder extends
      SchedulingRuleBuilder<OneTimeSchedulingRuleBuilder> {

   /**
    * Build a one time scheduling rule
    *
    * @param aSchedulingRule
    * @param aTaskRevision
    * @return scheduling rule key
    */
   public TaskSchedRuleKey build( OneTimeSchedulingRule aSchedulingRule, TaskTaskKey aTaskRevision ) {

      TaskSchedRule lTable = super.build( aSchedulingRule, aTaskRevision );

      if ( aSchedulingRule.getThreshold() != null ) {
         lTable.setIntervalQt( aSchedulingRule.getThreshold().doubleValue() );
      }

      return lTable.insert();
   }
}
