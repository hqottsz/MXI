package com.mxi.mx.core.services.fault;

import static com.mxi.mx.core.key.RefLabourRoleStatusKey.ACTV;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.DeferralReference.FailedSystemInfo;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.RefFailTypeBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationInterface;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.exception.InvalidReftermException;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailTypeKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefFaultSourceKey;
import com.mxi.mx.core.key.RefReferenceRequestStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceDao;
import com.mxi.mx.core.production.task.domain.TaskTerminatedEvent;
import com.mxi.mx.core.services.inventory.exception.LockedInventoryException;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.sd.SdFaultDao;
import com.mxi.mx.core.table.sd.SdFaultTable;
import com.mxi.mx.core.table.task.TaskDefnDao;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.table.evt.EvtInv;
import com.mxi.mx.core.unittest.table.evt.EvtStage;
import com.mxi.mx.core.unittest.table.sd.SdFault;
import com.mxi.mx.persistence.uuid.UuidUtils;


/**
 * This class tests the fault service.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class FaultServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private FaultKey faultKeyDefault;
   private InventoryKey inventoryKeyDefault;
   private FaultService faultService;

   public SdFaultDao jdbcSdFaultDao;

   private RecordingEventBus eventBus;


   @Test
   public void testSetInventory() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().withDescription( "ABC" ).build();
      InventoryKey newInventoryKey = new InventoryBuilder().withDescription( "DEF" ).build();

      TaskKey corrTaskKey = new TaskBuilder().onInventory( inventoryKey ).build();
      FaultKey faultKey = new SdFaultBuilder().onInventory( inventoryKey )
            .withCorrectiveTask( corrTaskKey ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setInventory( newInventoryKey, faultKey.getEventKey(), false,
            hr );

      // check Event inventory
      EvtInv lEvtInv = new EvtInv( faultKey.getEventKey(), newInventoryKey );
      lEvtInv.assertExist();

      // check the stage note message
      EvtStage lStage = new EvtStage( faultKey.getEventKey() );
      lStage.assertHr( hr );
      lStage.assertStageNote( "The failed system changed from ABC to DEF" );
   }


   @Test
   public void testSetInventoryNoChange() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();

      TaskKey corrTaskKey = new TaskBuilder().onInventory( inventoryKey ).build();
      FaultKey faultKey = new SdFaultBuilder().onInventory( inventoryKey )
            .withCorrectiveTask( corrTaskKey ).build();
      HumanResourceKey lHr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setInventory( inventoryKey, faultKey.getEventKey(), false, lHr );

      // check Event inventory
      EvtInv lEvtInv = new EvtInv( faultKey.getEventKey(), inventoryKey );
      lEvtInv.assertExist();

      // check the stage note message
      EvtStage lStage = new EvtStage( faultKey.getEventKey() );
      lStage.assertCount( 0 );
   }


   @Test
   public void testSetFailureType() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();
      RefFailTypeKey failType = new RefFailTypeBuilder().withCode( "G" ).build();
      RefFailTypeKey failTypeNew = new RefFailTypeBuilder().withCode( "H" ).build();

      FaultKey faultKey =
            new SdFaultBuilder().onInventory( inventoryKey ).withFailureType( failType ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setFailureType( failTypeNew, hr );

      // check failure type
      SdFault lSdFault = new SdFault( faultKey );
      lSdFault.assertFailType( failTypeNew.getCd() );

      // check the stage note message
      EvtStage lStage = new EvtStage( faultKey.getEventKey() );
      lStage.assertHr( hr );
      lStage.assertStageNote( "The failure type changed from G to H." );
   }


   @Test
   public void testSetFailureTypeToBlank() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();
      RefFailTypeKey failType = new RefFailTypeBuilder().withCode( "G" ).build();

      FaultKey faultKey =
            new SdFaultBuilder().onInventory( inventoryKey ).withFailureType( failType ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setFailureType( new RefFailTypeKey( null ), hr );

      // check failure type
      SdFault lSdFault = new SdFault( faultKey );
      lSdFault.assertFailType( null );

      // check the stage note message
      EvtStage lStage = new EvtStage( faultKey.getEventKey() );
      lStage.assertHr( hr );
      lStage.assertStageNote( "The failure type of G was removed." );
   }


   @Test
   public void testSetFailureTypeFromBlank() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();
      RefFailTypeKey failTypeNew = new RefFailTypeBuilder().withCode( "H" ).build();

      FaultKey faultKey = new SdFaultBuilder().onInventory( inventoryKey ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setFailureType( failTypeNew, hr );

      // check failure type
      SdFault lSdFault = new SdFault( faultKey );
      lSdFault.assertFailType( failTypeNew.getCd() );

      // check the stage note message
      EvtStage lStage = new EvtStage( faultKey.getEventKey() );
      lStage.assertHr( hr );
      lStage.assertStageNote( "The failure type of H was added." );
   }


   @Test
   public void testSetFailureTypeNoChange() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();
      RefFailTypeKey failType = new RefFailTypeBuilder().withCode( "G" ).build();

      FaultKey faultKey =
            new SdFaultBuilder().onInventory( inventoryKey ).withFailureType( failType ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setFailureType( failType, hr );

      // check failure type
      SdFault lSdFault = new SdFault( faultKey );
      lSdFault.assertFailType( failType.getCd() );

      // assert that no stage note was added.
      EvtStage stage = new EvtStage( faultKey.getEventKey() );
      stage.assertCount( 0 );
   }


   @Test
   public void testSetFailureSeverity() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();
      FaultKey faultKey = new SdFaultBuilder().onInventory( inventoryKey )
            .withFailureSeverity( RefFailureSeverityKey.MINOR ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setFailureSeverity( RefFailureSeverityKey.UNKNOWN, hr );

      // check severity
      SdFault sdFault = new SdFault( faultKey );
      sdFault.assertFailSev( RefFailureSeverityKey.UNKNOWN.getCd() );

      // check the stage note message
      EvtStage lStage = new EvtStage( faultKey.getEventKey() );
      lStage.assertHr( hr );
      lStage.assertStageNote( "The fault severity changed from MINOR to UNKNOWN." );
   }


   @Test
   public void testSetFailureSeverityNoChange() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();
      FaultKey faultKey = new SdFaultBuilder().onInventory( inventoryKey )
            .withFailureSeverity( RefFailureSeverityKey.MINOR ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setFailureSeverity( RefFailureSeverityKey.MINOR, hr );

      // check severity
      SdFault sdFault = new SdFault( faultKey );
      sdFault.assertFailSev( RefFailureSeverityKey.MINOR.getCd() );

      // assert that no stage note was added.
      EvtStage lStage = new EvtStage( faultKey.getEventKey() );
      lStage.assertCount( 0 );
   }


   @Test
   public void testSetFaultSource() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();
      FaultKey faultKey = new SdFaultBuilder().onInventory( inventoryKey )
            .withFaultSource( RefFaultSourceKey.MECH ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setFaultSource( RefFaultSourceKey.PILOT, hr );

      // check fault source
      SdFault lSdFault = new SdFault( faultKey );
      lSdFault.assertFaultSource( RefFaultSourceKey.PILOT.getCd() );

      // check the stage note message
      EvtStage stage = new EvtStage( faultKey.getEventKey() );
      stage.assertHr( hr );
      stage.assertStageNote( "The fault source changed from MECH to PILOT." );
   }


   @Test
   public void testSetFaultSourceNoChange() throws Exception {
      // arrange
      InventoryKey inventoryKey = new InventoryBuilder().build();
      FaultKey faultKey = new SdFaultBuilder().onInventory( inventoryKey )
            .withFaultSource( RefFaultSourceKey.MECH ).build();
      HumanResourceKey hr = Domain.createHumanResource();

      // act
      new FaultService( faultKey ).setFaultSource( RefFaultSourceKey.MECH, hr );

      // check fault source
      SdFault sdFault = new SdFault( faultKey );
      sdFault.assertFaultSource( RefFaultSourceKey.MECH.getCd() );

      // assert that no stage note was added.
      EvtStage stage = new EvtStage( faultKey.getEventKey() );
      stage.assertCount( 0 );
   }


   @Test
   public void testComposeFaultDescription_HappyCase() throws MxException {
      String expectedDesc = "ExistingDesc\n\nNewDesc";
      String newDesc = "NewDesc";
      String existingDesc = "ExistingDesc";
      String actual = FaultService.composeFaultDescription( newDesc, existingDesc );

      assertEquals( "The fault description does not match", expectedDesc, actual );
   }


   @Test( expected = StringTooLongException.class )
   public void testComposeFaultDescription_DescriptionTooLong() throws MxException {
      String expectedDesc = "ExistingDesc\nNewDesc\n";
      String newDesc = "NewDesc";
      String existingDesc = StringUtils.leftPad( "hi", 3997, '*' );
      String actual = FaultService.composeFaultDescription( newDesc, existingDesc );

      assertEquals( "The fault description does not match", expectedDesc, actual );
   }


   @Test
   public void setDeferralClass_valid() throws Exception {

      final String deferralClass = "MEL A";

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralClass( deferralClass, RefFailureSeverityKey.MEL.getCd() );

      SdFaultTable sdFaultTable = jdbcSdFaultDao.findByPrimaryKey( faultKeyDefault );
      assertEquals( RefFailureSeverityKey.MEL.getCd(), sdFaultTable.getFailSev() );
      assertEquals( deferralClass, sdFaultTable.getFailDefer() );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void setDeferralClass_missing() throws Exception {
      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralClass( null, RefFailureSeverityKey.MEL.getCd() );
   }


   @Test( expected = LockedInventoryException.class )
   public void setDeferralClass_locked_inventory() throws Exception {
      final String deferralClass = "MEL A";

      setLockedInventoryData();

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralClass( deferralClass, RefFailureSeverityKey.MEL.getCd() );
   }


   @Test( expected = InvalidReftermException.class )
   public void setDeferralClass_invalid_code() throws Exception {
      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralClass( "INVALID", RefFailureSeverityKey.MEL.getCd() );
   }


   @Test
   public void setDeferralReference_valid() throws Exception {
      final String deferralReference = "DR-1234";

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralReference( deferralReference, RefFailureSeverityKey.MEL.getCd() );

      SdFaultTable lSdFaultTable = jdbcSdFaultDao.findByPrimaryKey( faultKeyDefault );
      assertEquals( RefFailureSeverityKey.MEL.getCd(), lSdFaultTable.getFailSev() );
      assertEquals( deferralReference, lSdFaultTable.getDeferRefSDesc() );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void setDeferralReference_missing() throws Exception {
      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralReference( null, RefFailureSeverityKey.MEL.getCd() );
   }


   @Test( expected = StringTooLongException.class )
   public void setDeferralReference_too_long() throws Exception {

      final int MAX_ALLOWED_LENGTH = 80;
      final String deferralReference =
            RandomStringUtils.randomAlphanumeric( MAX_ALLOWED_LENGTH + 1 );

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralReference( deferralReference, RefFailureSeverityKey.MEL.getCd() );
   }


   @Test( expected = LockedInventoryException.class )
   public void setDeferralReference_locked_inventory() throws Exception {

      final String deferralReference = "DR-1234";

      setLockedInventoryData();

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralReference( deferralReference, RefFailureSeverityKey.MEL.getCd() );
   }


   @Test
   public void setDeferralAuthorization_valid() throws Exception {

      final String deferralAuthorization = "AUT-11111";

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralAuthorization( deferralAuthorization,
            RefFailureSeverityKey.MEL.getCd() );

      SdFaultTable sdFaultTable = jdbcSdFaultDao.findByPrimaryKey( faultKeyDefault );
      assertEquals( RefFailureSeverityKey.MEL.getCd(), sdFaultTable.getFailSev() );
      assertEquals( deferralAuthorization, sdFaultTable.getDeferCdSDesc() );
   }


   @Test( expected = StringTooLongException.class )
   public void setDeferralAuthorization_too_long() throws Exception {

      final int MAX_ALLOWED_LENGTH = 80;
      final String deferralAuthorization =
            RandomStringUtils.randomAlphanumeric( MAX_ALLOWED_LENGTH + 1 );

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralAuthorization( deferralAuthorization,
            RefFailureSeverityKey.MEL.getCd() );
   }


   @Test( expected = LockedInventoryException.class )
   public void setDeferralAuthorization_locked_inventory() throws Exception {

      final String deferralAuthorization = "AUT-11111";

      setLockedInventoryData();

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralAuthorization( deferralAuthorization,
            RefFailureSeverityKey.MEL.getCd() );
   }


   /**
    * Lock the aircraft inventory for certain scenarios around data setup for fault
    */
   private void setLockedInventoryData() {
      inventoryKeyDefault = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setLocked( true );
         }

      } );

      faultKeyDefault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setInventory( inventoryKeyDefault );
            aFault.setSeverity( RefFailureSeverityKey.MEL );
         }
      } );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void setDeferralAuthorization_missing() throws Exception {

      GlobalParametersStub globalParametersStub =
            new GlobalParametersStub( ParmTypeEnum.LOGIC.name() );
      globalParametersStub.setBoolean( "FAULT_DEFERRAL_AUTH_MANDATORY", true );
      GlobalParameters.setInstance( globalParametersStub );

      faultService = new FaultService( faultKeyDefault );
      faultService.setDeferralAuthorization( null, RefFailureSeverityKey.MEL.getCd() );
   }


   @Test( expected = ResolutionConfigSlotMissingException.class )
   public void setResolutionConfigSlot_noConfigSlotExists() throws MxException {

      faultService = new FaultService( faultKeyDefault );
      UUID resolutionConfigSlotId = UUID.randomUUID();

      faultService.setResolutionConfigSlot( resolutionConfigSlotId, HumanResourceKey.ADMIN );
   }


   @Test
   public void setResolutionConfigSlot_null() throws MxException {

      faultService = new FaultService( faultKeyDefault );

      faultService.setResolutionConfigSlot( null, HumanResourceKey.ADMIN );

      ConfigSlotKey resolutionConfigSlot =
            jdbcSdFaultDao.findByPrimaryKey( faultKeyDefault ).getResolutionConfigSlot();
      assertNull( resolutionConfigSlot );
   }


   @Test( expected = ResolutionConfigSlotClassException.class )
   public void setResolutionConfigSlot_invalidBOMClass() throws MxException {

      faultService = new FaultService( faultKeyDefault );
      ConfigSlotKey resolutionConfigSlot = new ConfigSlotBuilder( "10-20-30" )
            .withClass( RefBOMClassKey.ROOT ).withName( "Test" ).build();

      UUID resolutionConfigSlotId =
            EqpAssmblBom.findByPrimaryKey( resolutionConfigSlot ).getAlternateKey();

      faultService.setResolutionConfigSlot( resolutionConfigSlotId, HumanResourceKey.ADMIN );
   }


   @Test
   public void setResolutionConfigSlot_existingValidSlot() throws MxException {

      faultService = new FaultService( faultKeyDefault );
      ConfigSlotKey resolutionConfigSlot = new ConfigSlotBuilder( "10-20-30" )
            .withClass( RefBOMClassKey.SYS ).withName( "Test" ).build();

      UUID resolutionConfigSlotId =
            EqpAssmblBom.findByPrimaryKey( resolutionConfigSlot ).getAlternateKey();

      faultService.setResolutionConfigSlot( resolutionConfigSlotId, HumanResourceKey.ADMIN );

      assertEquals( resolutionConfigSlot,
            jdbcSdFaultDao.findByPrimaryKey( faultKeyDefault ).getResolutionConfigSlot() );
   }


   /**
    *
    * <pre>
    * - Given an aircraft assembly
      - AND an aircraft based on that assembly
      - AND a fault against the aircraft
      - AND a corrective task for that fault
      - And the fault is assigned to a work package
      - AND the fault is associated with a deferral reference
      - AND MOC authorization required on that deferral reference
      - And the deferral request is APPROVED
      - When the fault is deferred
      - Then the fault does not have any current reference
    *
    * </pre>
    *
    */
   @Test
   public void itDefersFaultAndRemovesAnyCurrentReferenceAssociationWhenFaultLabourStopped()
         throws Exception {

      // Required by the service
      UserKey user = Domain.createUser( aUser -> aUser.setUserId( 9999999 ) );
      HumanResourceKey humanResource = Domain.createHumanResource( aHr -> aHr.setUser( user ) );
      SecurityIdentificationInterface securityIdentification =
            new SecurityIdentificationStub( humanResource );
      SecurityIdentificationUtils.setInstance( securityIdentification );

      AssemblyKey aircraftAssembly = Domain.createAircraftAssembly();

      InventoryKey aircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssembly );
      } );

      TaskKey correctiveTask =
            Domain.createCorrectiveTask( aCorrTask -> aCorrTask.addLabour( aLabour -> {
               aLabour.setTechnicianRole( tech -> tech.setStatus( ACTV ) );
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
         aFault.setSeverity( RefFailureSeverityKey.MINOR );
      } );

      Domain.createFaultReferenceRequest( request -> {
         request.setReferenceRequestByFaultsCurrentRequest( fault );
         request.setRequestStatus( RefReferenceRequestStatusKey.APPROVED.getCd() );
         request.setHumanResourceKey( humanResource );
      } );

      FaultService faultService = new FaultService( fault );
      faultService.deferFault( humanResource, null, null );

      FaultReferenceDao faultReferenceDao =
            InjectorContainer.get().getInstance( FaultReferenceDao.class );

      FaultReferenceKey currentFaultReference =
            faultReferenceDao.getCurrentReferenceByFaultId( fault );

      assertNull( "Expected no currently associated reference with the fault",
            currentFaultReference );
   }


   @Test
   public void whenCertifyAFaultThenInspectionTaskIsTerminated() throws Exception {

      // ARRANGE
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly();

      final InventoryKey aircraftKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssemblyKey );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final FailedSystemInfo failedSystemInfo = new FailedSystemInfo();
      failedSystemInfo.setFailedSystemKey( aircraftRootConfigSlotKey );
      failedSystemInfo.setFailedSystemAltId(
            EqpAssmblBom.findByPrimaryKey( aircraftRootConfigSlotKey ).getAlternateKey() );

      final TaskTaskKey reqDefnTaskKey = Domain.createRequirementDefinition( aReq -> {
         aReq.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReq.setTaskClass( RefTaskClassKey.INSP );
         aReq.againstConfigurationSlot( aircraftRootConfigSlotKey );

      } );
      final TaskDefnKey reqDefnKey = TaskTaskTable.findByPrimaryKey( reqDefnTaskKey ).getTaskDefn();
      final TaskDefnDao taskDefnDao = InjectorContainer.get().getInstance( TaskDefnDao.class );
      final UUID reqDefnId = taskDefnDao.findByPrimaryKey( reqDefnKey ).getAlternateKey();

      final List<String> recurringInspections = new ArrayList<>();
      recurringInspections.add( UuidUtils.toHexString( reqDefnId ) );

      Domain.createDeferralReference( aDefRef -> {
         aDefRef.setName( "DEFERRAL_REFERENCE" );
         aDefRef.setAssemblyKey( aircraftAssemblyKey );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRecurringInspections( recurringInspections );
      } );

      final TaskKey correctiveTaskKey = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( aircraftKey );
      } );

      final Date date = new Date();

      FaultKey faultKey = Domain.createFault( aFault -> {
         aFault.setInventory( aircraftKey );
         aFault.setFoundOnDate( date );
         aFault.setDeferralReference( "DEFERRAL_REFERENCE" );
         aFault.setCorrectiveTask( correctiveTaskKey );
         aFault.setSeverity( RefFailureSeverityKey.MEL );
         aFault.setStatus( RefEventStatusKey.ACTV );
      } );

      TaskKey taskKey = Domain.createRequirement( aTask -> {
         aTask.setInventory( aircraftKey );
         aTask.setDefinition( reqDefnTaskKey );
         aTask.setRelatedFault( faultKey );
         aTask.setStatus( RefEventStatusKey.ACTV );
      } );

      FaultService faultService = new FaultService( faultKey );

      // ACT
      faultService.certify( HumanResourceKey.ADMIN, date );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      TaskTerminatedEvent terminatedEvent = ( TaskTerminatedEvent ) eventBus.getEventMessages()
            .get( eventBus.getEventMessages().size() - 1 ).getPayload();
      TaskKey actualTaskKey = terminatedEvent.getTaskKey();
      assertEquals( taskKey, actualTaskKey );
   }


   @Test
   public void whenMarkAFaultAsNotFoundThenInspectionTaskIsTerminated() throws Exception {

      // ARRANGE
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly();

      final InventoryKey aircraftKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( aircraftAssemblyKey );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      final FailedSystemInfo failedSystemInfo = new FailedSystemInfo();
      failedSystemInfo.setFailedSystemKey( aircraftRootConfigSlotKey );
      failedSystemInfo.setFailedSystemAltId(
            EqpAssmblBom.findByPrimaryKey( aircraftRootConfigSlotKey ).getAlternateKey() );

      final TaskTaskKey reqDefnTaskKey = Domain.createRequirementDefinition( aReq -> {
         aReq.setStatus( RefTaskDefinitionStatusKey.ACTV );
         aReq.setTaskClass( RefTaskClassKey.INSP );
         aReq.againstConfigurationSlot( aircraftRootConfigSlotKey );

      } );
      final TaskDefnKey reqDefnKey = TaskTaskTable.findByPrimaryKey( reqDefnTaskKey ).getTaskDefn();
      final TaskDefnDao taskDefnDao = InjectorContainer.get().getInstance( TaskDefnDao.class );
      final UUID reqDefnId = taskDefnDao.findByPrimaryKey( reqDefnKey ).getAlternateKey();

      final List<String> recurringInspections = new ArrayList<>();
      recurringInspections.add( UuidUtils.toHexString( reqDefnId ) );

      Domain.createDeferralReference( aDefRef -> {
         aDefRef.setName( "DEFERRAL_REFERENCE" );
         aDefRef.setAssemblyKey( aircraftAssemblyKey );
         aDefRef.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         aDefRef.setRecurringInspections( recurringInspections );
      } );

      final TaskKey correctiveTaskKey = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setInventory( aircraftKey );
      } );

      final Date date = new Date();

      FaultKey faultKey = Domain.createFault( aFault -> {
         aFault.setInventory( aircraftKey );
         aFault.setFoundOnDate( date );
         aFault.setDeferralReference( "DEFERRAL_REFERENCE" );
         aFault.setCorrectiveTask( correctiveTaskKey );
         aFault.setSeverity( RefFailureSeverityKey.MEL );
         aFault.setStatus( RefEventStatusKey.ACTV );
      } );

      TaskKey taskKey = Domain.createRequirement( aTask -> {
         aTask.setInventory( aircraftKey );
         aTask.setDefinition( reqDefnTaskKey );
         aTask.setRelatedFault( faultKey );
         aTask.setStatus( RefEventStatusKey.ACTV );
      } );

      FaultService faultService = new FaultService( faultKey );

      // ACT
      faultService.markAsNoFaultFound( HumanResourceKey.ADMIN );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      TaskTerminatedEvent terminatedEvent = ( TaskTerminatedEvent ) eventBus.getEventMessages()
            .get( eventBus.getEventMessages().size() - 1 ).getPayload();
      TaskKey actualTaskKey = terminatedEvent.getTaskKey();
      assertEquals( taskKey, actualTaskKey );

   }


   @Before
   public void loadData() throws Exception {

      jdbcSdFaultDao = InjectorContainer.get().getInstance( SdFaultDao.class );
      faultKeyDefault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setSeverity( RefFailureSeverityKey.MEL );
         }
      } );
      eventBus = fakeGuiceDaoRule.select( RecordingEventBus.class ).get();

   }
}
