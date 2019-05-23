
package com.mxi.mx.core.query.task;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefTaskClassKey.FOLLOW;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;


@RunWith( BlockJUnit4ClassRunner.class )
public class GetJobCardsFromSameTaskDefnTest {

   private static final String TASK_DB_ID = "4650";
   private static final String TASK_ID = "1234";
   private static final String TASK_ID2 = "1235";
   private static final String TASK_ID3 = "4321";
   private static final String TASK_KEY = TASK_DB_ID + ":" + TASK_ID;
   private static final String INV_KEY = "4650:9876";
   private static final String TASK_DEFN_STATUS = "ACTV";
   private static final String TASK_STATUS = "IN WORK";
   private static final String TASK_CONFIG_POS_SDESC = "POS 1";
   private static final String RMVD_PART1_CONFIG_POS_SDESC = "12-34-56 (1A [105WR]) ";
   private static final String RMVD_PART2_CONFIG_POS_SDESC = "12-34-56 (2B [201WR]) ";
   private static final String TASK_KEY1 = "4650:4321";
   private static final String TASK_KEY2 = "4650:4322";

   private static final String POSITION_DESCRIPTION = "CONFIG_POS_DESC";

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @ClassRule
   public static InjectionOverrideRule sInjectionOverrideRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetJobCardsFromSameTaskDefnTest.class,
            "GetJobCardsFromSameTaskDefnTest.xml" );
   }


   @Test
   public void itFindsJobCardsFromSameTaskDefinition() {
      QuerySet lQs = executeQuery( TASK_DB_ID, TASK_ID );

      // Ensure there is a result
      Assert.assertTrue( lQs.first() );

      // Ensure the result matches the expected data
      Assert.assertEquals( TASK_KEY, lQs.getString( "sched_key" ) );
      Assert.assertEquals( INV_KEY, lQs.getString( "inv_key" ) );
      Assert.assertEquals( TASK_DEFN_STATUS, lQs.getString( "task_defn_status" ) );
      Assert.assertEquals( TASK_STATUS, lQs.getString( "task_status" ) );
      Assert.assertEquals( TASK_CONFIG_POS_SDESC, lQs.getString( "task_config_pos_sdesc" ) );

      // Ensure both records are found for each job card task
      Assert.assertEquals( "There are two JIC records found with the same task definition", 2,
            lQs.getRowCount() );
   }


   @Test
   public void itFindsJobCardsOfRmvdPartFromSameTaskDefinition() {
      QuerySet lQs = executeQuery( TASK_DB_ID, TASK_ID3 );

      // Ensure there are results
      HashMap<String, String> lResults = new HashMap<String, String>();
      Assert.assertTrue( lQs.first() );
      lResults.put( lQs.getString( "sched_key" ), lQs.getString( "task_config_pos_sdesc" ) );

      Assert.assertTrue( lQs.next() );
      lResults.put( lQs.getString( "sched_key" ), lQs.getString( "task_config_pos_sdesc" ) );

      // Ensure the result matches the expected data
      Assert.assertEquals( RMVD_PART1_CONFIG_POS_SDESC, lResults.get( TASK_KEY1 ) );
      Assert.assertEquals( RMVD_PART2_CONFIG_POS_SDESC, lResults.get( TASK_KEY2 ) );

      // Ensure both records are found for each job card task
      Assert.assertEquals( "There are two JIC records found with the same task definition", 2,
            lQs.getRowCount() );
   }


   @Test
   public void itFindsStartedJobCardsFirst() {
      // This time pass in task 2 as an argument
      QuerySet lQs = executeQuery( TASK_DB_ID, TASK_ID2 );

      // Ensure there is a result
      Assert.assertTrue( lQs.first() );

      // Ensure that the first record retrieved is the in work task key
      Assert.assertEquals( TASK_KEY, lQs.getString( "sched_key" ) );

      // Ensure both records are found for each job card task
      Assert.assertEquals( "There are two JIC records found with the same task definition", 2,
            lQs.getRowCount() );
   }


   /**
    * Verify the query does not return job cards assigned to FOLLOW requirements and are in the work
    * scope of the same work package.
    */
   @Test
   public void testThatJicsAssignedToFollowReqsAreNotReturned() {

      // Given multiple active job card tasks based on the same job card definition and are against
      // the same inventory.
      TaskTaskKey lJicDefn = Domain.createJobCardDefinition();
      InventoryKey lInv = Domain.createAircraft( aInv -> {
         // Note; the query requires the inventory to have a configuration position description.
         aInv.setPositionDescription( POSITION_DESCRIPTION );
      } );
      TaskKey lJobCard1 = Domain.createJobCard( aJobCard1 -> {
         aJobCard1.setDefinition( lJicDefn );
         aJobCard1.setInventory( lInv );
         aJobCard1.setStatus( ACTV );
      } );
      TaskKey lJobCard2 = Domain.createJobCard( aJobCard2 -> {
         aJobCard2.setDefinition( lJicDefn );
         aJobCard2.setInventory( lInv );
         aJobCard2.setStatus( ACTV );
      } );

      // Given the job cards are each assigned to FOLLOW requirements.
      TaskKey lReq1 = Domain.createRequirement( aReq1 -> {
         aReq1.setTaskClass( FOLLOW );
         aReq1.addJobCard( lJobCard1 );
      } );
      TaskKey lReq2 = Domain.createRequirement( aReq2 -> {
         aReq2.setTaskClass( FOLLOW );
         aReq2.addJobCard( lJobCard2 );
      } );

      // Given a work package with both the requirements assigned and their job cards added to its
      // work scope.
      Domain.createWorkPackage( aWp -> {
         aWp.addTask( lReq1 );
         aWp.addTask( lReq2 );
         aWp.addWorkScopeTask( lJobCard1 );
         aWp.addWorkScopeTask( lJobCard2 );
      } );

      // When the query is executed with one of the job cards.
      QuerySet lQs = execute( lJobCard1 );

      // Then no job cards are returned.
      assertThat( "Unexpected number of rows returned.", lQs.getRowCount(), is( 0 ) );
   }


   /**
    * Verify the query only returns job cards assigned to non-FOLLOW requirements and are in the
    * work scope of the same work package.
    */
   @Test
   public void testThatJicsAssignedToNonFollowReqsAreReturned() {

      // Given multiple active job card tasks based on the same job card definition and are against
      // the same inventory.
      TaskTaskKey lJicDefn = Domain.createJobCardDefinition();
      InventoryKey lInv = Domain.createAircraft( aInv -> {
         // Note; the query requires the inventory to have a configuration position description.
         aInv.setPositionDescription( POSITION_DESCRIPTION );
      } );
      TaskKey lJobCard1 = Domain.createJobCard( aJobCard1 -> {
         aJobCard1.setDefinition( lJicDefn );
         aJobCard1.setInventory( lInv );
         aJobCard1.setStatus( ACTV );
      } );
      TaskKey lJobCard2 = Domain.createJobCard( aJobCard2 -> {
         aJobCard2.setDefinition( lJicDefn );
         aJobCard2.setInventory( lInv );
         aJobCard2.setStatus( ACTV );
      } );
      TaskKey lJobCard3 = Domain.createJobCard( aJobCard3 -> {
         aJobCard3.setDefinition( lJicDefn );
         aJobCard3.setInventory( lInv );
         aJobCard3.setStatus( ACTV );
      } );

      // Given one job card is assigned to a FOLLOW requirement.
      TaskKey lReq1 = Domain.createRequirement( aReq1 -> {
         aReq1.setTaskClass( FOLLOW );
         aReq1.addJobCard( lJobCard1 );
      } );

      // Given the other job cards are assigned to non-FOLLOW requirements.
      TaskKey lReq2 = Domain.createRequirement( aReq2 -> {
         aReq2.setTaskClass( REQ );
         aReq2.addJobCard( lJobCard2 );
      } );
      TaskKey lReq3 = Domain.createRequirement( aReq3 -> {
         aReq3.setTaskClass( REQ );
         aReq3.addJobCard( lJobCard3 );
      } );

      // Given a work package with both the requirements assigned and their job cards added to its
      // work scope.
      Domain.createWorkPackage( aWp -> {
         aWp.addTask( lReq1 );
         aWp.addTask( lReq2 );
         aWp.addTask( lReq3 );
         aWp.addWorkScopeTask( lJobCard1 );
         aWp.addWorkScopeTask( lJobCard2 );
         aWp.addWorkScopeTask( lJobCard3 );
      } );

      // When the query is executed with one of the job cards.
      QuerySet lQs = execute( lJobCard1 );

      List<TaskKey> lTasks = new ArrayList<>();
      while ( lQs.next() ) {
         lTasks.add( lQs.getKey( TaskKey.class, "sched_key" ) );
      }

      // Then only the job card assigned to the non-FOLLOW requirement is returned.
      assertThat( "Unexpected number of rows returned.", lTasks.size(), is( 2 ) );
      assertThat( "Expected second JIC to be returned.", lTasks, Matchers.hasItem( lJobCard2 ) );
      assertThat( "Expected third JIC to be returned.", lTasks, Matchers.hasItem( lJobCard3 ) );
   }


   private QuerySet execute( TaskKey lJic ) {
      return executeQuery( String.valueOf( lJic.getDbId() ), String.valueOf( lJic.getId() ) );
   }


   private QuerySet executeQuery( String aTaskDbId, String aTaskId ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskDbId", aTaskDbId );
      lArgs.add( "aTaskId", aTaskId );

      return iQao.executeQuery( "com.mxi.mx.core.query.task.GetJobCardsFromSameTaskDefn", lArgs );
   }

}
