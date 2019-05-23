package com.mxi.mx.core.query.taskdefn;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.task.TaskTaskDepTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Test com.mxi.mx.core.query.taskdefn.TaskDependencies query
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskDependenciesTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * This scenario consists of a task definition (REQ) that has multiple revisions. The last
    * revision does not include any relationship to other task definition or itself (ie: it is not
    * recurring, does not follow any other task, or has no previous link to another task). Then,
    * while the last revision is being activated the execution of the TaskDependencies query will
    * not bring any result.
    *
    * <ul>
    * <li>Create a REQ with revision 1, in SUPERSEDE status</li>
    * <li>Create REQ revision 2, in ACTV status</li>
    * <li>The REQ revision 2 is recurrent thus, a record in task_task_dep table is created</li>
    * <li>Create REQ revision 3, in REVISION status</li>
    * </ul>
    */
   @Test
   public void testTaskHasMultipleRevisions() {

      // Create REQ 1 revision 1 record
      TaskTaskKey lTaskRevision_Req_Rev1 = new TaskRevisionBuilder()
            .withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
            .withStatus( RefTaskDefinitionStatusKey.SUPRSEDE ).withRevisionNumber( 1 ).build();

      // Get the task definition key
      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskRevision_Req_Rev1 );
      TaskDefnKey lTaskDefnKey = lTaskTaskTable.getTaskDefn();

      // Create REQ 1 revision 2 record
      TaskTaskKey lTaskRevision_Req_Rev2 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
                  .withStatus( RefTaskDefinitionStatusKey.ACTV ).withRevisionNumber( 2 )
                  .isRecurring().withTaskDefn( lTaskDefnKey ).build();

      // REQ 1 revision 2 is recurring thus it is required
      // to insert a recod in the task_task_dep table
      TaskTaskDepTable lTaskTaskDep = TaskTaskDepTable
            .create( TaskTaskDepTable.generatePrimaryKey( lTaskRevision_Req_Rev2 ) );
      lTaskTaskDep.setDepTaskDefn( lTaskDefnKey );
      lTaskTaskDep.insert();

      // Create REQ 1 revision 3 record
      TaskTaskKey lTaskRevision_Req_Rev3 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
                  .withStatus( RefTaskDefinitionStatusKey.REVISION ).withRevisionNumber( 3 )
                  .withTaskDefn( lTaskDefnKey ).build();

      // Build query arguments and execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTaskRevision_Req_Rev3, "aTaskDbId", "aTaskId" );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that no rows are retrieved by the query
      assertEquals( "Number of retrieved rows", 0, iDataSet.getRowCount() );
   }


   /**
    * This scenario consists of task definition (REQ 2) that follows task definition (REQ 1). While
    * REQ 2 is being activated the execution of the TaskDependencies query will bring REQ 1 since,
    * REQ 2 depends on REQ 1 thus, REQ 2 is a dependant task definition.
    *
    * <ul>
    * <li>Create REQ 1 with revision 1, in ACTV status</li>
    * <li>Create REQ 2 with revision 1, in BUILD status</li>
    * <li>REQ 1 follows REQ 2 thus, a record in task_task_dep table is created</li>
    * </ul>
    */
   @Test
   public void testTaskIsDependant() {

      // Create REQ 1
      TaskTaskKey lTaskRevision_Req1 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
                  .withStatus( RefTaskDefinitionStatusKey.ACTV ).withRevisionNumber( 1 ).build();

      // Get the task definition key for REQ 1
      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskRevision_Req1 );
      TaskDefnKey lTaskDefnKey = lTaskTaskTable.getTaskDefn();

      // Create REQ 2
      TaskTaskKey lTaskRevision_Req2 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_2" )
                  .withStatus( RefTaskDefinitionStatusKey.BUILD ).withRevisionNumber( 1 ).build();

      // REQ 2 follows REQ 1, then create the relationship in task_task_dep table
      TaskTaskDepTable lTaskTaskDep =
            TaskTaskDepTable.create( TaskTaskDepTable.generatePrimaryKey( lTaskRevision_Req2 ) );
      lTaskTaskDep.setDepTaskDefn( lTaskDefnKey );
      lTaskTaskDep.setTaskDepAction( RefTaskDepActionKey.COMPLETE );
      lTaskTaskDep.insert();

      // Build query arguments and execute query for REQ 2
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTaskRevision_Req2, "aTaskDbId", "aTaskId" );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that only one row is retrieved by the query
      assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      // Assert that REQ 2 is dependant of REQ 1 then the REQ 1 task def key will be returned
      // by the query
      iDataSet.first();
      assertEquals( "task_def_key", lTaskDefnKey,
            iDataSet.getKey( TaskDefnKey.class, "task_def_key" ) );
   }


   /**
    * This scenario consists of task definition (REQ 2) that follows task definition (REQ 1). While
    * REQ 1 is being activated the execution of the TaskDependencies query will bring REQ 2 since,
    * REQ 1 has REQ 2 as dependant.
    *
    * <ul>
    * <li>Create REQ 1 with revision 1, in BUILD status</li>
    * <li>Create REQ 2 with revision 1, in BUILD status</li>
    * <li>REQ 1 follows REQ 2 thus, a record in task_task_dep table is created</li>
    * </ul>
    */
   @Test
   public void testTaskIsDependency() {

      // Create REQ 1
      TaskTaskKey lTaskRevision_Req1 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_1" )
                  .withStatus( RefTaskDefinitionStatusKey.BUILD ).withRevisionNumber( 1 ).build();

      // Get the task definition key for REQ 1
      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskRevision_Req1 );
      TaskDefnKey lTaskDefnKey_Req1 = lTaskTaskTable.getTaskDefn();

      // Create REQ 2
      TaskTaskKey lTaskRevision_Req2 =
            new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskCode( "REQ_2" )
                  .withStatus( RefTaskDefinitionStatusKey.BUILD ).withRevisionNumber( 1 ).build();

      // REQ 2 follows REQ 1, then create the relationship in task_task_dep table
      TaskTaskDepTable lTaskTaskDep =
            TaskTaskDepTable.create( TaskTaskDepTable.generatePrimaryKey( lTaskRevision_Req2 ) );
      lTaskTaskDep.setDepTaskDefn( lTaskDefnKey_Req1 );
      lTaskTaskDep.setTaskDepAction( RefTaskDepActionKey.COMPLETE );
      lTaskTaskDep.insert();

      // Get the task definition key for REQ 2
      lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskRevision_Req2 );

      TaskDefnKey lTaskDefnKey_Req2 = lTaskTaskTable.getTaskDefn();

      // Build query arguments and execute query for REQ 1
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTaskRevision_Req1, "aTaskDbId", "aTaskId" );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that only one row is retrieved by the query
      assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      // Assert that REQ 1 is dependency since REQ 2 depends on it in this association
      // then, the REQ 2 task def key will be returned by the query
      iDataSet.first();
      assertEquals( "task_def_key", lTaskDefnKey_Req2,
            iDataSet.getKey( TaskDefnKey.class, "task_def_key" ) );
   }


   /**
    * This scenario consists of a task definition (REQ 1) that is being activated, and since it is
    * recurring it will be gathered by the execution of TaskDependencies query while trying to
    * determine if has dependencies or is dependant. A recurring task has a dependency to itself.
    *
    * <ul>
    * <li>Create a REQ with revision 1, in BUILD status</li>
    * <li>The REQ is recurrent thus, a record in task_task_dep table is created</li>
    * </ul>
    */
   @Test
   public void testTaskIsRecurrent() {

      // Create the task definition
      TaskTaskKey lTaskRevision_Req = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskCode( "REQ_1" ).withStatus( RefTaskDefinitionStatusKey.BUILD )
            .withRevisionNumber( 1 ).isRecurring().build();

      // Get the task definition key
      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lTaskRevision_Req );
      TaskDefnKey lTaskDefnKey = lTaskTaskTable.getTaskDefn();

      // Since the task defn is recurring it is required
      // to insert a recod in the task_task_dep table
      TaskTaskDepTable lTaskTaskDep =
            TaskTaskDepTable.create( TaskTaskDepTable.generatePrimaryKey( lTaskRevision_Req ) );
      lTaskTaskDep.setDepTaskDefn( lTaskDefnKey );
      lTaskTaskDep.insert();

      // Build query arguments and execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lTaskRevision_Req, "aTaskDbId", "aTaskId" );

      iDataSet = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Assert that only one row is retrieved by the query
      assertEquals( "Number of retrieved rows", 1, iDataSet.getRowCount() );

      // Assert that REQ 1 is dependent to it-self, then
      // the REQ 1 task def key will be returned by the query
      iDataSet.first();
      assertEquals( "task_def_key", lTaskDefnKey,
            iDataSet.getKey( TaskDefnKey.class, "task_def_key" ) );
   }
}
