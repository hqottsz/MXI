package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvCondKey;


public class System {

   private String iName;

   private String iSerialNumber;
   private PartNoKey iPartNumber;
   private LocationKey iLocation;
   private OwnerKey iOwner;
   private ConfigSlotPositionKey iPosition;
   private RefInvCondKey iCondition;
   private Boolean iLocked;
   private String iBarcode;

   private Map<DataTypeKey, BigDecimal> iUsage = new HashMap<>();

   private Set<InventoryKey> iSerializedKeys = new HashSet<InventoryKey>();
   private Set<InventoryKey> iTrackedKeys = new HashSet<InventoryKey>();
   private Set<DomainConfiguration<TrackedInventory>> iTrackedConfigs =
         new HashSet<DomainConfiguration<TrackedInventory>>();

   private Set<InventoryKey> iEngines = new HashSet<InventoryKey>();
   private Set<DomainConfiguration<Engine>> iEngineConfigs =
         new HashSet<DomainConfiguration<Engine>>();

   private Set<DomainConfiguration<System>> iSubSystemConfigs =
         new HashSet<DomainConfiguration<System>>();


   public void addEngine( InventoryKey aEngine ) {
      iEngines.add( aEngine );
   }


   public void addEngine( DomainConfiguration<Engine> aEngineConfiguration ) {
      iEngineConfigs.add( aEngineConfiguration );
   }


   public Set<InventoryKey> getEngineKeys() {
      return iEngines;
   }


   public Set<DomainConfiguration<Engine>> getEngineConfigurations() {
      return iEngineConfigs;
   }


   public LocationKey getLocation() {
      return iLocation;
   }


   public void setLocation( LocationKey aLocation ) {
      iLocation = aLocation;
   }


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public void setOwner( OwnerKey aOwner ) {
      iOwner = aOwner;
   }


   public OwnerKey getOwner() {
      return iOwner;
   }


   public PartNoKey getPartNumber() {
      return iPartNumber;
   }


   public void setPartNumber( PartNoKey aPartNoKey ) {
      iPartNumber = aPartNoKey;
   }


   public void addSerialized( InventoryKey aSerialized ) {
      iSerializedKeys.add( aSerialized );
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


   public void addSubSystem( DomainConfiguration<System> aSubSystemConfiguration ) {
      iSubSystemConfigs.add( aSubSystemConfiguration );
   }


   public void addSubSystem( final String aName ) {
      DomainConfiguration<System> lSubSystemConfig = new DomainConfiguration<System>() {

         @Override
         public void configure( System aBuilder ) {
            aBuilder.setName( aName );
         }
      };
      addSubSystem( lSubSystemConfig );
   }


   public void addTracked( InventoryKey aTracked ) {
      iTrackedKeys.add( aTracked );
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


   public Set<DomainConfiguration<System>> getSubSystemConfigurations() {
      return iSubSystemConfigs;
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


   public ConfigSlotPositionKey getPosition() {
      return iPosition;
   }


   public void setPosition( ConfigSlotPositionKey aPosition ) {
      iPosition = aPosition;
   }


   public void setPosition( ConfigSlotKey aConfigSlot, int aPosition ) {
      this.setPosition( new ConfigSlotPositionKey( aConfigSlot, aPosition ) );
   }


   public RefInvCondKey getCondition() {
      return iCondition;
   }


   public void setCondition( RefInvCondKey aCondition ) {
      iCondition = aCondition;
   }


   public Optional<Boolean> isLocked() {
      return Optional.ofNullable( iLocked );
   }


   public void setLocked( boolean aLocked ) {
      iLocked = aLocked;
   }


   public Optional<String> getBarcode() {
      return Optional.ofNullable( iBarcode );
   }


   public void setBarcode( String aBarcode ) {
      iBarcode = aBarcode;
   }

}
