
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
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvCondKey;


/**
 *
 * Aircraft Domain Object
 *
 */
public final class Aircraft {

   private FcModelKey iForecastModel;
   private Map<DataTypeKey, BigDecimal> iUsage = new HashMap<>();
   private List<Blackout> iBlackouts = new ArrayList<>();
   private Boolean iAllowSynchronization;
   private Boolean iLocked;
   private AssemblyKey iAssembly;
   private AssemblyKey iOriginalAssembly;
   private CarrierKey iOperator;
   private PartNoKey iPart;
   private LocationKey iLocation;
   private RefInvCondKey iCondition;
   private String iApplicabilityCode;
   private String iSerialNumber;
   private Date iManufacturedDate;
   private Date iReceivedDate;
   private String iDescription;
   private String iBarcode;
   private String iPositionDescription;
   private Integer iAssociation;
   private OwnerKey iOwner;
   private AuthorityKey iAuthority;
   private Date iReleaseDate;
   private String iReleaseNumber;
   private String iReleaseRemarks;
   private Boolean iComplete;

   private List<CapabilityLevel> iCapabilityLevels = new ArrayList<CapabilityLevel>();

   private Set<InventoryKey> iSerializedKeys = new HashSet<InventoryKey>();

   private Set<InventoryKey> iEngineKeys = new HashSet<InventoryKey>();
   private Set<DomainConfiguration<Engine>> iEngineConfigs =
         new HashSet<DomainConfiguration<Engine>>();

   private Set<InventoryKey> iTrackedKeys = new HashSet<InventoryKey>();
   private Set<DomainConfiguration<TrackedInventory>> iTrackedConfigs =
         new HashSet<DomainConfiguration<TrackedInventory>>();

   // Systems are not their own entities, they are attributes of the aircraft.
   // Thus, they must be configured as part of an aircraft.
   private Set<DomainConfiguration<System>> iSystemConfigs =
         new HashSet<DomainConfiguration<System>>();


   Aircraft() {

   }


   public void setForecastModel( FcModelKey aForecastModel ) {
      iForecastModel = aForecastModel;
   }


   public void addBlackout( Date aStart, Date aEnd ) {
      iBlackouts.add( new Blackout( aStart, aEnd ) );
   }


   public void addUsage( DataTypeKey aType, BigDecimal aUsage ) {
      iUsage.put( aType, aUsage );
   }


   public FcModelKey getForecastModel() {
      return iForecastModel;
   }


   public Map<DataTypeKey, BigDecimal> getUsage() {
      return iUsage;
   }


   public List<Blackout> getBlackouts() {
      return iBlackouts;
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


   public void lock() {
      iLocked = true;
   }


   public void setLocked( boolean aLocked ) {
      iLocked = aLocked;
   }


   public Boolean isLocked() {
      return iLocked;
   }


   public void archive() {
      iCondition = RefInvCondKey.ARCHIVE;
   }


   public void setCondition( RefInvCondKey aCondition ) {
      iCondition = aCondition;
   }


   public RefInvCondKey getCondition() {
      return iCondition;
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


   public void setPart( PartNoKey aPart ) {
      iPart = aPart;
   }


   public PartNoKey getPart() {
      return iPart;
   }


   public LocationKey getLocation() {
      return iLocation;
   }


   public void setLocation( LocationKey aLocation ) {
      iLocation = aLocation;
   }


   public void setManufacturedDate( Date aManufacturedDate ) {
      iManufacturedDate = aManufacturedDate;
   }


   public Date getManufacturedDate() {
      return iManufacturedDate;
   }


   public void setReceivedDate( Date aReceivedDate ) {
      iReceivedDate = aReceivedDate;
   }


   public Date getReceivedDate() {
      return iReceivedDate;
   }


   public void setOperator( CarrierKey aOperator ) {
      iOperator = aOperator;
   }


   public CarrierKey getOperator() {
      return iOperator;
   }


   public AuthorityKey getAuthority() {
      return iAuthority;
   }


   //
   // Engine methods
   //

   public void addEngine( InventoryKey aEngineKey ) {
      iEngineKeys.add( aEngineKey );
   }


   public void addEngine( DomainConfiguration<Engine> aEngineConfiguration ) {
      iEngineConfigs.add( aEngineConfiguration );
   }


   public Set<InventoryKey> getEngineKeys() {
      return iEngineKeys;
   }


   public Set<DomainConfiguration<Engine>> getEngineConfigurations() {
      return iEngineConfigs;
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


   public String getBarcode() {
      return iBarcode;
   }


   public void setBarcode( String aBarcode ) {
      iBarcode = aBarcode;
   }


   public String getPositionDescription() {
      return iPositionDescription;
   }


   public void setPositionDescription( String aPositionDescription ) {
      iPositionDescription = aPositionDescription;
   }


   //
   // Tracked Inventory methods
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
   // System methods
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


   public Set<DomainConfiguration<System>> getSystems() {
      return iSystemConfigs;
   }


   public void setApplicabilityCode( String aApplicabilityCode ) {
      iApplicabilityCode = aApplicabilityCode;
   }


   public String getApplicabilityCode() {
      return iApplicabilityCode;
   }


   public void setSerialNumber( String aSerialNumber ) {
      iSerialNumber = aSerialNumber;
   }


   public String getSerialNumber() {
      return iSerialNumber;
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   public String getDescription() {
      return iDescription;
   }


   public void addCapabilityLevel( CapabilityLevel aCapabilityLevel ) {
      iCapabilityLevels.add( aCapabilityLevel );
   }


   public List<CapabilityLevel> getCapabilityLevels() {
      return iCapabilityLevels;
   }


   public Optional<Integer> getAssociation() {
      return Optional.ofNullable( iAssociation );
   }


   public void setAssociation( Integer aAssociation ) {
      iAssociation = aAssociation;
   }


   public void setOwner( OwnerKey aOwner ) {
      iOwner = aOwner;
   }


   public OwnerKey getOwner() {
      return iOwner;
   }


   public void withAuthority( AuthorityKey aAuthorityKey ) {
      iAuthority = aAuthorityKey;

   }


   public void setReleaseDate( Date aReleaseDate ) {
      iReleaseDate = aReleaseDate;
   }


   public Date getReleaseDate() {
      return iReleaseDate;
   }


   public void setReleaseNumber( String aReleaseNumber ) {
      iReleaseNumber = aReleaseNumber;
   }


   public String getReleaseNumber() {
      return iReleaseNumber;
   }


   public void setReleaseRemarks( String aReleaseRemarks ) {
      iReleaseRemarks = aReleaseRemarks;
   }


   public String getReleaseRemarks() {
      return iReleaseRemarks;
   }


   public void setComplete( boolean aComplete ) {
      iComplete = aComplete;
   }


   public Optional<Boolean> isComplete() {
      return Optional.ofNullable( iComplete );
   }


   public static final class Blackout {

      private final Date iEnd;
      private final Date iStart;


      public Blackout(Date aStart, Date aEnd) {
         iStart = aStart;
         iEnd = aEnd;
      }


      public Date getEnd() {
         return iEnd;
      }


      public Date getStart() {
         return iStart;
      }
   }

   /**
    *
    * Aircraft Capability Level Object
    *
    */
   public static final class CapabilityLevel {

      private final int iCapDbId;
      private final String iCapCd;
      private final int iLevelDbId;
      private final String iLevelCd;
      private final String iCustomLevel;
      private final int iConfigLevelDbId;
      private final String iConfigLevelCd;


      public CapabilityLevel(int aCapDbId, String aCapCd, int aLevelDbId, String aLevelCd,
            String aCustomLevel, int aConfigLevelDbId, String aConfigLevelCd) {
         iCapDbId = aCapDbId;
         iCapCd = aCapCd;
         iLevelDbId = aLevelDbId;
         iLevelCd = aLevelCd;
         iCustomLevel = aCustomLevel;
         iConfigLevelDbId = aConfigLevelDbId;
         iConfigLevelCd = aConfigLevelCd;

      }


      public int getCapDbId() {
         return iCapDbId;
      }


      public String getCapCd() {
         return iCapCd;
      }


      public int getLevelDbId() {
         return iLevelDbId;
      }


      public String getLevelCd() {
         return iLevelCd;
      }


      public String getCustomLevel() {
         return iCustomLevel;
      }


      public int getConfigLevelDbId() {
         return iConfigLevelDbId;
      }


      public String getConfigLevelCd() {
         return iConfigLevelCd;
      }
   }

}
