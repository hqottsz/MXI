package com.mxi.am.domain.builder;

import com.mxi.am.domain.ToolRequirement;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.TaskToolListKey;
import com.mxi.mx.core.table.task.TaskToolList;


/**
 * Domain builder for tool requirement
 *
 */
public class ToolRequirementBuilder {

   public static TaskToolListKey build( ToolRequirement aToolRequirement ) {

      TaskToolList lTaskToolList =
            TaskToolList.create( aToolRequirement.getRequirementDefinition() );

      // Get the Part Group for the part number of the tool
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aToolRequirement.getTool(), "part_no_db_id", "part_no_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "eqp_part_baseline", lArgs );
      lQs.next();
      if ( lQs.getString( "bom_part_db_id" ) == null ) {
         throw new RuntimeException( "Tool must belong to a Tool part group" );
      }
      lTaskToolList.setBomPart(
            new PartGroupKey( lQs.getInt( "bom_part_db_id" ), lQs.getInt( "bom_part_id" ) ) );
      lTaskToolList.setSchedHr( aToolRequirement.getScheduledHours().doubleValue() );

      return lTaskToolList.insert();

   }

}
