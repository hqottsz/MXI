package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;


public class ComponentWorkPackage {

   private String iName;
   private String iBarcode;
   private InventoryKey iInventory;
   private RefEventStatusKey iStatus;
   private LocationKey iLocation;
   private Boolean iRequestParts;
   private Set<TaskKey> iAssignedTasks = new HashSet<TaskKey>();
   private Date iActualStartDate;
   private Date iActualEndDate;
   private Date iScheduledStartDate;
   private Date iScheduledEndDate;
   private List<UsageSnapshot> iUsageSnapshots = new ArrayList<UsageSnapshot>();
   private List<InventoryKey> iSubAssemblies = new ArrayList<InventoryKey>();


   public void addUsageSnapshot( UsageSnapshot aUsageSnapshot ) {
      iUsageSnapshots.add( aUsageSnapshot );
   }


   public void addUsageSnapshot( InventoryKey aInventory, DataTypeKey aDataType, BigDecimal aTSN ) {
      iUsageSnapshots.add( new UsageSnapshot( aInventory, aDataType, aTSN ) );
   }


   public List<UsageSnapshot> getUsageSnapshots() {
      return iUsageSnapshots;
   }


   public void setInventory( InventoryKey aInventory ) {
      iInventory = aInventory;
   }


   public InventoryKey getInventory() {
      return iInventory;
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


   public void atLocation( LocationKey aLocation ) {
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


   public void hasRequestedParts() {
      iRequestParts = true;
   }


   public void assignTask( TaskKey aTaskKey ) {
      iAssignedTasks.add( aTaskKey );
   }


   public Set<TaskKey> getAssignedTasks() {
      return iAssignedTasks;
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


   public List<InventoryKey> getSubAssemblies() {
      return iSubAssemblies;
   }


   public void addSubAssembly( InventoryKey aSubAssembly ) {
      iSubAssemblies.add( aSubAssembly );
   }


   public Date getScheduledEndDate() {
      return iScheduledEndDate;
   }


   public void setScheduledEndDate( Date scheduledEndDate ) {
      this.iScheduledEndDate = scheduledEndDate;
   }


   public Date getScheduledStartDate() {
      return iScheduledStartDate;
   }


   public void setScheduledStartDate( Date scheduledStartDate ) {
      this.iScheduledStartDate = scheduledStartDate;
   }
}
