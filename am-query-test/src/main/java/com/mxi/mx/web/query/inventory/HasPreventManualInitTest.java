
package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Ensures that <code>HasPreventManualInit</code> query works
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class HasPreventManualInitTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This test case is testing that the prevent manual initialization is returned if there are two
    * requirement definitions. One is with correct prevent manual initialization which sets to true.
    * Another one sets to false.
    *
    * <pre>
    *    Given a requirement definition with prevent manual initialization which sets to true
    *    And  another requirement definition with prevent manual initialization which sets to false
    *    When execute this query HasPreventManualInit.qrx
    *    Then verify the task definition with correct prevent manual initialization is returned
    * </pre>
    */
   @Test
   public void testPreventManualInitializationIsReturned() {

      // Given a requirement definition with prevent manual initialization which sets to false
      final TaskTaskKey lReqDefinition1 = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setPreventManualInitialization( false );
      } );

      // Given a requirement definition with prevent manual initialization which sets to true
      final TaskTaskKey lReqDefinition2 = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setPreventManualInitialization( true );
      } );

      final List<TaskTaskKey> lReqDefinitions = new ArrayList<TaskTaskKey>();
      lReqDefinitions.add( lReqDefinition1 );
      lReqDefinitions.add( lReqDefinition2 );

      // When execute this query HasPreventManualInit.qrx
      final QuerySet lQs = executeQuery( lReqDefinitions );

      // verify the return has the prevent manual initialization
      final int lExpectedRowCount = 1;
      final int lActualRowCount = lQs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );

      lQs.first();

      final boolean lActualHasPreventManualInitialization =
            lQs.getBoolean( "has_prevent_manual_init" );
      assertTrue( lActualHasPreventManualInitialization );
   }


   /**
    * This test case is testing that the empty is returned if there are two requirement definitions
    * with prevent manual initialization which sets to false.
    *
    * <pre>
    *    Given two requirement definitions with prevent manual initialization which sets to false
    *    When execute this query HasPreventManualInit.qrx
    *    Then verify the empty is returned
    * </pre>
    */
   @Test
   public void testEmptyIsReturned() {

      // Given a requirement definition with prevent manual initialization which sets to false
      final TaskTaskKey lReqDefinition1 = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setPreventManualInitialization( false );
      } );

      // Given a requirement definition with prevent manual initialization which sets to false
      final TaskTaskKey lReqDefinition2 = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setPreventManualInitialization( false );
      } );

      final List<TaskTaskKey> lReqDefinitions = new ArrayList<TaskTaskKey>();
      lReqDefinitions.add( lReqDefinition1 );
      lReqDefinitions.add( lReqDefinition2 );

      // When execute this query HasPreventManualInit.qrx
      final QuerySet lQs = executeQuery( lReqDefinitions );

      // verify the return has the prevent manual initialization
      final int lExpectedRowCount = 0;
      final int lActualRowCount = lQs.getRowCount();
      assertEquals( "Unexpected Number of Rows returned", lExpectedRowCount, lActualRowCount );
   }


   private DataSet executeQuery( List<TaskTaskKey> aReqDefinitions ) {
      final List<String> lTaskTaskDbIds = new ArrayList<>();
      final List<String> lTaskTaskIds = new ArrayList<>();
      aReqDefinitions.forEach( item -> {
         lTaskTaskDbIds.add( String.valueOf( item.getDbId() ) );
         lTaskTaskIds.add( String.valueOf( item.getId() ) );

      } );

      final DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhere( "(task_task.task_db_id, task_task.task_id) IN ("
            + "SELECT * FROM TABLE ( ARRAY_PKG.getStrStrTable( "
            + ":aBindTaskTaskDbIds, :aBindTaskTaskIds )))" );
      lArgs.addStringArray( "aBindTaskTaskDbIds", lTaskTaskDbIds );
      lArgs.addStringArray( "aBindTaskTaskIds", lTaskTaskIds );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
