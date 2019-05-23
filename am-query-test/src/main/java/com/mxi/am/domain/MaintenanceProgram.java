package com.mxi.am.domain;

import java.util.HashMap;
import java.util.Map;

import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.MaintPrgmDefnKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * A maintenance program.
 *
 */
public class MaintenanceProgram {

   private MaintPrgmDefnKey iMaintPrgmDefnKey;
   private CarrierKey iOperator;
   private Boolean iLatestRevisionForOperator;
   private Map<TaskDefnKey, TaskTaskKey> iRequirementDefinitions =
         new HashMap<TaskDefnKey, TaskTaskKey>();


   public void basedOnDefinition( MaintPrgmDefnKey aMaintPrgmDefnKey ) {
      iMaintPrgmDefnKey = aMaintPrgmDefnKey;
   }


   public MaintPrgmDefnKey getMaintenanceProgramDefinition() {
      return iMaintPrgmDefnKey;
   }


   public void setOperator( CarrierKey aOperator ) {
      iOperator = aOperator;
   }


   public CarrierKey getOperator() {
      return iOperator;
   }


   public void setLatestRevisionForOperator( boolean aIsLatest ) {
      iLatestRevisionForOperator = aIsLatest;
   }


   public Boolean isLatestRevisionForOperator() {
      return iLatestRevisionForOperator;
   }


   public void addRequirementDefinition( TaskDefnKey aReqDefn, TaskTaskKey aReqDefnRev ) {
      iRequirementDefinitions.put( aReqDefn, aReqDefnRev );
   }


   public Map<TaskDefnKey, TaskTaskKey> getRequirementDefinitions() {
      return iRequirementDefinitions;
   }

}
