
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.ToolKey;
import com.mxi.mx.core.services.partgroup.AlternatePartService;
import com.mxi.mx.core.table.evt.EvtToolTable;


/**
 * This builder allows developers to build tool requirements for tasks.
 *
 */
public class ToolRequirementDomainBuilder implements DomainBuilder<ToolKey> {

   private PartGroupKey iToolGroup;
   private PartNoKey iToolNo;
   private InventoryKey iAssignedInventory;
   private TaskKey iTask;


   /**
    * Creates a new {@linkplain ToolRequirementDomainBuilder} object.
    *
    * @param aTask
    *           The task for this part requirement.
    */
   public ToolRequirementDomainBuilder(TaskKey aTask) {
      iTask = aTask;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ToolKey build() {

      if ( iToolGroup == null && iToolNo != null ) {
         // if the tool group is not provided, use the first tool group that the tool is in
         iToolGroup = AlternatePartService.getBomPartsFromPartNo( iToolNo )[0];
      } else {
         // You must provide either a tool part group or a tool part number to the builder
         return null;
      }

      EvtToolTable lEvtTool = EvtToolTable.create( iTask.getEventKey(), iToolGroup, iToolNo );
      lEvtTool.setInventoryKey( iAssignedInventory );

      return ( ToolKey ) lEvtTool.getPk();
   }


   /**
    * Sets the tool of the tool requirement.
    *
    * @param aToolNo
    *           The tool part number key
    *
    * @return The builder.
    */
   public ToolRequirementDomainBuilder forTool( PartNoKey aToolNo ) {
      iToolNo = aToolNo;

      return this;
   }


   /**
    * Sets the tool group of the tool requirement.
    *
    * @param aToolGroup
    *           The tool part group
    *
    * @return The builder.
    */
   public ToolRequirementDomainBuilder forToolGroup( PartGroupKey aToolGroup ) {
      iToolGroup = aToolGroup;

      return this;
   }


   /**
    * Sets the tool inventory assigned to the tool requirement.
    *
    * @param aToolInventory
    *           the tool inventory item
    *
    * @return The builder.
    */
   public ToolRequirementDomainBuilder withAssignedInventory( InventoryKey aToolInventory ) {
      iAssignedInventory = aToolInventory;

      return this;
   }
}
