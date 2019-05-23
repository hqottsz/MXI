package com.mxi.am.domain.builder;

import com.mxi.am.domain.LabourRole;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusTable;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;


public class LabourRoleBuilder {

   public static SchedLabourRoleKey build( LabourRole labourRole ) {

      SchedLabourRoleTable schedLabourRoleRow = SchedLabourRoleTable.create();
      schedLabourRoleRow.setSchedLabour( labourRole.getLabour() );
      schedLabourRoleRow.setLabourRoleType( labourRole.getType() );
      schedLabourRoleRow.setActualStartDate( labourRole.getActualStartDate() );
      schedLabourRoleRow.setActualEndDate( labourRole.getActualEndDate() );
      labourRole.getScheduledHours().ifPresent( hours -> {
         schedLabourRoleRow.setSchedHours( hours.doubleValue() );
      } );
      labourRole.getActualHours().ifPresent( hours -> {
         schedLabourRoleRow.setActualHours( hours.doubleValue() );
      } );
      SchedLabourRoleKey schedLabourRole = schedLabourRoleRow.insert();

      SchedLabourRoleStatusTable schedLabourRoleStatusRow = SchedLabourRoleStatusTable.create();
      schedLabourRoleStatusRow.setSchedLabourRole( schedLabourRole );
      schedLabourRoleStatusRow.setHr( labourRole.getHumanResouce() );

      // Set default values.
      schedLabourRoleStatusRow
            .setLabourRoleStatus( labourRole.getStatus().orElse( RefLabourRoleStatusKey.ACTV ) );
      schedLabourRoleStatusRow.setStatusOrder( 1 );

      schedLabourRoleStatusRow.insert();

      return schedLabourRole;
   }

}
