package com.mxi.mx.api.partgroups;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.api.configurationslots.ConfigurationSlotApiModel;
import com.mxi.mx.api.id.IdApiModel;


public class PartGroupApiModelBuilder {

   private IdApiModel<PartGroupApiModel> iId;
   private String iCode;
   private String iName;
   private IdApiModel<ConfigurationSlotApiModel> iConfigurationSlotId;
   private String iInventoryClassCode;
   private BigDecimal iQuantity;
   private String iPurchaseTypeCode;
   private Boolean iLineReplaceableUnit;
   private String iApplicabilityRange;
   private String iOtherConditions;
   private Boolean iMustRequestSpecificPart;
   private List<AlternatePartApiModel> iAlternateParts;


   public PartGroupApiModelBuilder() {
   }


   public PartGroupApiModelBuilder(PartGroupApiModel aPartGroup) {
      iId = aPartGroup.getId();
      iCode = aPartGroup.getCode();
      iName = aPartGroup.getName();
      iConfigurationSlotId = aPartGroup.getConfigurationSlotId();
      iInventoryClassCode = aPartGroup.getInventoryClassCode();
      iQuantity = aPartGroup.getQuantity();
      iPurchaseTypeCode = aPartGroup.getPurchaseTypeCode();
      iLineReplaceableUnit = aPartGroup.getLineReplaceableUnit();
      iApplicabilityRange = aPartGroup.getApplicabilityRange();
      iOtherConditions = aPartGroup.getOtherConditions();
      iMustRequestSpecificPart = aPartGroup.getMustRequestSpecificPart();
      iAlternateParts = aPartGroup.getAlternateParts();
   }


   public PartGroupApiModelBuilder id( IdApiModel<PartGroupApiModel> aId ) {
      iId = aId;
      return this;
   }


   public PartGroupApiModelBuilder code( String aCode ) {
      iCode = aCode;
      return this;
   }


   public PartGroupApiModelBuilder name( String aName ) {
      iName = aName;
      return this;
   }


   public PartGroupApiModelBuilder
         configurationSlotId( IdApiModel<ConfigurationSlotApiModel> aConfigurationSlotId ) {
      iConfigurationSlotId = aConfigurationSlotId;
      return this;
   }


   public PartGroupApiModelBuilder inventoryClassCode( String aInventoryClassCode ) {
      iInventoryClassCode = aInventoryClassCode;
      return this;
   }


   public PartGroupApiModelBuilder quantity( BigDecimal aQuantity ) {
      iQuantity = aQuantity;
      return this;
   }


   public PartGroupApiModelBuilder purchaseTypeCode( String aPurchaseTypeCode ) {
      iPurchaseTypeCode = aPurchaseTypeCode;
      return this;
   }


   public PartGroupApiModelBuilder lineReplaceableUnit( Boolean aLineReplaceableUnit ) {
      iLineReplaceableUnit = aLineReplaceableUnit;
      return this;
   }


   public PartGroupApiModelBuilder applicabilityRange( String aApplicabilityRange ) {
      iApplicabilityRange = aApplicabilityRange;
      return this;
   }


   public PartGroupApiModelBuilder otherConditions( String aOtherConditions ) {
      iOtherConditions = aOtherConditions;
      return this;
   }


   public PartGroupApiModelBuilder mustRequestSpecificPart( Boolean aMustRequestSpecificPart ) {
      iMustRequestSpecificPart = aMustRequestSpecificPart;
      return this;
   }


   public PartGroupApiModelBuilder alternatePart( AlternatePartApiModel aAlternatePart ) {
      iAlternateParts.add( aAlternatePart );
      return this;
   }


   public PartGroupApiModelBuilder alternateParts( List<AlternatePartApiModel> aAlternateParts ) {
      if ( aAlternateParts == null ) {
         // If AlternateParts is explicitly set to be null, respect that
         iAlternateParts = null;
      } else {
         iAlternateParts = new ArrayList<>( aAlternateParts );
      }
      return this;
   }


   public PartGroupApiModel build() {
      PartGroupApiModel lPartGroupApiModel = new PartGroupApiModel();
      lPartGroupApiModel.setId( iId );
      lPartGroupApiModel.setCode( iCode );
      lPartGroupApiModel.setName( iName );
      lPartGroupApiModel.setConfigurationSlotId( iConfigurationSlotId );
      lPartGroupApiModel.setInventoryClassCode( iInventoryClassCode );
      lPartGroupApiModel.setQuantity( iQuantity );
      lPartGroupApiModel.setPurchaseTypeCode( iPurchaseTypeCode );
      lPartGroupApiModel.setLineReplaceableUnit( iLineReplaceableUnit );
      lPartGroupApiModel.setApplicabilityRange( iApplicabilityRange );
      lPartGroupApiModel.setOtherConditions( iOtherConditions );
      lPartGroupApiModel.setMustRequestSpecificPart( iMustRequestSpecificPart );
      lPartGroupApiModel.setAlternateParts( iAlternateParts );
      return lPartGroupApiModel;
   }
}
