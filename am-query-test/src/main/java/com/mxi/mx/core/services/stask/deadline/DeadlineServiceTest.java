package com.mxi.mx.core.services.stask.deadline;

import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.ceilDay;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static com.mxi.mx.core.key.RefSchedFromKey.CUSTOM;
import static com.mxi.mx.core.key.RefSchedFromKey.EFFECTIV;
import static com.mxi.mx.core.key.RefSchedFromKey.LASTDUE;
import static com.mxi.mx.core.key.RefTaskClassKey.ADHOC;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.Block;
import com.mxi.am.domain.BlockChainDefinition;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskDeadlinesRemovedEvent;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.unittest.table.evt.EvtStage;


/**
 * Test cases for {@linkplain DeadlineService}
 *
 * Note: some tests for particular methods have been moved into their own method test classes under
 * the deadlineservice sub-directory.
 *
 */
public class DeadlineServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final Date NOW = new Date();

   private static final String BLOCK_CHAIN_NAME = "BLOCK_CHAIN_NAME";
   private static final int BLOCK_ORDER_1 = 1;
   private static final int BLOCK_ORDER_2 = 2;
   private static final int BLOCK_ORDER_3 = 3;
   private static final String BLOCK_1_CODE = "BLOCK1";
   private static final String BLOCK_2_CODE = "BLOCK2";
   private static final String BLOCK_3_CODE = "BLOCK3";
   private static final String BLOCK_1_NAME = BLOCK_1_CODE;
   private static final String BLOCK_2_NAME = BLOCK_2_CODE;
   private static final String BLOCK_3_NAME = BLOCK_3_CODE;
   private static final HumanResourceKey HR_KEY = HumanResourceKey.ADMIN;

   private GlobalParameters iGlobalParams;
   private Boolean iOrigIgnoreMisalignment;
   private RecordingEventBus iEventBus;


   /**
    * Verify that extending the deadline of the active block task of a block chain triggers the
    * re-zipping (assigning) of recurring requirement tasks to recurring block chain tasks.
    *
    * For the purposes of this test the config parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is
    * set to FALSE (its default setting), which causes each block chain task to be assigned one
    * requirement task. The requirement task must either have the same deadline as the block chain
    * task or must be the requirement task with earliest deadline after the block chain task's
    * deadline.
    *
    * Note: only one requirement task may be assigned to a block chain task so all other requirement
    * tasks remain unassigned.
    *
    * <pre>
    *    Example using recurring block chain every 3 days, recurring requirement every 2 days.
    *
    *                 Day: 01 02 03 04 05 06 07 08 09 10
    *    Block Chain task:       B1       B2       B3
    *          assignment:         \      |          \
    *    Requirement task:    R1    R2    R3    R4    R5
    *
    *    After deadline extension of 1 day for block tasks (req tasks not extended):
    *                 Day: 01 02 03 04 05 06 07 08 09 10
    *    Block Chain task:          B1       B2       B3
    *          assignment:          |          \      |
    *    Requirement task:    R1    R2    R3    R4    R5
    *
    *       R3 must be assigned to a block task with a deadline <= its own, so B1.
    *       But R2 is already assigned to B1 so R3 is left unassinged.
    *       Now R4 is assigned to B2.
    * </pre>
    *
    *
    * <pre>
    *    Given an activated recurring requirement definition using a calendar scheduling rule and scheduled from date
    *      And an activated recurring block chain definition using the same scheduled from date
    *          but a calendar scheduling rule with a larger frequency
    *      And an inventory applicable to both definitions and allowing synchronization
    *      And the configuration parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to FALSE (default)
    *      And requirement tasks based on the requirement definition
    *      And block tasks based on the block definitions for the block chain
    *      And each block task is assigned a requirement task with either the same deadline
    *          or the earliest deadline after the block chain task's deadline
    *     When the deadline of the active block chain task is extended
    *     Then each block chain task is assigned one requirement task with either
    *          the same deadline or the earliest deadline after the block chain task's deadline,
    *          which may be a different requirement task than originally assigned
    * </pre>
    */
   @Test
   public void
         itZipsRecurringReqTasksToRecurringBlockChainTasksWhenActiveBlockTaskDeadlineExtended()
               throws Exception {

      // Set up an aircraft assembly to be used to create the block chain and requirement
      // definitions against, as well as to create an aircraft.
      //
      // Note: we need to create a part due to a validation performed during task initialization,
      // which ensures the inventory part matches the the part number of the task definition's
      // assembly.
      final PartNoKey lAcftPart = Domain.createPart();
      final AssemblyKey lAcftAssy = createAcftAssyWithAcftPart( lAcftPart );
      final ConfigSlotKey lAcftRootConfigSlot = Domain.readRootConfigurationSlot( lAcftAssy );

      // Given an activated recurring requirement definition using a calendar scheduling rule and
      // scheduled from date. I.e. every 2 days.
      final int lReqInterval = 2;
      final TaskTaskKey lRequirementDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAcftRootConfigSlot );
                  aReqDefn.setRecurring( true );
                  aReqDefn.setStatus( ACTV );

                  aReqDefn.setScheduledFromEffectiveDate( NOW );
                  aReqDefn.setMinimumForecastRange( 6 );
                  aReqDefn.addSchedulingRule( CDY, lReqInterval );
               }
            } );

      // Given an activated recurring block chain definition using the same scheduled from date but
      // a calendar scheduling rule with a larger frequency. I.e. every 3 days.
      final int lBlockInterval = 3;
      final Map<Integer, TaskTaskKey> lBlockDefinitions =
            Domain.createBlockChainDefinition( new DomainConfiguration<BlockChainDefinition>() {

               @Override
               public void configure( BlockChainDefinition aBlockChainDefn ) {
                  aBlockChainDefn.setName( BLOCK_CHAIN_NAME );
                  aBlockChainDefn.setRecurring( true );
                  aBlockChainDefn.setConfigurationSlot( lAcftRootConfigSlot );
                  aBlockChainDefn.setStatus( ACTV );

                  aBlockChainDefn.addBlock( BLOCK_ORDER_1, BLOCK_1_CODE, BLOCK_1_NAME );
                  aBlockChainDefn.addBlock( BLOCK_ORDER_2, BLOCK_2_CODE, BLOCK_2_NAME );
                  aBlockChainDefn.addBlock( BLOCK_ORDER_3, BLOCK_3_CODE, BLOCK_3_NAME );

                  aBlockChainDefn.addRequirement( lRequirementDefinition, 1, 1 );

                  // Same scheduling as the requirement.
                  aBlockChainDefn.setScheduledFromEffectiveDate( NOW );
                  aBlockChainDefn.setMinimumForecastRange( 5 );
                  aBlockChainDefn.addRecurringSchedulingRule( CDY, lBlockInterval );
               }
            } );

      // Given an inventory applicable to both definitions and allowing synchronization.
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAcftAssy );
            aAircraft.setPart( lAcftPart );
            aAircraft.allowSynchronization();
         }
      } );

      // Given the configuration parameter BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT is set to FALSE.
      iGlobalParams.setBoolean( "BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT", false );

      // Given requirement tasks based on the requirement definition.
      List<TaskKey> lReqTasks = new ArrayList<TaskKey>();

      for ( int lIndex = 0; lIndex < 5; lIndex++ ) {

         final TaskKey lPrevTask = ( lIndex == 0 ) ? null : lReqTasks.get( lIndex - 1 );
         final RefEventStatusKey lStatus =
               ( lIndex == 0 ) ? RefEventStatusKey.ACTV : RefEventStatusKey.FORECAST;
         final RefSchedFromKey lSchedFrom = ( lIndex == 0 ) ? EFFECTIV : RefSchedFromKey.LASTDUE;

         // The deadline needs to be end-of-day and one interval after the previous task's
         // deadline.
         final Date lDeadline = ceilDay( addDays( NOW, ( lIndex + 1 ) * lReqInterval ) );

         lReqTasks.add( Domain.createRequirement( new DomainConfiguration<Requirement>() {

            @Override
            public void configure( Requirement aReq ) {
               aReq.setInventory( lAircraft );
               aReq.setDefinition( lRequirementDefinition );
               aReq.setStatus( lStatus );
               aReq.setPreviousTask( lPrevTask );
               aReq.addDeadline( new DomainConfiguration<Deadline>() {

                  @Override
                  public void configure( Deadline aDeadline ) {
                     aDeadline.setUsageType( CDY );
                     aDeadline.setScheduledFrom( lSchedFrom );
                     aDeadline.setInterval( lReqInterval );
                     aDeadline.setDueDate( lDeadline );
                  }
               } );
            }
         } ) );
      }

      // Given block tasks based on the block definitions for the block chain
      // and each block task is assigned a requirement task with either the same deadline
      // or the earliest deadline after the block chain task's deadline.
      //
      // Based on the scheduling rules and forecast range of a block task every 3 days and a req
      // task every 2 days then the blocks will be assigned the following reqs:
      //
      // Block1-Req2, Block2-Req3, Block3-Req5
      //
      List<TaskKey> lBlockTasks = new ArrayList<TaskKey>();

      for ( int lIndex = 0; lIndex < 3; lIndex++ ) {

         int lBlockNumber = lIndex + 1;
         final TaskTaskKey lBlockDefinition = lBlockDefinitions.get( lBlockNumber );

         final TaskKey lPrevTask = ( lIndex == 0 ) ? null : lBlockTasks.get( lIndex - 1 );
         final RefEventStatusKey lStatus =
               ( lIndex == 0 ) ? RefEventStatusKey.ACTV : RefEventStatusKey.FORECAST;
         final RefSchedFromKey lSchedFrom = ( lIndex == 0 ) ? EFFECTIV : RefSchedFromKey.LASTDUE;

         // The deadline needs to be end-of-day and one interval after the previous task's
         // deadline.
         final Date lDeadline = ceilDay( addDays( NOW, ( lIndex + 1 ) * lBlockInterval ) );

         // Get the REQ task to be assigned (convert number to index by subtracting 1).
         Integer lAssignedReqNumber = null;
         switch ( lBlockNumber ) {
            case 1:
               lAssignedReqNumber = 2;
               break;
            case 2:
               lAssignedReqNumber = 3;
               break;
            case 3:
               lAssignedReqNumber = 5;
               break;
         }
         final TaskKey lAssignedReqTask =
               ( lAssignedReqNumber != null ) ? lReqTasks.get( lAssignedReqNumber - 1 ) : null;

         lBlockTasks.add( Domain.createBlock( new DomainConfiguration<Block>() {

            @Override
            public void configure( Block aBlock ) {
               aBlock.setInventory( lAircraft );
               aBlock.setDefinition( lBlockDefinition );
               aBlock.setStatus( lStatus );
               aBlock.setPreviousBlock( lPrevTask );
               aBlock.addDeadline( new DomainConfiguration<Deadline>() {

                  @Override
                  public void configure( Deadline aDeadline ) {
                     aDeadline.setUsageType( CDY );
                     aDeadline.setScheduledFrom( lSchedFrom );
                     aDeadline.setInterval( lBlockInterval );
                     aDeadline.setDueDate( lDeadline );
                  }
               } );
               aBlock.addRequirement( lAssignedReqTask );
            }
         } ) );
      }

      // When the deadline of the active block chain task is extended.
      // (extending the deadline by 1 day)

      // Use the deadline of the req assigned to the first block as the earliest-deadline date.
      // (the same date used when extending the deadline via the UI when REQs)
      Date lEarliestDeadline =
            EvtSchedDeadTable.findByPrimaryKey( lReqTasks.get( 1 ), CDY ).getDeadlineDate();
      DeadlineExtensionTO lTO = new DeadlineExtensionTO();
      lTO.setEarliestDeadline( lEarliestDeadline );
      lTO.addCalendarUpdate( CDY, 1 );

      new DeadlineService( lBlockTasks.get( 0 ) ).extendDeadline( lTO, createHumanResource() );

      // Then each block chain task is assigned one requirement task with either
      // the same deadline or the earliest deadline after the block chain task's deadline,
      // which may be a different requirement task then originally assigned.
      //
      // Based on extending the active block task (and thus all the block tasks) by 1 day the
      // blocks
      // will be assigned the following reqs:
      //
      // Block1-Req2, *Block2-Req4*, Block3-Req5 (note Block2 has its REQ switched from 3 to 4)
      //
      assertEquals( "Unexpected block task for second req task.", lBlockTasks.get( 0 ),
            readHighestTask( lReqTasks.get( 1 ) ) );
      assertEquals( "Unexpected block task for fifth req task.", lBlockTasks.get( 1 ),
            readHighestTask( lReqTasks.get( 3 ) ) );
      assertEquals( "Unexpected block task for sixth req task.", lBlockTasks.get( 2 ),
            readHighestTask( lReqTasks.get( 4 ) ) );

      // Then all other requirement tasks are not assigned.
      assertEquals( "Unexpected block task for first req task.", lReqTasks.get( 0 ),
            readHighestTask( lReqTasks.get( 0 ) ) );
      assertEquals( "Unexpected block task for fourth req task.", lReqTasks.get( 2 ),
            readHighestTask( lReqTasks.get( 2 ) ) );
   }


   @Test
   public void setCalendarDeadline_withNullvalue_willremoveACTVandDendentFORECASTDeadlines()
         throws Exception {

      // assembly
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( aBuilder -> aBuilder.setCode( "ASSEMBLY" ) );

      // aircraft
      final InventoryKey lAircraft =
            Domain.createAircraft( aBuilder -> aBuilder.setAssembly( lAircraftAssembly ) );

      // fault
      final FaultKey lFault = Domain.createFault( aBuilder -> {
         aBuilder.setInventory( lAircraft );
         aBuilder.setStatus( RefEventStatusKey.CFACTV );
      } );

      // repetitive task ACTV with calendar deadlines
      final TaskKey lRepACTVTask = Domain.createRequirement( aTaskBuilder -> {
         aTaskBuilder.setStatus( RefEventStatusKey.ACTV );
         aTaskBuilder.setInventory( lAircraft );
         aTaskBuilder.setTaskClass( ADHOC );
         aTaskBuilder.setRecurrentSource( lFault );
         aTaskBuilder.addDeadline( aDeadlineBuilder -> {
            aDeadlineBuilder.setUsageType( CDY );
            aDeadlineBuilder.setScheduledFrom( EFFECTIV );
            aDeadlineBuilder.setInterval( 2 );
            aDeadlineBuilder.setDueDate( ceilDay( addDays( NOW, 2 ) ) );
         } );
      } );

      // repetitive task FORECAST with calendar deadlines
      final TaskKey lRepFORECASTTask = Domain.createRequirement( aTaskBuilder -> {
         aTaskBuilder.setStatus( RefEventStatusKey.FORECAST );
         aTaskBuilder.setInventory( lAircraft );
         aTaskBuilder.setTaskClass( ADHOC );
         aTaskBuilder.setRecurrentSource( lFault );
         aTaskBuilder.setPreviousTask( lRepACTVTask );
         aTaskBuilder.addDeadline( aDeadlineBuilder -> {
            aDeadlineBuilder.setUsageType( CDY );
            aDeadlineBuilder.setScheduledFrom( LASTDUE );
            aDeadlineBuilder.setInterval( 2 );
            aDeadlineBuilder.setDueDate( ceilDay( addDays( NOW, 2 * 2 ) ) );
         } );
      } );

      // remove calendar deadline
      new DeadlineService( lRepACTVTask ).setCalendarDeadline( null, HR_KEY, true );

      // assert usage was removed
      EvtSchedDeadTable lRepACTVTaskTable = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( lRepACTVTask.getEventKey(), CDY ) );
      EvtSchedDeadTable lRepForecastTaskTable = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( lRepFORECASTTask.getEventKey(), CDY ) );

      assertFalse( lRepACTVTaskTable.exists() );
      assertFalse( lRepForecastTaskTable.exists() );

      // assert stage note was added
      new EvtStage( lRepACTVTask.getEventKey() ).assertCount( 1 );
      new EvtStage( lRepFORECASTTask.getEventKey() ).assertCount( 1 );
   }


   @Test
   public void removeUsageDeadline_forACTVTask_willDeleteDependentFORECAST() throws Exception {

      // data setup
      final Integer lOriginalDeadlineInterval = 10;
      final Integer lOriginalStartValue = 0;

      // assembly
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( aBuilder -> aBuilder.setCode( "ASSEMBLY" ) );

      // aircraft
      final InventoryKey lAircraft =
            Domain.createAircraft( aBuilder -> aBuilder.setAssembly( lAircraftAssembly ) );

      // fault
      final FaultKey lFault = Domain.createFault( aBuilder -> {
         aBuilder.setInventory( lAircraft );
         aBuilder.setStatus( RefEventStatusKey.CFACTV );
      } );

      // repetitive task ACTV with usage deadline
      final TaskKey lRepACTVTask = Domain.createRequirement( aTaskBuilder -> {
         aTaskBuilder.setStatus( RefEventStatusKey.ACTV );
         aTaskBuilder.setInventory( lAircraft );
         aTaskBuilder.setTaskClass( ADHOC );
         aTaskBuilder.setRecurrentSource( lFault );
         aTaskBuilder.addDeadline( aDeadlineBuilder -> {
            aDeadlineBuilder.setScheduledFrom( CUSTOM );
            aDeadlineBuilder.setInterval( lOriginalDeadlineInterval );
            aDeadlineBuilder.setStartTsn( lOriginalStartValue );
            aDeadlineBuilder.setDueValue( lOriginalStartValue + lOriginalDeadlineInterval );
            aDeadlineBuilder.setUsageType( CYCLES );
         } );
      } );

      // repetitive task FORECAST with usage deadline
      final TaskKey lRepFORECASTTask = Domain.createRequirement( aTaskBuilder -> {
         aTaskBuilder.setStatus( RefEventStatusKey.FORECAST );
         aTaskBuilder.setInventory( lAircraft );
         aTaskBuilder.setTaskClass( ADHOC );
         aTaskBuilder.setRecurrentSource( lFault );
         aTaskBuilder.setPreviousTask( lRepACTVTask );
         aTaskBuilder.addDeadline( aDeadlineBuilder -> {
            aDeadlineBuilder.setScheduledFrom( CUSTOM );
            aDeadlineBuilder.setInterval( lOriginalDeadlineInterval );
            aDeadlineBuilder.setStartTsn( lOriginalStartValue );
            aDeadlineBuilder.setDueValue( lOriginalStartValue + lOriginalDeadlineInterval );
            aDeadlineBuilder.setUsageType( CYCLES );
         } );
      } );

      // remove usage deadline
      new DeadlineService( lRepACTVTask ).removeUsageDeadline( CYCLES, HR_KEY, false );

      // assert usage were removed for both, ACTV and FORECAST tasks
      EvtSchedDeadTable lRepACTVTaskTable = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( lRepACTVTask.getEventKey(), CYCLES ) );
      EvtSchedDeadTable lRepForecastTaskTable = EvtSchedDeadTable
            .findByPrimaryKey( new EventDeadlineKey( lRepFORECASTTask.getEventKey(), CYCLES ) );

      assertFalse( lRepACTVTaskTable.exists() );
      assertFalse( lRepForecastTaskTable.exists() );

      // assert stage note was added
      new EvtStage( lRepACTVTask.getEventKey() ).assertCount( 1 );
      new EvtStage( lRepFORECASTTask.getEventKey() ).assertCount( 1 );
   }


   @Test
   public void whenRemoveReqTaskDeadlinesThenTaskDeadlinesRemovedEventShouldBePublished()
         throws Exception {
      // ARRANGE
      final PartNoKey aircraftPartKey = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( configurationSlot -> {
            configurationSlot.addPartGroup( partGroup -> {
               partGroup.setInventoryClass( RefInvClassKey.ACFT );
               partGroup.addPart( aircraftPartKey );
            } );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
         reqDefinition.addSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
      } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setInventory( aircraftKey );
         requirement.setDefinition( reqDefinitionKey );
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.addUsageDeadline( DataTypeKey.HOURS, BigDecimal.TEN, null );
         Date today = new Date();
         final Date dueDate = DateUtils.addTimeDays( today, 11 );
         requirement.addCalendarDeadline( DataTypeKey.CDY, BigDecimal.TEN, dueDate );
      } );

      com.mxi.mx.core.services.stask.deadline.Deadline[] deadlines =
            new com.mxi.mx.core.services.stask.deadline.Deadline[0];

      // ACT
      new DeadlineService( taskKey ).modifyDeadlines( deadlines, HR_KEY, false );

      // ASSERT
      Set<DataTypeKey> dataTypeKeysRemoved = new HashSet<DataTypeKey>();
      dataTypeKeysRemoved.add( DataTypeKey.CDY );
      dataTypeKeysRemoved.add( DataTypeKey.HOURS );
      assertEquals( new TaskDeadlinesRemovedEvent( taskKey, dataTypeKeysRemoved, HR_KEY ), iEventBus
            .getEventMessages().get( iEventBus.getEventMessages().size() - 1 ).getPayload() );
   }


   @Test
   public void whenRemoveRepetitiveTaskDeadlinesThenTaskDeadlinesRemovedEventShouldBePublished()
         throws Exception {
      // ARRANGE
      final PartNoKey aircraftPartKey = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( configurationSlot -> {
            configurationSlot.addPartGroup( partGroup -> {
               partGroup.setInventoryClass( RefInvClassKey.ACFT );
               partGroup.addPart( aircraftPartKey );
            } );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
      } );

      // fault
      final FaultKey faultKey = Domain.createFault( fault -> {
         fault.setInventory( aircraftKey );
         fault.setStatus( RefEventStatusKey.CFACTV );
      } );

      final Integer deadlineInterval = 15;
      final Integer startValue = 2;

      // repetitive active task with calendar and usage deadlines
      final TaskKey activeTaskKey = Domain.createRequirement( task -> {
         task.setStatus( RefEventStatusKey.ACTV );
         task.setInventory( aircraftKey );
         task.setTaskClass( ADHOC );
         task.setRecurrentSource( faultKey );
         Date today = new Date();
         final Date dueDate = DateUtils.addTimeDays( today, 11 );
         task.addCalendarDeadline( DataTypeKey.CDY, BigDecimal.TEN, dueDate );
         task.addDeadline( deadline -> {
            deadline.setScheduledFrom( CUSTOM );
            deadline.setInterval( deadlineInterval );
            deadline.setStartTsn( startValue );
            deadline.setDueValue( startValue + deadlineInterval );
            deadline.setUsageType( DataTypeKey.HOURS );
         } );
      } );

      // repetitive forecast task with calendar and usage deadline
      final TaskKey lForecastTaskKey = Domain.createRequirement( task -> {
         task.setStatus( RefEventStatusKey.FORECAST );
         task.setInventory( aircraftKey );
         task.setTaskClass( ADHOC );
         task.setRecurrentSource( faultKey );
         task.setPreviousTask( activeTaskKey );
         task.addDeadline( deadline -> {
            deadline.setScheduledFrom( CUSTOM );
            deadline.setInterval( deadlineInterval );
            deadline.setStartTsn( startValue );
            deadline.setDueValue( startValue + deadlineInterval );
            deadline.setUsageType( DataTypeKey.HOURS );
         } );
      } );

      com.mxi.mx.core.services.stask.deadline.Deadline[] deadlines =
            new com.mxi.mx.core.services.stask.deadline.Deadline[0];

      // ACT
      new DeadlineService( activeTaskKey ).modifyDeadlines( deadlines, HR_KEY, false );

      // ASSERT
      assertTrue( iEventBus.getEventMessages().size() == 2 );

      TaskKey ExpectedTaskKey = activeTaskKey;
      Set<DataTypeKey> ExpectedDataTypeKeysRemoved = new HashSet<DataTypeKey>();
      ExpectedDataTypeKeysRemoved.add( DataTypeKey.HOURS );
      ExpectedDataTypeKeysRemoved.add( DataTypeKey.CDY );
      TaskDeadlinesRemovedEvent taskDeadlinesRemovedEvent = ( TaskDeadlinesRemovedEvent ) iEventBus
            .getEventMessages().get( iEventBus.getEventMessages().size() - 1 ).getPayload();
      TaskKey actualTaskKey = taskDeadlinesRemovedEvent.getTaskKey();

      Set<DataTypeKey> actualDataTypeKeys = taskDeadlinesRemovedEvent.getDataTypeKeys();
      assertEquals( ExpectedTaskKey, actualTaskKey );
      assertTrue( ExpectedDataTypeKeysRemoved.containsAll( actualDataTypeKeys ) );

      taskDeadlinesRemovedEvent = ( TaskDeadlinesRemovedEvent ) iEventBus.getEventMessages()
            .get( iEventBus.getEventMessages().size() - 2 ).getPayload();
      actualTaskKey = taskDeadlinesRemovedEvent.getTaskKey();
      ExpectedTaskKey = lForecastTaskKey;
      actualDataTypeKeys = taskDeadlinesRemovedEvent.getDataTypeKeys();
      assertEquals( ExpectedTaskKey, actualTaskKey );
      assertTrue( ExpectedDataTypeKeysRemoved.containsAll( actualDataTypeKeys ) );
   }


   @Before
   public void before() {
      iGlobalParams = GlobalParameters.getInstance( "LOGIC" );
      iOrigIgnoreMisalignment = iGlobalParams.getBool( "BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT" );
      iEventBus = iInjectionOverrideRule.select( RecordingEventBus.class ).get();
   }


   @After
   public void after() {
      iGlobalParams.setBoolean( "BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT", iOrigIgnoreMisalignment );
   }


   private AssemblyKey createAcftAssyWithAcftPart( final PartNoKey aAircraftPart ) {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAcftAssy ) {
            aAcftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aRootConfigSlot ) {
                  aRootConfigSlot.addPartGroup( new DomainConfiguration<PartGroup>() {

                     @Override
                     public void configure( PartGroup aRootCsPartGroup ) {
                        aRootCsPartGroup.setInventoryClass( ACFT );
                        aRootCsPartGroup.addPart( aAircraftPart );
                     }
                  } );
               }
            } );
         }
      } );
   }


   private TaskKey readHighestTask( TaskKey aTask ) {
      final SchedStaskDao lSchedStaskDao =
            InjectorContainer.get().getInstance( SchedStaskDao.class );
      final SchedStaskTable lSchedStaskTable = lSchedStaskDao.findByPrimaryKey( aTask );
      return lSchedStaskTable.getHTaskKey();

   }

}
