package com.mxi.am.domain.builder;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.ExternalReferenceItemKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefControlMethodKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.table.sched.SchedPartTable;


/**
 * This builder allows developers to build part requirements for tasks.
 *
 * @author dsewell
 */
public class PartRequirementDomainBuilder implements DomainBuilder<TaskPartKey> {

   private ConfigSlotPositionKey iAssyBomPosition;
   private PartGroupKey iBomPart;
   private ConfigSlotPositionKey iBomPosition;
   private PartNoKey iInstallPartNo;
   private Double iInstallQuantity;
   private Double iRemovalQuantity;
   private RefRemoveReasonKey iRemovalReason = null;
   private RefReqActionKey iRequestAction;
   private ExternalReferenceItemKey extRefItemKey = null;
   private RefControlMethodKey controlMethodKey;
   private double iSchedQt;
   private PartNoKey iSpecPartNo;
   private InventoryKey iRemovalInventoryKey;
   private String iRemovalSerialNo;
   private String iInstallSerialNo;
   private InventoryKey iInstallInventoryKey;
   private TaskKey iTask;


   /**
    * Creates a new {@linkplain PartRequirementDomainBuilder} object.
    *
    * @param aTask
    *           The task for this part requirement.
    */
   public PartRequirementDomainBuilder(TaskKey aTask) {
      iTask = aTask;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public TaskPartKey build() {

      SchedPartTable lSchedPart = SchedPartTable.create( iTask, iBomPart, iAssyBomPosition,
            iBomPosition, iSpecPartNo, iSchedQt, iRequestAction, extRefItemKey, controlMethodKey );

      final TaskPartKey lTaskPartKey = lSchedPart.getPk();

      if ( iInstallQuantity != null ) {

         DataSetArgument lArgs = lTaskPartKey.getPKWhereArg();
         lArgs.add( "sched_inst_part_id", 1 );
         lArgs.add( "inst_qt", iInstallQuantity );

         if ( iInstallPartNo != null ) {
            lArgs.add( iInstallPartNo, "part_no_db_id", "part_no_id" );
         }

         if ( iInstallSerialNo != null ) {
            lArgs.add( "serial_no_oem", iInstallSerialNo );
         }

         if ( iInstallInventoryKey != null ) {
            lArgs.add( iInstallInventoryKey.getPKWhereArg() );
         }

         MxDataAccess.getInstance().executeInsert( "sched_inst_part", lArgs );
      }

      if ( iRemovalQuantity != null ) {

         DataSetArgument lArgs = lTaskPartKey.getPKWhereArg();
         lArgs.add( "sched_rmvd_part_id", 1 );
         lArgs.add( "rmvd_qt", iRemovalQuantity );

         if ( iRemovalReason != null ) {
            lArgs.add( iRemovalReason, "remove_reason_db_id", "remove_reason_cd" );
         }

         if ( iSpecPartNo != null ) {
            lArgs.add( iSpecPartNo, "part_no_db_id", "part_no_id" );
         }

         if ( iRemovalSerialNo != null ) {
            lArgs.add( "serial_no_oem", iRemovalSerialNo );
         }

         if ( iRemovalInventoryKey != null ) {
            lArgs.add( iRemovalInventoryKey, "inv_no_db_id", "inv_no_id" );
         }

         MxDataAccess.getInstance().executeInsert( "sched_rmvd_part", lArgs );
      }

      return lTaskPartKey;
   }


   /**
    * Sets the part of the part requirement.
    *
    * @param aPartNo
    *           The part number key
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder forPart( PartNoKey aPartNo ) {
      iSpecPartNo = aPartNo;

      return this;
   }


   /**
    * Sets the part group of the part requirement.
    *
    * @param aPartGroup
    *           The part group
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder forPartGroup( PartGroupKey aPartGroup ) {
      iBomPart = aPartGroup;

      return this;
   }


   /**
    * Sets the position of the part requirement.
    *
    * @param aBomPosition
    *           The position
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder forPosition( ConfigSlotPositionKey aBomPosition ) {
      iBomPosition = aBomPosition;

      return this;
   }


   /**
    * Sets the part no key on the install part
    *
    * @param aPartNoKey
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withInstallPart( PartNoKey aPartNoKey ) {
      iInstallPartNo = aPartNoKey;

      return this;
   }


   /**
    * Sets the quantity of the part to be installed.
    *
    * @param aInstallQuantity
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withInstallQuantity( double aInstallQuantity ) {
      iInstallQuantity = aInstallQuantity;

      return this;
   }


   /**
    * Sets the next heighest assembly position for the part requirement.
    *
    * @param aNHPosition
    *           The position
    *
    * @return The builder
    */
   public PartRequirementDomainBuilder
         withNextHighestPosition( ConfigSlotPositionKey aNHPosition ) {
      iAssyBomPosition = aNHPosition;

      return this;
   }


   /**
    * Sets the quantity of the part to be removed.
    *
    * @param aRemovalQuantity
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withRemovalQuantity( double aRemovalQuantity ) {
      iRemovalQuantity = aRemovalQuantity;

      return this;
   }


   /**
    * Sets the removal reason of the part to be removed.
    *
    * @param aRemovalReason
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withRemovalReason( RefRemoveReasonKey aRemovalReason ) {
      iRemovalReason = aRemovalReason;

      return this;
   }


   /**
    * Sets the inventory for the part to be removed.
    *
    * @param aInventoryKey
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withRemovalInventory( InventoryKey aInventoryKey ) {
      iRemovalInventoryKey = aInventoryKey;

      return this;
   }


   /**
    * Sets the serial number of the part to be removed.
    *
    * @param aSerialNo
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withRemovalSerialNo( String aSerialNo ) {
      iRemovalSerialNo = aSerialNo;

      return this;
   }


   /**
    * Sets the serial number of the part to be installed.
    *
    * @param aSerialNo
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withInstallSerialNumber( String aSerialNo ) {
      iInstallSerialNo = aSerialNo;

      return this;
   }


   /**
    * Sets the inventory of the part to be installed.
    *
    * @param aSerialNo
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withInstallInventory( InventoryKey aInventoryKey ) {
      this.iInstallInventoryKey = aInventoryKey;

      return this;
   }


   /**
    * Sets the request action.
    *
    * @param aRequestAction
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withRequestAction( RefReqActionKey aRequestAction ) {
      iRequestAction = aRequestAction;

      return this;
   }


   /**
    * Sets the control method. How the part requirement was created: manually/baseline/external
    *
    * @param controlMethodKey
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder withControlMethod( RefControlMethodKey controlMethodKey ) {
      this.controlMethodKey = controlMethodKey;

      return this;
   }


   /**
    * Sets the control method. How the part requirement was created: manually/baseline/external
    *
    * @param controlMethodKey
    *
    * @return The builder.
    */
   public PartRequirementDomainBuilder
         withReference( ExternalReferenceItemKey externalReferenceItemKey ) {
      this.extRefItemKey = externalReferenceItemKey;

      return this;
   }

}
