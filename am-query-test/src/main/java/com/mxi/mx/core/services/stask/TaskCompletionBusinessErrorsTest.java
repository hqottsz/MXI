package com.mxi.mx.core.services.stask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.misc.TransactionHandler;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.WoLineKey;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.services.stask.details.DetailsService;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sched.SchedWOLineTable;


@RunWith( BlockJUnit4ClassRunner.class )
public class TaskCompletionBusinessErrorsTest {

   private static final LocationKey OTTAWA_LINE = new LocationKey( "4650:400" );

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

   private TaskBean iTaskBean;

   private static int iWorkOrderLineNo = 1;


   @Before
   public void setUp() {

      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      iAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
         }
      } );

      iCompleteService = new CompleteService();
      iTxHandler = new TransactionHandler( new SessionContextFake().getUserTransaction() );
      iHr = new HumanResourceDomainBuilder().build();

      int userId = OrgHr.findByPrimaryKey( iHr ).getUserId();
      UserParameters.setInstance( userId, "LOGIC", new UserParametersFake( userId, "LOGIC" ) );

      iTaskBean = new TaskBean();
      iTaskBean.setSessionContext( new SessionContextFake() );
   }


   private void setTaskCompletionBusinessError( TaskKey aWorkPackage, TaskKey aTask,
         String aError ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aWorkPackage, "aWpSchedDbId", "aWpSchedId" );
      lArgs.add( aTask, "aTaskSchedDbId", "aTaskSchedId" );

      lArgs.add( "aErrorMsg", aError );
      lArgs.add( "aErrorDt", new Date() );

      MxDataAccess.getInstance()
            .execute( "com.mxi.mx.core.query.task.complete.SetCompleteTaskBusinessErrors", lArgs );
   }


   /**
    *
    * This test verifies that workscope type errors are counted separate from non workscope
    *
    * @throws Throwable
    */
   @Test
   public void testWorkScopeVSNonWorkscopeBusinessErrors() throws Throwable {

      // Set up a work package with 2 JICs, 1 adhoc task and a deferred fault
      TaskKey lWorkPackage = createWorkPackage();
      TaskKey lParentTask = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lSubtask1 =
            createSubTaskInWorkPackage( lWorkPackage, lParentTask, RefTaskClassKey.ADHOC );
      addTaskToWorkPackageWorkScope( lWorkPackage, lSubtask1 );
      TaskKey lSubtask2 =
            createSubTaskInWorkPackage( lWorkPackage, lParentTask, RefTaskClassKey.ADHOC );
      addTaskToWorkPackageWorkScope( lWorkPackage, lSubtask2 );

      // Mark a failure against a non workscope task
      setTaskCompletionBusinessError( lWorkPackage, lParentTask, "Some Error" );

      assertBusinessErrorWorkscopeStatus( lParentTask );

      // Mark a failure a workscope task
      setTaskCompletionBusinessError( lWorkPackage, lSubtask1, "Some Error" );

      // Check that the setTaskCompletionBusinessError logic properly detected the task as being a
      // workscope task
      assertBusinessErrorWorkscopeStatus( lSubtask1 );

      // Only the workscope error should be seen. not both.
      assertTaskCompleteErrorCount( lWorkPackage, 1 );

      List<TaskKey> lSelectedTasks = new ArrayList<>();
      {
         lSelectedTasks.add( lSubtask1 );
      }

      // This should clear the workscope error but not complete the parent task
      iCompleteService.batchCompleteTasks( lSelectedTasks, new Date(), iHr, iTxHandler );

      assertComplete( lSubtask1 );

      // Only the non-workscope error should be seen. now
      assertTaskCompleteErrorCount( lWorkPackage, 1 );

      lSelectedTasks = new ArrayList<>();
      {
         lSelectedTasks.add( lParentTask );
      }

      // This should clear the non-workscope error
      iCompleteService.batchCompleteTasks( lSelectedTasks, new Date(), iHr, iTxHandler );

      assertComplete( lParentTask );

      // no errors should remain
      assertTaskCompleteErrorCount( lWorkPackage, 0 );

   }


   @Test
   public void testClearBusinessErrorsOnTaskCompletion() throws Throwable {

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

      // Mark a failure against JIC1
      setTaskCompletionBusinessError( lWorkPackage, lJIC1, "Some Error" );

      // Mark a failure against JIC2
      setTaskCompletionBusinessError( lWorkPackage, lJIC2, "Some Error" );

      // Mark a failure against the parent task
      setTaskCompletionBusinessError( lWorkPackage, lParentTask, "Some Error" );

      // Mark a failure against the fault
      setTaskCompletionBusinessError( lWorkPackage, lCorrectiveTask, "Some Error" );

      assertTaskCompleteErrorCount( lWorkPackage, 4 );

      // This should clear the above errors
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

      // The failures should be clear now except for 1 (JIC2)
      assertTaskCompleteErrorCount( lWorkPackage, 1 );

      // Add another parent and child task, and post an error on the JIC.
      TaskKey lNewParentTask = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.ADHOC );
      TaskKey lNewSubtask =
            createSubTaskInWorkPackage( lWorkPackage, lNewParentTask, RefTaskClassKey.ADHOC );

      // Mark a failure against the parent task
      setTaskCompletionBusinessError( lWorkPackage, lNewParentTask, "Some Error" );

      // Mark a failure against the sub task
      setTaskCompletionBusinessError( lWorkPackage, lNewSubtask, "Some Error" );

      assertTaskCompleteErrorCount( lWorkPackage, 3 );

      // Unassign the parent task (which till take the child with it). This should clear the
      // business errors on both tasks
      iTaskBean.unassignTask( lNewParentTask, iHr, null, null );

      // Both error counts should now be at 1 still
      assertTaskCompleteErrorCount( lWorkPackage, 1 );

      // Add an error to the now completed JIC2
      setTaskCompletionBusinessError( lWorkPackage, lJIC2, "Some Error" );

      // terminate the task which should clear the completion error
      iTaskBean.terminate( lJIC2, iHr, null, null );

      assertTaskCompleteErrorCount( lWorkPackage, 0 );

      // complete the work package
      iCompleteService = new CompleteService( lWorkPackage );
      iCompleteService.batchCompleteAll( new Date(), iHr, iTxHandler );

      // Both error counts should now be at 0
      assertTaskCompleteErrorCount( lWorkPackage, 0 );

      // Add an error to the now completed JIC2
      setTaskCompletionBusinessError( lWorkPackage, lJIC2, "Some Error" );

      assertTaskCompleteErrorCount( lWorkPackage, 1 );

      // Mark the task as error which should clear the completion error
      iTaskBean.markAsError( lJIC2, iHr, null, null );

      // Both error counts should now be at 0
      assertTaskCompleteErrorCount( lWorkPackage, 0 );

      // Add an error to now completed JIC1,JIC2,ParentTask)
      setTaskCompletionBusinessError( lWorkPackage, lJIC1, "Some Error" );
      setTaskCompletionBusinessError( lWorkPackage, lJIC2, "Some Error" );
      setTaskCompletionBusinessError( lWorkPackage, lParentTask, "Some Error" );

      // Both error counts should now be at 3
      assertTaskCompleteErrorCount( lWorkPackage, 3 );

      // Close the work package which should clear all errors
      iCompleteService.completeCheck( iHr, new Date() );

      // Both error counts should now be at 0
      assertTaskCompleteErrorCount( lWorkPackage, 0 );

   }


   /**
    *
    * Checks that the workscope bool has been correctly set for a task error when PL/SQL Logic in
    * SetCompleteTaskBusinessError.prx was run
    *
    * @param aTask
    */
   private void assertBusinessErrorWorkscopeStatus( TaskKey aTask ) {
      boolean lTaskHasWorkscopeLine = false;

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "sched_db_id", "sched_id" );
      DataSet lDataSet =
            MxDataAccess.getInstance().executeQuery( "sched_wo_line", lArgs, "sched_id" );
      if ( lDataSet.next() ) {
         lTaskHasWorkscopeLine = true;
      }

      lArgs.clear();
      lArgs.add( aTask, "task_sched_db_id", "task_sched_id" );
      lDataSet =
            MxDataAccess.getInstance().executeQuery( "sched_wp_error", lArgs, "workscope_bool" );
      if ( lDataSet.next() ) {
         assertTrue( lTaskHasWorkscopeLine == ( lDataSet.getInt( "workscope_bool" ) == 1 ) );
         return;
      }
      fail( "completion business error workscope boolean is not correctly set" );
   }


   private void assertTaskCompleteErrorCount( TaskKey aWorkPackage, int aExpectedCount ) {
      assertEquals( aExpectedCount,
            new DetailsService( aWorkPackage ).getBatchCompletionErrorCount() );
      assertEquals( aExpectedCount,
            getWorkPackageTaskBusinessErrors( aWorkPackage ).getRowCount() );
   }


   private DataSet getWorkPackageTaskBusinessErrors( TaskKey aWorkPackage ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aWorkPackage, "aRootDbId", "aRootId" );

      // execute the query
      return MxDataAccess.getInstance().executeQuery(
            "com.mxi.mx.core.query.task.complete.GetWorkPackageTaskBusinessErrors", lArgs );
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
            aBuilder.setLocation( OTTAWA_LINE );
         }
      } );
   }


   private void addTaskToWorkPackageWorkScope( TaskKey aWorkPackage, TaskKey aTask ) {
      WoLineKey aWoLineKey =
            new WoLineKey( aWorkPackage.getDbId(), aWorkPackage.getId(), iWorkOrderLineNo );

      SchedWOLineTable lWorkOrderLineTable = SchedWOLineTable.create( aWoLineKey );
      lWorkOrderLineTable.setSchedKey( aTask );
      lWorkOrderLineTable.setCollectedBool( false );
      lWorkOrderLineTable.insert();
      iWorkOrderLineNo++;
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
