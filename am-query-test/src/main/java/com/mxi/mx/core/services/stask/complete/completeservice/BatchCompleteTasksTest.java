
package com.mxi.mx.core.services.stask.complete.completeservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.misc.TransactionHandler;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.TaskErrorHolder;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


@RunWith( BlockJUnit4ClassRunner.class )
public class BatchCompleteTasksTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // Object under test
   private CompleteService iCompleteService;

   private InventoryKey iAircraft;
   private HumanResourceKey iHr;
   private TransactionHandler iTxHandler;


   @Before
   public void setUp() {
      iAircraft = Domain.createAircraft();
      iCompleteService = new CompleteService();
      iTxHandler = new TransactionHandler( new SessionContextFake().getUserTransaction() );
      iHr = new HumanResourceDomainBuilder().build();
   }


   @Test
   public void testBatchCompletion() throws Throwable {

      // Set up a work package with 2 JICs, 1 adhoc task and a deferred fault
      TaskKey lWorkPackage = createWorkPackage();
      TaskKey lJIC1 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lJIC2 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lParentTask = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.ADHOC );
      TaskKey lSubtask =
            createSubTaskInWorkPackage( lWorkPackage, lParentTask, RefTaskClassKey.ADHOC );
      TaskKey lCorrectiveTask = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.CORR );
      FaultKey lDeferredFault =
            createFaultWithCorrectiveTask( lCorrectiveTask, RefEventStatusKey.CFDEFER );

      // Select JIC1, Adhoc task with subtask and deferred fault for batch completion
      List<TaskKey> lSelectedTasks = new ArrayList<>();
      {
         lSelectedTasks.add( lJIC1 );
         lSelectedTasks.add( lParentTask );
         lSelectedTasks.add( lCorrectiveTask );
      }

      iCompleteService.batchCompleteTasks( lSelectedTasks, new Date(), iHr, iTxHandler );

      // Make sure tasks selected for batch completion are now complete
      assertComplete( lJIC1 );
      assertComplete( lParentTask );
      assertComplete( lSubtask );
      assertComplete( lCorrectiveTask );

      // Make sure tasks in WP not selected for batch completion remain incomplete
      assertNotComplete( lJIC2 );

      // The fault should now be certified
      assertEquals( RefEventStatusKey.CFCERT, EvtEventTable
            .findByPrimaryKey( new EventKey( lDeferredFault.toString() ) ).getEventStatus() );
   }


   /**
    *
    * This test ensures that if you only want to complete one JIC, the neighbour JIC is not also
    * completed
    *
    * @throws Throwable
    */
   @Test
   public void testBatchCompletionSingleJic() throws Throwable {

      // Set up a work package with 2 JICs, 1 adhoc task and a deferred fault
      TaskKey lWorkPackage = createWorkPackage();
      TaskKey lParentTask = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.ADHOC );
      TaskKey lSubtask1 =
            createSubTaskInWorkPackage( lWorkPackage, lParentTask, RefTaskClassKey.ADHOC );
      TaskKey lSubtask2 =
            createSubTaskInWorkPackage( lWorkPackage, lParentTask, RefTaskClassKey.ADHOC );

      List<TaskKey> lSelectedTasks = new ArrayList<>();
      {
         lSelectedTasks.add( lSubtask1 );
      }

      iCompleteService.batchCompleteTasks( lSelectedTasks, new Date(), iHr, iTxHandler );

      // Make sure tasks selected for batch completion are now complete
      assertComplete( lSubtask1 );

      // Make sure tasks in WP not selected for batch completion remain incomplete
      assertNotComplete( lParentTask );
      assertNotComplete( lSubtask2 );

   }


   @Test
   public void testBatchCompletion_partialFailure() throws Throwable {

      // Set up a work package with 1 task assigned and a loose task
      TaskKey lWorkPackage = createWorkPackage();
      TaskKey lTaskInWP = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.ADHOC );
      TaskKey lLooseTask = createTaskInWorkPackage( null, RefTaskClassKey.ADHOC );

      // SelectAdhoc task for batch completion
      List<TaskKey> lSelectedTasks = new ArrayList<>();
      {
         lSelectedTasks.add( lTaskInWP );
         lSelectedTasks.add( lLooseTask );
      }

      List<TaskErrorHolder> lBatchCompleteErrors =
            iCompleteService.batchCompleteTasks( lSelectedTasks, new Date(), iHr, iTxHandler );

      assertFalse( lBatchCompleteErrors.isEmpty() );

      assertComplete( lTaskInWP );
      assertNotComplete( lLooseTask );

   }


   @Test
   public void testBatchCompletion_taskWithPartReq_noPartNoError() throws Throwable {
      // Set up a work package with 1 task
      TaskKey lWorkPackage = createWorkPackage();
      TaskKey lTaskWithPartReq = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.ADHOC );

      // Add a part requirement to the task
      createPartRequirementForTask( lTaskWithPartReq );

      List<TaskKey> lSelectedTasks = new ArrayList<>();
      lSelectedTasks.add( lTaskWithPartReq );

      List<TaskErrorHolder> lBatchCompleteErrors =
            iCompleteService.batchCompleteTasks( lSelectedTasks, new Date(), iHr, iTxHandler );
      assertFalse( lBatchCompleteErrors.isEmpty() );
      assertNotComplete( lTaskWithPartReq );

   }


   private void assertComplete( TaskKey aTask ) {
      assertEquals( RefEventStatusKey.COMPLETE,
            EvtEventTable.findByPrimaryKey( new EventKey( aTask.toString() ) ).getEventStatus() );
   }


   private void assertNotComplete( TaskKey aTask ) {
      assertNotEquals( RefEventStatusKey.COMPLETE,
            EvtEventTable.findByPrimaryKey( new EventKey( aTask.toString() ) ).getEventStatus() );
   }


   private TaskKey createWorkPackage() {
      return Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setStatus( RefEventStatusKey.IN_WORK );
            aBuilder.setAircraft( iAircraft );
         }
      } );
   }


   private TaskKey createTaskInWorkPackage( TaskKey aWorkPackage, RefTaskClassKey aTaskClass ) {
      return new TaskBuilder().withParentTask( aWorkPackage ).withHighestTask( aWorkPackage )
            .withTaskClass( aTaskClass ).withStatus( RefEventStatusKey.ACTV )
            .onInventory( new InventoryBuilder().withClass( RefInvClassKey.TRK )
                  .withParentInventory( iAircraft ).build() )
            .build();
   }


   private TaskKey createSubTaskInWorkPackage( TaskKey aWorkPackage, TaskKey aParentTask,
         RefTaskClassKey aTaskClass ) {
      return new TaskBuilder().withParentTask( aParentTask ).withHighestTask( aWorkPackage )
            .withTaskClass( aTaskClass ).withStatus( RefEventStatusKey.ACTV ).build();
   }


   private FaultKey createFaultWithCorrectiveTask( final TaskKey aCorrectiveTask,
         final RefEventStatusKey aStatus ) {

      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aBuilder ) {
            aBuilder.setInventory( iAircraft );
            aBuilder.setFoundOnDate( new Date() );
            aBuilder.setCorrectiveTask( aCorrectiveTask );
            aBuilder.setStatus( aStatus );
         }

      } );

      SchedStaskTable.findByPrimaryKey( aCorrectiveTask ).setFault( lFault );

      return lFault;
   }


   private void createPartRequirementForTask( TaskKey aTaskWithPartReq ) {
      final PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "PARTGROUP" )
            .withInventoryClass( RefInvClassKey.TRK ).build();

      new PartRequirementDomainBuilder( aTaskWithPartReq ).forPartGroup( lPartGroupKey )
            .withInstallQuantity( 1 ).build();
   }

}
