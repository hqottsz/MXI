package com.mxi.mx.core.ejb.fault;

import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createFlight;
import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.am.domain.Domain.readSystem;
import static com.mxi.mx.core.key.RefEventStatusKey.CFACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static com.mxi.mx.core.key.RefEventTypeKey.CF;
import static com.mxi.mx.core.key.RefEventTypeKey.TS;
import static com.mxi.mx.core.key.RefFailureSeverityKey.AOG;
import static com.mxi.mx.core.key.RefFaultLogTypeKey.CABIN;
import static com.mxi.mx.core.key.RefTaskClassKey.CORR;
import static com.mxi.mx.web.api.fault.RaiseFaultConstants.LOG_AND_LEAVE_OPEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Inject;
import com.mxi.am.api.resource.maintenance.exec.fault.impl.FaultResourceBean;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.DeferralReference.FailedSystemInfo;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.ejb.DAOLocalStub;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.ejb.security.SecurityIdentityStub;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.common.utils.FormatUtil;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefFaultSourceKey;
import com.mxi.mx.core.key.RefFlightStageKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefResultEventKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceDao;
import com.mxi.mx.core.services.fault.RaiseFaultDetailsTO;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.ref.RefFailTypeTable;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sd.JdbcSdFaultDao;
import com.mxi.mx.core.table.sd.SdFaultDao;
import com.mxi.mx.core.table.sd.SdFaultResult;
import com.mxi.mx.core.table.sd.SdFaultTable;
import com.mxi.mx.core.table.task.JdbcTaskDefnDao;
import com.mxi.mx.core.table.task.TaskDefnDao;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Integrated unit tests for {@link FaultBean}
 *
 */
public class FaultBeanTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Inject
   FaultResourceBean faultResourceBean;

   private static final String SYSTEM_1 = "SYSTEM_1";
   private static final String FAULT_NAME = "FAULT_NAME";
   private static final String FAULT_CODE = "FAULT_CODE";
   private static final RefFlightStageKey FLIGHT_STAGE = new RefFlightStageKey( 0, "PFL" );
   private static final boolean SDR_FAULT = true;
   private static final String FAULT_DESCRIPTION = "FAULT_DESCRIPTION";
   private static final String LOGBOOK_REFERENCE = "LOGBOOK_REFERENCE";
   private static final String FAIL_TYPE_CODE = "FAILTYPE";
   private static final String FAULT_SOURCE = RefFaultSourceKey.PILOT.getCd();
   private static final String EXTERNAL_KEY = "EXTERNAL_KEY";
   private static final boolean ETOPS = true;
   private static final String DEFERRAL_CLASS_CODE = "MEL A";
   private static final String CLEAR_REFERENCE_FROM_FAULT = "CURRENT REFERENCE CLEARED FROM FAULT";
   private static final String ERROR_MESSAGE_NO_CURRENT_REFERENCE =
         "The selected fault has no current reference, thus the current reference cannot be cleared.";

   // Database dates have a granularity of seconds.
   private static final Date FOUND_ON_DATE = DateUtils.floorSecond( new Date() );

   private static final String DEFERRAL_REFERENCE = "DEFERRAL_REFERENCE";

   private static final int RESULT_EVENT_DB_ID_1 = 0;
   private static final String RESULT_EVENT_CD_1 = "ABT";
   private static final int RESULT_EVENT_DB_ID_2 = 0;
   private static final String RESULT_EVENT_CD_2 = "ATB";

   private static SdFaultDao sdFaultDaoDao = new JdbcSdFaultDao();
   private static EvtEventDao evtEventDao = new JdbcEvtEventDao();
   private static SchedStaskDao schedStaskDao = new JdbcSchedStaskDao();
   private static TaskDefnDao taskDefnDao = new JdbcTaskDefnDao();

   private HumanResourceKey authorizingHr;
   private int userId;

   private FaultBean faultBean;
   private List<RefResultEventKey> faultResultEventListExpected;


   /**
    * Set up a FaultBean to test against. As well as, an authorizing HR that will be required by
    * most, if not all, tests.
    */
   @Before
   public void before() {
      authorizingHr = createHumanResource();
      faultBean = new FaultBean();
      faultBean.setSessionContext( new SessionContextFake() );
      EjbFactory.setSingleton( new EjbFactoryStub( new SecurityIdentityStub( "currentuser", 0 ),
            new DAOLocalStub() ) );
   }


   /**
    *
    * Verify that when a logbook fault is raised and left open against a committed work package and
    * only the mandatory information is provided, that the fault and its corrective task is
    * persisted.
    *
    * What distinguishes a "logbook" fault is the fact that it is raised against a work package (and
    * not a JIC or other task).
    *
    */
   @Test
   public void createOpenLogbookFaultAgainstCommittedWorkpackageWithOnlyMandatoryInfo()
         throws Exception {

      setupForCreateFaultTest();

      // Given a committed work package.
      TaskKey workpackage = createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
      } );

      // Given a failed system on an aircraft (to raised the fault against).
      InventoryKey aircraft = createAircraft( acft -> {
         acft.addSystem( SYSTEM_1 );
      } );
      InventoryKey failedSystem = readSystem( aircraft, SYSTEM_1 );

      // When a logbook fault is raised and left open against the work package
      // and only the mandatory information is provided.
      //
      // Mandatory information consists of:
      // - mode (being log and leave open)
      // - related work package (thus making the fault a "logbook" fault)
      // - failed system
      RaiseFaultDetailsTO faultDetailsTo = new RaiseFaultDetailsTO();
      faultDetailsTo.setMode( LOG_AND_LEAVE_OPEN );
      faultDetailsTo.setTask( workpackage );
      faultDetailsTo.setFailedSystem( failedSystem );

      FaultBeanData lFaultBeanData = faultBean.createFault( faultDetailsTo, authorizingHr );
      FaultKey logbookFault = lFaultBeanData.getFaultKey();

      teardownForCreateFaultTest();

      // Then the fault is persisted with that mandatory information.
      //
      // Ensure a row is created in:
      // - sd_fault table for the fault
      // - evt_event table for the fault
      // - sched_stask for the fault's corrective task
      // - evt_event table for the fault's corrective task
      assertTrue( "Fault not created.", logbookFault.isValid() );

      EvtEventTable evtEventRowOfFault = evtEventDao.findByPrimaryKey( logbookFault.getEventKey() );
      assertThat( "Unexpected event type for fault.", evtEventRowOfFault.getEventType(), is( CF ) );
      assertThat( "Unexpected event status for fault that is left open.",
            evtEventRowOfFault.getEventStatus(), is( CFACTV ) );

      SchedStaskTable schedStaskRow =
            schedStaskDao.findByFaultId( logbookFault.getDbId(), logbookFault.getId() );
      assertThat( "Unexpected task class of corrective task of fault.",
            schedStaskRow.getTaskClass(), is( CORR ) );
      assertThat( "Unexpected inventory of corrective task of fault.",
            schedStaskRow.getMainInventory(), is( failedSystem ) );

      EvtEventTable evtEventRowOfTask =
            evtEventDao.findByPrimaryKey( schedStaskRow.getPk().getEventKey() );
      assertThat( "Unexpected event type for corrective task.", evtEventRowOfTask.getEventType(),
            is( TS ) );
      assertThat( "Unexpected highest event of corrective task (expected the work package event).",
            evtEventRowOfTask.getHEvent(), is( workpackage.getEventKey() ) );
      assertThat(
            "Unexpected next highest event of corrective task (expected the work package event).",
            evtEventRowOfTask.getNhEvent(), is( workpackage.getEventKey() ) );
   }


   /**
    *
    * Verify that when a logbook fault is raised and left open against a committed work package and
    * basic information is provided, that the fault and its corrective task is persisted.
    *
    *
    * What distinguishes a "logbook" fault is the fact that it is raised against a work package (and
    * not a JIC or other task).
    *
    * "Basic information" is subjectively information that is commonly entered for a fault (in
    * addition to the mandatory info) but does not drive secondary behaviours beyond being
    * persisted.
    *
    */
   @Test
   public void createOpenLogbookFaultAgainstCommittedWorkpackageWithBasicInfo() throws Exception {

      setupForCreateFaultTest();

      // Set up a fault failure type for testing.
      RefFailTypeTable refFailTypeRow = RefFailTypeTable.create();
      refFailTypeRow.setCode( FAIL_TYPE_CODE );
      refFailTypeRow.insert();

      // Given a committed work package.
      TaskKey workpackage = createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
      } );

      // Given a failed system on an aircraft (to raised the fault against).
      InventoryKey aircraft = createAircraft( acft -> {
         acft.addSystem( SYSTEM_1 );
      } );
      InventoryKey failedSystem = readSystem( aircraft, SYSTEM_1 );

      // Given a flight on which the fault occurred.
      FlightLegId flight = createFlight();

      // When a logbook fault is raised and left open against the work package
      // and "basic" information is provided (in addition to the mandatory info).
      RaiseFaultDetailsTO faultDetailsTo = new RaiseFaultDetailsTO();
      // Mandatory info...
      faultDetailsTo.setMode( LOG_AND_LEAVE_OPEN );
      faultDetailsTo.setTask( workpackage );
      faultDetailsTo.setFailedSystem( failedSystem );
      // Basic info...
      faultDetailsTo.setFaultSeverity( AOG );
      faultDetailsTo.setLogbookType( CABIN );
      faultDetailsTo.setFaultCode( FAULT_CODE );
      faultDetailsTo.setFoundDuringFlight( flight );
      faultDetailsTo.setFlightStage( FLIGHT_STAGE );
      faultDetailsTo.setFlightSafetyImpact( SDR_FAULT );
      faultDetailsTo.setFailureType( FAIL_TYPE_CODE );
      faultDetailsTo.setFoundBy( authorizingHr );
      faultDetailsTo.setFaultSource( FAULT_SOURCE );

      faultDetailsTo.setFaultName( FAULT_NAME );
      faultDetailsTo.setExternalKey( EXTERNAL_KEY );
      faultDetailsTo.setFaultDescription( FAULT_DESCRIPTION );
      faultDetailsTo.setLogbookRef( LOGBOOK_REFERENCE );
      faultDetailsTo.setFoundOnDate( FOUND_ON_DATE );

      faultDetailsTo.setETOPS( ETOPS );

      FaultBeanData lFaultBeanData = faultBean.createFault( faultDetailsTo, authorizingHr );
      FaultKey logbookFault = lFaultBeanData.getFaultKey();

      teardownForCreateFaultTest();

      // Then the fault is persisted with that basic information.
      //
      // Ensure a row is created in:
      // - sd_fault table for the fault
      // - evt_event table for the fault
      // - sched_stask for the fault's corrective task
      SdFaultTable sdFaultRow = sdFaultDaoDao.findByPrimaryKey( logbookFault );
      assertThat( "Unexpected severity.", sdFaultRow.getFailSeverityKey(), is( AOG ) );
      assertThat( "Unexpected logbook type.", sdFaultRow.getLogbookType(), is( CABIN ) );
      assertThat( "Unexpected (FRM) code.", sdFaultRow.getFRMCode(), is( FAULT_CODE ) );
      assertThat( "Unexpected flight.", sdFaultRow.getFoundDuringFlight(), is( flight ) );
      assertThat( "Unexpected flight stage.", sdFaultRow.getFlightStage(),
            is( FLIGHT_STAGE.getCd() ) );
      assertThat( "Unexpected flight safty impact.", sdFaultRow.isSdrBool(), is( SDR_FAULT ) );
      assertThat( "Unexpected failure type.", sdFaultRow.getFailureType(), is( FAIL_TYPE_CODE ) );
      assertThat( "Unexpected found by.", sdFaultRow.getFoundByHr(), is( authorizingHr ) );
      assertThat( "Unexpected source.", sdFaultRow.getFaultSource(), is( FAULT_SOURCE ) );

      EvtEventTable evtEventRowOfFault = evtEventDao.findByPrimaryKey( logbookFault.getEventKey() );
      assertThat( "Unexpected name.", evtEventRowOfFault.getEventSdesc(), is( FAULT_NAME ) );
      assertThat( "Unexpected external key.", evtEventRowOfFault.getExtKeySDesc(),
            is( EXTERNAL_KEY ) );
      assertThat( "Unexpected description.", evtEventRowOfFault.getEventLdesc(),
            is( FAULT_DESCRIPTION ) );
      assertThat( "Unexpected logbook reference.", evtEventRowOfFault.getDocRefSdesc(),
            is( LOGBOOK_REFERENCE ) );
      assertThat( "Unexpected found on date.", evtEventRowOfFault.getActualStartDt(),
            is( FOUND_ON_DATE ) );

      SchedStaskTable schedStaskRow =
            schedStaskDao.findByFaultId( logbookFault.getDbId(), logbookFault.getId() );
      assertThat( "Unexpected is etops.", schedStaskRow.isETOPS(), is( ETOPS ) );
   }


   /**
    *
    * Verify that an Open Logbook Fault is persisted when raised against an aircraft.
    */

   @Test
   public void createOpenLogbookFaultAgainstAircraft() throws Exception {

      setupForCreateFaultTest();

      // Given an aircraft and a failed system on an aircraft (to raise the fault against).
      InventoryKey aircraft = createAircraft( acft -> {
         acft.addSystem( SYSTEM_1 );
      } );

      InventoryKey failedSystem = readSystem( aircraft, SYSTEM_1 );

      // When a open logbook fault is raised against the aircraft with mandatory fault details.
      RaiseFaultDetailsTO faultDetailsTo = new RaiseFaultDetailsTO();
      faultDetailsTo.setMode( "LOG_AND_LEAVE_OPEN" );
      faultDetailsTo.setFailedSystem( failedSystem );

      FaultBeanData lFaultBeanData = faultBean.createFault( faultDetailsTo, authorizingHr );
      FaultKey logbookFault = lFaultBeanData.getFaultKey();

      // Then the fault is created with mandatory information.
      // Ensure a row is created in:
      // - sd_fault table for the fault
      // - evt_event table for the fault
      // - sched_stask for the fault's corrective task
      // - evt_event table for the fault's corrective task
      assertTrue( "Fault not created.", logbookFault.isValid() );

      EvtEventTable evtEventRowOfFault = evtEventDao.findByPrimaryKey( logbookFault.getEventKey() );
      assertThat( "Unexpected event type for fault.", evtEventRowOfFault.getEventType(), is( CF ) );

      assertThat( "Unexpected event status for fault that is left open.",
            evtEventRowOfFault.getEventStatus(), is( CFACTV ) );

      SchedStaskTable schedStaskRow =
            schedStaskDao.findByFaultId( logbookFault.getDbId(), logbookFault.getId() );
      assertThat( "Unexpected task class of corrective task of fault.",
            schedStaskRow.getTaskClass(), is( CORR ) );
      assertThat( "Unexpected inventory of corrective task of fault.",
            schedStaskRow.getMainInventory(), is( failedSystem ) );

      EvtEventTable evtEventRowOfTask =
            evtEventDao.findByPrimaryKey( schedStaskRow.getPk().getEventKey() );
      assertThat( "Unexpected event type for corrective task.", evtEventRowOfTask.getEventType(),
            is( TS ) );
   }


   /**
    * This test is testing deferring a fault with deferral reference, the fault will be deferred.
    *
    * <pre>
    *    Given an active fault.
    *    And an active deferral reference on the fault.
    *    When defer the fault.
    *    Then the fault will be deferred.
    * </pre>
    */
   @Test
   public void deferFaultDuringJobStopWithDeferralReference() throws Exception {
      LocationKey location = Domain.createLocation();
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly();
      InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setLocation( location );
      } );
      CarrierKey carrierKey = Domain.createOperator( operator -> {
         operator.setIATACode( "AA" );
         operator.setIATACode( "BBB" );
         operator.setCarrierCode( "AA-BBB" );
      } );

      ConfigSlotKey aircraftRootConfigSlotKey =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      FailedSystemInfo failedSystemInfo = new FailedSystemInfo();
      failedSystemInfo.setFailedSystemKey( aircraftRootConfigSlotKey );
      failedSystemInfo.setFailedSystemAltId(
            EqpAssmblBom.findByPrimaryKey( aircraftRootConfigSlotKey ).getAlternateKey() );
      List<CarrierKey> operators = new ArrayList<>();
      operators.add( carrierKey );
      FailDeferRefKey deferralReferenceKey = Domain.createDeferralReference( deferralReference -> {
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
         deferralReference.setOperators( operators );
         deferralReference.setName( DEFERRAL_REFERENCE );
         deferralReference.setFailedSystemInfo( failedSystemInfo );
         deferralReference.setAssemblyKey( aircraftAssemblyKey );
         deferralReference.setStatus( "ACTV" );
         deferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
      } );
      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftKey );
      } );
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setCurrentDeferralReference( deferralReferenceKey );
         fault.setFailedSystem( aircraftRootConfigSlotKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
      } );
      FaultBean faultBean = new FaultBean();
      faultBean.deferDuringJobStop( faultKey, false );

      EventKey faultEventKey = sdFaultDaoDao.findByPrimaryKey( faultKey ).getEventKey();
      assertEquals( "The fault is not deferred properly.", RefEventStatusKey.CFDEFER,
            evtEventDao.findByPrimaryKey( faultEventKey ).getEventStatus() );

   }


   /**
    * This test is testing deferring a fault without a deferral reference, the fault won't be
    * deferred.
    *
    * <pre>
    *    Given an active fault.
    *    When defer the fault.
    *    Then the fault will not be deferred.
    * </pre>
    */
   @Test
   public void deferFaultDuringJobStopWithoutDeferralReference() throws Exception {
      LocationKey location = Domain.createLocation();
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly();
      InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setLocation( location );
      } );
      CarrierKey carrierKey = Domain.createOperator( operator -> {
         operator.setIATACode( "AA" );
         operator.setIATACode( "BBB" );
         operator.setCarrierCode( "AA-BBB" );
      } );

      ConfigSlotKey aircraftRootConfigSlotKey =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      FailedSystemInfo failedSystemInfo = new FailedSystemInfo();
      failedSystemInfo.setFailedSystemKey( aircraftRootConfigSlotKey );
      failedSystemInfo.setFailedSystemAltId(
            EqpAssmblBom.findByPrimaryKey( aircraftRootConfigSlotKey ).getAlternateKey() );
      List<CarrierKey> operators = new ArrayList<>();
      operators.add( carrierKey );
      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftKey );
      } );
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setFailedSystem( aircraftRootConfigSlotKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
      } );
      FaultBean faultBean = new FaultBean();
      faultBean.deferDuringJobStop( faultKey, false );

      EventKey faultEventKey = sdFaultDaoDao.findByPrimaryKey( faultKey ).getEventKey();
      assertEquals( "The fault is deferred when there is no deferral reference.",
            RefEventStatusKey.CFACTV,
            evtEventDao.findByPrimaryKey( faultEventKey ).getEventStatus() );

   }


   /**
    * This test is testing deferring a fault with follow up task definition on the deferral
    * reference, the follow up tasks will be initialized.
    *
    * <pre>
    *    Given an active fault.
    *    And an active deferral reference on the fault.
    *    And an follow up task on the deferral reference.
    *    When defer the fault.
    *    Then the fault will be deferred.
    *    And the follow up task is initialized.
    * </pre>
    */
   @Test
   public void deferFaultDuringJobStopWithFollowTasks() throws Exception {

      PartNoKey acftPart = Domain.createPart();
      LocationKey location = Domain.createLocation();
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( assembly -> {
         assembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( aPartGroup -> {
               aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               aPartGroup.addPart( acftPart );
            } );
         } );
      } );
      InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( acftPart );
         aircraft.setLocation( location );
      } );
      CarrierKey carrierKey = Domain.createOperator( operator -> {
         operator.setIATACode( "AA" );
         operator.setIATACode( "BBB" );
         operator.setCarrierCode( "AA-BBB" );
      } );

      ConfigSlotKey aircraftRootConfigSlotKey =
            Domain.readRootConfigurationSlot( aircraftAssemblyKey );
      FailedSystemInfo failedSystemInfo = new FailedSystemInfo();
      failedSystemInfo.setFailedSystemKey( aircraftRootConfigSlotKey );
      failedSystemInfo.setFailedSystemAltId(
            EqpAssmblBom.findByPrimaryKey( aircraftRootConfigSlotKey ).getAlternateKey() );
      List<CarrierKey> operators = new ArrayList<>();
      operators.add( carrierKey );
      TaskTaskKey taskTaskKey = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.setCode( "FOLLOW_UP_TASK" );
         requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
         requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         requirementDefinition.setOrganization( OrgKey.ADMIN );
      } );
      String recurringInspection = FormatUtil.formatUniqueIdRemoveHyphens( taskDefnDao
            .findByPrimaryKey( TaskTaskTable.findByPrimaryKey( taskTaskKey ).getTaskDefn() )
            .getAlternateKey().toString() );
      List<String> recurringInspections = new ArrayList<>();
      recurringInspections.add( recurringInspection );
      FailDeferRefKey deferralReferenceKey = Domain.createDeferralReference( deferralReference -> {
         deferralReference.setFaultDeferralKey( new RefFailDeferKey( 0, DEFERRAL_CLASS_CODE ) );
         deferralReference.setOperators( operators );
         deferralReference.setName( DEFERRAL_REFERENCE );
         deferralReference.setFailedSystemInfo( failedSystemInfo );
         deferralReference.setAssemblyKey( aircraftAssemblyKey );
         deferralReference.setStatus( "ACTV" );
         deferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         deferralReference.setRecurringInspections( recurringInspections );
      } );
      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftKey );
      } );
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setCurrentDeferralReference( deferralReferenceKey );
         fault.setFailedSystem( aircraftRootConfigSlotKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
      } );
      FaultBean faultBean = new FaultBean();
      faultBean.deferDuringJobStop( faultKey, false );

      EventKey faultEventKey = sdFaultDaoDao.findByPrimaryKey( faultKey ).getEventKey();

      DataSetArgument args = new DataSetArgument();
      args.add( "task_class_cd", RefTaskClassKey.REQ.getCd() );
      args.add( aircraftKey, "main_inv_no_db_id", "main_inv_no_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_stask", args );

      assertEquals( "The fault is not deferred properly.", RefEventStatusKey.CFDEFER,
            evtEventDao.findByPrimaryKey( faultEventKey ).getEventStatus() );

      assertEquals( "Incorrect number of follow up tasks are generated.", 1, qs.getRowCount() );

   }


   /**
    * Description: Call clear reference on fault with current reference
    *
    * <pre>
    * Given an aircraft assembly AND
    * Given an aircraft inventory based on that assembly AND
    * Given a fault on that aircraft inventory AND
    * Given a corrective task on that fault AND
    * Given a repair reference on that fault as a current reference
    * When a technician attempts to remove a current reference from the fault
    * Then the fault no longer has a current reference
    * </pre>
    */
   @Test
   public void itRemovesCurrentReferenceOnFault() throws Exception {

      /* Setup */
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly();

      InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( aTaskTask -> {
         aTaskTask.setTaskClass( RefTaskClassKey.REPREF );
         aTaskTask.setDamageRecordBool( false );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setBarcode( "ABC" );
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReference );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setFoundOnDate( completeDate );
      } );

      /* Execution */
      new FaultBean().clearCurrentFaultReference( authorizingHr, faultKey );

      /* Assertions */
      FaultReferenceDao jdbcFaultReferenceDao =
            InjectorContainer.get().getInstance( FaultReferenceDao.class );
      assertNull( CLEAR_REFERENCE_FROM_FAULT,
            jdbcFaultReferenceDao.getCurrentReferenceByFaultId( faultKey ) );

   }


   /**
    *
    * This method will test whether sd_fault revision date is updated once the Fault Result Event is
    * unassigns from the Fault
    *
    * @throws Exception
    *
    */
   @Test
   public void testFaultResultUnassignRevisionDateUpdate() throws Exception {

      setupForCreateFaultTest();

      constructExpectedFaultResultEventList();

      // Given an aircraft and a failed system on an aircraft (to raise the fault against).
      InventoryKey aircraft = createAircraft( acft -> {
         acft.addSystem( SYSTEM_1 );
      } );

      InventoryKey failedSystem = readSystem( aircraft, SYSTEM_1 );

      List<RefResultEventKey> refResultEventKeyList = new ArrayList<RefResultEventKey>();
      RefResultEventKey refResultEventKey1 =
            new RefResultEventKey( RESULT_EVENT_DB_ID_1, RESULT_EVENT_CD_1 );
      RefResultEventKey refResultEventKey2 =
            new RefResultEventKey( RESULT_EVENT_DB_ID_2, RESULT_EVENT_CD_2 );
      refResultEventKeyList.add( refResultEventKey1 );
      refResultEventKeyList.add( refResultEventKey2 );

      // When a open logbook fault is raised against the aircraft with fault details.
      RaiseFaultDetailsTO faultDetailsTo = new RaiseFaultDetailsTO();
      faultDetailsTo.setMode( "LOG_AND_LEAVE_OPEN" );
      faultDetailsTo.setFailedSystem( failedSystem );
      faultDetailsTo.setResultEvents( refResultEventKeyList );

      FaultBeanData lFaultBeanData = faultBean.createFault( faultDetailsTo, authorizingHr );
      FaultKey logbookFault = lFaultBeanData.getFaultKey();

      assertTrue( "Fault not created.", logbookFault.isValid() );

      // Record revision date before Fault Result Event removal
      Date lRevsionDateBeforeUpdate = getRevisionDateOfFault( logbookFault );

      // To ensure an update in the revision date
      Thread.sleep( 2000 );

      // Remove a Fault Result Event from the fault
      refResultEventKeyList.remove( refResultEventKey1 );
      faultBean.editResultEvents( logbookFault, null, refResultEventKeyList );

      // Get the Fault Result Events assigned to the fault
      List<RefResultEventKey> faultResultEventListActual =
            SdFaultResult.getResultEvents( logbookFault );

      // Record revision date after Fault Result Event removal
      Date lRevsionDateAfterUpdate = getRevisionDateOfFault( logbookFault );

      // Assert whether the revision date is updated once the Fault Result Event is unassigns
      Assert.assertTrue( "The revision date of Fault was not updated",
            lRevsionDateBeforeUpdate.before( lRevsionDateAfterUpdate ) );

      // Assert Fault Result Event Lists
      assertFaultResultEventLists( faultResultEventListActual );

   }


   /**
    * Set up for testing FaultBean.ejbCreateFault()
    *
    */
   private void setupForCreateFaultTest() {

      // FaultBean.ejbCreateFault() requires the user parameter
      // ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP to be set for the authorizing HR.
      //
      // Note: UserParametersFake sets the default value for all user parameters and this test only
      // needs ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP to have a value (doesn't matter what value).
      userId = OrgHr.findByPrimaryKey( authorizingHr ).getUserId();
      UserParameters.setInstance( userId, "LOGIC", new UserParametersFake( userId, "LOGIC" ) );
   }


   /**
    * Tear down for testing FaultBean.ejbCreateFault().
    *
    * Note; call prior to assertions to ensure this gets called.
    *
    */
   private void teardownForCreateFaultTest() {
      // Reset the user parameters.
      UserParameters.setInstance( userId, "LOGIC", null );
   }


   /**
    * Get the revision date of the task
    *
    * @param faultKey
    *           fault key
    */
   private Date getRevisionDateOfFault( FaultKey faultKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( faultKey.getPKWhereArg() );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sd_fault", lArgs );

      if ( !lQs.first() ) {
         fail( "Fault not found for key " + faultKey );
      }

      return lQs.getDate( "revision_dt" );
   }


   /**
    * Construct the Expected Fault Result Event List
    *
    * @param faultKey
    *           fault key
    */
   private void constructExpectedFaultResultEventList() {
      faultResultEventListExpected = new ArrayList<RefResultEventKey>();
      RefResultEventKey refResultEventKey1 =
            new RefResultEventKey( RESULT_EVENT_DB_ID_1, RESULT_EVENT_CD_1 );
      faultResultEventListExpected.add( refResultEventKey1 );
   }


   /**
    * Assert the Fault Result Event Lists
    *
    * @param faultResultEventListActual
    *           List of RefResultEventKey objects
    */
   private void assertFaultResultEventLists( List<RefResultEventKey> faultResultEventListActual ) {

      for ( RefResultEventKey refResultEventKey : faultResultEventListActual ) {
         Assert.assertTrue( "Fault Result Event Object not Found.",
               faultResultEventListExpected.contains( refResultEventKey ) );
      }

   }

}
