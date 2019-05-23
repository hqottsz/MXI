package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvCondKey;


public class Engine {

   private Map<DataTypeKey, BigDecimal> iUsage = new HashMap<>();
   private AssemblyKey iOriginalAssembly;
   private Boolean iAllowSynchronization;
   private String iSerialNumber;
   private PartGroupKey iPartGroup;
   private ConfigSlotPositionKey iPosition;
   private LocationKey iLocation;
   private OwnerKey iOwner;
   private String iBarcode;
   private InventoryKey iParent;
   private String iDescription;
   private String iApplicabilityCode;
   private AssemblyKey iAssembly;
   private RefInvCondKey iCondition;
   private boolean iComplete;

   private PartNoKey iPartNumber;
   private DomainConfiguration<Part> iPartNumberConfiguration;

   private Set<InstallationRecord> iInstallationRecords = new HashSet<InstallationRecord>();
   private Set<RemovalRecord> iRemovalRecords = new HashSet<RemovalRecord>();

   private Set<InventoryKey> iSerializedKeys = new HashSet<InventoryKey>();
   private Set<InventoryKey> iTrackedKeys = new HashSet<InventoryKey>();

   private Set<DomainConfiguration<TrackedInventory>> iTrackedConfigs =
         new HashSet<DomainConfiguration<TrackedInventory>>();

   // Systems are not their own entities, they are attributes of the engine. Thus, can only be
   // configured as part of an engine.
   private Set<DomainConfiguration<System>> iSystemConfigs =
         new HashSet<DomainConfiguration<System>>();

   private AuthorityKey authorityKey;


   public Map<DataTypeKey, BigDecimal> getUsage() {
      return iUsage;
   }


   public void setUsage( Map<DataTypeKey, BigDecimal> aUsage ) {
      iUsage = aUsage;
   }


   public void addUsage( DataTypeKey aType, BigDecimal aUsage ) {
      iUsage.put( aType, aUsage );
   }


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


   public AssemblyKey getOriginalAssembly() {
      return iOriginalAssembly;
   }


   public void setOriginalAssembly( AssemblyKey aOriginalAssembly ) {
      iOriginalAssembly = aOriginalAssembly;
   }


   public void allowSynchronization() {
      iAllowSynchronization = true;
   }


   public void setAllowSynchronization( boolean aAllowSynchronization ) {
      iAllowSynchronization = aAllowSynchronization;
   }


   public Boolean isAllowingSynchronization() {
      return iAllowSynchronization;
   }


   public String getSerialNumber() {
      return iSerialNumber;
   }


   public void setSerialNumber( String aSerialNoOem ) {
      iSerialNumber = aSerialNoOem;
   }


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


   public LocationKey getLocation() {
      return iLocation;
   }


   public void setLocation( LocationKey aLocation ) {
      iLocation = aLocation;
   }


   public void setOwner( OwnerKey aOwner ) {
      iOwner = aOwner;
   }


   public OwnerKey getOwner() {
      return iOwner;
   }


   public void setBarcode( String aBarcode ) {
      iBarcode = aBarcode;
   }


   public String getBarcode() {
      return iBarcode;
   }


   public void setParent( InventoryKey aParent ) {
      iParent = aParent;
   }


   public InventoryKey getParent() {
      return iParent;
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   public String getDescription() {
      return iDescription;
   }


   //
   // Serialized Inventory
   //

   public void addSerialized( InventoryKey aSerializedInventoryKey ) {
      iSerializedKeys.add( aSerializedInventoryKey );
   }


   public Set<InventoryKey> getSerializedKeys() {
      return iSerializedKeys;
   }


   //
   // Tracked Inventory
   //

   public void addTracked( InventoryKey aTrackedKey ) {
      iTrackedKeys.add( aTrackedKey );
   }


   public void addTracked( DomainConfiguration<TrackedInventory> aTrackedConfiguration ) {
      iTrackedConfigs.add( aTrackedConfiguration );
   }


   public Set<InventoryKey> getTrackedKeys() {
      return iTrackedKeys;
   }


   public Set<DomainConfiguration<TrackedInventory>> getTrackedConfigurations() {
      return iTrackedConfigs;
   }


   //
   // Systems
   //

   public void addSystem( DomainConfiguration<System> aSystemConfiguration ) {
      iSystemConfigs.add( aSystemConfiguration );
   }


   public void addSystem( final String aName ) {
      DomainConfiguration<System> lSystemConfig = new DomainConfiguration<System>() {

         @Override
         public void configure( System aBuilder ) {
            aBuilder.setName( aName );
         }
      };
      iSystemConfigs.add( lSystemConfig );
   }


   public Set<DomainConfiguration<System>> getSystemConfigurations() {
      return iSystemConfigs;
   }


   public String getApplicabilityCode() {
      return iApplicabilityCode;
   }


   public void setApplicabilityCode( String aApplicabilityCode ) {
      iApplicabilityCode = aApplicabilityCode;
   }


   public AssemblyKey getAssembly() {
      return iAssembly;
   }


   public void setAssembly( AssemblyKey aAssembly ) {
      iAssembly = aAssembly;
   }


   public RefInvCondKey getCondition() {
      return iCondition;
   }


   public void setCondition( RefInvCondKey aCondition ) {
      iCondition = aCondition;
   }


   public boolean isComplete() {
      return iComplete;
   }


   public void setComplete( boolean aComplete ) {
      iComplete = aComplete;
   }


   public PartNoKey getPartNumber() {
      return iPartNumber;
   }


   public void setPartNumber( PartNoKey aPartNoKey ) {
      iPartNumber = aPartNoKey;
   }


   public DomainConfiguration<Part> getPartNumberConfiguration() {
      return iPartNumberConfiguration;
   }


   public void setPartNumber( DomainConfiguration<Part> aPartNumberConfiguration ) {
      iPartNumberConfiguration = aPartNumberConfiguration;
   }


   public AuthorityKey getAuthority() {
      return authorityKey;
   }


   public void setAuthority( AuthorityKey authorityKey ) {
      this.authorityKey = authorityKey;
   }

}
