package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.PartRequirement.PartInstallRequest;
import com.mxi.am.domain.PartRequirement.PartRemovalRequest;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedPartTable;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * Domain Builder for Part Requirement
 *
 */
public class PartRequirementBuilder {

   public static TaskPartKey build( PartRequirement aPartRequirement ) {

      SchedPartTable lSchedPart = SchedPartTable.create( aPartRequirement.getTaskKey(),
            aPartRequirement.getPartGroup(), aPartRequirement.getAssemblyConfigSlotPosition(),
            aPartRequirement.getConfigSlotPosition(), null,
            aPartRequirement.getQuantity().doubleValue(), null, null,
            aPartRequirement.getControlMethodKey() );

      final TaskPartKey lTaskPartKey = lSchedPart.getPk();

      for ( PartInstallRequest lPartInstallRequest : aPartRequirement.getPartInstallRequests() ) {

         DataSetArgument lArgs = lTaskPartKey.getPKWhereArg();
         lArgs.add( "sched_inst_part_id", 1 );
         lArgs.add( "inst_qt", lPartInstallRequest.getQuantity() );

         if ( lPartInstallRequest.getPartNoKey() != null ) {
            lArgs.add( lPartInstallRequest.getPartNoKey(), "part_no_db_id", "part_no_id" );
         } else {
            throw new RuntimeException(
                  "Part requirement must have a part number assigned to it for install" );
         }
         if ( lPartInstallRequest.getInventory() != null ) {
            lArgs.add( lPartInstallRequest.getInventory().getPKWhereArg() );
         } else {
            throw new RuntimeException(
                  "Part requirement must have an inventory assigned to it for install" );
         }

         InvInvTable lInvInvTable = InvInv.findByPrimaryKey( lPartInstallRequest.getInventory() );
         String lInventorySerialNo = ( String ) defaultIfNull( lInvInvTable.getSerialNoOem(),
               "SerialNo" + lInvInvTable.getPk() );
         lArgs.add( "serial_no_oem", lInventorySerialNo );
         MxDataAccess.getInstance().executeInsert( "sched_inst_part", lArgs );
      }

      for ( PartRemovalRequest lPartRemovalRequest : aPartRequirement.getPartRemovalRequests() ) {

         DataSetArgument lArgs = lTaskPartKey.getPKWhereArg();
         lArgs.add( "sched_rmvd_part_id", 1 );
         lArgs.add( "rmvd_qt", lPartRemovalRequest.getQuantity() );

         if ( lPartRemovalRequest.getPartNoKey() != null ) {
            lArgs.add( lPartRemovalRequest.getPartNoKey(), "part_no_db_id", "part_no_id" );
         } else {
            throw new RuntimeException(
                  "Part requirement must have a part number assigned to it for removal" );
         }

         if ( lPartRemovalRequest.getInventory() != null ) {
            lArgs.add( lPartRemovalRequest.getInventory().getPKWhereArg() );
         } else {
            throw new RuntimeException(
                  "Part requirement must have an inventory assigned to it for removal" );
         }

         RefRemoveReasonKey lRemovalReason =
               ( RefRemoveReasonKey ) defaultIfNull( lPartRemovalRequest.getRemovalReason(),
                     RefRemoveReasonKey.IMSCHD );

         lArgs.add( lRemovalReason, "remove_reason_db_id", "remove_reason_cd" );

         InvInvTable lInvInvTable = InvInv.findByPrimaryKey( lPartRemovalRequest.getInventory() );
         String lInventorySerialNo = ( String ) defaultIfNull( lInvInvTable.getSerialNoOem(),
               "SerialNo" + lInvInvTable.getPk() );
         lArgs.add( "serial_no_oem", lInventorySerialNo );

         MxDataAccess.getInstance().executeInsert( "sched_rmvd_part", lArgs );
      }

      return lTaskPartKey;

   }

}
