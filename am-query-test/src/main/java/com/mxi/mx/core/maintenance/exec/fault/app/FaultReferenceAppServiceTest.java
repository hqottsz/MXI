package com.mxi.mx.core.maintenance.exec.fault.app;

import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static java.math.BigDecimal.ONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.CorrectiveTask;
import com.mxi.am.domain.DeferralReference;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CapabilityKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FailDeferRefTaskDefnKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefReferenceRequestStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.maintenance.exec.fault.domain.Fault;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReference;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReference.FaultReferenceType;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReferenceRequest;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultRepository;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.services.stask.TaskServiceFactory;
import com.mxi.mx.core.services.stask.labour.LabourService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.fail.FailDeferRef;
import com.mxi.mx.core.table.fail.FailDeferRefDegradCap;
import com.mxi.mx.core.table.fail.FailDeferRefTaskDefn;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStepDao;
import com.mxi.mx.core.table.sched.SchedStepTableRow;
import com.mxi.mx.core.table.sd.SdFaultDao;
import com.mxi.mx.core.table.sd.SdFaultTable;
import com.mxi.mx.core.table.task.TaskDefnDao;
import com.mxi.mx.core.table.task.TaskDefnTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


public class FaultReferenceAppServiceTest {

   private static final int USER_ID = 0;
   private static final String TASK_BARCODE = "BARCODE-1";
   private static final String REQ_DEFINITION_CODE = "REQ_DEFN_CODE";
   private static final String CAPABILITY_DESC = "Extended Operations";
   private static final String DEFERRAL_CLASS_CODE = "MEL A";
   private static final String OPERATIONAL_RESTRICTION_FROM_DEFERRAL_REFERENCE =
         "I am a restriction in deferral reference.";
   private static final String OPERATIONAL_RESTRICTION_FROM_REPAIR_REFERENCE =
         "I am a restriction in repair reference.";
   private static final String DEF_REF_NAME = "DEF-REF-1";
   private static final CapabilityKey CAPABILITY_KEY = new CapabilityKey( 10, "ETOPS" );
   private static final CapabilityLevelKey CAPABILITY_LEVEL_KEY =
         new CapabilityLevelKey( 10, "ETOPS1", CAPABILITY_KEY );
   private static final String DEFERRAL_REFERENCE_STATUS = "ACTV";
   private static final String NON_APPLICABLE_RANGE = "200-300";
   private static final String APPLICABILITY_CODE = "100";
   private static final String DEFER_ON_APPROVAL = "DEFER_ON_APPROVAL";
   private static final HumanResourceKey REQUESTOR_HR = new HumanResourceKey( "4650:1" );

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule operateAsUserRule = new OperateAsUserRule( USER_ID, "SYSTEM" );

   private FaultReferenceAppService faultReferenceAppService;

   private SdFaultDao sdFaultDao;
   private EvtEventDao evtEventDao;
   private TaskDefnDao taskDefnDao;
   private FaultRepository faultRepository;
   private FailDeferRefKey deferralReferenceKey;
   private FaultKey faultWithDeferralKey;
   private InventoryKey aircraftInventoryKey;
   private AssemblyKey aircraftAssemblyKey;
   private HumanResourceKey hrKey;
   private TaskKey correctiveTask;
   private Date dateRequestedForFaultWithDeferral;
   private SchedStepDao schedStepDao;
   private LabourService labourService;

   private GlobalParametersStub configParms = new GlobalParametersStub( "LOGIC" );


   @Before
   public void setUp() {
      configParms.setBoolean( DEFER_ON_APPROVAL, true );
      configParms.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_BOM_PART_CD", false );
      configParms.setBoolean( "SPEC2000_UPPERCASE_TASK_CD", false );
      GlobalParameters.setInstance( configParms );

      sdFaultDao = InjectorContainer.get().getInstance( SdFaultDao.class );
      evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      taskDefnDao = InjectorContainer.get().getInstance( TaskDefnDao.class );
      schedStepDao = InjectorContainer.get().getInstance( SchedStepDao.class );

      faultReferenceAppService =
            InjectorContainer.get().getInstance( FaultReferenceAppService.class );
      faultRepository = InjectorContainer.get().getInstance( FaultRepository.class );

      OrgHr orgHrTable = OrgHr.findByUserId( new UserKey( USER_ID ) );
      orgHrTable.setAllAuthority( true );
      orgHrTable.update();
      hrKey = orgHrTable.getPk();

      aircraftAssemblyKey = Domain.createAircraftAssembly();

      aircraftInventoryKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssemblyKey );
            aircraft.setLocation( new LocationKey( "1:1" ) );
            aircraft.setApplicabilityCode( "06" );
         }
      } );

      correctiveTask = Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

         @Override
         public void configure( CorrectiveTask correctiveTask ) {
            correctiveTask.setBarcode( TASK_BARCODE );
            correctiveTask.setInventory( aircraftInventoryKey );
         }

      } );

      deferralReferenceKey =
            Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

               @Override
               public void configure( DeferralReference deferralReference ) {

                  deferralReference
                        .setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
                  deferralReference.setName( DEF_REF_NAME );
                  deferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
                  deferralReference.setAssemblyKey( aircraftAssemblyKey );
                  deferralReference.setStatus( DEFERRAL_REFERENCE_STATUS );
                  deferralReference.setOperationalRestrictions(
                        OPERATIONAL_RESTRICTION_FROM_DEFERRAL_REFERENCE );
                  deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
                  deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
               }
            } );

      faultWithDeferralKey =
            Domain.createFault( new DomainConfiguration<com.mxi.am.domain.Fault>() {

               @Override
               public void configure( com.mxi.am.domain.Fault fault ) {
                  fault.setIsEvaluated( false );
                  fault.setCorrectiveTask( correctiveTask );
                  fault.setInventory( aircraftInventoryKey );
                  fault.setStatus( RefEventStatusKey.CFACTV );
                  fault.setCurrentDeferralReference( deferralReferenceKey );
                  fault.setCreateReferenceRequest( true );
               }
            } );

      dateRequestedForFaultWithDeferral = faultRepository.get( faultWithDeferralKey ).get()
            .getCurrentFaultReference().get().getFaultReferenceRequest().get().getDateRequested();

      labourService = ( LabourService ) TaskServiceFactory.getInstance().getLabourService();

   }


   @Test
   public void approveRepairRequest() {

      // ASSEMBLE
      TaskTaskKey repairReferenceKey = Domain.createRequirementDefinition( reference -> {
         reference.setTaskName( "repairReferenceOtherAssembly" );
         reference.setCode( "abc" );
         reference.setTaskClass( RefTaskClassKey.REPREF );
         reference.setMocApprovalBool( true );
         reference.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reference.againstConfigurationSlot( new ConfigSlotKey( "1000:badAss:1" ) );
         reference.setOpsRestrictionsDesc( OPERATIONAL_RESTRICTION_FROM_REPAIR_REFERENCE );
         reference.setMocApprovalBool( true );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 1" );
         } );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 2" );
         } );
         reference.addLabourRequirement( new LabourRequirement( RefLabourSkillKey.ENG,
               BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE ) );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setIsEvaluated( false );
         fault.setCorrectiveTask( correctiveTask );
         fault.setInventory( aircraftInventoryKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
         fault.setCurrentRepairReference( repairReferenceKey );
         fault.setCreateReferenceRequest( true );
      } );

      Date dateRequested = faultRepository.get( faultKey ).get().getCurrentFaultReference().get()
            .getFaultReferenceRequest().get().getDateRequested();

      // ACT
      FaultApprovalRequestResponseTO response =
            faultReferenceAppService.approveReferenceRequest( faultKey, dateRequested );

      // ASSERT
      assertNotNull( response );
      assertNotNull( response.getAuthorizationCode() );
      assertNull( response.getSystemNotification() );

      SdFaultTable sdFaultTable = sdFaultDao.findByPrimaryKey( faultKey );

      assertNotNull( sdFaultTable.getEventKey() );

      assertNotNull( "Authorization code has to be generated when approving a request",
            sdFaultTable.getDeferCdSDesc() );

      assertEquals(
            "Operatonal restrictions from the deferral reference were not updated in the fault ",
            MxCoreUtils.appendString( hrKey, null, OPERATIONAL_RESTRICTION_FROM_REPAIR_REFERENCE ),

            sdFaultTable.getOperationalRestriction() );

      List<com.mxi.mx.core.services.stask.labour.LabourRequirement> schedLabourRequirements =
            labourService.getTaskLabourRequeriments( correctiveTask );
      assertEquals( 1, schedLabourRequirements.size() );

      // check the first step
      Fault fault = faultRepository.get( faultKey ).get();

      SchedStepKey schedStepKey =
            new SchedStepKey( fault.getTaskKey().getDbId(), fault.getTaskKey().getId(), 1 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertTrue( schedStepTableRow.exists() );

      schedStepKey =
            new SchedStepKey( fault.getTaskKey().getDbId(), fault.getTaskKey().getId(), 2 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertTrue( schedStepTableRow.exists() );

      Optional<FaultReference> currentReferenceOptional = fault.getCurrentFaultReference();
      assertTrue( currentReferenceOptional.isPresent() );

      Optional<FaultReferenceRequest> referenceRequestOptional =
            currentReferenceOptional.get().getFaultReferenceRequest();
      assertTrue( currentReferenceOptional.isPresent() );

      assertEquals( "Fault request was not approved", referenceRequestOptional.get().getStatusKey(),
            RefReferenceRequestStatusKey.APPROVED );
      assertNotNull( "Fault request date resolved was not updated",
            referenceRequestOptional.get().getDateResolved() );

      EventKey eventKey = sdFaultDao.findByPrimaryKey( faultKey ).getEventKey();
      assertNotNull( eventKey );
      assertEquals( "A fault with repair reference cannot be deferred", RefEventStatusKey.CFACTV,
            evtEventDao.findByPrimaryKey( eventKey ).getEventStatus() );

   }


   @Test
   public void approveDeferralRequest_withoutReccurringInspection() {

      FaultApprovalRequestResponseTO response = faultReferenceAppService
            .approveReferenceRequest( faultWithDeferralKey, dateRequestedForFaultWithDeferral );

      assertNotNull( response );
      assertNotNull( response.getAuthorizationCode() );
      assertNull( response.getSystemNotification() );

      SdFaultTable sdFaultTable = sdFaultDao.findByPrimaryKey( faultWithDeferralKey );

      assertNotNull( sdFaultTable.getEventKey() );

      assertNotNull( "Authorization code has to be generated when approving a request",
            sdFaultTable.getDeferCdSDesc() );

      assertTrue(
            "Operatonal restrictions from the deferral reference were not updated in the fault ",
            sdFaultTable.getOperationalRestriction()
                  .contains( OPERATIONAL_RESTRICTION_FROM_DEFERRAL_REFERENCE ) );

      EventKey eventKey = sdFaultDao.findByPrimaryKey( faultWithDeferralKey ).getEventKey();
      assertNotNull( eventKey );
      assertEquals( "There is no defer event for the fault", RefEventStatusKey.CFDEFER,
            evtEventDao.findByPrimaryKey( eventKey ).getEventStatus() );

   }


   /**
    * This test is testing if the config param DEFER_ON_APPROVAL is false, the fault will not be
    * deferred automatically.
    *
    */
   @Test
   public void approveDeferralRequest_withDeferOnApprovalFalse() {

      configParms.setBoolean( DEFER_ON_APPROVAL, false );

      EventKey eventKey = sdFaultDao.findByPrimaryKey( faultWithDeferralKey ).getEventKey();

      assertEquals( "Fault is deferred incorrectly.", RefEventStatusKey.CFACTV,
            evtEventDao.findByPrimaryKey( eventKey ).getEventStatus() );

   }


   @Test
   public void approveDeferralRequest_withReccurringInspection() {
      // create a requirement definition
      final ConfigSlotKey aircraftRootConfigSlot = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {

                  requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
                  requirementDefinition.setCode( REQ_DEFINITION_CODE );
                  requirementDefinition.setRevisionNumber( 1 );
                  requirementDefinition.setStatus( ACTV );
                  requirementDefinition.addRecurringSchedulingRule( CDY, ONE );
                  requirementDefinition.isOnCondition();
                  requirementDefinition.setRecurring( true );
                  requirementDefinition.setScheduledFromEffectiveDate( new Date() );
                  requirementDefinition.setMinimumForecastRange( ONE );
                  requirementDefinition.setOrganization( null );
               }
            } );

      // associate the requirement with the deferral reference as recurring inspection
      TaskDefnTable taskDefnTable = taskDefnDao
            .findByPrimaryKey( TaskTaskTable.findByPrimaryKey( reqDefnKey ).getTaskDefn() );

      FailDeferRefTaskDefnKey failDeferRefTaskDefnKey = new FailDeferRefTaskDefnKey(
            FailDeferRef.findByPrimaryKey( deferralReferenceKey ).getAlternateKey(),
            taskDefnTable.getAlternateKey() );

      FailDeferRefTaskDefn failDeferRefTaskDefn =
            FailDeferRefTaskDefn.create( failDeferRefTaskDefnKey );
      failDeferRefTaskDefn.insert();

      FaultApprovalRequestResponseTO response = faultReferenceAppService
            .approveReferenceRequest( faultWithDeferralKey, dateRequestedForFaultWithDeferral );

      // system notification
      final String lSystemErrorMessage =
            "Create the recurring inspection task(s) manually if necessary. "
                  + "The system could not initialize one or more of the associated recurring inspection(s) for the fault "
                  + TASK_BARCODE + " using the deferral reference " + DEF_REF_NAME + ".";

      assertEquals( lSystemErrorMessage, response.getSystemNotification() );

      SdFaultTable sdFaultTable = sdFaultDao.findByPrimaryKey( faultWithDeferralKey );

      assertNotNull( "Authorization code has to be generated when approving a request",
            sdFaultTable.getDeferCdSDesc() );

      assertNotNull( sdFaultTable.getEventKey() );
      assertEquals( "There is no defer event for the fault", RefEventStatusKey.CFDEFER,
            evtEventDao.findByPrimaryKey( sdFaultTable.getEventKey() ).getEventStatus() );

      assertNotNull(
            "Operatonal restrictions from the repair reference were not updated in the fault ",
            sdFaultTable.getOperationalRestriction() );

   }


   @Test
   public void approveDeferralRequest_withCapabilities() throws Exception {

      // set up capabilities and capabilities level for assembly
      addCapabilities();
      addCapabilitiesLevels();
      assignCapabilitiesToAssembly();

      // add degraded capability to the deferral reference
      FailDeferRefDegradCap failDeferRefDegradCap = new FailDeferRefDegradCap();
      failDeferRefDegradCap.setDeferralReferenceId(
            FailDeferRef.findByPrimaryKey( deferralReferenceKey ).getAlternateKey() );
      failDeferRefDegradCap.setCapability( CAPABILITY_KEY );
      failDeferRefDegradCap.setCapabilityLevel( CAPABILITY_LEVEL_KEY );
      failDeferRefDegradCap.insert();

      // authorize deferral
      FaultApprovalRequestResponseTO response = faultReferenceAppService
            .approveReferenceRequest( faultWithDeferralKey, dateRequestedForFaultWithDeferral );

      assertNotNull( response.getAuthorizationCode() );
      assertNull( response.getSystemNotification() );

      SdFaultTable sdFaultTable = sdFaultDao.findByPrimaryKey( faultWithDeferralKey );

      assertNotNull( "Authorization code has to be generated when approving a request",
            sdFaultTable.getDeferCdSDesc() );

      assertNotNull( sdFaultTable.getEventKey() );
      assertEquals( "There is no defer event for the fault", RefEventStatusKey.CFDEFER,
            evtEventDao.findByPrimaryKey( sdFaultTable.getEventKey() ).getEventStatus() );
   }


   @Test
   public void searchReferences_withRepairAndDeferralReferences() {
      createReferences();
      List<ReferenceSearchResponseTO> references = faultReferenceAppService
            .searchReferences( faultWithDeferralKey, null, null, "reference" );
      assertNotNull( references );
      assertEquals( 2, references.size() );
      // both deferral and repair references should be returned
      assertTrue( references.stream().filter( r -> r.getName().equals( "repair reference 1" )
            && r.getType() == FaultReferenceType.REPAIR ).findFirst().isPresent() );
      assertTrue( references.stream().filter( r -> r.getName().equals( "deferral reference 1" )
            && r.getType() == FaultReferenceType.DEFERRAL ).findFirst().isPresent() );
   }


   @Test
   public void searchReferences_forDeferredFault() {

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setStatus( RefEventStatusKey.CFDEFER );
         fault.setCorrectiveTask( correctiveTask );
      } );

      createReferences();
      List<ReferenceSearchResponseTO> references =
            faultReferenceAppService.searchReferences( faultKey, null, null, "reference" );
      assertNotNull( references );
      assertEquals( 1, references.size() );
      // only repair references should be returned for deferred fault
      assertTrue( references.stream().filter( r -> r.getName().equals( "repair reference 1" )
            && r.getType() == FaultReferenceType.REPAIR ).findFirst().isPresent() );

   }


   /**
    * Returns repair reference types and deferral reference types. The test sets up 2 deferral types
    * and 2 repair types. Only one of the each type is used. The result set should return the 3
    * types that are used by the valid references.
    */
   @Test
   public void searchReferenceTypes_withRepairAndDeferrals() {

      // Deferral Type 1
      RefFailureSeverityKey refFailureSeverityKey = new RefFailureSeverityKey( "0:SEV1" );
      Domain.createFailureSeverity( failureSeverity -> {
         failureSeverity.setId( refFailureSeverityKey );
         failureSeverity.setName( "Severity 1" );
      } );

      // Deferral Type 2
      RefFailureSeverityKey refFailureSeverityKey2 = new RefFailureSeverityKey( "0:SEV2" );
      Domain.createFailureSeverity( failureSeverity -> {
         failureSeverity.setId( refFailureSeverityKey2 );
         failureSeverity.setName( "Severity 2" );
      } );

      // Repair type 1
      RefTaskSubclassKey refTaskSubclassKey = new RefTaskSubclassKey( "10:REP1" );
      Domain.createTaskSubClass( taskSubClass -> {
         taskSubClass.setId( refTaskSubclassKey );
         taskSubClass.setName( "Task subclass 1" );
         taskSubClass.setTaskClassKey( RefTaskClassKey.REPREF );
      } );

      // Repair type 2
      RefTaskSubclassKey refTaskSubclassKey2 = new RefTaskSubclassKey( "10:REP2" );
      Domain.createTaskSubClass( taskSubClass -> {
         taskSubClass.setId( refTaskSubclassKey2 );
         taskSubClass.setName( "Task subclass 2" );
         taskSubClass.setTaskClassKey( RefTaskClassKey.REPREF );
      } );

      // Valid repair reference that uses repair reference type 1
      DomainConfiguration<RequirementDefinition> RR1 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReference1" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1234:ACFT:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setTaskSubClass( refTaskSubclassKey );
               }
            };

      Domain.createRequirementDefinition( RR1 );

      // Valid deferral reference that uses deferral reference type 1
      Domain.createDeferralReference( deferralReference -> {
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
         deferralReference.setName( "deferral reference 1" );
         deferralReference.setFaultSeverityKey( refFailureSeverityKey );
         deferralReference.setAssemblyKey( aircraftAssemblyKey );
         deferralReference.setStatus( DEFERRAL_REFERENCE_STATUS );
         deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
      } );

      // Deferral reference that uses deferral reference type 2 but is invalid due to applicability
      // range.
      Domain.createDeferralReference( deferralReference -> {
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
         deferralReference.setName( "deferral reference 1" );
         deferralReference.setFaultSeverityKey( refFailureSeverityKey2 );
         deferralReference.setAssemblyKey( aircraftAssemblyKey );
         deferralReference.setStatus( DEFERRAL_REFERENCE_STATUS );
         deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
         deferralReference.setApplicabilityRange( NON_APPLICABLE_RANGE );
      } );

      List<ReferenceTypeTO> types =
            faultReferenceAppService.searchReferenceTypes( faultWithDeferralKey );

      // The size of the list should be 3 since there are 2 valid references set up here and one in
      // setup().
      assertEquals( 3, types.size() );
      assertTrue( types.stream().filter( t -> t.getId().equals( refFailureSeverityKey )
            && t.getType() == FaultReferenceType.DEFERRAL ).findFirst().isPresent() );
      assertTrue( types.stream().filter( t -> t.getId().equals( refTaskSubclassKey )
            && t.getType() == FaultReferenceType.REPAIR ).findFirst().isPresent() );
      assertTrue( types.stream().filter( t -> t.getId().equals( RefFailureSeverityKey.MEL )
            && t.getType() == FaultReferenceType.DEFERRAL ).findFirst().isPresent() );
   }


   @Test
   public void searchReferenceTypes_forDeferredFault() {

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setStatus( RefEventStatusKey.CFDEFER );
         fault.setCorrectiveTask( correctiveTask );
      } );

      RefFailureSeverityKey refFailureSeverityKey = new RefFailureSeverityKey( "0:SEV1" );
      Domain.createFailureSeverity( failureSeverity -> {
         failureSeverity.setId( refFailureSeverityKey );
         failureSeverity.setName( "Severity 1" );
      } );

      RefTaskSubclassKey refTaskSubclassKey = new RefTaskSubclassKey( "10:REP1" );
      Domain.createTaskSubClass( taskSubClass -> {
         taskSubClass.setId( refTaskSubclassKey );
         taskSubClass.setName( "Task subclass 1" );
         taskSubClass.setTaskClassKey( RefTaskClassKey.REPREF );
      } );

      DomainConfiguration<RequirementDefinition> RR1 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReference1" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1234:ACFT:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setTaskSubClass( refTaskSubclassKey );
               }
            };

      Domain.createRequirementDefinition( RR1 );

      Domain.createDeferralReference( deferralReference -> {
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
         deferralReference.setName( "deferral reference 1" );
         deferralReference.setFaultSeverityKey( refFailureSeverityKey );
         deferralReference.setAssemblyKey( aircraftAssemblyKey );
         deferralReference.setStatus( DEFERRAL_REFERENCE_STATUS );
         deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
      } );

      List<ReferenceTypeTO> types = faultReferenceAppService.searchReferenceTypes( faultKey );

      assertFalse( types.stream().filter( t -> t.getId().equals( refFailureSeverityKey )
            && t.getType() == FaultReferenceType.DEFERRAL ).findFirst().isPresent() );
      assertFalse( types.stream().filter( t -> t.getId().equals( RefFailureSeverityKey.MEL )
            && t.getType() == FaultReferenceType.DEFERRAL ).findFirst().isPresent() );
      // only task subclasses should be returned
      assertTrue( types.stream().filter( t -> t.getId().equals( refTaskSubclassKey )
            && t.getType() == FaultReferenceType.REPAIR ).findFirst().isPresent() );
   }


   private void createReferences() {

      Domain.createRequirementDefinition( builder -> {
         builder.setTaskName( "repair reference 1" );
         builder.setCode( "abc1" );
         builder.setTaskClass( RefTaskClassKey.REPREF );
         builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
         builder.againstConfigurationSlot( new ConfigSlotKey( aircraftAssemblyKey, 1 ) );

      } );

      Domain.createDeferralReference( deferralReference -> {
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
         deferralReference.setName( "deferral reference 1" );
         deferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         deferralReference.setAssemblyKey( aircraftAssemblyKey );
         deferralReference.setStatus( DEFERRAL_REFERENCE_STATUS );
         deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
      } );

   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft inventory based on that assembly with an applicability code
    * And a fault against the aircraft
    * And a deferral reference associated with the fault
    * And the deferral reference has an on-condition requirement definition associated with it
    * And the requirement definition has an applicablity range outside of the aircraft's applicability code
    * When the deferral reference is approved
    * Then no exception is thrown
    * </pre>
    *
    */
   @Test
   public void
         approveDeferralRequest_withNonApplicableRequirementDoesntThrowTaskDefinitionNotApplicableException() {

      // ASSEMBLE
      InventoryKey aircraftInventoryKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aircraft ) {
                  aircraft.setAssembly( aircraftAssemblyKey );
                  aircraft.setLocation( Domain.createLocation() );
                  aircraft.setApplicabilityCode( APPLICABILITY_CODE );
               }
            } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask correctiveTask ) {
                  correctiveTask.setBarcode( TASK_BARCODE );
                  correctiveTask.setInventory( aircraftInventoryKey );
               }
            } );

      FailDeferRefKey deferralReferenceKey =
            Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

               @Override
               public void configure( DeferralReference deferralReference ) {

                  deferralReference
                        .setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
                  deferralReference.setName( DEF_REF_NAME );
                  deferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
                  deferralReference.setAssemblyKey( aircraftAssemblyKey );
                  deferralReference.setStatus( DEFERRAL_REFERENCE_STATUS );
                  deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
                  deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
               }
            } );

      FaultKey faultKey = Domain.createFault( new DomainConfiguration<com.mxi.am.domain.Fault>() {

         @Override
         public void configure( com.mxi.am.domain.Fault fault ) {
            fault.setIsEvaluated( false );
            fault.setCorrectiveTask( correctiveTask );
            fault.setInventory( aircraftInventoryKey );
            fault.setStatus( RefEventStatusKey.CFACTV );
            fault.setCurrentDeferralReference( deferralReferenceKey );
            fault.setCreateReferenceRequest( true );
         }
      } );

      final ConfigSlotKey aircraftRootConfigSlot = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      OrgKey organization = Domain.createOrganization( org -> org.setType( RefOrgTypeKey.MRO ) );

      UserKey user = Domain.createUser( aUser -> aUser.setUserId( 10001 ) );
      Domain.createHumanResource( aHr -> {
         aHr.setAllAuthority( true );
         aHr.setOrganization( organization );
         aHr.setUser( user );
      } );

      final TaskTaskKey reqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition requirementDefinition ) {
                  requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
                  requirementDefinition.setCode( REQ_DEFINITION_CODE );
                  requirementDefinition.setRevisionNumber( 1 );
                  requirementDefinition.setStatus( ACTV );
                  requirementDefinition.isOnCondition();
                  requirementDefinition.setApplicabilityRange( NON_APPLICABLE_RANGE );
                  requirementDefinition.setOrganization( organization );
               }
            } );

      // associate the requirement with the deferral reference as recurring inspection
      TaskDefnDao taskDefnDao = InjectorContainer.get().getInstance( TaskDefnDao.class );
      TaskDefnTable taskDefnTable = taskDefnDao
            .findByPrimaryKey( TaskTaskTable.findByPrimaryKey( reqDefnKey ).getTaskDefn() );

      FailDeferRefTaskDefnKey failDeferRefTaskDefnKey = new FailDeferRefTaskDefnKey(
            FailDeferRef.findByPrimaryKey( deferralReferenceKey ).getAlternateKey(),
            taskDefnTable.getAlternateKey() );

      FailDeferRefTaskDefn failDeferRefTaskDefn =
            FailDeferRefTaskDefn.create( failDeferRefTaskDefnKey );
      failDeferRefTaskDefn.insert();

      Date dateRequested = faultRepository.get( faultKey ).get().getCurrentFaultReference().get()
            .getFaultReferenceRequest().get().getDateRequested();

      // ACT
      FaultApprovalRequestResponseTO response =
            faultReferenceAppService.approveReferenceRequest( faultKey, dateRequested );

      // ASSERT
      assertNotNull( "Expected an authorization code", response.getAuthorizationCode() );
      // Assert that no notification is received in the response.
      assertNull( "Did not expect an system notification", response.getSystemNotification() );

   }


   /**
    *
    * Test that when a repair reference is applied, the steps from the reference are copied to the
    * fault if MOC approval is not required. This test focuses on the copying of the steps since the
    * 'applyReference' functionality is heavily tested by the FaultReferenceServiceTest.
    *
    * @throws Exception
    */
   @Test
   public void applyReference_copyStepsAndSkillsMOCNotRequired() throws Exception {
      // ASSEMBLE
      TaskTaskKey repairReferenceKey = createRepairReferenceWithStepsAndLabour( false );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCorrectiveTask( correctiveTask );
      } );

      FaultReference faultReference =
            FaultReference.builder().repairReferenceKey( repairReferenceKey ).build();

      // ACT
      faultReferenceAppService.applyReference( REQUESTOR_HR, faultKey, faultReference );

      // ASSERT
      List<com.mxi.mx.core.services.stask.labour.LabourRequirement> schedLabourRequirements =
            labourService.getTaskLabourRequeriments( correctiveTask );
      assertEquals( 1, schedLabourRequirements.size() );

      // check the first step
      Fault fault = faultRepository.get( faultKey ).get();

      SchedStepKey schedStepKey =
            new SchedStepKey( fault.getTaskKey().getDbId(), fault.getTaskKey().getId(), 1 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertTrue( schedStepTableRow.exists() );

      schedStepKey =
            new SchedStepKey( fault.getTaskKey().getDbId(), fault.getTaskKey().getId(), 2 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertTrue( schedStepTableRow.exists() );
   }


   @Test
   public void applyReference_copyStepsAndSkillsMOCRequired() throws Exception {
      // ASSEMBLE
      TaskTaskKey repairReferenceKey = createRepairReferenceWithStepsAndLabour( true );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCorrectiveTask( correctiveTask );
      } );

      FaultReference faultReference =
            FaultReference.builder().repairReferenceKey( repairReferenceKey ).build();

      // ACT
      faultReferenceAppService.applyReference( REQUESTOR_HR, faultKey, faultReference );

      // ASSERT
      List<com.mxi.mx.core.services.stask.labour.LabourRequirement> schedLabourRequirements =
            labourService.getTaskLabourRequeriments( correctiveTask );
      assertEquals( 0, schedLabourRequirements.size() );

      // check the first step
      Fault fault = faultRepository.get( faultKey ).get();

      SchedStepKey schedStepKey =
            new SchedStepKey( fault.getTaskKey().getDbId(), fault.getTaskKey().getId(), 1 );
      SchedStepTableRow schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertFalse( schedStepTableRow.exists() );

      schedStepKey =
            new SchedStepKey( fault.getTaskKey().getDbId(), fault.getTaskKey().getId(), 2 );
      schedStepTableRow = schedStepDao.findByPrimaryKey( schedStepKey );
      assertFalse( schedStepTableRow.exists() );
   }


   /**
    *
    * Add capability codes
    *
    */
   private void addCapabilities() {

      DataSetArgument args = new DataSetArgument();
      args.add( CAPABILITY_KEY, "acft_cap_db_id", "acft_cap_cd" );
      args.add( "desc_sdesc", CAPABILITY_DESC );
      args.add( "cap_order", 1 );

      MxDataAccess.getInstance().executeInsert( "ref_acft_cap", args );
   }


   /**
    *
    * Add capability levels
    *
    */
   private void addCapabilitiesLevels() {

      DataSetArgument args = new DataSetArgument();
      args.add( CAPABILITY_LEVEL_KEY, "acft_cap_level_db_id", "acft_cap_level_cd", "acft_cap_db_id",
            "acft_cap_cd" );
      args.add( "desc_sdesc", CAPABILITY_DESC );
      args.add( "level_order", 1 );

      MxDataAccess.getInstance().executeInsert( "ref_acft_cap_level", args );
   }


   /**
    *
    * Assign capabilities to the aircraft assembly
    *
    */
   private void assignCapabilitiesToAssembly() {

      DataSetArgument args = new DataSetArgument();
      args.add( "assmbl_db_id", aircraftAssemblyKey.getDbId() );
      args.add( "assmbl_cd", aircraftAssemblyKey.getCd() );
      args.add( "acft_cap_db_id", CAPABILITY_KEY.getDbId() );
      args.add( "acft_cap_cd", CAPABILITY_KEY.getCd() );
      args.add( "acft_cap_level_db_id", CAPABILITY_LEVEL_KEY.getDbId() );
      args.add( "acft_cap_level_cd", CAPABILITY_LEVEL_KEY.getCd() );

      MxDataAccess.getInstance().executeInsert( "assmbl_cap_levels", args );
   }


   private TaskTaskKey createRepairReferenceWithStepsAndLabour( boolean mocApprovalBool ) {
      return Domain.createRequirementDefinition( reference -> {
         reference.setTaskClass( RefTaskClassKey.REPREF );
         reference.setTaskName( "Repair Reference" );
         reference.setCode( "REPREF1" );
         reference.setMocApprovalBool( mocApprovalBool );
         reference.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 1" );
         } );
         reference.addStep( step -> {
            step.setDescription( "Repair Step 2" );
         } );
         reference.addLabourRequirement( new LabourRequirement( RefLabourSkillKey.ENG,
               BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE ) );
      } );
   }

}
