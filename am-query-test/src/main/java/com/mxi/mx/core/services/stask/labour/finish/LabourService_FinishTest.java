package com.mxi.mx.core.services.stask.labour.finish;

import static com.mxi.mx.core.key.RefLabourRoleStatusKey.ACTV;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationInterface;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.RefReferenceRequestStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.esigner.EsignatureService;
import com.mxi.mx.core.services.stask.labour.DefaultLabourRoleStatusFinder;
import com.mxi.mx.core.services.stask.labour.FinishJobTO;
import com.mxi.mx.core.services.stask.labour.LabourRoleUtils;
import com.mxi.mx.core.services.stask.labour.LabourService;
import com.mxi.mx.core.services.stask.labour.SchedLabourTO;
import com.mxi.mx.core.services.stask.panel.SchedPanelService;
import com.mxi.mx.core.services.stask.workcapture.WorkCaptureService;
import com.mxi.mx.core.table.inv.InvDamageDao;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.domain.inventory.damage.InventoryDamage;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;
import com.mxi.mx.repository.inventory.damage.InventoryDamageRepository;


/**
 * This class contains tests for {@linkplain LabourService#finish}
 *
 */
public class LabourService_FinishTest {

   private static final String damageLocationDescription = "DamageLoc";

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException expectedExpectation = ExpectedException.none();

   public LabourService labourService;

   private UserKey user;
   private HumanResourceKey humanResource;


   @Before
   public void setup() {

      // Required by the service
      user = Domain.createUser( aUser -> aUser.setUserId( 9999999 ) );
      humanResource = Domain.createHumanResource( aHr -> aHr.setUser( user ) );
      SecurityIdentificationInterface securityIdentification =
            new SecurityIdentificationStub( humanResource );
      SecurityIdentificationUtils.setInstance( securityIdentification );
      labourService = new LabourService( new LabourRoleUtils(), new EsignatureService(),
            new DefaultLabourRoleStatusFinder(), new WorkCaptureService(), new SchedPanelService(),
            new SequentialUuidGenerator() );
   }


   /**
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault on against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a repair reference
      - AND damage record required on that repair reference
      - AND the fault has no damage record
      - When a technician attempts to finish a labour on the corrective task
      - Then a ValidationException is thrown
    *
    * </pre>
    *
    */
   @Test
   public void itThrowsValidationExceptionStatingDamageRecordMissingForRepairReference()
         throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();
      InventoryKey aircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( aircraftAssembly ) );

      TaskKey correctiveTask = Domain.createCorrectiveTask( aCorrTask -> {
         aCorrTask.setInventory( aircraft );
         aCorrTask.addLabour( aLabour -> {
            aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
            aLabour.setStage( RefLabourStageKey.IN_WORK );
         } );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( aTaskTask -> {
         aTaskTask.setTaskClass( RefTaskClassKey.REPREF );
         aTaskTask.setDamageRecordBool( true );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      Domain.createFault( aFault -> {
         aFault.setCurrentRepairReference( repairReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      expectedExpectation.expect( ValidationException.class );
      expectedExpectation.expectMessage( StringContains
            .containsString( i18n.get( "core.msg.ERROR_FAULT_MISSING_DAMAGE_RECORD_message" ) ) );

      labourService.finish( schedLabourKey, new FinishJobTO() );

   }


   /**
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault on against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a repair reference
      - AND damage record required on that repair reference
      - AND the fault has a damage record
      - When a technician attempts to finish a labour on the corrective task
      - Then a ValidationException is thrown
    *
    * </pre>
    *
    */
   @Test
   public void
         itThrowsValidationExceptionStatingDamageRecordAgainstComponentMissingForRepairReference()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();
      InventoryKey aircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( aircraftAssembly ) );

      TaskKey correctiveTask = Domain.createCorrectiveTask( aCorrTask -> {
         aCorrTask.setInventory( aircraft );
         aCorrTask.addLabour( aLabour -> {
            aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
            aLabour.setStage( RefLabourStageKey.IN_WORK );
         } );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( aTaskTask -> {
         aTaskTask.setTaskClass( RefTaskClassKey.REPREF );
         aTaskTask.setDamagedComponentBool( true );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      FaultKey faultKey = Domain.createFault( aFault -> {
         aFault.setCurrentRepairReference( repairReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
      } );

      createDamageRecord( aircraft, faultKey );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      expectedExpectation.expect( ValidationException.class );
      expectedExpectation.expectMessage( StringContains.containsString(
            i18n.get( "core.msg.ERROR_FAULT_MISSING_DAMAGE_RECORD_WITH_COMPNENT_message" ) ) );

      labourService.finish( schedLabourKey, new FinishJobTO() );

   }


   /**
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault on against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a repair reference
      - AND damage record required on that repair reference
      - AND the fault has no damage record
      - When a technician attempts to finish a labour on the corrective task
      - Then a ValidationException is thrown
    *
    * </pre>
    *
    */
   @Test
   public void itCompletesLabourWhenDamageRecordAgainstAircraft() throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();
      InventoryKey aircraft =
            Domain.createAircraft( aAircraft -> aAircraft.setAssembly( aircraftAssembly ) );

      TaskKey correctiveTask = Domain.createCorrectiveTask( aCorrTask -> {

         aCorrTask.addLabour( aLabour -> {
            aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
            aLabour.setStage( RefLabourStageKey.IN_WORK );

         } );
         aCorrTask.setInventory( aircraft );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( aTaskTask -> {
         aTaskTask.setTaskClass( RefTaskClassKey.REPREF );
         aTaskTask.setDamageRecordBool( true );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      FaultKey faultKey = Domain.createFault( aFault -> {
         aFault.setCurrentRepairReference( repairReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
      } );

      createDamageRecord( aircraft, faultKey );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      FinishJobTO lFinishJobTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lFinishJobTO.setTechnician( humanResource, "" );
      lFinishJobTO.setSchedLabourTO( lSchedLabourTO );

      labourService.finish( schedLabourKey, lFinishJobTO );

      SchedLabourTable lSchedLabourTable = SchedLabourTable.findByPrimaryKey( schedLabourKey );
      assertNotNull( "Expected a labour row", lSchedLabourTable );
      assertEquals( "Expected labour to be finished", RefLabourStageKey.COMPLETE,
            lSchedLabourTable.getLabourStage() );

   }


   /**
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault on against the aircraft component
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a repair reference
      - AND damage component required on that repair reference
      - AND the fault has no damage record
      - When a technician attempts to finish a labour on the corrective task
      - Then a ValidationException is thrown
    *
    * </pre>
    *
    */
   @Test
   public void itCompletesLabourWhenDamageRecordAgainstAircraftComponent() throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey trackedInventory = Domain.createTrackedInventory();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
         aAircraft.addTracked( trackedInventory );
      } );

      TaskKey correctiveTask = Domain.createCorrectiveTask( aCorrTask -> {

         aCorrTask.addLabour( aLabour -> {
            aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
            aLabour.setStage( RefLabourStageKey.IN_WORK );
         } );
         aCorrTask.setInventory( aircraft );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( aTaskTask -> {
         aTaskTask.setTaskClass( RefTaskClassKey.REPREF );
         aTaskTask.setDamagedComponentBool( true );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      FaultKey faultKey = Domain.createFault( aFault -> {
         aFault.setCurrentRepairReference( repairReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
      } );

      createDamageRecord( trackedInventory, faultKey );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      FinishJobTO lFinishJobTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lFinishJobTO.setTechnician( humanResource, "" );
      lFinishJobTO.setSchedLabourTO( lSchedLabourTO );
      labourService.finish( schedLabourKey, lFinishJobTO );

      SchedLabourTable lSchedLabourTable = SchedLabourTable.findByPrimaryKey( schedLabourKey );
      assertNotNull( "Expected a labour row", lSchedLabourTable );
      assertEquals( "Expected labour to be finished", RefLabourStageKey.COMPLETE,
            lSchedLabourTable.getLabourStage() );

   }


   /**
    *
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault on against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a deferral reference
      - When a technician attempts to finish a labour on the corrective task
      - Then a ValidationException is thrown
    *
    * </pre>
    *
    */
   @Test
   public void
         itThrowsValidationExceptionWhenDeferralReferenceAssociatedWithFaultAndLabourAttemptedToBeFinished()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( false );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      Domain.createFault( aFault -> {
         aFault.setCurrentDeferralReference( deferralReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      FinishJobTO lFinishJobTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lFinishJobTO.setTechnician( humanResource, "" );
      lFinishJobTO.setSchedLabourTO( lSchedLabourTO );

      expectedExpectation.expect( ValidationException.class );
      expectedExpectation
            .expectMessage( StringContains.containsString( i18n.get( "core.err.33914" ) ) );

      labourService.finish( schedLabourKey, lFinishJobTO );

   }


   /**
    *
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault on against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a deferral reference
      - And the fault reference requires MOC approval
      - And the deferral reference request status is NOT approved
      - When a technician attempts to finish a labour on the corrective task
      - Then a ValidationException is thrown
    *
    * </pre>
    *
    */
   @Test
   public void
         itThrowsValidationExceptionWhenDeferralReferenceRequiresMOCApprovalButDeferralRequestNotApprovedAndLabourAttemptedToBeFinished()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRequiredMocAuth( true );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      FaultKey fault = Domain.createFault( aFault -> {
         aFault.setCurrentDeferralReference( deferralReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
      } );

      Domain.createFaultReferenceRequest( request -> {
         request.setReferenceRequestByFaultsCurrentRequest( fault );
         request.setRequestStatus( RefReferenceRequestStatusKey.PENDING.getCd() );
         request.setHumanResourceKey( humanResource );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      FinishJobTO lFinishJobTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lFinishJobTO.setTechnician( humanResource, "" );
      lFinishJobTO.setSchedLabourTO( lSchedLabourTO );

      expectedExpectation.expect( ValidationException.class );
      expectedExpectation
            .expectMessage( StringContains.containsString( i18n.get( "core.err.33910" ) ) );

      labourService.finish( schedLabourKey, lFinishJobTO );

   }


   /**
    *
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault was previously associated with a deferral reference but no current reference
      - When a technician attempts to finish a labour on the corrective task
      - Then the labour is finished
    *
    * </pre>
    *
    */
   @Test
   public void
         itFinishesLabourWhenFaultHasNoCurrentlyAssociatedFaultReferenceAndLabourAttemptedToBeFinished()
               throws Exception {

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
               aLabour.setStage( RefLabourStageKey.IN_WORK );
            } ) );

      FailDeferRefKey deferralReference = Domain.createDeferralReference( aDefRef -> {
         aDefRef.setAssemblyKey( aircraftAssembly );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
      } );

      Date faultFoundOnDate = new Date();

      Domain.createWorkPackage( aWp -> aWp.addTask( correctiveTask ) );

      Domain.createFault( aFault -> {
         aFault.setNonCurrentDeferralReference( deferralReference );
         aFault.setCorrectiveTask( correctiveTask );
         aFault.setFoundOnDate( faultFoundOnDate );
         aFault.setInventory( aircraft );
      } );

      List<SchedLabourKey> labourList = Domain.readLabourRequirement( correctiveTask );
      if ( labourList.size() > 1 )
         fail( "Expected a row from the sched_labour table" );
      SchedLabourKey schedLabourKey = labourList.get( 0 );

      FinishJobTO lFinishJobTO = new FinishJobTO();
      SchedLabourTO lSchedLabourTO = new SchedLabourTO();
      lSchedLabourTO.setEndDate( faultFoundOnDate, "" );
      lFinishJobTO.setTechnician( humanResource, "" );
      lFinishJobTO.setSchedLabourTO( lSchedLabourTO );

      labourService.finish( schedLabourKey, lFinishJobTO );

   }


   private InventoryDamageKey createDamageRecord( InventoryKey damageInventory,
         FaultKey faultKey ) {
      InventoryDamageRepository jdbcInventoryDamageRepository =
            InjectorContainer.get().getInstance( InventoryDamageRepository.class );
      InvDamageDao jdbcInvDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey lInventoryDamageKey = jdbcInvDamageDao.generatePrimaryKey();
      UUID lInventoryDamageAltId = jdbcInvDamageDao.generateAltId();

      InventoryDamage lInventoryDamage = InventoryDamage.builder().key( lInventoryDamageKey )
            .altId( lInventoryDamageAltId ).locationDescription( damageLocationDescription )
            .inventoryKey( damageInventory ).faultKey( faultKey ).build();

      return jdbcInventoryDamageRepository.create( lInventoryDamage );
   }

}
