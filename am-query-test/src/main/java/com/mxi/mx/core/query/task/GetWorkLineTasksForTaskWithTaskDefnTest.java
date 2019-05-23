
package com.mxi.mx.core.query.task;

import static com.mxi.mx.core.key.RefTaskClassKey.FOLLOW;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query com.mxi.mx.core.query.task.GetWorkLineTasksForTaskWithTaskDefn.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkLineTasksForTaskWithTaskDefnTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @ClassRule
   public static InjectionOverrideRule sInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * Tests the retrieval workscope tasks.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testThatOrphanedChildTaskIsNotReturned() throws Exception {

      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetWorkLineTasksForTaskWithTaskDefnTest.class );

      TaskKey lTaskKey = new TaskKey( 1001, 5 );
      QuerySet lQuerySet = execute( lTaskKey );

      assertTrue( "Has a first row", lQuerySet.next() );

      assertEquals( "task primary key", lTaskKey.toString(), lQuerySet.getString( "sched_key" ) );

      assertFalse( "has no more rows", lQuerySet.next() );
   }


   /**
    * Verify that a job card is returned by the query when it is assigned to a requirement that has
    * a class of REQ and that requirement is assigned to the provided work package.
    */
   @Test
   public void testThatJicAssignedToRequirementWithClassReqIsReturned() {

      // Given a job card based on a definition that is work scope enabled.
      TaskKey lJobCard = Domain.createJobCard( aJobCard -> {
         aJobCard.setDefinition( Domain.createJobCardDefinition( aDefn -> {
            aDefn.setWorkScopeEnabled( true );
         } ) );
      } );

      // Given a requirement with a class of REQ and with the job card assigned.
      TaskKey lReq = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( REQ );
         aReq.addJobCard( lJobCard );
      } );

      // Given a work package with the requirement is assigned.
      TaskKey lWorkPackage = Domain.createWorkPackage( aWp -> {
         aWp.addTask( lReq );
      } );

      // When the query is executed with the work package.
      QuerySet lQs = execute( lWorkPackage );
      lQs.next();

      // Then the job card is returned.
      assertThat( "Unexpected number of rows returned.", lQs.getRowCount(), is( 1 ) );
      assertThat( "Unexpected JIC returned.", lQs.getKey( TaskKey.class, "sched_key" ),
            is( lJobCard ) );
   }


   /**
    * Verify that a job card is not returned by the query when it is assigned to a requirement that
    * has a class of FOLLOW and that requirement is assigned to the provided work package.
    */
   @Test
   public void testThatJicAssignedToRequirementWithClassFollowIsNotReturned() {

      // Given a job card based on a definition that is work scope enabled.
      TaskKey lJobCard = Domain.createJobCard( aJobCard -> {
         aJobCard.setDefinition( Domain.createJobCardDefinition( aDefn -> {
            aDefn.setWorkScopeEnabled( true );
         } ) );
      } );

      // Given a requirement with a class of FOLLOW and with the job card assigned.
      TaskKey lReq = Domain.createRequirement( aReq -> {
         aReq.setTaskClass( FOLLOW );
         aReq.addJobCard( lJobCard );
      } );

      // Given a work package with the requirement is assigned.
      TaskKey lWorkPackage = Domain.createWorkPackage( aWp -> {
         aWp.addTask( lReq );
      } );

      // When the query is executed with the work package.
      QuerySet lQs = execute( lWorkPackage );

      // Then the job card is not returned.
      assertThat( "Unexpected number of rows returned.", lQs.getRowCount(), is( 0 ) );
   }


   /**
    * Verify that a requirement assigned to a provided work package is not returned by the query
    * when its class is FOLLOW.
    *
    * <pre>
    *    Given a requirement with class of Follow.
    *    And a work package with the requirement assigned.
    *    When the query is executed with the work package.
    *    Then the requirement is not returned.
    * </pre>
    */
   @Test
   public void testThatRequirementWihtClassFollowIsNotReturned() {
      TaskKey lFollowOnRequirement = Domain.createRequirement( aFollowOnTask -> {
         aFollowOnTask.setTaskClass( FOLLOW );
      } );

      TaskKey lWorkPackage = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lFollowOnRequirement );
      } );

      QuerySet lQs = execute( lWorkPackage );

      assertThat( "Unexpected number of rows returned.", lQs.getRowCount(), is( 0 ) );
   }


   /**
    * This method executes the query in GetWorkscopeTasks.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The query set returned by the query.
    */
   private QuerySet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aTaskKey, "aTaskDbId", "aTaskId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
