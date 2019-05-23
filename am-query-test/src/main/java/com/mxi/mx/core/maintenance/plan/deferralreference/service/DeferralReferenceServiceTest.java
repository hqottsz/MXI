package com.mxi.mx.core.maintenance.plan.deferralreference.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.DeferralReference;
import com.mxi.am.domain.DeferralReference.DeferralReferenceDeadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Operator;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.exception.InvalidReftermException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FailDeferCarrierKey;
import com.mxi.mx.core.key.FailDeferRefConflictDefKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefSevTypeKey;
import com.mxi.mx.core.maintenance.plan.deferralreference.dao.DeferralReferenceSearchCriteria;
import com.mxi.mx.core.maintenance.plan.deferralreference.exception.ConflictingAssociatedDeferralReferenceException;
import com.mxi.mx.core.maintenance.plan.deferralreference.model.DeadlineDetails;
import com.mxi.mx.core.maintenance.plan.deferralreference.model.DeferralReferenceDetails;
import com.mxi.mx.core.maintenance.plan.deferralreference.model.DeferralReferenceListItemDetails;
import com.mxi.mx.core.maintenance.plan.deferralreference.model.OperatorDetails;
import com.mxi.mx.core.services.fault.DuplicateNameException;
import com.mxi.mx.core.table.eqp.EqpAssmbl;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.fail.FailDeferCarrier;
import com.mxi.mx.core.table.fail.FailDeferRef;
import com.mxi.mx.core.table.fail.FailDeferRefConflictDef;
import com.mxi.mx.core.table.fail.FailDeferRefDead;
import com.mxi.mx.core.table.org.OrgCarrierTable;
import com.mxi.mx.core.utils.uuid.UuidConverter;


/**
 * Test the {@link DeferralReferenceService} class
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class DeferralReferenceServiceTest {

   private static final String DEFAULT_NAME = "Deferral Reference";
   private static final ArrayList<DeferralReferenceDeadline> NO_CUSTOM_DEADLINES =
         new ArrayList<>();
   private final UuidConverter uuidConverter = new UuidConverter();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // Service class under test
   private DeferralReferenceServiceInterface iService = new DeferralReferenceService();

   // Test data
   private static final String FAILED_SYSTEM_CONFIG_SLOT_CODE = "SYSTEM 1";
   private static final String CORRECT_APPL_RANGE = "05-09";
   private static final String INCORRECT_APPL_RANGE = "20-28";
   private DeferralReferenceDetails iDeferralReferenceDetails;
   private AssemblyKey iAssemblyKey;
   private UUID iAssemblyId;
   private OrgCarrierTable iTableOrgCarrier;
   private UUID iFailedSystemAltId;
   private InventoryKey aircraftKey;


   @Before
   public void loadData() throws Exception {
      // have an assembly
      iAssemblyKey = Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAcftAssy ) {
            aAcftAssy.setCode( "ACFT-1" );
         }
      } );

      aircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setApplicabilityCode( "08" );
         }
      } );

      EqpAssmbl lAssembly = EqpAssmbl.findByPrimaryKey( iAssemblyKey );
      iAssemblyId = lAssembly.getAlternateKey();

      // have a carrier to be used as the operator
      CarrierKey lCarrierKey = createValidOperator();
      iTableOrgCarrier = OrgCarrierTable.findByPrimaryKey( lCarrierKey );

      // create a failed system config slot
      ConfigSlotKey lFailedSystemConfigSlotDbKey =
            new ConfigSlotBuilder( FAILED_SYSTEM_CONFIG_SLOT_CODE ).build();
      EqpAssmblBom lFailedSystemConfigSlot =
            EqpAssmblBom.findByPrimaryKey( lFailedSystemConfigSlotDbKey );

      iFailedSystemAltId = lFailedSystemConfigSlot.getAlternateKey();
   }


   /*
    * Tests the deferral reference creation action
    */
   @Test
   public void save_createDeferralReference() throws Exception {

      setDeferralReferenceDetails();

      // set related list
      List<DeferralReferenceListItemDetails> lRelatedDeferralList = new ArrayList<>();
      lRelatedDeferralList.add( new DeferralReferenceListItemDetails(
            createRelatedDeferralReference( "RELATED" ).getAlternateKey() ) );

      iDeferralReferenceDetails.setAssociated( lRelatedDeferralList );

      // set conflicted list
      List<DeferralReferenceListItemDetails> lConflictedDeferralList = new ArrayList<>();
      lConflictedDeferralList.add( new DeferralReferenceListItemDetails(
            createRelatedDeferralReference( "CONFLICTING" ).getAlternateKey() ) );
      iDeferralReferenceDetails.setConflicting( lConflictedDeferralList );

      // execute test
      UUID lId = iService.save( iDeferralReferenceDetails );

      // assert
      FailDeferRef lFailDeferRefTable = FailDeferRef.findByAltId( lId );

      assertEquals( iDeferralReferenceDetails.getName(), lFailDeferRefTable.getDeferralSDesc() );
      assertEquals( iDeferralReferenceDetails.getAssemblyId(), iAssemblyId );
      assertEquals( new RefSevTypeKey( 0, iDeferralReferenceDetails.getDeferralClass().getCd() ),
            lFailDeferRefTable.getDeferral() );
      assertEquals( new RefFailDeferKey( 0, iDeferralReferenceDetails.getFaultSeverityCode() ),
            lFailDeferRefTable.getSeverity() );
      assertEquals( -1, lFailDeferRefTable.getInstalledSystemQty() );
      assertEquals( 1, lFailDeferRefTable.getOperationalSystemQty() );
      assertFalse( lFailDeferRefTable.getMocApprovalBool() );
   }


   @Test
   public void save_update_addRequiredFieldsToLegacy() throws Exception {

      FailDeferRefKey lFailDeferRefKey = createLegacyDeferralReference();
      UUID lId = FailDeferRef.findByPrimaryKey( lFailDeferRefKey ).getAlternateKey();

      // set up details for update
      setDeferralReferenceDetails();
      iDeferralReferenceDetails.setId( lId );

      // execute the method under test
      iService.save( iDeferralReferenceDetails );

      FailDeferRef lDefRefTable = FailDeferRef.findByAltId( lId );
      UUID lExpectedFailedSystemId =
            uuidConverter.convertStringToUuid( iDeferralReferenceDetails.getFailedSystemId() );

      // Make sure the failed system can be updated on legacy deferral references
      assertEquals( lExpectedFailedSystemId, lDefRefTable.getAssemblyBomId() );
   }


   @Test
   public void save_updateDeferralReference() throws Exception {

      // have a deferral reference in the system
      FailDeferRefKey lFailDeferRefKey = createDeferralReferenceWithConflictedAndRelated();
      UUID lId = FailDeferRef.findByPrimaryKey( lFailDeferRefKey ).getAlternateKey();

      // set up details for update
      setDeferralReferenceDetails();
      iDeferralReferenceDetails.setId( lId );
      iDeferralReferenceDetails.setName( "UPDATED" );
      iDeferralReferenceDetails.setStatusCode( "INACTIVE" );
      iDeferralReferenceDetails.setRequiredMocAuth( false );

      // update the operator
      List<OperatorDetails> lOperators = new ArrayList<>();
      CarrierKey lOperator1 = Domain.createOperator( new DomainConfiguration<Operator>() {

         @Override
         public void configure( Operator aOperator ) {

            aOperator.setIATACode( "XX" );
            aOperator.setIATACode( "YYY" );
            aOperator.setCarrierCode( "XX-YYY" );

         }
      } );

      lOperators.add( new OperatorDetails( lOperator1 ) );
      iDeferralReferenceDetails.setOperators( lOperators );

      // update the deferral conflicting
      FailDeferRefKey lConflictingDeferKey = createDeferralReference();
      FailDeferRef lTableConflictingDefer = FailDeferRef.findByPrimaryKey( lConflictingDeferKey );
      lTableConflictingDefer.setDeferralSDesc( "CONFLICTING 2" );
      lTableConflictingDefer.setAssembly( iAssemblyKey );
      lTableConflictingDefer.update();
      List<DeferralReferenceListItemDetails> lConflictedDeferralList = new ArrayList<>();
      lConflictedDeferralList.add(
            new DeferralReferenceListItemDetails( lTableConflictingDefer.getAlternateKey() ) );

      iDeferralReferenceDetails.setConflicting( lConflictedDeferralList );

      // execute test
      iService.save( iDeferralReferenceDetails );

      // assert
      FailDeferRef lDefRefTable = FailDeferRef.findByAltId( lId );
      UUID lDefRefUuid = lDefRefTable.getAlternateKey();
      String lDefRefId = uuidConverter.convertUuidToString( lDefRefUuid );

      assertEquals( iDeferralReferenceDetails.getName(), lDefRefTable.getDeferralSDesc() );
      assertEquals( iDeferralReferenceDetails.getId(), lDefRefUuid );
      assertEquals( iDeferralReferenceDetails.getStatusCode(), lDefRefTable.getStatus() );
      assertEquals( iDeferralReferenceDetails.isRequiredMocAuth(),
            lDefRefTable.getMocApprovalBool() );

      // deferral reference and new operator relationship is not null
      FailDeferCarrierKey lFailDeferCarrierKey = new FailDeferCarrierKey(
            lDefRefTable.getPk().getFailDeferRefDbId(), lDefRefTable.getPk().getFailDeferRefId(),
            lOperator1.getDbId(), lOperator1.getId() );
      assertNotNull( FailDeferCarrier.findByPrimaryKey( lFailDeferCarrierKey ) );

      // new operator is persisted
      assertEquals( "XX-YYY", OrgCarrierTable.findByPrimaryKey( lOperator1 ).getCode() );

      // deferral reference and new conflicting relationship is not null
      FailDeferRefConflictDefKey lFailDeferRefConflictDefKey =
            new FailDeferRefConflictDefKey( lDefRefId,
                  uuidConverter.convertUuidToString( lTableConflictingDefer.getAlternateKey() ) );
      assertNotNull( FailDeferRefConflictDef.findByPrimaryKey( lFailDeferRefConflictDefKey ) );
   }


   @Test( expected = ConflictingAssociatedDeferralReferenceException.class )
   public void save_cannotBeConflictingAndAssociatedAtSameTime() throws Exception {

      setDeferralReferenceDetails();

      // set related list
      List<DeferralReferenceListItemDetails> lAssociatedDeferralReferences = new ArrayList<>();
      lAssociatedDeferralReferences.add( new DeferralReferenceListItemDetails(
            createRelatedDeferralReference( "CONFLICTING" ).getAlternateKey() ) );
      iDeferralReferenceDetails.setAssociated( lAssociatedDeferralReferences );

      // set conflicted list same as related
      iDeferralReferenceDetails.setConflicting( lAssociatedDeferralReferences );

      // execute test
      iService.save( iDeferralReferenceDetails );
   }


   /*
    * Tests that the deferral reference is not duplicated
    */
   @Test( expected = DuplicateNameException.class )
   public void save_duplicateName() throws Exception {

      // have an existing deferral reference in the system
      FailDeferRefKey lFailDeferRefKey = createDeferralReference();
      FailDeferRef lTableDeferRef = FailDeferRef.findByPrimaryKey( lFailDeferRefKey );
      lTableDeferRef.setDeferralSDesc( "NAME" );
      lTableDeferRef.setAssembly( iAssemblyKey );
      lTableDeferRef.update();

      // add operators
      CarrierKey lOperator = Domain.createOperator();
      FailDeferCarrier.create( lFailDeferRefKey, lOperator ).insert();

      // attempt to create a new one with same name, operator list, assembly code
      setDeferralReferenceDetails();
      iDeferralReferenceDetails.setName( "NAME" );

      // set operator
      List<OperatorDetails> lOperators = new ArrayList<>();
      lOperators.add( new OperatorDetails( lOperator ) );
      iDeferralReferenceDetails.setOperators( lOperators );

      // set assembly
      iDeferralReferenceDetails.setAssemblyId( iAssemblyId );

      // execute test
      iService.save( iDeferralReferenceDetails );

   }


   @Test( expected = InvalidReftermException.class )
   public void save_invalidSeverityCode() throws Exception {

      setDeferralReferenceDetails();
      iDeferralReferenceDetails.setFaultSeverityCode( "INVALID" );

      // execute test
      iService.save( iDeferralReferenceDetails );
   }


   @Test( expected = IllegalStateException.class )
   public void save_severityCodeIsAOG() throws Exception {

      setDeferralReferenceDetails();
      iDeferralReferenceDetails.setFaultSeverityCode( "AOG" );

      // execute test
      iService.save( iDeferralReferenceDetails );
   }


   @Test
   public void search_withoutCriteria() throws Exception {

      createLegacyDeferralReference();

      createDeferralReference();

      Collection<DeferralReferenceDetails> lDeferralReferenceList = iService.search( null );

      Assert.assertEquals( 2, lDeferralReferenceList.size() );
   }


   @Test
   public void search_filterByStatus() throws Exception {

      createDeferralReference();

      DeferralReferenceSearchCriteria lDeferralReferenceSearchCriteria =
            new DeferralReferenceSearchCriteria();
      lDeferralReferenceSearchCriteria.setStatus( "INACTV" );

      Collection<DeferralReferenceDetails> lDeferralReferenceList =
            iService.search( lDeferralReferenceSearchCriteria );

      Assert.assertEquals( 0, lDeferralReferenceList.size() );
   }


   @Test
   public void search_filterByType() throws Exception {

      createDeferralReference( "10-20-30 Deferral Reference", NO_CUSTOM_DEADLINES,
            RefFailureSeverityKey.MEL );
      createDeferralReference( "10-20-40 Deferral Reference", NO_CUSTOM_DEADLINES,
            RefFailureSeverityKey.MINOR );
      createDeferralReference( "10-20-50 Deferral Reference", NO_CUSTOM_DEADLINES,
            RefFailureSeverityKey.MINOR );

      DeferralReferenceSearchCriteria lDeferralReferenceSearchCriteria =
            new DeferralReferenceSearchCriteria();
      lDeferralReferenceSearchCriteria.setType( RefFailureSeverityKey.MINOR.toValueString() );

      Collection<DeferralReferenceDetails> lDeferralReferenceList =
            iService.search( lDeferralReferenceSearchCriteria );

      Assert.assertEquals( 2, lDeferralReferenceList.size() );
   }


   @Test
   public void search_filterByQueryString() throws Exception {
      String lDeferralReferenceToFilterOut = "Filtered out";
      String lQueryString = "20";

      // Set up the test data, 3 deferral references
      createDeferralReference( "10-20-30 Deferral Reference", NO_CUSTOM_DEADLINES );
      createDeferralReference( "10-20-40 Deferral Reference", NO_CUSTOM_DEADLINES );
      createDeferralReference( lDeferralReferenceToFilterOut, NO_CUSTOM_DEADLINES );

      // Perform a search without the filter and ensure all 3 records come back
      DeferralReferenceSearchCriteria lSearchCriteria = new DeferralReferenceSearchCriteria();
      lSearchCriteria.setQueryString( "" );

      Collection<DeferralReferenceDetails> lResults = iService.search( lSearchCriteria );

      assertEquals( 3, lResults.size() );

      // Perform a search with the filter and ensure the non-matching record is filtered out
      lSearchCriteria.setQueryString( lQueryString );

      lResults = iService.search( lSearchCriteria );

      // Make sure there are 2 results
      assertEquals( 2, lResults.size() );

      // Make sure the expected deferral reference has been filtered out
      for ( DeferralReferenceDetails lDetails : lResults ) {
         assertNotEquals( lDeferralReferenceToFilterOut, lDetails.getName() );
      }
   }


   @Test
   public void search_filterByFailedSystemId_nonNull() throws Exception {
      // Set up two deferral references, one with a failed system and one without
      createLegacyDeferralReference();
      createDeferralReference();

      DeferralReferenceSearchCriteria lSearchCriteria = new DeferralReferenceSearchCriteria();
      lSearchCriteria.setFailedSystemId( DeferralReferenceSearchCriteria.NON_NULL_VALUE );

      Collection<DeferralReferenceDetails> lResults = iService.search( lSearchCriteria );

      assertEquals( 1, lResults.size() );
      String lFailedSystemId = lResults.iterator().next().getFailedSystemId();
      assertNotNull( lFailedSystemId );
   }


   @Test
   public void search_filterByFailedSystemId_null() throws Exception {
      // Set up two deferral references, one with a failed system and one without
      createLegacyDeferralReference();
      createDeferralReference();

      DeferralReferenceSearchCriteria lSearchCriteria = new DeferralReferenceSearchCriteria();
      lSearchCriteria.setFailedSystemId( DeferralReferenceSearchCriteria.NULL_VALUE );

      Collection<DeferralReferenceDetails> lResults = iService.search( lSearchCriteria );

      assertEquals( 1, lResults.size() );
      String lFailedSystemId = lResults.iterator().next().getFailedSystemId();
      assertEquals( null, lFailedSystemId );
   }


   @Test
   public void search_onlyApplicableReferences() throws Exception {

      // Set up two deferral references, one with a valid applicability range and the other with
      // an invalid applicability range
      Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

         @Override
         public void configure( DeferralReference aDeferralReference ) {
            aDeferralReference.setApplicabilityRange( CORRECT_APPL_RANGE );
            aDeferralReference.setName( "REF1" );
            aDeferralReference.setFaultDeferralKey( new RefFailDeferKey( "MEL A" ) );
            aDeferralReference.setAssemblyKey( iAssemblyKey );
         }
      } );

      Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

         @Override
         public void configure( DeferralReference aDeferralReference ) {
            aDeferralReference.setApplicabilityRange( INCORRECT_APPL_RANGE );
            aDeferralReference.setName( "REF2" );
            aDeferralReference.setFaultDeferralKey( new RefFailDeferKey( "MEL B" ) );
            aDeferralReference.setAssemblyKey( iAssemblyKey );
         }
      } );

      DeferralReferenceSearchCriteria searchCriteria = new DeferralReferenceSearchCriteria();
      searchCriteria.setAircraftKey( Optional.of( aircraftKey ) );

      Collection<DeferralReferenceDetails> results = iService.search( searchCriteria );

      assertEquals( 1, results.size() );
      String name = results.iterator().next().getName();
      assertEquals( "REF1", name );

   }


   @Test
   public void getById_findsDeferralReferenceById() throws Exception {

      UUID lDeferralReferenceId =
            FailDeferRef.findByPrimaryKey( createDeferralReference() ).getAlternateKey();

      DeferralReferenceDetails lDeferralReference = iService.getById( lDeferralReferenceId );

      Assert.assertEquals( lDeferralReferenceId, lDeferralReference.getId() );
   }


   @Test
   public void save_updateDeadlineInfoFromDefaultToDefault() throws Exception {
      // set up baseline deferral reference
      FailDeferRefKey lFailDeferRefKey = createDeferralReference();

      // update baseline deferral reference
      UUID lNewDeferralReferenceId =
            FailDeferRef.findByPrimaryKey( lFailDeferRefKey ).getAlternateKey();

      setDeferralReferenceDetails();
      iDeferralReferenceDetails.setId( lNewDeferralReferenceId );

      List<DeadlineDetails> lNewCustomDeadlines = iDeferralReferenceDetails.getDeadlines();
      {
         // remove all custom deadlines
         lNewCustomDeadlines.clear();
      }

      // update the deferral reference with no custom deadlines
      lNewDeferralReferenceId = iService.save( iDeferralReferenceDetails );

      // assert
      DeferralReferenceDetails lNewDeferralReference = iService.getById( lNewDeferralReferenceId );
      List<DeadlineDetails> lCustomDeadlines = lNewDeferralReference.getDeadlines();

      Assert.assertTrue( lCustomDeadlines.isEmpty() );
   }


   @Test
   public void save_updateDeadlineInfoFromDefaultToCustom() throws Exception {
      // set up baseline deferral reference
      FailDeferRefKey lFailDeferRefKey = createDeferralReference();

      // update baseline deferral reference
      UUID lNewDeferralReferenceId =
            FailDeferRef.findByPrimaryKey( lFailDeferRefKey ).getAlternateKey();

      setDeferralReferenceDetails();

      iDeferralReferenceDetails.setId( lNewDeferralReferenceId );

      Map<DataTypeKey, DeadlineDetails> lNewCustomDeadlinesMap =
            new HashMap<DataTypeKey, DeadlineDetails>();
      List<DeadlineDetails> lNewCustomDeadlines = iDeferralReferenceDetails.getDeadlines();
      {
         // add some custom deadlines
         DeadlineDetails lCreateCalendarDayDeadline =
               createDeadline( DataTypeKey.CDY, new BigDecimal( 1 ).setScale(
                     FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );
         DeadlineDetails lCreateFlightHoursDeadline =
               createDeadline( DataTypeKey.HOURS, new BigDecimal( 2 ).setScale(
                     FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );
         DeadlineDetails lCreateCyclesDeadline =
               createDeadline( DataTypeKey.CYCLES, new BigDecimal( 3 ).setScale(
                     FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );

         lNewCustomDeadlines.add( lCreateCalendarDayDeadline );
         lNewCustomDeadlines.add( lCreateFlightHoursDeadline );
         lNewCustomDeadlines.add( lCreateCyclesDeadline );
         lNewCustomDeadlinesMap.put( DataTypeKey.CDY, lCreateCalendarDayDeadline );
         lNewCustomDeadlinesMap.put( DataTypeKey.HOURS, lCreateFlightHoursDeadline );
         lNewCustomDeadlinesMap.put( DataTypeKey.CYCLES, lCreateCyclesDeadline );
      }

      // update the deferral reference with custom deadlines
      lNewDeferralReferenceId = iService.save( iDeferralReferenceDetails );

      // assert
      DeferralReferenceDetails lNewDeferralReference = iService.getById( lNewDeferralReferenceId );
      List<DeadlineDetails> lCustomDeadlines = lNewDeferralReference.getDeadlines();

      Assert.assertEquals( lNewCustomDeadlines.size(), lCustomDeadlines.size() );
      for ( DeadlineDetails lDeferralReferenceDeadlineDetails : lCustomDeadlines ) {
         DataTypeKey lDataTypeKey = lDeferralReferenceDeadlineDetails.getType();
         BigDecimal lQuantity = lDeferralReferenceDeadlineDetails.getQuantity();
         DeadlineDetails lDeferralReferenceDeadlineDetailsPushedIn =
               lNewCustomDeadlinesMap.get( lDataTypeKey );
         BigDecimal lQuantityPushedIn = lDeferralReferenceDeadlineDetailsPushedIn.getQuantity();

         Assert.assertNotNull( lDeferralReferenceDeadlineDetailsPushedIn );
         Assert.assertEquals( lQuantity, lQuantityPushedIn );
      }
   }


   @Test
   public void save_updateDeadlineInfoFromCustomToDefault() throws Exception {
      // set up baseline deferral reference
      final List<DeferralReferenceDeadline> lOriginalCustomDeadlines = new ArrayList<>();
      {
         // add some custom deadlines
         DeferralReferenceDeadline lCreateCalendarDayDeadline = new DeferralReferenceDeadline();
         {
            lCreateCalendarDayDeadline.setDataType( DataTypeKey.CDY );
            lCreateCalendarDayDeadline.setQuantity( new BigDecimal( 1 )
                  .setScale( FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );
         }
         DeferralReferenceDeadline lCreateFlightHoursDeadline = new DeferralReferenceDeadline();
         {
            lCreateFlightHoursDeadline.setDataType( DataTypeKey.HOURS );
            lCreateFlightHoursDeadline.setQuantity( new BigDecimal( 2 )
                  .setScale( FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );
         }
         DeferralReferenceDeadline lCreateCyclesDeadline = new DeferralReferenceDeadline();
         {
            lCreateCyclesDeadline.setDataType( DataTypeKey.CYCLES );
            lCreateCyclesDeadline.setQuantity( new BigDecimal( 3 )
                  .setScale( FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );
         }

         lOriginalCustomDeadlines.add( lCreateCalendarDayDeadline );
         lOriginalCustomDeadlines.add( lCreateFlightHoursDeadline );
         lOriginalCustomDeadlines.add( lCreateCyclesDeadline );
      }
      FailDeferRefKey lFailDeferRefKey =
            createDeferralReference( DEFAULT_NAME, lOriginalCustomDeadlines );

      // update baseline deferral reference
      UUID lNewDeferralReferenceId =
            FailDeferRef.findByPrimaryKey( lFailDeferRefKey ).getAlternateKey();
      DeferralReferenceDetails lNewDeferralReference = iService.getById( lNewDeferralReferenceId );
      List<DeadlineDetails> lPersistedDeadlines = lNewDeferralReference.getDeadlines();

      Assert.assertEquals( lPersistedDeadlines.size(), lOriginalCustomDeadlines.size() );

      setDeferralReferenceDetails();

      iDeferralReferenceDetails.setId( lNewDeferralReferenceId );

      List<DeadlineDetails> lNewCustomDeadlines = iDeferralReferenceDetails.getDeadlines();
      {
         // remove all custom deadlines
         lNewCustomDeadlines.clear();
      }

      // update the deferral reference with custom deadlines
      lNewDeferralReferenceId = iService.save( iDeferralReferenceDetails );

      // assert
      lNewDeferralReference = iService.getById( lNewDeferralReferenceId );
      List<DeadlineDetails> lCustomDeadlines = lNewDeferralReference.getDeadlines();

      Assert.assertTrue( lCustomDeadlines.isEmpty() );
   }


   /**
    * Test:
    * <ol>
    * <li>Adding a new custom deadline.
    * <li>Removing an existing custom deadline.
    * <li>Modifying an existing custom deadline.
    * </ol>
    *
    * @throws Exception
    *
    */
   @Test
   public void save_updateDeadlineInfoFromCustomToCustom() throws Exception {
      // set up baseline deferral reference
      final List<DeferralReferenceDeadline> lOriginalCustomDeadlines = new ArrayList<>();
      {
         // add some custom deadlines
         DeferralReferenceDeadline lCreateCalendarDayDeadline = new DeferralReferenceDeadline();
         {
            lCreateCalendarDayDeadline.setDataType( DataTypeKey.CDY );
            lCreateCalendarDayDeadline.setQuantity( new BigDecimal( 1 )
                  .setScale( FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );
         }
         DeferralReferenceDeadline lCreateFlightHoursDeadline = new DeferralReferenceDeadline();
         {
            lCreateFlightHoursDeadline.setDataType( DataTypeKey.HOURS );
            lCreateFlightHoursDeadline.setQuantity( new BigDecimal( 2 )
                  .setScale( FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );
         }

         lOriginalCustomDeadlines.add( lCreateCalendarDayDeadline );
         lOriginalCustomDeadlines.add( lCreateFlightHoursDeadline );
      }
      FailDeferRefKey lFailDeferRefKey =
            createDeferralReference( DEFAULT_NAME, lOriginalCustomDeadlines );

      // update baseline deferral reference
      UUID lNewDeferralReferenceId =
            FailDeferRef.findByPrimaryKey( lFailDeferRefKey ).getAlternateKey();

      setDeferralReferenceDetails();

      iDeferralReferenceDetails.setId( lNewDeferralReferenceId );

      Map<DataTypeKey, DeadlineDetails> lNewCustomDeadlinesMap =
            new HashMap<DataTypeKey, DeadlineDetails>();
      List<DeadlineDetails> lNewCustomDeadlines = iDeferralReferenceDetails.getDeadlines();
      {
         // modify the flight hours deadline
         DeadlineDetails lCreateFlightHoursDeadline =
               createDeadline( DataTypeKey.HOURS, new BigDecimal( 8 ).setScale(
                     FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );

         // add a cycles deadline
         DeadlineDetails lCreateCyclesDeadline =
               createDeadline( DataTypeKey.CYCLES, new BigDecimal( 3 ).setScale(
                     FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );

         lNewCustomDeadlines.add( lCreateFlightHoursDeadline );
         lNewCustomDeadlines.add( lCreateCyclesDeadline );
         lNewCustomDeadlinesMap.put( DataTypeKey.HOURS, lCreateFlightHoursDeadline );
         lNewCustomDeadlinesMap.put( DataTypeKey.CYCLES, lCreateCyclesDeadline );
      }

      // update the deferral reference with custom deadlines
      lNewDeferralReferenceId = iService.save( iDeferralReferenceDetails );

      // assert
      DeferralReferenceDetails lNewDeferralReference = iService.getById( lNewDeferralReferenceId );
      List<DeadlineDetails> lCustomDeadlines = lNewDeferralReference.getDeadlines();

      Assert.assertEquals( lNewCustomDeadlinesMap.size(), lCustomDeadlines.size() );
      for ( DeadlineDetails lDeferralReferenceDeadlineDetails : lCustomDeadlines ) {
         DataTypeKey lDataTypeKey = lDeferralReferenceDeadlineDetails.getType();
         BigDecimal lQuantity = lDeferralReferenceDeadlineDetails.getQuantity();
         DeadlineDetails lDeferralReferenceDeadlineDetailsPushedIn =
               lNewCustomDeadlinesMap.get( lDataTypeKey );
         BigDecimal lQuantityPushedIn = lDeferralReferenceDeadlineDetailsPushedIn.getQuantity();

         Assert.assertNotNull( lDeferralReferenceDeadlineDetailsPushedIn );
         Assert.assertEquals( lQuantity, lQuantityPushedIn );
      }
   }


   /**
    * Creates an operator in the system
    *
    * @return {@link CarrierKey}
    */
   private CarrierKey createValidOperator() {

      CarrierKey lOperator = Domain.createOperator( new DomainConfiguration<Operator>() {

         @Override
         public void configure( Operator aOperator ) {

            aOperator.setIATACode( "AA" );
            aOperator.setIATACode( "BBB" );
            aOperator.setCarrierCode( "AA-BBB" );

         }
      } );

      return lOperator;
   }


   /**
    * Creates a deadline details object
    *
    * @param aType
    * @param aQuantity
    * @return {@link DeadlineDetails}
    */
   private DeadlineDetails createDeadline( DataTypeKey aType, BigDecimal aQuantity ) {
      DeadlineDetails lDeadline = new DeadlineDetails();
      lDeadline.setType( aType );
      lDeadline.setQuantity( aQuantity );
      return lDeadline;
   }


   /**
    * Creates a deferral reference in the system without related or conflicted.
    *
    * @return FailDeferRefKey
    */
   private FailDeferRefKey createDeferralReference( final String aName,
         final List<DeferralReferenceDeadline> aCustomDeadlines ) {
      return createDeferralReference( aName, aCustomDeadlines, RefFailureSeverityKey.MEL );
   }


   /**
    * Creates a deferral reference in the system without related or conflicted.
    *
    * @return FailDeferRefKey
    */
   private FailDeferRefKey createDeferralReference( final String aName,
         final List<DeferralReferenceDeadline> aCustomDeadlines, RefFailureSeverityKey severity ) {

      FailDeferRefKey lFailDeferRefKey =
            Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

               @Override
               public void configure( DeferralReference aDeferralReference ) {
                  aDeferralReference.setStatus( "ACTV" );
                  aDeferralReference.setName( aName );
                  aDeferralReference.setFaultSeverityKey( severity );
                  aDeferralReference.setFaultDeferralKey( new RefFailDeferKey( "MEL A" ) );
                  aDeferralReference.setAssemblyKey( iAssemblyKey );
                  aDeferralReference.getFailedSystemInfo()
                        .setFailedSystemAltId( iFailedSystemAltId );
                  aDeferralReference.setInstalledSystems( 1 );
                  aDeferralReference.setOperationalSystemsForDispatch( 1 );
                  aDeferralReference.setRequiredMocAuth( false );

                  List<CarrierKey> lOperators = new ArrayList<>();
                  lOperators.add( iTableOrgCarrier.getPk() );

                  aDeferralReference.setOperators( lOperators );

                  List<DeferralReferenceDeadline> lCustomDeadlines = new ArrayList<>();
                  if ( aCustomDeadlines != null ) {
                     lCustomDeadlines = aCustomDeadlines;
                  }

                  aDeferralReference.setDeadlines( lCustomDeadlines );
               }

            } );

      return lFailDeferRefKey;
   }


   private FailDeferRefKey createDeferralReference() {
      return createDeferralReference( DEFAULT_NAME, NO_CUSTOM_DEADLINES );
   }


   /**
    * Populate the {@link DeferralReference} with mandatory data.
    *
    * @param aDeferralReference
    * @param aCustomDeadlines
    *           Pass an empty list to indicate that the default scheduling defined by the fault
    *           class is to be applied.
    */
   private void setupBasicDeferralReference( DeferralReference aDeferralReference,
         List<DeferralReferenceDeadline> aCustomDeadlines ) {

      aDeferralReference.setStatus( "ACTV" );
      aDeferralReference.setName( DEFAULT_NAME );
      aDeferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
      aDeferralReference.setFaultDeferralKey( new RefFailDeferKey( "MEL A" ) );
      aDeferralReference.setAssemblyKey( iAssemblyKey );
      aDeferralReference.getFailedSystemInfo().setFailedSystemAltId( iFailedSystemAltId );
      aDeferralReference.setInstalledSystems( 1 );
      aDeferralReference.setOperationalSystemsForDispatch( 1 );
      aDeferralReference.setRequiredMocAuth( false );
      List<CarrierKey> lOperators = new ArrayList<>();
      lOperators.add( iTableOrgCarrier.getPk() );

      aDeferralReference.setOperators( lOperators );

      List<DeferralReferenceDeadline> lCustomDeadlines = new ArrayList<>();
      if ( aCustomDeadlines != null ) {
         lCustomDeadlines = aCustomDeadlines;
      }

      aDeferralReference.setDeadlines( lCustomDeadlines );
   }


   /**
    * Creates a deferral reference in the system with conflicted and related deferrals.
    *
    * @return FailDeferRefKey
    */
   private FailDeferRefKey createDeferralReferenceWithConflictedAndRelated() {
      List<DeferralReferenceDeadline> lCustomDeadlines = new ArrayList<>();

      return createDeferralReferenceWithConflictedAndRelated( lCustomDeadlines );
   }


   /**
    * Creates a deferral reference in the system with conflicted and related deferrals.
    *
    * @return FailDeferRefKey
    */
   private FailDeferRefKey createDeferralReferenceWithConflictedAndRelated(
         final List<DeferralReferenceDeadline> aCustomDeadlines ) {

      FailDeferRefKey lFailDeferRefKey =
            Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

               @Override
               public void configure( DeferralReference aDeferralReferenceConfiguration ) {

                  setupBasicDeferralReference( aDeferralReferenceConfiguration, aCustomDeadlines );

                  // set related list
                  List<String> lRelatedDeferralList = new ArrayList<>();
                  lRelatedDeferralList.add( uuidConverter.convertUuidToString(
                        ( createRelatedDeferralReference( "RELATED" ).getAlternateKey() ) ) );
                  aDeferralReferenceConfiguration
                        .setAssociatedDeferralReferences( lRelatedDeferralList );

                  // set conflicted list
                  List<String> lConflictedDeferralList = new ArrayList<>();
                  lConflictedDeferralList.add( uuidConverter.convertUuidToString(
                        ( createRelatedDeferralReference( "CONFLICTING" ).getAlternateKey() ) ) );

                  aDeferralReferenceConfiguration
                        .setConflictingDeferralReferences( lConflictedDeferralList );

               }
            } );

      return lFailDeferRefKey;
   }


   private FailDeferRefKey createLegacyDeferralReference() {
      FailDeferRefKey lFailDeferRefKey =
            Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

               @Override
               public void configure( DeferralReference aDeferralReferenceConfiguration ) {
                  setupBasicDeferralReference( aDeferralReferenceConfiguration, null );
                  aDeferralReferenceConfiguration.getFailedSystemInfo()
                        .setFailedSystemAltId( null );
               }
            } );

      return lFailDeferRefKey;
   }


   /**
    * Set deferral reference configuration details pojo with valid data.
    *
    */
   private void setDeferralReferenceDetails() {

      iDeferralReferenceDetails = new DeferralReferenceDetails();
      iDeferralReferenceDetails.setName( "NAME" );
      iDeferralReferenceDetails.setFaultSeverityCode( "MEL" );
      iDeferralReferenceDetails.setDeferralClass( new RefFailDeferKey( "MEL A" ) );
      iDeferralReferenceDetails.setNumberInstalled( -1 );
      iDeferralReferenceDetails.setRequiredForDispatch( 1 );
      iDeferralReferenceDetails.setStatusCode( "ACTV" );
      iDeferralReferenceDetails
            .setFailedSystemId( uuidConverter.convertUuidToString( iFailedSystemAltId ) );
      iDeferralReferenceDetails.setAssemblyId( iAssemblyId );

      List<OperatorDetails> lOperators = new ArrayList<>();
      lOperators.add( new OperatorDetails( createValidOperator() ) );
      iDeferralReferenceDetails.setOperators( lOperators );
   }


   private FailDeferRef createRelatedDeferralReference( String aDescription ) {
      FailDeferRef lTableRelatedDefer = FailDeferRef.findByPrimaryKey( createDeferralReference() );
      lTableRelatedDefer.setDeferralSDesc( aDescription );
      lTableRelatedDefer.setAssembly( iAssemblyKey );
      lTableRelatedDefer.update();

      return lTableRelatedDefer;
   }

}
