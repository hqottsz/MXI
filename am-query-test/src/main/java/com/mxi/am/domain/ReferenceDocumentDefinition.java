package com.mxi.am.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class ReferenceDocumentDefinition {

   private TaskTaskKey iPreviousRevision;
   private String iCode;
   private Boolean iUnique;
   private Boolean iOnCondition;
   private Integer iRevisionNumber;
   private RefTaskDefinitionStatusKey iStatus;
   private ConfigSlotKey iConfigurationSlot;
   private Boolean iFleetApproval;
   private Date iEffectiveDate;

   private Map<RefTaskDepActionKey, Set<TaskTaskKey>> iLinkToTask =
         new HashMap<RefTaskDepActionKey, Set<TaskTaskKey>>();

   // Schedule From Defaults to Manufactured Date
   private ScheduleFromOption iScheduleFromOption = ScheduleFromOption.MANUFACTURED_DATE;


   public static enum ScheduleFromOption {
      MANUFACTURED_DATE, RECEIVED_DATE, EFFECTIVE_DATE
   };


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public String getCode() {
      return iCode;
   }


   public void setOnCondition( boolean aOnCondition ) {
      iOnCondition = aOnCondition;
   }


   public Boolean isOnCondition() {
      return iOnCondition;
   }


   public void setStatus( RefTaskDefinitionStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public RefTaskDefinitionStatusKey getStatus() {
      return iStatus;
   }


   public void setPreviousRevision( TaskTaskKey aPreviousRevision ) {
      iPreviousRevision = aPreviousRevision;
   }


   public TaskTaskKey getPreviousRevision() {
      return iPreviousRevision;
   }


   public void setUnique( boolean aUnique ) {
      iUnique = aUnique;
   }


   public Boolean isUnique() {
      return iUnique;
   }


   public void setRevisionNumber( Integer aRevisionNumber ) {
      iRevisionNumber = aRevisionNumber;
   }


   public Integer getRevisionNumber() {
      return iRevisionNumber;
   }


   public void addLinkToTask( RefTaskDepActionKey aAction, TaskTaskKey aLinkToTask ) {
      Set<TaskTaskKey> lLinkToTask = iLinkToTask.get( aAction );

      if ( lLinkToTask == null ) {
         lLinkToTask = new HashSet<TaskTaskKey>();
         iLinkToTask.put( aAction, lLinkToTask );
      }
      lLinkToTask.add( aLinkToTask );
   }


   public Map<RefTaskDepActionKey, Set<TaskTaskKey>> getLinkToTasksDefinitionMap() {
      return iLinkToTask;
   }


   public void againstConfigurationSlot( ConfigSlotKey aConfigurationSlot ) {
      iConfigurationSlot = aConfigurationSlot;
   }


   public ConfigSlotKey getConfigurationSlot() {
      return iConfigurationSlot;
   }


   public void setFleetApproval( Boolean aFleetApproval ) {
      iFleetApproval = aFleetApproval;
   }


   public Boolean getFleetApproval() {
      return iFleetApproval;
   }


   public void setScheduledFromManufacturedDate() {
      iScheduleFromOption = ScheduleFromOption.MANUFACTURED_DATE;
   }


   public void setScheduledFromReceivedDate() {
      iScheduleFromOption = ScheduleFromOption.RECEIVED_DATE;
   }


   public void setScheduledFromEffectiveDate( Date aEffectiveDate ) {
      if ( aEffectiveDate == null ) {
         throw new RuntimeException(
               "Effective Date must be provided when scheduling requirement from effective date." );
      }
      iScheduleFromOption = ScheduleFromOption.EFFECTIVE_DATE;
      iEffectiveDate = aEffectiveDate;
   }


   protected void setScheduleFromOption() {
      // Do not allow the user to set the schedule-from, we will set it via the
      // setScheduledFrom*Date() methods.
   }


   public ScheduleFromOption getScheduleFromOption() {
      return iScheduleFromOption;
   }


   public Date getEffectiveDate() {
      return iEffectiveDate;
   }

}
