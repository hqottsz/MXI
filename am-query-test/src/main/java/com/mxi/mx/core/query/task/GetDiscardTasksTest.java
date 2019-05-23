
package com.mxi.mx.core.query.task;

import static com.mxi.am.domain.Domain.createRepairOrder;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RepairOrder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Tests getDiscardTasks.qrx query
 *
 * @author nsubotic
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetDiscardTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final String ACTIVE_TASK = "Discard Actv";
   public static final String TERMINATE_TASK = "Discard Terminate";
   public static final String CANCEL_TASK = "Discard Cancel";
   public static final String ERROR_TASK = "Discard Error";
   public static final String NA_TASK = "Discard Na";


   /**
    * Tests query getDiscardTasks.qrx that returns DISCARD tasks for a work package. DISCARD tasks
    * with following statuses are excluded from the returned set:
    *
    * <ul>
    * <li>'CANCEL'</li>,
    * <li>'ERROR',</li>
    * <li>'N/A'</li>,
    * <li>'TERMINATE'</li>
    * </ul>
    */
   @Test
   public void testGetDiscardTasks() {

      // parent inventory for work package and tasks under that work package
      final InventoryKey lParentInventory = new InventoryBuilder().build();

      // build and insert into database work package
      TaskKey lWorkPackageTask = createRepairOrder( new DomainConfiguration<RepairOrder>() {

         @Override
         public void configure( RepairOrder aRepairOrder ) {
            aRepairOrder.setMainInventory( lParentInventory );
         }
      } );

      // add tasks with different statuses to database
      addTask( lParentInventory, lWorkPackageTask, RefEventStatusKey.ACTV, ACTIVE_TASK );
      addTask( lParentInventory, lWorkPackageTask, RefEventStatusKey.TERMINATE, TERMINATE_TASK );
      addTask( lParentInventory, lWorkPackageTask, RefEventStatusKey.CANCEL, CANCEL_TASK );
      addTask( lParentInventory, lWorkPackageTask, RefEventStatusKey.ERROR, ERROR_TASK );
      addTask( lParentInventory, lWorkPackageTask, RefEventStatusKey.NA, NA_TASK );

      // execute the query
      Set<String> lTasks = execute( lWorkPackageTask.getEventKey() );
      String lTask = lTasks.iterator().next();

      // assert that only ACTV task is returned
      assertEquals( "only one task is returned", 1, lTasks.size() );
      assertEquals( "only active task is returned", ACTIVE_TASK, lTask );
   }


   /**
    * Builds and adds a task to SchedStaskTable and EvtEvent table.
    *
    * @param aParentInventory
    *           task's inentory
    * @param aWorkPackageTask
    *           the work package to assign the task to it
    * @param aRefEventStatusKey
    *           task's status
    * @param aName
    *           task's name
    */
   private void addTask( InventoryKey aParentInventory, TaskKey aWorkPackageTask,
         RefEventStatusKey aRefEventStatusKey, String aName ) {

      new TaskBuilder().onInventory( aParentInventory ).withTaskClass( RefTaskClassKey.DISCARD )
            .withStatus( aRefEventStatusKey ).withParentTask( aWorkPackageTask ).withName( aName )
            .build();
   }


   /**
    * Executes the query
    *
    * @param aWorkPackageTask
    *           the work package
    *
    * @return the set of DISCARD tasks excluding tasks with statuses
    *         <ul>
    *         <li>'CANCEL'</li>,
    *         <li>'ERROR',</li>
    *         <li>'N/A'</li>,
    *         <li>'TERMINATE'</li>
    *         </ul>
    */
   private Set<String> execute( EventKey aWorkPackageTask ) {
      Set<String> lTasks = new HashSet<String>();
      DataSetArgument lDsArgs = new DataSetArgument();
      lDsArgs.add( "aEventDbId", aWorkPackageTask.getDbId() );
      lDsArgs.add( "aEventId", aWorkPackageTask.getId() );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.getDiscardTasks", lDsArgs );
      while ( lQs.next() ) {
         lTasks.add( lQs.getString( "task_name" ) );
      }

      return lTasks;
   }
}
