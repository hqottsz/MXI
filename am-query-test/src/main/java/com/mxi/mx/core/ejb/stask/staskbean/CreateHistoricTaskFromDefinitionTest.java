package com.mxi.mx.core.ejb.stask.staskbean;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * <pre>
 *
 * Integration unit tests for
 * {@link STaskBean#createHistoricTaskFromDefinition(InventoryKey, TaskKey, TaskTaskKey, Date, com.mxi.mx.core.services.event.inventory.UsageSnapshot[], String, HumanResourceKey, String, TaskKey)
 * and
 * {@link STaskBean#createHistoricTask(InventoryKey, TaskKey, HumanResourceKey, String, String, String, java.util.List, String, String, Date)}}
 *
 * </pre>
 */
public class CreateHistoricTaskFromDefinitionTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey iAuthHr;
   private int iUserId;
   private static final Date HISTORIC_TASK_COMPLETION_DATE = DateUtils.addDays( new Date(), -10 );
   private static final Boolean IS_HISTORICAL = true;
   private SchedStaskDao iSchedStaskDao;
   private EvtEventDao iEvtEventDao;


   @Before
   public void before() {
      iAuthHr = Domain.createHumanResource();
      iUserId = OrgHr.findByPrimaryKey( iAuthHr ).getUserId();
      UserParameters.setInstance( iUserId, "LOGIC", new UserParametersFake( iUserId, "LOGIC" ) );
      iSchedStaskDao = new JdbcSchedStaskDao();
      iEvtEventDao = new JdbcEvtEventDao();
   }


   @After
   public void after() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }


   /**
    *
    * Verify that a historical task is created from a requirement definition.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot
    *       - and the root config-slot has a part group with a part
    *       - and an aircraft based on this assembly has a part
    *       - and a requirement definition of class REQ against the root config-slot of the aircraft assembly
    *
    * When - a historical task is created against the aircraft based on the requirement definition
    *
    * Then - the historical task is created successfully
    *
    * </pre>
    *
    */
   @Test
   public void itCreatesHistoricTaskBasedOnRequirementDefinition()
         throws MxException, TriggerException {

      // Given
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( lAircraftPart );
                  } ) ) );

      final ConfigSlotKey lConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lAircraftPart );
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lConfigSlot );
      } );

      // When
      final TaskKey lHistoricTaskKey = new STaskBean().createHistoricTaskFromDefinition( lAircraft,
            null, lReqDefinition, HISTORIC_TASK_COMPLETION_DATE, null, null, iAuthHr, null, null );

      // Then
      EvtEventTable lEvtEventTable =
            iEvtEventDao.findByPrimaryKey( lHistoricTaskKey.getEventKey() );

      // Verify that a task is created
      assertEquals( "Unexpectedly, the task was not created.", RefEventTypeKey.TS,
            lEvtEventTable.getEventType() );

      // Verify that the created task is historical
      assertEquals( "Unexpectedly, the created task was not historical.", IS_HISTORICAL,
            lEvtEventTable.getHistBool() );

      // Verify that the historical task is created from the requirement definition
      TaskTaskKey lActualReqDefn =
            iSchedStaskDao.findByPrimaryKey( lHistoricTaskKey ).getTaskTaskKey();
      assertEquals( "Unexpectedly, incorrect Requirement definition is initialized", lReqDefinition,
            lActualReqDefn );
   }


   /**
    *
    * Verify that a historical task is created from a requirement definition of class MOD.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot
    *       - and the root config-slot has a part group with two parts
    *       - and an aircraft based on this assembly has a part
    *       - and a requirement definition of class MOD against the root config-slot of the aircraft assembly
    *
    * When - a historical task is created against the aircraft based on the requirement definition
    *
    * Then - the historical task is created successfully
    *
    * </pre>
    *
    */
   @Test
   public void itCreatesHistoricTaskBasedOnRequirementDefinitionOfClassMod()
         throws MxException, TriggerException {

      // Given
      final PartNoKey lOldPartNo = Domain.createPart();
      final PartNoKey lNewPartNo = Domain.createPart();

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( lOldPartNo );
                     aPartGroup.addPart( lNewPartNo );
                  } ) ) );

      final ConfigSlotKey lConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lOldPartNo );
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setTaskClass( RefTaskClassKey.MOD );
         aReqDefn.againstConfigurationSlot( lConfigSlot );
         aReqDefn.addPartTransformation( lOldPartNo, lNewPartNo );
      } );

      // When
      final TaskKey lHistoricTaskKey = new STaskBean().createHistoricTaskFromDefinition( lAircraft,
            null, lReqDefinition, HISTORIC_TASK_COMPLETION_DATE, null, null, iAuthHr, null, null );

      // Then
      EvtEventTable lEvtEventTable =
            iEvtEventDao.findByPrimaryKey( lHistoricTaskKey.getEventKey() );

      // Verify that a task is created
      assertEquals( "Unexpectedly, the task was not created.", RefEventTypeKey.TS,
            lEvtEventTable.getEventType() );

      // Verify that the created task is historical
      assertEquals( "Unexpectedly, the created task was not historical.", IS_HISTORICAL,
            lEvtEventTable.getHistBool() );

      // Verify that the historical task is created from the requirement definition
      SchedStaskTable lSchedStask = iSchedStaskDao.findByPrimaryKey( lHistoricTaskKey );
      assertEquals( "Unexpectedly, incorrect Requirement definition is initialized", lReqDefinition,
            lSchedStask.getTaskTaskKey() );
      assertEquals( "Unexpectedly, Requirement definition of incorrect class is initialized",
            RefTaskClassKey.MOD, lSchedStask.getTaskClass() );

   }


   /**
    *
    * Verify that a historical task is created from a requirement definition of class CORR.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot
    *       - and the root config-slot has a part group with a part
    *       - and an aircraft based on this assembly has a part
    *       - and a requirement definition of class CORR against the root config-slot of the aircraft assembly
    *
    * When - a historical task is created against the aircraft based on the requirement definition
    *
    * Then - the historical task is created successfully
    *
    * </pre>
    *
    */
   @Test
   public void itCreatesHistoricTaskBasedOnRequirementDefinitionOfClassCorr()
         throws MxException, TriggerException {

      // Given
      final PartNoKey lPartNo = Domain.createPart();

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( lPartNo );
                  } ) ) );

      final ConfigSlotKey lConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lPartNo );
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setTaskClass( RefTaskClassKey.CORR );
         aReqDefn.againstConfigurationSlot( lConfigSlot );
      } );

      // When
      final TaskKey lHistoricTaskKey = new STaskBean().createHistoricTaskFromDefinition( lAircraft,
            null, lReqDefinition, HISTORIC_TASK_COMPLETION_DATE, null, null, iAuthHr, null, null );

      // Then
      EvtEventTable lEvtEventTable =
            iEvtEventDao.findByPrimaryKey( lHistoricTaskKey.getEventKey() );

      // Verify that a task is created
      assertEquals( "Unexpectedly, the task was not created.", RefEventTypeKey.TS,
            lEvtEventTable.getEventType() );

      // Verify that the created task is historical
      assertEquals( "Unexpectedly, the created task was not historical.", IS_HISTORICAL,
            lEvtEventTable.getHistBool() );

      // Verify that the historical task is created from the requirement definition
      SchedStaskTable lSchedStask = iSchedStaskDao.findByPrimaryKey( lHistoricTaskKey );
      assertEquals( "Unexpectedly, incorrect Requirement definition is initialized", lReqDefinition,
            lSchedStask.getTaskTaskKey() );
      assertEquals( "Unexpectedly, Requirement definition of incorrect class is initialized",
            RefTaskClassKey.CORR, lSchedStask.getTaskClass() );

   }


   /**
    *
    * Verify that a historical task is created from a requirement definition of class OVHL.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot
    *       - and the root config-slot has a part group with a part
    *       - and an aircraft based on this assembly has a part
    *       - and a requirement definition of class OVHL against the root config-slot of the aircraft assembly
    *
    * When - a historical task is created against the aircraft based on the requirement definition
    *
    * Then - the historical task is created successfully
    *
    * </pre>
    *
    */
   @Test
   public void itCreatesHistoricTaskBasedOnRequirementDefinitionOfClassOvhl()
         throws MxException, TriggerException {

      // Given
      final PartNoKey lPartNo = Domain.createPart();

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( lPartNo );
                  } ) ) );

      final ConfigSlotKey lConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lPartNo );
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setTaskClass( RefTaskClassKey.OVHL );
         aReqDefn.againstConfigurationSlot( lConfigSlot );
      } );

      // When
      final TaskKey lHistoricTaskKey = new STaskBean().createHistoricTaskFromDefinition( lAircraft,
            null, lReqDefinition, HISTORIC_TASK_COMPLETION_DATE, null, null, iAuthHr, null, null );

      // Then
      EvtEventTable lEvtEventTable =
            iEvtEventDao.findByPrimaryKey( lHistoricTaskKey.getEventKey() );

      // Verify that a task is created
      assertEquals( "Unexpectedly, the task was not created.", RefEventTypeKey.TS,
            lEvtEventTable.getEventType() );

      // Verify that the created task is historical
      assertEquals( "Unexpectedly, the created task was not historical.", IS_HISTORICAL,
            lEvtEventTable.getHistBool() );

      // Verify that the historical task is created from the requirement definition
      SchedStaskTable lSchedStask = iSchedStaskDao.findByPrimaryKey( lHistoricTaskKey );
      assertEquals( "Unexpectedly, incorrect Requirement definition is initialized", lReqDefinition,
            lSchedStask.getTaskTaskKey() );
      assertEquals( "Unexpectedly, Requirement definition of incorrect class is initialized",
            RefTaskClassKey.OVHL, lSchedStask.getTaskClass() );
   }


   /**
    *
    * Verify that an historical ad-hoc sub-task is created for a historical ad-hoc task.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot
    *       - and the root config-slot has a part group with a part
    *       - and an aircraft based on this assembly has a part
    *       - and a historical ad-hoc task on the aircraft.
    *
    * When - a historical ad-hoc sub-task is created for a historical ad-hoc task against the aircraft
    *
    * Then - the historical ad-hoc sub-task is created successfully
    *
    * </pre>
    *
    */
   @Test
   public void itCreatesAdHocHistoricSubTaskForHistoricAdhocTask()
         throws MxException, TriggerException {

      final String lTASK_NAME = "REQSub-1";

      // Given
      final PartNoKey lPartNo = Domain.createPart();

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( lPartNo );
                  } ) ) );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lPartNo );
      } );

      final TaskKey lAdhocTask = Domain.createAdhocTask( aAdhocTask -> {
         aAdhocTask.setStatus( RefEventStatusKey.COMPLETE );
         aAdhocTask.setInventory( lAircraft );
      } );

      // When
      final TaskKey lHistoricSubTaskKey = new STaskBean().createHistoricTask( lAircraft, lAdhocTask,
            iAuthHr, lTASK_NAME, null, null, null, null, null, null );

      // Then
      EvtEventTable lEvtEventTable =
            iEvtEventDao.findByPrimaryKey( lHistoricSubTaskKey.getEventKey() );

      final EventKey lEventKey = lEvtEventTable.getNhEvent();
      final EventKey lParentEventKey = lAdhocTask.getEventKey();

      // Verify that a task is created
      assertEquals( "Unexpectedly, the task was not created.", RefEventTypeKey.TS,
            lEvtEventTable.getEventType() );

      // Verify that the created task is historical
      assertEquals( "Unexpectedly, the created task was not historical.", IS_HISTORICAL,
            lEvtEventTable.getHistBool() );

      // Verify that the created task is a sub-task of the given historical ad-hoc task
      assertEquals(
            "Unexpectedly, the created historical sub-task is for a wrong historical Ad-hoc task.",
            lParentEventKey, lEventKey );

   }


   /**
    *
    * Verify that a definition based historical sub-task can be created for a historical ad-hoc
    * task.
    *
    * <pre>
    *
    * Given - an aircraft assembly with a root config-slot
    *       - and the root config-slot has a part group with a part
    *       - and an aircraft based on this assembly has a part
    *       - and a historical ad-hoc task on the aircraft.
    *
    * When - a historical sub-task based on the requirement definition is created for a historical ad-hoc task against the aircraft
    *
    * Then - the historical sub-task is created successfully
    *
    * </pre>
    *
    */
   @Test
   public void itCreatesHistoricSubTaskBasedOnRequirementDefinitionForHistoricAdhocTask()
         throws MxException, TriggerException {

      // Given
      final PartNoKey lPartNo = Domain.createPart();

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAircraftAssembly -> aAircraftAssembly.setRootConfigurationSlot(
                  aRootConfigSlot -> aRootConfigSlot.addPartGroup( aPartGroup -> {
                     aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aPartGroup.addPart( lPartNo );
                  } ) ) );

      final ConfigSlotKey lConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );

      final InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setAssembly( lAircraftAssembly );
         aAircraft.setPart( lPartNo );
      } );

      final TaskTaskKey lReqDefinition = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lConfigSlot );
      } );

      final TaskKey lAdhocTask = Domain.createAdhocTask( aAdhocTask -> {
         aAdhocTask.setStatus( RefEventStatusKey.COMPLETE );
         aAdhocTask.setInventory( lAircraft );
      } );

      // When
      final TaskKey lHistoricTaskKey =
            new STaskBean().createHistoricTaskFromDefinition( lAircraft, lAdhocTask, lReqDefinition,
                  HISTORIC_TASK_COMPLETION_DATE, null, null, iAuthHr, null, null );

      // Then
      EvtEventTable lEvtEventTable =
            iEvtEventDao.findByPrimaryKey( lHistoricTaskKey.getEventKey() );

      final EventKey lEventKey = lEvtEventTable.getNhEvent();
      final EventKey lParentEventKey = lAdhocTask.getEventKey();

      // Verify that a task is created
      assertEquals( "Unexpectedly, the task was not created.", RefEventTypeKey.TS,
            lEvtEventTable.getEventType() );

      // Verify that the created task is historical
      assertEquals( "Unexpectedly, the created task was not historical.", IS_HISTORICAL,
            lEvtEventTable.getHistBool() );

      // Verify that the created task is a sub-task of the given historical ad-hoc task
      assertEquals(
            "Unexpectedly, the created historical sub-task is for a wrong historical Ad-hoc task.",
            lParentEventKey, lEventKey );

      // Verify that the historical task is created from the requirement definition
      SchedStaskTable lSchedStask = iSchedStaskDao.findByPrimaryKey( lHistoricTaskKey );
      assertEquals( "Unexpectedly, incorrect Requirement definition is initialized", lReqDefinition,
            lSchedStask.getTaskTaskKey() );
   }

}
