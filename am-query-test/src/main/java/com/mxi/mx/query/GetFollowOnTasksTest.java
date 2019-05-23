package com.mxi.mx.query;

import static com.mxi.am.domain.Domain.createAdhocTask;
import static com.mxi.am.domain.Domain.createCorrectiveTask;
import static com.mxi.am.domain.Domain.createFault;
import static com.mxi.am.domain.Domain.createRequirement;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.floorSecond;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static com.mxi.mx.core.key.DataTypeKey.CHR;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefSchedPriorityKey.LOW;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.mim.MimDataType;


/**
 * Unit tests for the com.mxi.mx.query.GetOnFollowOnTasks query.
 */
public final class GetFollowOnTasksTest {

   @Rule
   public DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final String FOLLOW_ON_BARCODE = "barcode";
   private static final String FOLLOW_ON_DESCRIPTION = "description";
   private static final int DRIVING_DEADLINE_DEVIATION = 0;
   private static final int DRIVING_DEADLINE_USAGE_REMAINING = 1;
   private static final int NON_DRIVING_DEADLINE_DEVIATION = 2;
   private static final int NON_DRIVING_DEADLINE_USAGE_REMAINING = 3;


   /**
    *
    * Verify the query does not return the corrective task of a fault.
    *
    * <pre>
    *    Given a fault with a corrective task
    *     When the query is executed with the corrective task
    *     Then the corrective task is not returned
    * </pre>
    *
    */
   @Test
   public void itDoesNotReturnCorrectiveTask() {

      // Given a fault with a corrective task.
      TaskKey lCorrectiveTask = createCorrectiveTask();
      createFault( aFault -> aFault.setCorrectiveTask( lCorrectiveTask ) );

      // When the query is executed with the corrective task.
      List<TaskKey> lFollowOnTasks = executeQuery( lCorrectiveTask );

      // Then the corrective task is not returned.
      assertThat( "Unexpected number of tasks returned.", lFollowOnTasks.size(), is( 0 ) );
   }


   /**
    *
    * Verify the query returns repetitive tasks associated to a fault.
    *
    * <pre>
    *    Given a fault with a corrective task
    *      And repetitive adhoc tasks related to the fault
    *          (one active and many forecast repetitive tasks)
    *     When the query is executed with the corrective task
    *     Then the active and forecast repetitive tasks are returned
    * </pre>
    *
    * Note: In reality the query does not recognize active and forecast tasks, it only recognizes
    * tasks with FAULTREL (fault-related)) relationships. The active and forecast tasks in this test
    * have been given FAULTREL relationships. I am calling them active and forecast tasks to make it
    * more familiar.
    *
    */
   @Test
   public void itReturnsRepetitiveTasks() {

      // Given a fault with a corrective task.
      TaskKey lCorrectiveTask = createCorrectiveTask();
      FaultKey lFault = createFault( aFault -> aFault.setCorrectiveTask( lCorrectiveTask ) );

      // Given repetitive adhoc tasks related to the fault.
      // (one active and many forecast repetitive tasks)
      TaskKey lActvRepTask =
            createAdhocTask( aActiveRepTask -> aActiveRepTask.setRelatedFault( lFault ) );
      TaskKey lForecastRepTask1 =
            createAdhocTask( aForecastRepTask1 -> aForecastRepTask1.setRelatedFault( lFault ) );
      TaskKey lForecastRepTask2 =
            createAdhocTask( aForecastRepTask2 -> aForecastRepTask2.setRelatedFault( lFault ) );

      // When the query is executed with the corrective task.
      List<TaskKey> lFollowOnTasks = executeQuery( lCorrectiveTask );

      // Then the active and forecast repetitive tasks are returned.

      assertThat( "Unexpected number of tasks returned.", lFollowOnTasks.size(), is( 3 ) );
      assertThat( "ACTV repetitive task not returned.", lFollowOnTasks, hasItem( lActvRepTask ) );
      assertThat( "First FORECAST repetitive task not returned.", lFollowOnTasks,
            hasItem( lForecastRepTask1 ) );
      assertThat( "Second FORECAST repetitive task not returned.", lFollowOnTasks,
            hasItem( lForecastRepTask2 ) );
   }


   /**
    *
    * Verify the query does not return sub-tasks of a fault's corrective task.
    *
    * <pre>
    *    Given a fault with a corrective task
    *      And the corrective task has sub-tasks
    *     When the query is executed with the corrective task
    *     Then the sub-task is not returned
    * </pre>
    *
    */
   @Test
   public void itDoesNotReturnSubtasksOfTheCorrectiveTask() {

      // Given a fault with a corrective task and the corrective task has sub-tasks.
      TaskKey lCorrectiveTask = createCorrectiveTask( aCorrTask -> {
         aCorrTask.addSubtask( createRequirement() );
         aCorrTask.addSubtask( createRequirement() );
      } );
      createFault( aFault -> aFault.setCorrectiveTask( lCorrectiveTask ) );

      // When the query is executed with the corrective task.
      List<TaskKey> lFollowOnTasks = executeQuery( lCorrectiveTask );

      // Then no tasks are returned.
      assertThat( "Unexpected number of tasks returned.", lFollowOnTasks.size(), is( 0 ) );
   }


   /**
    *
    * Verify the query returns fault-related tasks associated to a fault.
    *
    * <pre>
    *    Given a fault with a corrective task
    *      And requirement tasks that are fault-related to the fault
    *     When the query is executed with the corrective task
    *     Then the fault-related requirement tasks are returned
    * </pre>
    *
    */
   @Test
   public void itReturnsFaultRelatedTasks() {

      // Given a fault with a corrective task.
      TaskKey lCorrectiveTask = createCorrectiveTask();
      FaultKey lFault = createFault( aFault -> aFault.setCorrectiveTask( lCorrectiveTask ) );

      // Given requirement tasks that are fault-related to the fault.
      TaskKey lReq1 = createRequirement( aReq -> aReq.setRelatedFault( lFault ) );
      TaskKey lReq2 = createRequirement( aReq -> aReq.setRelatedFault( lFault ) );

      // When the query is executed with the corrective task.
      List<TaskKey> lFollowOnTasks = executeQuery( lCorrectiveTask );

      // Then the fault-related requirement tasks are returned.
      assertThat( "Unexpected number of tasks returned.", lFollowOnTasks.size(), is( 2 ) );
      assertThat( "First fault-related requirement task not returned.", lFollowOnTasks,
            hasItem( lReq1 ) );
      assertThat( "Second fault-related requirement task not returned.", lFollowOnTasks,
            hasItem( lReq2 ) );
   }


   /**
    * Verify the query returns fault-related recurring tasks associated to a fault.
    *
    * <pre>
    *    Given a fault with a corrective task
    *      And recurring requirement tasks that are fault-related to the fault
    *          (one active and many forecast tasks)
    *     When the query is executed with the corrective task
    *     Then the fault-related recurring requirement tasks are returned
    * </pre>
    *
    * Note: In reality the query does not recognize active and forecast tasks, it only recognizes
    * tasks with FAULTREL (fault-related)) relationships. The active and forecast tasks in this test
    * have been given FAULTREL relationships. I am calling them active and forecast tasks to make it
    * more familiar.
    *
    */
   @Test
   public void itReturnsFaultRelatedRecurringTasks() {

      // Given a fault with a corrective task.
      TaskKey lCorrectiveTask = createCorrectiveTask();
      FaultKey lFault = createFault( aFault -> aFault.setCorrectiveTask( lCorrectiveTask ) );

      // Given recurring requirement tasks that are fault-related to the fault.
      // (one active and many forecast tasks)
      TaskKey lActvReq = createRequirement( aActvReq -> aActvReq.setRelatedFault( lFault ) );
      TaskKey lForecastReq1 = createRequirement( aForecastReq1 -> {
         aForecastReq1.setPreviousTask( lActvReq );
         aForecastReq1.setRelatedFault( lFault );
      } );
      TaskKey lForecastReq2 = createRequirement( aForecastReq2 -> {
         aForecastReq2.setPreviousTask( lForecastReq1 );
         aForecastReq2.setRelatedFault( lFault );
      } );

      // When the query is executed with the corrective task.
      List<TaskKey> lFollowOnTasks = executeQuery( lCorrectiveTask );

      // Then the fault-related requirement tasks are returned.
      assertThat( "Unexpected number of tasks returned.", lFollowOnTasks.size(), is( 3 ) );
      assertThat( "Active fault-related recurring requirement task not returned.", lFollowOnTasks,
            hasItem( lActvReq ) );
      assertThat( "First forecast fault-related recurring requirement task not returned.",
            lFollowOnTasks, hasItem( lForecastReq1 ) );
      assertThat( "Second forecast fault-related recurring requirement task not returned.",
            lFollowOnTasks, hasItem( lForecastReq2 ) );
   }


   /**
    * Verify the query does not return fault-related, orphaned recurring tasks associated to a
    * fault.
    *
    * <pre>
    *    Given a fault with a corrective task
    *      And cancelled recurring requirement tasks that are fault-related to the fault
    *          (one cancelled and many orphaned forecast tasks)
    *     When the query is executed with the corrective task
    *     Then only the cancelled fault-related task is returned
    * </pre>
    *
    * Note: In reality the query does not recognize active and forecast tasks, it only recognizes
    * tasks with FAULTREL (fault-related)) relationships. The active and forecast tasks in this test
    * have been given FAULTREL relationships. I am calling them active and forecast tasks to make it
    * more familiar.
    *
    */
   @Test
   public void itDoesNotReturnOrphanedFaultRelatedTasks() {

      // Given a fault with a corrective task.
      TaskKey lCorrectiveTask = createCorrectiveTask();
      FaultKey lFault = createFault( aFault -> aFault.setCorrectiveTask( lCorrectiveTask ) );

      // Given recurring requirement tasks that are fault-related to the fault.
      // (one cancelled and many orphaned forecast tasks)
      TaskKey lCancelledReq =
            createRequirement( aCancelledReq -> aCancelledReq.setRelatedFault( lFault ) );
      TaskKey lForecastReq1 = createRequirement( aForecastReq1 -> {
         aForecastReq1.setPreviousTask( lCancelledReq );
         aForecastReq1.setRelatedFault( lFault );
         aForecastReq1.setOrphaned( true );
      } );
      createRequirement( aForecastReq2 -> {
         aForecastReq2.setPreviousTask( lForecastReq1 );
         aForecastReq2.setRelatedFault( lFault );
         aForecastReq2.setOrphaned( true );
      } );

      // When the query is executed with the corrective task.
      List<TaskKey> lFollowOnTasks = executeQuery( lCorrectiveTask );

      // Then only the cancelled fault-related task is returned.
      assertThat( "Unexpected number of tasks returned.", lFollowOnTasks.size(), is( 1 ) );
      assertThat( "Cancelled fault-related recurring requirement task not returned.",
            lFollowOnTasks, hasItem( lCancelledReq ) );
   }


   /**
    *
    * Verify the query returns fault-related task information (non-deadline) associated to a fault.
    * (The deadline information will be tested in another test method.)
    *
    * <pre>
    *    Given a fault with a corrective task
    *      And a requirement task that is fault-related to the fault
    *      And the requirement has the relevant non-deadline information retrieved by the query
    *     When the query is executed with the corrective task
    *     Then the fault-related requirement task's information is returned
    * </pre>
    *
    */
   @Test
   public void itReturnsFaultRelatedTaskInfo() {

      // Given a fault with a corrective task.
      TaskKey lCorrectiveTask = createCorrectiveTask();
      FaultKey lFault = createFault( aFault -> aFault.setCorrectiveTask( lCorrectiveTask ) );

      // Given a requirement task that is fault-related to the fault
      // and the requirement has the relevant non-deadline information retrieved by the query.
      //
      // Note: actual end date has granularity of seconds.
      Date lEndDate = floorSecond( new Date() );
      TaskKey lReq = createRequirement( aReq -> {
         aReq.setRelatedFault( lFault );
         aReq.setDescription( FOLLOW_ON_DESCRIPTION );
         aReq.setBarcode( FOLLOW_ON_BARCODE );
         aReq.setStatus( ACTV );
         aReq.setSchedulingPriority( LOW );
         aReq.setActualEndDate( lEndDate );
      } );

      // When the query is executed with the corrective task.
      QuerySet lQs = executeQueryToQuerySet( lCorrectiveTask );
      lQs.next();

      // Then the fault-related requirement tasks are returned.
      assertThat( "Unexpected number of tasks returned.", lQs.getRowCount(), is( 1 ) );
      assertThat( "Fault-related requirement task not returned.",
            lQs.getKey( TaskKey.class, "follow_on_task_key" ), is( lReq ) );
      assertThat( "Unexpected follow-on task description.", lQs.getString( "follow_on_task_sdesc" ),
            is( FOLLOW_ON_DESCRIPTION ) );
      assertThat( "Unexpected follow-on task barcode.", lQs.getString( "follow_on_task_barcode" ),
            is( FOLLOW_ON_BARCODE ) );
      assertThat( "Unexpected follow-on task status code.",
            lQs.getString( "follow_on_task_status" ), is( ACTV.getCd() ) );
      assertThat( "Unexpected follow-on task scheduling priority code.",
            lQs.getString( "sched_priority_cd" ), is( LOW.getCd() ) );
      assertThat( "Unexpected follow-on task actual end date.", lQs.getDate( "event_dt" ),
            is( lEndDate ) );
   }


   /**
    * Verify the query returns the driving deadline information of a fault-related task associated
    * to a fault.
    *
    * <pre>
    *    Given a fault with a corrective task
    *      And a requirement task that is fault-related to the fault
    *      And the requirement task has a driving deadline
    *      And the requirement task has a non-driving deadline
    *     When the query is executed with the corrective task
    *     Then the driving deadline information of the fault-related task is returned
    * </pre>
    *
    */
   @Test
   public void itReturnsDrivingDeadlineInfoOfFaultRelatedTask() {

      // Given a fault with a corrective task.
      TaskKey lCorrectiveTask = createCorrectiveTask();
      FaultKey lFault = createFault( aFault -> aFault.setCorrectiveTask( lCorrectiveTask ) );

      // Given a requirement task that is fault-related to the fault
      // and the requirement task has a driving deadline
      // and the requirement task has a non-driving deadline.
      //
      // Note: due date has granularity of seconds.
      Date lDrivingDueDate = floorSecond( addDays( new Date(), 1 ) );
      Date lNonDrivingDueDate = floorSecond( addDays( new Date(), 2 ) );
      createRequirement( aReq -> {
         aReq.setRelatedFault( lFault );
         aReq.addDeadline( aDrivingDeadline -> {
            aDrivingDeadline.setDriving( true );
            aDrivingDeadline.setDeviation( DRIVING_DEADLINE_DEVIATION );
            aDrivingDeadline.setUsageType( CDY );
            aDrivingDeadline.setUsageRemaining( DRIVING_DEADLINE_USAGE_REMAINING );
            aDrivingDeadline.setDueDate( lDrivingDueDate );
         } );
         aReq.addDeadline( aNonDrivingDeadline -> {
            aNonDrivingDeadline.setDriving( false );
            aNonDrivingDeadline.setDeviation( NON_DRIVING_DEADLINE_DEVIATION );
            aNonDrivingDeadline.setUsageType( CHR );
            aNonDrivingDeadline.setUsageRemaining( NON_DRIVING_DEADLINE_USAGE_REMAINING );
            aNonDrivingDeadline.setDueDate( lNonDrivingDueDate );
         } );
      } );

      // When the query is executed with the corrective task.
      QuerySet lQs = executeQueryToQuerySet( lCorrectiveTask );
      lQs.next();

      // Then the driving deadline information of the fault-related task is returned.
      assertThat( "Unexpected number of tasks returned.", lQs.getRowCount(), is( 1 ) );

      assertThat( "Unexpected deadline domain type code.", lQs.getString( "domain_type_cd" ),
            is( getDomainType( CDY ) ) );
      assertThat( "Unexpected deadline deviation.", lQs.getInt( "deviation_qt" ),
            is( DRIVING_DEADLINE_DEVIATION ) );
      assertThat( "Unexpected deadline usage remaining.", lQs.getInt( "usage_rem_qt" ),
            is( DRIVING_DEADLINE_USAGE_REMAINING ) );
      assertThat( "Unexpected deadline engineering unit.", lQs.getString( "eng_unit_cd" ),
            is( getEngUnit( CDY ) ) );
      assertThat( "Unexpected deadline precision.", lQs.getInt( "entry_prec_qt" ),
            is( getPrecision( CDY ) ) );
      assertThat( "Unexpected deadline due date.", lQs.getDate( "ext_sched_dead_dt" ),
            is( lDrivingDueDate ) );
   }


   /**
    *
    * Execute the query using the provided task and return the resulting query set.
    *
    * @param aTask
    * @return list of follow-on tasks
    */
   private QuerySet executeQueryToQuerySet( TaskKey aTask ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aTask, "aCorrTaskDbId", "aCorrTaskId" );
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }


   /**
    *
    * Execute the query using the provided task and return a list of follow-on tasks retrieved by
    * the query.
    *
    * @param aTask
    * @return list of follow-on tasks
    */
   private List<TaskKey> executeQuery( TaskKey aTask ) {
      QuerySet lQs = executeQueryToQuerySet( aTask );
      List<TaskKey> lActualTasks = new ArrayList<>();
      while ( lQs.next() ) {
         lActualTasks.add( lQs.getKey( TaskKey.class, "follow_on_task_key" ) );
      }
      return lActualTasks;
   }


   private String getDomainType( final DataTypeKey aDataType ) {
      return MimDataType.getDomainType( aDataType ).getCd();
   }


   private String getEngUnit( DataTypeKey aDataType ) {
      return MimDataType.findByPrimaryKey( aDataType ).getEngUnit().getCd();
   }


   private int getPrecision( DataTypeKey aDataType ) {
      MimDataType.findByPrimaryKey( aDataType );
      return MimDataType.getEntryPrecQt( aDataType );
   }

}
