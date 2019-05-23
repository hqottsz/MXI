
package com.mxi.mx.common.query.work;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.WorkItemBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.common.dataset.SQLStatement;


/**
 * Unit tests for the UnscheduleIncompleteWorkItems query file.
 */
@RunWith( Theories.class )
public final class UnscheduleIncompleteWorkItemsTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public final FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @DataPoints
   public static final Date[] TEST_DATES;

   private static final String GET_WORK_ITEM_SQL =
         "SELECT id FROM utl_work_item WHERE server_id IS NULL";

   private static final String SERVER_ID_1 = "SERVER_ID_1";
   private static final String SERVER_ID_2 = "SERVER_ID_2";

   private static final Date PRIOR_TO_CUTOFF_DATE;
   private static final Date CUTOFF_DATE;
   private static final Date AFTER_CUTOFF_DATE;

   static {
      Calendar lCutoffDate = Calendar.getInstance();
      lCutoffDate.set( 2014, 2, 17, 11, 00, 00 );

      CUTOFF_DATE = lCutoffDate.getTime();
      PRIOR_TO_CUTOFF_DATE = DateUtils.addMinutes( CUTOFF_DATE, -15 );
      AFTER_CUTOFF_DATE = DateUtils.addMinutes( CUTOFF_DATE, 15 );

      // Setup the various test dates in relation to a cutoff date (including a null date).
      TEST_DATES = new Date[4];
      TEST_DATES[0] = null;
      TEST_DATES[1] = PRIOR_TO_CUTOFF_DATE;
      TEST_DATES[2] = CUTOFF_DATE;
      TEST_DATES[3] = AFTER_CUTOFF_DATE;
   }


   // ~ Methods
   // -------------------------------------------------------------------------------------

   /**
    * Test the scenario with two target servers. One server has a work item scheduled prior to the
    * cutoff but not started. The other server has a work item scheduled after the cutoff but not
    * started. Since the first server has a work item scheduled before the cutoff it is is expected
    * to be returned from the query. second server should not be returned. Hence we expect one row
    * from the query.
    *
    * @throws Exception
    */
   @Test
   public void testMultipleServersWhenOneWorkItemSchedPriorAndOtherWorkItemSchedAfterCutoff()
         throws Exception {

      // Create a work item that is scheduled to one server with a schedule date prior to the
      // cutoff but not started.
      int lWorkItemId = new WorkItemBuilder().withServerId( SERVER_ID_1 )
            .withScheduledDate( PRIOR_TO_CUTOFF_DATE ).withStartDate( null ).withEndDate( null )
            .build();

      // Create another work item that is scheduled to another server with a schedule date after
      // the cutoff but not started.
      new WorkItemBuilder().withServerId( SERVER_ID_2 ).withScheduledDate( AFTER_CUTOFF_DATE )
            .withStartDate( null ).withEndDate( null ).build();

      QuerySet lQs = executeQuery();

      assertEquals( "Unexpected number of work item ids returned.", 1, lQs.getRowCount() );
      assertEquals( "Unexpected work item id returned.", lWorkItemId, lQs.getInt( "id" ) );

   }


   /**
    * Test the scenario where one target server has a work item scheduled to it prior to the cutoff
    * date but is not started and another target server also has a work item scheduled to it prior
    * to the cutoff date but is not started. Since either of the work items were not started since
    * the cutoff, both rows should be returned from the query.
    *
    * @throws Exception
    */
   @Test
   public void testMultipleServersWithWorkItemsScheduledPriorToCutoffButNotStarted()
         throws Exception {

      // Create a work item that is scheduled to one server with a schedule date prior to the
      // cutoff but not started.
      new WorkItemBuilder().withServerId( SERVER_ID_1 ).withScheduledDate( PRIOR_TO_CUTOFF_DATE )
            .withStartDate( null ).withEndDate( null ).build();

      // Create another work item that is scheduled to another server with a schedule date prior to
      // the cutoff but not started.
      new WorkItemBuilder().withServerId( SERVER_ID_2 ).withScheduledDate( PRIOR_TO_CUTOFF_DATE )
            .withStartDate( null ).withEndDate( null ).build();

      QuerySet lQs = executeQuery();

      assertEquals( "Unexpected number of work item ids returned.", 2, lQs.getRowCount() );

   }


   /**
    * Test the scenario where the target server has multiple work items scheduled to it. Work items
    * have a scheduled date prior to the cutoff date. One has not been started and the other has
    * been started but after the cutoff date. As second work item started after the cutoff date, it
    * is not returned from the query. Only one work item should be returned.
    *
    * @throws Exception
    */
   @Test
   public void testServerWithOneOfManyWorkItemsStartedAfterCutoff() throws Exception {

      // Create a work item that is scheduled prior to the cutoff date but not started.
      int lWorkItemId = new WorkItemBuilder().withServerId( SERVER_ID_1 )
            .withScheduledDate( PRIOR_TO_CUTOFF_DATE ).withStartDate( null ).withEndDate( null )
            .build();

      // Create another work item that is scheduled prior to the cutoff date but was started after
      // the cutoff date.
      new WorkItemBuilder().withServerId( SERVER_ID_1 ).withScheduledDate( PRIOR_TO_CUTOFF_DATE )
            .withStartDate( AFTER_CUTOFF_DATE ).withEndDate( null ).build();

      QuerySet lQs = executeQuery();

      assertEquals( "Unexpected number of work item ids returned.", 1, lQs.getRowCount() );
      assertEquals( "Unexpected work item id returned.", lWorkItemId, lQs.getInt( "id" ) );
   }


   /**
    * Test the various combinations of schedule, start, and end dates for a single work item
    * scheduled to a single server.
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testServerWithOneWorkItem( Date lScheduleDate, Date lStartDate, Date lEndDate )
         throws Exception {
      executeServerWithOneWorkItem( lScheduleDate, lStartDate, lEndDate );
   }


   /**
    * Test the scenario where the target server has two work items schedule to it. One work item is
    * completed and the other work item is scheduled after the cutoff. The completed work item is
    * ignored by the query, however, because the other work item has been scheduled after the cutoff
    * date, it is not returned from the query as well.
    *
    * @throws Exception
    */
   @Test
   public void testServerWithOneWorkItemCompletedAndAnotherScheduledAfterTheCutoff()
         throws Exception {

      // Create a work item that is completed.
      new WorkItemBuilder().withServerId( SERVER_ID_1 ).withScheduledDate( PRIOR_TO_CUTOFF_DATE )
            .withStartDate( CUTOFF_DATE ).withEndDate( AFTER_CUTOFF_DATE ).build();

      // Create another work item that is scheduled prior to the cutoff date but not started.
      new WorkItemBuilder().withServerId( SERVER_ID_1 ).withScheduledDate( AFTER_CUTOFF_DATE )
            .withStartDate( null ).withEndDate( null ).build();

      QuerySet lQs = executeQuery();

      assertEquals( "Unexpected number of work item ids returned.", 0, lQs.getRowCount() );
   }


   /**
    * Test the scenario where the target server has two work items schedule to it. One work item is
    * completed and the other work item is scheduled prior to the cutoff but has not been started.
    * The completed work item is ignored by the query, however, the other work item is expected to
    * be returned from the query.
    *
    * @throws Exception
    */
   @Test
   public void testServerWithOneWorkItemCompletedAndAnotherScheduledPriorToCutoffButNotStarted()
         throws Exception {

      // Create a work item that is completed.
      new WorkItemBuilder().withServerId( SERVER_ID_1 ).withScheduledDate( PRIOR_TO_CUTOFF_DATE )
            .withStartDate( CUTOFF_DATE ).withEndDate( AFTER_CUTOFF_DATE ).build();

      // Create another work item that is scheduled prior to the cutoff date but not started.
      int lWorkItemId = new WorkItemBuilder().withServerId( SERVER_ID_1 )
            .withScheduledDate( PRIOR_TO_CUTOFF_DATE ).withStartDate( null ).withEndDate( null )
            .build();

      QuerySet lQs = executeQuery();

      assertEquals( "Unexpected number of work items ids returned.", 1, lQs.getRowCount() );
      assertEquals( "Unexpected work item id returned.", lWorkItemId, lQs.getInt( "id" ) );

   }


   /**
    * Test the scenario where one target server has a work item scheduled to it prior to the cutoff
    * date but is started after the cutoff and another target server has a work item scheduled to it
    * prior to the cutoff date but is started prior to the cutoff. Since the second server's work
    * item was started prior to the cutoff only its work item is expected to be returned from the
    * query.
    *
    * @throws Exception
    */
   @Test
   public void testServerWithWorkItemStartedAfterCutoffAndServerWithWorkItemStartedBeforeCutoff()
         throws Exception {

      // Create a work item that is scheduled to one server with a schedule date prior to the
      // cutoff but started after the cutoff.
      new WorkItemBuilder().withServerId( SERVER_ID_1 ).withScheduledDate( PRIOR_TO_CUTOFF_DATE )
            .withStartDate( AFTER_CUTOFF_DATE ).withEndDate( null ).build();

      // Create another work item that is scheduled to another server with a schedule date and a
      // start date prior to the cutoff.
      int lWorkItemId = new WorkItemBuilder().withServerId( SERVER_ID_2 )
            .withScheduledDate( PRIOR_TO_CUTOFF_DATE ).withStartDate( PRIOR_TO_CUTOFF_DATE )
            .withEndDate( null ).build();

      QuerySet lQs = executeQuery();

      // Assert only the expected server id is returned.
      assertEquals( "Unexpected number of server ids returned.", 1, lQs.getRowCount() );
      assertEquals( "Unexpected work item id returned.", lWorkItemId, lQs.getInt( "id" ) );
   }


   /**
    * Execute the query UnscheduleIncompleteWorkItems and then collect the unscheduled work items
    *
    * @return data set containing unscheduled work items
    */
   private QuerySet executeQuery() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aInactivityCutoffDate", CUTOFF_DATE );

      MxDataAccess.getInstance()
            .executeUpdate( "com.mxi.mx.common.query.work.UnscheduleIncompleteWorkItems", lArgs );

      SQLStatement lStatement = new SQLStatement( GET_WORK_ITEM_SQL );
      lStatement.prepare( iDatabaseConnectionRule.getConnection() );

      QuerySet lQs = lStatement.executeQuery( iDatabaseConnectionRule.getConnection() );
      lQs.first();
      return lQs;

   }


   /**
    * Exercise the actual test of a single work item scheduled to a single server using the provided
    * schedule, start, and end dates.
    *
    * @param aScheduleDate
    * @param aStartDate
    * @param aEndDate
    *
    * @throws Exception
    */
   private void executeServerWithOneWorkItem( Date aScheduleDate, Date aStartDate, Date aEndDate )
         throws Exception {

      // Emulate jUnit4.1 assumeNotNull and assumeTrue in order to ignore certain data values.
      if ( aEndDate != null ) {
         if ( ( aScheduleDate == null ) || ( aStartDate == null )
               || ( aScheduleDate.after( aStartDate ) ) || ( aScheduleDate.after( aEndDate ) )
               || ( aStartDate.after( aEndDate ) ) ) {
            return;
         }
      }

      if ( aStartDate != null ) {
         if ( ( aScheduleDate == null ) || aScheduleDate.after( aStartDate ) ) {
            return;
         }
      }

      StringBuilder lDateInfo = new StringBuilder( "Dates: " );
      lDateInfo.append( "aScheduleDate=" );
      lDateInfo.append( aScheduleDate );
      lDateInfo.append( " , aStartDate=" );
      lDateInfo.append( aStartDate );
      lDateInfo.append( " , aEndDate=" );
      lDateInfo.append( aEndDate );

      // Create a work item using the passed in dates.
      int lWorkItemId =
            new WorkItemBuilder().withServerId( SERVER_ID_1 ).withScheduledDate( aScheduleDate )
                  .withStartDate( aStartDate ).withEndDate( aEndDate ).build();

      // Execute the GetInactiveWorkItems.qrx query.
      QuerySet lQs = executeQuery();

      // Verify scenarios where the work item id is expected to be returned.
      if ( ( aScheduleDate != null ) && ( ( aScheduleDate.before( CUTOFF_DATE )
            && ( aStartDate == null ) && ( aEndDate == null ) )
            || ( ( aStartDate != null ) && aStartDate.before( CUTOFF_DATE )
                  && ( aEndDate == null ) ) ) ) {

         assertEquals( "Unexpected number of work item ids returned. " + lDateInfo, 1,
               lQs.getRowCount() );
         assertEquals( "Unexpected work item id returned.", lWorkItemId, lQs.getInt( "id" ) );
      } else {

         // Otherwise, expect nothing to be returned.
         assertEquals( "Unexpected number of work item ids returned. " + lDateInfo, 0,
               lQs.getRowCount() );
      }
   }
}
