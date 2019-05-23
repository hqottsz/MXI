package com.mxi.am.domain.reader;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Reader for retrieving Step information for Requirement Definitions.
 */
public class RequirementDefinitionStepReader {

   /**
    * Returns the step key of the corresponding requirement definition step using the provided
    * definitions step number, if exists.
    */
   public static TaskStepKey read( TaskTaskKey requirementDefinition, int stepNumber ) {
      DataSetArgument args = new DataSetArgument();
      args.add( requirementDefinition, "task_db_id", "task_id" );
      args.add( "step_ord", stepNumber );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "task_step", args );
      if ( qs.getRowCount() > 1 ) {
         throw new RuntimeException( "More than one row exists in task_step for task defn:"
               + requirementDefinition + " and step order: " + stepNumber );
      }
      qs.next();
      return qs.getKey( TaskStepKey.class, "task_db_id", "task_id", "step_id" );
   }

}
