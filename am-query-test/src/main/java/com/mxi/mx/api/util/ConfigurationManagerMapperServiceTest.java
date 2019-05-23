package com.mxi.mx.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.api.configurationmanager.publish.model.AlternatePartSet;
import com.mxi.mx.api.configurationmanager.publish.model.AlternatePartTaskIncompatibility;
import com.mxi.mx.api.configurationmanager.publish.model.BadRequestResponse;
import com.mxi.mx.api.configurationmanager.publish.model.BaseUnprocessableEntityResponse;
import com.mxi.mx.api.configurationmanager.publish.model.BatchAlternatePart;
import com.mxi.mx.api.configurationmanager.publish.model.BatchPartGroup;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorAlternatePartHasIncompatibilityRule;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorAlternatePartHasIncompatibilityRuleData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorAlternatePartHasLockedTaskIncompatibility;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorAlternatePartHasLockedTaskIncompatibilityData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorCode;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorConfigSlotNotFound;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorConfigSlotNotFoundErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorCreationOfTrackedPartGroupIsNotSupported;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorIncompatiblePartNotTRK;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorIncompatiblePartNotTRKData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorInstalledInventoryApplicabilityInvalidated;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorInstalledInventoryApplicabilityInvalidatedErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorInvalidAlternatePartApplicabilityRange;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorInvalidAlternatePartApplicabilityRangeErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorInvalidPartGroupApplicabilityRange;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorInvalidPartGroupApplicabilityRangeErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorInventoryClassMismatch;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorInventoryClassMismatchErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorMustRequestSpecificPartFailed;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorNoValidTaskDefinitionForTaskIncompatibility;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorNoValidTaskDefinitionForTaskIncompatibilityData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorObsoletePart;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorObsoletePartErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorPartInUse;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorPartInUseErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorPartsNotFound;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorPartsNotFoundErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorReadOnlyProperty;
import com.mxi.mx.api.configurationmanager.publish.model.ErrorReadOnlyPropertyErrorData;
import com.mxi.mx.api.configurationmanager.publish.model.InternalServerErrorResponse;
import com.mxi.mx.api.configurationmanager.publish.model.InvalidPart;
import com.mxi.mx.api.configurationmanager.publish.model.PartWithInvalidApplicabilityRange;
import com.mxi.mx.api.configurationmanager.publish.model.PartWithInventoryClassMismatch;
import com.mxi.mx.api.configurationmanager.publish.model.SerializedAlternatePart;
import com.mxi.mx.api.configurationmanager.publish.model.SerializedPartGroup;
import com.mxi.mx.api.configurationmanager.publish.model.SuccessfulResponse;
import com.mxi.mx.api.configurationmanager.publish.model.TrackedAlternatePart;
import com.mxi.mx.api.configurationmanager.publish.model.TrackedPartGroup;
import com.mxi.mx.api.configurationmanager.publish.model.TrackedPartIncompatibility;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.exception.ConfigurationSlotNotFoundException;
import com.mxi.mx.core.exception.InputCodeException;
import com.mxi.mx.core.exception.KeyConversionException;
import com.mxi.mx.core.exception.PartGroupNotFoundException;
import com.mxi.mx.core.exception.ReadOnlyPropertyException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPurchTypeKey;
import com.mxi.mx.core.services.SystemService;
import com.mxi.mx.core.services.bom.ConfigSlotService;
import com.mxi.mx.core.services.bom.InstalledInventoryNotApplicableException;
import com.mxi.mx.core.services.bom.PartGroupService;
import com.mxi.mx.core.services.bom.PartRequirementNotUsingSpecificPartException;
import com.mxi.mx.core.services.ietm.IetmAssemblyMapDoesNotExistException;
import com.mxi.mx.core.services.inventory.exception.InvalidInventoryClassException;
import com.mxi.mx.core.services.inventory.exception.InventoryClassMismatchException;
import com.mxi.mx.core.services.part.ApplicabilityRange;
import com.mxi.mx.core.services.part.CompatibilityRuleExistsException;
import com.mxi.mx.core.services.part.InvalidPartStatusException;
import com.mxi.mx.core.services.part.PartNoBomPartInUseException;
import com.mxi.mx.core.services.part.PartNoHasPartReqException;
import com.mxi.mx.core.services.part.PartNoService;
import com.mxi.mx.core.services.taskdefn.TaskDefnService;
import com.mxi.mx.core.services.taskdefn.exception.AlternatePartHasLockedTaskIncompatibilityException;
import com.mxi.mx.domain.Id;
import com.mxi.mx.domain.configslot.ConfigSlot;
import com.mxi.mx.domain.part.Part;
import com.mxi.mx.domain.part.Part.Key;
import com.mxi.mx.domain.partgroup.PartGroup;
import com.mxi.mx.domain.partgroup.alternatepart.AlternatePart;
import com.mxi.mx.persistence.uuid.UuidUtils;


public class ConfigurationManagerMapperServiceTest {

   /** Human Resource */
   private static final HumanResourceKey AUTHORIZING_HR = new HumanResourceKey( 4650, 1 );

   /** Assembly */
   private static final String ASSEMBLY_CODE = "ACFT_CD1";
   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 4650, "ACFT_CD1" );

   /** ATA */
   private static final String ROOT_ATA_CODE = "ROOT";
   private static final String SYSTEM_ATA_CODE = "SYS-APU";
   private static final String ANOTHER_SYSTEM_ATA_CODE = "SYS-1";
   private static final String PARENT_TRACKED_ATA_CODE = "PARENT";
   private static final String TRACKED_NO_PARENT_ATA_CODE = "TRACKED_NO_PARENT";
   private static final String SYSTEM_CONFIGURATION_SLOT_ID = "10000000000000000000000000000001";
   private static final String TRACKED_NO_PARENT_CONFIGURATION_SLOT_ID =
         "10000000000000000000000000000004";
   private static final Id<ConfigSlot> SYSTEM_CONFIGURATION_SLOT =
         new Id<>( SYSTEM_CONFIGURATION_SLOT_ID );
   private static final Id<ConfigSlot> PARENT_TRACKED_CONFIGURATION_SLOT =
         new Id<>( "10000000000000000000000000000002" );

   /** Part Group */
   private static final Id<PartGroup> TRACKED_PART_GROUP_WITH_NO_PARENT_ID =
         new Id<>( "20000000000000000000000000000004" );
   private static final Id<PartGroup> BATCH_PART_GROUP_WITH_NO_PARENT_ID =
         new Id<>( "20000000000000000000000000000003" );
   private static final String TRACKED_PART_GROUP_WITH_PARENT = "TRK_WITH_PARENT";
   private static final String TRACKED_PART_GROUP_WITH_NO_PARENT = "TRK_NO_PARENT";
   private static final String SERIALIZED_PART_GROUP_WITH_PARENT = "SER_WITH_PARENT";
   private static final String SERIALIZED_PART_GROUP_WITH_NO_PARENT = "SER_NO_PARENT";
   private static final String BATCH_PART_GROUP_WITH_PARENT = "BATCH_WITH_PARENT";
   private static final String BATCH_PART_GROUP_WITH_NO_PARENT = "BATCH_NO_PARENT";
   private static final String TRACKED_PART_GROUP_WITH_NO_PARENT_NAME = "Tracked with no parent";
   private static final String BATCH_PART_GROUP_WITH_NO_PARENT_NAME = "Batch with no parent";
   private static final String TRACKED_PART_GROUP_CODE_NEW = "TrackedPartGroupCode";
   private static final String SERIALIZED_PART_GROUP_CODE_NEW = "SerializedPartGroupCode";
   private static final String BATCH_PART_GROUP_CODE_NEW = "BatchPartGroupCode";

   /** Part */
   private static final String PART_MANUFACTURER = "ABC";
   private static final String BATCH_PART_NO_OEM = "BATCH_PART_NUMBER_1";
   private static final Part SER_PART_NUMBER_1 =
         new Part( new Id<Part>( "00000000000000000000000000000001" ), "SER_PART_NUMBER_1",
               PART_MANUFACTURER, RefInvClassKey.SER_CD );
   private static final Part BATCH_PART_NUMBER_1 =
         new Part( new Id<Part>( "00000000000000000000000000000002" ), BATCH_PART_NO_OEM,
               PART_MANUFACTURER, RefInvClassKey.BATCH_CD );
   private static final Part TRACKED_PART_NUMBER_1 =
         new Part( new Id<Part>( "00000000000000000000000000000003" ), "TRACKED_PART_NUMBER_1",
               PART_MANUFACTURER, RefInvClassKey.TRK_CD );

   /** Invalid Data */
   private static final String NONEXISTENT_CODE = "does-not-exist";
   private static final String INVALID_APPLICABILITY_RANGE = "1-23";
   private static final String NONEXISTENT_PART_NUMBER = "NONEXISTENT_PART_NUMBER";

   /** Task Incompatibility */
   private static final String TASK_CODE = "SYS-REP-1";
   private static final String INCOMPATIBLE_WHEN_COMPLETE = "COMPLETE";
   private static final AlternatePartTaskIncompatibility TASK_INCOMPATIBILITY =
         new AlternatePartTaskIncompatibility().taskCode( TASK_CODE ).ataCode( SYSTEM_ATA_CODE )
               .incompatibleWhen( INCOMPATIBLE_WHEN_COMPLETE );

   /** HTTP Status Code */
   private static final int HTTP_UNPROCESSABLE_ENTITY_STATUS = 422;

   /** Read-only Properties */
   private static final Collection<String> READONLY_PROPERTIES =
         new HashSet<>( Arrays.asList( "assemblyCode", "ataCode" ) );

   private BatchPartGroup iBatchPartGroup;
   private BatchAlternatePart iBatchAlternatePart;
   private SerializedAlternatePart iSerializedAlternatePart;
   private SerializedPartGroup iSerializedPartGroup;
   private TrackedPartGroup iTrackedPartGroup;
   private TrackedAlternatePart iTrackedAlternatePart;
   private ConfigurationManagerMapperService iConfigurationManagerMapperServiceWithMockPartGroupService;

   @Mock
   SystemService iMockSystemService;

   @Mock
   PartGroupService iMockPartGroupService;

   @Mock
   PartNoService iMockPartNoService;

   @Mock
   TaskDefnService iMockTaskDefnService;

   @Mock
   ConfigSlotService iMockConfigSlotService;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      MockitoAnnotations.initMocks( this );

      ConfigSlot lSystemConfigurationSlot = new ConfigSlot.Builder()
            .alternateKey( UuidUtils.fromHexString( SYSTEM_CONFIGURATION_SLOT_ID ) )
            .ataCode( SYSTEM_ATA_CODE ).classKey( RefBOMClassKey.SYS ).name( "SYS-APU-NAME" )
            .key( new ConfigSlotKey( 4650, ASSEMBLY_CODE, 1 ) ).build();
      ConfigSlot lTrackedNoParentConfigurationSlot = new ConfigSlot.Builder()
            .alternateKey( UuidUtils.fromHexString( TRACKED_NO_PARENT_CONFIGURATION_SLOT_ID ) )
            .ataCode( TRACKED_NO_PARENT_ATA_CODE ).classKey( RefBOMClassKey.TRK )
            .name( "TRACKED_NO_PARENT-NAME" ).key( new ConfigSlotKey( 4650, ASSEMBLY_CODE, 4 ) )
            .build();

      when( iMockPartNoService.findByNaturalKey( BATCH_PART_NO_OEM, PART_MANUFACTURER ) )
            .thenReturn( Optional.of( BATCH_PART_NUMBER_1 ) );
      when( iMockConfigSlotService.findByNaturalKey( ASSEMBLY_CODE, SYSTEM_ATA_CODE ) )
            .thenReturn( Optional.of( lSystemConfigurationSlot ) );
      when( iMockConfigSlotService.findByNaturalKey( ASSEMBLY_CODE, TRACKED_NO_PARENT_ATA_CODE ) )
            .thenReturn( Optional.of( lTrackedNoParentConfigurationSlot ) );

      iConfigurationManagerMapperServiceWithMockPartGroupService =
            new ConfigurationManagerMapperService( iMockPartGroupService, iMockPartNoService,
                  iMockTaskDefnService, iMockConfigSlotService );
   }


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iBatchPartGroup = ( BatchPartGroup ) new BatchPartGroup().quantity( new BigDecimal( "1.2" ) )
            .code( BATCH_PART_GROUP_CODE_NEW ).name( "batch part group new" )
            .ataCode( SYSTEM_ATA_CODE )
            .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything().toString() )
            .assemblyCode( ASSEMBLY_CODE ).mustRequestSpecificPart( true )
            .standardAlternatePartManufacturer( BATCH_PART_NUMBER_1.getManufacturerCode() )
            .standardAlternatePartNumber( BATCH_PART_NUMBER_1.getOemPartNumber() )
            .lineReplaceableUnit( true );
      iBatchAlternatePart = ( BatchAlternatePart ) new BatchAlternatePart()
            .partManufacturer( BATCH_PART_NUMBER_1.getManufacturerCode() )
            .partNumber( BATCH_PART_NUMBER_1.getOemPartNumber() )
            .applicabilityRange(
                  ApplicabilityRange.createRangeApplicableToEverything().getExpression() )
            .hasOtherConditions( true ).approved( true );
      iBatchPartGroup.addAlternatePartsItem( iBatchAlternatePart );
      iSerializedAlternatePart = ( SerializedAlternatePart ) new SerializedAlternatePart()
            .partManufacturer( SER_PART_NUMBER_1.getManufacturerCode() )
            .partNumber( SER_PART_NUMBER_1.getOemPartNumber() )
            .applicabilityRange(
                  ApplicabilityRange.createRangeApplicableToEverything().getExpression() )
            .hasOtherConditions( true ).approved( true );
      iSerializedPartGroup = ( SerializedPartGroup ) new SerializedPartGroup().quantity( 2 )
            .code( SERIALIZED_PART_GROUP_CODE_NEW ).name( "serialized part group new" )
            .ataCode( SYSTEM_ATA_CODE )
            .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything().toString() )
            .assemblyCode( ASSEMBLY_CODE ).mustRequestSpecificPart( true )
            .standardAlternatePartManufacturer( SER_PART_NUMBER_1.getManufacturerCode() )
            .standardAlternatePartNumber( SER_PART_NUMBER_1.getOemPartNumber() )
            .lineReplaceableUnit( true );
      iSerializedPartGroup.addAlternatePartsItem( iSerializedAlternatePart );
      iTrackedAlternatePart =
            ( TrackedAlternatePart ) new TrackedAlternatePart().interchangeabilityOrder( 9 )
                  .partManufacturer( TRACKED_PART_NUMBER_1.getManufacturerCode() )
                  .partNumber( TRACKED_PART_NUMBER_1.getOemPartNumber() )
                  .applicabilityRange(
                        ApplicabilityRange.createRangeApplicableToEverything().getExpression() )
                  .hasOtherConditions( true ).approved( true );
      iTrackedPartGroup = ( TrackedPartGroup ) new TrackedPartGroup()
            .code( TRACKED_PART_GROUP_CODE_NEW ).name( "New tracked part group" )
            .ataCode( TRACKED_NO_PARENT_ATA_CODE )
            .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything().toString() )
            .assemblyCode( ASSEMBLY_CODE ).mustRequestSpecificPart( true )
            .standardAlternatePartManufacturer( TRACKED_PART_NUMBER_1.getManufacturerCode() )
            .standardAlternatePartNumber( TRACKED_PART_NUMBER_1.getOemPartNumber() )
            .lineReplaceableUnit( true );
      iTrackedPartGroup.addAlternatePartsItem( iTrackedAlternatePart );

      new ConfigSlotBuilder( ROOT_ATA_CODE ).withRootAssembly( ASSEMBLY_KEY ).withName( "ROOT" )
            .withClass( RefBOMClassKey.ROOT ).build();
   }


   @Test( expected = NullPointerException.class )
   public void publish_nullPartGroup_throwsException() {
      new ConfigurationManagerMapperService().publish( null, AUTHORIZING_HR );
   }


   @Test( expected = NullPointerException.class )
   public void publish_nullUser_throwsException() {
      new ConfigurationManagerMapperService().publish( iBatchPartGroup, null );
   }


   @Test
   public void batchPartGroup_publishNew() throws Exception {
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            lResponse );
   }


   @Test
   public void batchPartGroup_publishNew_underRootConfigSlot() throws Exception {
      iBatchPartGroup.ataCode( ROOT_ATA_CODE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            lResponse );
   }


   @Test
   public void batchPartGroup_publishExisting() throws Exception {
      iBatchPartGroup.code( BATCH_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            lResponse );
   }


   @Test
   public void serializedPartGroup_publishNew() throws Exception {
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), lResponse );
   }


   @Test
   public void serializedPartGroup_publishNew_underRootConfigSlot() throws Exception {
      iSerializedPartGroup.ataCode( ROOT_ATA_CODE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), lResponse );
   }


   @Test
   public void serializedPartGroup_publishExisting() throws Exception {
      iSerializedPartGroup.code( SERIALIZED_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), lResponse );
   }


   @Test
   public void trackedPartGroup_publishNew_returnsErrorCreationOfTrackedPartGroupIsNotSupported()
         throws Exception {
      when( iMockPartGroupService.find( iTrackedPartGroup.getAssemblyCode(),
            iTrackedPartGroup.getCode() ) ).thenReturn( Optional.<PartGroup>empty() );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iTrackedPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );

      assertTrue(
            "Expected ErrorCreationOfTrackedPartGroupIsNotSupported but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorCreationOfTrackedPartGroupIsNotSupported );
      ErrorCreationOfTrackedPartGroupIsNotSupported lError =
            ( ErrorCreationOfTrackedPartGroupIsNotSupported ) lResponse;
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.CREATION_OF_TRACKED_PART_GROUP_IS_NOT_SUPPORTED.getCode(), null );
   }


   @Test
   public void trackedPartGroup_publishExisting() throws Exception {
      AlternatePartSet<TrackedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      iTrackedAlternatePart.addPartIncompatibility(
            new TrackedPartIncompatibility().partGroupCode( TRACKED_PART_GROUP_WITH_NO_PARENT )
                  .partNumber( TRACKED_PART_NUMBER_1.getOemPartNumber() )
                  .manufacturer( TRACKED_PART_NUMBER_1.getManufacturerCode() ) );
      lAlternatePartSet.add( iTrackedAlternatePart );
      iTrackedPartGroup.alternateParts( lAlternatePartSet )
            .code( TRACKED_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            lResponse );
   }


   @Test
   public void
         trackedPartGroup_publishChangingAtaCodeToOtherSystemAtaCode_returnsErrorReadOnlyProperty() {
      /*
       * The part group TRACKED_PART_GROUP_WITH_NO_PARENT is defined in XML data and has no parent
       * part group (nesting under a system "SYS-APU"). This changes the ATA system (configuration
       * slot) to be another existing ATA system "SYS-1" which is not possible.
       */
      iTrackedPartGroup.code( TRACKED_PART_GROUP_WITH_NO_PARENT )
            .ataCode( ANOTHER_SYSTEM_ATA_CODE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertReadOnlyPropertyErrorBody( lResponse, iTrackedPartGroup.getAssemblyCode(),
            iTrackedPartGroup.getCode(), READONLY_PROPERTIES );
   }


   @Test
   public void
         trackedPartGroup_publishChangingAtaCodeToOtherTrackedAtaCode_returnsErrorReadOnlyProperty() {
      /*
       * The part group TRACKED_PART_GROUP_WITH_PARENT is defined in XML data and has an existing
       * parent part group (code "PARENT"). This tests tries to change the parent to be an existing
       * tracked part group (code "TRK_WITH_NO_PARENT"), which should not be possible
       */
      iTrackedPartGroup.code( TRACKED_PART_GROUP_WITH_PARENT )
            .ataCode( TRACKED_NO_PARENT_ATA_CODE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertReadOnlyPropertyErrorBody( lResponse, iTrackedPartGroup.getAssemblyCode(),
            iTrackedPartGroup.getCode(), READONLY_PROPERTIES );
   }


   @Test
   public void
         trackedPartGroup_publishWithInvalidAlternatePartApplicabilityRange_returnsErrorInvalidAlternatePartApplicabilityRange() {
      AlternatePartSet<TrackedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      iTrackedAlternatePart.applicabilityRange( INVALID_APPLICABILITY_RANGE );
      lAlternatePartSet.add( iTrackedAlternatePart );
      iTrackedPartGroup.alternateParts( lAlternatePartSet )
            .code( TRACKED_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorInvalidAlternatePartApplicabilityRangeErrorData but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInvalidAlternatePartApplicabilityRange );
      ErrorInvalidAlternatePartApplicabilityRange lError =
            ( ErrorInvalidAlternatePartApplicabilityRange ) lResponse;
      PartWithInvalidApplicabilityRange lPartWithInvalidApplicabilityRange =
            new PartWithInvalidApplicabilityRange()
                  .partNumber( iTrackedAlternatePart.getPartNumber() )
                  .partManufacturer( iTrackedAlternatePart.getPartManufacturer() )
                  .applicabilityRange( INVALID_APPLICABILITY_RANGE );
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.INVALID_ALTERNATE_PART_APPLICABILITY_RANGE.getCode(),
            new ErrorInvalidAlternatePartApplicabilityRangeErrorData()
                  .partsWithInvalidApplicabilityRanges(
                        Arrays.asList( lPartWithInvalidApplicabilityRange ) ) );
   }


   @Test
   public void
         trackedPartGroup_publishWithInvalidPartGroupApplicabilityRange_returnsErrorInvalidPartGroupApplicabilityRange() {
      iTrackedPartGroup.code( TRACKED_PART_GROUP_WITH_NO_PARENT )
            .applicabilityRange( INVALID_APPLICABILITY_RANGE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorInvalidPartGroupApplicabilityRange but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInvalidPartGroupApplicabilityRange );
      ErrorInvalidPartGroupApplicabilityRange lError =
            ( ErrorInvalidPartGroupApplicabilityRange ) lResponse;
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.INVALID_PART_GROUP_APPLICABILITY_RANGE.getCode(),
            new ErrorInvalidPartGroupApplicabilityRangeErrorData()
                  .applicabilityRange( INVALID_APPLICABILITY_RANGE ) );
   }


   @Test
   public void trackedPartGroup_publishWithNonexistentAssembly_returnsErrorConfigSlotNotFound() {
      iTrackedPartGroup.assemblyCode( NONEXISTENT_CODE ).code( TRACKED_PART_GROUP_WITH_NO_PARENT );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorConfigSlotNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorConfigSlotNotFound );
      ErrorConfigSlotNotFound lError = ( ErrorConfigSlotNotFound ) lResponse;
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.CONFIG_SLOT_NOT_FOUND.getCode(),
            new ErrorConfigSlotNotFoundErrorData().ataCode( TRACKED_NO_PARENT_ATA_CODE ) );
   }


   @Test
   public void trackedPartGroup_publishWithNonexistentAtaCode_returnsErrorConfigSlotNotFound() {
      /*
       * The part group TRACKED_PART_GROUP_WITH_PARENT is defined in XML data and has an existing
       * parent part group (code "PARENT"). This changes the ATA system to one that does not exist
       * in the CTE, which should fail even if the parent part group exists.
       */
      iTrackedPartGroup.ataCode( NONEXISTENT_CODE ).code( TRACKED_PART_GROUP_WITH_PARENT );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorConfigSlotNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorConfigSlotNotFound );
      ErrorConfigSlotNotFound lError = ( ErrorConfigSlotNotFound ) lResponse;
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.CONFIG_SLOT_NOT_FOUND.getCode(),
            new ErrorConfigSlotNotFoundErrorData().ataCode( NONEXISTENT_CODE ) );
   }


   @Test
   public void trackedPartGroup_publishWithNonexistentPart_returnsErrorPartsNotFound() {
      iTrackedPartGroup.code( TRACKED_PART_GROUP_WITH_NO_PARENT );
      iTrackedPartGroup.getAlternateParts().iterator().next()
            .setPartNumber( NONEXISTENT_PART_NUMBER );
      iTrackedPartGroup.setStandardAlternatePartNumber( NONEXISTENT_PART_NUMBER );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertTrue( "Expected ErrorPartsNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorPartsNotFound );
      ErrorPartsNotFound lError = ( ErrorPartsNotFound ) lResponse;
      List<InvalidPart> lNotFoundParts = new ArrayList<>();
      InvalidPart lNotFoundPart =
            new InvalidPart().partNumber( NONEXISTENT_PART_NUMBER ).partManufacturer(
                  iTrackedPartGroup.getAlternateParts().iterator().next().getPartManufacturer() );
      lNotFoundParts.add( lNotFoundPart );
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.PARTS_NOT_FOUND.getCode(),
            new ErrorPartsNotFoundErrorData().parts( lNotFoundParts ) );
   }


   @Test
   public void
         trackedPartGroup_publishWithNoValidTaskDefinitionForTaskIncompatibility_returnsErrorNoValidTaskDefinitionForTaskIncompatibility()
               throws Exception {
      AlternatePartSet<TrackedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      iTrackedAlternatePart
            .addPartIncompatibility( new TrackedPartIncompatibility()
                  .partGroupCode( TRACKED_PART_GROUP_WITH_NO_PARENT )
                  .partNumber( TRACKED_PART_NUMBER_1.getOemPartNumber() )
                  .manufacturer( TRACKED_PART_NUMBER_1.getManufacturerCode() ) )
            .addTaskIncompatibility( TASK_INCOMPATIBILITY );
      lAlternatePartSet.add( iTrackedAlternatePart );
      iTrackedPartGroup.alternateParts( lAlternatePartSet )
            .code( TRACKED_PART_GROUP_WITH_NO_PARENT );

      when( iMockTaskDefnService.buildTaskIncompatibility( eq( ASSEMBLY_CODE ), eq( TASK_CODE ),
            eq( SYSTEM_ATA_CODE ), eq( INCOMPATIBLE_WHEN_COMPLETE ), any( PartGroupKey.class ),
            any( PartNoKey.class ) ) ).thenReturn( null );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorNoValidTaskDefinitionForTaskIncompatibility but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorNoValidTaskDefinitionForTaskIncompatibility );
      ErrorNoValidTaskDefinitionForTaskIncompatibility lError =
            ( ErrorNoValidTaskDefinitionForTaskIncompatibility ) lResponse;
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.NO_VALID_TASK_DEFINITION_FOR_TASK_INCOMPATIBILITIES.getCode(),
            new ErrorNoValidTaskDefinitionForTaskIncompatibilityData().ataCode( SYSTEM_ATA_CODE )
                  .taskCode( TASK_CODE ).incompatibleWhen( INCOMPATIBLE_WHEN_COMPLETE ) );
   }


   @Test
   public void
         trackedPartGroup_publishWithPartIncompatibilityOEMNotFound_returnsPartsNotFoundException() {
      AlternatePartSet<TrackedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      iTrackedAlternatePart.addPartIncompatibility(
            new TrackedPartIncompatibility().partGroupCode( TRACKED_PART_GROUP_WITH_NO_PARENT )
                  .partNumber( NONEXISTENT_PART_NUMBER ).manufacturer( PART_MANUFACTURER ) );
      lAlternatePartSet.add( iTrackedAlternatePart );
      iTrackedPartGroup.alternateParts( lAlternatePartSet )
            .code( TRACKED_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertTrue( "Expected ErrorPartsNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorPartsNotFound );
      ErrorPartsNotFound lError = ( ErrorPartsNotFound ) lResponse;
      List<InvalidPart> lNotFoundParts = new ArrayList<>();
      InvalidPart lNotFoundPart = new InvalidPart().partNumber( NONEXISTENT_PART_NUMBER )
            .partManufacturer( iTrackedPartGroup.getAlternateParts().iterator().next()
                  .getPartIncompatibilities().get( 0 ).getManufacturer() );
      lNotFoundParts.add( lNotFoundPart );
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.PARTS_NOT_FOUND.getCode(),
            new ErrorPartsNotFoundErrorData().parts( lNotFoundParts ) );
   }


   @Test
   public void
         trackedPartGroup_publishWithPartIncompatibilityPartGroupNotFound_skipsPartIncompatibility() {
      AlternatePartSet<TrackedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      iTrackedAlternatePart.addPartIncompatibility(
            new TrackedPartIncompatibility().partGroupCode( TRACKED_PART_GROUP_WITH_NO_PARENT )
                  .partNumber( TRACKED_PART_NUMBER_1.getOemPartNumber() )
                  .manufacturer( TRACKED_PART_NUMBER_1.getManufacturerCode() ) );
      lAlternatePartSet.add( iTrackedAlternatePart );
      iTrackedPartGroup.alternateParts( lAlternatePartSet )
            .code( TRACKED_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            lResponse );
   }


   @Test
   public void
         trackedPartGroup_publishWithPartIncompatibilityPartNotAssignedToPartGroup_skipsPartIncompatibility()
               throws KeyConversionException {
      AlternatePartSet<TrackedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      iTrackedAlternatePart.addPartIncompatibility(
            new TrackedPartIncompatibility().partGroupCode( TRACKED_PART_GROUP_WITH_NO_PARENT )
                  .partNumber( TRACKED_PART_NUMBER_1.getOemPartNumber() )
                  .manufacturer( TRACKED_PART_NUMBER_1.getManufacturerCode() ) );
      lAlternatePartSet.add( iTrackedAlternatePart );
      iTrackedPartGroup.alternateParts( lAlternatePartSet )
            .code( TRACKED_PART_GROUP_WITH_NO_PARENT ).ataCode( TRACKED_NO_PARENT_ATA_CODE );

      when( iMockPartGroupService.find( iTrackedPartGroup.getAssemblyCode(),
            iTrackedAlternatePart.getPartIncompatibilities().get( 0 ).getPartGroupCode() ) )
                  .thenReturn( Optional.of( buildExistingTrackedPartGroup() ) );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertSuccessfulResponse( iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            lResponse );
   }


   @Test
   public void
         trackedPartGroup_publishWithPartIncompatibilityPartNotTracked_returnsErrorInvalidInventoryClass() {
      AlternatePartSet<TrackedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();

      iTrackedAlternatePart.addPartIncompatibility(
            new TrackedPartIncompatibility().partGroupCode( TRACKED_PART_GROUP_WITH_NO_PARENT )
                  .partNumber( SER_PART_NUMBER_1.getOemPartNumber() )
                  .manufacturer( SER_PART_NUMBER_1.getManufacturerCode() ) );
      lAlternatePartSet.add( iTrackedAlternatePart );
      iTrackedPartGroup.alternateParts( lAlternatePartSet )
            .code( TRACKED_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorIncompatiblePartNotTRK but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorIncompatiblePartNotTRK );
      ErrorIncompatiblePartNotTRK lError = ( ErrorIncompatiblePartNotTRK ) lResponse;
      List<InvalidPart> lNotTrackedParts = new ArrayList<>();
      InvalidPart lNotTrackedPart =
            new InvalidPart().partNumber( SER_PART_NUMBER_1.getOemPartNumber() )
                  .partManufacturer( SER_PART_NUMBER_1.getManufacturerCode() );
      lNotTrackedParts.add( lNotTrackedPart );
      assertErrorBody( lError, iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            ErrorCode.INCOMPATIBLE_PART_NOT_TRK.getCode(),
            new ErrorIncompatiblePartNotTRKData().parts( lNotTrackedParts ) );
   }


   @Test
   public void
         trackedPartGroup_publishWithStandardAlternatePartNotInAlternateParts_returnsBadRequestResponse() {
      iTrackedPartGroup.code( TRACKED_PART_GROUP_WITH_NO_PARENT );
      TrackedAlternatePart lAlternatePart =
            ( TrackedAlternatePart ) new TrackedAlternatePart().interchangeabilityOrder( 2 )
                  .partManufacturer( PART_MANUFACTURER ).partNumber( "TRACKED_PART_NUMBER_2" )
                  .applicabilityRange(
                        ApplicabilityRange.createRangeApplicableToEverything().getExpression() )
                  .hasOtherConditions( true );
      AlternatePartSet<TrackedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      lAlternatePartSet.add( lAlternatePart );
      iTrackedPartGroup.alternateParts( lAlternatePartSet );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iTrackedPartGroup, AUTHORIZING_HR );

      assertBadRequestResponse( iTrackedPartGroup.getAssemblyCode(), iTrackedPartGroup.getCode(),
            lResponse );
   }


   @Test
   public void
         batchPartGroup_publishChangingAtaCodeToOtherSystemAtaCode_returnsErrorReadOnlyProperty() {
      /*
       * The part group BATCH_PART_GROUP_WITH_PARENT is defined in XML data and has an existing
       * parent part group (code "PARENT"). This changes the ATA system (configuration slot) which
       * is not possible.
       */
      iBatchPartGroup.code( BATCH_PART_GROUP_WITH_NO_PARENT ).ataCode( ANOTHER_SYSTEM_ATA_CODE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertReadOnlyPropertyErrorBody( lResponse, iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), READONLY_PROPERTIES );
   }


   @Test
   public void
         batchPartGroup_publishChangingAtaCodeToOtherTrackedAtaCode_returnsErrorReadOnlyProperty() {
      /*
       * The part group BATCH_PART_GROUP_WITH_NO_PARENT is defined in XML data and has no parent
       * part group (nesting under a system). This tests tries to change it to be under an existing
       * tracked part group (code "PARENT").
       */
      iBatchPartGroup.code( BATCH_PART_GROUP_WITH_NO_PARENT ).ataCode( PARENT_TRACKED_ATA_CODE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertReadOnlyPropertyErrorBody( lResponse, iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), READONLY_PROPERTIES );
   }


   @Test
   public void
         batchPartGroup_publishWithInvalidAlternatePartApplicabilityRange_returnsErrorInvalidAlternatePartApplicabilityRange() {
      AlternatePartSet<BatchAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      iBatchAlternatePart.applicabilityRange( INVALID_APPLICABILITY_RANGE );
      lAlternatePartSet.add( iBatchAlternatePart );
      iBatchPartGroup.alternateParts( lAlternatePartSet ).code( BATCH_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorInvalidAlternatePartApplicabilityRange but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInvalidAlternatePartApplicabilityRange );
      ErrorInvalidAlternatePartApplicabilityRange lError =
            ( ErrorInvalidAlternatePartApplicabilityRange ) lResponse;
      PartWithInvalidApplicabilityRange lPartWithInvalidApplicabilityRange =
            new PartWithInvalidApplicabilityRange()
                  .partNumber( iBatchAlternatePart.getPartNumber() )
                  .partManufacturer( iBatchAlternatePart.getPartManufacturer() )
                  .applicabilityRange( INVALID_APPLICABILITY_RANGE );
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.INVALID_ALTERNATE_PART_APPLICABILITY_RANGE.getCode(),
            new ErrorInvalidAlternatePartApplicabilityRangeErrorData()
                  .partsWithInvalidApplicabilityRanges(
                        Arrays.asList( lPartWithInvalidApplicabilityRange ) ) );
   }


   @Test
   public void
         batchPartGroup_publishWithInvalidPartGroupApplicabilityRange_returnsErrorInvalidPartGroupApplicabilityRange() {
      iBatchPartGroup.code( BATCH_PART_GROUP_WITH_NO_PARENT )
            .applicabilityRange( INVALID_APPLICABILITY_RANGE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorInvalidPartGroupApplicabilityRange but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInvalidPartGroupApplicabilityRange );
      ErrorInvalidPartGroupApplicabilityRange lError =
            ( ErrorInvalidPartGroupApplicabilityRange ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.INVALID_PART_GROUP_APPLICABILITY_RANGE.getCode(),
            new ErrorInvalidPartGroupApplicabilityRangeErrorData()
                  .applicabilityRange( INVALID_APPLICABILITY_RANGE ) );
   }


   @Test
   public void batchPartGroup_publishWithNonexistentAssembly_returnsErrorConfigSlotNotFound() {
      iBatchPartGroup.assemblyCode( NONEXISTENT_CODE ).code( BATCH_PART_GROUP_WITH_NO_PARENT );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorConfigSlotNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorConfigSlotNotFound );
      ErrorConfigSlotNotFound lError = ( ErrorConfigSlotNotFound ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.CONFIG_SLOT_NOT_FOUND.getCode(),
            new ErrorConfigSlotNotFoundErrorData().ataCode( SYSTEM_ATA_CODE ) );
   }


   @Test
   public void batchPartGroup_publishWithNonexistentAtaCode_returnsErrorConfigSlotNotFound() {
      /*
       * The part group BATCH_PART_GROUP_WITH_PARENT is defined in XML data and has an existing
       * parent part group (code "PARENT"). This changes the ATA system to one that does not exist
       * in the CTE, which should fail even if the parent part group exists.
       */
      iBatchPartGroup.ataCode( NONEXISTENT_CODE ).code( BATCH_PART_GROUP_WITH_PARENT );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorConfigSlotNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorConfigSlotNotFound );
      ErrorConfigSlotNotFound lError = ( ErrorConfigSlotNotFound ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.CONFIG_SLOT_NOT_FOUND.getCode(),
            new ErrorConfigSlotNotFoundErrorData().ataCode( NONEXISTENT_CODE ) );
   }


   @Test
   public void batchPartGroup_publishWithNonexistentPart_returnsErrorPartsNotFound() {
      iBatchPartGroup.getAlternateParts().iterator().next()
            .setPartNumber( NONEXISTENT_PART_NUMBER );
      iBatchPartGroup.setStandardAlternatePartNumber( NONEXISTENT_PART_NUMBER );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertTrue( "Expected ErrorPartsNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorPartsNotFound );
      ErrorPartsNotFound lError = ( ErrorPartsNotFound ) lResponse;
      List<InvalidPart> lNotFoundParts = new ArrayList<>();
      InvalidPart lNotFoundPart =
            new InvalidPart().partNumber( NONEXISTENT_PART_NUMBER ).partManufacturer(
                  iBatchPartGroup.getAlternateParts().iterator().next().getPartManufacturer() );
      lNotFoundParts.add( lNotFoundPart );
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.PARTS_NOT_FOUND.getCode(),
            new ErrorPartsNotFoundErrorData().parts( lNotFoundParts ) );
   }


   @Test
   public void
         batchPartGroup_publishWithNoValidTaskDefinitionForTaskIncompatibility_returnsErrorNoValidTaskDefinitionForTaskIncompatibility()
               throws Exception {
      iBatchAlternatePart.addTaskIncompatibility( TASK_INCOMPATIBILITY );

      AlternatePartSet<BatchAlternatePart> lAlternatePartSet = new AlternatePartSet<>();

      lAlternatePartSet.add( iBatchAlternatePart );
      iBatchPartGroup.alternateParts( lAlternatePartSet ).code( BATCH_PART_GROUP_WITH_NO_PARENT );

      when( iMockTaskDefnService.buildTaskIncompatibility( eq( ASSEMBLY_CODE ), eq( TASK_CODE ),
            eq( SYSTEM_ATA_CODE ), eq( INCOMPATIBLE_WHEN_COMPLETE ), any( PartGroupKey.class ),
            any( PartNoKey.class ) ) ).thenReturn( null );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorNoValidTaskDefinitionForTaskIncompatibility but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorNoValidTaskDefinitionForTaskIncompatibility );
      ErrorNoValidTaskDefinitionForTaskIncompatibility lError =
            ( ErrorNoValidTaskDefinitionForTaskIncompatibility ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.NO_VALID_TASK_DEFINITION_FOR_TASK_INCOMPATIBILITIES.getCode(),
            new ErrorNoValidTaskDefinitionForTaskIncompatibilityData().ataCode( SYSTEM_ATA_CODE )
                  .taskCode( TASK_CODE ).incompatibleWhen( INCOMPATIBLE_WHEN_COMPLETE ) );
   }


   @Test
   public void
         batchPartGroup_publishWithStandardAlternatePartNotInAlternateParts_returnsBadRequestResponse() {
      BatchAlternatePart lAlternatePart = ( BatchAlternatePart ) new BatchAlternatePart()
            .partManufacturer( PART_MANUFACTURER ).partNumber( "BATCH_PART_NUMBER_2" )
            .applicabilityRange(
                  ApplicabilityRange.createRangeApplicableToEverything().getExpression() )
            .hasOtherConditions( true );
      AlternatePartSet<BatchAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      lAlternatePartSet.add( lAlternatePart );
      iBatchPartGroup.alternateParts( lAlternatePartSet );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iBatchPartGroup, AUTHORIZING_HR );

      assertBadRequestResponse( iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            lResponse );
   }


   @Test
   public void
         serializedPartGroup_publishChangingAtaCodeToOtherSystemAtaCode_returnsErrorReadOnlyProperty() {
      /*
       * The part group SERIALIZED_PART_GROUP_WITH_PARENT is defined in XML data and has an existing
       * parent part group (code "PARENT"). This changes the ATA system (configuration slot) which
       * is not possible.
       */
      iSerializedPartGroup.code( SERIALIZED_PART_GROUP_WITH_PARENT )
            .ataCode( ANOTHER_SYSTEM_ATA_CODE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertReadOnlyPropertyErrorBody( lResponse, iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), READONLY_PROPERTIES );
   }


   @Test
   public void
         serializedPartGroup_publishChangingAtaCodeToOtherTrackedAtaCode_returnsErrorReadOnlyProperty() {
      /*
       * The part group SERIALIZED_PART_GROUP_WITH_NO_PARENT is defined in XML data and has no
       * parent part group (nesting under a system). This tests tries to change it to be under an
       * existing tracked part group (code "PARENT").
       */
      iSerializedPartGroup.code( SERIALIZED_PART_GROUP_WITH_NO_PARENT )
            .ataCode( PARENT_TRACKED_ATA_CODE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertReadOnlyPropertyErrorBody( lResponse, iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), READONLY_PROPERTIES );
   }


   @Test
   public void
         serializedPartGroup_publishWithInvalidAlternatePartApplicabilityRange_returnsErrorInvalidAlternatePartApplicabilityRange() {
      AlternatePartSet<SerializedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      iSerializedAlternatePart.applicabilityRange( INVALID_APPLICABILITY_RANGE );
      lAlternatePartSet.add( iSerializedAlternatePart );
      iSerializedPartGroup.alternateParts( lAlternatePartSet )
            .code( SERIALIZED_PART_GROUP_WITH_NO_PARENT );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorInvalidAlternatePartApplicabilityRange but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInvalidAlternatePartApplicabilityRange );
      ErrorInvalidAlternatePartApplicabilityRange lError =
            ( ErrorInvalidAlternatePartApplicabilityRange ) lResponse;
      PartWithInvalidApplicabilityRange lPartWithInvalidApplicabilityRange =
            new PartWithInvalidApplicabilityRange()
                  .partNumber( iSerializedAlternatePart.getPartNumber() )
                  .partManufacturer( iSerializedAlternatePart.getPartManufacturer() )
                  .applicabilityRange( INVALID_APPLICABILITY_RANGE );
      assertErrorBody( lError, iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(),
            ErrorCode.INVALID_ALTERNATE_PART_APPLICABILITY_RANGE.getCode(),
            new ErrorInvalidAlternatePartApplicabilityRangeErrorData()
                  .partsWithInvalidApplicabilityRanges(
                        Arrays.asList( lPartWithInvalidApplicabilityRange ) ) );
   }


   @Test
   public void
         serializedPartGroup_publishWithInvalidPartGroupApplicabilityRange_returnsErrorInvalidPartGroupApplicabilityRange() {
      iSerializedPartGroup.code( SERIALIZED_PART_GROUP_WITH_NO_PARENT )
            .applicabilityRange( INVALID_APPLICABILITY_RANGE );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorInvalidPartGroupApplicabilityRange but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInvalidPartGroupApplicabilityRange );
      ErrorInvalidPartGroupApplicabilityRange lError =
            ( ErrorInvalidPartGroupApplicabilityRange ) lResponse;
      assertErrorBody( lError, iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(),
            ErrorCode.INVALID_PART_GROUP_APPLICABILITY_RANGE.getCode(),
            new ErrorInvalidPartGroupApplicabilityRangeErrorData()
                  .applicabilityRange( INVALID_APPLICABILITY_RANGE ) );
   }


   @Test
   public void serializedPartGroup_publishWithNonexistentAssembly_returnsErrorConfigSlotNotFound() {
      iSerializedPartGroup.assemblyCode( NONEXISTENT_CODE )
            .code( SERIALIZED_PART_GROUP_WITH_NO_PARENT );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorConfigSlotNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorConfigSlotNotFound );
      ErrorConfigSlotNotFound lError = ( ErrorConfigSlotNotFound ) lResponse;
      assertErrorBody( lError, iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), ErrorCode.CONFIG_SLOT_NOT_FOUND.getCode(),
            new ErrorConfigSlotNotFoundErrorData().ataCode( SYSTEM_ATA_CODE ) );
   }


   @Test
   public void serializedPartGroup_publishWithNonexistentAtaCode_returnsErrorConfigSlotNotFound() {
      /*
       * The part group SERIALIZED_PART_GROUP_WITH_PARENT is defined in XML data and has an existing
       * parent part group (code "PARENT"). This changes the ATA system to one that does not exist
       * in the CTE, which should fail even if the parent part group exists.
       */
      iSerializedPartGroup.ataCode( NONEXISTENT_CODE ).code( SERIALIZED_PART_GROUP_WITH_PARENT );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorConfigSlotNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorConfigSlotNotFound );
      ErrorConfigSlotNotFound lError = ( ErrorConfigSlotNotFound ) lResponse;
      assertErrorBody( lError, iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), ErrorCode.CONFIG_SLOT_NOT_FOUND.getCode(),
            new ErrorConfigSlotNotFoundErrorData().ataCode( NONEXISTENT_CODE ) );
   }


   @Test
   public void serializedPartGroup_publishWithNonexistentPart_returnsErrorPartsNotFound() {
      iSerializedPartGroup.getAlternateParts().iterator().next()
            .setPartNumber( NONEXISTENT_PART_NUMBER );
      iSerializedPartGroup.setStandardAlternatePartNumber( NONEXISTENT_PART_NUMBER );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertTrue( "Expected ErrorPartsNotFound but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorPartsNotFound );
      ErrorPartsNotFound lError = ( ErrorPartsNotFound ) lResponse;
      List<InvalidPart> lNotFoundParts = new ArrayList<>();
      InvalidPart lNotFoundPart = new InvalidPart().partNumber( NONEXISTENT_PART_NUMBER )
            .partManufacturer( iSerializedPartGroup.getAlternateParts().iterator().next()
                  .getPartManufacturer() );
      lNotFoundParts.add( lNotFoundPart );
      assertErrorBody( lError, iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), ErrorCode.PARTS_NOT_FOUND.getCode(),
            new ErrorPartsNotFoundErrorData().parts( lNotFoundParts ) );
   }


   @Test
   public void
         serializedPartGroup_publishWithNoValidTaskDefinitionForTaskIncompatibility_returnsErrorNoValidTaskDefinitionForTaskIncompatibility()
               throws Exception {
      iSerializedAlternatePart.addTaskIncompatibility( TASK_INCOMPATIBILITY );

      AlternatePartSet<SerializedAlternatePart> alternateParts = new AlternatePartSet<>();
      alternateParts.add( iSerializedAlternatePart );
      iSerializedPartGroup.alternateParts( alternateParts )
            .code( SERIALIZED_PART_GROUP_WITH_NO_PARENT );

      when( iMockTaskDefnService.buildTaskIncompatibility( eq( ASSEMBLY_CODE ), eq( TASK_CODE ),
            eq( SYSTEM_ATA_CODE ), eq( INCOMPATIBLE_WHEN_COMPLETE ), any( PartGroupKey.class ),
            any( PartNoKey.class ) ) ).thenReturn( null );

      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorNoValidTaskDefinitionForTaskIncompatibility but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorNoValidTaskDefinitionForTaskIncompatibility );
      ErrorNoValidTaskDefinitionForTaskIncompatibility lError =
            ( ErrorNoValidTaskDefinitionForTaskIncompatibility ) lResponse;
      assertErrorBody( lError, iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(),
            ErrorCode.NO_VALID_TASK_DEFINITION_FOR_TASK_INCOMPATIBILITIES.getCode(),
            new ErrorNoValidTaskDefinitionForTaskIncompatibilityData().ataCode( SYSTEM_ATA_CODE )
                  .taskCode( TASK_CODE ).incompatibleWhen( INCOMPATIBLE_WHEN_COMPLETE ) );
   }


   @Test
   public void
         serializedPartGroup_publishWithStandardAlternatePartNotInAlternateParts_returnsBadRequestResponse() {
      SerializedAlternatePart lAlternatePart =
            ( SerializedAlternatePart ) new SerializedAlternatePart()
                  .partManufacturer( PART_MANUFACTURER ).partNumber( "SER_PART_NUMBER_2" )
                  .applicabilityRange(
                        ApplicabilityRange.createRangeApplicableToEverything().getExpression() )
                  .hasOtherConditions( true );
      AlternatePartSet<SerializedAlternatePart> lAlternatePartSet = new AlternatePartSet<>();
      lAlternatePartSet.add( lAlternatePart );
      iSerializedPartGroup.alternateParts( lAlternatePartSet );
      Object lResponse =
            new ConfigurationManagerMapperService().publish( iSerializedPartGroup, AUTHORIZING_HR );

      assertBadRequestResponse( iSerializedPartGroup.getAssemblyCode(),
            iSerializedPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_configurationSlotNotFoundException_returnsInternalServerErrorResponse()
         throws Exception {
      mockCreateBatchPartGroupThrows( ConfigurationSlotNotFoundException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_ietmAssemblyMapDoesNotExistException_returnsInternalServerErrorResponse()
         throws Exception {
      mockCreateBatchPartGroupThrows( IetmAssemblyMapDoesNotExistException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_inputCodeException_returnsInternalServerErrorResponse() throws Exception {
      mockCreateBatchPartGroupThrows( InputCodeException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_invalidInventoryClassException_returnsInternalServerErrorResponse()
         throws Exception {
      mockCreateBatchPartGroupThrows( InvalidInventoryClassException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_invalidPartStatusException_returnsErrorObsoletePart() throws Exception {
      mockCreateBatchPartGroupThrows( InvalidPartStatusException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );

      assertTrue( "Expected ErrorObsoletePart but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorObsoletePart );
      ErrorObsoletePart lError = ( ErrorObsoletePart ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.OBSOLETE_PART.getCode(),
            new ErrorObsoletePartErrorData().partNumber( null ).partManufacturer( null ) );
   }


   @Test
   public void create_inventoryClassMismatchException_returnsErrorInventoryClassMismatch()
         throws Exception {
      Key lPart1 = SER_PART_NUMBER_1.getKey();
      Key lPart2 = TRACKED_PART_NUMBER_1.getKey();
      Map<Part.Key, RefInvClassKey> lPartToInventoryClassCodeMap = new HashMap<>();
      lPartToInventoryClassCodeMap.put( lPart1, RefInvClassKey.SER );
      lPartToInventoryClassCodeMap.put( lPart2, RefInvClassKey.TRK );

      InventoryClassMismatchException lInventoryClassMismatchException =
            new InventoryClassMismatchException( RefInvClassKey.BATCH,
                  lPartToInventoryClassCodeMap );
      mockCreateBatchPartGroupThrows( lInventoryClassMismatchException );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );

      assertTrue(
            "Expected ErrorInventoryClassMismatch but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInventoryClassMismatch );
      ErrorInventoryClassMismatch lError = ( ErrorInventoryClassMismatch ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.INVENTORY_CLASS_MISMATCH.getCode(),
            new ErrorInventoryClassMismatchErrorData()
                  .partGroupInventoryClass( ConfigurationManagerMapperService
                        .convertInventoryClassKeyToDomainValue( RefInvClassKey.BATCH ) )
                  .partsWithMismatchedInventoryClasses( buildPartsWithInventoryClassMismatchList(
                        lInventoryClassMismatchException ) ) );
   }


   @Test
   public void create_keyConversionException_returnsInternalServerErrorResponse() throws Exception {
      mockCreateBatchPartGroupThrows( KeyConversionException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_mandatoryArgumentException_returnsInternalServerErrorResponse()
         throws Exception {
      mockCreateBatchPartGroupThrows( MandatoryArgumentException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_partNoHasPartReqException_returnsInternalServerErrorResponse()
         throws Exception {
      mockCreateBatchPartGroupThrows( PartNoHasPartReqException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_partsNotFoundException_returnsInternalServerErrorResponse() throws Exception {
      mockCreateBatchPartGroupThrows( com.mxi.mx.core.exception.PartsNotFoundException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void create_triggerException_returnsInternalServerErrorResponse() throws Exception {
      mockCreateBatchPartGroupThrows( TriggerException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).update( any( PartGroup.class ),
            any( HumanResourceKey.class ), any( Boolean.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void
         update_compatibilityRuleExistsException_returnsErrorAlternatePartHasIncompatibilityRule()
               throws Exception {
      mockUpdateBatchPartGroupThrows( CompatibilityRuleExistsException.class );

      when( iMockPartNoService.get( any( PartNoKey.class ) ) ).thenReturn( SER_PART_NUMBER_1 );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );

      assertTrue(
            "Expected ErrorAlternatePartHasIncompatilityRule but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorAlternatePartHasIncompatibilityRule );
      ErrorAlternatePartHasIncompatibilityRule lError =
            ( ErrorAlternatePartHasIncompatibilityRule ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.ALTERNATE_PART_HAS_INCOMPATIBILITY_RULE.getCode(),
            new ErrorAlternatePartHasIncompatibilityRuleData()
                  .partNumber( SER_PART_NUMBER_1.getOemPartNumber() )
                  .partManufacturer( SER_PART_NUMBER_1.getManufacturerCode() ) );
   }


   @Test
   public void update_configurationSlotNotFoundException_returnsInternalServerErrorResponse()
         throws Exception {
      mockUpdateBatchPartGroupThrows( ConfigurationSlotNotFoundException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void update_ietmAssemblyMapDoesNotExistException_returnsInternalServerErrorResponse()
         throws Exception {
      mockUpdateBatchPartGroupThrows( IetmAssemblyMapDoesNotExistException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void update_inputCodeException_returnsInternalServerErrorResponse() throws Exception {
      mockUpdateBatchPartGroupThrows( InputCodeException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void
         update_installedInventoryNotApplicableException_returnsErrorInstalledInventoryApplicabilityInvalidated()
               throws Exception {
      mockUpdateBatchPartGroupThrows( InstalledInventoryNotApplicableException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );

      assertTrue(
            "Expected ErrorInstalledInventoryApplicabilityInvalidated but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInstalledInventoryApplicabilityInvalidated );
      ErrorInstalledInventoryApplicabilityInvalidated lError =
            ( ErrorInstalledInventoryApplicabilityInvalidated ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.INSTALLED_INVENTORY_APPLICABILITY_INVALIDATED.getCode(),
            new ErrorInstalledInventoryApplicabilityInvalidatedErrorData()
                  .applicabilityRange( iBatchPartGroup.getApplicabilityRange() ) );
   }


   @Test
   public void update_invalidInventoryClassException_returnsInternalServerErrorResponse()
         throws Exception {
      mockUpdateBatchPartGroupThrows( InvalidInventoryClassException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void update_invalidPartStatusException_returnsErrorObsoletePart() throws Exception {
      mockUpdateBatchPartGroupThrows( InvalidPartStatusException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );

      assertTrue( "Expected ErrorObsoletePart but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorObsoletePart );
      ErrorObsoletePart lError = ( ErrorObsoletePart ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.OBSOLETE_PART.getCode(),
            new ErrorObsoletePartErrorData().partNumber( null ).partManufacturer( null ) );
   }


   @Test
   public void update_inventoryClassMismatchException_returnsErrorInventoryClassMismatch()
         throws Exception {
      Key lPart1 = SER_PART_NUMBER_1.getKey();
      Key lPart2 = TRACKED_PART_NUMBER_1.getKey();
      Map<Part.Key, RefInvClassKey> lPartToInventoryClassCodeMap = new HashMap<>();
      lPartToInventoryClassCodeMap.put( lPart1, RefInvClassKey.SER );
      lPartToInventoryClassCodeMap.put( lPart2, RefInvClassKey.TRK );

      InventoryClassMismatchException lInventoryClassMismatchException =
            new InventoryClassMismatchException( RefInvClassKey.BATCH,
                  lPartToInventoryClassCodeMap );
      mockUpdateBatchPartGroupThrows( lInventoryClassMismatchException );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );

      assertTrue(
            "Expected ErrorInventoryClassMismatch but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorInventoryClassMismatch );
      ErrorInventoryClassMismatch lError = ( ErrorInventoryClassMismatch ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.INVENTORY_CLASS_MISMATCH.getCode(),
            new ErrorInventoryClassMismatchErrorData()
                  .partGroupInventoryClass( ConfigurationManagerMapperService
                        .convertInventoryClassKeyToDomainValue( RefInvClassKey.BATCH ) )
                  .partsWithMismatchedInventoryClasses( buildPartsWithInventoryClassMismatchList(
                        lInventoryClassMismatchException ) ) );
   }


   @Test
   public void update_keyConversionException_returnsInternalServerErrorResponse() throws Exception {
      mockUpdateBatchPartGroupThrows( KeyConversionException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void update_mandatoryArgumentException_returnsInternalServerErrorResponse()
         throws Exception {
      mockUpdateBatchPartGroupThrows( MandatoryArgumentException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void update_partGroupNotFoundException_returnsInternalServerErrorResponse()
         throws Exception {
      mockUpdateBatchPartGroupThrows( PartGroupNotFoundException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void update_partNoBomPartInUseException_returnsErrorPartInUse() throws Exception {
      mockUpdateBatchPartGroupThrows( PartNoBomPartInUseException.class );
      when( iMockPartNoService.get( any( PartNoKey.class ) ) ).thenReturn( SER_PART_NUMBER_1 );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );

      assertTrue( "Expected ErrorPartInUse but got " + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorPartInUse );
      ErrorPartInUse lError = ( ErrorPartInUse ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.PART_IN_USE.getCode(),
            new ErrorPartInUseErrorData().partNumber( SER_PART_NUMBER_1.getOemPartNumber() )
                  .partManufacturer( SER_PART_NUMBER_1.getManufacturerCode() ) );
   }


   @Test
   public void update_partNoHasPartReqException_returnsInternalServerErrorResponse()
         throws Exception {
      mockUpdateBatchPartGroupThrows( PartNoHasPartReqException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void
         update_partRequirementNotUsingSpecificPartException_returnsErrorMustRequestSpecificPartFailed()
               throws Exception {
      mockUpdateBatchPartGroupThrows( PartRequirementNotUsingSpecificPartException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );

      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );

      assertTrue(
            "Expected ErrorMustRequestSpecificPartFailed but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorMustRequestSpecificPartFailed );
      ErrorMustRequestSpecificPartFailed lError = ( ErrorMustRequestSpecificPartFailed ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.MUST_REQUEST_SPECIFIC_PART_FAILED.getCode(), null );
   }


   @Test
   public void update_partsNotFoundException_returnsInternalServerErrorResponse() throws Exception {
      mockUpdateBatchPartGroupThrows( com.mxi.mx.core.exception.PartsNotFoundException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void update_readOnlyPropertyException_returnsErrorReadOnlyProperty() throws Exception {
      iBatchPartGroup.code( BATCH_PART_GROUP_WITH_NO_PARENT );
      when( iMockPartGroupService.find( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode() ) )
                  .thenReturn( Optional.of( buildExistingBatchPartGroupWithNoParent() ) );
      when( iMockPartGroupService.update( any( PartGroup.class ), any( HumanResourceKey.class ),
            any( Boolean.class ) ) )
                  .thenThrow( new ReadOnlyPropertyException(
                        new HashSet<String>( Arrays.asList( "any property is readonly" ) ) ) );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );

      Collection<String> lReadOnlyProperties = new HashSet<>();
      lReadOnlyProperties.add( "any property is readonly" );
      assertReadOnlyPropertyErrorBody( lResponse, iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lReadOnlyProperties );
   }


   @Test
   public void update_triggerException_returnsInternalServerErrorResponse() throws Exception {
      mockUpdateBatchPartGroupThrows( TriggerException.class );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );
      verify( iMockPartGroupService, never() ).create( any( PartGroup.class ),
            any( HumanResourceKey.class ) );
      assertInternalServerErrorResponse( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode(), lResponse );
   }


   @Test
   public void
         update_withPartRemovalHasLockedTaskIncompatibility_returnsAlternatePartHasLockedTaskIncompatibilityException()
               throws Exception {
      AlternatePartHasLockedTaskIncompatibilityException lMockException =
            mock( AlternatePartHasLockedTaskIncompatibilityException.class );

      String lTaskCode = "task code";
      String lPartNumber = "part number";
      String lPartManufacturer = "part manufacturer";

      when( lMockException.getTaskCode() ).thenReturn( lTaskCode );
      when( lMockException.getPartNumber() ).thenReturn( lPartNumber );
      when( lMockException.getPartManufacturer() ).thenReturn( lPartManufacturer );

      mockUpdateBatchPartGroupThrows( lMockException );

      Object lResponse = iConfigurationManagerMapperServiceWithMockPartGroupService
            .publish( iBatchPartGroup, AUTHORIZING_HR );

      assertTrue(
            "Expected ErrorAlternatePartHasLockedTaskIncompatibility but got "
                  + lResponse.getClass().getSimpleName(),
            lResponse instanceof ErrorAlternatePartHasLockedTaskIncompatibility );
      ErrorAlternatePartHasLockedTaskIncompatibility lError =
            ( ErrorAlternatePartHasLockedTaskIncompatibility ) lResponse;
      assertErrorBody( lError, iBatchPartGroup.getAssemblyCode(), iBatchPartGroup.getCode(),
            ErrorCode.LOCKED_TASK_INCOMPATIBILITY.getCode(),
            new ErrorAlternatePartHasLockedTaskIncompatibilityData( lTaskCode, lPartNumber,
                  lPartManufacturer ) );
   }


   private <T extends Exception> void mockCreateBatchPartGroupThrows( Class<T> aMockExceptionClass )
         throws Exception {
      mockCreateBatchPartGroupThrows( mock( aMockExceptionClass ) );
   }


   private <T extends Exception> void mockCreateBatchPartGroupThrows( Throwable aThrowable )
         throws Exception {
      when( iMockPartGroupService.find( iBatchPartGroup.getAssemblyCode(),
            iBatchPartGroup.getCode() ) ).thenReturn( Optional.<PartGroup>empty() );
      when( iMockPartGroupService.create( any( PartGroup.class ), any( HumanResourceKey.class ) ) )
            .thenThrow( aThrowable );
   }


   private <T extends Exception> void mockUpdateBatchPartGroupThrows( Class<T> aClass )
         throws Exception {
      mockUpdateBatchPartGroupThrows( mock( aClass ) );
   }


   private <T extends Exception> void mockUpdateBatchPartGroupThrows( Throwable aThrowable )
         throws Exception {
      iBatchPartGroup.code( BATCH_PART_GROUP_WITH_NO_PARENT );
      when( iMockPartGroupService.find( ASSEMBLY_CODE, BATCH_PART_GROUP_WITH_NO_PARENT ) )
            .thenReturn( Optional.of( buildExistingBatchPartGroupWithNoParent() ) );
      when( iMockPartGroupService.update( any( PartGroup.class ), any( HumanResourceKey.class ),
            any( Boolean.class ) ) ).thenThrow( aThrowable );
   }


   private void assertBadRequestResponse( String aExpectedAssemblyCode, String aExpectedCode,
         Object aResponse ) {
      assertTrue( "Expected BadRequestResponse but got " + aResponse.toString(),
            aResponse instanceof BadRequestResponse );
      BadRequestResponse lResponse = ( BadRequestResponse ) aResponse;
      assertEquals( aExpectedAssemblyCode, lResponse.getAssemblyCode() );
      assertEquals( aExpectedCode, lResponse.getCode() );
      assertTrue( lResponse.getHttpStatusCode() != null );
      assertEquals( Response.Status.BAD_REQUEST.getStatusCode(),
            lResponse.getHttpStatusCode().intValue() );
   }


   private <T extends BaseUnprocessableEntityResponse> void assertErrorBody( T aError,
         String aExpectedAssemblyCode, String aExpectedCode, int aExpectedErrorCode,
         Object aExpectedErrorData ) {
      assertEquals( aExpectedAssemblyCode, aError.getAssemblyCode() );
      assertEquals( aExpectedCode, aError.getCode() );
      assertTrue( aError.getHttpStatusCode() != null );
      assertEquals( HTTP_UNPROCESSABLE_ENTITY_STATUS, aError.getHttpStatusCode().intValue() );
      assertEquals( aExpectedErrorCode, aError.getErrorCode().intValue() );
      assertEquals( aExpectedErrorData, aError.getErrorData() );
   }


   private void assertInternalServerErrorResponse( String aExpectedAssemblyCode,
         String aExpectedCode, Object aResponse ) {
      assertTrue(
            "Expected InternalServerErrorResponse but got " + aResponse.getClass().getSimpleName(),
            aResponse instanceof InternalServerErrorResponse );
      InternalServerErrorResponse lResponse = ( InternalServerErrorResponse ) aResponse;
      assertEquals( aExpectedAssemblyCode, lResponse.getAssemblyCode() );
      assertEquals( aExpectedCode, lResponse.getCode() );
      assertTrue( lResponse.getHttpStatusCode() != null );
      assertEquals( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
            lResponse.getHttpStatusCode().intValue() );
   }


   private void assertReadOnlyPropertyErrorBody( Object aResponse, String aExpectedAssemblyCode,
         String aExpectedCode, Collection<String> aExpectedReadOnlyProperties ) {
      assertTrue( "Expected ErrorReadOnlyProperty but got " + aResponse.getClass().getSimpleName(),
            aResponse instanceof ErrorReadOnlyProperty );
      ErrorReadOnlyProperty lError = ( ErrorReadOnlyProperty ) aResponse;
      assertEquals( aExpectedAssemblyCode, lError.getAssemblyCode() );
      assertEquals( aExpectedCode, lError.getCode() );
      assertTrue( lError.getHttpStatusCode() != null );
      assertEquals( HTTP_UNPROCESSABLE_ENTITY_STATUS, lError.getHttpStatusCode().intValue() );
      assertEquals( ErrorCode.READONLY_PROPERTY.getCode(), lError.getErrorCode().intValue() );
      ErrorReadOnlyPropertyErrorData lErrorData =
            ( ErrorReadOnlyPropertyErrorData ) lError.getErrorData();
      assertEquals( aExpectedReadOnlyProperties.size(), lErrorData.getProperties().size() );
      assertTrue(
            "Expected ErrorReadOnlyPropertyErrorData to contain "
                  + aExpectedReadOnlyProperties.toString() + " but got "
                  + lErrorData.getProperties().toString(),
            lErrorData.getProperties().containsAll( aExpectedReadOnlyProperties ) );
   }


   private void assertSuccessfulResponse( String aExpectedAssemblyCode, String aExpectedCode,
         Object aResponse ) {
      assertTrue( "Expected Successful response but got " + aResponse.getClass().getSimpleName(),
            aResponse instanceof SuccessfulResponse );
      SuccessfulResponse lSuccessfulResponse = ( SuccessfulResponse ) aResponse;
      assertEquals( aExpectedAssemblyCode, lSuccessfulResponse.getAssemblyCode() );
      assertEquals( aExpectedCode, lSuccessfulResponse.getCode() );
      assertTrue( lSuccessfulResponse.getHttpStatusCode() != null );
      assertEquals( Response.Status.OK.getStatusCode(),
            lSuccessfulResponse.getHttpStatusCode().intValue() );
   }


   private static PartGroup buildExistingBatchPartGroupWithNoParent() {
      return PartGroup.builder().id( BATCH_PART_GROUP_WITH_NO_PARENT_ID ).assembly( ASSEMBLY_KEY )
            .code( BATCH_PART_GROUP_WITH_NO_PARENT ).name( BATCH_PART_GROUP_WITH_NO_PARENT_NAME )
            .quantity( new BigDecimal( "1.2" ) ).inventoryClassKey( RefInvClassKey.BATCH )
            .configurationSlotId( SYSTEM_CONFIGURATION_SLOT )
            .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
            .purchaseTypeKey( new RefPurchTypeKey( null ) ).lineReplaceableUnit( true )
            .mustRequestSpecificPart( false )
            .alternateParts( Collections.<AlternatePart>emptyList() ).build();
   }


   private static PartGroup buildExistingTrackedPartGroup() {
      return PartGroup.builder().id( TRACKED_PART_GROUP_WITH_NO_PARENT_ID ).assembly( ASSEMBLY_KEY )
            .code( TRACKED_PART_GROUP_WITH_NO_PARENT )
            .name( TRACKED_PART_GROUP_WITH_NO_PARENT_NAME ).inventoryClassKey( RefInvClassKey.TRK )
            .quantity( BigDecimal.ONE ).configurationSlotId( PARENT_TRACKED_CONFIGURATION_SLOT )
            .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
            .purchaseTypeKey( new RefPurchTypeKey( null ) ).lineReplaceableUnit( true )
            .mustRequestSpecificPart( false )
            .alternateParts( Collections.<AlternatePart>emptyList() ).build();
   }


   private static List<PartWithInventoryClassMismatch> buildPartsWithInventoryClassMismatchList(
         InventoryClassMismatchException aInventoryClassMismatchException ) {
      List<PartWithInventoryClassMismatch> lPartsWithInventoryClassMismatch = new ArrayList<>();
      for ( Map.Entry<Part.Key, RefInvClassKey> lPartInventoryEntry : aInventoryClassMismatchException
            .getPartToInventoryClassCodeMap().entrySet() ) {
         lPartsWithInventoryClassMismatch.add( new PartWithInventoryClassMismatch()
               .partInventoryClass( ConfigurationManagerMapperService
                     .convertInventoryClassKeyToDomainValue( lPartInventoryEntry.getValue() ) )
               .partNumber( lPartInventoryEntry.getKey().getNumber() )
               .partManufacturer( lPartInventoryEntry.getKey().getManufacturerCode() ) );
      }
      return lPartsWithInventoryClassMismatch;
   }
}
