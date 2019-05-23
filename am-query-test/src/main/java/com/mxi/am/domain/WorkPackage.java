package com.mxi.am.domain;

import static com.mxi.mx.core.key.RefLabourSkillKey.ENG;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;


public class WorkPackage {

   private String iName;
   private String iBarcode;
   private InventoryKey iAircraft;
   private RefEventStatusKey iStatus;
   private LocationKey iLocation;
   private Boolean iRequestParts;
   private Date iActualStartDate;
   private Date iActualEndDate;
   private Date iScheduledStartDate;
   private Date iScheduledEndDate;
   private boolean iAutoComplete = false;
   private String iReleaseNumber;
   private String iReleaseRemarks;
   private String iWorkOrderDescription;

   private List<TaskKey> iTasks = new ArrayList<>();
   private List<UsageSnapshot> iUsageSnapshots = new ArrayList<>();
   private List<InventoryKey> iSubAssemblies = new ArrayList<>();
   private List<DepartmentKey> iCrews = new ArrayList<>();
   private List<TaskKey> iWorkScopeTasks = new ArrayList<>();
   private List<RefWorkTypeKey> iWorkTypes = new ArrayList<>();
   private List<TaskKey> iCollectedTasks = new ArrayList<>();
   private List<DomainConfiguration<SignOffRequirement>> iSignOffRequirementConfigs =
         new ArrayList<>();


   WorkPackage() {

   }


   public void setAircraft( InventoryKey aInventory ) {
      iAircraft = aInventory;
   }


   public InventoryKey getAircraft() {
      return iAircraft;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public String getName() {
      return iName;
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public void setBarcode( String aBarcode ) {
      iBarcode = aBarcode;
   }


   public String getBarcode() {
      return iBarcode;
   }


   public void setLocation( LocationKey aLocation ) {
      iLocation = aLocation;
   }


   public LocationKey getLocation() {
      return iLocation;
   }


   public Boolean isRequestParts() {
      return iRequestParts;
   }


   public void setRequestedParts( boolean aRequestParts ) {
      iRequestParts = aRequestParts;
   }


   /**
    * Returns the value of the iAutoComplete property.
    *
    * @return the value of the iAutoComplete property
    */
   public boolean isAutoComplete() {
      return iAutoComplete;
   }


   public void setAutoComplete( boolean aAutoComplete ) {
      this.iAutoComplete = aAutoComplete;
   }


   public void hasRequestedParts() {
      iRequestParts = true;
   }


   public Date getActualStartDate() {
      return iActualStartDate;
   }


   public void setActualStartDate( Date aActualStartDate ) {
      iActualStartDate = aActualStartDate;
   }


   public Date getActualEndDate() {
      return iActualEndDate;
   }


   public void setActualEndDate( Date aActualEndDate ) {
      iActualEndDate = aActualEndDate;
   }


   public Date getScheduledStartDate() {
      return iScheduledStartDate;
   }


   public void setScheduledStartDate( Date aScheduledStartDate ) {
      iScheduledStartDate = aScheduledStartDate;
   }


   public Date getScheduledEndDate() {
      return iScheduledEndDate;
   }


   public void setScheduledEndDate( Date aScheduledEndDate ) {
      iScheduledEndDate = aScheduledEndDate;
   }


   public String getReleaseNumber() {
      return iReleaseNumber;
   }


   public void setReleaseNumber( String aReleaseNumber ) {
      iReleaseNumber = aReleaseNumber;
   }


   public String getReleaseRemarks() {
      return iReleaseRemarks;
   }


   public void setReleaseRemarks( String aReleaseRemarks ) {
      iReleaseRemarks = aReleaseRemarks;
   }


   public String getWorkOrderDescription() {
      return iWorkOrderDescription;
   }


   public void setWorkOrderDescription( String aWorkOrderDescription ) {
      iWorkOrderDescription = aWorkOrderDescription;
   }


   public void addTask( TaskKey aTaskKey ) {
      iTasks.add( aTaskKey );
   }


   public List<TaskKey> getTasks() {
      return iTasks;
   }


   public void addUsageSnapshot( UsageSnapshot aUsageSnapshot ) {
      iUsageSnapshots.add( aUsageSnapshot );
   }


   /**
    *
    * Helper method for adding a usage snapshot with just a TSN.
    *
    * @param aUsageSnapshot
    */
   public void addUsageSnapshot( InventoryKey aInventory, DataTypeKey aDataType, BigDecimal aTSN ) {
      iUsageSnapshots.add( new UsageSnapshot( aInventory, aDataType, aTSN ) );
   }


   public List<UsageSnapshot> getUsageSnapshots() {
      return iUsageSnapshots;
   }


   public void addSubAssembly( InventoryKey aSubAssembly ) {
      iSubAssemblies.add( aSubAssembly );
   }


   public List<InventoryKey> getSubAssemblies() {
      return iSubAssemblies;
   }


   public void addCrew( DepartmentKey aDepartment ) {
      iCrews.add( aDepartment );
   }


   public List<DepartmentKey> getCrews() {
      return iCrews;
   }


   public List<TaskKey> getWorkScopeTasks() {
      return iWorkScopeTasks;
   }


   public void addWorkScopeTask( TaskKey aTask ) {
      addWorkScopeTask( aTask, false );
   }


   public void addWorkScopeTask( TaskKey aTask, boolean aCollected ) {
      iWorkScopeTasks.add( aTask );
      if ( aCollected ) {
         iCollectedTasks.add( aTask );
      }
   }


   public List<TaskKey> getCollectedTasks() {
      return iCollectedTasks;
   }


   public List<RefWorkTypeKey> getWorkTypes() {
      return iWorkTypes;
   }


   public void addWorkType( RefWorkTypeKey aWorkType ) {
      iWorkTypes.add( aWorkType );
   }


   public List<DomainConfiguration<SignOffRequirement>> getSignOffRequirementConfigs() {
      return iSignOffRequirementConfigs;
   }


   public void
         addSignOffRequirement( DomainConfiguration<SignOffRequirement> aSignOffRequirement ) {
      iSignOffRequirementConfigs.add( aSignOffRequirement );
   }


   /**
    * Helper method when the details of the sign-off requirement are not relevant.
    */
   public void addSignOffRequirement() {
      iSignOffRequirementConfigs.add( new DomainConfiguration<SignOffRequirement>() {

         @Override
         public void configure( SignOffRequirement builder ) {
            builder.setSkill( ENG );
         }
      } );
   }

}
