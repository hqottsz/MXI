package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvCondKey;


public class TrackedInventory {

   private Map<DataTypeKey, BigDecimal> iUsage = new HashMap<>();
   private Boolean iAllowSynchronization;
   private InventoryKey iParent;
   private String iBarcode;
   private String iSerialNumber;
   private String iAssemblyCode;
   private String iConfigSlotCode;
   private String iPositionCode;
   private PartNoKey iPartNumber;
   private PartGroupKey iPartGroup;
   private LocationKey iLocation;
   private RefInvCondKey iCondition;
   private OwnerKey iOwner;
   private String iDescription;
   private Date iManufactureDate;
   private boolean iIssued;
   private AssemblyKey iAssembly;
   private AssemblyKey iOriginalAssembly;
   private boolean iComplete;
   private ConfigSlotPositionKey iPosition;
   private Integer iAssociation;
   private boolean iLocked;

   private Set<InstallationRecord> iInstallationRecords = new HashSet<InstallationRecord>();
   private Set<RemovalRecord> iRemovalRecords = new HashSet<RemovalRecord>();

   private Set<InventoryKey> iSerializedKeys = new HashSet<InventoryKey>();
   private Set<InventoryKey> iTrackedKeys = new HashSet<InventoryKey>();
   private Set<DomainConfiguration<TrackedInventory>> iTrackedConfigs =
         new HashSet<DomainConfiguration<TrackedInventory>>();


   public void
         addInstallationRecord( DomainConfiguration<InstallationRecord> aDomainConfiguration ) {
      InstallationRecord lInstallationRecord = new InstallationRecord();
      aDomainConfiguration.configure( lInstallationRecord );
      iInstallationRecords.add( lInstallationRecord );
   }


   public Set<InstallationRecord> getInstallationRecords() {
      return iInstallationRecords;
   }


   public void addRemovalRecord( DomainConfiguration<RemovalRecord> aDomainConfiguration ) {
      RemovalRecord lRemovalRecord = new RemovalRecord();
      aDomainConfiguration.configure( lRemovalRecord );
      iRemovalRecords.add( lRemovalRecord );
   }


   public Set<RemovalRecord> getRemovalRecords() {
      return iRemovalRecords;
   }


   public boolean isIssued() {
      return iIssued;
   }


   public void setIssued( boolean aIssued ) {
      iIssued = aIssued;
   }


   public boolean isComplete() {
      return iComplete;
   }


   public void setComplete( boolean aComplete ) {
      iComplete = aComplete;
   }


   public void setAllowSynchronization( boolean aAllowSync ) {
      iAllowSynchronization = aAllowSync;
   }


   public Boolean isAllowingSynchronization() {
      return iAllowSynchronization;
   }


   public String getDescription() {
      return iDescription;
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   public void setBarcode( String aBarcode ) {
      iBarcode = aBarcode;
   }


   public String getBarcode() {
      return iBarcode;
   }


   public void setCondition( RefInvCondKey aCondition ) {
      iCondition = aCondition;
   }


   public RefInvCondKey getCondition() {
      return iCondition;
   }


   public void setLocation( LocationKey aLocation ) {
      iLocation = aLocation;
   }


   public LocationKey getLocation() {
      return iLocation;
   }


   public void setOwner( OwnerKey aOwner ) {
      iOwner = aOwner;
   }


   public OwnerKey getOwner() {
      return iOwner;
   }


   public void setParent( InventoryKey aParent ) {
      iParent = aParent;
   }


   public InventoryKey getParent() {
      return iParent;
   }


   public void setPartNumber( PartNoKey aPartNumber ) {
      iPartNumber = aPartNumber;
   }


   public PartNoKey getPartNumber() {
      return iPartNumber;
   }


   public void addSerialized( InventoryKey aSerializedInventoryKey ) {
      iSerializedKeys.add( aSerializedInventoryKey );
   }


   public Set<InventoryKey> getSerializedKeys() {
      return iSerializedKeys;
   }


   public String getSerialNumber() {
      return iSerialNumber;
   }


   public void setSerialNumber( String aSerialNoOem ) {
      iSerialNumber = aSerialNoOem;
   }


   public void addTracked( InventoryKey aTrackedInventoryKey ) {
      iTrackedKeys.add( aTrackedInventoryKey );
   }


   public Set<InventoryKey> getTrackedKeys() {
      return iTrackedKeys;
   }


   public void addTracked( DomainConfiguration<TrackedInventory> aTrackedInvConfiguration ) {
      iTrackedConfigs.add( aTrackedInvConfiguration );
   }


   public Set<DomainConfiguration<TrackedInventory>> getTrackedConfigurations() {
      return iTrackedConfigs;
   }


   public String getConfigSlotCode() {
      return iConfigSlotCode;
   }


   public String getPositionCode() {
      return iPositionCode;
   }


   public String getAssemblyCode() {
      return iAssemblyCode;
   }


   public void setLastKnownConfigSlot( String aAssemblyCode, String aConfigSlotCode,
         String aPositionCode ) {
      iAssemblyCode = aAssemblyCode;
      iConfigSlotCode = aConfigSlotCode;
      iPositionCode = aPositionCode;
   }


   public Map<DataTypeKey, BigDecimal> getUsage() {
      return iUsage;
   }


   public void setUsage( Map<DataTypeKey, BigDecimal> aUsage ) {
      iUsage = aUsage;
   }


   public void addUsage( DataTypeKey aType, BigDecimal aUsage ) {
      iUsage.put( aType, aUsage );
   }


   public PartGroupKey getPartGroup() {
      return iPartGroup;
   }


   public void setPartGroup( PartGroupKey aPartGroup ) {
      iPartGroup = aPartGroup;
   }


   public Date getManufactureDate() {
      return iManufactureDate;
   }


   public void setManufactureDate( Date aManufactureDate ) {
      iManufactureDate = aManufactureDate;
   }


   public void setAssembly( AssemblyKey aAssembly ) {
      iAssembly = aAssembly;
   }


   public AssemblyKey getAssembly() {
      return iAssembly;
   }


   public void setOriginalAssembly( AssemblyKey aOriginalAssembly ) {
      iOriginalAssembly = aOriginalAssembly;
   }


   public AssemblyKey getOriginalAssembly() {
      return iOriginalAssembly;
   }


   public ConfigSlotPositionKey getPosition() {
      return iPosition;
   }


   public void setPosition( ConfigSlotPositionKey aPosition ) {
      iPosition = aPosition;
   }


   public Optional<Integer> getAssociation() {
      return Optional.ofNullable( iAssociation );
   }


   public void setAssociation( Integer aAssociation ) {
      iAssociation = aAssociation;
   }


   public boolean isLocked() {
      return iLocked;
   }


   public void setLocked( boolean iLocked ) {
      this.iLocked = iLocked;

   }

}
