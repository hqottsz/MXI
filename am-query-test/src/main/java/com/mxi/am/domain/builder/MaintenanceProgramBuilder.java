
package com.mxi.am.domain.builder;

import java.util.Map;

import com.mxi.am.domain.MaintenanceProgram;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.MaintPrgmCarrierMapKey;
import com.mxi.mx.core.key.MaintPrgmDefnKey;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.MaintPrgmTaskKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.maint.MaintPrgmCarrierMapTable;
import com.mxi.mx.core.table.maint.MaintPrgmTable;
import com.mxi.mx.core.table.maint.MaintPrgmTaskTable;


public class MaintenanceProgramBuilder {

   public static MaintPrgmKey build( MaintenanceProgram aMp ) {

      MaintPrgmDefnKey lMaintPrgmDefn = aMp.getMaintenanceProgramDefinition();
      if ( lMaintPrgmDefn == null ) {
         throw new RuntimeException(
               "Maintenance program definition required to create maintenance program." );
      }

      MaintPrgmTable lMaintPrgmTable = MaintPrgmTable.create();
      lMaintPrgmTable.setMaintPrgmDefn( lMaintPrgmDefn );
      MaintPrgmKey lMaintPrgmKey = lMaintPrgmTable.insert();

      CarrierKey lOperator = aMp.getOperator();
      if ( lOperator != null ) {
         MaintPrgmCarrierMapTable lMaintPrgmCarrierMapTable = MaintPrgmCarrierMapTable
               .create( new MaintPrgmCarrierMapKey( lMaintPrgmKey, lOperator ) );
         lMaintPrgmCarrierMapTable.setLatestRevisionBool( aMp.isLatestRevisionForOperator() );
         lMaintPrgmCarrierMapTable.insert();
      }

      Map<TaskDefnKey, TaskTaskKey> lReqDefns = aMp.getRequirementDefinitions();
      for ( TaskDefnKey lReqDefnKey : lReqDefns.keySet() ) {
         MaintPrgmTaskTable lMaintPrgmTaskTable =
               MaintPrgmTaskTable.create( new MaintPrgmTaskKey( lMaintPrgmKey, lReqDefnKey ) );
         lMaintPrgmTaskTable.setTask( lReqDefns.get( lReqDefnKey ) );
         lMaintPrgmTaskTable.insert();
      }

      return lMaintPrgmKey;

   }
}
