package com.mxi.mx.core.services.stask.complete.completeservice;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.EventMessage;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ibm.icu.impl.Assert;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.message.MxMessage;
import com.mxi.mx.common.security.SecurityIdentificationInterface;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefReschedFromKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.maintenance.exec.fault.app.FaultReferenceAppService;
import com.mxi.mx.core.maintenance.exec.fault.domain.Fault;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultRepository;
import com.mxi.mx.core.maintenance.exec.fault.infra.JdbcFaultRepository;
import com.mxi.mx.core.production.task.domain.TaskCompletedEvent;
import com.mxi.mx.core.production.task.domain.TaskTerminatedEvent;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.services.task.FollowOnTaskQueryService;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventRel;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.inv.InvDamageDao;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskDefnDao;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.utils.uuid.UuidConverter;
import com.mxi.mx.domain.inventory.damage.InventoryDamage;
import com.mxi.mx.repository.inventory.damage.JdbcInventoryDamageRepository;


/**
 * Tests for {@linkplain CompleteService#complete}
 *
 */

public class CompleteService_CompleteTest {

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException expectedException = ExpectedException.none();

   private static final String ASSEMBLY_CODE = "A320";
   private static final String ENGINE_PART_GROUP_CODE = "ENGPG";

   private static final BigDecimal THRESHOLD = new BigDecimal( 10 );
   public static final boolean RECURRING = true;
   public static final boolean NOT_RECURRING = false;
   public static final boolean USE_RESCHED_RULE = false;
   public static final boolean USE_SCHED_RULE = true;
   public static final String SYSTEM_NAME = "SYSTEM_NAME";
   private static final Date REQUIREMENT_EXECUTION_DATE = DateUtils.addDays( new Date(), -7 );
   private static final Date FOLLOW_ON_TASK_EFFECTIVE_DATE = DateUtils.addDays( new Date(), -5 );
   private static final String DATE_FORMAT = "yyyy-MM-dd";
   private static final Date AIRCRAFT_MANUFACTURED_DATE = DateUtils.addDays( new Date(), -15 );
   private static final BigDecimal CURRENT_USAGE = new BigDecimal( 50 );
   private static final BigDecimal MANUFACTURED_DATE_USAGE = new BigDecimal( 0 );
   private final String LOCATION_MDESC = "LOCATION";
   private static final String CONFIG_SLOT_ROOT_CODE = "ROOT_CODE";
   private static final String CONFIG_SLOT_SYS_CODE = "SYS_CODE";
   private static final String CONFIG_SLOT_TRK_CODE = "TRACKED_CODE";
   private static final String TRK_PKG_CODE = "TRK";
   private static final String TASK_COMPLETE_MESSAGE = "Task has been completed";
   private static final String ERROR_MESSAGE_NO_DAMAGE_RECORD =
         "The selected reference requires that a damage record is added before this fault can be completed.";
   private static final String ERROR_MESSAGE_NO_DAMAGED_COMPONENT =
         "The selected reference requires that a damaged component is selected before this fault can be completed.";
   private static final int USER_ID = 9999;

   private FaultReferenceAppService faultReferenceAppService;
   private FaultRepository faultRepository;
   private OrgKey orgKey;
   private UserKey user;
   private HumanResourceKey humanResource;

   private RecordingEventBus eventBus;


   @Before
   public void setUp() {

      UserParametersFake userParms = new UserParametersFake( USER_ID, ParmTypeEnum.LOGIC.name() );
      userParms.setString( "REPETITIVE_TASK_TO_MONITOR_FAULT", "ERROR" );
      UserParameters.setInstance( USER_ID, ParmTypeEnum.LOGIC.name(), userParms );

      orgKey = Domain.createOrganization( o -> o.setType( RefOrgTypeKey.DEPT ) );
      user = Domain.createUser( user -> user.setUserId( USER_ID ) );
      humanResource = Domain.createHumanResource( hr -> {
         hr.setUser( user );
         hr.setOrganization( orgKey );
      } );

      SecurityIdentificationInterface securityIdentification =
            new SecurityIdentificationStub( humanResource );
      SecurityIdentificationUtils.setInstance( securityIdentification );

      faultReferenceAppService =
            InjectorContainer.get().getInstance( FaultReferenceAppService.class );
      faultRepository = InjectorContainer.get().getInstance( JdbcFaultRepository.class );

      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();

   }


   /**
    * Tests that an Active Following task is created when completing a task that has a create link.
    *
    * <pre>
    *    Given - an aircraft associated to an aircraft assembly and part
    *            AND two requirement definitions that are defined against the root config-slot of the aircraft assembly,
    *            AND a the second requirement definition has a create link to the first requirement definition
    *            AND a task based on the second requirement definition assigned to the aircraft
    *    When - the task is completed
    *    Then - a new active task based on the first requirement definition is created against the aircraft
    * </pre>
    */
   @Test
   public void itCreatesFollowingTask() throws Exception {

      // Given
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey aircraft = createAircraftInventoryWithManufacturedDateAndCurrentUsage(
            aircraftPart, aircraftAssembly );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssembly );

      final TaskTaskKey followingTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
            } );

      final TaskTaskKey requirementDefinitionKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     followingTaskDefinition );
            } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraft );
         requirement.setDefinition( requirementDefinitionKey );
      } );

      // When
      new CompleteService( taskKey ).complete( Domain.createHumanResource(),
            REQUIREMENT_EXECUTION_DATE, null );

      // Then
      List<EvtEventRel> eventRelationships =
            EvtEventRel.findByRelationshipType( taskKey, RefRelationTypeKey.DEPT );
      assertThat( "Incorrect number of dependent tasks were created.", eventRelationships.size(),
            is( 1 ) );

      EvtEventDao evtEventDao = new JdbcEvtEventDao();
      EvtEventTable eventTable = evtEventDao
            .findByPrimaryKey( TaskKey.getTaskKey( eventRelationships.get( 0 ).getRelEvent() ) );
      assertThat( "Task not created from CRT link", eventTable.exists(), is( true ) );
      assertThat( "Active task not created from CRT link", eventTable.getEventStatus(),
            is( RefEventStatusKey.ACTV ) );

      SchedStaskDao schedStaskDao = new JdbcSchedStaskDao();
      SchedStaskTable schedStaskTable = schedStaskDao
            .findByPrimaryKey( TaskKey.getTaskKey( eventRelationships.get( 0 ).getRelEvent() ) );
      assertThat( "Active task not associated to correct inventory",
            schedStaskTable.getMainInventory(), is( aircraft ) );
   }


   /**
    * Tests that Follow-on task is created using the scheduling rule: Manufactured Date.
    *
    * <pre>
    *    Given - an aircraft associated to an aircraft assembly and part
    *            AND a Follow-on requirement definition that is defined against the root config-slot of the aircraft assembly,
    *                and is scheduled from the manufactured date and has a usage based scheduling rule
    *            AND a REQ Task Definition that is also defined against the root config-slot of the aircraft assembly,
    *                and creates a follow-on task based on the Follow-on Task Definition above upon completion
    *    When - the REQ task is completed
    *    Then - the created Follow-on Task must be have the same start quantity as it had on the manufactured date
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCreatesFollowOnTaskBasedOnManufacturedDateHoursUsageSchedulingRule()
         throws Exception {

      // Given
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey aircraft = createAircraftInventoryWithManufacturedDateAndCurrentUsage(
            aircraftPart, aircraftAssembly );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssembly );

      final TaskTaskKey followOnTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.addSchedulingRule( DataTypeKey.HOURS, THRESHOLD );
               requirementDefinition
                     .setSchedulingRuleUsedWhenCreatedFromAnotherTask( USE_SCHED_RULE );
               requirementDefinition.setScheduledFromManufacturedDate();
            } );

      final TaskTaskKey requirementDefinitionKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     followOnTaskDefinition );
            } );

      final TaskKey requirementKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraft );
         requirement.setDefinition( requirementDefinitionKey );
         requirement.setActualStartDate( DateUtils.addDays( AIRCRAFT_MANUFACTURED_DATE, 1 ) );
      } );

      // When
      new CompleteService( requirementKey ).complete( Domain.createHumanResource(),
            REQUIREMENT_EXECUTION_DATE, null );

      // Then
      List<EvtEventRel> eventRelationships =
            EvtEventRel.findByRelationshipType( requirementKey, RefRelationTypeKey.DEPT );
      assertEquals( "Incorrect number of dependent tasks were created.", eventRelationships.size(),
            1 );

      EvtSchedDeadTable eventScheduleDeadlines = EvtSchedDeadTable
            .findByPrimaryKey( eventRelationships.get( 0 ).getRelEvent(), DataTypeKey.HOURS );

      assertThat( "The scheduled start date was not using the correct scheduling code.",
            eventScheduleDeadlines.getScheduledFrom(), is( RefSchedFromKey.BIRTH ) );
      assertThat(
            "The scheduled start usage was not the same as the usage at the manufactured date.",
            new BigDecimal( eventScheduleDeadlines.getStartQt() ), is( MANUFACTURED_DATE_USAGE ) );

   }


   /**
    * Tests that Follow-on task is created using the rescheduling rule: Execute Date.
    *
    * <pre>
    *    Given - an aircraft associated to an aircraft assembly and part
    *            AND a Follow-on requirement definition that is defined against the root config-slot of the aircraft assembly,
    *                and is scheduled from the execute date and has a calendar based scheduling rule
    *            AND a REQ Task Definition that is also defined against the root config-slot of the aircraft assembly,
    *                and creates a follow-on task based on the Follow-on Task Definition above upon completion
    *    When - the REQ task is completed
    *    Then - the created Follow-on Task must be scheduled based on the execute date
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCreatesFollowOnTaskBasedOnExecuteDateCalendarDayReschedulingRule()
         throws Exception {

      // Given
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey aircraft = createAircraftInventoryWithManufacturedDateAndCurrentUsage(
            aircraftPart, aircraftAssembly );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssembly );

      final TaskTaskKey followOnTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.addSchedulingRule( DataTypeKey.CDY, THRESHOLD );
               requirementDefinition
                     .setSchedulingRuleUsedWhenCreatedFromAnotherTask( USE_RESCHED_RULE );
               requirementDefinition.setRescheduleFromOption( RefReschedFromKey.EXECUTE );

            } );

      final TaskTaskKey requirementDefinitionKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     followOnTaskDefinition );

            } );

      final TaskKey requirementKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraft );
         requirement.setDefinition( requirementDefinitionKey );
      } );

      // When
      new CompleteService( requirementKey ).complete( Domain.createHumanResource(),
            REQUIREMENT_EXECUTION_DATE, null );

      // Then
      List<EvtEventRel> eventRelationships =
            EvtEventRel.findByRelationshipType( requirementKey, RefRelationTypeKey.DEPT );

      assertEquals( "Incorrect number of dependent tasks were created.", eventRelationships.size(),
            1 );

      EvtSchedDeadTable eventScheduleDeadlines = EvtSchedDeadTable
            .findByPrimaryKey( eventRelationships.get( 0 ).getRelEvent(), DataTypeKey.CDY );

      assertThat( "The scheduled start date was not using the correct rescheduling code.",
            eventScheduleDeadlines.getScheduledFrom(), is( RefSchedFromKey.LASTEND ) );
      assertThat( "The scheduled start date was not the execution date.",
            DateUtils.toString( eventScheduleDeadlines.getStartDate(), DATE_FORMAT ),
            is( DateUtils.toString( REQUIREMENT_EXECUTION_DATE, DATE_FORMAT ) ) );

   }


   /**
    * Tests that Follow-on task is created using the scheduling rule: Effective Date.
    *
    * <pre>
    *    Given - an aircraft associated to an aircraft assembly and part
    *            AND a Follow-on requirement definition that is defined against the root config-slot of the aircraft assembly,
    *                and is scheduled from the effective date and has a calendar based scheduling rule
    *            AND a REQ Task Definition that is also defined against the root config-slot of the aircraft assembly,
    *                and creates a follow-on task based on the Follow-on Task Definition above upon completion
    *    When - the REQ task is completed
    *    Then - the created Follow-on Task must be scheduled based on the effective date
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCreatesFollowOnTaskBasedOnEffectiveDateCalendarDaySchedulingRule()
         throws Exception {

      // Given
      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey aircraft = createAircraftInventoryWithManufacturedDateAndCurrentUsage(
            aircraftPart, aircraftAssembly );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssembly );

      final TaskTaskKey followOnTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.addSchedulingRule( DataTypeKey.CDY, THRESHOLD );
               requirementDefinition
                     .setSchedulingRuleUsedWhenCreatedFromAnotherTask( USE_SCHED_RULE );
               requirementDefinition.setScheduledFromEffectiveDate( FOLLOW_ON_TASK_EFFECTIVE_DATE );
            } );

      final TaskTaskKey requirementDefinitionKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REQ );
               requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.CRT,
                     followOnTaskDefinition );
            } );

      final TaskKey requirementKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraft );
         requirement.setDefinition( requirementDefinitionKey );
      } );

      // When
      new CompleteService( requirementKey ).complete( Domain.createHumanResource(),
            REQUIREMENT_EXECUTION_DATE, null );

      // Then
      List<EvtEventRel> eventRelationships =
            EvtEventRel.findByRelationshipType( requirementKey, RefRelationTypeKey.DEPT );
      assertEquals( "Incorrect number of dependent tasks were created.", eventRelationships.size(),
            1 );

      EvtSchedDeadTable eventScheduleDeadlines = EvtSchedDeadTable
            .findByPrimaryKey( eventRelationships.get( 0 ).getRelEvent(), DataTypeKey.CDY );

      assertThat( "The scheduled start date was not using the correct scheduling code.",
            eventScheduleDeadlines.getScheduledFrom(), is( RefSchedFromKey.EFFECTIV ) );
      assertThat( "The scheduled start date was not the effective date.",
            DateUtils.toString( eventScheduleDeadlines.getStartDate(), DATE_FORMAT ),
            is( DateUtils.toString( FOLLOW_ON_TASK_EFFECTIVE_DATE, DATE_FORMAT ) ) );

   }


   /**
    * Description: It will set the IPN event date generated in correspondence to part transformation
    * to MOD requirement completion date.
    *
    * <pre>
    * Given two engine part numbers
    * Given an engine assembly and an engine based on the assembly and one of the part numbers
    * Given an active MOD requirement against the engine with a part transformation to another part number
    * When the REQ is completed
    * Then the part transformation event date is set to the MOD requirement completion date.
    * </pre>
    */
   @Test
   public void itSetsPartTransformationDateToRequirementCompletionDate() throws Exception {
      // Given
      final PartNoKey lowThrustRatingEnginePartNo = createEnginePartNo();
      final PartNoKey highThrustRatingEnginePartNo = createEnginePartNo();
      final AssemblyKey engineAssembly =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly builder ) {
                  builder.setCode( ASSEMBLY_CODE );
                  builder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot builder ) {
                        builder.setConfigurationSlotClass( RefBOMClassKey.ROOT );
                        builder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup builder ) {
                              builder.setCode( ENGINE_PART_GROUP_CODE );
                              builder.addPart( highThrustRatingEnginePartNo );
                              builder.addPart( lowThrustRatingEnginePartNo );

                           }
                        } );
                     }
                  } );

               }
            } );

      final ConfigSlotKey engineRootConfigSlot = Domain.readRootConfigurationSlot( engineAssembly );
      // Required by a validation in DefaultChangePartNoService
      final PartGroupKey enginePartGroup =
            Domain.readPartGroup( engineRootConfigSlot, ENGINE_PART_GROUP_CODE );

      // Required by DefaultChangePartNoService.setPartNoEvt method
      final OwnerKey owner = Domain.createOwner();

      final InventoryKey engine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine builder ) {
            builder.setPartNumber( lowThrustRatingEnginePartNo );
            builder.setPartGroup( enginePartGroup );
            builder.setOwner( owner );
            builder.setAssembly( engineAssembly );
         }
      } );

      final TaskTaskKey reqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.addPartTransformation( lowThrustRatingEnginePartNo,
                        highThrustRatingEnginePartNo );
                  builder.setTaskClass( RefTaskClassKey.MOD );
                  builder.againstConfigurationSlot( engineRootConfigSlot );
               }
            } );

      final TaskKey req = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement builder ) {
            builder.setInventory( engine );
            builder.setTaskClass( RefTaskClassKey.MOD );
            builder.setStatus( RefEventStatusKey.ACTV );
            builder.setDefinition( reqDefn );
         }
      } );

      Date reqCompletionDate = DateUtils.addDays( DateUtils.getCurrentDateWithZeroTime(), -10 );
      HumanResourceKey hrKey = Domain.createHumanResource();
      // When
      new CompleteService( req ).complete( hrKey, reqCompletionDate, null );
      // Then
      DataSetArgument args = new DataSetArgument();
      args.add( "event_type_cd", RefEventTypeKey.IPN.getCd() );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "evt_event", args );
      if ( !qs.next() )
         Assert.fail( "Part Transformation wasn't completed succesfully" );
      Date actualPartTransformationDate = qs.getDate( "event_dt" );
      assertEquals( "Part Transformation event date wasn't set to the requirement completion date",
            0, actualPartTransformationDate.compareTo( reqCompletionDate ) );

   }


   /**
    * Description: It will complete a fault with an active repair reference and no damage record
    *
    * <pre>
    * Given a fault with a repair reference
    * When complete task is called on the corrective task
    * Then the fault is completed
    * </pre>
    */
   @Test
   public void itCompletesFaultWithRepairReferenceWithNoRequiredDamageRecord() throws Exception {

      /* Setup */
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey ACFT = Domain.createAircraft( ACFTBuilder -> {
         ACFTBuilder.setAssembly( aircraftAssembly );
         ACFTBuilder.setPart( aircraftPart );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( taskTask -> {
         taskTask.setTaskClass( RefTaskClassKey.REPREF );
         taskTask.setDamageRecordBool( false );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( ACFT );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReference );
         fault.setCorrectiveTask( correctiveTask );
         fault.setFoundOnDate( completeDate );
         fault.setInventory( ACFT );
      } );

      HumanResourceKey hrKey = Domain.createHumanResource();

      /* Execution */
      CompleteService.completeNonRootTask( correctiveTask, hrKey, completeDate );

      /* Assertions */
      JdbcEvtEventDao jdbcEvtEventDao = new JdbcEvtEventDao();
      RefEventStatusKey taskCompletion =
            jdbcEvtEventDao.findByPrimaryKey( correctiveTask ).getEventStatus();

      assertEquals( TASK_COMPLETE_MESSAGE, taskCompletion, RefEventStatusKey.COMPLETE );
   }


   /**
    * Description: It will complete a fault with an active repair reference with a damage record
    * requirement and a damage record on the fault
    *
    * <pre>
    * Given a fault with a repair reference that has a damage record requirement and
    * Given the fault has a damage record
    * When complete task is called on the corrective task
    * Then the fault is completed
    * </pre>
    */
   @Test
   public void itCompletesFaultWithRepairReferenceWithRequiredDamageRecord() throws Exception {

      /* Setup */
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey ACFT = Domain.createAircraft( ACFTBuilder -> {
         ACFTBuilder.setAssembly( aircraftAssembly );
         ACFTBuilder.setPart( aircraftPart );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( taskTask -> {
         taskTask.setTaskClass( RefTaskClassKey.REPREF );
         taskTask.setDamageRecordBool( true );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( ACFT );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReference );
         fault.setCorrectiveTask( correctiveTask );
         fault.setFoundOnDate( completeDate );
         fault.setInventory( ACFT );
         fault.setCurrentRepairReference( repairReference );
      } );

      JdbcInventoryDamageRepository jdbcInventoryDamageRepository =
            new JdbcInventoryDamageRepository();
      InvDamageDao jdbcInvDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey inventoryDamageKey = jdbcInvDamageDao.generatePrimaryKey();
      UUID inventoryDamageAltId = jdbcInvDamageDao.generateAltId();

      InventoryDamage inventoryDamage = InventoryDamage.builder().key( inventoryDamageKey )
            .altId( inventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( ACFT ).faultKey( faultKey ).build();

      jdbcInventoryDamageRepository.create( inventoryDamage );

      HumanResourceKey hrKey = Domain.createHumanResource();

      /* Execution */
      CompleteService.completeNonRootTask( correctiveTask, hrKey, completeDate );

      /* Assertions */
      JdbcEvtEventDao jdbcEvtEventDao = new JdbcEvtEventDao();
      RefEventStatusKey taskCompletion =
            jdbcEvtEventDao.findByPrimaryKey( correctiveTask ).getEventStatus();

      assertEquals( TASK_COMPLETE_MESSAGE, taskCompletion, RefEventStatusKey.COMPLETE );
   }


   /**
    * Description: It will complete a fault with an active repair reference with a damaged component
    * requirement and a damage record on a component on the fault
    *
    * <pre>
    * Given a fault with a repair reference that has a damaged component requirement and
    * Given the fault has a damage record on a component
    * When complete task is called on the corrective task
    * Then the fault is completed
    * </pre>
    */
   @Test
   public void itCompletesFaultWithRepairReferenceWithRequiredDamageRecordOnComponent()
         throws Exception {

      /* Setup */
      final PartNoKey aircraftPart = Domain.createPart();
      final PartNoKey damagedPart = Domain.createPart();

      final AssemblyKey aircraftAssembly =
            createAircraftAssemblyWithTrackedConfigSlot( aircraftPart, damagedPart );
      final InventoryKey ACFT = Domain.createAircraft( ACFTBuilder -> {
         ACFTBuilder.setAssembly( aircraftAssembly );
         ACFTBuilder.setPart( aircraftPart );
      } );

      final InventoryKey damagedInventory = Domain.createTrackedInventory( trackedInventory -> {
         trackedInventory.setPartNumber( damagedPart );
         trackedInventory.setLocation( Domain.createLocation() );
         trackedInventory.setParent( ACFT );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( taskTask -> {
         taskTask.setTaskClass( RefTaskClassKey.REPREF );
         taskTask.setDamageRecordBool( true );
         taskTask.setDamagedComponentBool( true );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( ACFT );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReference );
         fault.setCorrectiveTask( correctiveTask );
         fault.setFoundOnDate( completeDate );
         fault.setInventory( ACFT );
         fault.setCurrentRepairReference( repairReference );
      } );

      JdbcInventoryDamageRepository jdbcInventoryDamageRepository =
            new JdbcInventoryDamageRepository();
      InvDamageDao jdbcInvDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey inventoryDamageKey = jdbcInvDamageDao.generatePrimaryKey();
      UUID inventoryDamageAltId = jdbcInvDamageDao.generateAltId();

      InventoryDamage inventoryDamage = InventoryDamage.builder().key( inventoryDamageKey )
            .altId( inventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( damagedInventory ).faultKey( faultKey ).build();

      jdbcInventoryDamageRepository.create( inventoryDamage );

      HumanResourceKey hrKey = Domain.createHumanResource();

      /* Execution */
      CompleteService.completeNonRootTask( correctiveTask, hrKey, completeDate );

      /* Assertions */
      JdbcEvtEventDao jdbcEvtEventDao = new JdbcEvtEventDao();
      RefEventStatusKey taskCompletion =
            jdbcEvtEventDao.findByPrimaryKey( correctiveTask ).getEventStatus();

      assertEquals( TASK_COMPLETE_MESSAGE, taskCompletion, RefEventStatusKey.COMPLETE );
   }


   /**
    * Description: It will complete a fault with a repair reference associated to the fault that has
    * MOC authorization required and MOC approval for said reference.
    *
    * <pre>
    * Given an aircraft assembly AND
    * Given an aircraft on that assembly AND
    * Given a fault on that aircraft AND
    * Given a corrective task for that fault AND
    * Given a current repair reference on that fault AND
    * Given MOC authorization required on that repair reference AND
    * Given the MOC approval on the repair reference request
    * When a technician attempts to complete the corrective task
    * Then the task is completed
    * </pre>
    */
   @Test
   public void itCompletesFaultWithRepairReferenceWithMOCAuthRequired() throws Exception {

      // ARRANGE
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey ACFT = Domain.createAircraft( ACFTBuilder -> {
         ACFTBuilder.setAssembly( aircraftAssembly );
         ACFTBuilder.setPart( aircraftPart );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( taskTask -> {
         taskTask.setTaskClass( RefTaskClassKey.REPREF );
         taskTask.setMocApprovalBool( true );
         taskTask.setDamageRecordBool( false );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( ACFT );
      } );

      // note: for repair references to work, the corrective task must be added to the task, not the
      // other way around
      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReference );
         fault.setCorrectiveTask( correctiveTask );
         fault.setFoundOnDate( completeDate );
         fault.setInventory( ACFT );
      } );

      // approve the request created by the repair reference
      Date dateRequested = Domain.readFaultReferenceRequest( faultKey ).getDateRequested();
      FaultReferenceAppService faultReferenceAppService =
            InjectorContainer.get().getInstance( FaultReferenceAppService.class );
      faultReferenceAppService.approveReferenceRequest( faultKey, dateRequested );

      HumanResourceKey hrKey = Domain.createHumanResource();

      // ACT

      CompleteService.completeNonRootTask( correctiveTask, hrKey, completeDate );

      // ASSERT
      EvtEventDao jdbcEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      RefEventStatusKey taskCompletion =
            jdbcEvtEventDao.findByPrimaryKey( correctiveTask ).getEventStatus();

      assertEquals( TASK_COMPLETE_MESSAGE, RefEventStatusKey.COMPLETE, taskCompletion );
   }


   /**
    * Description: It will NOT complete a fault with an active repair reference with a damage record
    * requirement and no damage record on the fault
    *
    * <pre>
    * Given a fault with a repair reference that has a damage record requirement and
    * Given the fault has no damage record
    * When complete task is called on the corrective task
    * Then a FaultMissingDamageRecordException is thrown
    * </pre>
    */
   @Test
   public void itDoesNotCompleteFaultWithRepairReferenceWithRequiredDamageRecord()
         throws Exception {

      /* Setup */
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey ACFT = Domain.createAircraft( ACFTBuilder -> {
         ACFTBuilder.setAssembly( aircraftAssembly );
         ACFTBuilder.setPart( aircraftPart );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( taskTask -> {
         taskTask.setTaskClass( RefTaskClassKey.REPREF );
         taskTask.setDamageRecordBool( true );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( ACFT );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReference );
         fault.setCorrectiveTask( correctiveTask );
         fault.setFoundOnDate( completeDate );
         fault.setCurrentRepairReference( repairReference );
      } );

      HumanResourceKey hrKey = Domain.createHumanResource();

      /* Execution */

      expectedException.expect( ValidationException.class );
      expectedException.expectMessage( ERROR_MESSAGE_NO_DAMAGE_RECORD );
      CompleteService.completeNonRootTask( correctiveTask, hrKey, completeDate );
   }


   /**
    * Description: It will NOT complete a fault with an active repair reference with a damaged
    * component requirement and a damage record on the ACFT on the fault
    *
    * <pre>
    * Given a fault with a repair reference that has a damaged component requirement and
    * Given the fault has a damage record on the ACFT
    * When complete task is called on the corrective task
    * Then a FaultMissingDamageRecordException is thrown
    * </pre>
    */
   @Test
   public void itDoesNotCompleteFaultWithRepairReferenceWithRequiredDamageRecordOnComponent()
         throws Exception {

      /* Setup */
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey ACFT = Domain.createAircraft( ACFTBuilder -> {
         ACFTBuilder.setAssembly( aircraftAssembly );
         ACFTBuilder.setPart( aircraftPart );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( taskTask -> {
         taskTask.setTaskClass( RefTaskClassKey.REPREF );
         taskTask.setDamageRecordBool( true );
         taskTask.setDamagedComponentBool( true );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( ACFT );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReference );
         fault.setCorrectiveTask( correctiveTask );
         fault.setFoundOnDate( completeDate );
         fault.setCurrentRepairReference( repairReference );
      } );

      JdbcInventoryDamageRepository jdbcInventoryDamageRepository =
            new JdbcInventoryDamageRepository();
      InvDamageDao jdbcInvDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
      InventoryDamageKey inventoryDamageKey = jdbcInvDamageDao.generatePrimaryKey();
      UUID inventoryDamageAltId = jdbcInvDamageDao.generateAltId();

      InventoryDamage inventoryDamage = InventoryDamage.builder().key( inventoryDamageKey )
            .altId( inventoryDamageAltId ).locationDescription( LOCATION_MDESC )
            .inventoryKey( ACFT ).faultKey( faultKey ).build();

      jdbcInventoryDamageRepository.create( inventoryDamage );

      HumanResourceKey hrKey = Domain.createHumanResource();

      /* Execution */

      expectedException.expect( ValidationException.class );
      expectedException.expectMessage( ERROR_MESSAGE_NO_DAMAGED_COMPONENT );
      CompleteService.completeNonRootTask( correctiveTask, hrKey, completeDate );
   }


   /**
    * Description: It will NOT complete a fault with a repair reference associated to the fault that
    * has MOC authorization required and NO MOC approval for said reference.
    *
    * <pre>
    * Given an aircraft assembly AND
    * Given an aircraft on that assembly AND
    * Given a fault on that aircraft AND
    * Given a corrective task for that fault AND
    * Given a current repair reference on that fault AND
    * Given MOC authorization required on that repair reference AND
    * Given the NO MOC approval on the repair reference request
    * When a technician attempts to complete the corrective task
    * Then a ValidationException is thrown
    * </pre>
    */
   @Test
   public void itDoesNotCompleteFaultWithRepairReferenceWithMOCAuthRequired() throws Exception {

      /* Setup */
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey ACFT = Domain.createAircraft( ACFTBuilder -> {
         ACFTBuilder.setAssembly( aircraftAssembly );
         ACFTBuilder.setPart( aircraftPart );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( taskTask -> {
         taskTask.setTaskClass( RefTaskClassKey.REPREF );
         taskTask.setMocApprovalBool( true );
         taskTask.setDamageRecordBool( false );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( ACFT );
      } );

      Domain.createFault( fault -> {
         fault.setCurrentRepairReference( repairReference );
         fault.setCorrectiveTask( correctiveTask );
         fault.setFoundOnDate( completeDate );
      } );

      HumanResourceKey hrKey = Domain.createHumanResource();

      /* Execution */

      expectedException.expect( ValidationException.class );
      expectedException
            .expectMessage( StringContains.containsString( i18n.get( "core.err.33910" ) ) );

      CompleteService.completeNonRootTask( correctiveTask, hrKey, completeDate );
   }


   /**
    * Description: It will NOT complete a fault with a deferral reference associated to the fault.
    *
    * <pre>
    * Given an aircraft assembly AND
    * Given an aircraft on that assembly AND
    * Given a fault on that aircraft AND
    * Given a corrective task for that fault AND
    * Given a current deferral reference on that fault AND
    * When a technician attempts to complete the corrective task
    * Then a ValidationException is thrown
    * </pre>
    */
   @Test
   public void itDoesNotCompleteFaultWithDeferralReference() throws Exception {

      /* Setup */
      final PartNoKey aircraftPart = Domain.createPart();

      final AssemblyKey aircraftAssembly = createAircraftAssembly( aircraftPart );
      final InventoryKey ACFT = Domain.createAircraft( ACFTBuilder -> {
         ACFTBuilder.setAssembly( aircraftAssembly );
         ACFTBuilder.setPart( aircraftPart );
      } );

      FailDeferRefKey deferralReference = Domain.createDeferralReference();

      Date completeDate = new Date();

      TaskKey correctiveTask = Domain.createCorrectiveTask();

      Domain.createFault( fault -> {
         fault.setCurrentDeferralReference( deferralReference );
         fault.setCorrectiveTask( correctiveTask );
         fault.setFoundOnDate( completeDate );
         fault.setInventory( ACFT );
      } );

      HumanResourceKey hrKey = Domain.createHumanResource();

      /* Execution */
      expectedException.expect( ValidationException.class );
      expectedException
            .expectMessage( StringContains.containsString( i18n.get( "core.err.33914" ) ) );
      CompleteService.completeNonRootTask( correctiveTask, hrKey, completeDate );
   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft based on that assembly
    * And a requirement definition against the aircraft's root
    * And a task based on this requirement definition
    * And another requirement definition against the the same aircraft's root having a COMPLETE
    * dependency on the previous requirement definition
    * And a task based on this requirement definition
    * When the task based on non-dependent requirement definition is completed
    * Then the task based on the dependent following requirement definition gets completed at the same time.
    * </pre>
    *
    */
   @Test
   public void itCompletesFollowingTaskOnSameDateAsDependentCompletedTask() throws Exception {

      // Required by the service layer
      int userId = OrgHr.findByPrimaryKey( HumanResourceKey.ADMIN ).getUserId();
      UserParametersStub userParametersStub = new UserParametersStub( userId, "LOGIC" );
      userParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( userId, "LOGIC", userParametersStub );

      AssemblyKey assembly = Domain.createAircraftAssembly();
      ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( assembly );
      InventoryKey aircraft = Domain.createAircraft( acft -> acft.setAssembly( assembly ) );
      TaskTaskKey followingReqDefn = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.againstConfigurationSlot( rootConfigSlot );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );
      TaskKey followingTask = Domain.createRequirement( req -> {
         req.setDefinition( followingReqDefn );
         req.setInventory( aircraft );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      final TaskTaskKey reqDefn = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.againstConfigurationSlot( rootConfigSlot );
         requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.COMPLETE,
               followingReqDefn );
      } );
      TaskKey task = Domain.createRequirement( req -> {
         req.setDefinition( reqDefn );
         req.setInventory( aircraft );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      Date taskCompletionDate = DateUtils.floorSecond( REQUIREMENT_EXECUTION_DATE );
      // When
      new CompleteService( task ).complete( Domain.createHumanResource(), taskCompletionDate,
            null );

      // Then
      EvtEventDao dao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable eventRow = dao.findByPrimaryKey( followingTask );
      assertEquals( "The following task is expected to be completed.", RefEventStatusKey.COMPLETE,
            eventRow.getEventStatus() );
      assertEquals(
            "The following task's completion date should match the driving task's completion date",
            taskCompletionDate.toString(), eventRow.getEventDate().toString() );

   }


   /*
    * Validates that follow-on tasks created for a deferred fault are cancelled when the fault is
    * finished.
    */
   @Test
   public void itCompletesFaultWithDeferralFollowTasks() throws Exception {

      // Given

      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = createAircraftAssembly( aircraftPart );
      final InventoryKey aircraftInventoryKey =
            createAircraftInventoryWithManufacturedDateAndCurrentUsage( aircraftPart,
                  aircraftAssemblyKey );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssemblyKey );

      final TaskTaskKey followOnTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.addSchedulingRule( DataTypeKey.CDY, THRESHOLD );
               requirementDefinition
                     .setSchedulingRuleUsedWhenCreatedFromAnotherTask( USE_RESCHED_RULE );
               requirementDefinition.setRescheduleFromOption( RefReschedFromKey.EXECUTE );
               requirementDefinition.setOrganization( orgKey );
            } );

      TaskDefnKey taskDefnKey =
            TaskTaskTable.findByPrimaryKey( followOnTaskDefinition ).getTaskDefn();
      UUID taskDefAltId = InjectorContainer.get().getInstance( TaskDefnDao.class )
            .findByPrimaryKey( taskDefnKey ).getAlternateKey();

      FailDeferRefKey failDeferRefKey = Domain.createDeferralReference( deferral -> {
         deferral.getRecurringInspections()
               .add( new UuidConverter().convertUuidToString( taskDefAltId ) );
         deferral.getOperators().add( new CarrierKey( "1:1" ) );
         deferral.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         deferral.setFaultDeferralKey( new RefFailDeferKey( 0, "MEL A" ) );
         deferral.setAssemblyKey( aircraftAssemblyKey );
         deferral.setName( "defer" );
         deferral.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferral.setStatus( "ACTV" );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentDeferralReference( failDeferRefKey );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setFoundOnDate( completeDate );
         fault.setInventory( aircraftInventoryKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
      } );

      Fault fault = faultRepository.get( faultKey ).get();
      // defer fault to generate the follow-on tasks
      faultReferenceAppService.deferFault( fault );

      // When
      CompleteService.completeNonRootTask( correctiveTaskKey, humanResource, completeDate );

      FollowOnTaskQueryService followOnTaskQueryService =
            new FollowOnTaskQueryService( correctiveTaskKey );
      // Then

      // once the fault is completed all the follow-on tasks should be cancelled.
      followOnTaskQueryService.getFollowOnTasks().getRows()
            .forEach( follow -> assertEquals(
                  "Follow-on task should be cancelled after completing fault", "CANCEL",
                  follow.getString( "FOLLOW_ON_TASK_STATUS" ) ) );
   }


   /**
    * <pre>
    * Given an aircraft assembly
    * And an aircraft based on that assembly with current usage
    * And a requirement definition against the aircraft's root
    * And a task based on this requirement definition
    * And another requirement definition against the the same aircraft's root having a COMPLETE
    * dependency on the previous requirement definition
    * And a task based on this requirement definition
    * When the task based on non-dependent requirement definition is completed
    * Then the task based on the dependent following requirement definition gets completed
    * with the same usage as the first task.
    * </pre>
    *
    */
   @Test
   public void itCompletesFollowingTaskWithSameUsageAsDependentCompletedTask() throws Exception {

      // Required by the service layer
      int userId = OrgHr.findByPrimaryKey( HumanResourceKey.ADMIN ).getUserId();
      UserParametersStub userParametersStub = new UserParametersStub( userId, "LOGIC" );
      userParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( userId, "LOGIC", userParametersStub );

      AssemblyKey assembly = Domain.createAircraftAssembly();
      ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( assembly );
      InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setAssembly( assembly );
         acft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );
      TaskTaskKey followingReqDefn = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.againstConfigurationSlot( rootConfigSlot );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );
      TaskKey followingTask = Domain.createRequirement( req -> {
         req.setDefinition( followingReqDefn );
         req.setInventory( aircraft );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      final TaskTaskKey reqDefn = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.againstConfigurationSlot( rootConfigSlot );
         requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.COMPLETE,
               followingReqDefn );
      } );
      TaskKey task = Domain.createRequirement( req -> {
         req.setDefinition( reqDefn );
         req.setInventory( aircraft );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      Date taskCompletionDate = DateUtils.floorSecond( REQUIREMENT_EXECUTION_DATE );

      // When
      new CompleteService( task ).complete( Domain.createHumanResource(), taskCompletionDate,
            null );

      // Then
      EventInventoryUsageKey eventInventoryUsageKey = new EventInventoryUsageKey(
            new EventInventoryKey( task.getEventKey(), 1 ), DataTypeKey.HOURS );
      EvtInvUsageTable evtInvUsageTableRow =
            EvtInvUsageTable.findByPrimaryKey( eventInventoryUsageKey );
      Double taskUsageAtCompletion = evtInvUsageTableRow.getTsnQt();
      EventInventoryUsageKey followingTaskInventoryUsageKey = new EventInventoryUsageKey(
            new EventInventoryKey( followingTask.getEventKey(), 1 ), DataTypeKey.HOURS );
      EvtInvUsageTable followingTaskInvUsageTableRow =
            EvtInvUsageTable.findByPrimaryKey( followingTaskInventoryUsageKey );
      Double followingTaskUsageAtCompletion = followingTaskInvUsageTableRow.getTsnQt();
      assertEquals( "Expected the tasks to have same usage", taskUsageAtCompletion,
            followingTaskUsageAtCompletion );

   }


   /*
    * Validates that error messages are returned when a user tries to complete a fault with active
    * adhoc recurring tasks, if REPETITIVE_TASK_TO_MONITOR_FAULT parameter is set to ERROR
    */
   @Test
   public void itCheckWarningsForFaultWithRecurringAdhocTasks() throws Exception {

      // Given

      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = createAircraftAssembly( aircraftPart );
      final InventoryKey aircraftInventoryKey =
            createAircraftInventoryWithManufacturedDateAndCurrentUsage( aircraftPart,
                  aircraftAssemblyKey );

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setFoundOnDate( new Date() );
         fault.setInventory( aircraftInventoryKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
      } );

      Domain.createRepetitiveTask( task -> {
         task.setRelatedFault( faultKey );
         task.setRepeatInterval( new BigDecimal( 2 ) );
         task.setStartDate( new Date() );
         task.setStatus( RefEventStatusKey.ACTV );
         task.setFaultRelationType( RefRelationTypeKey.RECSRC );
      } );

      // When
      List<MxMessage> messages = CompleteService.checkForWarnings( null,
            new TaskKey[] { correctiveTaskKey }, humanResource );
      assertNotNull( messages );
      assertTrue( messages.size() > 1 );
      // in this case because REPETITIVE_TASK_TO_MONITOR_FAULT parameter is set to ERROR, we should
      // get a error message
      // indicating that the fault cannot be cancelled because there are active recurring adhoc
      // inspections
      messages.stream().filter( msg -> msg != null )
            .forEach( msg -> assertEquals( "core.msg.REPETITIVE_TASK_TO_MONITOR_FAULT_ERR_message",
                  msg.getMsgFrameKey() ) );

   }


   /**
    * Validates that no error messages are returned when a user tries to complete a fault with
    * active follow-on tasks even if REPETITIVE_TASK_TO_MONITOR_FAULT parameter is set to ERROR.
    * REPETITIVE_TASK_TO_MONITOR_FAULT should only affect ADHOC tasks.
    */
   @Test
   public void itCheckWarningsForFaultWithFollowTasks() throws Exception {

      // Given

      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = createAircraftAssembly( aircraftPart );
      final InventoryKey aircraftInventoryKey =
            createAircraftInventoryWithManufacturedDateAndCurrentUsage( aircraftPart,
                  aircraftAssemblyKey );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( aircraftAssemblyKey );

      final TaskTaskKey followOnTaskDefinition =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( rootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.addSchedulingRule( DataTypeKey.CDY, THRESHOLD );
               requirementDefinition
                     .setSchedulingRuleUsedWhenCreatedFromAnotherTask( USE_RESCHED_RULE );
               requirementDefinition.setRescheduleFromOption( RefReschedFromKey.EXECUTE );
               requirementDefinition.setOrganization( orgKey );
            } );

      TaskDefnKey taskDefnKey =
            TaskTaskTable.findByPrimaryKey( followOnTaskDefinition ).getTaskDefn();
      UUID taskDefAltId = InjectorContainer.get().getInstance( TaskDefnDao.class )
            .findByPrimaryKey( taskDefnKey ).getAlternateKey();

      FailDeferRefKey failDeferRefKey = Domain.createDeferralReference( deferral -> {
         deferral.getRecurringInspections()
               .add( new UuidConverter().convertUuidToString( taskDefAltId ) );
         deferral.getOperators().add( new CarrierKey( "1:1" ) );
         deferral.setFaultSeverityKey( RefFailureSeverityKey.MEL );
         deferral.setFaultDeferralKey( new RefFailDeferKey( 0, "MEL A" ) );
         deferral.setAssemblyKey( aircraftAssemblyKey );
         deferral.setName( "defer" );
         deferral.getFailedSystemInfo().setFailedSystemAltId( UUID.randomUUID() );
         deferral.setStatus( "ACTV" );
      } );

      Date completeDate = new Date();

      TaskKey correctiveTaskKey = Domain.createCorrectiveTask( correctiveTask -> {
         correctiveTask.setInventory( aircraftInventoryKey );
      } );

      FaultKey faultKey = Domain.createFault( fault -> {
         fault.setCurrentDeferralReference( failDeferRefKey );
         fault.setCorrectiveTask( correctiveTaskKey );
         fault.setFoundOnDate( completeDate );
         fault.setInventory( aircraftInventoryKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
      } );

      Fault fault = faultRepository.get( faultKey ).get();
      // defer fault to generate the follow-on tasks
      faultReferenceAppService.deferFault( fault );

      // When
      List<MxMessage> messages = CompleteService.checkForWarnings( null,
            new TaskKey[] { correctiveTaskKey }, humanResource );
      // Then
      // messages should not be returned for follow tasks
      assertNotNull( messages );

   }


   @Test
   public void
         whenCompleteATaskWithAutoTerminateFollowOnTaskThenTaskCompletedEventAndTaskTerminatedEventShouldBePublished()
               throws Exception {

      // ARRANGE
      final AssemblyKey assembly = Domain.createAircraftAssembly();
      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( assembly );
      final InventoryKey aircraft = Domain.createAircraft( acft -> {
         acft.setAssembly( assembly );
         acft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      // setup two following task definitions
      final TaskTaskKey followingReqDefn = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.againstConfigurationSlot( rootConfigSlot );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );
      final TaskTaskKey followingReqDefn2 = Domain.createRequirementDefinition( reqDefn -> {
         reqDefn.againstConfigurationSlot( rootConfigSlot );
         reqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      // create actual tasks from the task definition above.
      final TaskKey followingTask = Domain.createRequirement( req -> {
         req.setDefinition( followingReqDefn );
         req.setInventory( aircraft );
         req.setStatus( RefEventStatusKey.ACTV );
      } );
      final TaskKey followingTask2 = Domain.createRequirement( req -> {
         req.setDefinition( followingReqDefn );
         req.setInventory( aircraft );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      // create a task definition with "terminated on completion" following tasks
      final TaskTaskKey reqDefn = Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.againstConfigurationSlot( rootConfigSlot );
         requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.TERMINATE,
               followingReqDefn );
         requirementDefinition.addFollowingTaskDefinition( RefTaskDepActionKey.TERMINATE,
               followingReqDefn2 );
      } );

      // create actual task base on requirement definition above.
      final TaskKey task = Domain.createRequirement( req -> {
         req.setDefinition( reqDefn );
         req.setInventory( aircraft );
         req.setStatus( RefEventStatusKey.ACTV );
      } );

      final Date taskCompletionDate = DateUtils.floorSecond( REQUIREMENT_EXECUTION_DATE );

      // ACT
      new CompleteService( task ).complete( Domain.createHumanResource(), taskCompletionDate,
            null );

      // ASSERT
      assertEquals( 3, eventBus.getEventMessages().size() );

      List<EventMessage<?>> taskCompletedEvents = eventBus.getEventMessages().stream()
            .filter( eventMessage -> eventMessage.getPayload() instanceof TaskCompletedEvent )
            .collect( Collectors.toList() );
      assertEquals( 1, taskCompletedEvents.size() );
      assertEquals( task,
            ( ( TaskCompletedEvent ) taskCompletedEvents.get( 0 ).getPayload() ).getTaskKey() );

      Set<TaskKey> taskKeys = eventBus.getEventMessages().stream().filter( eventMessage -> {
         return eventMessage.getPayload() instanceof TaskTerminatedEvent;
      } ).map( event -> {
         return ( ( TaskTerminatedEvent ) event.getPayload() ).getTaskKey();
      } ).collect( Collectors.toSet() );
      assertTrue( taskKeys.contains( followingTask ) );
      assertTrue( taskKeys.contains( followingTask2 ) );
   }


   private AssemblyKey createAircraftAssembly( final PartNoKey aircraftPart ) {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aircraftAssembly ) {
            aircraftAssembly
                  .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot rootConfigSlot ) {
                        rootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup rootCsPartGroup ) {
                              rootCsPartGroup.setInventoryClass( ACFT );
                              rootCsPartGroup.addPart( aircraftPart );
                           }
                        } );
                     }
                  } );
         }
      } );
   }


   private PartNoKey createEnginePartNo() {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part part ) {
            part.setInventoryClass( RefInvClassKey.ASSY );
         }

      } );
   }


   private InventoryKey createAircraftInventoryWithManufacturedDateAndCurrentUsage(
         final PartNoKey aircraftPart, final AssemblyKey assembly ) {
      return Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( assembly );
         aircraft.setPart( aircraftPart );
         aircraft.addSystem( SYSTEM_NAME );
         aircraft.setManufacturedDate( AIRCRAFT_MANUFACTURED_DATE );
         aircraft.addUsage( DataTypeKey.HOURS, CURRENT_USAGE );
         aircraft.setLocation( new LocationKey( "1:1" ) );
      } );
   }


   private AssemblyKey createAircraftAssemblyWithTrackedConfigSlot( final PartNoKey aircraftPart,
         final PartNoKey trkPart ) {
      return Domain.createAircraftAssembly( aircraftAssembly -> {

         aircraftAssembly.setCode( ASSEMBLY_CODE );
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            rootConfigSlot.addPartGroup( rootCsPartGroup -> {
               rootCsPartGroup.setInventoryClass( ACFT );
               rootCsPartGroup.addPart( aircraftPart );
            } );

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.addPartGroup( sysCsPartGroup -> {
                  sysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );

               sysConfigSlot.addConfigurationSlot( trkedConfigSlot -> {
                  trkedConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  trkedConfigSlot.setCode( CONFIG_SLOT_TRK_CODE );
                  trkedConfigSlot.addPartGroup( trkedCsPartGroup -> {
                     trkedCsPartGroup.setCode( TRK_PKG_CODE );
                     trkedCsPartGroup.setInventoryClass( RefInvClassKey.TRK );
                     trkedCsPartGroup.addPart( trkPart );
                  } );
               } );
            } );
         } );
      } );
   }

}
