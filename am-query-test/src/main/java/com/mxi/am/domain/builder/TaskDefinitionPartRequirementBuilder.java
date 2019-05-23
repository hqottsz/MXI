package com.mxi.am.domain.builder;

import com.mxi.am.domain.TaskDefinitionPartRequirement;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefPartProviderTypeKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.TaskPartListKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskPartList;


/**
 * Builds TaskDefinitionPartRequirements.
 */
public class TaskDefinitionPartRequirementBuilder {

   private TaskTaskKey iTaskDefinition;
   private ConfigSlotPositionKey iBomItemPosition;
   private RefRemoveReasonKey iRemoveReason;
   private PartGroupKey iPartGroup;
   private PartNoKey iSpecificPart;
   private RefPartProviderTypeKey iPartProviderType;
   private double iRequiredQuantity = 1;
   private boolean iIsRemove;
   private boolean iIsInstall;
   private RefReqPriorityKey iReqPriority = RefReqPriorityKey.NORMAL;
   private RefReqActionKey iReqAction = RefReqActionKey.NOREQ;


   public TaskDefinitionPartRequirementBuilder withTaskDefinition( TaskTaskKey aTaskDefinition ) {
      iTaskDefinition = aTaskDefinition;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder
         withBomItemPosition( ConfigSlotPositionKey aBomItemPosition ) {
      iBomItemPosition = aBomItemPosition;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder
         withRemoveReason( RefRemoveReasonKey aRemoveReason ) {
      iRemoveReason = aRemoveReason;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder withPartGroup( PartGroupKey aPartGroup ) {
      iPartGroup = aPartGroup;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder withSpecificPart( PartNoKey aSpecificPart ) {
      iSpecificPart = aSpecificPart;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder
         withPartProviderType( RefPartProviderTypeKey aPartProviderType ) {
      iPartProviderType = aPartProviderType;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder withRequiredQuantity( double aRequiredQuantity ) {
      iRequiredQuantity = aRequiredQuantity;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder withIsRemove( boolean aIsRemove ) {
      iIsRemove = aIsRemove;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder withIsInstall( boolean aIsInstall ) {
      iIsInstall = aIsInstall;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder withReqPriority( RefReqPriorityKey aReqPriority ) {
      iReqPriority = aReqPriority;
      return this;
   }


   public TaskDefinitionPartRequirementBuilder withReqAction( RefReqActionKey aReqAction ) {
      iReqAction = aReqAction;
      return this;
   }


   public TaskPartListKey build() {
      TaskPartList lPartRequirement =
            TaskPartList.create( TaskPartList.generatePrimaryKey( iTaskDefinition ) );
      {
         lPartRequirement.setBomItemPosition( iBomItemPosition );
         lPartRequirement.setBOMPart( iPartGroup );
         lPartRequirement.setInstallBool( iIsInstall );
         lPartRequirement.setPartProviderType( iPartProviderType );
         lPartRequirement.setRemoveBool( iIsRemove );
         lPartRequirement.setRemoveReason( iRemoveReason );
         lPartRequirement.setReqPriority( iReqPriority );
         lPartRequirement.setReqQt( iRequiredQuantity );
         lPartRequirement.setRequestAction( iReqAction );
         lPartRequirement.setSpecPartNo( iSpecificPart );
      }
      return lPartRequirement.insert();
   }


   public static TaskPartListKey build( TaskDefinitionPartRequirement aPartRequirement ) {
      TaskPartList lPartRequirement = TaskPartList
            .create( TaskPartList.generatePrimaryKey( aPartRequirement.getTaskDefinition() ) );
      {
         lPartRequirement.setBomItemPosition( aPartRequirement.getBomItemPosition() );
         lPartRequirement.setBOMPart( aPartRequirement.getPartGroup() );
         lPartRequirement.setInstallBool( aPartRequirement.isInstall() );
         lPartRequirement.setPartProviderType( aPartRequirement.getPartProviderType() );
         lPartRequirement.setRemoveBool( aPartRequirement.isRemove() );
         lPartRequirement.setRemoveReason( aPartRequirement.getRemoveReason() );
         lPartRequirement.setReqPriority( aPartRequirement.getReqPriority() );
         lPartRequirement.setReqQt( aPartRequirement.getRequiredQuantity() );
         lPartRequirement.setRequestAction( aPartRequirement.getReqAction() );
         lPartRequirement.setSpecPartNo( aPartRequirement.getSpecificPart() );
      }
      return lPartRequirement.insert();
   }

}
