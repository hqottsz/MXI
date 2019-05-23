package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class BlockChainDefinition {

   private String iName;
   private Boolean iRecurring;
   private RefTaskDefinitionStatusKey iStatus;
   private ConfigSlotKey iConfigurationSlot;
   private Boolean iOnCondition;
   private Integer iRevisionNumber;
   private BigDecimal iMinimumForecastRange;
   private Boolean iApprovedForFleet;
   private Date iEffectiveDate;
   private Boolean iUnique;
   private ScheduleFromOption iScheduleFromOption;
   private List<RequirementInfo> iRequirements = new ArrayList<>();
   private List<OneTimeSchedulingRule> iOneTimeSchedulingRules = new ArrayList<>();
   private List<RecurringSchedulingRule> iRecurringSchedulingRules = new ArrayList<>();

   // Using a list to preserve order of blocks when added.
   private List<BlockInfo> iBlocks = new ArrayList<BlockInfo>();


   public static enum ScheduleFromOption {
      MANUFACTURED_DATE, RECEIVED_DATE, EFFECTIVE_DATE
   };


   public void setName( String aName ) {
      iName = aName;
   }


   public String getName() {
      return iName;
   }


   public void setRecurring( boolean aRecurring ) {
      iRecurring = aRecurring;
   }


   public Boolean isRecurring() {
      return iRecurring;
   }


   public void setStatus( RefTaskDefinitionStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public RefTaskDefinitionStatusKey getStatus() {
      return iStatus;
   }


   public void setConfigurationSlot( ConfigSlotKey aConfigurationSlot ) {
      iConfigurationSlot = aConfigurationSlot;
   }


   public ConfigSlotKey getConfigurationSlot() {
      return iConfigurationSlot;
   }


   public void setOnCondition( boolean aOnCondition ) {
      iOnCondition = aOnCondition;
   }


   public Boolean isOnCondition() {
      return iOnCondition;
   }


   public void setRevisionNumber( Integer aRevisionNumber ) {
      iRevisionNumber = aRevisionNumber;
   }


   public Integer getRevisionNumber() {
      return iRevisionNumber;
   }


   public void setMinimumForecastRange( BigDecimal aMinimumForecastRange ) {
      iMinimumForecastRange = aMinimumForecastRange;
   }


   public void setMinimumForecastRange( int aMinimumForecastRange ) {
      setMinimumForecastRange( BigDecimal.valueOf( aMinimumForecastRange ) );
   }


   public BigDecimal getMinimumForecastRange() {
      return iMinimumForecastRange;
   }


   public void setApprovedForFleet( boolean aApprovedForFleet ) {
      iApprovedForFleet = aApprovedForFleet;
   }


   public Boolean isApprovedForFleet() {
      return iApprovedForFleet;
   }


   public Date getEffectiveDate() {
      return iEffectiveDate;
   }


   public void setEffectiveDate( Date aEffectiveDate ) {
      iEffectiveDate = aEffectiveDate;
   }


   public void setUnique( boolean aUnique ) {
      iUnique = aUnique;
   }


   public Boolean isUnique() {
      return iUnique;
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


   public ScheduleFromOption getScheduleFromOption() {
      return iScheduleFromOption;
   }


   public void addBlock( Integer aNumber, String aCode, String aName ) {
      iBlocks.add( new BlockInfo( aNumber, aCode, aName ) );
   }


   public void addBlock( Integer aNumber, String aCode, String aName,
         TaskTaskKey aPreviousRevision ) {
      iBlocks.add( new BlockInfo( aNumber, aCode, aName, aPreviousRevision ) );
   }


   public List<BlockInfo> getBlocks() {
      return iBlocks;
   }


   public void addRequirement( TaskTaskKey aRequirementDefinition, int aStartingBlock,
         int aInterval ) {
      iRequirements.add( new RequirementInfo( aRequirementDefinition, aStartingBlock, aInterval ) );
   }


   public List<RequirementInfo> getRequirements() {
      return iRequirements;
   }


   public void addOneTimeSchedulingRule( DataTypeKey aUsageParameter, BigDecimal aThreshold ) {
      iOneTimeSchedulingRules.add( new OneTimeSchedulingRule( aUsageParameter, aThreshold ) );
   }


   public void addOneTimeSchedulingRule( DataTypeKey aUsageParameter, int aThreshold ) {
      addOneTimeSchedulingRule( aUsageParameter, BigDecimal.valueOf( aThreshold ) );
   }


   public List<OneTimeSchedulingRule> getOneTimeSchedulingRules() {
      return iOneTimeSchedulingRules;
   }


   public void addRecurringSchedulingRule( DataTypeKey aUsageParameter, BigDecimal aThreshold ) {
      iRecurringSchedulingRules.add( new RecurringSchedulingRule( aUsageParameter, aThreshold ) );
   }


   public void addRecurringSchedulingRule( DataTypeKey aUsageParameter, int aThreshold ) {
      addRecurringSchedulingRule( aUsageParameter, BigDecimal.valueOf( aThreshold ) );
   }


   public List<RecurringSchedulingRule> getRecurringSchedulingRules() {
      return iRecurringSchedulingRules;
   }


   public class BlockInfo {

      private Integer iNumber;
      private String iCode;
      private String iName;
      private TaskTaskKey iPreviousRevision;


      public BlockInfo(Integer aNumber, String aCode, String aName) {
         this( aNumber, aCode, aName, null );
      }


      public BlockInfo(Integer aNumber, String aCode, String aName, TaskTaskKey aPreviousRevision) {
         iNumber = aNumber;
         iCode = aCode;
         iName = aName;
         iPreviousRevision = aPreviousRevision;
      }


      public Integer getNumber() {
         return iNumber;
      }


      public String getCode() {
         return iCode;
      }


      public String getName() {
         return iName;
      }


      public TaskTaskKey getPreviousRevision() {
         return iPreviousRevision;
      }

   }

   public class RequirementInfo {

      private TaskTaskKey iRequirementDefinition;
      private int iInterval;
      private int iStartingBlock;


      public RequirementInfo(TaskTaskKey aRequirementDefinition, int aStartingBlock,
            int aInterval) {
         iRequirementDefinition = aRequirementDefinition;
         iStartingBlock = aStartingBlock;
         iInterval = aInterval;
      }


      public TaskTaskKey getRequirementDefinition() {
         return iRequirementDefinition;
      }


      public int getInterval() {
         return iInterval;
      }


      public int getStartingBlock() {
         return iStartingBlock;
      };

   }

}
