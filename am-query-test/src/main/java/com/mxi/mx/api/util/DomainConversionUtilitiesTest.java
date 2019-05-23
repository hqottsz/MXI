package com.mxi.mx.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.api.configurationslots.ConfigurationSlotApiModel;
import com.mxi.mx.api.id.IdApiModel;
import com.mxi.mx.api.partgroups.AlternatePartApiModel;
import com.mxi.mx.api.partgroups.AlternatePartApiModelBuilder;
import com.mxi.mx.api.partgroups.PartGroupApiModel;
import com.mxi.mx.api.partgroups.PartGroupApiModelBuilder;
import com.mxi.mx.api.parts.PartApiModel;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.core.exception.ApiModelValidationException;
import com.mxi.mx.core.exception.ConfigurationSlotNotFoundException;
import com.mxi.mx.core.exception.InvalidReftermException;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPurchTypeKey;
import com.mxi.mx.core.services.part.ApplicabilityRange;
import com.mxi.mx.core.services.part.InvalidApplicabilityRangeException;
import com.mxi.mx.domain.Id;
import com.mxi.mx.domain.configslot.ConfigSlot;
import com.mxi.mx.domain.partgroup.PartGroup;
import com.mxi.mx.domain.partgroup.alternatepart.AlternatePart;


public class DomainConversionUtilitiesTest {

   private static final IdApiModel<PartGroupApiModel> EXISTING_PART_GROUP_ID =
         new IdApiModel<>( "00000000000000000000000000000001" );
   private static final String PART_GROUP_CODE = "DomainConversionUtilitiesTest";
   private static final String PART_GROUP_NAME = "DomainConversionUtilitiesTest Name";
   private static final Id<ConfigSlot> CONFIGURATION_SLOT_ID =
         new Id<>( "10000000000000000000000000000001" );
   private static final RefInvClassKey INVENTORY_CLASS_CODE = RefInvClassKey.BATCH;
   private static final BigDecimal QUANTITY = BigDecimal.valueOf( 15.67 );
   private static final RefPurchTypeKey PURCHASE_TYPE_KEY = new RefPurchTypeKey( 1, "OTHER" );
   private static final Boolean LINE_REPLACEABLE_UNIT = false;
   private static final ApplicabilityRange PART_GROUP_APPLICABILITY_RANGE =
         ApplicabilityRange.createRangeApplicableToEverything();
   private static final String OTHER_CONDITIONS = "Some Other Conditions";
   private static final Boolean MUST_REQUEST_SPECIFIC_PART = true;

   private static final IdApiModel<PartApiModel> PART =
         new IdApiModel<>( "00000000000000000000000000000000" );
   private static final Boolean STANDARD = true;
   private static final Boolean APPROVED = true;
   private static final Integer INTERCHANGEABILITY_ORDER = 1;
   private static final ApplicabilityRange ALTERNATE_PART_APPLICABILITY_RANGE =
         ApplicabilityRange.createRangeApplicableToEverything();
   private static final Boolean HAS_OTHER_CONDITIONS = true;
   private static final String BASELINE_CODE = "baseline";

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iDefaultAlternatePartApiModelBuilder =
            new AlternatePartApiModelBuilder().partId( PART ).standard( STANDARD )
                  .approved( APPROVED ).interchangeabilityOrder( INTERCHANGEABILITY_ORDER )
                  .applicabilityRange( ALTERNATE_PART_APPLICABILITY_RANGE.toString() )
                  .hasOtherConditions( HAS_OTHER_CONDITIONS ).baselineCode( BASELINE_CODE );
      iDefaultPartGroupApiModelBuilder = new PartGroupApiModelBuilder().code( PART_GROUP_CODE )
            .name( PART_GROUP_NAME ).quantity( QUANTITY )
            .inventoryClassCode( INVENTORY_CLASS_CODE.getCd() )
            .configurationSlotId(
                  new IdApiModel<ConfigurationSlotApiModel>( CONFIGURATION_SLOT_ID.toString() ) )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE.toString() )
            .purchaseTypeCode( PURCHASE_TYPE_KEY.getCd() )
            .lineReplaceableUnit( LINE_REPLACEABLE_UNIT ).otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Collections.<AlternatePartApiModel>emptyList() );
   }


   private AlternatePartApiModelBuilder iDefaultAlternatePartApiModelBuilder;

   private PartGroupApiModelBuilder iDefaultPartGroupApiModelBuilder;


   @Test
   public void allProperties_areValidWhenBuildDomainPartGroupForCreate() throws MxException {
      AlternatePartApiModel lAlternatePartModel = iDefaultAlternatePartApiModelBuilder
            .partId( new IdApiModel<PartApiModel>( "00000000000000000000000000000020" ) ).build();
      PartGroupApiModel lApiPartGroup = iDefaultPartGroupApiModelBuilder.applicabilityRange( "1" )
            .alternatePart( lAlternatePartModel ).build();
      PartGroup lDomainPartGroup =
            DomainConversionUtilities.buildDomainPartGroupForCreate( lApiPartGroup );
      assertFalse( lDomainPartGroup.getId().isPresent() );
      assertEquals( lApiPartGroup.getInventoryClassCode(),
            lDomainPartGroup.getInventoryClassKey().getCd() );
      assertEquals( lApiPartGroup.getPurchaseTypeCode(),
            lDomainPartGroup.getPurchaseTypeKey().get().getCd() );
      assertEquals( lApiPartGroup.getApplicabilityRange(),
            lDomainPartGroup.getApplicabilityRange().toString() );
      assertEquals( lApiPartGroup.getQuantity(), lDomainPartGroup.getQuantity() );
      assertEquals( lApiPartGroup.getCode(), lDomainPartGroup.getCode() );
      assertEquals( lApiPartGroup.getName(), lDomainPartGroup.getName() );
      assertEquals( lApiPartGroup.getConfigurationSlotId().toString(),
            lDomainPartGroup.getConfigurationSlotId().toString() );
      assertEquals( lApiPartGroup.getLineReplaceableUnit(),
            lDomainPartGroup.getLineReplaceableUnit() );
      assertEquals( lApiPartGroup.getMustRequestSpecificPart(),
            lDomainPartGroup.getMustRequestSpecificPart() );
      assertEquals( lApiPartGroup.getOtherConditions(),
            lDomainPartGroup.getOtherConditions().get() );
      assertEquals( 1, lApiPartGroup.getAlternateParts().size() );
      assertEquals( 1, lDomainPartGroup.getAlternateParts().size() );
      AlternatePartApiModel lApiAlternatePart = lApiPartGroup.getAlternateParts().get( 0 );
      AlternatePart lDomainAlternatePart = lDomainPartGroup.getAlternateParts().get( 0 );
      assertEquals( lApiAlternatePart.getPartId().toString(),
            lDomainAlternatePart.getPartId().toString() );
      assertEquals( lApiAlternatePart.getStandard(), lDomainAlternatePart.getStandard() );
      assertEquals( lApiAlternatePart.getApproved(), lDomainAlternatePart.getApproved() );
      assertEquals( lApiAlternatePart.getInterchangeabilityOrder().intValue(),
            lDomainAlternatePart.getInterchangeabilityOrder() );
      assertEquals( lApiAlternatePart.getApplicabilityRange(),
            lDomainAlternatePart.getApplicabilityRange().toString() );
      assertEquals( lApiAlternatePart.getHasOtherConditions(),
            lDomainAlternatePart.getHasOtherConditions() );
      assertEquals( lApiAlternatePart.getBaselineCode(),
            lDomainAlternatePart.getBaselineCode().get() );
   }


   @Test
   public void allProperties_areValidWhenBuildDomainPartGroupForUpdate() throws MxException {
      AlternatePartApiModel lAlternatePartModel = iDefaultAlternatePartApiModelBuilder
            .partId( new IdApiModel<PartApiModel>( "00000000000000000000000000000020" ) )
            .standard( true ).approved( false ).interchangeabilityOrder( 2 )
            .applicabilityRange( "00001-00003" ).hasOtherConditions( false )
            .baselineCode( "A base line code" ).build();
      PartGroupApiModel lApiPartGroup =
            iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID ).quantity( BigDecimal.ONE )
                  .inventoryClassCode( RefInvClassKey.SER.getCd() )
                  .lineReplaceableUnit( !LINE_REPLACEABLE_UNIT ).applicabilityRange( "250-999" )
                  .alternatePart( lAlternatePartModel ).build();
      PartGroup lDomainPartGroup =
            DomainConversionUtilities.buildDomainPartGroupForUpdate( lApiPartGroup );
      assertTrue( lDomainPartGroup.getId().isPresent() );
      assertEquals( lApiPartGroup.getId().toString(), lDomainPartGroup.getId().get().toString() );
      assertEquals( lApiPartGroup.getInventoryClassCode(),
            lDomainPartGroup.getInventoryClassKey().getCd() );
      assertEquals( lApiPartGroup.getPurchaseTypeCode(),
            lDomainPartGroup.getPurchaseTypeKey().get().getCd() );
      assertEquals( lApiPartGroup.getApplicabilityRange(),
            lDomainPartGroup.getApplicabilityRange().toString() );
      assertEquals( lApiPartGroup.getQuantity(), lDomainPartGroup.getQuantity() );
      assertEquals( lApiPartGroup.getCode(), lDomainPartGroup.getCode() );
      assertEquals( lApiPartGroup.getName(), lDomainPartGroup.getName() );
      assertEquals( lApiPartGroup.getConfigurationSlotId().toString(),
            lDomainPartGroup.getConfigurationSlotId().toString() );
      assertEquals( lApiPartGroup.getLineReplaceableUnit(),
            lDomainPartGroup.getLineReplaceableUnit() );
      assertEquals( lApiPartGroup.getMustRequestSpecificPart(),
            lDomainPartGroup.getMustRequestSpecificPart() );
      assertEquals( lApiPartGroup.getOtherConditions(),
            lDomainPartGroup.getOtherConditions().get() );
      assertEquals( 1, lApiPartGroup.getAlternateParts().size() );
      assertEquals( 1, lDomainPartGroup.getAlternateParts().size() );
      AlternatePartApiModel lApiAlternatePart = lApiPartGroup.getAlternateParts().get( 0 );
      AlternatePart lDomainAlternatePart = lDomainPartGroup.getAlternateParts().get( 0 );
      assertEquals( lApiAlternatePart.getPartId().toString(),
            lDomainAlternatePart.getPartId().toString() );
      assertEquals( lApiAlternatePart.getStandard(), lDomainAlternatePart.getStandard() );
      assertEquals( lApiAlternatePart.getApproved(), lDomainAlternatePart.getApproved() );
      assertEquals( lApiAlternatePart.getInterchangeabilityOrder().intValue(),
            lDomainAlternatePart.getInterchangeabilityOrder() );
      assertEquals( lApiAlternatePart.getApplicabilityRange(),
            lDomainAlternatePart.getApplicabilityRange().toString() );
      assertEquals( lApiAlternatePart.getHasOtherConditions(),
            lDomainAlternatePart.getHasOtherConditions() );
      assertEquals( lApiAlternatePart.getBaselineCode(),
            lDomainAlternatePart.getBaselineCode().get() );
   }


   @Test
   public void id_mustBeNullWhenBuildDomainPartGroupForCreate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder
            .id( new IdApiModel<PartGroupApiModel>( UUID.randomUUID() ) ).build();
      assertValidationErrorsForCreate( lModel, "Malformed Request: id must be null" );
   }


   @Test
   public void id_cannotBeNullWhenBuildDomainPartGroupForUpdate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( null ).build();
      assertValidationErrorsForUpdate( lModel, "Malformed Request: id may not be null" );
   }


   @Test
   public void code_cannotBeNullWhenBuildDomainPartGroupForCreate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.code( null ).build();
      assertValidationErrorsForCreate( lModel, "Malformed Request: code may not be null" );
   }


   @Test
   public void code_cannotBeNullWhenBuildDomainPartGroupForUpdate() throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID ).code( null ).build();
      assertValidationErrorsForUpdate( lModel, "Malformed Request: code may not be null" );
   }


   @Test
   public void configurationSlotId_cannotBeNullWhenBuildDomainPartGroupForCreate()
         throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.configurationSlotId( null ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: configurationSlotId may not be null" );
   }


   @Test
   public void configurationSlotId_cannotBeNullWhenBuildDomainPartGroupForUpdate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .configurationSlotId( null ).build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: configurationSlotId may not be null" );
   }


   @Test
   public void inventoryClassCode_cannotBeNullWhenBuildDomainPartGroupForCreate()
         throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.inventoryClassCode( null ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: inventoryClassCode may not be null" );
   }


   @Test
   public void inventoryClassCode_cannotBeNullWhenBuildDomainPartGroupForUpdate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .inventoryClassCode( null ).build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: inventoryClassCode may not be null" );
   }


   @Test( expected = InvalidReftermException.class )
   public void inventoryClassCode_mustBeValidForCreate() throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.inventoryClassCode( "Not_A_Valid_RefTerm" ).build();
      DomainConversionUtilities.buildDomainPartGroupForCreate( lModel );
   }


   @Test( expected = InvalidReftermException.class )
   public void inventoryClassCode_mustBeValidForUpdate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .inventoryClassCode( "Not_A_Valid_RefTerm" ).build();
      DomainConversionUtilities.buildDomainPartGroupForUpdate( lModel );
   }


   @Test
   public void lineReplaceableUnit_cannotBeNullWhenBuildDomainPartGroupForCreate()
         throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.lineReplaceableUnit( null ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: lineReplaceableUnit may not be null" );
   }


   @Test
   public void lineReplaceableUnit_cannotBeNullWhenBuildDomainPartGroupForUpdate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .lineReplaceableUnit( null ).build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: lineReplaceableUnit may not be null" );
   }


   @Test
   public void mustRequestSpecificPart_cannotBeNullWhenBuildDomainPartGroupForCreate()
         throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.mustRequestSpecificPart( null ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: mustRequestSpecificPart may not be null" );
   }


   @Test
   public void mustRequestSpecificPart_cannotBeNullWhenBuildDomainPartGroupForUpdate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .mustRequestSpecificPart( null ).build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: mustRequestSpecificPart may not be null" );
   }


   @Test
   public void name_cannotBeNullWhenBuildDomainPartGroupForCreate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.name( null ).build();
      assertValidationErrorsForCreate( lModel, "Malformed Request: name may not be null" );
   }


   @Test
   public void name_cannotBeNullWhenBuildDomainPartGroupForUpdate() throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID ).name( null ).build();
      assertValidationErrorsForUpdate( lModel, "Malformed Request: name may not be null" );
   }


   @Test
   public void quantity_cannotBeNullWhenBuildDomainPartGroupForCreate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.quantity( null ).build();
      assertValidationErrorsForCreate( lModel, "Malformed Request: quantity may not be null" );
   }


   @Test
   public void quantity_cannotBeNullWhenBuildDomainPartGroupForUpdate() throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID ).quantity( null ).build();
      assertValidationErrorsForUpdate( lModel, "Malformed Request: quantity may not be null" );
   }


   @Test
   public void alternateParts_applicabilityRange_canBeNullForCreate() throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder
                  .alternatePart(
                        iDefaultAlternatePartApiModelBuilder.applicabilityRange( null ).build() )
                  .build();
      DomainConversionUtilities.buildDomainPartGroupForCreate( lModel );
   }


   @Test
   public void alternateParts_applicabilityRange_canBeNullForUpdate() throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
                  .alternatePart(
                        iDefaultAlternatePartApiModelBuilder.applicabilityRange( null ).build() )
                  .build();
      DomainConversionUtilities.buildDomainPartGroupForUpdate( lModel );
   }


   @Test
   public void alternateParts_cannotBeNullWhenBuildDomainPartGroupForCreate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.alternateParts( null ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: alternateParts may not be null" );
   }


   @Test
   public void alternateParts_cannotBeNullWhenBuildDomainPartGroupForUpdate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .alternateParts( null ).build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: alternateParts may not be null" );
   }


   @Test
   public void alternateParts_partId_cannotBeNullWhenBuildDomainPartGroupForCreate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder
            .alternatePart( iDefaultAlternatePartApiModelBuilder.partId( null ).build() ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: alternateParts[0].partId may not be null" );
   }


   @Test
   public void alternateParts_partId_cannotBeNullWhenBuildDomainPartGroupForUpdate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .alternatePart( iDefaultAlternatePartApiModelBuilder.partId( null ).build() ).build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: alternateParts[0].partId may not be null" );
   }


   @Test
   public void
         alternateParts_interchangeabilityOrder_cannotBeNullWhenBuildDomainPartGroupForCreate()
               throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder
            .alternatePart(
                  iDefaultAlternatePartApiModelBuilder.interchangeabilityOrder( null ).build() )
            .build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: alternateParts[0].interchangeabilityOrder may not be null" );
   }


   @Test
   public void
         alternateParts_interchangeabilityOrder_cannotBeNullWhenBuildDomainPartGroupForUpdate()
               throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .alternatePart(
                  iDefaultAlternatePartApiModelBuilder.interchangeabilityOrder( null ).build() )
            .build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: alternateParts[0].interchangeabilityOrder may not be null" );
   }


   @Test
   public void alternateParts_standard_cannotBeNullWhenBuildDomainPartGroupForCreate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder
            .alternatePart( iDefaultAlternatePartApiModelBuilder.standard( null ).build() ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: alternateParts[0].standard may not be null" );
   }


   @Test
   public void alternateParts_standard_cannotBeNullWhenBuildDomainPartGroupForUpdate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .alternatePart( iDefaultAlternatePartApiModelBuilder.standard( null ).build() ).build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: alternateParts[0].standard may not be null" );
   }


   @Test
   public void alternateParts_approved_cannotBeNullWhenBuildDomainPartGroupForCreate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder
            .alternatePart( iDefaultAlternatePartApiModelBuilder.approved( null ).build() ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: alternateParts[0].approved may not be null" );
   }


   @Test
   public void alternateParts_approved_cannotBeNullWhenBuildDomainPartGroupForUpdate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .alternatePart( iDefaultAlternatePartApiModelBuilder.approved( null ).build() ).build();
      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: alternateParts[0].approved may not be null" );
   }


   @Test
   public void alternateParts_hasOtherConditions_cannotBeNullWhenBuildDomainPartGroupForCreate()
         throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder
                  .alternatePart(
                        iDefaultAlternatePartApiModelBuilder.hasOtherConditions( null ).build() )
                  .build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: alternateParts[0].hasOtherConditions may not be null" );
   }


   @Test
   public void alternateParts_hasOtherConditions_cannotBeNullWhenBuildDomainPartGroupForUpdate()
         throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
                  .alternatePart(
                        iDefaultAlternatePartApiModelBuilder.hasOtherConditions( null ).build() )
                  .build();

      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: alternateParts[0].hasOtherConditions may not be null" );
   }


   @Test
   public void alternatePart_cannotBeNullWhenBuildDomainPartGroupForCreate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.alternatePart( null ).build();
      assertValidationErrorsForCreate( lModel,
            "Malformed Request: alternateParts {com.mxi.mx.api.validation.NoNullElements.message}" );
   }


   @Test
   public void alternatePart_cannotBeNullWhenBuildDomainPartGroupForUpdate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .alternatePart( null ).build();

      assertValidationErrorsForUpdate( lModel,
            "Malformed Request: alternateParts {com.mxi.mx.api.validation.NoNullElements.message}" );
   }


   @Test( expected = InvalidReftermException.class )
   public void purchaseTypeCode_mustBeValidIfNotNullForCreate() throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.purchaseTypeCode( "JUNK" ).build();
      DomainConversionUtilities.buildDomainPartGroupForCreate( lModel );
   }


   @Test( expected = InvalidReftermException.class )
   public void purchaseTypeCode_mustBeValidIfNotNullForUpdate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .purchaseTypeCode( "JUNK" ).build();
      DomainConversionUtilities.buildDomainPartGroupForUpdate( lModel );
   }


   @Test
   public void purchaseTypeCode_canBeNullForCreate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.purchaseTypeCode( null ).build();
      DomainConversionUtilities.buildDomainPartGroupForCreate( lModel );
   }


   @Test
   public void purchaseTypeCode_canBeNullForUpdate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .purchaseTypeCode( null ).build();
      DomainConversionUtilities.buildDomainPartGroupForUpdate( lModel );
   }


   @Test
   public void applicabilityRange_canBeNullForCreate() throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.applicabilityRange( null ).build();
      DomainConversionUtilities.buildDomainPartGroupForCreate( lModel );
   }


   @Test
   public void applicabilityRange_canBeNullForUpdate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .applicabilityRange( null ).build();
      DomainConversionUtilities.buildDomainPartGroupForUpdate( lModel );
   }


   @Test( expected = InvalidApplicabilityRangeException.class )
   public void applicabilityRange_mustFollowApplicabilityRangeExpressionRulesForCreate()
         throws MxException {
      PartGroupApiModel lModel =
            iDefaultPartGroupApiModelBuilder.applicabilityRange( "1,two,3-4,five-six" ).build();
      DomainConversionUtilities.buildDomainPartGroupForCreate( lModel );
   }


   @Test( expected = InvalidApplicabilityRangeException.class )
   public void applicabilityRange_mustFollowApplicabilityRangeExpressionRulesForUpdate()
         throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .applicabilityRange( "1,two,3-4,five-six" ).build();
      DomainConversionUtilities.buildDomainPartGroupForUpdate( lModel );
   }


   @Test( expected = ConfigurationSlotNotFoundException.class )
   public void configurationSlotId_CannotBeNullForCreate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder
            .configurationSlotId( new IdApiModel<ConfigurationSlotApiModel>( UUID.randomUUID() ) )
            .build();
      DomainConversionUtilities.buildDomainPartGroupForCreate( lModel );
   }


   @Test( expected = ConfigurationSlotNotFoundException.class )
   public void configurationSlotId_CannotBeNullForUpdate() throws MxException {
      PartGroupApiModel lModel = iDefaultPartGroupApiModelBuilder.id( EXISTING_PART_GROUP_ID )
            .configurationSlotId( new IdApiModel<ConfigurationSlotApiModel>( UUID.randomUUID() ) )
            .build();
      DomainConversionUtilities.buildDomainPartGroupForUpdate( lModel );
   }


   private void assertValidationErrorsForUpdate( PartGroupApiModel aModel,
         String aExpectedValidationErrors ) throws MxException {
      try {
         DomainConversionUtilities.buildDomainPartGroupForUpdate( aModel );
         fail( "Expected ApiModelValidationException" );
      } catch ( ApiModelValidationException ex ) {
         String lActualValidationErrors = ex.getData().getValidationErrors();
         assertEquals( aExpectedValidationErrors, lActualValidationErrors );
      }
   }


   private void assertValidationErrorsForCreate( PartGroupApiModel aModel,
         String aExpectedValidationErrors ) throws MxException {
      try {
         DomainConversionUtilities.buildDomainPartGroupForCreate( aModel );
         fail( "Expected ApiModelValidationException" );
      } catch ( ApiModelValidationException ex ) {
         String lActualValidationErrors = ex.getData().getValidationErrors();
         assertEquals( aExpectedValidationErrors, lActualValidationErrors );
      }
   }
}
