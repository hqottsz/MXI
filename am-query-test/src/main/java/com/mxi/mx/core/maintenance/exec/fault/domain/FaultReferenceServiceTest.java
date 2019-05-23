package com.mxi.mx.core.maintenance.exec.fault.domain;

import static com.mxi.mx.core.key.RefLabourRoleStatusKey.ACTV;
import static com.mxi.mx.core.key.RefLabourStageKey.IN_WORK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.CorrectiveTask;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefReferenceRequestStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


public class FaultReferenceServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private final HumanResourceKey REQUESTOR_HR = new HumanResourceKey( "4650:1" );
   private final HumanResourceKey ACTION_HR = new HumanResourceKey( "4650:2" );
   private static final String DEFERRAL_CLASS_CODE = "MEL A";
   private static final String DEF_REF_NAME = "DEF-REF-1";

   private FaultKey faultKey;
   private ConfigSlotKey rootConfigSlot;
   private InventoryKey aircraftInventoryKey;
   private TaskTaskKey repairReferenceKey;
   private FaultReference faultReference;
   private FaultRepository faultRepository;

   private FaultReferenceService faultReferenceService;


   @Before
   public void setup() {

      faultReferenceService = InjectorContainer.get().getInstance( FaultReferenceService.class );
      faultRepository = InjectorContainer.get().getInstance( FaultRepository.class );

      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addConfigurationSlot( subConfigSlot -> {
               subConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               subConfigSlot.setCode( "ABC" );
            } );
         } );
      } );

      rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssemblyKey );

      aircraftInventoryKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aircraft ) {
            aircraft.setAssembly( aircraftAssemblyKey );
            aircraft.setLocation( new LocationKey( "1:1" ) );
         }
      } );

      DomainConfiguration<RequirementDefinition> RR1 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReference1" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setMocApprovalBool( true );
               }
            };

      repairReferenceKey = Domain.createRequirementDefinition( RR1 );

      final TaskKey correctiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask correctiveTask ) {
                  correctiveTask.setInventory( aircraftInventoryKey );
               }

            } );
      faultKey = Domain.createFault( new DomainConfiguration<com.mxi.am.domain.Fault>() {

         @Override
         public void configure( com.mxi.am.domain.Fault fault ) {
            fault.setIsEvaluated( false );
            fault.setCorrectiveTask( correctiveTask );
            fault.setStatus( RefEventStatusKey.CFACTV );
            fault.setFailedSystem( rootConfigSlot );
         }
      } );
      faultReference = FaultReference.builder().repairReferenceKey( repairReferenceKey ).build();
   }


   @Test
   public void applyReference_happyPath() throws Exception {

      faultReferenceService.applyReference( REQUESTOR_HR, faultKey, faultReference );

      Optional<Fault> faultOptional = faultRepository.get( faultKey );
      assertTrue( faultOptional.isPresent() );

      Fault fault = faultOptional.get();
      assertTrue( fault.getCurrentFaultReference().isPresent() );

      Optional<FaultReference> currentReference = fault.getCurrentFaultReference();
      assertTrue( currentReference.isPresent() );

      Optional<FaultReferenceRequest> requestOptional =
            currentReference.get().getFaultReferenceRequest();
      assertTrue( requestOptional.isPresent() );
      FaultReferenceRequest request = requestOptional.get();
      assertEquals( RefReferenceRequestStatusKey.PENDING, request.getStatusKey() );
      assertEquals( REQUESTOR_HR, request.getRequestorHumanResourceKey() );

   }


   @Test( expected = FaultReferenceException.class )
   public void applyReference_deferedFault() throws Exception {

      String faultName = "fault test";
      FailDeferRefKey deferralReferenceKey = Domain.createDeferralReference( deferralReference -> {
         deferralReference.setName( DEF_REF_NAME );
         deferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
         deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
         deferralReference.setRequiredMocAuth( true );
      } );

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
         correctiveTask.setName( faultName );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setStatus( RefEventStatusKey.CFDEFER );
         fault.setFailedSystem( rootConfigSlot );
         fault.setName( faultName );
      } );

      faultReference =
            FaultReference.builder().deferralReferenceKey( deferralReferenceKey ).build();

      try {
         faultReferenceService.applyReference( REQUESTOR_HR, faultKey, faultReference );
      } catch ( FaultReferenceException e ) {
         assertEquals( e.getCode(), "33921" );
         throw e;
      }
      fail( "Exception when selecting deferral reference for deferred fault was not thrown." );

   }


   @Test( expected = FaultReferenceException.class )
   public void applyDeferralReference_mocAuthRequired_labourRowInWork() throws Exception {

      FailDeferRefKey deferralReferenceKey = Domain.createDeferralReference( deferralReference -> {
         deferralReference.setName( DEF_REF_NAME );
         deferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
         deferralReference.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferralReference.getOperators().add( new CarrierKey( "1:1" ) );
         deferralReference.setRequiredMocAuth( true );
      } );

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
         correctiveTask.addLabour( labour -> {
            labour.setStage( IN_WORK );
            labour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
         } );
      } );

      faultKey = Domain.createFault( fault -> {
         fault.setIsEvaluated( false );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
         fault.setCurrentDeferralReference( deferralReferenceKey );
         fault.setFailedSystem( rootConfigSlot );
      } );

      faultReference =
            FaultReference.builder().deferralReferenceKey( deferralReferenceKey ).build();

      // Execution
      faultReferenceService.applyReference( REQUESTOR_HR, faultKey, faultReference );
   }


   @Test
   public void approveReferenceRequest_happyPath() throws Exception {

      // ASSEMBLE
      faultReferenceService.applyReference( REQUESTOR_HR, faultKey, faultReference );

      FaultReferenceRequest request = faultRepository.get( faultKey ).get()
            .getCurrentFaultReference().get().getFaultReferenceRequest().get();

      // ACT
      faultReferenceService.approveReferenceRequest( ACTION_HR, faultKey, "123",
            request.getDateRequested() );

      // ASSERT
      Optional<Fault> faultOptional = faultRepository.get( faultKey );
      assertTrue( faultOptional.isPresent() );

      Fault fault = faultOptional.get();
      assertEquals( "123", fault.getAuthorizationCode() );

      Optional<FaultReference> currentReferenceOptional = fault.getCurrentFaultReference();
      assertTrue( currentReferenceOptional.isPresent() );

      Optional<FaultReferenceRequest> requestOptional =
            currentReferenceOptional.get().getFaultReferenceRequest();
      assertTrue( requestOptional.isPresent() );

      request = requestOptional.get();
      assertEquals( RefReferenceRequestStatusKey.APPROVED, request.getStatusKey() );
      assertEquals( REQUESTOR_HR, request.getRequestorHumanResourceKey() );
      assertEquals( ACTION_HR, request.getApproverHumanResourceKey() );
   }


   @Test
   public void rejectReferenceRequest_happyPath() throws Exception {

      // ASSEMBLE
      faultReferenceService.applyReference( REQUESTOR_HR, faultKey, faultReference );
      FaultReferenceRequest request = faultRepository.get( faultKey ).get()
            .getCurrentFaultReference().get().getFaultReferenceRequest().get();

      // ACT
      faultReferenceService.rejectReferenceRequest( ACTION_HR, faultKey,
            request.getDateRequested() );

      // ASSERT
      Optional<Fault> faultOptional = faultRepository.get( faultKey );
      assertTrue( faultOptional.isPresent() );

      Fault fault = faultOptional.get();
      Optional<FaultReference> currentReferenceOptional = fault.getCurrentFaultReference();

      Optional<FaultReferenceRequest> requestOptional =
            currentReferenceOptional.get().getFaultReferenceRequest();
      assertTrue( requestOptional.isPresent() );

      request = requestOptional.get();
      assertEquals( RefReferenceRequestStatusKey.REJECTED, request.getStatusKey() );
      assertEquals( REQUESTOR_HR, request.getRequestorHumanResourceKey() );
      assertEquals( null, request.getApproverHumanResourceKey() );
   }


   @Test
   public void cancelReferenceRequest_happyPath() throws Exception {

      // ASSMBLE
      faultReferenceService.applyReference( REQUESTOR_HR, faultKey, faultReference );

      // ACT
      faultReferenceService.cancelReferenceRequest( ACTION_HR, faultKey );

      // ASSERT
      Optional<Fault> faultOptional = faultRepository.get( faultKey );
      assertTrue( faultOptional.isPresent() );

      Fault fault = faultOptional.get();
      Optional<FaultReference> currentReferenceOptional = fault.getCurrentFaultReference();
      assertTrue( currentReferenceOptional.isPresent() );

      Optional<FaultReferenceRequest> requestOptional =
            currentReferenceOptional.get().getFaultReferenceRequest();
      assertTrue( requestOptional.isPresent() );

      FaultReferenceRequest request = requestOptional.get();
      assertEquals( RefReferenceRequestStatusKey.CANCELLED, request.getStatusKey() );
      assertEquals( REQUESTOR_HR, request.getRequestorHumanResourceKey() );
      assertEquals( null, request.getApproverHumanResourceKey() );
   }


   @Test
   public void replaceReferenceForPendingRequest_happyPath() throws Exception {

      // create new repair reference that will replace the old one.
      DomainConfiguration<RequirementDefinition> RR2 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReference2" );
                  builder.setCode( "code2" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setMocApprovalBool( true );
               }
            };

      TaskTaskKey repRefKey = Domain.createRequirementDefinition( RR2 );

      FaultReference newFaultReference =
            FaultReference.builder().repairReferenceKey( repRefKey ).build();

      // apply RR1 to the fault
      faultReferenceService.applyReference( ACTION_HR, faultKey, faultReference );

      // get the date that the request was made
      Optional<Fault> currentFaultOptional = faultRepository.get( faultKey );
      Date dateRequested = currentFaultOptional.get().getCurrentFaultReference().get()
            .getFaultReferenceRequest().get().getDateRequested();

      // replace RR1 with RR2
      faultReferenceService.replaceReferenceForPendingRequest( ACTION_HR, faultKey,
            newFaultReference, dateRequested );

      Optional<Fault> faultOptional = faultRepository.get( faultKey );
      assertTrue( faultOptional.isPresent() );

      Fault fault = faultOptional.get();
      Optional<FaultReference> currentReference = fault.getCurrentFaultReference();
      assertTrue( currentReference.isPresent() );
      assertEquals( repRefKey, currentReference.get().getRepairReferenceKey() );

      Optional<FaultReferenceRequest> requestOptional =
            currentReference.get().getFaultReferenceRequest();
      assertTrue( requestOptional.isPresent() );

      FaultReferenceRequest request = requestOptional.get();
      assertEquals( RefReferenceRequestStatusKey.PENDING, request.getStatusKey() );
      assertEquals( ACTION_HR, request.getRequestorHumanResourceKey() );
      assertEquals( null, request.getApproverHumanResourceKey() );
   }

}
