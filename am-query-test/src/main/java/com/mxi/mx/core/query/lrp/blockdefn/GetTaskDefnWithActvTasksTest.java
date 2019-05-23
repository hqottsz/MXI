
package com.mxi.mx.core.query.lrp.blockdefn;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.key.MxKey;
import com.mxi.mx.core.unittest.MxAssert;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;


/**
 * Tests that query returns only definitions that have actv tasks.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTaskDefnWithActvTasksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetTaskDefnWithActvTasksTest.class );
   }


   /** The query execution data set. */
   private DataSet iDataSet = null;


   /**
    * In this case, since both definitions do not have an active task, there should be no definition
    * keys returned.
    */
   @Test
   public void testTaskDefinitionWithActvTaskDoesNotExists() {

      // Build query arguments.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAcftDbId", 4650 );
      lArgs.add( "aAcftId", 1 );

      List<MxKey> lList = new ArrayList<MxKey>();
      lList.add( new MxKey( "4650:1" ) );
      lList.add( new MxKey( "4650:2" ) );

      lArgs.addWhereIn( "WHERE_TASK_TASK",
            new String[] { "task_task.task_defn_db_id", "task_task.task_defn_id" }, lList );

      // Execute the query.
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      MxAssert.assertEquals( false, iDataSet.first() );
   }


   /**
    * In this case, there should be one active task definition key returned.
    */
   @Test
   public void testTaskDefinitionWithActvTaskExists() {

      // Build query arguments.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAcftDbId", 4650 );
      lArgs.add( "aAcftId", 2 );

      List<MxKey> lList = new ArrayList<MxKey>();
      lList.add( new MxKey( "4650:3" ) );
      lList.add( new MxKey( "4650:4" ) );

      lArgs.addWhereIn( "WHERE_TASK_TASK",
            new String[] { "task_task.task_defn_db_id", "task_task.task_defn_id" }, lList );

      // Execute the query.
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      MxAssert.assertEquals( true, iDataSet.first() );

      IdKey lKey = new IdKeyImpl( iDataSet.getInt( "task_defn_db_id" ),
            iDataSet.getInt( "task_defn_id" ) );
      MxAssert.assertNotNull( lKey );
      MxAssert.assertEquals( new IdKeyImpl( 4650, 4 ), lKey );
      MxAssert.assertEquals( false, iDataSet.next() );
   }


   /**
    * In this case, there should be one inwork/active task definition key returned.
    */
   @Test
   public void testTaskDefinitionWithInWorkTaskExists() {

      // Build query arguments.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAcftDbId", 4650 );
      lArgs.add( "aAcftId", 3 );

      List<MxKey> lList = new ArrayList<MxKey>();
      lList.add( new MxKey( "4650:5" ) );
      lList.add( new MxKey( "4650:6" ) );

      lArgs.addWhereIn( "WHERE_TASK_TASK",
            new String[] { "task_task.task_defn_db_id", "task_task.task_defn_id" }, lList );

      // Execute the query.
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      MxAssert.assertEquals( true, iDataSet.first() );

      IdKey lKey = new IdKeyImpl( iDataSet.getInt( "task_defn_db_id" ),
            iDataSet.getInt( "task_defn_id" ) );
      MxAssert.assertNotNull( lKey );
      MxAssert.assertEquals( new IdKeyImpl( 4650, 6 ), lKey );
      MxAssert.assertEquals( false, iDataSet.next() );
   }


   /**
    * In this case, there should be one paused/active task definition key returned.
    */
   @Test
   public void testTaskDefinitionWithPausedTaskExists() {

      // Build query arguments.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAcftDbId", 4650 );
      lArgs.add( "aAcftId", 4 );

      List<MxKey> lList = new ArrayList<MxKey>();
      lList.add( new MxKey( "4650:7" ) );
      lList.add( new MxKey( "4650:8" ) );

      lArgs.addWhereIn( "WHERE_TASK_TASK",
            new String[] { "task_task.task_defn_db_id", "task_task.task_defn_id" }, lList );

      // Execute the query.
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      MxAssert.assertEquals( true, iDataSet.first() );

      IdKey lKey = new IdKeyImpl( iDataSet.getInt( "task_defn_db_id" ),
            iDataSet.getInt( "task_defn_id" ) );
      MxAssert.assertNotNull( lKey );
      MxAssert.assertEquals( new IdKeyImpl( 4650, 8 ), lKey );
      MxAssert.assertEquals( false, iDataSet.next() );
   }
}
