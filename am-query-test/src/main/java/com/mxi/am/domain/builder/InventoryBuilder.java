package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.InvKitMapKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvCapabilityKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefInvOperKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.table.inv.InvAcReg;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvKitMapTable;
import com.mxi.mx.core.table.inv.InvKitTable;


/**
 * Builds an <code>inv_inv</code> object
 */
public class InventoryBuilder implements DomainBuilder<InventoryKey> {

   private AircraftRegistrationBuilder iAircraftRegBuilder = new AircraftRegistrationBuilder();

   private String iApplicabilityCode;

   private TestDataBuilderStub<InventoryKey> iAssemblyInventory =
         new NullTestDataBuilder<InventoryKey>();

   private AuthorityKey iAuthority;

   private DomainBuilder<ConfigSlotPositionKey> iConfigSlotPositionBuilder =
         new NullTestDataBuilder<ConfigSlotPositionKey>();

   private String iDescription;

   private InventoryKey iHInventory = null;

   private RefInvClassKey iInventoryClass = RefInvClassKey.ACFT;

   private RefInvCondKey iInventoryCondition = RefInvCondKey.INSRV;

   private boolean iIssued = false;

   private LocationKey iLocation;

   private boolean iLocked = false;

   private Date iManufactDt;

   private InventoryKey iNhInventory = null;

   private CarrierKey iOperator;

   private PurchaseOrderLineKey iOrderLine;

   private AssemblyKey iOriginalAssembly = null;

   private OwnerKey iOwner;

   private RefOwnerTypeKey iOwnershipType = RefOwnerTypeKey.LOCAL;

   private DomainBuilder<PartGroupKey> iPartGroupBuilder = new NullTestDataBuilder<PartGroupKey>();

   private DomainBuilder<PartNoKey> iPartNoBuilder = new PartNoBuilder();

   private boolean iPreventSync = true;

   private Date iReceivedDt;

   private Date iShelfExpiryDt;

   private String iSerialNo;

   private String iBatchNo;

   private String iBarcode;

   private double iBinQt;

   private double iReserveQt;

   private String iReleaseNumber;

   private Date iReleaseDate;

   private String iReleaseRemarks;

   private boolean iReserved = false;

   private boolean iComplete = false;

   private InvInvTable.FinanceStatusCd iFinanceStatusCd;

   private Map<DataTypeKey, UsageParameterBuilder> iUsageParameterBuilders =
         new HashMap<DataTypeKey, UsageParameterBuilder>();

   private InventoryKey iKit;

   private Integer iAircraftGroup;


   /**
    * Sets the inventory to allow synchronization.
    *
    * @return The builder
    */
   public InventoryBuilder allowSync() {
      iPreventSync = false;

      return this;
   }


   /**
    * Sets the location of the inventory.
    *
    * @param aLocation
    *           The location
    *
    * @return The builder
    */
   public InventoryBuilder atLocation( LocationKey aLocation ) {
      iLocation = aLocation;

      return this;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public InventoryKey build() {
      InventoryKey lInventory = InvInvTable.generatePk();
      InvInvTable lTable = InvInvTable.create( lInventory );
      lTable.setInvCond( iInventoryCondition );
      lTable.setInvClass( iInventoryClass );
      lTable.setLockedBool( iLocked );
      lTable.setIssuedBool( iIssued );
      lTable.setInvNoSdesc( iDescription );
      lTable.setNhInvNo( iNhInventory );
      lTable.setPartNo( iPartNoBuilder.build() );
      lTable.setHInvNo( ( iHInventory == null ) ? lInventory : iHInventory );
      lTable.setPartGroup( iPartGroupBuilder.build() );
      lTable.setLocation( iLocation );
      lTable.setOwner( iOwner );
      lTable.setOwnershipType( iOwnershipType );
      lTable.setPurchaseOrderLine( iOrderLine );
      lTable.setPreventReqdBool( iPreventSync );
      lTable.setApplEffCd( iApplicabilityCode );
      lTable.setSerialNoOEM( iSerialNo );
      lTable.setBatchNoOem( iBatchNo );
      lTable.setBarcode( iBarcode );
      lTable.setManufactDt( iManufactDt );
      lTable.setReceivedDt( iReceivedDt );
      lTable.setCarrier( iOperator );
      lTable.setAuthority( iAuthority );
      lTable.setReservedQt( iReserveQt );
      lTable.setReleaseNumber( iReleaseNumber );
      lTable.setReleaseDate( iReleaseDate );
      lTable.setReleaseRemarks( iReleaseRemarks );
      lTable.setReservedBool( iReserved );
      lTable.setComplete( iComplete );
      lTable.setShelfExpiryDt( iShelfExpiryDt );

      if ( iFinanceStatusCd != null ) {
         lTable.setFinanceStatusCd( iFinanceStatusCd );
      }

      // only set the bin quantity for batch items
      if ( RefInvClassKey.BATCH.equals( iInventoryClass ) ) {
         lTable.setBinQt( iBinQt );
      }

      InventoryKey lAssemblyInventory = iAssemblyInventory.build();
      lTable.setAssmblInvNo( ( lAssemblyInventory == null ) ? lInventory : lAssemblyInventory );
      lTable.setOrigAssmbl( iOriginalAssembly );

      ConfigSlotPositionKey lConfigSlotPosition = iConfigSlotPositionBuilder.build();
      if ( !iUsageParameterBuilders.isEmpty() && ( lConfigSlotPosition == null ) ) {
         // If usage parameters are provided then the inventory needs a config slot position.
         lConfigSlotPosition = new ConfigSlotPositionBuilder().build();
      }

      lTable.setBomItemPosition( lConfigSlotPosition );

      InventoryKey lInventoryKey = lTable.insert();

      if ( ACFT.equals( iInventoryClass ) ) {
         iAircraftRegBuilder.forAircraft( lInventoryKey ).build();
      }

      for ( UsageParameterBuilder lUsageParameterBuilder : iUsageParameterBuilders.values() ) {
         lUsageParameterBuilder.forInventory( lInventoryKey );
         lUsageParameterBuilder.build();
      }

      if ( RefInvClassKey.KIT.equals( iInventoryClass ) ) {
         InvKitTable.create( lInventoryKey ).insert();
      }

      if ( iKit != null ) {
         InvKitMapKey lInvKitMapKey = new InvKitMapKey( iKit, lInventoryKey );
         InvKitMapTable.create( lInvKitMapKey ).insert();
      }

      if ( iAircraftGroup != null ) {
         // Assign the first aircraft to AIRCRAFT_GROUP_1
         DataSetArgument lArgs = new DataSetArgument();
         lArgs.add( lInventoryKey, "acft_no_db_id", "acft_no_id" );
         lArgs.add( "group_id", iAircraftGroup );
         MxDataAccess.getInstance().executeInsert( "acft_group_assignment", lArgs );
      }

      return lInventoryKey;
   }


   public InventoryBuilder isInKit( InventoryKey aKitInventory ) {
      iKit = aKitInventory;

      return this;
   }


   /**
    * Sets the inventory as issued.
    *
    * @return The builder
    */
   public InventoryBuilder isIssued() {
      iIssued = true;

      return this;
   }


   /**
    * Sets the inventory to locked.
    *
    * @return The builder
    */
   public InventoryBuilder isLocked() {
      iLocked = true;

      return this;
   }


   /**
    * Sets the inventory to reserved.
    *
    * @return The builder
    */
   public InventoryBuilder isReserved() {
      iReserved = true;

      return this;
   }


   /**
    * Sets reserve qty.
    *
    * @param aReserveQt
    *           the reserve qty
    *
    * @return the builder
    */
   public InventoryBuilder withReserveQt( double aReserveQt ) {
      iReserveQt = aReserveQt;

      return this;
   }


   /**
    * Sets the date of manufacture.
    *
    * @param aManufactDt
    *           The date of manufacture
    *
    * @return The builder
    */
   public InventoryBuilder manufacturedOn( Date aManufactDt ) {
      iManufactDt = aManufactDt;

      return this;
   }


   /**
    * Sets the inventory to prevent synchronization.
    *
    * @return The builder
    */
   public InventoryBuilder preventSync() {
      iPreventSync = true;

      return this;
   }


   /**
    * Sets the date of receipt.
    *
    * @param aReceiptDt
    *           The date of receipt
    *
    * @return The builder
    */
   public InventoryBuilder receivedOn( Date aReceiptDt ) {
      iReceivedDt = aReceiptDt;

      return this;
   }


   /**
    * Sets the applicability code.
    *
    * @param aApplicabilityCode
    *           the applicability code
    *
    * @return The builder
    */
   public InventoryBuilder withApplicabilityCode( String aApplicabilityCode ) {
      iApplicabilityCode = aApplicabilityCode;

      return this;
   }


   /**
    * Sets the assembly inventory.
    *
    * @param aInventory
    *           The assembly inventory
    *
    * @return The builder
    */
   public InventoryBuilder withAssemblyInventory( InventoryKey aInventory ) {
      return withAssemblyInventory( new TestDataBuilderStub<InventoryKey>( aInventory ) );
   }


   /**
    * Sets the assembly inventory using a builder
    *
    * @param aAssemblyInventoryBuilder
    *           The assembly inventory builder
    *
    * @return The builder
    */
   public InventoryBuilder
         withAssemblyInventory( TestDataBuilderStub<InventoryKey> aAssemblyInventoryBuilder ) {
      iAssemblyInventory = aAssemblyInventoryBuilder;

      return this;
   }


   /**
    * Sets the capability (applies to aircraft only)
    *
    * @param aCapability
    *           The capability
    *
    * @return The builder
    */
   public InventoryBuilder withCapability( RefInvCapabilityKey aCapability ) {
      iAircraftRegBuilder.withCapability( aCapability );

      return this;
   }


   /**
    * Sets the inventory class
    *
    * @param aInventoryClass
    *           the inventory class
    *
    * @return the builder
    */
   public InventoryBuilder withClass( RefInvClassKey aInventoryClass ) {
      iInventoryClass = aInventoryClass;

      return this;
   }


   /**
    * Sets the finance status code
    *
    * @param aFinanceStatusCd
    *           the finance status code
    *
    * @return the builder
    */
   public InventoryBuilder withFinanceStatusCd( InvInvTable.FinanceStatusCd aFinanceStatusCd ) {

      iFinanceStatusCd = aFinanceStatusCd;

      return this;
   }


   /**
    * Sets the condition.
    *
    * @param aCondition
    *           the condition code
    *
    * @return The builder
    */
   public InventoryBuilder withCondition( RefInvCondKey aCondition ) {
      iInventoryCondition = aCondition;

      return this;
   }


   /**
    * Sets the configuration slot position
    *
    * @param aConfigSlotPositionBuilder
    *           the config slot position builder
    *
    * @return the builder
    */
   public InventoryBuilder
         withConfigSlotPosition( DomainBuilder<ConfigSlotPositionKey> aConfigSlotPositionBuilder ) {
      iConfigSlotPositionBuilder = aConfigSlotPositionBuilder;

      return this;
   }


   /**
    * Sets the configuration slot position
    *
    * @param aConfigSlotPosition
    *           the configuration slot position
    *
    * @return the builder
    */
   public InventoryBuilder withConfigSlotPosition( ConfigSlotPositionKey aConfigSlotPosition ) {
      return withConfigSlotPosition(
            new TestDataBuilderStub<ConfigSlotPositionKey>( aConfigSlotPosition ) );
   }


   /**
    * Sets the CYCLES usage for TSN, TSO, and TSI.
    *
    * @param aCyclesUsage
    *           The current cycles usage
    *
    * @return The builder
    */
   public InventoryBuilder withCyclesUsage( double aCyclesUsage ) {
      return withUsage( DataTypeKey.CYCLES, aCyclesUsage );
   }


   /**
    * Sets the inventory description.
    *
    * @param aDescription
    *           The description
    *
    * @return The builder
    */
   public InventoryBuilder withDescription( String aDescription ) {
      iDescription = aDescription;

      return this;
   }


   /**
    * Sets the forecast model for an aircraft.
    *
    * @param aForecastModel
    *           The forecast model
    *
    * @return The builder
    */
   public InventoryBuilder withForecastModel( FcModelKey aForecastModel ) {
      iAircraftRegBuilder.withForecastModel( aForecastModel );

      return this;
   }


   /**
    * Sets the highest inventory
    *
    * @param aHInventory
    *           the inventory
    *
    * @return the builder
    */
   public InventoryBuilder withHighestInventory( InventoryKey aHInventory ) {
      iHInventory = aHInventory;
      if ( iNhInventory == null ) {
         iNhInventory = iHInventory;
      }

      return this;
   }


   /**
    * Sets the HOURS usage for TSN, TSO, and TSI.
    *
    * @param aHoursUsage
    *           The current hours usage
    *
    * @return The builder
    */
   public InventoryBuilder withHoursUsage( double aHoursUsage ) {
      return withUsage( DataTypeKey.HOURS, aHoursUsage );
   }


   /**
    * Sets the operational state (applies to aircraft only)
    *
    * @param aOperationalState
    *           The operational state
    *
    * @return The builder
    */
   public InventoryBuilder withOperationalState( RefInvOperKey aOperationalState ) {
      iAircraftRegBuilder.withOperationalState( aOperationalState );

      return this;
   }


   /**
    * Sets the operator (a.k.a. carrier)
    *
    * @param aOperator
    *           The operator (carrier)
    *
    * @return The builder
    */
   public InventoryBuilder withOperator( CarrierKey aOperator ) {
      iOperator = aOperator;

      return this;
   }


   /**
    * Sets the order line.
    *
    * @param aOrderLine
    *           The order line
    *
    * @return The builder
    */
   public InventoryBuilder withOrderLine( PurchaseOrderLineKey aOrderLine ) {
      iOrderLine = aOrderLine;

      return this;
   }


   /**
    * Sets the original assembly
    *
    * @param aOriginalAssembly
    *           the original assembly
    *
    * @return the builder
    */
   public InventoryBuilder withOriginalAssembly( AssemblyKey aOriginalAssembly ) {
      iOriginalAssembly = aOriginalAssembly;

      return this;
   }


   /**
    * Sets the owner of the inventory.
    *
    * @param aOwner
    *           The owner.
    *
    * @return The builder
    */
   public InventoryBuilder withOwner( OwnerKey aOwner ) {
      iOwner = aOwner;

      return this;
   }


   /**
    * Sets the ownership type.
    *
    * @param aOwnershipType
    *           The ownership type
    *
    * @return The builder
    */
   public InventoryBuilder withOwnershipType( RefOwnerTypeKey aOwnershipType ) {
      iOwnershipType = aOwnershipType;

      return this;
   }


   /**
    * Sets the parent inventory
    *
    * @param aNhInventory
    *           the parent inventory
    *
    * @return the builder
    */
   public InventoryBuilder withParentInventory( InventoryKey aNhInventory ) {
      iNhInventory = aNhInventory;
      if ( iHInventory == null ) {
         iHInventory = aNhInventory;
      }

      return this;
   }


   /**
    * Sets the part group
    *
    * @param aPartGroup
    *           the part group
    *
    * @return the builder
    */
   public InventoryBuilder withPartGroup( PartGroupKey aPartGroup ) {
      return withPartGroup( new TestDataBuilderStub<PartGroupKey>( aPartGroup ) );
   }


   /**
    * Sets the part group builder
    *
    * @param aPartGroupBuilder
    *           the part group builder
    *
    * @return the builder
    */
   public InventoryBuilder withPartGroup( DomainBuilder<PartGroupKey> aPartGroupBuilder ) {
      iPartGroupBuilder = aPartGroupBuilder;

      return this;
   }


   /**
    * Sets the part no builder
    *
    * @param aPartNoBuilder
    *           the part no builder
    *
    * @return the builder
    */
   public InventoryBuilder withPartNo( DomainBuilder<PartNoKey> aPartNoBuilder ) {
      iPartNoBuilder = aPartNoBuilder;

      return this;
   }


   /**
    * Sets the part no
    *
    * @param aPartNo
    *           the part no
    *
    * @return the builder
    */
   public InventoryBuilder withPartNo( PartNoKey aPartNo ) {
      return withPartNo( new TestDataBuilderStub<PartNoKey>( aPartNo ) );
   }


   /**
    * Sets the authority required for the inventory.
    *
    * @param aAuthority
    *           the authority
    *
    * @return The builder
    */
   public InventoryBuilder withRequiredAuthority( AuthorityKey aAuthority ) {
      iAuthority = aAuthority;

      return this;
   }


   /**
    * Sets the serial number.
    *
    * @param aSerialNo
    *           The serial number
    *
    * @return The builder
    */
   public InventoryBuilder withSerialNo( String aSerialNo ) {
      iSerialNo = aSerialNo;

      return this;
   }


   /**
    * Sets the batch number.
    *
    * @param aBatchNo
    *           The batch number
    *
    * @return The builder
    */
   public InventoryBuilder withBatchNo( String aBatchNo ) {
      iBatchNo = aBatchNo;

      return this;
   }


   /**
    * Sets the barcode.
    *
    * @param aBarcode
    *           The barcode
    *
    * @return The builder
    */
   public InventoryBuilder withBarcode( String aBarcode ) {
      iBarcode = aBarcode;

      return this;
   }


   /**
    * Sets a usage parameter given the provided data type and quantity (used for TSN, TSO, and TSI).
    *
    * @param aDataType
    *           the data type
    * @param aTsn
    *           the TSN quantity to be used for TSN, TSO, and TSI
    *
    * @return the builder
    */
   public InventoryBuilder withUsage( DataTypeKey aDataType, double aTsn ) {
      return withUsage( aDataType, aTsn, aTsn, aTsn );
   }


   /**
    * Sets bin qty.
    *
    * @param aBinQt
    *           the bin qty
    *
    * @return the builder
    */
   public InventoryBuilder withBinQt( double aBinQt ) {
      iBinQt = aBinQt;

      return this;
   }


   /**
    *
    * Sets release number
    *
    * @param aReleaseNumber
    * @return
    */
   public InventoryBuilder withReleaseNumber( String aReleaseNumber ) {
      iReleaseNumber = aReleaseNumber;

      return this;
   }


   /**
    *
    * Sets release date
    *
    * @param aReleaseDate
    * @return
    */
   public InventoryBuilder withReleaseDate( Date aReleaseDate ) {
      iReleaseDate = aReleaseDate;

      return this;
   }


   /**
    *
    * Sets release remarks
    *
    * @param aReleaseRemarks
    * @return
    */
   public InventoryBuilder withReleaseRemarks( String aReleaseRemarks ) {
      iReleaseRemarks = aReleaseRemarks;

      return this;
   }


   /**
    *
    * Sets complete bool
    *
    * @param aComplete
    * @return
    */
   public InventoryBuilder withComplete( Boolean aComplete ) {
      iComplete = aComplete;

      return this;
   }


   /**
    * Sets Locked bool
    *
    * @param aLocked
    */
   public InventoryBuilder withLockedBoolean( boolean aLocked ) {
      iLocked = aLocked;

      return this;

   }


   /**
    * Sets a usage parameter given the provided data type and quantities.
    *
    * @param aDataType
    *           the data type
    * @param aTsn
    *           the TSN quantity
    * @param aTso
    *           the TSO quantity
    * @param aTsi
    *           the TSI quantity
    *
    * @return the builder
    */
   public InventoryBuilder withUsage( DataTypeKey aDataType, double aTsn, double aTso,
         double aTsi ) {
      iUsageParameterBuilders.put( aDataType, new UsageParameterBuilder().forDataType( aDataType )
            .withTsn( aTsn ).withTso( aTso ).withTsi( aTsi ) );

      return this;
   }


   /**
    * Sets the Registration Code.
    *
    * @param aRegistrationCode
    *           The Registration Code
    *
    * @return The builder
    */
   public InventoryBuilder withRegistrationCode( String aRegistrationCode ) {
      iAircraftRegBuilder.withRegistrationCode( aRegistrationCode );

      return this;
   }


   public InventoryBuilder inAicraftGroup( Integer aAircraftGroup ) {
      iAircraftGroup = aAircraftGroup;

      return this;
   }


   /**
    * Sets the shelf expiry date.
    *
    * @param aReceiptDt
    *           The date of receipt
    *
    * @return The builder
    */
   public InventoryBuilder withShelfExpiryDt( Date aShelfExpiryDt ) {
      iShelfExpiryDt = aShelfExpiryDt;

      return this;
   }


   /**
    * This builder helps the inventory builder when the inventory is an aircraft.
    *
    * @author dsewell
    */
   private class AircraftRegistrationBuilder implements DomainBuilder<AircraftKey> {

      private RefInvCapabilityKey iCapability;

      private FcModelKey iForecastModel;

      private InventoryKey iInventoryKey;

      private RefInvOperKey iOperationalState = RefInvOperKey.NORM;

      private String iAcRegCd = null;


      /**
       * Builds the aircraft registration.
       *
       * @return The aircraft key
       */
      @Override
      public AircraftKey build() {
         InvAcReg.create( iInventoryKey, iOperationalState, null, iCapability, null, iAcRegCd, null,
               false, null, null, null, iForecastModel, false, null );

         return new AircraftKey( iInventoryKey );
      }


      /**
       * Sets the aircraft
       *
       * @param aInventoryKey
       *           The aircraft
       *
       * @return The builder
       */
      public AircraftRegistrationBuilder forAircraft( InventoryKey aInventoryKey ) {
         iInventoryKey = aInventoryKey;

         return this;
      }


      /**
       * Sets the capability
       *
       * @param aCapability
       *           The capability
       *
       * @return The builder
       */
      public AircraftRegistrationBuilder withCapability( RefInvCapabilityKey aCapability ) {
         iCapability = aCapability;

         return this;
      }


      /**
       * Sets the forecast model.
       *
       * @param aForecastModel
       *           The forecast model
       *
       * @return The builder
       */
      public AircraftRegistrationBuilder withForecastModel( FcModelKey aForecastModel ) {
         iForecastModel = aForecastModel;

         return this;
      }


      /**
       * Sets the operational state
       *
       * @param aOperationalState
       *           The operational state
       *
       * @return The builder
       */
      public AircraftRegistrationBuilder withOperationalState( RefInvOperKey aOperationalState ) {
         iOperationalState = aOperationalState;

         return this;
      }


      /**
       * Sets the registration code.
       *
       * @param aRegistrationCode
       *           The registration code
       *
       * @return The builder
       */
      public AircraftRegistrationBuilder withRegistrationCode( String aRegistrationCode ) {
         iAcRegCd = aRegistrationCode;

         return this;
      }

   }
}
