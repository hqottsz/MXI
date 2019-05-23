
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
 * This class tests the com.mxi.mx.core.query.alert.taskdefn.GetExecuteAdvisoriesByTaskRevision
 * query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetExecuteAdvisoriesByTaskRevisionTest {

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
   private TaskAdvisoryKey iExecuteAdvisoryKey;


   /**
    * make sure it returns user_id(s) associated with the advisory role for completed task.
    *
    * @throws Exception
    */
   @Test
   public void testWhenOnlyOneTaskExist() throws Exception {

      withExecuteAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.COMPLETE )
            .build();

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.EXECUTE, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();

      assertEquals( "Unexpected Number of retrieved rows.", 1, iDataSet.getRowCount() );
      assertEquals( "Unexpected User Id", USER_ID, iDataSet.getInt( "user_id" ) );
   }


   /**
    * make sure it returns user_id(s) associated with the advisory role for completed task.
    *
    * @throws Exception
    */
   @Test
   public void testWhenMultipleTaskExist() throws Exception {

      withRevision( REVISION_TWO );
      withExecuteAdvisories( iTaskRev1, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.COMPLETE )
            .build();
      new TaskBuilder().withTaskRevision( iTaskRev2 ).withStatus( RefEventStatusKey.ACTV ).build();

      // Run Query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.EXECUTE, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();

      assertEquals( "Unexpected Number of retrieved rows.", 1, iDataSet.getRowCount() );
      assertEquals( "Unexpected Role Id", USER_ID, iDataSet.getInt( "user_id" ) );
   }


   /**
    * make sure if old revision has no advisory then no data is returned when actual task is
    * completed.
    *
    * @throws Exception
    */
   @Test
   public void testWhenOldRevTaskWithNoAdvisoryExist() throws Exception {

      withRevision( REVISION_TWO );
      // add advisory to revision 2, note that no advisory is added on revision 1
      withExecuteAdvisories( iTaskRev2, ROLE_ID );
      withUser( USER_ID, ROLE_ID );

      new TaskBuilder().withTaskRevision( iTaskRev1 ).withStatus( RefEventStatusKey.COMPLETE )
            .build();
      new TaskBuilder().withTaskRevision( iTaskRev2 ).withStatus( RefEventStatusKey.ACTV ).build();

      // Run Query for task with revision 1, no records should be returned
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskRev1, "aTaskDbId", "aTaskId" );
      lArgs.add( RefTaskAdvisoryTypeKey.EXECUTE, "aTaskAdvisoryTypeDbId", "aTaskAdvisoryTypeCd" );
      lArgs.add( "aRoleId", ROLE_ID );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( "Unexpected Number of retrieved rows.", 0, iDataSet.getRowCount() );
   }


   /**
    * Create Advisories
    */
   private void withExecuteAdvisories( TaskTaskKey aTask, int aRoleId ) {

      iExecuteAdvisoryKey = new TaskAdvisoryKey( aTask, aRoleId, RefTaskAdvisoryTypeKey.EXECUTE );
      TaskAdvisoryTable.create( iExecuteAdvisoryKey ).insert();
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
