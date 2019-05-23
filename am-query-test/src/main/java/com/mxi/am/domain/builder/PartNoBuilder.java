package com.mxi.am.domain.builder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefFinancialClassKey;
import com.mxi.mx.core.key.RefHazmatKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPartTypeKey;
import com.mxi.mx.core.key.RefPartUseKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.table.eqp.EqpManufactTable;
import com.mxi.mx.core.table.eqp.EqpPartAltUnit;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.ref.RefFinancialClass;


/**
 * Builds a <code>eqp_part_no</code> object
 */
public class PartNoBuilder implements DomainBuilder<PartNoKey> {

   private RefAbcClassKey iAbcClass;

   private String iApplicabilityRange;

   private FncAccountKey iAssetAccount;

   private FncAccountKey iRepairOrderAccount;

   private BigDecimal iAvgUnitPrice;

   private RefFinanceTypeKey iFinancialType;

   private RefInvClassKey iInvClass;

   private ManufacturerKey iManufacturer;

   private PartGroupKey iAlternatePartGroup;

   private Boolean iPartGroupApproved = false;

   private String iPartNoOem;

   private boolean iProcurableBool = true;

   private boolean iReceiptInspBool = false;

   private boolean iRepairBool = false;

   private boolean iNoAutoReserveBool = false;

   private String iShortDescription;

   private RefPartStatusKey iStatus;

   private BigDecimal iTotalQt;

   private BigDecimal iTotalValue;

   private RefQtyUnitKey iUnitType = RefQtyUnitKey.EA;

   private StockNoKey iStockNoKey;

   private boolean iHasDefaultPartGroup = false;

   private PartGroupKey iDefaultPartGroup;

   private boolean iIsTool = false;

   private Map<RefQtyUnitKey, BigDecimal> iAlternateUnitTypes =
         new HashMap<RefQtyUnitKey, BigDecimal>();

   private String iStorageDescription;

   private RefPartTypeKey iPartType;

   private RefHazmatKey iHazmat;


   /**
    * {@inheritDoc}
    */
   @Override
   public PartNoKey build() {
      PartNoKey lPartNo = EqpPartNoTable.generatePartNoPK();
      EqpPartNoTable lTable = EqpPartNoTable.create( lPartNo );

      lTable.setInvClass( iInvClass );
      lTable.setQtyUnit( iUnitType );
      lTable.setPartStatus( iStatus );
      lTable.setAvgUnitPrice( iAvgUnitPrice );
      lTable.setTotalQt( iTotalQt );
      lTable.setTotalValue( iTotalValue );
      lTable.setAbcClass( iAbcClass );
      lTable.setAssetAccount( iAssetAccount );
      lTable.setEqpManufact( iManufacturer );
      lTable.setReceiptInspBool( iReceiptInspBool );
      lTable.setStorageLdesc( iStorageDescription );
      lTable.setPartType( iPartType );
      lTable.setHazmat( iHazmat );

      if ( iPartNoOem != null ) {
         lTable.setPartNoOem( iPartNoOem );
      }

      lTable.setPartNoSdesc( iShortDescription );

      if ( iFinancialType != null ) {
         RefFinancialClass lRefFinancialClass = RefFinancialClass.findByPrimaryKey(
               new RefFinancialClassKey( ( int ) PrimaryKeyService.getInstance().getDatabaseId(),
                     iFinancialType.getCd() ) );
         if ( !lRefFinancialClass.exists() ) {
            lRefFinancialClass = RefFinancialClass.create( iFinancialType.getCd() );
            lRefFinancialClass.setFinancialType( iFinancialType );
            lRefFinancialClass.insert();
         }

         lTable.setFinancialClass( lRefFinancialClass.getPk() );
      }

      if ( iAlternatePartGroup != null ) {
         EqpPartBaselineTable lEqpPartBaseline = EqpPartBaselineTable
               .create( new EqpPartBaselineKey( iAlternatePartGroup, lPartNo ) );

         lEqpPartBaseline.setApplEffLdesc( iApplicabilityRange );
         if ( iPartGroupApproved ) {
            lEqpPartBaseline.setApprovedBool( iPartGroupApproved );
         }
         lEqpPartBaseline.insert();
      }

      if ( iHasDefaultPartGroup && !iIsTool ) {
         iDefaultPartGroup = new PartGroupDomainBuilder( "DEFAULT_PART_GROUP" )
               .withPartGroupName( "Default Part Group" ).withInventoryClass( iInvClass )
               .withPartNo( lPartNo ).build();
      }

      if ( iIsTool ) {
         lTable.setPartUse( RefPartUseKey.TOOLS );
         // always create a default tool part group
         new PartGroupDomainBuilder( iPartNoOem ).withInventoryClass( iInvClass )
               .withPartNo( lPartNo ).isToolGroup().build();
      }

      lTable.setRepairBool( iRepairBool );
      lTable.setProcurableBool( iProcurableBool );
      lTable.setNoAutoReserveBool( iNoAutoReserveBool );

      if ( iStockNoKey != null ) {
         lTable.setStockNumber( iStockNoKey );
      }

      if ( iRepairOrderAccount != null ) {
         lTable.setRepairOrderAccount( iRepairOrderAccount );
      }

      iAlternateUnitTypes.forEach( ( lAlternateUnitType, lConversionFactor ) -> {
         EqpPartAltUnit lEqpPartAltUnit = EqpPartAltUnit.create( lPartNo, lAlternateUnitType );
         lEqpPartAltUnit.setQtyConvertQt( lConversionFactor );
         lEqpPartAltUnit.insert();
      } );

      if ( iManufacturer != null ) {
         if ( !EqpManufactTable.findByPrimaryKey( iManufacturer ).exists() ) {
            EqpManufactTable lEqpManufactTable = EqpManufactTable.create( iManufacturer );
            lEqpManufactTable.insert();
         }
      }
      return lTable.insert();
   }


   public PartNoBuilder isReceiptInsp() {
      iReceiptInspBool = true;

      return this;
   }


   public PartNoBuilder isAlternateIn( PartGroupKey aPartGroup ) {
      iAlternatePartGroup = aPartGroup;

      return this;
   }


   public PartNoBuilder isPartGroupApproved() {
      iPartGroupApproved = true;
      return this;
   }


   /**
    * Sets the part to non-procurable.
    *
    * @return The builder
    */
   public PartNoBuilder isNonProcurable() {
      iProcurableBool = false;

      return this;
   }


   /**
    * Sets no auto-reservation for the part
    *
    * @return The builder
    */
   public PartNoBuilder isNoAutoReserve() {
      iNoAutoReserveBool = true;

      return this;
   }


   /**
    * Makes the part into a tool.
    *
    * @return The builder
    */
   public PartNoBuilder isTool() {
      iIsTool = true;

      return this;
   }


   /**
    * Sets the part's manufacturer.
    *
    * @param aManufacturer
    *           The manufacturer
    *
    * @return The builder
    */
   public PartNoBuilder manufacturedBy( ManufacturerKey aManufacturer ) {
      iManufacturer = aManufacturer;

      return this;
   }


   public PartNoBuilder withStorageDescription( String aStorageDescription ) {
      iStorageDescription = aStorageDescription;

      return this;
   }


   public PartNoBuilder withHazmat( RefHazmatKey aHazmat ) {
      iHazmat = aHazmat;
      return this;
   }


   /**
    * Sets the ABC class
    *
    * @param aAbcClass
    *           The ABC class
    *
    * @return The builder
    */
   public PartNoBuilder withAbcClass( RefAbcClassKey aAbcClass ) {
      iAbcClass = aAbcClass;

      return this;
   }


   /**
    *
    * Sets an alternate unit of measure on the part with the given conversion factor.
    *
    * @param aAlternateUnit
    *           the alternate unit of measure
    * @param aConversionFactor
    *           the conversion factor (x) where x standard unit of measure = 1 alternate unit of
    *           measure
    * @return The builder
    */
   public PartNoBuilder withAlternateUnitType( RefQtyUnitKey aAlternateUnitType,
         BigDecimal aConversionFactor ) {
      iAlternateUnitTypes.put( aAlternateUnitType, aConversionFactor );

      return this;
   }


   public PartNoBuilder
         withAlternateUnitTypes( Map<RefQtyUnitKey, BigDecimal> aAlternateunitTypes ) {
      iAlternateUnitTypes = aAlternateunitTypes;

      return this;
   }


   public PartNoBuilder withPartType( RefPartTypeKey aPartType ) {
      iPartType = aPartType;
      return this;
   }


   /**
    * Sets the applicability range expression.
    *
    * @param aApplicabilityRange
    *           the applicability range
    *
    * @return the builder
    */
   public PartNoBuilder withApplicabilityRange( String aApplicabilityRange ) {
      iApplicabilityRange = aApplicabilityRange;

      return this;
   }


   /**
    * Sets the part's asset account.
    *
    * @param aAssetAccount
    *           The asset account.
    *
    * @return The builder
    */
   public PartNoBuilder withAssetAccount( FncAccountKey aAssetAccount ) {
      iAssetAccount = aAssetAccount;

      return this;
   }


   /**
    * Sets the average unit price.
    *
    * @param aAvgUnitPrice
    *           The average unit price
    *
    * @return The builder
    */
   public PartNoBuilder withAverageUnitPrice( BigDecimal aAvgUnitPrice ) {
      iAvgUnitPrice = aAvgUnitPrice;

      return this;
   }


   public PartNoBuilder withDefaultPartGroup() {

      iHasDefaultPartGroup = true;

      return this;
   }


   /**
    * Sets the financial type.
    *
    * @param aFinancialType
    *           The financial type
    *
    * @return The builder
    */
   public PartNoBuilder withFinancialType( RefFinanceTypeKey aFinancialType ) {
      iFinancialType = aFinancialType;

      return this;
   }


   /**
    * Sets the inventory class of the part.
    *
    * @param aInvClass
    *           The inventory class.
    *
    * @return The builder
    */
   public PartNoBuilder withInventoryClass( RefInvClassKey aInvClass ) {
      iInvClass = aInvClass;

      return this;
   }


   /**
    * Sets the OEM Part Number.
    *
    * @param aPartNoOem
    *           The OEM part number
    *
    * @return The builder
    */
   public PartNoBuilder withOemPartNo( String aPartNoOem ) {
      iPartNoOem = aPartNoOem;

      return this;
   }


   /**
    * Sets Repairable flag
    *
    * @param aRepairBool
    *           set Repairable flag
    *
    * @return The builder
    */
   public PartNoBuilder withRepairBool( boolean aRepairBool ) {
      iRepairBool = aRepairBool;

      return this;
   }


   /**
    * Sets the part's repair order account.
    *
    * @param aRepairOrderAccount
    *           The repair order account.
    *
    * @return The builder
    */
   public PartNoBuilder withRepairOrderAccount( FncAccountKey aRepairOrderAccount ) {
      iRepairOrderAccount = aRepairOrderAccount;

      return this;
   }


   /**
    * Sets the short description.
    *
    * @param aDescription
    *           The description
    *
    * @return The builder
    */
   public PartNoBuilder withShortDescription( String aDescription ) {
      iShortDescription = aDescription;

      return this;
   }


   /**
    * Sets the part number's status
    *
    * @param aStatus
    *           The status
    *
    * @return The builder
    */
   public PartNoBuilder withStatus( RefPartStatusKey aStatus ) {
      iStatus = aStatus;

      return this;
   }


   /**
    * Sets the stock
    *
    * @param aStockNoKey
    *           The stock
    *
    * @return The builder
    */
   public PartNoBuilder withStock( StockNoKey aStockNoKey ) {
      iStockNoKey = aStockNoKey;

      return this;
   }


   /**
    * Sets the total quantity
    *
    * @param aTotalQt
    *           The total quantity
    *
    * @return The builder
    */
   public PartNoBuilder withTotalQuantity( BigDecimal aTotalQt ) {
      iTotalQt = aTotalQt;

      return this;
   }


   /**
    * Sets the total value.
    *
    * @param aTotalValue
    *           The total value
    *
    * @return The builder
    */
   public PartNoBuilder withTotalValue( BigDecimal aTotalValue ) {
      iTotalValue = aTotalValue;

      return this;
   }


   /**
    * Sets the part's unit type.
    *
    * @param aUnitType
    *           The unit type.
    *
    * @return The builder
    */
   public PartNoBuilder withUnitType( RefQtyUnitKey aUnitType ) {
      iUnitType = aUnitType;

      return this;
   }


   public PartGroupKey getDefaultPartGroup() {
      return iDefaultPartGroup;
   }
}
