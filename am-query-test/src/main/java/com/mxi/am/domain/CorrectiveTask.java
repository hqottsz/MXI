package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;


public class CorrectiveTask {

   private String iBarcode;
   private InventoryKey iInventory;
   private Date iStartDate;
   private Date iEndDate;
   private RefEventStatusKey iStatus;
   private FaultKey iFaultKey;
   private Boolean iHistorical;
   private List<TaskKey> iSubtasks = new ArrayList<>();
   private Map<DataTypeKey, BigDecimal> iUsagesAtCompletion = new HashMap<>();
   private List<DomainConfiguration<Deadline>> iDeadlineConfigurations = new ArrayList<>();
   private List<DomainConfiguration<Labour>> iLabourConfigs = new ArrayList<>();
   private String iActionDescription = "";
   private String name;
   private List<String> iSteps = new ArrayList<>();


   public String getBarcode() {
      return iBarcode;
   }


   public void setBarcode( String aBarcode ) {
      iBarcode = aBarcode;
   }


   public InventoryKey getInventory() {
      return iInventory;
   }


   public void setInventory( InventoryKey aInventory ) {
      iInventory = aInventory;
   }


   public Date getStartDate() {
      return iStartDate;
   }


   public void setStartDate( Date aStartDate ) {
      iStartDate = aStartDate;
   }


   public Date getEndDate() {
      return iEndDate;
   }


   public void setEndDate( Date aEndDate ) {
      iEndDate = aEndDate;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public void setStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public void addSubtask( TaskKey aSubtask ) {
      iSubtasks.add( aSubtask );
   }


   public List<TaskKey> getSubtasks() {
      return iSubtasks;
   }


   public Map<DataTypeKey, BigDecimal> getUsagesAtCompletion() {
      return iUsagesAtCompletion;
   }


   public void addUsageAtCompletion( DataTypeKey aType, BigDecimal aUsage ) {
      iUsagesAtCompletion.put( aType, aUsage );
   }


   public void addDeadline( DomainConfiguration<Deadline> aDeadlineConfiguration ) {
      iDeadlineConfigurations.add( aDeadlineConfiguration );
   }


   public List<DomainConfiguration<Deadline>> getDeadlines() {
      return iDeadlineConfigurations;
   }


   public FaultKey getFaultKey() {
      return iFaultKey;
   }


   public void setFaultKey( FaultKey aFaultKey ) {
      iFaultKey = aFaultKey;
   }


   public Boolean getHistoricalBool() {
      return iHistorical;
   }


   public void setHistoricalBool( Boolean aHistoricalBool ) {
      iHistorical = aHistoricalBool;
   }


   public void addLabour( DomainConfiguration<Labour> aLabourConfiguration ) {
      iLabourConfigs.add( aLabourConfiguration );
   }


   public List<DomainConfiguration<Labour>> getLabourConfigurations() {
      return iLabourConfigs;
   }


   public String getActionDescription() {
      return iActionDescription;
   }


   public void setActionDescription( String aActionDescription ) {
      this.iActionDescription = aActionDescription;
   }


   public String getName() {
      return name;
   }


   public void setName( String name ) {
      this.name = name;
   }


   public void addStep( String aStep ) {
      iSteps.add( aStep );
   }


   public List<String> getSteps() {
      return iSteps;
   }
}
