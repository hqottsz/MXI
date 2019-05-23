package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefImpactKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefSchedPriorityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskPriorityKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;


public class Requirement {

   private RefTaskClassKey iTaskClass;
   private RefTaskSubclassKey iTaskSubclass;
   private InventoryKey iInventory;
   private TaskTaskKey iDefinition;
   private RefEventStatusKey iStatus;
   private Date iActualStartDate;
   private Date iActualEndDate;
   private Date iScheduledStartDate;
   private Date iScheduledEndDate;
   private TaskKey iPreviousTask;
   private TaskKey iNextTask;
   private TaskKey iParentTask;
   private FaultKey iRecurrentSource;
   private String iBarcode;
   private Boolean iOrphaned;
   private String iDescription;
   private RefSchedPriorityKey iSchedulingPriority;
   private FncAccountKey iIssueToAccount;
   private Boolean iSoftDeadline;
   private Date iPlanByDate;
   private String iName;
   private String iCode;
   private RefTaskPriorityKey iPriority;

   private Set<TaskKey> iJobCards = new HashSet<>();
   private Set<TaskKey> iSubTasks = new HashSet<>();
   private Set<UsageSnapshot> iUsages = new HashSet<>();
   private List<DomainConfiguration<Deadline>> iDeadlineConfigurations = new ArrayList<>();
   private Map<RefImpactKey, String> iImpacts = new HashMap<>();
   private List<IetmTopicKey> iTechnicalReferences = new ArrayList<>();
   private List<IetmTopicKey> iAttachments = new ArrayList<>();
   private List<DomainConfiguration<InstallationRecord>> iInstallConfigurations = new ArrayList<>();
   private List<DomainConfiguration<PartRequirement>> iPartRequirementConfigurations =
         new ArrayList<>();
   private List<DomainConfiguration<Labour>> iLabourConfigs = new ArrayList<>();

   // The fault to which this requirement has a direct association.
   // (implementation detail for clarity: used to populate sched_stask.fault_<key> columns )
   private FaultKey iAssociatedFault;

   // The fault to which this requirement has a FAULTREL relationship with.
   // (implementation detail for clarity: used to add a row to the evt_event_rel table)
   private FaultKey iFaultRelatedFault;


   Requirement() {

   }


   public List<IetmTopicKey> getAttachments() {
      return iAttachments;
   }


   public void addAttachment( IetmTopicKey aAttached ) {
      iAttachments.add( aAttached );
   }


   public List<IetmTopicKey> getTechnicalReferences() {
      return iTechnicalReferences;
   }


   public void addTechnicalReference( IetmTopicKey aTechRef ) {
      iTechnicalReferences.add( aTechRef );
   }


   public List<DomainConfiguration<PartRequirement>> getPartRequirementConfigurations() {
      return iPartRequirementConfigurations;
   }


   public void
         addPartRequirement( DomainConfiguration<PartRequirement> aPartRequirementConfiguration ) {
      iPartRequirementConfigurations.add( aPartRequirementConfiguration );
   }


   public Map<RefImpactKey, String> getImpacts() {
      return iImpacts;
   }


   public void addImpact( RefImpactKey aImpactKey, String aDescription ) {

      iImpacts.put( aImpactKey, aDescription );
   }


   public void setTaskClass( RefTaskClassKey aTaskClass ) {
      iTaskClass = aTaskClass;
   }


   public RefTaskClassKey getTaskClass() {
      return iTaskClass;
   }


   public void setTaskSubclass( RefTaskSubclassKey aSubclass ) {
      iTaskSubclass = aSubclass;
   }


   public RefTaskSubclassKey getTaskSubclass() {
      return iTaskSubclass;
   }


   public void setInventory( InventoryKey aMainInventory ) {
      iInventory = aMainInventory;
   }


   public InventoryKey getInventory() {
      return iInventory;
   }


   public void setDefinition( TaskTaskKey aDefinition ) {
      iDefinition = aDefinition;
   }


   public TaskTaskKey getDefinition() {
      return iDefinition;
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public String getName() {
      return iName;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public String getCode() {
      return iCode;
   }


   public void setPriority( RefTaskPriorityKey taskPriority ) {
      iPriority = taskPriority;
   }


   public RefTaskPriorityKey getPriority() {
      return iPriority;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public void setActualStartDate( Date aDate ) {
      iActualStartDate = aDate;
   }


   public Date getActualStartDate() {
      return iActualStartDate;
   }


   public void setActualEndDate( Date aDate ) {
      iActualEndDate = aDate;
   }


   public Date getActualEndDate() {
      return iActualEndDate;
   }


   public void setScheduledStartDate( Date aDate ) {
      iScheduledStartDate = aDate;
   }


   public Date getScheduledStartDate() {
      return iScheduledStartDate;
   }


   public void setScheduledEndDate( Date aDate ) {
      iScheduledEndDate = aDate;
   }


   public Date getScheduledEndDate() {
      return iScheduledEndDate;
   }


   public void setPreviousTask( TaskKey aPreviousTask ) {
      iPreviousTask = aPreviousTask;
   }


   public TaskKey getPreviousTask() {
      return iPreviousTask;
   }


   public void setNextTask( TaskKey aNextTask ) {
      iNextTask = aNextTask;
   }


   public TaskKey getNextTask() {
      return iNextTask;
   }


   public void setParentTask( TaskKey aParentTask ) {
      iParentTask = aParentTask;
   }


   public TaskKey getParentTask() {
      return iParentTask;
   }


   public void setBarcode( String aBarcode ) {
      iBarcode = aBarcode;
   }


   public String getBarcode() {
      return iBarcode;
   }


   public void setOrphaned( boolean aOrphaned ) {
      iOrphaned = aOrphaned;
   }


   public Optional<Boolean> isOrphaned() {
      return Optional.ofNullable( iOrphaned );
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   public String getDescription() {
      return iDescription;
   }


   public void setSchedulingPriority( RefSchedPriorityKey aSchedulingPriority ) {
      iSchedulingPriority = aSchedulingPriority;
   }


   public RefSchedPriorityKey getSchedulingPriority() {
      return iSchedulingPriority;
   }


   public void setIssueToAccount( FncAccountKey aIssueToAccount ) {
      iIssueToAccount = aIssueToAccount;
   }


   public FncAccountKey getIssueToAccount() {
      return iIssueToAccount;
   }


   public void setSoftDeadline( boolean aSoftDeadline ) {
      iSoftDeadline = aSoftDeadline;
   }


   public Optional<Boolean> isSoftDeadline() {
      return Optional.ofNullable( iSoftDeadline );
   }


   public void setPlanByDate( Date aPlanByDate ) {
      iPlanByDate = aPlanByDate;

   }


   public Date getPlanByDate() {
      return iPlanByDate;
   }


   public void addJobCard( TaskKey aJobCard ) {
      iJobCards.add( aJobCard );
   }


   public Set<TaskKey> getJobCards() {
      return iJobCards;
   }


   public void addUsage( UsageSnapshot aUsage ) {
      iUsages.add( aUsage );
   }


   public Set<UsageSnapshot> getUsages() {
      return iUsages;
   }


   public void addSubTask( TaskKey aSubTask ) {
      iSubTasks.add( aSubTask );

   }


   public Set<TaskKey> getSubTasks() {
      return iSubTasks;
   }


   public void addDeadline( DomainConfiguration<Deadline> aDeadlineConfiguration ) {
      iDeadlineConfigurations.add( aDeadlineConfiguration );
   }


   public List<DomainConfiguration<Deadline>> getDeadlines() {
      return iDeadlineConfigurations;
   }


   public List<DomainConfiguration<InstallationRecord>> getInstallConfigurations() {
      return iInstallConfigurations;
   }


   public void addInstall( DomainConfiguration<InstallationRecord> aInstallConfiguration ) {
      iInstallConfigurations.add( aInstallConfiguration );
   }


   /**
    * Helper method for adding calendar based deadline.
    */
   public void addCalendarDeadline( final DataTypeKey aUsageType,
         final RefSchedFromKey aScheduledFrom, final BigDecimal aStartTsn,
         final BigDecimal aInterval, final Date aDueDate ) {

      addCalendarDeadline( aUsageType, aScheduledFrom, aStartTsn, aInterval, aDueDate, null, null,
            null );
   }


   /**
    * Helper method for adding calendar based deadline.
    */
   public void addCalendarDeadline( final DataTypeKey aUsageType,
         final RefSchedFromKey aScheduledFrom, final BigDecimal aStartTsn,
         final BigDecimal aInterval, final Date aDueDate, final Date aStartDate ) {

      addCalendarDeadline( aUsageType, aScheduledFrom, aStartTsn, aInterval, aDueDate, aStartDate,
            null, null, null );
   }


   /**
    * Helper method for adding calendar based deadline.
    */
   public void addCalendarDeadline( final DataTypeKey aUsageType,
         final RefSchedFromKey aScheduledFrom, final BigDecimal aStartTsn,
         final BigDecimal aInterval, final Date aDueDate, final BigDecimal aScheduleToPlanLow,
         final BigDecimal aScheduleToPlanHigh, final BigDecimal aUsageRemaining ) {

      iDeadlineConfigurations.add( new DomainConfiguration<Deadline>() {

         @Override
         public void configure( Deadline aDeadline ) {
            aDeadline.setUsageType( aUsageType );
            aDeadline.setScheduledFrom( aScheduledFrom );
            aDeadline.setStartTsn( aStartTsn );
            aDeadline.setInterval( aInterval );
            aDeadline.setDueDate( aDueDate );
            aDeadline.setUsageRemaining( aUsageRemaining );
            aDeadline.setScheduleToPlanWindow( aScheduleToPlanLow, aScheduleToPlanHigh );
         }
      } );
   }


   public void addCalendarDeadline( final DataTypeKey aUsageType,
         final RefSchedFromKey aScheduledFrom, final BigDecimal aStartTsn,
         final BigDecimal aInterval, final Date aDueDate, final Date aStartDate,
         final BigDecimal aScheduleToPlanLow, final BigDecimal aScheduleToPlanHigh,
         final BigDecimal aUsageRemaining ) {

      iDeadlineConfigurations.add( new DomainConfiguration<Deadline>() {

         @Override
         public void configure( Deadline aDeadline ) {
            aDeadline.setUsageType( aUsageType );
            aDeadline.setScheduledFrom( aScheduledFrom );
            aDeadline.setStartTsn( aStartTsn );
            aDeadline.setInterval( aInterval );
            aDeadline.setDueDate( aDueDate );
            aDeadline.setStartDate( aStartDate );
            aDeadline.setUsageRemaining( aUsageRemaining );
            aDeadline.setScheduleToPlanWindow( aScheduleToPlanLow, aScheduleToPlanHigh );
         }
      } );
   }


   /**
    * Helper method for adding calendar based deadline.
    */
   public void addCalendarDeadline( DataTypeKey aUsageType, BigDecimal aInterval, Date aDueDate ) {
      addCalendarDeadline( aUsageType, null, null, aInterval, aDueDate );
   }


   public void addUsageDeadline( final DataTypeKey aUsageType, final RefSchedFromKey aScheduledFrom,
         final BigDecimal aStartTsn, final BigDecimal aInterval, final BigDecimal aDueValue ) {

      iDeadlineConfigurations.add( new DomainConfiguration<Deadline>() {

         @Override
         public void configure( Deadline aDeadline ) {
            aDeadline.setUsageType( aUsageType );
            aDeadline.setScheduledFrom( aScheduledFrom );
            aDeadline.setStartTsn( aStartTsn );
            aDeadline.setInterval( aInterval );
            aDeadline.setDueValue( aDueValue );
         }
      } );
   }


   public void addLabour( DomainConfiguration<Labour> aLabourConfiguration ) {
      iLabourConfigs.add( aLabourConfiguration );
   }


   /**
    * Add a labour without configuration. Use when you need the requirement to have a labour
    * requirement but do not care about the details of that labour.
    */
   public void addLabour() {
      addLabour( new DomainConfiguration<Labour>() {

         @Override
         public void configure( Labour aBuilder ) {
            // Add a labour without any configuration.
         }
      } );
   }


   public List<DomainConfiguration<Labour>> getLabourConfigurations() {
      return iLabourConfigs;
   }


   /**
    *
    * Sets the fault to which this requirement has a direct association.
    *
    * (implementation detail for clarity: used to populate sched_stask.fault_<key> columns )
    *
    * @param aFault
    */
   public void setAssociatedFault( FaultKey aFault ) {
      iAssociatedFault = aFault;
   }


   public FaultKey getAssociatedFault() {
      return iAssociatedFault;
   }


   /**
    *
    * Sets the fault to which this requirement has a FAULTREL relationship with.
    *
    * (implementation detail for clarity: used to add a row to the evt_event_rel table)
    *
    * @param aFault
    */
   public void setRelatedFault( FaultKey aFault ) {
      iFaultRelatedFault = aFault;
   }


   public FaultKey getRelatedFault() {
      return iFaultRelatedFault;
   }


   public FaultKey getRecurrentSource() {
      return iRecurrentSource;
   }


   public void setRecurrentSource( FaultKey aRecurrentSource ) {
      iRecurrentSource = aRecurrentSource;
   }


   /**
    * Helper method for adding usage based deadline.
    */
   public void addUsageDeadline( DataTypeKey aUsageType, BigDecimal aInterval,
         BigDecimal aDueValue ) {
      addUsageDeadline( aUsageType, null, null, aInterval, aDueValue );
   }

}
