package com.mxi.mx.core.maintenance.exec.fault.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefReferenceRequestStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.maintenance.exec.fault.domain.Fault;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReference;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReferenceRequest;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultReferenceRequest.FaultRequestStatus;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultRepository;
import com.mxi.mx.core.utils.uuid.UuidConverter;


@RunWith( BlockJUnit4ClassRunner.class )
public class JdbcFaultRepositoryTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private static final UUID FAULT_WITH_REFERENCES_UUID =
         new UuidConverter().convertStringToUuid( "E34DDEC77100452487ED91C6DE0A8CD2" );
   private static final UUID FAULT_WITHOUT_REFERENCES_UUID =
         new UuidConverter().convertStringToUuid( "59720662DDEF479DAA9038BB9B10B879" );
   private static final FaultKey FAULT_WITH_REPAIR_REFERENCE = new FaultKey( "4650:1" );
   private static final FaultKey FAULT_WITHOUT_REFERENCES = new FaultKey( "4650:2" );
   private static final FaultKey FAULT_WITH_DEFERRAL_REFERENCE = new FaultKey( "4650:3" );
   private static final FaultReferenceKey REFERENCE_WITH_REQUEST = new FaultReferenceKey( "123:1" );
   private static final FaultReferenceKey REFERENCE_WITHOUT_REQUEST =
         new FaultReferenceKey( "124:1" );
   private static final HumanResourceKey HR_KEY = new HumanResourceKey( "1:3431" );

   private FaultRepository repository;


   @Before
   public void setUp() {
      repository = InjectorContainer.get().getInstance( FaultRepository.class );
      DataLoaders.load( databaseConnectionRule.getConnection(), JdbcFaultRepositoryTest.class );
   }


   @Test
   public void getFault() {
      Fault fault = repository.get( FAULT_WITHOUT_REFERENCES ).get();
      assertNotNull( fault );
      assertEquals( fault.getAltId(), FAULT_WITHOUT_REFERENCES_UUID );
      assertEquals( fault.getName(), "name2" );
      assertEquals( fault.getAuthorizationCode(), "10011" );
   }


   @Test
   public void get_faultWithoutReferences() {
      Fault fault = repository.get( FAULT_WITHOUT_REFERENCES ).get();
      assertNotNull( fault );
      assertEquals( fault.getAltId(), FAULT_WITHOUT_REFERENCES_UUID );
      assertTrue( fault.getFaultReferences().isEmpty() );
   }


   @Test
   public void get_faultWithReferencesAndRequest() {
      Fault fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();
      assertNotNull( fault );
      assertEquals( fault.getAltId(), FAULT_WITH_REFERENCES_UUID );
      assertEquals( fault.getFaultReferences().size(), 2 );
      Optional<FaultReference> reference = fault.getFaultReferences().stream()
            .filter( r -> REFERENCE_WITH_REQUEST.equals( r.getFaultReferenceKey() ) ).findFirst();
      assertTrue( reference.isPresent() );
      assertTrue( reference.get().getFaultReferenceRequest().isPresent() );
   }


   @Test
   public void searchCurrentFaultRequests_pendingRequests() {
      List<Fault> faults = repository.searchCurrentFaultRequests( FaultRequestStatus.PENDING );
      assertNotNull( faults );
      assertEquals( 2, faults.size() );
   }


   @Test
   public void searchCurrentFaultRequests_noRequests() {
      List<Fault> faults = repository.searchCurrentFaultRequests( FaultRequestStatus.CANCELLED );
      assertNotNull( faults );
      assertEquals( 0, faults.size() );
   }


   @Test
   public void searchCurrentFaultRequests_pendingRequestsAreSortedByDateRequested() {
      List<Fault> faults = repository.searchCurrentFaultRequests( FaultRequestStatus.PENDING );
      assertNotNull( faults );
      assertEquals( 2, faults.size() );
      assertEquals( FAULT_WITH_DEFERRAL_REFERENCE, faults.get( 0 ).getId() );
      assertEquals( FAULT_WITH_REPAIR_REFERENCE, faults.get( 1 ).getId() );
   }


   @Test
   public void save_repairReferenceWithoutRequest() {
      Fault fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();
      TaskTaskKey repRefKey = new TaskTaskKey( "2:3433" );
      FaultReference faultReference = FaultReference.builder().repairReferenceKey( repRefKey )
            .notes( "new notes" ).reasonKey( new RefStageReasonKey( "1:REASON" ) ).build();
      fault.applyFaultReference( faultReference );
      FaultKey faultKey = repository.save( fault );
      assertNotNull( faultKey );
      assertEquals( faultKey, FAULT_WITH_REPAIR_REFERENCE );
      fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();
      assertEquals( fault.getFaultReferences().size(), 3 );
      assertTrue( fault.getCurrentFaultReference().isPresent() );
      assertEquals( fault.getCurrentFaultReference().get().getRepairReferenceKey(), repRefKey );
      assertEquals( fault.getCurrentFaultReference().get().getNotes(), "new notes" );
      assertFalse( fault.getCurrentFaultReference().get().getFaultReferenceRequest().isPresent() );
   }


   @Test
   public void save_repairDeferralWithoutRequest() {
      Fault fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();
      FailDeferRefKey deferRefKey = new FailDeferRefKey( "3:4356" );
      FaultReference faultReference = FaultReference.builder().deferralReferenceKey( deferRefKey )
            .notes( "new notes" ).reasonKey( new RefStageReasonKey( "1:REASON" ) ).build();

      fault.applyFaultReference( faultReference );

      FaultKey faultKey = repository.save( fault );
      assertNotNull( faultKey );
      assertEquals( faultKey, FAULT_WITH_REPAIR_REFERENCE );

      fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();

      assertEquals( fault.getFaultReferences().size(), 3 );
      assertTrue( fault.getCurrentFaultReference().isPresent() );
      assertEquals( fault.getCurrentFaultReference().get().getDeferralReferenceKey(), deferRefKey );
      assertEquals( fault.getCurrentFaultReference().get().getNotes(), "new notes" );
      assertFalse( fault.getCurrentFaultReference().get().getFaultReferenceRequest().isPresent() );
   }


   @Test
   public void save_repairReferenceWithRequest() {

      Fault fault = repository.get( FAULT_WITHOUT_REFERENCES ).get();
      TaskTaskKey repRefKey = new TaskTaskKey( "2:3433" );
      RefReferenceRequestStatusKey statusKey = new RefReferenceRequestStatusKey( "0:PENDING" );
      FaultReferenceRequest faultReferenceRequest =
            FaultReferenceRequest.builder().dateRequested( new Date() )
                  .requestorHumanResourceKey( HR_KEY ).statusKey( statusKey ).build();
      FaultReference faultReference = FaultReference.builder().repairReferenceKey( repRefKey )
            .notes( "new notes" ).reasonKey( new RefStageReasonKey( "1:REASON" ) )
            .faultReferenceRequest( faultReferenceRequest ).build();

      fault.applyFaultReference( faultReference );

      FaultKey faultKey = repository.save( fault );

      assertEquals( faultKey, FAULT_WITHOUT_REFERENCES );
      fault = repository.get( FAULT_WITHOUT_REFERENCES ).get();
      assertEquals( 1, fault.getFaultReferences().size() );
      assertTrue( fault.getCurrentFaultReference().isPresent() );
      assertEquals( fault.getCurrentFaultReference().get().getRepairReferenceKey(), repRefKey );
      assertEquals( fault.getCurrentFaultReference().get().getNotes(), "new notes" );
      assertTrue( fault.getCurrentFaultReference().get().getFaultReferenceRequest().isPresent() );
      assertEquals(
            fault.getCurrentFaultReference().get().getFaultReferenceRequest().get().getStatusKey(),
            statusKey );

   }


   @Test
   public void save_deferralReferenceWithRequest() {
      Fault fault = repository.get( FAULT_WITHOUT_REFERENCES ).get();

      FailDeferRefKey deferRefKey = new FailDeferRefKey( "3:4356" );
      FaultReferenceRequest faultReferenceRequest =
            FaultReferenceRequest.builder().dateRequested( new Date() )
                  .requestorHumanResourceKey( new HumanResourceKey( "1:3431" ) )
                  .statusKey( RefReferenceRequestStatusKey.PENDING ).build();

      FaultReference faultReference = FaultReference.builder().deferralReferenceKey( deferRefKey )
            .notes( "new notes" ).reasonKey( new RefStageReasonKey( "1:REASON" ) )
            .faultReferenceRequest( faultReferenceRequest ).build();
      fault.applyFaultReference( faultReference );
      FaultKey faultKey = repository.save( fault );
      assertNotNull( faultKey );
      assertEquals( faultKey, FAULT_WITHOUT_REFERENCES );
      fault = repository.get( FAULT_WITHOUT_REFERENCES ).get();
      assertEquals( fault.getFaultReferences().size(), 1 );
      assertTrue( fault.getCurrentFaultReference().isPresent() );
      assertEquals( fault.getCurrentFaultReference().get().getDeferralReferenceKey(), deferRefKey );
      assertEquals( fault.getCurrentFaultReference().get().getNotes(), "new notes" );
      assertTrue( fault.getCurrentFaultReference().get().getFaultReferenceRequest().isPresent() );
      assertEquals(
            fault.getCurrentFaultReference().get().getFaultReferenceRequest().get().getStatusKey(),
            RefReferenceRequestStatusKey.PENDING );

   }


   @Test
   public void remove_referenceWithoutRequest() {
      Fault fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();
      List<FaultReference> references = new ArrayList<>( fault.getFaultReferences() );

      assertEquals( fault.getFaultReferences().size(), 2 );

      references.removeIf( r -> REFERENCE_WITHOUT_REQUEST.equals( r.getFaultReferenceKey() ) );
      fault = Fault.builder().fault( fault ).faultReferences( references ).build();
      repository.save( fault );
      fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();

      assertEquals( fault.getFaultReferences().size(), 1 );
      assertTrue( fault.getCurrentFaultReference().isPresent() );
      assertEquals( fault.getCurrentFaultReference().get().getFaultReferenceKey(),
            REFERENCE_WITH_REQUEST );
      assertFalse( fault.getFaultReferences().stream()
            .filter( r -> REFERENCE_WITHOUT_REQUEST.equals( r.getFaultReferenceKey() ) ).findFirst()
            .isPresent() );
      assertTrue( fault.getFaultReferences().stream()
            .filter( r -> REFERENCE_WITH_REQUEST.equals( r.getFaultReferenceKey() ) ).findFirst()
            .isPresent() );
   }


   @Test
   public void remove_referenceWithRequest() {
      Fault fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();
      List<FaultReference> references = new ArrayList<>( fault.getFaultReferences() );

      assertEquals( 2, references.size() );

      references.removeIf( r -> REFERENCE_WITH_REQUEST.equals( r.getFaultReferenceKey() ) );

      assertEquals( 1, references.size() );

      FaultReference remainingFaultReference =
            FaultReference.builder().faultReference( references.get( 0 ) ).current( true ).build();

      references.clear();
      references.add( remainingFaultReference );
      fault = Fault.builder().fault( fault ).faultReferences( references ).build();

      repository.save( fault );
      fault = repository.get( FAULT_WITH_REPAIR_REFERENCE ).get();

      assertEquals( fault.getFaultReferences().size(), 1 );
      assertTrue( fault.getCurrentFaultReference().isPresent() );
      assertEquals( fault.getCurrentFaultReference().get().getFaultReferenceKey(),
            REFERENCE_WITHOUT_REQUEST );
      assertFalse( fault.getFaultReferences().stream()
            .filter( r -> REFERENCE_WITH_REQUEST.equals( r.getFaultReferenceKey() ) ).findFirst()
            .isPresent() );
      assertTrue( fault.getFaultReferences().stream()
            .filter( r -> REFERENCE_WITHOUT_REQUEST.equals( r.getFaultReferenceKey() ) ).findFirst()
            .isPresent() );
   }
}
