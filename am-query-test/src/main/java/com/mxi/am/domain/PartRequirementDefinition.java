package com.mxi.am.domain;

import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Domain class for Part Requirement
 *
 */
public class PartRequirementDefinition {

   private PartGroupKey iPartGroup;
   private ConfigSlotPositionKey iPosition;
   private Double iQuantity;
   private Boolean iIsInstall;
   private Boolean iIsRemoval;
   private RefRemoveReasonKey iRemovalReason;
   private PartNoKey iSpecificPart;
   private RefReqActionKey iAction;
   private TaskTaskKey iTaskDefinition;


   public PartGroupKey getPartGroup() {
      return iPartGroup;
   }


   public void setPartGroup( PartGroupKey aPartGroup ) {
      iPartGroup = aPartGroup;
   }


   public ConfigSlotPositionKey getPosition() {
      return iPosition;
   }


   public void setPosition( ConfigSlotPositionKey aPosition ) {
      iPosition = aPosition;
   }


   public Double getQuantity() {
      return iQuantity;
   }


   public void setQuantity( Double aQuantity ) {
      iQuantity = aQuantity;
   }


   public Boolean isIsInstall() {
      return iIsInstall;
   }


   public void setIsInstall( Boolean aIsInstall ) {
      iIsInstall = aIsInstall;
   }


   public Boolean isIsRemoval() {
      return iIsRemoval;
   }


   public void setIsRemoval( Boolean aIsRemoval ) {
      iIsRemoval = aIsRemoval;
   }


   public RefRemoveReasonKey getRemovalReason() {
      return iRemovalReason;
   }


   public void setRemovalReason( RefRemoveReasonKey aRemovalReason ) {
      iRemovalReason = aRemovalReason;
   }


   public PartNoKey getSpecificPart() {
      return iSpecificPart;
   }


   public void setSpecificPart( PartNoKey aSpecificPart ) {
      iSpecificPart = aSpecificPart;
   }


   public RefReqActionKey getAction() {
      return iAction;
   }


   public void setAction( RefReqActionKey aAction ) {
      iAction = aAction;
   }


   public TaskTaskKey getTaskDefinition() {
      return iTaskDefinition;
   }


   public void setTaskDefinition( TaskTaskKey aTaskDefinition ) {
      iTaskDefinition = aTaskDefinition;
   }

}
