package com.mxi.mx.core.services.stask.complete.completeservice;

import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.work.WorkItem;
import com.mxi.mx.common.services.work.precondition.PreconditionFailedException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.complete.CompleteTask;
import com.mxi.mx.core.worker.task.preconditions.BatchCompleteTasksPrecondition;


/**
 * This class tests that the batch complete work item precondition works properly and blocks while
 * child tasks have not yet completed
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class EvaluateBatchCompleteTasksPreconditionTest {

   private static final int BATCH_COMPLETE_ID = 1;

   private final TaskKey TEST_TASK = new TaskKey( 0, 1 );

   private static int workItemId = 1;

   private HumanResourceKey iHr;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      iHr = new HumanResourceDomainBuilder().build();
   }


   @Test
   public void testPrecondition() throws Throwable {

      CompleteTask lCompleteTask =
            new CompleteTask.CompleteTaskBuilder().batchId( BATCH_COMPLETE_ID )
                  .completionDate( new Date() ).user( iHr ).task( TEST_TASK ).build();

      generateWorkItem( lCompleteTask );

      BatchCompleteTasksPrecondition lPrecondition =
            new BatchCompleteTasksPrecondition( BATCH_COMPLETE_ID );

      try {
         // Check that the precondition fails
         lPrecondition.evaluate();
         fail( "precondition did not fail as expected" );
      } catch ( PreconditionFailedException e ) {

      }
      markCompleteTaskWorkItemAsComplete( TEST_TASK );
      // Check that the precondition now passes
      lPrecondition.evaluate();
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
   private void markCompleteTaskWorkItemAsComplete( TaskKey aChildTask ) {
      // Isolate the work item row in the database
      CompleteTask lCompleteTask =
            new CompleteTask.CompleteTaskBuilder().batchId( BATCH_COMPLETE_ID )
                  .completionDate( new Date() ).user( iHr ).task( aChildTask ).build();

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
