package com.mxi.mx.core.services.stask.complete.completeservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.misc.TransactionHandler;
import com.mxi.mx.common.services.work.WorkItem;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.complete.BatchCompleteTasks;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.services.stask.complete.CompleteTask;
import com.mxi.mx.core.services.stask.details.DetailsService;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * This class tests the behavior of the progress calculation during a batch completion of one or
 * more tasks
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class BatchCompleteProgressTest {

   private static final LocationKey OTTAWA_LINE = new LocationKey( "4650:400" );

   private static final int BATCH_COMPLETE_ID = 1;

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

   private static int workItemId = 1;


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


   /**
    *
    * This will test the progress milestones. This is the number of child work items (task trees)
    * which are being completed in the batch. This is the course grained progress data that has to
    * reach 100% (all milestones complete) every time.
    *
    * @throws Throwable
    */
   @Test
   public void testBatchCompleteProgressMilestones() throws Throwable {

      // Set up a work package with 2 JICs, 1 adhoc task and a deferred fault
      TaskKey lWorkPackage = createWorkPackage();
      TaskKey lJIC1 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lJIC2 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lJIC3 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lJIC4 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lJIC5 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lJIC6 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lJIC7 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );
      TaskKey lJIC8 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.JIC );

      List<TaskKey> lSelectedTasks = new ArrayList<>();
      {
         lSelectedTasks.add( lJIC1 );
         lSelectedTasks.add( lJIC2 );
         lSelectedTasks.add( lJIC3 );
         lSelectedTasks.add( lJIC4 );
         lSelectedTasks.add( lJIC5 );
         lSelectedTasks.add( lJIC6 );
         lSelectedTasks.add( lJIC7 );
         lSelectedTasks.add( lJIC8 );
      }

      createBatchCompleteWorkItems( lWorkPackage, lSelectedTasks );

      List<TaskKey> lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lJIC1 );
         lCompletedTasks.add( lJIC2 );
      }

      markCompleteTaskWorkItemAsComplete( lCompletedTasks );

      // We should be at exactly 25% complete
      assertBatchCompleteProgress( lWorkPackage, 25 );

      lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lJIC3 );
         lCompletedTasks.add( lJIC4 );
      }

      markCompleteTaskWorkItemAsComplete( lCompletedTasks );

      // We should be at exactly 50% complete
      assertBatchCompleteProgress( lWorkPackage, 50 );

      lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lJIC5 );
         lCompletedTasks.add( lJIC6 );
      }

      markCompleteTaskWorkItemAsComplete( lCompletedTasks );

      // We should be at exactly 75% complete
      assertBatchCompleteProgress( lWorkPackage, 75 );

      lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lJIC7 );
         lCompletedTasks.add( lJIC8 );
      }

      markCompleteTaskWorkItemAsComplete( lCompletedTasks );

      // We should be at exactly 100% complete
      assertBatchCompleteProgress( lWorkPackage, 100 );

   }


   /**
    *
    * This will test the additional progress data provided by actual task completion within the
    * milestones. (How many tasks in the task tree are completed)
    *
    * @throws Throwable
    */
   @Test
   public void testBatchCompleteProgressMilestonesWithActualProgress() throws Throwable {

      // Set up a work package with 2 JICs, 1 adhoc task and a deferred fault
      TaskKey lWorkPackage = createWorkPackage();
      TaskKey lREQ1 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lJIC1a = createSubTaskInWorkPackage( lWorkPackage, lREQ1, RefTaskClassKey.JIC );
      TaskKey lJIC1b = createSubTaskInWorkPackage( lWorkPackage, lREQ1, RefTaskClassKey.JIC );
      TaskKey lREQ2 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lJIC2a = createSubTaskInWorkPackage( lWorkPackage, lREQ2, RefTaskClassKey.JIC );
      TaskKey lJIC2b = createSubTaskInWorkPackage( lWorkPackage, lREQ2, RefTaskClassKey.JIC );
      TaskKey lREQ3 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lJIC3a = createSubTaskInWorkPackage( lWorkPackage, lREQ3, RefTaskClassKey.JIC );
      TaskKey lJIC3b = createSubTaskInWorkPackage( lWorkPackage, lREQ3, RefTaskClassKey.JIC );
      TaskKey lREQ4 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lJIC4a = createSubTaskInWorkPackage( lWorkPackage, lREQ4, RefTaskClassKey.JIC );
      TaskKey lJIC4b = createSubTaskInWorkPackage( lWorkPackage, lREQ4, RefTaskClassKey.JIC );
      TaskKey lREQ5 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lJIC5a = createSubTaskInWorkPackage( lWorkPackage, lREQ5, RefTaskClassKey.JIC );
      TaskKey lJIC5b = createSubTaskInWorkPackage( lWorkPackage, lREQ5, RefTaskClassKey.JIC );
      TaskKey lREQ6 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lJIC6a = createSubTaskInWorkPackage( lWorkPackage, lREQ6, RefTaskClassKey.JIC );
      TaskKey lJIC6b = createSubTaskInWorkPackage( lWorkPackage, lREQ6, RefTaskClassKey.JIC );
      TaskKey lREQ7 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lJIC7a = createSubTaskInWorkPackage( lWorkPackage, lREQ7, RefTaskClassKey.JIC );
      TaskKey lJIC7b = createSubTaskInWorkPackage( lWorkPackage, lREQ7, RefTaskClassKey.JIC );
      TaskKey lREQ8 = createTaskInWorkPackage( lWorkPackage, RefTaskClassKey.REQ );
      TaskKey lJIC8a = createSubTaskInWorkPackage( lWorkPackage, lREQ8, RefTaskClassKey.JIC );
      TaskKey lJIC8b = createSubTaskInWorkPackage( lWorkPackage, lREQ8, RefTaskClassKey.JIC );

      List<TaskKey> lSelectedTasks = new ArrayList<>();
      {
         lSelectedTasks.add( lREQ1 );
         lSelectedTasks.add( lREQ2 );
         lSelectedTasks.add( lREQ3 );
         lSelectedTasks.add( lREQ4 );
         lSelectedTasks.add( lREQ5 );
         lSelectedTasks.add( lREQ6 );
         lSelectedTasks.add( lREQ7 );
         lSelectedTasks.add( lREQ8 );
      }

      // Generate batch complete work items for all of the root tasks in the work package
      createBatchCompleteWorkItems( lWorkPackage, lSelectedTasks );

      List<TaskKey> lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lREQ1 );
         lCompletedTasks.add( lREQ2 );
         lCompletedTasks.add( lREQ3 );
         lCompletedTasks.add( lREQ4 );
      }
      // Complete half of the root level tasks
      iCompleteService.batchCompleteTasks( lCompletedTasks, new Date(), iHr, iTxHandler );
      markCompleteTaskWorkItemAsComplete( lCompletedTasks );

      assertComplete( lREQ1 );
      assertComplete( lREQ2 );
      assertComplete( lREQ3 );
      assertComplete( lREQ4 );

      assertNotComplete( lREQ5 );
      assertNotComplete( lREQ6 );
      assertNotComplete( lREQ7 );
      assertNotComplete( lREQ8 );

      // We should be at exactly 50% complete
      assertBatchCompleteProgress( lWorkPackage, 50 );

      lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lJIC5a );
         lCompletedTasks.add( lJIC6a );
         lCompletedTasks.add( lJIC7a );
      }
      // Batch complete half of the remaining the sub JIC's
      iCompleteService.batchCompleteTasks( lCompletedTasks, new Date(), iHr, iTxHandler );

      // Make sure tasks selected for batch completion are now complete
      assertComplete( lJIC5a );
      assertComplete( lJIC6a );
      assertComplete( lJIC7a );

      // Make sure tasks in WP not selected for batch completion remain incomplete
      assertNotComplete( lREQ5 );
      assertNotComplete( lREQ6 );
      assertNotComplete( lREQ7 );
      assertNotComplete( lREQ8 );
      assertNotComplete( lJIC5b );
      assertNotComplete( lJIC6b );
      assertNotComplete( lJIC7b );
      assertNotComplete( lJIC8b );

      // We should be at exactly 63% complete. This is because half the milestones are complete
      // (milestones take priority over actual task completion), then most of the second half of the
      // milestones which are not completed we have half actual sub tasks completed
      assertBatchCompleteProgress( lWorkPackage, 63 );

      // Complete one more additional JIC
      lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lJIC8a );
      }
      // Batch complete half of the remaining the sub JIC's
      iCompleteService.batchCompleteTasks( lCompletedTasks, new Date(), iHr, iTxHandler );
      assertComplete( lJIC8a );

      // This will bring us up to 67%
      assertBatchCompleteProgress( lWorkPackage, 67 );

      // If we complete one more of the milestones we will jump to 75%
      lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lREQ5 );
      }
      markCompleteTaskWorkItemAsComplete( lCompletedTasks );
      assertBatchCompleteProgress( lWorkPackage, 75 );

      // Complete all outstanding tasks in the work package
      iCompleteService = new CompleteService( lWorkPackage );
      iCompleteService.batchCompleteAll( new Date(), iHr, iTxHandler );

      // We should be at exactly 100% complete
      assertBatchCompleteProgress( lWorkPackage, 100 );

      lCompletedTasks = new ArrayList<>();
      {
         lCompletedTasks.add( lREQ5 );
         lCompletedTasks.add( lREQ6 );
         lCompletedTasks.add( lREQ7 );
         lCompletedTasks.add( lREQ8 );
      }
      markCompleteTaskWorkItemAsComplete( lCompletedTasks );

      // We should be at exactly 100% complete
      assertBatchCompleteProgress( lWorkPackage, 100 );

      // Close the work package
      iCompleteService.completeCheck( iHr, new Date() );

   }


   /**
    *
    * Use the details service to check the batch complete progress against a work package
    *
    * @param aWorkPackage
    * @param aExpectedProgress
    */
   private void assertBatchCompleteProgress( TaskKey aWorkPackage, int aExpectedProgress ) {
      assertEquals( aExpectedProgress,
            new DetailsService( aWorkPackage ).getBatchCompleteProgress() );
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


   private TaskKey createTaskInWorkPackage( TaskKey aWorkPackage, RefTaskClassKey aTaskClass ) {
      return new TaskBuilder().withParentTask( aWorkPackage ).withHighestTask( aWorkPackage )
            .withTaskClass( aTaskClass ).withStatus( RefEventStatusKey.ACTV )
            .onInventory( new InventoryBuilder().withClass( RefInvClassKey.TRK )
                  .withParentInventory( iAircraft ).build() )
            .build();
   }


   /**
    *
    * Simulate the work item generation from a background batch complete operation
    *
    * @param aWorkPackage
    * @param childTasks
    */
   private void createBatchCompleteWorkItems( TaskKey aWorkPackage, List<TaskKey> childTasks ) {

      // Prepare the parent batch complete grouping work item
      BatchCompleteTasks lBatchComplete =
            new BatchCompleteTasks.Builder().parentTask( aWorkPackage ).completionDate( new Date() )
                  .hr( iHr ).batchId( BATCH_COMPLETE_ID ).build();

      generateWorkItem( lBatchComplete );
      for ( TaskKey lChildTask : childTasks ) {
         CompleteTask lCompleteTask =
               new CompleteTask.CompleteTaskBuilder().batchId( lBatchComplete.getId() )
                     .completionDate( new Date() ).user( iHr ).task( lChildTask ).build();

         generateWorkItem( lCompleteTask );

      }
   }


   /**
    *
    * a light weight simulation of work item generation
    *
    * @param workItem
    */
   private void generateWorkItem( WorkItem workItem ) {
      final String INSERT_WORK_ITEM =
            "INSERT INTO utl_work_item(id,type,key,data,scheduled_date,utl_id) VALUES (?,?,?,?,?,?)";

      try ( PreparedStatement lStatement =
            iDatabaseConnectionRule.getConnection().prepareStatement( INSERT_WORK_ITEM ); ) {

         lStatement.setInt( 1, workItemId++ );
         lStatement.setString( 2, workItem.getType() );
         lStatement.setString( 3,
               ( ( workItem == null ) ? null : workItem.getKey().toValueString() ) );
         lStatement.setString( 4, workItem.getData() );
         lStatement.setTimestamp( 5, new Timestamp( new Date().getTime() ) );
         lStatement.setLong( 6, 0 );
         if ( lStatement.executeUpdate() != 1 ) {
            fail( "failed to insert work item into database" );
         }

      } catch ( SQLException e ) {
         throw new MxRuntimeException( "Could not generate work items.", e );
      }

   }


   /**
    *
    * Simulate the completion of a work item by setting a start and end date on it
    *
    * @param childTasks
    */
   private void markCompleteTaskWorkItemAsComplete( List<TaskKey> childTasks ) {
      for ( TaskKey lChildTask : childTasks ) {
         // Isolate the work item row in the database
         CompleteTask lCompleteTask =
               new CompleteTask.CompleteTaskBuilder().batchId( BATCH_COMPLETE_ID )
                     .completionDate( new Date() ).user( iHr ).task( lChildTask ).build();

         PreparedStatement lStatement;

         String lSQLStatement =
               "UPDATE utl_work_item SET start_date = sysdate, end_date=sysdate WHERE type=? AND key=? AND data LIKE ?";

         try {
            lStatement = iDatabaseConnectionRule.getConnection().prepareStatement( lSQLStatement );
            lStatement.setString( 1, lCompleteTask.getType() );
            lStatement.setString( 2, lCompleteTask.getKey().toValueString() );
            lStatement.setString( 3, lCompleteTask.getTask().toValueString() + ";%" );
            // Mark the work item as complete (give it an end date)
            if ( lStatement.executeUpdate() != 1 ) {
               fail( "failed to update work item to complete status" );
            }

         } catch ( SQLException e ) {
            throw new MxRuntimeException( "Could not mark work item as complete", e );
         }

      }
   }


   private TaskKey createSubTaskInWorkPackage( TaskKey aWorkPackage, TaskKey aParentTask,
         RefTaskClassKey aTaskClass ) {
      return new TaskBuilder().withParentTask( aParentTask ).withHighestTask( aWorkPackage )
            .withTaskClass( aTaskClass ).withStatus( RefEventStatusKey.ACTV ).build();
   }

}
