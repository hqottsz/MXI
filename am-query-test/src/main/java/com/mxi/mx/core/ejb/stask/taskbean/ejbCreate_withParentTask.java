package com.mxi.mx.core.ejb.stask.taskbean;

import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createAircraftAssembly;
import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.am.domain.Domain.createRequirementDefinition;
import static com.mxi.am.domain.Domain.readRootConfigurationSlot;
import static com.mxi.mx.common.utils.DateUtils.floorMillisecond;
import static com.mxi.mx.common.utils.DateUtils.getEndOfDay;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.RefEventTypeKey.TS;
import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static com.mxi.mx.core.key.RefLabourRoleTypeKey.CERT;
import static com.mxi.mx.core.key.RefLabourRoleTypeKey.INSP;
import static com.mxi.mx.core.key.RefLabourRoleTypeKey.TECH;
import static com.mxi.mx.core.key.RefLabourSkillKey.ENG;
import static com.mxi.mx.core.key.RefSchedFromKey.EFFECTIV;
import static com.mxi.mx.core.key.RefSchedPriorityKey.NONE;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.OneTimeSchedulingRule;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedStaskFlagKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.ZoneKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskFlagsTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskPanelTable;
import com.mxi.mx.core.table.task.TaskZoneTable;


/**
 * Integration unit test for
 * {@link TaskBean#ejbCreate(TaskKey, InventoryKey, TaskTaskKey, Date, HumanResourceKey, TaskKey)}
 *
 * This is the overloaded method to which a parent task key is provided (see signature above).
 *
 *
 * Note; some of these tests do not actually provide a parent task key. The reason these tests are
 * here are because this is the method that is actually used by the UI in many scenarios.
 *
 */
public class ejbCreate_withParentTask {

   private static final boolean PREVENT_EXECUTION = true;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private static EvtEventDao evtEventDao = new JdbcEvtEventDao();
   private static SchedStaskDao schedStaskDao = new JdbcSchedStaskDao();

   private static final TaskKey NO_PROVIDED_TASK_KEY = null;
   private static final Date NA_PREV_COMPLETION_DATE = null;
   private static final TaskKey NA_PARENT_TASK = null;

   private static final String REQ_CODE = "REQ_CODE";
   private static final String REVISION_NOTE = "REVISION_NOTE";
   private static final BigDecimal SCHED_RULE_DEVIATION = BigDecimal.valueOf( 2 );
   private static final BigDecimal SCHED_RULE_NOTIFICATION = BigDecimal.valueOf( 3 );
   private static final BigDecimal SCHED_RULE_PLAN_HIGH = BigDecimal.valueOf( 4 );
   private static final BigDecimal SCHED_RULE_PLAN_LOW = BigDecimal.valueOf( 5 );
   private static final BigDecimal SCHED_RULE_THRESHOLD = BigDecimal.valueOf( 6 );
   private static final Date EFFECTIVE_DATE = new Date();
   private static final BigDecimal LABOUR_SCHEDULED_HOURS = BigDecimal.valueOf( 7 );
   private static final BigDecimal LABOUR_CERIFICATION_HOURS = BigDecimal.valueOf( 8 );
   private static final BigDecimal LABOUR_INSPECTION_HOURS = BigDecimal.valueOf( 9 );
   private static final String INSTRUCTIONS = "Pick the snails off the teflon injector.\n"
         + "Uncouple the aft plutonium force compressor.\n"
         + "Reroute emergency power through the nucleonic plasma splitter.";
   private static final BigDecimal TOOL_SCHEDULED_HOURS = BigDecimal.valueOf( 5 );
   private static final String ZONE_CODE = "Zone 9";
   private static final String PANEL_CODE = "Panel 5";

   private PartNoKey aircraftPart;

   private AssemblyKey aircraftAssembly;

   private InventoryKey aircraftInventory;

   private ConfigSlotKey aircraftRootConfigSlot;


   @Before
   public void setup() {
      // Setup some support data.
      // Note; ejbCreate performs a validation to ensure the inventory's part matches the
      // definition's config slot part.
      aircraftPart = Domain.createPart();
      aircraftAssembly = buildAssembly( aircraftPart );
      aircraftInventory = buildInventory( aircraftPart, aircraftAssembly );
      aircraftRootConfigSlot = readRootConfigurationSlot( aircraftAssembly );
   }


   /**
    * Verify that when an executable requirement definition is used to create a task, that the task
    * is created with the definitions mandatory attributes.
    */
   @Test
   public void execReqCreatedFromDefnWithMandatoryAttrs() throws Exception {

      // Given an executable requirement definition with mandatory attributes set and a status of
      // ACTV.
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );
      } );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      // Then the executable requirement task contains all the mandatory attributes.
      SchedStaskTable schedStaskRow = schedStaskDao.findByPrimaryKey( task );
      assertThat( schedStaskRow.getTaskTaskKey(), is( execReqDefn ) );
      assertThat( schedStaskRow.getMainInventory(), is( aircraftInventory ) );
      assertThat( schedStaskRow.getTaskClass(), is( REQ ) );
      assertThat( schedStaskRow.getOrigPartNo(), is( aircraftPart ) );

      // Then the executable requirement task contains other expected attributes.
      assertThat( schedStaskRow.getBarcode(), notNullValue() );
      assertThat( schedStaskRow.getHTaskKey(), is( task ) );

      EventKey event = task.getEventKey();
      EvtEventTable evtEventRow = evtEventDao.findByPrimaryKey( event );
      assertThat( evtEventRow.getEventType(), is( TS ) );
      assertThat( evtEventRow.getHEvent(), is( event ) );
      assertThat( evtEventRow.getEventStatus(), is( RefEventStatusKey.ACTV ) );
      assertThat( evtEventRow.getSchedPriority(), is( NONE ) );
   }


   /**
    * Verify that when an executable requirement definition with some optional attributes is used to
    * create a task, that the task is created with those optional attributes.
    */
   @Test
   public void execReqCreatedFromDefnWithOptionalAttrs() throws Exception {

      // Given an executable requirement definition with a status of ACTV.
      // And the definition has optional attributes set.
      // (there may be a lot more attributes but these are the ones supported by the test domain
      // builders at the time of coding)
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );

         defn.setCode( REQ_CODE );
         defn.setPreventExecution( PREVENT_EXECUTION );
         defn.setRevisionNote( REVISION_NOTE );
         // Note that Instructions are optional from service standpoint, however are mandatory from
         // a UI standpoint.
         defn.setInstructions( INSTRUCTIONS );
      } );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      // Then the executable requirement task contains all the optional attributes.
      EvtEventTable evtEventRow = evtEventDao.findByPrimaryKey( task );
      SchedStaskFlagsTable schedStaskFlagsRow =
            SchedStaskFlagsTable.findByPrimaryKey( new SchedStaskFlagKey( task ) );
      SchedStaskTable schedStaskRow = schedStaskDao.findByPrimaryKey( task );

      String expectedSDesc = REQ_CODE + " ()";
      assertThat( evtEventRow.getEventSdesc(), is( expectedSDesc ) );
      assertThat( schedStaskFlagsRow.isPreventExeBool(), is( PREVENT_EXECUTION ) );
      assertThat( evtEventRow.getDocRefSdesc(), is( REVISION_NOTE ) );
      assertThat( schedStaskRow.getInstructionLdesc(), is( INSTRUCTIONS ) );
   }


   /**
    * Verify that when an executable requirement definition with a scheduling rule is used to create
    * a task, that the task is created with a deadline (corresponding to the definition's scheduling
    * rule).
    */
   @Test
   public void execReqCreatedFromDefnWithSchedulingRule() throws Exception {

      // Given an executable requirement definition with a status of ACTV.
      // And the definition has a scheduling rule.
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );

         OneTimeSchedulingRule schedulingRule = new OneTimeSchedulingRule();
         schedulingRule.setUsageParameter( CDY );
         schedulingRule.setDeviation( SCHED_RULE_DEVIATION );
         schedulingRule.setNotification( SCHED_RULE_NOTIFICATION );
         schedulingRule.setSchedToPlanHigh( SCHED_RULE_PLAN_HIGH );
         schedulingRule.setSchedToPlanLow( SCHED_RULE_PLAN_LOW );
         schedulingRule.setThreshold( SCHED_RULE_THRESHOLD );

         defn.setScheduledFromEffectiveDate( EFFECTIVE_DATE );
         defn.addSchedulingRule( schedulingRule );

      } );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      // Then the executable requirement task contains a deadline corresponding to the definitions
      // scheduling rule.

      EvtSchedDeadTable evtSchedDeadRow = EvtSchedDeadTable.findByPrimaryKey( task, CDY );
      assertThat( evtSchedDeadRow.getScheduledFrom(), is( EFFECTIV ) );
      assertThat( evtSchedDeadRow.getDeviationQt(), is( SCHED_RULE_DEVIATION.doubleValue() ) );
      assertThat( evtSchedDeadRow.getNotifyQt(), is( SCHED_RULE_NOTIFICATION.doubleValue() ) );
      assertThat( evtSchedDeadRow.getPostfixedQt(), is( SCHED_RULE_PLAN_HIGH.doubleValue() ) );
      assertThat( evtSchedDeadRow.getPrefixedQt(), is( SCHED_RULE_PLAN_LOW.doubleValue() ) );
      assertThat( evtSchedDeadRow.getIntervalQt(), is( SCHED_RULE_THRESHOLD.doubleValue() ) );

      // Expected start date is the end-of-day of the effective date (without milliseconds).
      Date expectedStartDate = floorMillisecond( getEndOfDay( EFFECTIVE_DATE ) );
      assertThat( evtSchedDeadRow.getStartDate(), is( expectedStartDate ) );
   }


   /**
    * Verify that when an executable requirement definition with a labour requirement is used to
    * create a task, that the task is created with a labour row (corresponding to the definition's
    * labour requirement).
    */
   @Test
   public void execReqCreatedFromDefnWithLabourRequirement() throws Exception {

      // Given an executable requirement definition with a status of ACTV.
      // And the definition has a labour requirement.
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );

         LabourRequirement labour = new LabourRequirement( ENG, LABOUR_SCHEDULED_HOURS,
               LABOUR_CERIFICATION_HOURS, LABOUR_INSPECTION_HOURS );
         labour.setNumberOfPeopleRequired( BigDecimal.ONE );

         defn.addLabourRequirement( labour );
      } );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      // Then the executable requirement task contains a labour requirement
      DataSetArgument args;
      QuerySet qs;

      args = new DataSetArgument();
      args.add( task, "sched_db_id", "sched_id" );
      args.add( ENG, "labour_skill_db_id", "labour_skill_cd" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "SCHED_LABOUR", args );
      qs.next();
      assertThat( qs.getRowCount(), is( 1 ) );
      assertThat( qs.getBoolean( "work_perf_bool" ), is( true ) );
      assertThat( qs.getBoolean( "cert_bool" ), is( true ) );
      assertThat( qs.getBoolean( "insp_bool" ), is( true ) );
      SchedLabourKey schedLabourKey =
            qs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      args = new DataSetArgument();
      args.add( schedLabourKey, "labour_db_id", "labour_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "SCHED_LABOUR_ROLE", args );

      assertThat( qs.getRowCount(), is( 3 ) );

      Map<RefLabourRoleTypeKey, BigDecimal> roleToScheduledHours = new HashMap<>();
      while ( qs.next() ) {
         roleToScheduledHours.put( qs.getKey( RefLabourRoleTypeKey.class, "labour_role_type_db_id",
               "labour_role_type_cd" ), qs.getBigDecimal( "sched_hr" ) );
      }

      assertThat( "Expected labour roles were not found", roleToScheduledHours.keySet(),
            Matchers.containsInAnyOrder( TECH, INSP, CERT ) );

      assertThat( "The technician (TECH) role did not have the expected number of scheduled hours",
            roleToScheduledHours.get( TECH ), is( LABOUR_SCHEDULED_HOURS ) );
      assertThat( "The certifier (CERT) role did not have the expected number of scheduled hours",
            roleToScheduledHours.get( CERT ), is( LABOUR_CERIFICATION_HOURS ) );
      assertThat( "The inspector (INSP) role did not have the expected number of scheduled hours",
            roleToScheduledHours.get( INSP ), is( LABOUR_INSPECTION_HOURS ) );
   }


   /**
    * Verify that when an executable requirement definition with a part requirement is used to
    * create a task, that the task is created with a sched part row (corresponding to the
    * definition's labour requirement) and a part request.
    */
   @Test
   public void execReqCreatedFromDefnWithPartRequirement() throws Exception {

      final PartGroupKey installedPartGroup =
            Domain.createPartGroup( new DomainConfiguration<PartGroup>() {

               @Override
               public void configure( PartGroup aBuilder ) {
                  aBuilder.setInventoryClass( RefInvClassKey.SER );
               }
            } );

      final PartNoKey installedPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInDefaultPartGroup( true );
            aBuilder.setInventoryClass( RefInvClassKey.SER );
            aBuilder.setPartGroup( installedPartGroup, true );
         }
      } );

      // Given an executable requirement definition with a status of ACTV.
      // And the definition has a part requirement.
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );

         defn.addPartRequirementDefinition( accessPartRequirementDefinition -> {
            accessPartRequirementDefinition.setAction( RefReqActionKey.REQ );
            accessPartRequirementDefinition.setIsInstall( true );
            accessPartRequirementDefinition.setIsRemoval( true );
            accessPartRequirementDefinition.setQuantity( Double.valueOf( 1 ) );
            accessPartRequirementDefinition.setPartGroup( installedPartGroup );
            accessPartRequirementDefinition.setSpecificPart( installedPart );
         } );
      } );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      DataSetArgument args = new DataSetArgument();
      QuerySet qs;

      // Then the executable requirement task contains a sched-part requirement
      args.add( task, "sched_db_id", "sched_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "SCHED_PART", args );
      assertThat( "Expected sched-part was not found", qs.getRowCount(), is( 1 ) );

      qs.next();

      RefReqActionKey refActionKey =
            qs.getKey( RefReqActionKey.class, "req_action_db_id", "req_action_cd" );
      assertThat( "Unexpected sched-part action", refActionKey, is( RefReqActionKey.REQ ) );

      assertThat( "Unexpected sched-part quantity", qs.getInteger( "sched_qt" ), is( 1 ) );

      PartGroupKey partGroupKey =
            qs.getKey( PartGroupKey.class, "sched_bom_part_db_id", "sched_bom_part_id" );
      assertThat( "sched-part not associated to expected part group", partGroupKey,
            is( installedPartGroup ) );

      PartNoKey partKey = qs.getKey( PartNoKey.class, "spec_part_no_db_id", "spec_part_no_id" );
      assertThat( "sched-part not associated to expected part", partKey, is( installedPart ) );

      int schedPartId = qs.getInteger( "sched_part_id" );

      args.clear();
      args.add( new TaskPartKey( task, schedPartId ), "sched_db_id", "sched_id", "sched_part_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "REQ_PART", args );
      assertThat( "Expected Part Request was not found", qs.getRowCount(), is( 1 ) );
   }


   /**
    * Verify that when an executable requirement definition with a tool requirement is used to
    * create a task, that the task is created with an evt_tool row (corresponding to the
    * definition's tool requirement).
    */
   @Test
   public void execReqCreatedFromDefnWithToolRequirement() throws Exception {

      final PartNoKey toolPartKey = Domain.createPart( part -> {
         part.setInDefaultPartGroup( true );
         part.setInventoryClass( RefInvClassKey.SER );
         part.setTool( true );
      } );

      // Given an executable requirement definition with a status of ACTV.
      // And the definition has a part requirement.
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );

         defn.addToolRequirement( toolPartKey, TOOL_SCHEDULED_HOURS );
      } );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      DataSetArgument args = new DataSetArgument();
      QuerySet qs;

      // Then the executable requirement task contains a tool requirement
      args.add( task, "event_db_id", "event_id" );
      args.add( toolPartKey, "part_no_db_id", "part_no_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "EVT_TOOL", args );
      assertThat( "Expected task tool requirement was not found", qs.getRowCount(), is( 1 ) );

      qs.next();

      assertThat( "Unexpected tool scheduled hours", qs.getBigDecimal( "sched_hr" ),
            is( TOOL_SCHEDULED_HOURS ) );
   }


   /**
    * Verify that when an executable requirement definition with a measurement requirement is used
    * to create a task, that the task is created with a inv_parm_data row (corresponding to the
    * definition's measurement requirement).
    */
   @Test
   public void execReqCreatedFromDefnWithMeasurementRequirement() throws Exception {

      DataTypeKey measurementDefinitionKey = Domain.createMeasurementDefinition();

      // Given an executable requirement definition with a status of ACTV.
      // And the definition has a part requirement.
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );

         defn.addMeasurement( measurementDefinitionKey );
      } );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      DataSetArgument args = new DataSetArgument();
      QuerySet qs;

      // Then the executable requirement task contains a measurement requirement
      args.add( task, "event_db_id", "event_id" );
      args.add( measurementDefinitionKey, "data_type_db_id", "data_type_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "INV_PARM_DATA", args );
      assertThat( "Expected measurement requirement was not found", qs.getRowCount(), is( 1 ) );
   }


   /*
    * Verify that when an executable requirement definition with a zone is used to create a task,
    * that the task is created with a sched_zone row (corresponding to the definition's zone).
    */
   @Test
   public void execReqCreatedFromDefnWithZone() throws Exception {

      // Given an executable requirement definition with a status of ACTV.
      // And the definition has a part requirement.
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );
      } );

      ZoneKey zoneKey = Domain.createZone( zone -> {
         zone.setZoneCode( ZONE_CODE );
         zone.setParentAssembly( aircraftAssembly );
      } );

      // Add the zone to the requirement definition
      associateZoneToRequirement( execReqDefn, zoneKey );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      DataSetArgument args = new DataSetArgument();
      QuerySet qs;

      // Then the executable requirement task contains the expected zone
      args.add( task, "sched_db_id", "sched_id" );
      args.add( zoneKey, "zone_db_id", "zone_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "SCHED_ZONE", args );
      assertThat( "Expected sched_zone was not found", qs.getRowCount(), is( 1 ) );
   }


   /*
    * Verify that when an executable requirement definition with a panel is used to create a task,
    * that the task is created with a sched_panel row (corresponding to the definition's panel).
    */
   @Test
   public void execReqCreatedFromDefnWithPanel() throws Exception {

      // Given an executable requirement definition with a status of ACTV.
      // And the definition has a part requirement.
      TaskTaskKey execReqDefn = createRequirementDefinition( defn -> {
         defn.setExecutable( true );
         defn.setTaskClass( REQ );
         defn.againstConfigurationSlot( aircraftRootConfigSlot );
         defn.setStatus( ACTV );
      } );

      PanelKey panelKey = Domain.createPanel( panel -> {
         panel.setCode( PANEL_CODE );
         panel.setParentAssembly( aircraftAssembly );
      } );

      // Add the panel to the requirement definition
      associatePanelToRequirement( execReqDefn, panelKey );

      // When a task is created from the definition.
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      TaskKey task = taskBean.createFromTaskDefinition( NO_PROVIDED_TASK_KEY, aircraftInventory,
            execReqDefn, NA_PREV_COMPLETION_DATE, createHumanResource(), NA_PARENT_TASK );

      DataSetArgument args = new DataSetArgument();
      QuerySet qs;

      // Then the executable requirement task contains the expected panel
      args.add( task, "sched_db_id", "sched_id" );
      args.add( panelKey, "panel_db_id", "panel_id" );
      qs = QuerySetFactory.getInstance().executeQueryTable( "SCHED_PANEL", args );
      assertThat( "Expected sched_panel was not found", qs.getRowCount(), is( 1 ) );
   }


   private void associateZoneToRequirement( TaskTaskKey execReqDefn, ZoneKey zoneKey ) {
      TaskZoneTable lTaskZone =
            TaskZoneTable.create( TaskZoneTable.generatePrimaryKey( execReqDefn ) );
      lTaskZone.setZone( zoneKey );
      lTaskZone.insert();
   }


   private void associatePanelToRequirement( TaskTaskKey execReqDefn, PanelKey panelKey ) {
      TaskPanelTable taskPanelTable =
            TaskPanelTable.create( TaskPanelTable.generatePrimaryKey( execReqDefn ) );
      taskPanelTable.setPanel( panelKey );
      taskPanelTable.insert();
   }


   private AssemblyKey buildAssembly( PartNoKey part ) {
      return createAircraftAssembly( assy -> {
         assy.setRootConfigurationSlot( rootCs -> {
            rootCs.addPartGroup( pg -> {
               pg.setInventoryClass( ACFT );
               pg.addPart( part );
            } );
         } );
      } );
   }


   private InventoryKey buildInventory( PartNoKey part, AssemblyKey assembly ) {
      return createAircraft( acft -> {
         acft.setAssembly( assembly );
         acft.setPart( part );
      } );
   }

}
