package com.mxi.am.domain;

import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefPartProviderTypeKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class TaskDefinitionPartRequirement {

   private TaskTaskKey iTaskDefinition;
   private ConfigSlotPositionKey iBomItemPosition;
   private RefRemoveReasonKey iRemoveReason;
   private PartGroupKey iPartGroup;
   private PartNoKey iSpecificPart;
   private RefPartProviderTypeKey iPartProviderType;
   private double iRequiredQuantity;
   private boolean iIsRemove;
   private boolean iIsInstall;
   private RefReqPriorityKey iReqPriority;
   private RefReqActionKey iReqAction = RefReqActionKey.NOREQ;


   public TaskTaskKey getTaskDefinition() {
      return iTaskDefinition;
   }


   public void setTaskDefinition( TaskTaskKey aTaskDefinition ) {
      iTaskDefinition = aTaskDefinition;
   }


   public ConfigSlotPositionKey getBomItemPosition() {
      return iBomItemPosition;
   }


   public void setBomItemPosition( ConfigSlotPositionKey aBomItemPosition ) {
      iBomItemPosition = aBomItemPosition;
   }


   public RefRemoveReasonKey getRemoveReason() {
      return iRemoveReason;
   }


   public void setRemoveReason( RefRemoveReasonKey aRemoveReason ) {
      iRemoveReason = aRemoveReason;
   }


   public PartGroupKey getPartGroup() {
      return iPartGroup;
   }


   public void setPartGroup( PartGroupKey aPartGroup ) {
      iPartGroup = aPartGroup;
   }


   public PartNoKey getSpecificPart() {
      return iSpecificPart;
   }


   public void setSpecificPart( PartNoKey aSpecificPart ) {
      iSpecificPart = aSpecificPart;
   }


   public RefPartProviderTypeKey getPartProviderType() {
      return iPartProviderType;
   }


   public void setPartProviderType( RefPartProviderTypeKey aPartProviderType ) {
      iPartProviderType = aPartProviderType;
   }


   public double getRequiredQuantity() {
      return iRequiredQuantity;
   }


   public void setRequiredQuantity( double aRequiredQuantity ) {
      iRequiredQuantity = aRequiredQuantity;
   }


   public boolean isRemove() {
      return iIsRemove;
   }


   public void setIsRemove( boolean aIsRemove ) {
      iIsRemove = aIsRemove;
   }


   public boolean isInstall() {
      return iIsInstall;
   }


   public void setIsInstall( boolean aIsInstall ) {
      iIsInstall = aIsInstall;
   }


   public RefReqPriorityKey getReqPriority() {
      return iReqPriority;
   }


   public void setReqPriority( RefReqPriorityKey aReqPriority ) {
      iReqPriority = aReqPriority;
   }


   public RefReqActionKey getReqAction() {
      return iReqAction;
   }


   public void setReqAction( RefReqActionKey aReqAction ) {
      iReqAction = aReqAction;
   }


   @Override
   public String toString() {
      return "TaskDefinitionPartRequirement [iTaskDefinition=" + iTaskDefinition
            + ", iBomItemPosition=" + iBomItemPosition + ", iRemoveReason=" + iRemoveReason
            + ", iPartGroup=" + iPartGroup + ", iSpecificPart=" + iSpecificPart
            + ", iPartProviderType=" + iPartProviderType + ", iRequiredQuantity="
            + iRequiredQuantity + ", iIsRemove=" + iIsRemove + ", iIsInstall=" + iIsInstall
            + ", iReqPriority=" + iReqPriority + ", iReqAction=" + iReqAction + "]";
   }

}
