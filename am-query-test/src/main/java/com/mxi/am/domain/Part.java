package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefHazmatKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPartTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.StockNoKey;


public class Part {

   private RefInvClassKey iInventoryClass;
   private Boolean iInDefaultPartGroup;
   private RefPartStatusKey iStatus;
   private String iPartNoOem;

   private String iShortDescription;
   private String iCode;
   private PartGroupKey iPartGroup;
   private Boolean iPartGroupApproved;
   private String iApplicabilityRange;
   private ManufacturerKey iManufacturer;
   private boolean tool;
   private RefQtyUnitKey iQtyUnitKey;
   private BigDecimal iAverageUnitPrice;
   private RefFinanceTypeKey iFinancialType;
   private BigDecimal iQuantity;
   private BigDecimal iTotalValue;
   private StockNoKey iStockNoKey;
   private Boolean iRepairable;
   private FncAccountKey iFinancialAccount;
   private String iStorageDescription;
   private RefPartTypeKey iPartType;
   private RefHazmatKey iHazmat;
   private List<PartAdvisory> iPartAdvisories = new ArrayList<PartAdvisory>();


   public RefHazmatKey getHazmat() {
      return iHazmat;
   }


   public void setHazmat( RefHazmatKey aHazmat ) {
      iHazmat = aHazmat;
   }


   public RefPartTypeKey getPartType() {
      return iPartType;
   }


   public void setPartType( RefPartTypeKey aPartType ) {
      iPartType = aPartType;
   }


   public void addAdvisory( PartAdvisory aAdvisory ) {
      iPartAdvisories.add( aAdvisory );
   }


   public List<PartAdvisory> getPartAdvisories() {
      return iPartAdvisories;
   }


   public String getStorageDescription() {
      return iStorageDescription;
   }


   public void setStorageDescription( String aStorageDescription ) {
      iStorageDescription = aStorageDescription;
   }


   public FncAccountKey getFinancialAccount() {
      return iFinancialAccount;
   }


   public void setFinancialAccount( FncAccountKey account ) {
      iFinancialAccount = account;
   }


   public StockNoKey getStockNoKey() {
      return iStockNoKey;
   }


   public void setStockNoKey( StockNoKey aStockNoKey ) {
      iStockNoKey = aStockNoKey;
   }


   public RefInvClassKey getInventoryClass() {
      return iInventoryClass;
   }


   public void setInventoryClass( RefInvClassKey aInventoryClass ) {
      iInventoryClass = aInventoryClass;
   }


   public Optional<Boolean> isInDefaultPartGroup() {
      return Optional.ofNullable( iInDefaultPartGroup );
   }


   public void setInDefaultPartGroup( Boolean aInDefaultPartGroup ) {
      iInDefaultPartGroup = aInDefaultPartGroup;
   }


   public RefPartStatusKey getPartStatus() {
      return iStatus;
   }


   public void setPartStatus( RefPartStatusKey aStatus ) {
      iStatus = aStatus;
   }


   public String getShortDescription() {
      return iShortDescription;
   }


   public void setShortDescription( String aShortDescription ) {
      iShortDescription = aShortDescription;
   }


   public PartGroupKey getPartGroup() {
      return iPartGroup;
   }


   public Optional<Boolean> isPartGroupApproved() {
      return Optional.ofNullable( iPartGroupApproved );
   }


   public void setPartGroup( PartGroupKey aPartGroup, Boolean aPartGroupApproved ) {
      iPartGroup = aPartGroup;
      iPartGroupApproved = aPartGroupApproved;
   }


   public String getApplicabilityRange() {
      return iApplicabilityRange;
   }


   public void setApplicabilityRange( String aApplicabilityRange ) {
      iApplicabilityRange = aApplicabilityRange;
   }


   public ManufacturerKey getManufacturer() {
      return iManufacturer;
   }


   public void setManufacturer( ManufacturerKey aManufacturer ) {
      iManufacturer = aManufacturer;
   }


   /**
    * Returns the value of the iPartNoOem property.
    *
    * @return the value of the iPartNoOem property
    */
   public String getPartNoOem() {
      return iPartNoOem;
   }


   /**
    * Sets a new value for the iPartNoOem property.
    *
    * @param iPartNoOem
    *           the new value for the iPartNoOem property
    */
   public void setPartNoOem( String iPartNoOem ) {
      this.iPartNoOem = iPartNoOem;
   }


   public boolean isTool() {
      return tool;
   }


   public void setTool( boolean tool ) {
      this.tool = tool;
   }


   public RefQtyUnitKey getQtyUnitKey() {
      return iQtyUnitKey;
   }


   public void setQtyUnitKey( RefQtyUnitKey aQtyUnitKey ) {
      iQtyUnitKey = aQtyUnitKey;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public BigDecimal getAverageUnitPrice() {
      return iAverageUnitPrice;
   }


   public void setAverageUnitPrice( BigDecimal aAverageUnitPrice ) {
      iAverageUnitPrice = aAverageUnitPrice;
   }


   public RefFinanceTypeKey getFinancialType() {
      return iFinancialType;
   }


   public void setFinancialType( RefFinanceTypeKey aFinancialType ) {
      iFinancialType = aFinancialType;
   }


   public BigDecimal getQuantity() {
      return iQuantity;
   }


   public void setQuantity( BigDecimal aQuantity ) {
      iQuantity = aQuantity;
   }


   public BigDecimal getTotalValue() {
      return iTotalValue;
   }


   public void setTotalValue( BigDecimal aTotalValue ) {
      iTotalValue = aTotalValue;
   }


   public Optional<Boolean> isRepairable() {
      return Optional.ofNullable( iRepairable );
   }


   public void setRepairable( Boolean iRepairable ) {
      this.iRepairable = iRepairable;
   }

}
