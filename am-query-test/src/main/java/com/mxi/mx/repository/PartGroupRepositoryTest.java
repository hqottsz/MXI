package com.mxi.mx.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.internationalization.StringBundles;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.exception.KeyConversionException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EqpPartBaselineKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPurchTypeKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.log.Log;
import com.mxi.mx.core.services.log.LogService;
import com.mxi.mx.core.services.part.ApplicabilityRange;
import com.mxi.mx.core.services.part.InvalidApplicabilityRangeException;
import com.mxi.mx.core.services.taskdefn.exception.AlternatePartHasLockedTaskIncompatibilityException;
import com.mxi.mx.core.table.eqp.EqpPartBaselineTable;
import com.mxi.mx.core.utils.KeyConversionUtilities;
import com.mxi.mx.domain.Id;
import com.mxi.mx.domain.configslot.ConfigSlot;
import com.mxi.mx.domain.part.Part;
import com.mxi.mx.domain.partgroup.PartGroup;
import com.mxi.mx.domain.partgroup.alternatepart.AlternatePart;
import com.mxi.mx.domain.partgroup.alternatepart.incompatibilities.PartIncompatibility;
import com.mxi.mx.domain.partgroup.alternatepart.incompatibilities.TaskIncompatibility;
import com.mxi.mx.domain.partgroup.alternatepart.incompatibilities.TaskIncompatibility.IncompatibleWhen;
import com.mxi.mx.repository.sensitivity.SensitivityRepository;
import com.mxi.mx.testing.FakeStringBundles;


public class PartGroupRepositoryTest {

   private static final ApplicabilityRange PART_GROUP_APPLICABILITY_RANGE;
   private static final ApplicabilityRange PART_GROUP_APPLICABILITY_RANGE_NEW;
   private static final ApplicabilityRange PART_APPLICABILITY_RANGE;
   private static final ApplicabilityRange PART_APPLICABILITY_RANGE_NEW;

   static {
      StringBundles.setSingleton( new FakeStringBundles() );

      try {
         PART_GROUP_APPLICABILITY_RANGE =
               new ApplicabilityRange( "01-50,200,300-400,10000000-10000001" );
         PART_GROUP_APPLICABILITY_RANGE_NEW =
               new ApplicabilityRange( "03-58,400,500-600,30000000-30000009" );
         PART_APPLICABILITY_RANGE = new ApplicabilityRange( "01-50" );
         PART_APPLICABILITY_RANGE_NEW = new ApplicabilityRange( "11-50" );
      } catch ( InvalidApplicabilityRangeException | StringTooLongException e ) {
         throw new RuntimeException( e );
      }
   }

   private static final RefSensitivityKey SENS_1 = new RefSensitivityKey( "SENS_1" );
   private static final RefSensitivityKey SENS_2 = new RefSensitivityKey( "SENS_2" );
   private static final RefSensitivityKey SENS_5 = new RefSensitivityKey( "SENS_5" );
   private static final RefSensitivityKey SENS_9 = new RefSensitivityKey( "SENS_9" );
   private static final RefSensitivityKey SENS_10 = new RefSensitivityKey( "SENS_10" );

   private static final RefPurchTypeKey PURCHASE_TYPE_KEY = new RefPurchTypeKey( 1, "CMNHW" );
   private static final RefPurchTypeKey PURCHASE_TYPE_CODE_NEW = new RefPurchTypeKey( 2, "CONSUM" );
   private static final String EXISTING_ASSEMBLY_CODE = "ACFT_CD1";
   private static final AssemblyKey ASSEMBLY_KEY = new AssemblyKey( 4650, EXISTING_ASSEMBLY_CODE );

   private static final PartGroupKey INVALID_KEY_1 = new PartGroupKey( 0, 10 );
   private static final PartGroupKey INVALID_KEY_2 = new PartGroupKey( 0, 11 );

   private static final Set<PartGroupKey> INVALID_KEYS =
         new HashSet<>( Arrays.asList( INVALID_KEY_1, INVALID_KEY_2 ) );

   private static final PartGroup PART_GROUP_TO_UPDATE_SENSITIVITIES =
         PartGroup.builder().key( new PartGroupKey( 4650, 10 ) ).assembly( ASSEMBLY_KEY )
               .configurationSlotId( new Id<ConfigSlot>( "10000000000000000000000000000002" ) )
               .inventoryClassKey( RefInvClassKey.BATCH )
               .purchaseTypeKey( new RefPurchTypeKey( 2, "CONSUM" ) ).code( "ToUpdate" )
               .name( "Sensitive" ).quantity( BigDecimal.ONE ).lineReplaceableUnit( false )
               .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
               .mustRequestSpecificPart( true ).otherConditions( "Conditions" )
               .id( new Id<PartGroup>( "20000000000000000000000000000006" ) )
               .sensitivities( Arrays.asList( SENS_1, SENS_9 ) ).build();

   private static final PartGroup PART_GROUP_PARENT =
         PartGroup.builder().id( new Id<PartGroup>( "20000000000000000000000000000007" ) )
               .key( new PartGroupKey( 4650, 7 ) ).code( "PARENT" ).name( "Parent" )
               .assembly( ASSEMBLY_KEY )
               .configurationSlotId( new Id<ConfigSlot>( "10000000000000000000000000000003" ) )
               .inventoryClassKey( RefInvClassKey.TRK ).quantity( BigDecimal.ONE )
               .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( false )
               .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
               .otherConditions( "other conditions" ).mustRequestSpecificPart( false )
               .sensitivity( SENS_1 ).build();

   private static final Id<Part> BATCH_PART_1 = new Id<>( "00000000000000000000000000000001" );
   private static final Id<Part> PART_2 = new Id<>( "00000000000000000000000000000002" );
   private static final Id<Part> PART_3 = new Id<>( "00000000000000000000000000000003" );
   private static final String PART_GROUP_CODE = "ParGR";
   private static final String PART_GROUP_CODE_NEW = "newParGR";
   private static final Boolean MUST_REQUEST_SPECIFIC_PART = true;
   private static final String PART_GROUP_NAME = "Part Group Name";
   private static final String PART_GROUP_NAME_NEW = "New Part Group Name";
   private static final BigDecimal QUANTITY = BigDecimal.valueOf( 15.67 );
   private static final RefInvClassKey INVENTORY_CLASS_KEY_BATCH = RefInvClassKey.BATCH;
   private static final RefInvClassKey INVENTORY_CLASS_KEY_TRK = RefInvClassKey.TRK;
   private static final Id<ConfigSlot> CONFIGURATION_SLOT_ID =
         new Id<>( "10000000000000000000000000000001" );

   private static final Boolean LINE_REPLACEABLE_UNIT = false;
   private static final String OTHER_CONDITIONS = "Some Other Conditions";
   private static final String OTHER_CONDITIONS_NEW = "Some New Other Conditions";
   private static final Integer INTERCHANGEABILITY_ORDER = 5;
   private static final Integer INTERCHANGEABILITY_ORDER_NEW = 6;

   private static final Boolean STANDARD = true;
   private static final Boolean APPROVED = true;
   private static final Boolean HAS_OTHER_CONDITIONS = true;
   private static final String BASELINE_CODE = "baseline";
   private static final String BASELINE_CODE_NEW = "new baseline";
   private static final IetmTopicKey IETM_TOPIC = new IetmTopicKey( "5000000:1000:53" );
   private static final IetmTopicKey IETM_TOPIC_NEW = new IetmTopicKey( "5000000:1000:54" );
   private static final String EXISTING_PART_GROUP_WITH_PARENT = "TRK_WITH_PARENT";

   private static final AlternatePart BATCH_PART_ONE = new AlternatePart.Builder()
         .partId( new Id<Part>( "00000000000000000000000000000001" ) ).interchangeabilityOrder( 5 )
         .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
         .approved( APPROVED ).standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
         .baselineCode( BASELINE_CODE ).build();

   private static final AlternatePart BATCH_PART_TWO = new AlternatePart.Builder()
         .partId( new Id<Part>( "00000000000000000000000000000002" ) ).interchangeabilityOrder( 5 )
         .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
         .approved( APPROVED ).standard( false ).hasOtherConditions( HAS_OTHER_CONDITIONS )
         .baselineCode( BASELINE_CODE ).build();

   private static final AlternatePart TRK_PART_ONE = new AlternatePart.Builder()
         .partId( new Id<Part>( "00000000000000000000000000000004" ) ).interchangeabilityOrder( 1 )
         .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
         .approved( APPROVED ).standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
         .baselineCode( BASELINE_CODE ).build();

   private static final AlternatePart TRK_PART_TWO = new AlternatePart.Builder()
         .partId( new Id<Part>( "00000000000000000000000000000005" ) ).interchangeabilityOrder( 1 )
         .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
         .approved( APPROVED ).standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
         .baselineCode( BASELINE_CODE ).build();

   private static final AlternatePart TRK_PART_THREE = new AlternatePart.Builder()
         .partId( new Id<Part>( "00000000000000000000000000000006" ) ).interchangeabilityOrder( 1 )
         .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
         .approved( APPROVED ).standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
         .baselineCode( BASELINE_CODE ).build();

   private static final AlternatePart TRK_PART_FOUR = new AlternatePart.Builder()
         .partId( new Id<Part>( "00000000000000000000000000000007" ) ).interchangeabilityOrder( 1 )
         .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
         .approved( APPROVED ).standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
         .baselineCode( BASELINE_CODE ).build();

   private static final String PART_INCOMPATIBILITY_MANUFACTURER = "ABC";
   private static final String PART_INCOMPATIBILITY_PART_NO_1 = "OEM_PART_NUMBER_6";
   private static final String PART_INCOMPATIBILITY_PART_GROUP_CODE_1 = "TRK_WITH_PARENT";
   private static final String PART_INCOMPATIBILITY_PART_NO_2 = "OEM_PART_NUMBER_7";
   private static final String PART_INCOMPATIBILITY_PART_GROUP_CODE_2 = "TRK_NO_PARENT";

   private static final PartIncompatibility INCOMPATIBLE_PART_ONE =
         new PartIncompatibility.Builder()
               .partGroupId( new Id<PartGroup>( "20000000000000000000000000000004" ) )
               .partId( new Id<Part>( "00000000000000000000000000000006" ) )
               .partGroupCode( PART_INCOMPATIBILITY_PART_GROUP_CODE_1 )
               .partNumber( PART_INCOMPATIBILITY_PART_NO_1 )
               .manufacturer( PART_INCOMPATIBILITY_MANUFACTURER ).build();

   private static final PartIncompatibility INCOMPATIBLE_PART_TWO =
         new PartIncompatibility.Builder()
               .partGroupId( new Id<PartGroup>( "20000000000000000000000000000008" ) )
               .partId( new Id<Part>( "00000000000000000000000000000007" ) )
               .partGroupCode( PART_INCOMPATIBILITY_PART_GROUP_CODE_2 )
               .partNumber( PART_INCOMPATIBILITY_PART_NO_2 )
               .manufacturer( PART_INCOMPATIBILITY_MANUFACTURER ).build();

   private static final String TASK_INCOMPATIBILITY_TASK_CD = "TASK_CD_1";
   private static final String TASK_INCOMPATIBILITY_ASSBML_BOM_CD = "SYS-APU";
   private static final TaskTaskKey TASK_TASK_KEY_1 = new TaskTaskKey( 1111, 11 );
   private static final TaskTaskKey TASK_TASK_KEY_2 = new TaskTaskKey( 2222, 22 );
   private static final TaskDefnKey TASK_DEFN_KEY_1 = new TaskDefnKey( 1111, 11 );
   private static final TaskDefnKey TASK_DEFN_KEY_2 = new TaskDefnKey( 2222, 22 );

   private static final TaskIncompatibility TASK_INCOMP_1 = new TaskIncompatibility.Builder()
         .taskCode( TASK_INCOMPATIBILITY_TASK_CD ).ataCode( TASK_INCOMPATIBILITY_ASSBML_BOM_CD )
         .incompatibleWhen( IncompatibleWhen.OPEN ).taskKey( TASK_TASK_KEY_1 )
         .taskDefnKey( TASK_DEFN_KEY_1 ).build();

   private static final TaskIncompatibility TASK_INCOMP_2 = new TaskIncompatibility.Builder()
         .taskCode( TASK_INCOMPATIBILITY_TASK_CD ).ataCode( TASK_INCOMPATIBILITY_ASSBML_BOM_CD )
         .incompatibleWhen( IncompatibleWhen.OPEN ).taskKey( TASK_TASK_KEY_2 )
         .taskDefnKey( TASK_DEFN_KEY_2 ).build();

   private static final TaskIncompatibility TASK_INCOMP_3 = new TaskIncompatibility.Builder()
         .taskCode( TASK_INCOMPATIBILITY_TASK_CD ).ataCode( TASK_INCOMPATIBILITY_ASSBML_BOM_CD )
         .incompatibleWhen( IncompatibleWhen.COMPLETE ).taskKey( TASK_TASK_KEY_1 )
         .taskDefnKey( TASK_DEFN_KEY_1 ).build();

   private static final PartGroup PART_GROUP_WITH_SENSITIVITIES = PartGroup.builder()
         .id( new Id<PartGroup>( "20000000000000000000000000000005" ) )
         .key( new PartGroupKey( 4650, 9 ) ).code( "Sensitive" ).assembly( ASSEMBLY_KEY )
         .name( "Sensitive" )
         .configurationSlotId( new Id<ConfigSlot>( "10000000000000000000000000000002" ) )
         .inventoryClassKey( RefInvClassKey.BATCH ).quantity( BigDecimal.ONE )
         .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( false )
         .applicabilityRange( ApplicabilityRange.createRangeApplicableToEverything() )
         .otherConditions( "Conditions" ).mustRequestSpecificPart( true ).sensitivity( SENS_1 )
         .alternateParts( Arrays.asList( BATCH_PART_ONE, BATCH_PART_TWO ) ).build();

   private static final List<PartGroup> VALID_PART_GROUPS =
         Arrays.asList( PART_GROUP_WITH_SENSITIVITIES, PART_GROUP_PARENT );

   private static final HumanResourceKey HUMAN_RESOURCE_KEY = new HumanResourceKey( 4650, 1 );

   private static final Set<PartGroupKey> VALID_AND_INVALID_KEYS = new HashSet<>(
         Arrays.asList( PART_GROUP_WITH_SENSITIVITIES.getKey().get(), INVALID_KEY_1 ) );

   private static final Set<PartGroupKey> VALID_KEYS =
         new HashSet<>( Arrays.asList( PART_GROUP_WITH_SENSITIVITIES.getKey().get(),
               PART_GROUP_PARENT.getKey().get() ) );

   private PartGroupRepository iPartGroupRepository;
   private LogService iMockLogService;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iMockLogService = mock( LogService.class );
      iPartGroupRepository = new PartGroupRepository( MxDataAccess.getInstance(), iMockLogService,
            mock( StringBundles.class ), mock( SensitivityRepository.class ) );
   }


   @Test( expected = RuntimeException.class )
   public void get_ByKeyThrowsExceptionWhenKeyIsInvalid() {
      iPartGroupRepository.get( INVALID_KEY_1 );
   }


   @Test( expected = NullPointerException.class )
   public void get_ByKeyNullThrowsException() {
      iPartGroupRepository.get( ( PartGroupKey ) null );
   }


   @Test
   public void get_ByKey() {
      assertEquals( PART_GROUP_WITH_SENSITIVITIES,
            iPartGroupRepository.get( PART_GROUP_WITH_SENSITIVITIES.getKey().get() ) );
   }


   @Test( expected = NullPointerException.class )
   public void get_ByKeys_null() {
      iPartGroupRepository.get( ( Set<PartGroupKey> ) null );
   }


   @Test( expected = RuntimeException.class )
   public void get_ByKeys_invalidKeys() {
      iPartGroupRepository.get( INVALID_KEYS );
   }


   @Test( expected = RuntimeException.class )
   public void get_ByKeys_invalidAndValidKeys() {
      iPartGroupRepository.get( VALID_AND_INVALID_KEYS );
   }


   @Test
   public void get_ByKeys_validKeys() {
      assertEquals( VALID_PART_GROUPS, iPartGroupRepository.get( VALID_KEYS ) );
   }


   @Test
   public void create_withApprovedAlternatePart() throws Exception {
      AlternatePart lAlternatePart = defaultAlternatePartBuilder().build();
      PartGroup lPartGroup = defaultPartGroupBuilder().alternatePart( lAlternatePart ).build();
      Id<PartGroup> lPartGroupId = iPartGroupRepository.create( lPartGroup );

      assertPartGroup( lPartGroupId, PartGroup.builder( lPartGroup ).id( lPartGroupId ).build() );
   }


   @Test
   public void create_withUnApprovedAlternatePart() throws Exception {
      AlternatePart lAlternatePart = defaultAlternatePartBuilder().approved( false ).build();
      PartGroup lPartGroup = defaultPartGroupBuilder().alternatePart( lAlternatePart ).build();
      Id<PartGroup> lPartGroupId = iPartGroupRepository.create( lPartGroup );

      assertPartGroup( lPartGroupId, PartGroup.builder( lPartGroup ).id( lPartGroupId ).build() );
   }


   @Test( expected = NullPointerException.class )
   public void update_throwsExceptionWithNullHumanResourceKey() throws Exception {
      iPartGroupRepository.update( PART_GROUP_TO_UPDATE_SENSITIVITIES, null );
   }


   @Test
   public void update_doesNotCreateHistoryNoteWhenSensitivitiesDoNotChange() throws Exception {
      iPartGroupRepository.update( PART_GROUP_TO_UPDATE_SENSITIVITIES,
            mock( HumanResourceKey.class ) );

      // No logs should be written if no sensitivities have changed
      verifyZeroInteractions( iMockLogService );
   }


   @Test
   public void update_PartGroup_EnableSensitivities() throws Exception {
      PartGroup lPartGroupBeforeUpdate = PartGroup.builder( PART_GROUP_TO_UPDATE_SENSITIVITIES )
            .sensitivities( Arrays.asList( SENS_1, SENS_9 ) ).build();

      assertThat( iPartGroupRepository.get( PART_GROUP_TO_UPDATE_SENSITIVITIES.getKey().get() ),
            is( lPartGroupBeforeUpdate ) );

      PartGroup lUpdatedPartGroup = PartGroup.builder( PART_GROUP_TO_UPDATE_SENSITIVITIES )
            .sensitivities( Arrays.asList( SENS_1, SENS_2, SENS_9, SENS_10 ) ).build();

      iPartGroupRepository.update( lUpdatedPartGroup, HUMAN_RESOURCE_KEY );

      PartGroup lDatabaseAfterUpdate = iPartGroupRepository.get( new PartGroupKey( 4650, 10 ) );
      assertThat( lDatabaseAfterUpdate, is( lUpdatedPartGroup ) );

      // Validate that a sensitivity change has yielded a log message
      verify( iMockLogService, times( 1 ) ).log( any( Log.class ) );
   }


   @Test
   public void update_PartGroup_Disable_Sensitivitities() throws Exception {
      PartGroup lPartGroupBeforeUpdate = PartGroup.builder( PART_GROUP_TO_UPDATE_SENSITIVITIES )
            .sensitivities( Arrays.asList( SENS_1, SENS_9 ) ).build();

      assertThat( iPartGroupRepository.get( new PartGroupKey( 4650, 10 ) ),
            is( lPartGroupBeforeUpdate ) );

      PartGroup lUpdatedPartGroup = PartGroup.builder( PART_GROUP_TO_UPDATE_SENSITIVITIES )
            .sensitivities( Arrays.asList( SENS_1 ) ).build();

      iPartGroupRepository.update( lUpdatedPartGroup, HUMAN_RESOURCE_KEY );

      PartGroup lDatabaseAfterUpdate = iPartGroupRepository.get( new PartGroupKey( 4650, 10 ) );
      assertThat( lDatabaseAfterUpdate, is( lUpdatedPartGroup ) );

      // Validate that a sensitivity change has yielded a log message
      verify( iMockLogService, times( 1 ) ).log( any( Log.class ) );
   }


   @Test
   public void update_PartGroup_EnableAndDisable_Sensitivitities() throws Exception {
      PartGroup lPartGroupBeforeUpdate = PartGroup.builder( PART_GROUP_TO_UPDATE_SENSITIVITIES )
            .sensitivities( Arrays.asList( SENS_1, SENS_9 ) ).build();

      assertThat( iPartGroupRepository.get( new PartGroupKey( 4650, 10 ) ),
            is( lPartGroupBeforeUpdate ) );

      PartGroup lUpdatedPartGroup = PartGroup.builder( PART_GROUP_TO_UPDATE_SENSITIVITIES )
            .sensitivities( Arrays.asList( SENS_2, SENS_10 ) ).build();

      iPartGroupRepository.update( lUpdatedPartGroup, HUMAN_RESOURCE_KEY );

      PartGroup lDatabaseAfterUpdate = iPartGroupRepository.get( new PartGroupKey( 4650, 10 ) );
      assertThat( lDatabaseAfterUpdate, is( lUpdatedPartGroup ) );

      // Validate that a sensitivity change has yielded a log message
      verify( iMockLogService, times( 1 ) ).log( any( Log.class ) );
   }


   @Test
   public void update_PartGroup_Sensitivities_EnabledDoesNotGetDisabled() throws Exception {
      PartGroup lPartGroupBeforeUpdate = PartGroup.builder( PART_GROUP_TO_UPDATE_SENSITIVITIES )
            .sensitivities( Arrays.asList( SENS_1, SENS_9 ) ).build();

      assertThat( iPartGroupRepository.get( new PartGroupKey( 4650, 10 ) ),
            is( lPartGroupBeforeUpdate ) );

      PartGroup lUpdatedPartGroup = PartGroup.builder( PART_GROUP_TO_UPDATE_SENSITIVITIES )
            .sensitivities( Arrays.asList( SENS_2, SENS_10 ) ).build();

      iPartGroupRepository.update( lUpdatedPartGroup, HUMAN_RESOURCE_KEY );

      PartGroup lDatabaseAfterUpdate = iPartGroupRepository.get( new PartGroupKey( 4650, 10 ) );
      assertThat( lDatabaseAfterUpdate, is( lUpdatedPartGroup ) );

      /*
       * Sensitivity 5 is a globally inactive, assigned, and enabled sensitivity. The assertions
       * below ensure that the status is preserved after sensitivities 1 and 9 have been updated to
       * sensitivities 2 and 10.
       */

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( new PartGroupKey( 4650, 10 ), new String[] { "bom_part_db_id", "bom_part_id" } );
      lArgs.add( SENS_5, new String[] { "sensitivity_cd" } );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "eqp_bom_part_sens", lArgs );

      assertFalse( lQs.isEmpty() );

      if ( lQs.first() ) {
         assertThat( lQs.getString( "sensitivity_cd" ), is( SENS_5.getKeyAsString() ) );
      }
   }


   @Test
   public void update_withAlternateParts() throws Exception {
      Id<Part> lAlternatePartIdToDelete = BATCH_PART_1;
      Id<Part> lAlternatePartIdToUpdate = PART_2;
      Id<Part> lAlternatePartIdToCreate = PART_3;
      AlternatePart lAlternatePartToDelete =
            defaultAlternatePartBuilder().partId( lAlternatePartIdToDelete ).build();
      AlternatePart lAlternatePartToUpdate = new AlternatePart.Builder()
            .partId( lAlternatePartIdToUpdate ).interchangeabilityOrder( INTERCHANGEABILITY_ORDER )
            .applicabilityRange( PART_APPLICABILITY_RANGE ).approved( APPROVED )
            .standard( !STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
            .baselineCode( BASELINE_CODE ).build();
      AlternatePart lAlternatePartToCreate = new AlternatePart.Builder()
            .partId( lAlternatePartIdToCreate ).interchangeabilityOrder( INTERCHANGEABILITY_ORDER )
            .applicabilityRange( PART_APPLICABILITY_RANGE ).approved( APPROVED )
            .standard( !STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
            .baselineCode( BASELINE_CODE ).build();

      PartGroup lPartGroup = PartGroup.builder().code( PART_GROUP_CODE ).assembly( ASSEMBLY_KEY )
            .name( PART_GROUP_NAME ).quantity( QUANTITY )
            .inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
            .alternateParts( Arrays.asList( lAlternatePartToUpdate, lAlternatePartToDelete ) )
            .build();
      Id<PartGroup> lPartGroupId = iPartGroupRepository.create( lPartGroup );
      AlternatePart lUpdatedAlternatePart =
            new AlternatePart.Builder().partId( lAlternatePartIdToUpdate )
                  .interchangeabilityOrder( INTERCHANGEABILITY_ORDER_NEW )
                  .applicabilityRange( PART_APPLICABILITY_RANGE_NEW ).approved( !APPROVED )
                  .standard( STANDARD ).hasOtherConditions( !HAS_OTHER_CONDITIONS )
                  .baselineCode( BASELINE_CODE_NEW ).build();

      PartGroup lPartGroupToUpdate = PartGroup.builder().id( lPartGroupId ).assembly( ASSEMBLY_KEY )
            .code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW ).quantity( QUANTITY )
            .inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( Arrays.asList( lUpdatedAlternatePart, lAlternatePartToCreate ) )
            .build();

      iPartGroupRepository.update( lPartGroupToUpdate, HUMAN_RESOURCE_KEY );

      assertPartGroup( lPartGroupId, lPartGroupToUpdate );
   }


   @Test
   public void update_removeAlternatePartWithIncompatibilities() throws Exception {
      AlternatePart lAlternatePart = new AlternatePart.Builder( TRK_PART_ONE )
            .partIncompatibilities( Arrays.asList( INCOMPATIBLE_PART_ONE, INCOMPATIBLE_PART_TWO ) )
            .taskIncompatibilities( Arrays.asList( TASK_INCOMP_1 ) ).standard( true ).build();

      PartGroup lPartGroup = PartGroup.builder().code( PART_GROUP_CODE ).assembly( ASSEMBLY_KEY )
            .name( PART_GROUP_NAME ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_TRK )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
            .alternateParts( Arrays.asList( lAlternatePart ) ).build();
      Id<PartGroup> lPartGroupId = iPartGroupRepository.create( lPartGroup );

      PartGroup lPartGroupToUpdate = PartGroup.builder().id( lPartGroupId ).assembly( ASSEMBLY_KEY )
            .code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_TRK )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( new ArrayList<AlternatePart>() ).build();

      iPartGroupRepository.update( lPartGroupToUpdate, HUMAN_RESOURCE_KEY );

      assertPartGroup( lPartGroupId, lPartGroupToUpdate );
   }


   @Test
   public void update_withPartIncompatibilitiesAdded() throws MxException {
      AlternatePart lExistingAlternatePart = new AlternatePart.Builder( TRK_PART_ONE ).build();

      PartGroup lPartGroup = PartGroup.builder().code( PART_GROUP_CODE ).assembly( ASSEMBLY_KEY )
            .name( PART_GROUP_NAME ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_TRK )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
            .alternateParts( Arrays.asList( lExistingAlternatePart ) ).build();
      Id<PartGroup> lPartGroupId = iPartGroupRepository.create( lPartGroup );

      lExistingAlternatePart = new AlternatePart.Builder( TRK_PART_ONE )
            .partIncompatibilities( Arrays.asList( INCOMPATIBLE_PART_ONE, INCOMPATIBLE_PART_TWO ) )
            .build();

      PartGroup lPartGroupToUpdate = PartGroup.builder().id( lPartGroupId ).assembly( ASSEMBLY_KEY )
            .code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_TRK )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( Arrays.asList( lExistingAlternatePart ) ).build();

      iPartGroupRepository.update( lPartGroupToUpdate, HUMAN_RESOURCE_KEY );

      /*
       * We are not verifying the part group in this test, only the part incompatibilities
       */
      assertPartIncompatibilities( lPartGroupId, lPartGroupToUpdate.getAlternateParts() );
   }


   @Test
   public void update_withPartIncompatibilityRemoved() throws MxException {
      AlternatePart lExistingAlternatePart = new AlternatePart.Builder( TRK_PART_TWO )
            .partIncompatibilities( Arrays.asList( INCOMPATIBLE_PART_ONE, INCOMPATIBLE_PART_TWO ) )
            .build();

      PartGroup lPartGroup = PartGroup.builder().code( PART_GROUP_CODE ).assembly( ASSEMBLY_KEY )
            .name( PART_GROUP_NAME ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_TRK )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
            .alternateParts( Arrays.asList( lExistingAlternatePart ) ).build();
      Id<PartGroup> lPartGroupId = iPartGroupRepository.create( lPartGroup );

      lExistingAlternatePart = new AlternatePart.Builder( TRK_PART_ONE )
            .partIncompatibilities( Arrays.asList( INCOMPATIBLE_PART_ONE ) ).build();

      PartGroup lPartGroupToUpdate = PartGroup.builder().id( lPartGroupId ).assembly( ASSEMBLY_KEY )
            .code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_TRK )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( Arrays.asList( lExistingAlternatePart ) ).build();

      iPartGroupRepository.update( lPartGroupToUpdate, HUMAN_RESOURCE_KEY );

      /*
       * We are not verifying the part group in this test, only the part incompatibilities
       */
      assertPartIncompatibilities( lPartGroupId, lPartGroupToUpdate.getAlternateParts() );
   }


   @Test
   public void update_withDuplicatePartIncompatibilitiesSkipsDuplicate() throws MxException {
      AlternatePart lAlternatePart1 = new AlternatePart.Builder( TRK_PART_THREE )
            .partIncompatibilities( Arrays.asList( INCOMPATIBLE_PART_TWO ) ).build();
      AlternatePart lAlternatePart2 = new AlternatePart.Builder( TRK_PART_FOUR )
            .partIncompatibilities( Arrays.asList( INCOMPATIBLE_PART_ONE ) ).build();

      PartGroup lPartGroup1 =
            PartGroup.builder().id( new Id<PartGroup>( "20000000000000000000000000000004" ) )
                  .code( PART_GROUP_CODE ).assembly( ASSEMBLY_KEY ).name( PART_GROUP_NAME )
                  .quantity( BigDecimal.ONE ).inventoryClassKey( INVENTORY_CLASS_KEY_TRK )
                  .configurationSlotId( CONFIGURATION_SLOT_ID )
                  .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
                  .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
                  .otherConditions( OTHER_CONDITIONS )
                  .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
                  .alternateParts( Arrays.asList( lAlternatePart1 ) ).build();

      PartGroup lPartGroup2 = PartGroup.builder().assembly( ASSEMBLY_KEY )
            .id( new Id<PartGroup>( "20000000000000000000000000000008" ) )
            .code( PART_INCOMPATIBILITY_PART_GROUP_CODE_2 ).name( PART_GROUP_NAME_NEW )
            .quantity( BigDecimal.ONE ).inventoryClassKey( INVENTORY_CLASS_KEY_TRK )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( Arrays.asList( lAlternatePart2 ) ).build();

      iPartGroupRepository.update( lPartGroup1, HUMAN_RESOURCE_KEY );
      iPartGroupRepository.update( lPartGroup2, HUMAN_RESOURCE_KEY );

      /*
       * We are not verifying the part group in this test, only the part incompatibilities
       */
      assertPartIncompatibilities( lPartGroup1.getId().get(), Arrays.asList( lAlternatePart1 ) );
   }


   @Test
   public void update_withTaskIncompatibilities_Added() throws MxException {

      AlternatePart lPart = new AlternatePart.Builder( BATCH_PART_ONE )
            .taskIncompatibilities( Arrays.asList( TASK_INCOMP_1 ) ).standard( true ).build();

      AlternatePart lPart_Task_Incompatibility_Added = new AlternatePart.Builder( BATCH_PART_ONE )
            .taskIncompatibilities( Arrays.asList( TASK_INCOMP_1, TASK_INCOMP_2 ) ).standard( true )
            .build();

      PartGroup lPartGroup1 = PartGroup.builder().code( PART_GROUP_CODE ).assembly( ASSEMBLY_KEY )
            .name( PART_GROUP_NAME ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
            .alternateParts( Arrays.asList( lPart ) ).build();

      Id<PartGroup> lPartGroupIdlPartGroup1 = iPartGroupRepository.create( lPartGroup1 );

      PartGroup lPartGroup2 = PartGroup.builder().id( lPartGroupIdlPartGroup1 )
            .assembly( ASSEMBLY_KEY ).code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW )
            .quantity( BigDecimal.ONE ).inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( Arrays.asList( lPart_Task_Incompatibility_Added ) ).build();

      iPartGroupRepository.update( lPartGroup2, HUMAN_RESOURCE_KEY );

      // Validates that only the provided list of task_incompatibilities for the part group exists
      // in the database.
      assertTaskIncompatibilities( lPartGroupIdlPartGroup1, lPartGroup2.getAlternateParts() );
   }


   @Test
   public void update_withTaskIncompatibilities_Updated() throws MxException {

      AlternatePart lPart = new AlternatePart.Builder( BATCH_PART_ONE )
            .taskIncompatibilities( Arrays.asList( TASK_INCOMP_1 ) ).standard( true ).build();

      AlternatePart lPart_Task_Incompatibility_Updated = new AlternatePart.Builder( BATCH_PART_ONE )
            .taskIncompatibilities( Arrays.asList( TASK_INCOMP_3 ) ).standard( true ).build();

      PartGroup lPartGroup1 = PartGroup.builder().code( PART_GROUP_CODE ).assembly( ASSEMBLY_KEY )
            .name( PART_GROUP_NAME ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
            .alternateParts( Arrays.asList( lPart ) ).build();

      Id<PartGroup> lPartGroupIdlPartGroup1 = iPartGroupRepository.create( lPartGroup1 );

      PartGroup lPartGroup2 = PartGroup.builder().id( lPartGroupIdlPartGroup1 )
            .assembly( ASSEMBLY_KEY ).code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW )
            .quantity( BigDecimal.ONE ).inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( Arrays.asList( lPart_Task_Incompatibility_Updated ) ).build();

      iPartGroupRepository.update( lPartGroup2, HUMAN_RESOURCE_KEY );

      // Validates that only the provided list of task_incompatibilities for the part group exists
      // in the database.
      assertTaskIncompatibilities( lPartGroupIdlPartGroup1, lPartGroup2.getAlternateParts() );
   }


   @Test
   public void update_withTaskIncompatibilities_Removed() throws MxException {

      AlternatePart lPart = new AlternatePart.Builder( BATCH_PART_ONE )
            .taskIncompatibilities( Arrays.asList( TASK_INCOMP_1, TASK_INCOMP_2 ) ).standard( true )
            .build();

      AlternatePart lPart_Task_Incompatibility_Removed = new AlternatePart.Builder( BATCH_PART_ONE )
            .taskIncompatibilities( Arrays.asList( TASK_INCOMP_1 ) ).standard( true ).build();

      PartGroup lPartGroup1 = PartGroup.builder().code( PART_GROUP_CODE ).assembly( ASSEMBLY_KEY )
            .name( PART_GROUP_NAME ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
            .alternateParts( Arrays.asList( lPart ) ).build();

      Id<PartGroup> lPartGroupIdlPartGroup1 = iPartGroupRepository.create( lPartGroup1 );

      PartGroup lPartGroup2 = PartGroup.builder().id( lPartGroupIdlPartGroup1 )
            .assembly( ASSEMBLY_KEY ).code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW )
            .quantity( BigDecimal.ONE ).inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( Arrays.asList( lPart_Task_Incompatibility_Removed ) ).build();

      iPartGroupRepository.update( lPartGroup2, HUMAN_RESOURCE_KEY );

      // Validates that only the provided list of task_incompatibilities for the part group exists
      // in the database.
      assertTaskIncompatibilities( lPartGroupIdlPartGroup1, lPartGroup2.getAlternateParts() );
   }


   @Test
   public void update_removeTaskIncompatibilityThrowsExceptionWhenTaskIsLocked()
         throws MxException {

      AlternatePart lPart_Task_Incompatibility_Removed =
            new AlternatePart.Builder( BATCH_PART_ONE ).standard( true ).build();

      PartGroup lPartGroup2 = PartGroup.builder()
            .id( new Id<PartGroup>( "20000000000000000000000000000005" ) ).assembly( ASSEMBLY_KEY )
            .code( PART_GROUP_CODE_NEW ).name( PART_GROUP_NAME_NEW ).quantity( BigDecimal.ONE )
            .inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE_NEW )
            .purchaseTypeKey( PURCHASE_TYPE_CODE_NEW ).lineReplaceableUnit( !LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS_NEW )
            .mustRequestSpecificPart( !MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC_NEW )
            .alternateParts( Arrays.asList( lPart_Task_Incompatibility_Removed ) ).build();

      try {
         iPartGroupRepository.update( lPartGroup2, HUMAN_RESOURCE_KEY );
         fail( "No Exception was thrown." );
      } catch ( AlternatePartHasLockedTaskIncompatibilityException e ) {
         assertEquals( "Locked Task Code (Locked Task Name)", e.getTaskCode() );
         assertEquals( "OEM_PART_NUMBER_1", e.getPartNumber() );
         assertEquals( "ABC", e.getPartManufacturer() );
      }
   }


   @Test
   public void find_existingPartGroupById() throws Exception {
      Id<PartGroup> lExistingPartGroupId =
            iPartGroupRepository.create( defaultPartGroupBuilder().build() );
      PartGroup lExistingPartGroup = iPartGroupRepository.get( lExistingPartGroupId );

      Optional<PartGroup> lPartGroup = iPartGroupRepository.find( lExistingPartGroupId );

      if ( !lPartGroup.isPresent() ) {
         fail( "Expected part group could not be found." );
      }

      assertEquals( lExistingPartGroup, lPartGroup.get() );
   }


   @Test
   public void find_existingPartGroupWithAlternatePartsById() throws Exception {
      AlternatePart lPart1 = defaultAlternatePartBuilder().partId( BATCH_PART_1 ).build();
      AlternatePart lPart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).standard( false ).build();
      PartGroup lPartGroupToCreate =
            defaultPartGroupBuilder().alternateParts( Arrays.asList( lPart1, lPart2 ) ).build();
      Id<PartGroup> lExistingPartGroupId = iPartGroupRepository.create( lPartGroupToCreate );
      PartGroup lExistingPartGroup = iPartGroupRepository.get( lExistingPartGroupId );

      Optional<PartGroup> lPartGroup = iPartGroupRepository.find( lExistingPartGroupId );

      if ( !lPartGroup.isPresent() ) {
         fail( "Expected part group could not be found." );
      }

      assertEquals( lExistingPartGroup, lPartGroup.get() );
   }


   @Test
   public void find_existingPartGroupByCodeAndConfigurationSlotId() throws Exception {
      Id<PartGroup> lExistingPartGroupId =
            iPartGroupRepository.create( defaultPartGroupBuilder().build() );
      PartGroup lExistingPartGroup = iPartGroupRepository.get( lExistingPartGroupId );

      Optional<PartGroup> lPartGroup = iPartGroupRepository
            .find( lExistingPartGroup.getConfigurationSlotId(), lExistingPartGroup.getCode() );

      if ( !lPartGroup.isPresent() ) {
         fail( "Expected part group could not be found." );
      }

      assertEquals( lExistingPartGroup, lPartGroup.get() );
   }


   @Test
   public void find_existingPartGroupByAssemblyCodeAndCode() throws Exception {
      Id<PartGroup> lExistingPartGroupId =
            iPartGroupRepository.create( defaultPartGroupBuilder().build() );
      PartGroup lExistingPartGroup = iPartGroupRepository.get( lExistingPartGroupId );

      Optional<PartGroup> lPartGroup =
            iPartGroupRepository.find( EXISTING_ASSEMBLY_CODE, lExistingPartGroup.getCode() );
      assertTrue( lPartGroup.isPresent() );
      assertEquals( lExistingPartGroup, lPartGroup.get() );
   }


   @Test
   public void find_existingPartGroupByAssemblyCodeAndCodeReturnsEmptyWhenAssemblyDoesNotExist() {
      Optional<PartGroup> lPartGroup = iPartGroupRepository.find( "NOT-REAL", PART_GROUP_CODE );
      assertTrue( !lPartGroup.isPresent() );
      assertEquals( Optional.empty(), lPartGroup );
   }


   @Test
   public void find_existingPartGroupByAssemblyCodeAndCodeReturnsEmptyWhenPartGroupDoesNotExist() {
      Optional<PartGroup> lPartGroup =
            iPartGroupRepository.find( EXISTING_ASSEMBLY_CODE, "NOT-REAL" );
      assertTrue( !lPartGroup.isPresent() );
      assertEquals( Optional.empty(), lPartGroup );
   }


   @Test
   public void findParentPartGroup_existingParentPartGroupByAssemblyCodeAndCode() throws Exception {
      Optional<PartGroup> lPartGroup = iPartGroupRepository
            .findParentPartGroup( EXISTING_ASSEMBLY_CODE, EXISTING_PART_GROUP_WITH_PARENT );
      assertTrue( lPartGroup.isPresent() );
      assertEquals( PART_GROUP_PARENT, lPartGroup.get() );
   }


   @Test
   public void findParentPartGroup_parentPartGroupReturnsEmptyWhenThePartGroupDoesNotHaveParent()
         throws Exception {
      PartGroup lExistingPartGroupWhichDoesNotHaveParent = defaultPartGroupBuilder().build();
      Id<PartGroup> lExistingPartGroupId =
            iPartGroupRepository.create( lExistingPartGroupWhichDoesNotHaveParent );

      Optional<PartGroup> lPartGroup =
            iPartGroupRepository.findParentPartGroup( EXISTING_ASSEMBLY_CODE,
                  defaultPartGroupBuilder().id( lExistingPartGroupId ).build().getCode() );
      assertTrue( !lPartGroup.isPresent() );
      assertEquals( Optional.empty(), lPartGroup );
   }


   @Test
   public void
         findParentPartGroup_existingParentPartGroupByAssemblyCodeAndCodeReturnsEmptyWhenAssemblyDoesNotExist() {
      Optional<PartGroup> lPartGroup =
            iPartGroupRepository.findParentPartGroup( "NOT-REAL", EXISTING_PART_GROUP_WITH_PARENT );
      assertTrue( !lPartGroup.isPresent() );
      assertEquals( Optional.empty(), lPartGroup );
   }


   @Test
   public void
         findParentPartGroup_existingParentPartGroupByAssemblyCodeAndCodeReturnsEmptyWhenPartGroupDoesNotExist() {
      Optional<PartGroup> lPartGroup =
            iPartGroupRepository.findParentPartGroup( EXISTING_ASSEMBLY_CODE, "NOT-REAL" );
      assertTrue( !lPartGroup.isPresent() );
      assertEquals( Optional.empty(), lPartGroup );
   }


   @Test
   public void get_existingPartGroupById() throws Exception {
      assertEquals( PART_GROUP_WITH_SENSITIVITIES,
            iPartGroupRepository.get( PART_GROUP_WITH_SENSITIVITIES.getId().get() ) );
   }


   @Test
   public void get_existingPartGroupWithAlternatePartsById() throws Exception {
      AlternatePart lPart1 = defaultAlternatePartBuilder().partId( BATCH_PART_1 ).build();
      AlternatePart lPart2 =
            defaultAlternatePartBuilder().partId( PART_2 ).standard( false ).build();
      PartGroup lPartGroupToCreate =
            defaultPartGroupBuilder().alternateParts( Arrays.asList( lPart1, lPart2 ) ).build();
      Id<PartGroup> lExistingPartGroupId = iPartGroupRepository.create( lPartGroupToCreate );
      PartGroup lExistingPartGroup = iPartGroupRepository.get( lExistingPartGroupId );

      PartGroup lExpectedPartGroup = PartGroup.builder( lPartGroupToCreate )
            .id( lExistingPartGroupId ).key( lExistingPartGroup.getKey().get() ).build();
      assertEquals( lExpectedPartGroup, lExistingPartGroup );
   }


   @Test( expected = IllegalStateException.class )
   public void get_throwsExceptionWhenPartGroupDoesNotExistById() throws Exception {
      iPartGroupRepository.get( new Id<PartGroup>( UUID.randomUUID() ) );
   }


   @Test
   public void get_existingPartGroupByCodeAndConfigurationSlotId() throws Exception {
      Id<PartGroup> lExistingPartGroupId =
            iPartGroupRepository.create( defaultPartGroupBuilder().build() );
      PartGroup lExistingPartGroup = iPartGroupRepository.get( lExistingPartGroupId );

      PartGroup lPartGroup = iPartGroupRepository.get( lExistingPartGroup.getConfigurationSlotId(),
            lExistingPartGroup.getCode() );

      assertEquals( lExistingPartGroup, lPartGroup );
   }


   @Test( expected = IllegalStateException.class )
   public void get_throwsExceptionWhenPartGroupCodeDoesNotExistByCodeAndConfigurationSlotId()
         throws Exception {
      PartGroup lExistingPartGroup = defaultPartGroupBuilder().build();
      iPartGroupRepository.create( lExistingPartGroup );
      iPartGroupRepository.get( lExistingPartGroup.getConfigurationSlotId(), "nonexistent" );
   }


   @Test( expected = IllegalStateException.class )
   public void get_throwsExceptionWhenConfigurationSlotIdDoesNotExistByCodeAndConfigurationSlotId()
         throws Exception {
      PartGroup lExistingPartGroup = defaultPartGroupBuilder().build();
      iPartGroupRepository.create( lExistingPartGroup );
      iPartGroupRepository.get( new Id<ConfigSlot>( UUID.randomUUID() ),
            lExistingPartGroup.getCode() );
   }


   private void assertPartGroup( Id<PartGroup> aActualPartGroupId, PartGroup aExpectedPartGroup ) {
      Optional<PartGroup> lActualPartGroup = iPartGroupRepository.find( aActualPartGroupId );

      assertTrue( lActualPartGroup.isPresent() );

      PartGroup lPartGroup = PartGroup.builder( aExpectedPartGroup ).id( aActualPartGroupId )
            .key( lActualPartGroup.get().getKey().get() ).build();
      assertEquals( lPartGroup, lActualPartGroup.get() );
   }


   private void assertPartIncompatibilities( Id<PartGroup> aActualPartGroupId,
         List<AlternatePart> aExpectedAlternateParts ) throws MxException {
      for ( AlternatePart lAlternatePart : aExpectedAlternateParts ) {
         EqpPartBaselineTable lEqpPartBaselineTable =
               iPartGroupRepository.find( aActualPartGroupId, lAlternatePart.getPartId() );
         List<PartIncompatibility> lActualPartIncompatibilities =
               iPartGroupRepository.findPartIncompatibilities( lEqpPartBaselineTable.getPk() );

         if ( lActualPartIncompatibilities.isEmpty() ) {
            lActualPartIncompatibilities = null;
         }
         assertEquals( lAlternatePart.getPartIncompatibilities(), lActualPartIncompatibilities );
      }

   }


   private void assertTaskIncompatibilities( Id<PartGroup> aPartGroup,
         List<AlternatePart> aAlternateParts ) throws KeyConversionException {

      for ( AlternatePart lPart : aAlternateParts ) {

         PartGroupKey lPartGroup = KeyConversionUtilities.idToKey( aPartGroup, PartGroupKey.class );
         PartNoKey lPartNoKey =
               KeyConversionUtilities.idToKey( lPart.getPartId(), PartNoKey.class );
         EqpPartBaselineKey lEqpPartBaselineKey = new EqpPartBaselineKey( lPartGroup, lPartNoKey );

         Map<TaskDefnKey, IncompatibleWhen> lTasksInDatabase =
               iPartGroupRepository.findTaskIncompatibilities( lEqpPartBaselineKey );
         Map<TaskDefnKey, IncompatibleWhen> lTasksToValidate = new HashMap<>();

         for ( TaskIncompatibility lTask : lPart.getTaskIncompatibilities() ) {
            lTasksToValidate.put( lTask.getTaskDefnKey(), lTask.getIncompatibleWhen() );
         }

         assertEquals( lTasksToValidate, lTasksInDatabase );
      }
   }


   private static PartGroup.Builder defaultPartGroupBuilder() {
      return PartGroup.builder().assembly( ASSEMBLY_KEY ).code( PART_GROUP_CODE )
            .name( PART_GROUP_NAME ).quantity( QUANTITY )
            .inventoryClassKey( INVENTORY_CLASS_KEY_BATCH )
            .configurationSlotId( CONFIGURATION_SLOT_ID )
            .applicabilityRange( PART_GROUP_APPLICABILITY_RANGE )
            .purchaseTypeKey( PURCHASE_TYPE_KEY ).lineReplaceableUnit( LINE_REPLACEABLE_UNIT )
            .otherConditions( OTHER_CONDITIONS )
            .mustRequestSpecificPart( MUST_REQUEST_SPECIFIC_PART ).ietmTopicKey( IETM_TOPIC )
            .alternateParts( Collections.<AlternatePart>emptyList() );
   }


   private static AlternatePart.Builder defaultAlternatePartBuilder() {
      return new AlternatePart.Builder().partId( BATCH_PART_1 )
            .interchangeabilityOrder( INTERCHANGEABILITY_ORDER )
            .applicabilityRange( PART_APPLICABILITY_RANGE ).approved( APPROVED )
            .standard( STANDARD ).hasOtherConditions( HAS_OTHER_CONDITIONS )
            .baselineCode( BASELINE_CODE );
   }

}
