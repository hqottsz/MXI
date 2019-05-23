package com.mxi.mx.core.services.bom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.PositiveValueException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.exception.ConfigurationSlotNotFoundException;
import com.mxi.mx.core.exception.InputCodeException;
import com.mxi.mx.core.exception.KeyConversionException;
import com.mxi.mx.core.exception.PartGroupNotFoundException;
import com.mxi.mx.core.exception.PartsNotFoundException;
import com.mxi.mx.core.exception.ReadOnlyPropertyException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartGroupSensitivityKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPurchTypeKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.services.ietm.IetmAssemblyMapDoesNotExistException;
import com.mxi.mx.core.services.inventory.exception.InvalidInventoryClassException;
import com.mxi.mx.core.services.inventory.exception.InventoryClassMismatchException;
import com.mxi.mx.core.services.part.ApplicabilityRange;
import com.mxi.mx.core.services.part.CompatibilityRuleExistsException;
import com.mxi.mx.core.services.part.InvalidApplicabilityRangeException;
import com.mxi.mx.core.services.part.InvalidPartStatusException;
import com.mxi.mx.core.services.part.PartNoBomPartInUseException;
import com.mxi.mx.core.services.part.PartNoHasPartReqException;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.eqp.partgroup.PartGroupSensitivityDao;
import com.mxi.mx.core.table.eqp.partgroup.PartGroupSensitivityTable;
import com.mxi.mx.domain.Id;
import com.mxi.mx.domain.configslot.ConfigSlot;
import com.mxi.mx.domain.part.Part;
import com.mxi.mx.domain.partgroup.PartGroup;
import com.mxi.mx.domain.partgroup.alternatepart.AlternatePart;
import com.mxi.mx.testing.FakeStringBundles;


public class PartGroupServiceTest {

   private static final PartGroupKey EXISTING_AIRCRAFT_PART_GROUP_KEY = new PartGroupKey( 4650, 4 );
   private static final PartGroupKey EXISTING_ASSEMBLY_PART_GROUP_KEY = new PartGroupKey( 4650, 3 );
   private static final PartGroupKey EXISTING_SERIALIZED_PART_GROUP_KEY =
         new PartGroupKey( 4650, 9 );
   private static final PartGroupKey EXISTING_KIT_PART_GROUP_KEY = new PartGroupKey( 4650, 8 );
   private static final String IETM_SHORT_DESCRIPTION = "IETM short description";
   private static final IetmTopicKey IETM_TOPIC_EXISTS_ON_ASSEMBLY_KEY =
         new IetmTopicKey( 4650, 2, 1 );
   private static final IetmTopicKey IETM_TOPIC_EXISTS_NOT_ON_ASSEMBLY_KEY =
         new IetmTopicKey( 4650, 1, 1 );
   private static final Id<PartGroup> EXISTING_PART_GROUP_ID =
         new Id<PartGroup>( "00000000000000000000000000000001" );
   private static final PartGroupKey EXISTING_PART_GROUP_KEY = new PartGroupKey( 4650, 1 );
   private static final Id<PartGroup> EXISTING_TRK_PART_GROUP_ID_WITH_APPLICABILITY =
         new Id<PartGroup>( "00000000000000000000000000000002" );
   private static final PartGroupKey EXISTING_TRK_PART_GROUP_KEY = new PartGroupKey( 4650, 2 );
   private static final Id<PartGroup> EXISTING_ASSY_PART_GROUP_ID =
         new Id<PartGroup>( "00000000000000000000000000000003" );
   private static final Id<PartGroup> EXISTING_ACFT_PART_GROUP_ID =
         new Id<PartGroup>( "00000000000000000000000000000004" );
   private static final Id<PartGroup> EXISTING_TRK_PART_GROUP_ID =
         new Id<PartGroup>( "00000000000000000000000000000005" );
   private static final Id<PartGroup> EXISTING_BATCH_PART_GROUP_ID_WITH_TASK =
         new Id<PartGroup>( "00000000000000000000000000000006" );
   private static final PartGroupKey EXISTING_BATCH_PART_GROUP_KEY = new PartGroupKey( 4650, 6 );
   private static final Id<PartGroup> EXISTING_PART_GROUP_ID_WITH_COMPATIBILITY_RULE =
         new Id<PartGroup>( "00000000000000000000000000000007" );
   private static final PartGroupKey EXISTING_PART_GROUP_ID_WITH_COMPATIBILITY_RULE_KEY =
         new PartGroupKey( 4650, 7 );
   private static final Id<PartGroup> EXISTING_KIT_PART_GROUP_ID =
         new Id<PartGroup>( "00000000000000000000000000000008" );

   private static final Id<Part> PART_UNKNOWN = new Id<>( "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" );
   private static final Id<Part> BATCH_PART_1 = new Id<>( "00000000000000000000000000000001" );
   private static final Id<Part> PART_2 = new Id<>( "00000000000000000000000000000002" );
   private static final Id<Part> PART_3 = new Id<>( "00000000000000000000000000000003" );
   private static final Id<Part> PART_4 = new Id<>( "00000000000000000000000000000004" );
   private static final Id<Part> IN_USE_PART_5 = new Id<>( "00000000000000000000000000000005" );
   private static final Id<Part> TRACK_PART = new Id<>( "00000000000000000000000000000006" );
   private static final Id<Part> PART_10_WITH_PART_REQUEST =
         new Id<>( "00000000000000000000000000000010" );
   private static final Id<Part> PART_11_OBSLT = new Id<>( "00000000000000000000000000000011" );
   private static final String PART_GROUP_CODE = "ParGR";
   private static final String PART_GROUP_CODE_NEW = "newParGR";
   private static final String PART_GROUP_CODE_EXISTING = "EXISTING";
   private static final Boolean MUST_REQUEST_SPECIFIC_PART = true;
   private static final String PART_GROUP_NAME = "Part Group Name";
   private static final String PART_GROUP_NAME_NEW = "New Part Group Name";
   private static final BigDecimal QUANTITY = BigDecimal.valueOf( 15.67 );
   private static final RefInvClassKey INVENTORY_CLASS_CODE = RefInvClassKey.BATCH;
   private static final Id<ConfigSlot> CONFIGURATION_SLOT_ID =
         new Id<>( "10000000000000000000000000000001" );
   private static final Id<ConfigSlot> CONFIGURATION_SLOT_ID_UNKNOWN =
         new Id<>( "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" );
   private static final ApplicabilityRange PART_GROUP_APPLICABILITY_RANGE;
   private static final ApplicabilityRange PART_GROUP_APPLICABILITY_RANGE_NEW;
   private static final RefPurchTypeKey PURCHASE_TYPE_KEY = new RefPurchTypeKey( 1, "CMNHW" );
   private static final RefPurchTypeKey PURCHASE_TYPE_KEY_NEW = new RefPurchTypeKey( 2, "CONSUM" );
   private static final Boolean LINE_REPLACEABLE_UNIT = false;
   private static final String OTHER_CONDITIONS = "Some Other Conditions";
   private static final String OTHER_CONDITIONS_NEW = "Some New Other Conditions";
   private static final Integer INTERCHANGEABILITY_ORDER = 5;
   private static final Integer INTERCHANGEABILITY_ORDER_NEW = 6;

   private static final ApplicabilityRange ALTERNATE_PART_APPLICABILITY_RANGE;
   private static final ApplicabilityRange ALTERNATE_PART_APPLICABILITY_RANGE_NEW;
   private static final Boolean STANDARD = true;
   private static final Boolean APPROVED = true;
   private static final Boolean HAS_OTHER_CONDITIONS = true;
   private static final HumanResourceKey AUTHORIZING_HR = new HumanResourceKey( 4650, 1 );
   private static final String BASELINE_CODE = "baseline";
   private static final String BASELINE_CODE_NEW = "new baseline";
   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 4650, "ACFT_CD1" );

   private static final boolean CHECK_INCOMPATIBILITIES = true;

   private PartGroupSensitivityDao partGroupSensitivityDao;
   private PartGroupService iPartGroupService;

   static {
      // FakeJavaEeDependenciesRule is evaluating after the static block, so that StringBundles
      // instance is not stubbed at this point
      StringBundles.setSingleton( new FakeStringBundles() );
      try {
         PART_GROUP_APPLICABILITY_RANGE =
               new ApplicabilityRange( "01-50,200,300-400,10000000-10000001" );
         PART_GROUP_APPLICABILITY_RANGE_NEW =
               ApplicabilityRange.createRangeApplicableToEverything();

         ALTERNATE_PART_APPLICABILITY_RANGE = new ApplicabilityRange( "01-50" );
         ALTERNATE_PART_APPLICABILITY_RANGE_NEW = new ApplicabilityRange( "11-50" );
      } catch ( InvalidApplicabilityRangeException | StringTooLongException e ) {
         throw new RuntimeException( e );
      }
   }

   private static final PartGroupKey PART_GROUP_KEY_UPDATE = new PartGroupKey( 4650, 10 );
   private static final PartGroupKey PART_GROUP_KEY_WITH_SENSTIVITIES_TO_REMOVE =
         new PartGroupKey( 4650, 11 );
   private static final Id<PartGroup> PART_GROUP_KEY_WITH_SENSTIVITIES_TO_REMOVE_ID =
         new Id<PartGroup>( "00000000000000000000000000000011" );
   private static final Id<PartGroup> PART_GROUP_ID_UPDATE =
         new Id<PartGroup>( "00000000000000000000000000000010" );

   private static final RefSensitivityKey SENS_1 = new RefSensitivityKey( "SENS_1" );
   private static final RefSensitivityKey SENS_2 = new RefSensitivityKey( "SENS_2" );
   private static final RefSensitivityKey SENS_3 = new RefSensitivityKey( "SENS_3" );
   private static final RefSensitivityKey SENS_4 = new RefSensitivityKey( "SENS_4" );

   private static final Map<RefSensitivityKey, Boolean> SENSITIVITIES_FOR_UPDATE = new HashMap<>();
   {
      SENSITIVITIES_FOR_UPDATE.put( SENS_1, Boolean.TRUE );
      SENSITIVITIES_FOR_UPDATE.put( SENS_2, Boolean.FALSE );
      SENSITIVITIES_FOR_UPDATE.put( SENS_3, Boolean.TRUE );
      SENSITIVITIES_FOR_UPDATE.put( SENS_4, Boolean.TRUE );

   }

   private static final EditPartGroupTO EDITPARTGROUPTO_FOR_UPDATE = new EditPartGroupTO();
   {
      EDITPARTGROUPTO_FOR_UPDATE.setSensitivities( SENSITIVITIES_FOR_UPDATE );
      try {
         EDITPARTGROUPTO_FOR_UPDATE.setPartGroupCode( "Sensitive" );
         EDITPARTGROUPTO_FOR_UPDATE.setPartGroupName( "Sensitive" );
         EDITPARTGROUPTO_FOR_UPDATE.setLineReplaceableUnit( LINE_REPLACEABLE_UNIT );
         EDITPARTGROUPTO_FOR_UPDATE.setPurchaseType( PURCHASE_TYPE_KEY );
         EDITPARTGROUPTO_FOR_UPDATE.setQuantity( Double.valueOf( 1 ) );
      } catch ( PositiveValueException | MandatoryArgumentException
            | StringTooLongException aException ) {
         throw new RuntimeException( aException );
      }
      EDITPARTGROUPTO_FOR_UPDATE.setRequestSpecificPart( false );
      EDITPARTGROUPTO_FOR_UPDATE.setHumanResourceKey( AUTHORIZING_HR );
   }

   private static final List<RefSensitivityKey> SENSITIVITIES_AFTER_UPDATE =
         Arrays.asList( SENS_1, SENS_3, SENS_4 );


   @Before
   public void setUp() {
      iPartGroupService = new PartGroupService();
   }


   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   @Test
   public void set_PartGroup_Sensitivities() throws MxException {
      iPartGroupService.set( PART_GROUP_KEY_UPDATE, EDITPARTGROUPTO_FOR_UPDATE );
      PartGroup lPartGroupAfterUpdate = iPartGroupService.get( PART_GROUP_ID_UPDATE );
      assertThat( SENSITIVITIES_AFTER_UPDATE, is( lPartGroupAfterUpdate.getSensitivities() ) );
   }


   @Test
   public void removeAllSensitivities_fromPartGroup() throws MxException {
      iPartGroupService.removeAllSensitivities( PART_GROUP_KEY_WITH_SENSTIVITIES_TO_REMOVE );
      PartGroup partGroupAfterSensitivitiesRemoved =
            iPartGroupService.get( PART_GROUP_KEY_WITH_SENSTIVITIES_TO_REMOVE );
      assertThat( partGroupAfterSensitivitiesRemoved.getSensitivities(), is( empty() ) );
   }


   @Test
   public void delete_PartGroup_WithSensitivities() throws MxException {
      iPartGroupService.delete( PART_GROUP_KEY_WITH_SENSTIVITIES_TO_REMOVE );

      Optional<PartGroup> deletePartGroup =
            iPartGroupService.find( PART_GROUP_KEY_WITH_SENSTIVITIES_TO_REMOVE_ID );

      if ( deletePartGroup.isPresent() ) {
         fail( "Expected part group to be deleted." );
      }

      assertFalse( exists( PART_GROUP_KEY_WITH_SENSTIVITIES_TO_REMOVE, SENS_1 ) );
      assertFalse( exists( PART_GROUP_KEY_WITH_SENSTIVITIES_TO_REMOVE, SENS_2 ) );

   }


   private boolean exists( PartGroupKey partGroupKey, RefSensitivityKey sensitivity ) {
      partGroupSensitivityDao =
            InjectorContainer.get().getInstance( PartGroupSensitivityDao.class );
      PartGroupSensitivityTable partGroupSensitivties = partGroupSensitivityDao
            .findByPrimaryKey( new PartGroupSensitivityKey( partGroupKey, sensitivity ) );
      return partGroupSensitivties.exists();
   }


   @Test
   public void create_alternatePartBooleanProperties() throws Exception {
      AlternatePart lAlternatePart1 =
            defaultAlternatePartBuilder().partId( BATCH_PART_1 ).approved( APPROVED )
                  .standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS ).build();
      AlternatePart lAlternatePart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).approved( !APPROVED )
                  .standard( !STANDARD ).hasOtherConditions( !HAS_OTHER_CONDITIONS ).build();
      PartGroup lPartGroup = defaultPartGroupBuilder()
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2 ) ).build();

      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
      assertCreatedPartGroup( lPartGroupId, lPartGroup );
   }


   /**
    * This test highlights a bizarre core business logic whereby the quantity is reduced. There is
    * no outstanding documentation on this odd behavior. This test ensures that our code keeps the
    * odd behavior. See OPER-13175
    */
   @Test
   public void create_chopsQuantityInexplicably() throws Exception {
      PartGroup lPartGroup =
            defaultPartGroupBuilder().quantity( new BigDecimal( "12345678901234567890" ) ).build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      Optional<PartGroup> lFoundPartGroup = iPartGroupService.find( lPartGroupId );

      if ( !lFoundPartGroup.isPresent() ) {
         fail( "Expected part group could not be found." );
      }

      assertEquals( new BigDecimal( "12345678901234567000" ).toPlainString(),
            lFoundPartGroup.get().getQuantity().toPlainString() );
   }


   @Test
   public void create_lineReplaceableUnitIsFalse() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().lineReplaceableUnit( false ).build();

      assertCreatedPartGroup( iPartGroupService.create( lPartGroup, AUTHORIZING_HR ), lPartGroup );
   }


   @Test
   public void create_lineReplaceableUnitIsTrue() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().lineReplaceableUnit( true ).build();

      assertCreatedPartGroup( iPartGroupService.create( lPartGroup, AUTHORIZING_HR ), lPartGroup );
   }


   @Test
   public void create_mustRequestSpecificPartIsFalse() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().mustRequestSpecificPart( false ).build();

      assertCreatedPartGroup( iPartGroupService.create( lPartGroup, AUTHORIZING_HR ), lPartGroup );
   }


   @Test
   public void create_mustRequestSpecificPartIsTrue() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().mustRequestSpecificPart( true ).build();

      assertCreatedPartGroup( iPartGroupService.create( lPartGroup, AUTHORIZING_HR ), lPartGroup );
   }


   @Test
   public void create_standardPartCanBeSpecifiedOutOfOrder() throws Exception {
      AlternatePart lAlternatePart1 =
            defaultAlternatePartBuilder().partId( BATCH_PART_1 ).standard( !STANDARD ).build();
      AlternatePart lAlternatePart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).standard( STANDARD ).build();
      PartGroup lPartGroup = defaultPartGroupBuilder()
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2 ) ).build();

      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      assertCreatedPartGroup( lPartGroupId, lPartGroup );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void create_throwsExceptionWhenAuthorizedHrIsNotProvided() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().build();

      iPartGroupService.create( lPartGroup, null );
   }


   @Test( expected = PartNoHasPartReqException.class )
   public void create_throwsExceptionWhenConfigurationSlotIsSoftwareAndAlternatePartHasPartRequest()
         throws Exception {
      AlternatePart lAlternatePartWithPartRequest =
            defaultAlternatePartBuilder().partId( PART_10_WITH_PART_REQUEST ).build();
      PartGroup lPartGroup =
            defaultPartGroupBuilder().alternatePart( lAlternatePartWithPartRequest )
                  .configurationSlotId( CONFIGURATION_SLOT_ID ).build();

      iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
   }


   @Test( expected = PartNoHasPartReqException.class )
   public void
         update_throwsExceptionWhenConfigurationSlotIsSoftwareAndAlternatePartToCreateHasPartRequest()
               throws Exception {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();
      AlternatePart lAlternatePartWithPartRequest =
            defaultAlternatePartBuilder().partId( PART_10_WITH_PART_REQUEST ).build();
      PartGroup lPartGroup =
            PartGroup.builder( lCreatedPartGroup ).alternatePart( lAlternatePartWithPartRequest )
                  .configurationSlotId( CONFIGURATION_SLOT_ID ).build();

      iPartGroupService.update( lPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = InvalidPartStatusException.class )
   public void create_throwsExceptionWhenAlternatePartIsObsolete() throws Exception {
      AlternatePart lObsoleteAlternatePart =
            defaultAlternatePartBuilder().partId( PART_11_OBSLT ).build();
      PartGroup lPartGroup =
            defaultPartGroupBuilder().alternatePart( lObsoleteAlternatePart ).build();

      iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
   }


   @Test( expected = InputCodeException.class )
   public void create_throwsExceptionWhenCodeAlreadyExistsForAssembly() throws Exception {
      PartGroup lPartGroup1 = defaultPartGroupBuilder().build();
      PartGroup lPartGroup2 = defaultPartGroupBuilder().build();
      iPartGroupService.create( lPartGroup1, AUTHORIZING_HR );

      iPartGroupService.create( lPartGroup2, AUTHORIZING_HR );
   }


   @Test( expected = PartsNotFoundException.class )
   public void create_throwsExceptionWhenPartGroupHasAlternatePartWhichDoesNotExist()
         throws Exception {
      AlternatePart lAlternatePart = defaultAlternatePartBuilder().partId( PART_UNKNOWN ).build();
      PartGroup lPartGroup = defaultPartGroupBuilder().alternatePart( lAlternatePart ).build();

      iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
   }


   @Test( expected = ConfigurationSlotNotFoundException.class )
   public void create_throwsExceptionWhenUnknownConfigurationSlotIsReferenced() throws Exception {
      PartGroup lPartGroup =
            defaultPartGroupBuilder().configurationSlotId( CONFIGURATION_SLOT_ID_UNKNOWN ).build();

      iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
   }


   @Test
   public void
         create_throwsProperlyConstructedExceptionWithAlternatePartAndPartGroupInventoryClassMismatch()
               throws Exception {
      AlternatePart lAlternatePart1 = defaultAlternatePartBuilder().partId( BATCH_PART_1 ).build();
      AlternatePart lAlternatePart2 =
            defaultAlternatePartBuilder().partId( TRACK_PART ).standard( false ).build();
      PartGroup lPartGroup = defaultPartGroupBuilder().inventoryClassKey( RefInvClassKey.SER )
            .quantity( BigDecimal.ONE )
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2 ) ).build();

      try {
         iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
      } catch ( InventoryClassMismatchException e ) {
         assertInventoryClassMismatchException( lPartGroup, e );
      }
   }


   @Test( expected = IetmAssemblyMapDoesNotExistException.class )
   public void create_throwsExceptionWhenIetmTopicKeyExistsButIetmAssemblyMapDoesNotExist()
         throws Exception {
      PartGroup lPartGroup =
            defaultPartGroupBuilder().ietmTopicKey( IETM_TOPIC_EXISTS_NOT_ON_ASSEMBLY_KEY ).build();

      iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
   }


   /**
    * API create does not support KIT part groups, which is different from legacy implementation in
    * core transaction engine
    */
   @Test( expected = InvalidInventoryClassException.class )
   public void create_throwsExceptionWithExistingUnsupportedInventoryClass()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lPartGroup = defaultPartGroupBuilder().inventoryClassKey( RefInvClassKey.KIT )
            .quantity( BigDecimal.ONE ).build();

      iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
   }


   @Test
   public void create_withInventoryClassSER() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().inventoryClassKey( RefInvClassKey.SER )
            .quantity( BigDecimal.TEN ).build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      assertCreatedPartGroup( lPartGroupId, lPartGroup );
   }


   @Test
   public void create_withInventoryClassBATCH() throws Exception {
      PartGroup lPartGroup =
            defaultPartGroupBuilder().inventoryClassKey( RefInvClassKey.BATCH ).build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      assertCreatedPartGroup( lPartGroupId, lPartGroup );
   }


   @Test
   public void create_withAlternatePart() throws Exception {
      AlternatePart lAlternatePart = defaultAlternatePartBuilder().build();
      PartGroup lPartGroup = defaultPartGroupBuilder().alternatePart( lAlternatePart ).build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      assertCreatedPartGroup( lPartGroupId, lPartGroup );
   }


   @Test
   public void create_withAlternatePartsUsingOnlyNonNullableFields() throws Exception {
      AlternatePart lAlternatePart1 = new AlternatePart.Builder().partId( BATCH_PART_1 )
            .interchangeabilityOrder( INTERCHANGEABILITY_ORDER ).approved( APPROVED )
            .applicabilityRange( new ApplicabilityRange( null ) ).standard( STANDARD )
            .baselineCode( null ).hasOtherConditions( HAS_OTHER_CONDITIONS ).build();

      AlternatePart lAlternatePart2 = new AlternatePart.Builder().partId( PART_2 )
            .interchangeabilityOrder( INTERCHANGEABILITY_ORDER ).approved( APPROVED )
            .applicabilityRange( new ApplicabilityRange( null ) ).standard( !STANDARD )
            .baselineCode( null ).hasOtherConditions( HAS_OTHER_CONDITIONS ).build();

      PartGroup lPartGroup = defaultPartGroupBuilder()
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2 ) ).build();

      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      assertCreatedPartGroup( lPartGroupId, lPartGroup );
   }


   @Test
   public void create_withMultipleAlternateParts() throws Exception {
      List<AlternatePart> lAlternateParts = defaultAlternateParts();
      PartGroup lPartGroup = defaultPartGroupBuilder().alternateParts( lAlternateParts ).build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      assertCreatedPartGroup( lPartGroupId, lPartGroup );
   }


   @Test
   public void create_withNonNullableFieldsAndNoAlternateParts() throws Exception {
      PartGroup lPartGroup = PartGroup.builder().assembly( ASSEMBLY_KEY ).code( PART_GROUP_CODE )
            .name( PART_GROUP_NAME ).quantity( QUANTITY ).inventoryClassKey( INVENTORY_CLASS_CODE )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( new ApplicabilityRange( null ) ).purchaseTypeKey( null )
            .lineReplaceableUnit( LINE_REPLACEABLE_UNIT ).otherConditions( null )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Collections.<AlternatePart>emptyList() ).build();

      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      assertCreatedPartGroup( lPartGroupId, lPartGroup );
   }


   @Test
   public void create_withoutAlternateParts() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().build();
      assertCreatedPartGroup( iPartGroupService.create( lPartGroup, AUTHORIZING_HR ), lPartGroup );
   }


   @Test
   public void set_trackedPartGroup() throws Throwable {
      EditPartGroupTO lEditPartGroupTO = new EditPartGroupTO();
      {
         lEditPartGroupTO
               .setApplicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() );
         lEditPartGroupTO.setPartGroupCode( PART_GROUP_CODE_NEW );
         lEditPartGroupTO.setPartGroupName( PART_GROUP_NAME_NEW );
         lEditPartGroupTO.setIetmTopicShortDescription( IETM_SHORT_DESCRIPTION );
         lEditPartGroupTO.setLineReplaceableUnit( !LINE_REPLACEABLE_UNIT );
         lEditPartGroupTO.setPurchaseType( PURCHASE_TYPE_KEY_NEW );
         lEditPartGroupTO.setQuantity( Double.valueOf( 1 ) );
         lEditPartGroupTO.setRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART );
      }

      iPartGroupService.set( EXISTING_TRK_PART_GROUP_KEY, lEditPartGroupTO );

      EqpBomPart lEqpBomPart = EqpBomPart.findByPrimaryKey( EXISTING_TRK_PART_GROUP_KEY );

      assertThat( lEqpBomPart.getApplEffLdesc(),
            is( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() ) );
      assertThat( lEqpBomPart.getBomPartCd(), equalToIgnoringCase( PART_GROUP_CODE_NEW ) );
      assertThat( lEqpBomPart.getBomPartName(), is( PART_GROUP_NAME_NEW ) );
      assertThat( lEqpBomPart.getIetmTopic(), is( IETM_TOPIC_EXISTS_ON_ASSEMBLY_KEY ) );
      assertThat( lEqpBomPart.isLineReplaceableUnit(), is( !LINE_REPLACEABLE_UNIT ) );
      assertThat( lEqpBomPart.getPurchType(), is( PURCHASE_TYPE_KEY_NEW ) );
      assertThat( lEqpBomPart.getPartQt(), is( Double.valueOf( 1 ) ) );
   }


   @Test
   public void set_serializedPartGroup() throws Throwable {
      EditPartGroupTO lEditPartGroupTO = new EditPartGroupTO();
      {
         lEditPartGroupTO
               .setApplicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() );
         lEditPartGroupTO.setPartGroupCode( PART_GROUP_CODE_NEW );
         lEditPartGroupTO.setPartGroupName( PART_GROUP_NAME_NEW );
         lEditPartGroupTO.setIetmTopicShortDescription( IETM_SHORT_DESCRIPTION );
         lEditPartGroupTO.setLineReplaceableUnit( !LINE_REPLACEABLE_UNIT );
         lEditPartGroupTO.setPurchaseType( PURCHASE_TYPE_KEY_NEW );
         lEditPartGroupTO.setQuantity( Double.valueOf( 1 ) );
         lEditPartGroupTO.setRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART );
      }

      iPartGroupService.set( EXISTING_SERIALIZED_PART_GROUP_KEY, lEditPartGroupTO );

      EqpBomPart lEqpBomPart = EqpBomPart.findByPrimaryKey( EXISTING_SERIALIZED_PART_GROUP_KEY );

      assertThat( lEqpBomPart.getApplEffLdesc(),
            is( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() ) );
      assertThat( lEqpBomPart.getBomPartCd(), equalToIgnoringCase( PART_GROUP_CODE_NEW ) );
      assertThat( lEqpBomPart.getBomPartName(), is( PART_GROUP_NAME_NEW ) );
      assertThat( lEqpBomPart.getIetmTopic(), is( IETM_TOPIC_EXISTS_ON_ASSEMBLY_KEY ) );
      assertThat( lEqpBomPart.isLineReplaceableUnit(), is( !LINE_REPLACEABLE_UNIT ) );
      assertThat( lEqpBomPart.getPurchType(), is( PURCHASE_TYPE_KEY_NEW ) );
      assertThat( lEqpBomPart.getPartQt(), is( Double.valueOf( 1 ) ) );
   }


   @Test
   public void set_batchPartGroup() throws Throwable {
      EditPartGroupTO lEditPartGroupTO = new EditPartGroupTO();
      {
         lEditPartGroupTO
               .setApplicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() );
         lEditPartGroupTO.setPartGroupCode( PART_GROUP_CODE_NEW );
         lEditPartGroupTO.setPartGroupName( PART_GROUP_NAME_NEW );
         lEditPartGroupTO.setIetmTopicShortDescription( IETM_SHORT_DESCRIPTION );
         lEditPartGroupTO.setLineReplaceableUnit( !LINE_REPLACEABLE_UNIT );
         lEditPartGroupTO.setPurchaseType( PURCHASE_TYPE_KEY_NEW );
         lEditPartGroupTO.setQuantity( QUANTITY.doubleValue() );
         lEditPartGroupTO.setRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART );
      }

      iPartGroupService.set( EXISTING_BATCH_PART_GROUP_KEY, lEditPartGroupTO );

      EqpBomPart lEqpBomPart = EqpBomPart.findByPrimaryKey( EXISTING_BATCH_PART_GROUP_KEY );

      assertThat( lEqpBomPart.getApplEffLdesc(),
            is( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() ) );
      assertThat( lEqpBomPart.getBomPartCd(), equalToIgnoringCase( PART_GROUP_CODE_NEW ) );
      assertThat( lEqpBomPart.getBomPartName(), is( PART_GROUP_NAME_NEW ) );
      assertThat( lEqpBomPart.getIetmTopic(), is( IETM_TOPIC_EXISTS_ON_ASSEMBLY_KEY ) );
      assertThat( lEqpBomPart.isLineReplaceableUnit(), is( !LINE_REPLACEABLE_UNIT ) );
      assertThat( lEqpBomPart.getPurchType(), is( PURCHASE_TYPE_KEY_NEW ) );
      assertThat( lEqpBomPart.getPartQt(), is( QUANTITY.doubleValue() ) );
   }


   @Test
   public void set_kitPartGroup() throws Throwable {
      EditPartGroupTO lEditPartGroupTO = new EditPartGroupTO();
      {
         lEditPartGroupTO
               .setApplicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() );
         lEditPartGroupTO.setPartGroupCode( PART_GROUP_CODE_NEW );
         lEditPartGroupTO.setPartGroupName( PART_GROUP_NAME_NEW );
         lEditPartGroupTO.setIetmTopicShortDescription( IETM_SHORT_DESCRIPTION );
         lEditPartGroupTO.setLineReplaceableUnit( !LINE_REPLACEABLE_UNIT );
         lEditPartGroupTO.setPurchaseType( PURCHASE_TYPE_KEY_NEW );
         lEditPartGroupTO.setQuantity( Double.valueOf( 1 ) );
         lEditPartGroupTO.setRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART );
      }

      iPartGroupService.set( EXISTING_KIT_PART_GROUP_KEY, lEditPartGroupTO );

      EqpBomPart lEqpBomPart = EqpBomPart.findByPrimaryKey( EXISTING_KIT_PART_GROUP_KEY );

      assertThat( lEqpBomPart.getApplEffLdesc(),
            is( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() ) );
      assertThat( lEqpBomPart.getBomPartCd(), equalToIgnoringCase( PART_GROUP_CODE_NEW ) );
      assertThat( lEqpBomPart.getBomPartName(), is( PART_GROUP_NAME_NEW ) );
      assertThat( lEqpBomPart.getIetmTopic(), is( IETM_TOPIC_EXISTS_ON_ASSEMBLY_KEY ) );
      assertThat( lEqpBomPart.isLineReplaceableUnit(), is( !LINE_REPLACEABLE_UNIT ) );
      assertThat( lEqpBomPart.getPurchType(), is( PURCHASE_TYPE_KEY_NEW ) );
      assertThat( lEqpBomPart.getPartQt(), is( Double.valueOf( 1 ) ) );
   }


   @Test
   public void set_aircraftPartGroup() throws Throwable {
      EditPartGroupTO lEditPartGroupTO = new EditPartGroupTO();
      {
         lEditPartGroupTO
               .setApplicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() );
         lEditPartGroupTO.setPartGroupCode( PART_GROUP_CODE_NEW );
         lEditPartGroupTO.setPartGroupName( PART_GROUP_NAME_NEW );
         lEditPartGroupTO.setIetmTopicShortDescription( IETM_SHORT_DESCRIPTION );
         lEditPartGroupTO.setPurchaseType( PURCHASE_TYPE_KEY_NEW );
         lEditPartGroupTO.setQuantity( Double.valueOf( 1 ) );
         lEditPartGroupTO.setRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART );
      }

      iPartGroupService.set( EXISTING_AIRCRAFT_PART_GROUP_KEY, lEditPartGroupTO );

      EqpBomPart lEqpBomPart = EqpBomPart.findByPrimaryKey( EXISTING_AIRCRAFT_PART_GROUP_KEY );

      assertThat( lEqpBomPart.getApplEffLdesc(),
            is( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() ) );
      assertThat( lEqpBomPart.getBomPartCd(), equalToIgnoringCase( PART_GROUP_CODE_NEW ) );
      assertThat( lEqpBomPart.getBomPartName(), is( PART_GROUP_NAME_NEW ) );
      assertThat( lEqpBomPart.getIetmTopic(), is( IETM_TOPIC_EXISTS_ON_ASSEMBLY_KEY ) );
      assertThat( lEqpBomPart.getPurchType(), is( PURCHASE_TYPE_KEY_NEW ) );
      assertThat( lEqpBomPart.getPartQt(), is( Double.valueOf( 1 ) ) );
   }


   @Test
   public void set_assemblyPartGroup() throws Throwable {
      EditPartGroupTO lEditPartGroupTO = new EditPartGroupTO();
      {
         lEditPartGroupTO
               .setApplicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() );
         lEditPartGroupTO.setPartGroupCode( PART_GROUP_CODE_NEW );
         lEditPartGroupTO.setPartGroupName( PART_GROUP_NAME_NEW );
         lEditPartGroupTO.setIetmTopicShortDescription( IETM_SHORT_DESCRIPTION );
         lEditPartGroupTO.setPurchaseType( PURCHASE_TYPE_KEY_NEW );
         lEditPartGroupTO.setQuantity( Double.valueOf( 1 ) );
         lEditPartGroupTO.setRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART );
      }

      iPartGroupService.set( EXISTING_ASSEMBLY_PART_GROUP_KEY, lEditPartGroupTO );

      EqpBomPart lEqpBomPart = EqpBomPart.findByPrimaryKey( EXISTING_ASSEMBLY_PART_GROUP_KEY );

      assertThat( lEqpBomPart.getApplEffLdesc(),
            is( PART_GROUP_APPLICABILITY_RANGE_NEW.getExpression() ) );
      assertThat( lEqpBomPart.getBomPartCd(), equalToIgnoringCase( PART_GROUP_CODE_NEW ) );
      assertThat( lEqpBomPart.getBomPartName(), is( PART_GROUP_NAME_NEW ) );
      assertThat( lEqpBomPart.getIetmTopic(), is( IETM_TOPIC_EXISTS_ON_ASSEMBLY_KEY ) );
      assertThat( lEqpBomPart.getPurchType(), is( PURCHASE_TYPE_KEY_NEW ) );
      assertThat( lEqpBomPart.getPartQt(), is( Double.valueOf( 1 ) ) );
   }


   @Test
   public void find_byIdReturnsEmptyWhenPartGroupDoesNotExist() throws Exception {
      Optional<PartGroup> lPartGroup =
            iPartGroupService.find( new Id<PartGroup>( UUID.randomUUID() ) );
      assertTrue( !lPartGroup.isPresent() );
      assertEquals( Optional.empty(), lPartGroup );
   }


   @Test
   public void find_byCodeAndConfigurationSlotIdReturnsEmptyWhenPartGroupDoesNotExist()
         throws Exception {
      Optional<PartGroup> lPartGroup =
            iPartGroupService.find( new Id<ConfigSlot>( UUID.randomUUID() ), "notexisting" );
      assertTrue( !lPartGroup.isPresent() );
      assertEquals( Optional.empty(), lPartGroup );
   }


   @Test
   public void find_existingPartGroup() throws Exception {
      AlternatePart lAlternatePart1 =
            defaultAlternatePartBuilder().partId( BATCH_PART_1 ).standard( !STANDARD ).build();
      AlternatePart lAlternatePart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).standard( STANDARD ).build();
      AlternatePart lAlternatePart3 =
            defaultAlternatePartBuilder().partId( PART_3 ).standard( !STANDARD ).build();
      PartGroup lExpectedPartGroup = defaultPartGroupBuilder()
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2, lAlternatePart3 ) )
            .build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lExpectedPartGroup, AUTHORIZING_HR );
      PartGroup lCreatedPartGroup = iPartGroupService.get( lPartGroupId );

      Optional<PartGroup> lPartGroup = iPartGroupService.find( lPartGroupId );

      if ( !lPartGroup.isPresent() ) {
         fail( "Expected part group could not be found." );
      }

      assertEquals( PartGroup.builder( lExpectedPartGroup ).id( lCreatedPartGroup.getId().get() )
            .key( lCreatedPartGroup.getKey().get() ).build(), lPartGroup.get() );
   }


   @Test
   public void find_existingPartGroupByCodeAndConfigurationSlotId() throws Exception {
      AlternatePart lAlternatePart1 =
            defaultAlternatePartBuilder().partId( BATCH_PART_1 ).standard( !STANDARD ).build();
      AlternatePart lAlternatePart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).standard( STANDARD ).build();
      AlternatePart lAlternatePart3 =
            defaultAlternatePartBuilder().partId( PART_3 ).standard( !STANDARD ).build();
      PartGroup lExpectedPartGroup = defaultPartGroupBuilder()
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2, lAlternatePart3 ) )
            .build();
      Id<PartGroup> lExpectedPartGroupId =
            iPartGroupService.create( lExpectedPartGroup, AUTHORIZING_HR );
      PartGroup lCreatedPartGroup = iPartGroupService.get( lExpectedPartGroupId );

      Optional<PartGroup> lPartGroup =
            iPartGroupService.find( CONFIGURATION_SLOT_ID, PART_GROUP_CODE );

      if ( !lPartGroup.isPresent() ) {
         fail( "Expected part group could not be found." );
      }

      assertEquals( PartGroup.builder( lExpectedPartGroup ).id( lCreatedPartGroup.getId().get() )
            .key( lCreatedPartGroup.getKey().get() ).build(), lPartGroup.get() );
   }


   @Test
   public void get_existingPartGroup() throws Exception {
      AlternatePart lAlternatePart1 =
            defaultAlternatePartBuilder().partId( BATCH_PART_1 ).standard( !STANDARD ).build();
      AlternatePart lAlternatePart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).standard( STANDARD ).build();
      AlternatePart lAlternatePart3 =
            defaultAlternatePartBuilder().partId( PART_3 ).standard( !STANDARD ).build();
      PartGroup lExpectedPartGroup = defaultPartGroupBuilder()
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2, lAlternatePart3 ) )
            .build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lExpectedPartGroup, AUTHORIZING_HR );
      PartGroup lCreatedPartGroup = iPartGroupService.find( lPartGroupId ).get();

      PartGroup lPartGroup = iPartGroupService.get( lPartGroupId );

      assertEquals( PartGroup.builder( lExpectedPartGroup ).id( lPartGroupId )
            .key( lCreatedPartGroup.getKey().get() ).build(), lPartGroup );
   }


   @Test
   public void get_existingPartGroupByCodeAndConfigurationSlotId() throws Exception {
      AlternatePart lAlternatePart1 =
            defaultAlternatePartBuilder().partId( BATCH_PART_1 ).standard( !STANDARD ).build();
      AlternatePart lAlternatePart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).standard( STANDARD ).build();
      AlternatePart lAlternatePart3 =
            defaultAlternatePartBuilder().partId( PART_3 ).standard( !STANDARD ).build();
      PartGroup lExpectedPartGroup = defaultPartGroupBuilder()
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2, lAlternatePart3 ) )
            .build();
      Id<PartGroup> lExpectedPartGroupId =
            iPartGroupService.create( lExpectedPartGroup, AUTHORIZING_HR );
      PartGroup lExistingPartGroup = iPartGroupService.find( lExpectedPartGroupId ).get();

      PartGroup lPartGroup = iPartGroupService.get( CONFIGURATION_SLOT_ID, PART_GROUP_CODE );

      assertEquals( PartGroup.builder( lExpectedPartGroup ).id( lExpectedPartGroupId )
            .key( lExistingPartGroup.getKey().get() ).build(), lPartGroup );
   }


   @Test( expected = NullPointerException.class )
   public void get_throwsExceptionWhenConfigurationSlotIdIsNull() throws Exception {
      iPartGroupService.get( ( Id<ConfigSlot> ) null, PART_GROUP_CODE );
   }


   @Test( expected = NullPointerException.class )
   public void get_throwsExceptionWhenIdIsNull() throws Exception {
      iPartGroupService.get( ( Id<PartGroup> ) null );
   }


   @Test( expected = NullPointerException.class )
   public void get_throwsExceptionWhenPartGroupCodeIsNull() throws Exception {
      iPartGroupService.get( CONFIGURATION_SLOT_ID, null );
   }


   /**
    * Validates update part group properties and alternate parts list
    */
   @Test
   public void update_partGroupAndAlternateParts()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lCreatedPartGroup = createDefaultPartGroupAndPart();

      AlternatePart lAlternatePart3 =
            defaultAlternatePartBuilder().partId( PART_3 ).standard( !STANDARD ).build();
      AlternatePart lNewAlternatePart =
            defaultAlternatePartBuilder().partId( PART_4 ).standard( STANDARD ).build();
      PartGroup lUpdatedPartGroup = PartGroup.builder( lCreatedPartGroup )
            .code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_KEY_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Arrays.asList( lAlternatePart3, lNewAlternatePart ) ).build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_applicabilityRange()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();

      PartGroup lUpdatedPartGroup = PartGroup.builder( lCreatedPartGroup )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW ).build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_code() throws TriggerException, KeyConversionException, MxException {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();

      PartGroup lUpdatedPartGroup =
            PartGroup.builder( lCreatedPartGroup ).code( PART_GROUP_CODE_NEW ).build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_inUseAlternatePart() throws Exception {
      AlternatePart lAlternatePart = defaultAlternatePartBuilder().partId( IN_USE_PART_5 )
            .interchangeabilityOrder( INTERCHANGEABILITY_ORDER_NEW )
            .applicabilityRange( ALTERNATE_PART_APPLICABILITY_RANGE_NEW ).approved( !APPROVED )
            .standard( STANDARD ).hasOtherConditions( !HAS_OTHER_CONDITIONS )
            .baselineCode( BASELINE_CODE_NEW ).build();
      PartGroup lUpdatedPartGroup =
            defaultPartGroupBuilder().id( EXISTING_PART_GROUP_ID ).key( EXISTING_PART_GROUP_KEY )
                  .code( PART_GROUP_CODE_EXISTING ).alternatePart( lAlternatePart ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( EXISTING_PART_GROUP_ID, lUpdatedPartGroup );
   }


   @Test
   public void update_lineReplaceableUnit()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();

      PartGroup lUpdatedPartGroup = PartGroup.builder( lCreatedPartGroup )
            .lineReplaceableUnit( !LINE_REPLACEABLE_UNIT ).build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_mustRequestSpecificPart()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();

      PartGroup lUpdatedPartGroup = PartGroup.builder( lCreatedPartGroup )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_name() throws TriggerException, KeyConversionException, MxException {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();

      PartGroup lUpdatedPartGroup =
            PartGroup.builder( lCreatedPartGroup ).name( PART_GROUP_NAME_NEW ).build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_otherConditions()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();

      PartGroup lUpdatedPartGroup =
            PartGroup.builder( lCreatedPartGroup ).otherConditions( OTHER_CONDITIONS_NEW ).build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_standardAlternatePart()
         throws TriggerException, KeyConversionException, MxException {
      // PART_2 is the standard part
      PartGroup lCreatedPartGroup = createDefaultPartGroupAndPart();

      AlternatePart lNewAlternatePart1 =
            defaultAlternatePartBuilder().partId( BATCH_PART_1 ).standard( !STANDARD ).build();
      AlternatePart lNewAlternatePart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).standard( !STANDARD ).build();
      AlternatePart lNewAlternatePart3 =
            defaultAlternatePartBuilder().partId( PART_3 ).standard( STANDARD ).build();
      PartGroup lUpdatedPartGroup = PartGroup.builder( lCreatedPartGroup )
            .alternateParts(
                  Arrays.asList( lNewAlternatePart1, lNewAlternatePart2, lNewAlternatePart3 ) )
            .build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_alternatePartToRemoveWithCompatibilityRuleNoIncompatibilitiesCheck()
         throws Exception {
      // Part group(EXISTING_PART_GROUP_ID_WITH_COMPATIBILITY_RULE) has an alternate
      // part(part_id=12) which is incompatible, here we are removing it.
      PartGroup lUpdatedPartGroup =
            defaultPartGroupBuilder().id( EXISTING_PART_GROUP_ID_WITH_COMPATIBILITY_RULE )
                  .key( EXISTING_PART_GROUP_ID_WITH_COMPATIBILITY_RULE_KEY ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, !CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lUpdatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void update_throwsExceptionWhenAuthorizedHrIsNotProvided() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().build();

      iPartGroupService.update( lPartGroup, null, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = PartsNotFoundException.class )
   public void update_throwsExceptionWhenPartGroupHasAlternatePartWhichDoesNotExist()
         throws Exception {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();
      AlternatePart lAlternatePart = defaultAlternatePartBuilder().partId( PART_UNKNOWN ).build();
      PartGroup lPartGroup =
            PartGroup.builder( lCreatedPartGroup ).alternatePart( lAlternatePart ).build();

      iPartGroupService.update( lPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = InputCodeException.class )
   public void update_throwsExceptionWhenCodeAlreadyExistsForAssembly() throws Exception {
      PartGroup lPartGroup1 = defaultPartGroupBuilder().code( "code1" ).build();
      PartGroup lPartGroup2 = defaultPartGroupBuilder().code( "code2" ).build();
      iPartGroupService.create( lPartGroup1, AUTHORIZING_HR );
      Id<PartGroup> lPartGroup2Id = iPartGroupService.create( lPartGroup2, AUTHORIZING_HR );

      PartGroup lPartGroup2ToUpdate =
            defaultPartGroupBuilder().id( lPartGroup2Id ).code( "code1" ).build();
      iPartGroupService.update( lPartGroup2ToUpdate, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = InvalidPartStatusException.class )
   public void update_throwsExceptionWhenAlternatePartIsObsolete() throws Exception {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();
      AlternatePart lObsoleteAlternatePart =
            defaultAlternatePartBuilder().partId( PART_11_OBSLT ).build();
      PartGroup lPartGroup =
            PartGroup.builder( lCreatedPartGroup ).alternatePart( lObsoleteAlternatePart ).build();

      iPartGroupService.update( lPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = PartNoBomPartInUseException.class )
   public void update_throwsExceptionWhenInUseAlternatePartToRemoveFromPartGroup()
         throws Exception {
      PartGroup lUpdatedPartGroup = defaultPartGroupBuilder().id( EXISTING_PART_GROUP_ID )
            .key( EXISTING_PART_GROUP_KEY ).code( PART_GROUP_CODE_EXISTING ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = CompatibilityRuleExistsException.class )
   public void update_throwsExceptionWhenAlternatePartToRemoveHasCompatibilityRule()
         throws Exception {
      // Part group(EXISTING_PART_GROUP_ID_WITH_COMPATIBILITY_RULE) has an alternate
      // part(part_id=12) which is incompatible, here we are removing it.
      PartGroup lUpdatedPartGroup =
            defaultPartGroupBuilder().id( EXISTING_PART_GROUP_ID_WITH_COMPATIBILITY_RULE ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = InstalledInventoryNotApplicableException.class )
   public void
         update_throwsExceptionWhenInventoryClassIsASSYAndInstalledInventoryBecomesInapplicable()
               throws MxException, TriggerException, KeyConversionException {
      PartGroup lUpdatedPartGroup = defaultPartGroupBuilder().id( EXISTING_ASSY_PART_GROUP_ID )
            .quantity( BigDecimal.ONE ).inventoryClassKey( RefInvClassKey.ASSY )
            .applicabilityRange( new ApplicabilityRange( "A16" ) ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = InstalledInventoryNotApplicableException.class )
   public void
         update_throwsExceptionWhenInventoryClassIsTRKAndInstalledInventoryBecomesInapplicable()
               throws MxException, TriggerException, KeyConversionException {
      PartGroup lUpdatedPartGroup =
            defaultPartGroupBuilder().id( EXISTING_TRK_PART_GROUP_ID_WITH_APPLICABILITY )
                  .quantity( BigDecimal.ONE ).inventoryClassKey( RefInvClassKey.TRK )
                  .applicabilityRange( new ApplicabilityRange( "A16" ) ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   /**
    * API update does not support KIT part groups, which is different from legacy implementation in
    * core transaction engine
    */
   @Test( expected = IllegalArgumentException.class )
   public void update_throwsExceptionWhenInventoryClassIsKIT()
         throws MxException, TriggerException, KeyConversionException {
      PartGroup lUpdatedPartGroup = defaultPartGroupBuilder().id( EXISTING_KIT_PART_GROUP_ID )
            .quantity( BigDecimal.ONE ).inventoryClassKey( RefInvClassKey.KIT ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = InvalidInventoryClassException.class )
   public void update_throwsExceptionWhenLineReplaceableUnitIsTrueForInventoryClassACFT()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lUpdatedPartGroup = defaultPartGroupBuilder().id( EXISTING_ACFT_PART_GROUP_ID )
            .quantity( BigDecimal.ONE ).inventoryClassKey( RefInvClassKey.ACFT )
            .lineReplaceableUnit( !LINE_REPLACEABLE_UNIT ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = IetmAssemblyMapDoesNotExistException.class )
   public void update_throwsExceptionWhenIetmTopicKeyExistsButIetmAssemblyMapDoesNotExist()
         throws Exception {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();
      PartGroup lPartGroup = PartGroup.builder( lCreatedPartGroup )
            .ietmTopicKey( IETM_TOPIC_EXISTS_NOT_ON_ASSEMBLY_KEY ).build();

      iPartGroupService.update( lPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = IllegalArgumentException.class )
   public void update_throwsExceptionWhenIdIsNull() throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().id( null )
            .alternateParts( Collections.<AlternatePart>emptyList() ).build();

      iPartGroupService.update( lPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = PartGroupNotFoundException.class )
   public void update_throwsExceptionWhenPartGroupDoesNotExist() throws Exception {
      Id<PartGroup> lPartGroupId = new Id<PartGroup>( UUID.randomUUID() );
      PartGroup lPartGroup = defaultPartGroupBuilder().id( lPartGroupId )
            .alternateParts( Collections.<AlternatePart>emptyList() ).build();

      iPartGroupService.update( lPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = ReadOnlyPropertyException.class )
   public void update_throwsExceptionWhenReadOnlyPropertiesAreChanged() throws Exception {
      PartGroup lCreatedPartGroup = defaultPartGroupBuilder().build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lCreatedPartGroup, AUTHORIZING_HR );

      PartGroup lUpdatedPartGroup = defaultPartGroupBuilder().id( lPartGroupId )
            .quantity( BigDecimal.valueOf( 2 ) ).inventoryClassKey( RefInvClassKey.SER )
            .configurationSlotId( CONFIGURATION_SLOT_ID_UNKNOWN ).build();

      try {
         iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
      } catch ( ReadOnlyPropertyException e ) {
         HashSet<String> lExpectedProperties = new HashSet<>();
         lExpectedProperties
               .addAll( Arrays.asList( "inventoryClassCode", "configurationSlotId", "quantity" ) );
         assertEquals( lExpectedProperties, e.getData().getProperties() );
         throw e;
      }
   }


   @Test
   public void
         update_throwsProperlyConstructedExceptionWithAlternatePartAndPartGroupInventoryClassMismatch()
               throws Exception {
      PartGroup lPartGroup = defaultPartGroupBuilder().quantity( BigDecimal.ONE )
            .inventoryClassKey( RefInvClassKey.SER ).build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );

      AlternatePart lAlternatePart1 = defaultAlternatePartBuilder().partId( BATCH_PART_1 ).build();
      AlternatePart lAlternatePart2 =
            defaultAlternatePartBuilder().partId( TRACK_PART ).standard( false ).build();

      PartGroup lUpdatedPartGroup = defaultPartGroupBuilder().id( lPartGroupId )
            .quantity( BigDecimal.ONE ).inventoryClassKey( RefInvClassKey.SER )
            .alternateParts( Arrays.asList( lAlternatePart1, lAlternatePart2 ) ).build();

      try {
         iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
      } catch ( InventoryClassMismatchException e ) {
         assertInventoryClassMismatchException( lPartGroup, e );
      }
   }


   private void assertInventoryClassMismatchException( PartGroup aPartGroup,
         InventoryClassMismatchException aException ) {
      assertNotNull( "Expected InventoryClassMismatchException to not be null", aException );
      assertEquals( aPartGroup.getInventoryClassKey(), aException.getPartGroupInventoryClass() );
      Map<Part.Key, RefInvClassKey> lPartToInventoryClassCodeMap =
            aException.getPartToInventoryClassCodeMap();
      assertNotNull( "thrown InventoryClassMismatchException's partToInventoryClassCodeMap is null",
            lPartToInventoryClassCodeMap );
      assertEquals( 2, lPartToInventoryClassCodeMap.size() );
      Part.Key lExpectedPart1 = new Part.Key( "OEM_PART_NUMBER_1", "ABC" );
      Part.Key lExpectedPart2 = new Part.Key( "OEM_PART_NUMBER_6", "ABC" );
      assertEquals( RefInvClassKey.BATCH, lPartToInventoryClassCodeMap.get( lExpectedPart1 ) );
      assertEquals( RefInvClassKey.TRK, lPartToInventoryClassCodeMap.get( lExpectedPart2 ) );
   }


   @Test( expected = NullPointerException.class )
   public void update_throwsExceptionWithAlternatePartToCreateWithoutPartId() throws Exception {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();
      AlternatePart lAlternatePartToCreateWithoutPartId =
            defaultAlternatePartBuilder().partId( null ).build();
      PartGroup lUpdatedPartGroup = PartGroup.builder( lCreatedPartGroup )
            .alternatePart( lAlternatePartToCreateWithoutPartId ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test( expected = PartRequirementNotUsingSpecificPartException.class )
   public void update_throwsExceptionWithAlternatePartReqsHaveSpecificPartsValidator()
         throws Exception {
      PartGroup lUpdatedPartGroup = defaultPartGroupBuilder()
            .id( EXISTING_BATCH_PART_GROUP_ID_WITH_TASK ).mustRequestSpecificPart( true ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
   }


   @Test
   public void update_unapproveStandardAlternatePart()
         throws TriggerException, KeyConversionException, MxException {
      AlternatePart lAlternatePart = defaultAlternatePartBuilder().partId( PART_2 )
            .approved( APPROVED ).standard( STANDARD ).build();
      PartGroup lPartGroup = defaultPartGroupBuilder().alternatePart( lAlternatePart ).build();
      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
      PartGroup lCreatedPartGroup = iPartGroupService.get( lPartGroupId );

      AlternatePart lUpdatedAlternatePart =
            new AlternatePart.Builder( lAlternatePart ).approved( !APPROVED ).build();
      PartGroup lDefaultPartGroup =
            defaultPartGroupBuilder().alternatePart( lUpdatedAlternatePart ).build();
      PartGroup lUpdatedPartGroup = PartGroup.builder( lDefaultPartGroup )
            .id( lCreatedPartGroup.getId().get() ).key( lCreatedPartGroup.getKey().get() ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lCreatedPartGroup.getId().get(), lUpdatedPartGroup );
   }


   @Test
   public void update_withNonNullableFieldsAndNoAlternateParts()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lCreatedPartGroup = createDefaultPartGroup();
      Id<PartGroup> lPartGroupId = lCreatedPartGroup.getId().get();

      PartGroup lUpdatedPartGroup = PartGroup.builder().assembly( ASSEMBLY_KEY ).id( lPartGroupId )
            .key( lCreatedPartGroup.getKey().get() ).code( PART_GROUP_CODE ).name( PART_GROUP_NAME )
            .quantity( QUANTITY ).inventoryClassKey( INVENTORY_CLASS_CODE )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( new ApplicabilityRange( null ) ).purchaseTypeKey( null )
            .lineReplaceableUnit( LINE_REPLACEABLE_UNIT ).otherConditions( null )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Collections.<AlternatePart>emptyList() ).build();
      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );

      assertUpdatedPartGroup( lPartGroupId, lUpdatedPartGroup );
   }


   @Test
   public void update_withTRKInventoryClass()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lUpdatedPartGroup = defaultPartGroupBuilder().id( EXISTING_TRK_PART_GROUP_ID )
            .key( EXISTING_TRK_PART_GROUP_KEY ).code( PART_GROUP_CODE_NEW )
            .name( PART_GROUP_NAME_NEW ).quantity( BigDecimal.ONE )
            .inventoryClassKey( RefInvClassKey.TRK ).otherConditions( OTHER_CONDITIONS_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_KEY_NEW )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .otherConditions( OTHER_CONDITIONS_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).build();

      iPartGroupService.update( lUpdatedPartGroup, AUTHORIZING_HR, CHECK_INCOMPATIBILITIES );
      assertUpdatedPartGroup( EXISTING_TRK_PART_GROUP_ID, lUpdatedPartGroup );
   }


   @Test
   public void getPartGroupById() throws TriggerException, KeyConversionException, MxException {
      PartGroup lExistingPartGroup = PartGroup.builder().assembly( ASSEMBLY_KEY )
            .code( PART_GROUP_CODE ).name( PART_GROUP_NAME ).quantity( QUANTITY )
            .inventoryClassKey( INVENTORY_CLASS_CODE ).configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Collections.<AlternatePart>emptyList() ).build();
      Id<PartGroup> lId = iPartGroupService.create( lExistingPartGroup, AUTHORIZING_HR );
      PartGroup lCreatedPartGroup = iPartGroupService.find( lId ).get();

      assertEquals( PartGroup.builder( lExistingPartGroup ).id( lId )
            .key( lCreatedPartGroup.getKey().get() ).build(), iPartGroupService.get( lId ) );
   }


   private void assertUpdatedPartGroup( Id<PartGroup> aActualPartGroupId,
         PartGroup aExpectedPartGroup ) {
      Optional<PartGroup> lOptionalActualPartGroup = iPartGroupService.find( aActualPartGroupId );

      if ( !lOptionalActualPartGroup.isPresent() ) {
         fail( "Expected part group could not be found." );
      }

      if ( !aExpectedPartGroup.getId().isPresent() ) {
         Assert.fail( "Expecting part group to have an ID" );
      }

      if ( !aExpectedPartGroup.getKey().isPresent() ) {
         Assert.fail( "Expecting part group to have a key" );
      }

      PartGroup lActualPartGroup = lOptionalActualPartGroup.get();
      PartGroup lExpectedPartGroup = PartGroup.builder( aExpectedPartGroup )
            .id( lActualPartGroup.getId().get() ).key( lActualPartGroup.getKey().get() ).build();
      assertEquals( lExpectedPartGroup, lActualPartGroup );
   }


   private void assertCreatedPartGroup( Id<PartGroup> aActualPartGroupId,
         PartGroup aExpectedPartGroup ) {
      Optional<PartGroup> lOptionalActualPartGroup = iPartGroupService.find( aActualPartGroupId );

      if ( !lOptionalActualPartGroup.isPresent() ) {
         fail( "Expected part group could not be found." );
      }

      if ( aExpectedPartGroup.getId().isPresent() ) {
         Assert.fail( "Expecting part group to not have an ID yet" );
      }
      if ( aExpectedPartGroup.getKey().isPresent() ) {
         Assert.fail( "Expecting part group to not have a key yet" );
      }
      PartGroup lActualPartGroup = lOptionalActualPartGroup.get();
      if ( !lActualPartGroup.getId().isPresent() ) {
         Assert.fail( "Expecting part group to have an ID" );
      }
      if ( !lActualPartGroup.getKey().isPresent() ) {
         Assert.fail( "Expecting part group to have an Key" );
      }
      PartGroup lExpectedPartGroup = PartGroup.builder( aExpectedPartGroup )
            .id( lActualPartGroup.getId().get() ).key( lActualPartGroup.getKey().get() ).build();

      assertEquals( lExpectedPartGroup, lActualPartGroup );
   }


   private static PartGroup.Builder defaultPartGroupBuilder() {
      return PartGroup.builder().assembly( ASSEMBLY_KEY ).code( PART_GROUP_CODE )
            .name( PART_GROUP_NAME ).quantity( QUANTITY ).inventoryClassKey( INVENTORY_CLASS_CODE )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART )
            .alternateParts( Collections.<AlternatePart>emptyList() );
   }


   private static AlternatePart.Builder defaultAlternatePartBuilder() {
      return new AlternatePart.Builder().partId( BATCH_PART_1 )
            .interchangeabilityOrder( INTERCHANGEABILITY_ORDER )
            .applicabilityRange( ALTERNATE_PART_APPLICABILITY_RANGE ).approved( APPROVED )
            .standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
            .baselineCode( BASELINE_CODE );
   }


   private static List<AlternatePart> defaultAlternateParts() {
      AlternatePart lAlternatePart1 = defaultAlternatePartBuilder().partId( BATCH_PART_1 )
            .interchangeabilityOrder( INTERCHANGEABILITY_ORDER )
            .applicabilityRange( ALTERNATE_PART_APPLICABILITY_RANGE ).approved( APPROVED )
            .standard( !STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
            .baselineCode( BASELINE_CODE ).build();
      AlternatePart lAlternatePart2 = defaultAlternatePartBuilder().partId( PART_2 )
            .interchangeabilityOrder( INTERCHANGEABILITY_ORDER )
            .applicabilityRange( ALTERNATE_PART_APPLICABILITY_RANGE ).approved( APPROVED )
            .standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
            .baselineCode( BASELINE_CODE ).build();
      AlternatePart lAlternatePart3 = defaultAlternatePartBuilder().partId( PART_3 )
            .interchangeabilityOrder( INTERCHANGEABILITY_ORDER )
            .applicabilityRange( ALTERNATE_PART_APPLICABILITY_RANGE ).approved( APPROVED )
            .standard( !STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
            .baselineCode( BASELINE_CODE ).build();

      return Arrays.asList( lAlternatePart1, lAlternatePart2, lAlternatePart3 );
   }


   private PartGroup createDefaultPartGroup()
         throws TriggerException, KeyConversionException, MxException {
      PartGroup lPartGroup = defaultPartGroupBuilder().build();

      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
      return iPartGroupService.get( lPartGroupId );
   }


   private PartGroup createDefaultPartGroupAndPart()
         throws TriggerException, KeyConversionException, MxException {
      List<AlternatePart> lAlternateParts = defaultAlternateParts();
      PartGroup lPartGroup = defaultPartGroupBuilder().alternateParts( lAlternateParts ).build();

      Id<PartGroup> lPartGroupId = iPartGroupService.create( lPartGroup, AUTHORIZING_HR );
      return iPartGroupService.get( lPartGroupId );
   }

}
