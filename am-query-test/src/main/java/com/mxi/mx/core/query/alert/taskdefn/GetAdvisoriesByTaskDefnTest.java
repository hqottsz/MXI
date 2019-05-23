
package com.mxi.mx.core.query.alert.taskdefn;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskAdvisoryTypeKey;
import com.mxi.mx.core.key.TaskAdvisoryKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskAdvisoryTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Ensures <code>GetAdvisoriesByTaskDefn</code> query functions properly
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAdvisoriesByTaskDefnTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final int REVISION_TWO = 2;
   public static final int ROLE_ID = 100;
   private static final int USER_ID = 1;

   /** The query execution data set */
   private DataSet iDataSet = null;
   private TaskTaskKey iTaskRev1;
   private TaskTaskKey iTaskRev2;
   private TaskDefnKey iTaskDefn;
   private TaskAdvisoryKey iFleetAdvisoryKey;


   /**
    * <pre>
    * Given:
    * 1)
    * -task definition
    * 2)
    * -Rev1 Task based on task definition
    * ---No task created
    * 
    * When  Rev1 key is passed to the query, it should not return user_id(s) as there is
    * no task for the task definition.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenNoTaskOfSameRevisionExist() throws Exception {

      withFleetAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.FLEET, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Unexpected Number of retrieved rows.", 0, iDataSet.getRowCount() );
   }


   /**
    * <pre>
    * Given:
    * 1)
    * -task definition
    * 2)
    * -Rev1 Task based on task definition
    * ---task is active
    * 
    * When Task Rev1 task is passed to the query, it should not return user_id(s) as there is
    * active task(Rev1) for the task definition.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenOnlyOneActiveTaskOfSameRevisionExist() throws Exception {

      withFleetAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.ACTV ).build();

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.FLEET, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Unexpected Number of retrieved rows.", 0, iDataSet.getRowCount() );
   }


   /**
    * <pre>
    * Given:
    * 1)
    * -task definition
    * 2)
    * -Rev1 Task based on task definition
    * ---task is complete
    * 
    * When Task Rev1 task is passed to the query, it should return user_id(s) as there is
    * no active task for the task definition.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenOnlyOneCompletedTaskOfSameRevisionExist() throws Exception {

      withFleetAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.COMPLETE )
            .build();

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.FLEET, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();

      assertEquals( "Unexpected Number of retrieved rows.", 1, iDataSet.getRowCount() );
      assertEquals( "Unexpected Role Id", USER_ID, iDataSet.getInt( "user_id" ) );
   }


   /**
    * <pre>
    * Given:
    * 1)
    * -task definition
    * 2)
    * -Rev1 Task based on task definition
    * ---2 tasks - one active and one complete
    * 
    * When Task Rev1 task is passed to the query, it should not return user_id(s) as there is
    * an active task(Rev1) for the task definition.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenAtLeastOneActiveTaskOfSameRevisoinExist() throws Exception {

      withFleetAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.ACTV ).build();
      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.COMPLETE )
            .build();

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.FLEET, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Unexpected Number of retrieved rows.", 0, iDataSet.getRowCount() );
   }


   /**
    * <pre>
    * Given:
    * 1)
    * -task definition
    * 2)
    * -Rev1 active Task based on task definition
    * 3)
    *  Rev2 active task based on task definition
    * 
    * When Task Rev1 task is passed to the query, it should not return user_id(s) as there is
    * active task(Rev2) for the task definition.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenActiveTaskOfDifferentRevisionsExist() throws Exception {

      withRevision( REVISION_TWO );
      withFleetAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.ACTV ).build();
      new TaskBuilder().withTaskRevision( iTaskRev2 ).withStatus( RefEventStatusKey.ACTV ).build();

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.FLEET, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Unexpected Number of retrieved rows.", 0, iDataSet.getRowCount() );
   }


   /**
    * <pre>
    * Given:
    * 1)
    * -task definition
    * 2)
    * -Rev1 COMPLETED Task based on task definition
    * 3)
    *  Rev2 active task based on task definition
    * 
    * When Task Rev1 task is passed to the query, it should not return user_id(s) as there is
    * active task(Rev2) for the task definition.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenAtLeastOneActiveTaskOfDifferentRevisionsExist() throws Exception {

      withRevision( REVISION_TWO );
      withFleetAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.COMPLETE )
            .build();
      new TaskBuilder().withTaskRevision( iTaskRev2 ).withStatus( RefEventStatusKey.ACTV ).build();

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.FLEET, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Unexpected Number of retrieved rows.", 0, iDataSet.getRowCount() );
   }


   /**
    * <pre>
    * Given:
    * 1)
    * -task definition
    * 2)
    * -Rev1 COMPLETED Task based on task definition
    * 3)
    *  Rev2 COMPLETED task based on task definition
    * 
    * When Task Rev1 task is passed to the query, it should return user_id(s) as there is
    * no active task for both revisions of the task definition.
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenNoActiveTaskOfDifferentRevisionsExist() throws Exception {

      withRevision( REVISION_TWO );
      withFleetAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.COMPLETE )
            .build();
      new TaskBuilder().withTaskRevision( iTaskRev2 ).withStatus( RefEventStatusKey.COMPLETE )
            .build();

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.FLEET, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();

      assertEquals( "Unexpected Number of retrieved rows.", 1, iDataSet.getRowCount() );
      assertEquals( "Unexpected Role Id", USER_ID, iDataSet.getInt( "user_id" ) );
   }


   /**
    * Create Advisories
    */
   private void withFleetAdvisories( TaskTaskKey aTask, int aRoleId ) {

      iFleetAdvisoryKey = new TaskAdvisoryKey( aTask, aRoleId, RefTaskAdvisoryTypeKey.FLEET );
      TaskAdvisoryTable.create( iFleetAdvisoryKey ).insert();
   }


   /**
    * Create second revision
    */

   private void withRevision( int aRevisionNumber ) {

      iTaskRev2 = new TaskRevisionBuilder().withTaskDefn( iTaskDefn )
            .withRevisionNumber( aRevisionNumber ).build();
   }


   /**
    * Set up user.
    */
   private void withUser( int aUserId, int aRoleId ) {

      DataSetArgument lArgsUsr = new DataSetArgument();
      lArgsUsr.add( "user_id", aUserId );
      lArgsUsr.add( "role_id", aRoleId );
      lArgsUsr.add( "role_order", 1 );
      MxDataAccess.getInstance().executeInsert( "utl_user_role", lArgsUsr );
   }


   /**
    *
    * {@inheritDoc}
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void setUp() throws Exception {
      iTaskRev1 = new TaskRevisionBuilder().build();
      iTaskDefn = TaskTaskTable.findByPrimaryKey( iTaskRev1 ).getTaskDefn();
   }
}
