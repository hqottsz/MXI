
package com.mxi.mx.db.view.reqtasks;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskJicReqMapTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Tests the ReqTasks View
 */
@RunWith( Theories.class )
public final class ReqTasksTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @DataPoint
   public static final InventoryKey AIRCRAFT_1 = new InventoryKey( 4650, 1 );

   @DataPoint
   public static final InventoryKey SYS_11 = new InventoryKey( 4650, 11 );

   @DataPoint
   public static final InventoryKey TRK_111 = new InventoryKey( 4650, 111 );

   @DataPoint
   public static final InventoryKey TRK_1111 = new InventoryKey( 4650, 1111 );

   @DataPoint
   public static final InventoryKey ENGINE_121 = new InventoryKey( 4650, 121 );

   @DataPoint
   public static final InventoryKey TRK_12111 = new InventoryKey( 4650, 12111 );

   @DataPoint
   public static final InventoryKey SYS_1221 = new InventoryKey( 4650, 1221 );

   @DataPoint
   public static final InventoryKey TRK_122111 = new InventoryKey( 4650, 122111 );

   @DataPoint
   public static final InventoryKey ENGINE_2 = new InventoryKey( 4650, 2 );

   @DataPoint
   public static final InventoryKey SYS_21 = new InventoryKey( 4650, 21 );

   @DataPoint
   public static final InventoryKey TRK_211 = new InventoryKey( 4650, 211 );

   @DataPoint
   public static final InventoryKey TRK_2111 = new InventoryKey( 4650, 2111 );

   @DataPoint
   public static final InventoryKey TRK_3 = new InventoryKey( 4650, 3 );

   @DataPoint
   public static final InventoryKey TRK_31 = new InventoryKey( 4650, 31 );

   public static final TaskDefnKey REQ_DEFN_4 = new TaskDefnKey( 4650, 4 );

   public static final TaskTaskKey REQ_REV_41 = new TaskTaskKey( 4650, 41 );

   public static final TaskKey REQ_411 = new TaskKey( 4650, 411 );

   public static final TaskKey REQ_412 = new TaskKey( 4650, 412 );

   public static final TaskTaskKey JIC_4111 = new TaskTaskKey( 4650, 4111 );

   public static final TaskDefnKey REQ_DEFN_5 = new TaskDefnKey( 4650, 5 );

   public static final TaskTaskKey REQ_REV_51 = new TaskTaskKey( 4650, 51 );

   public static final TaskKey REQ_511 = new TaskKey( 4650, 511 );

   public static final TaskTaskKey JIC_5111 = new TaskTaskKey( 4650, 5111 );


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /**
    * This test ensures that historic tasks do not return rows.
    *
    * @throws SQLException
    */
   @Test
   public void testHistoricReqNotReturned() throws SQLException {
      withTaskDefn( REQ_DEFN_4, REQ_REV_41 );
      withJICMapping( REQ_DEFN_4, JIC_4111 );
      withTaskForInv( REQ_411, REQ_REV_41, ENGINE_121, false );
      withTaskForInv( REQ_412, REQ_REV_41, ENGINE_121, true );

      Collection<TaskKey> lReqsForEngine121 = reqsFor( ENGINE_121 );

      assertThat( sizeOf( lReqsForEngine121 ), is( equalTo( 1 ) ) );
      assertThat( REQ_411, isIn( lReqsForEngine121 ) );
      assertThat( REQ_412, not( isIn( lReqsForEngine121 ) ) );
   }


   /**
    * Tests a simple mapping for all inventory data. See the inventory tree in ReqTasksTest.xml.
    *
    * @param aInventory
    *           The inventory data point
    */
   @Theory
   @Test
   public void testMappingForInventory( InventoryKey aInventory ) {
      withTaskDefn( REQ_DEFN_4, REQ_REV_41 );
      withJICMapping( REQ_DEFN_4, JIC_4111 );
      withTaskForInv( REQ_411, REQ_REV_41, aInventory );

      Collection<TaskTaskKey> lJicsForInv = jicsFor( aInventory );

      assertThat( sizeOf( lJicsForInv ), is( equalTo( 1 ) ) );
      assertThat( JIC_4111, isIn( lJicsForInv ) );
   }


   /**
    * Tests multiple mappings for an engine attached to a system on an aircraft.
    */
   @Test
   public void testMultipleMappingsForInventory() {
      withTaskDefn( REQ_DEFN_4, REQ_REV_41 );
      withJICMapping( REQ_DEFN_4, JIC_4111 );
      withTaskForInv( REQ_411, REQ_REV_41, ENGINE_121 );

      withTaskDefn( REQ_DEFN_5, REQ_REV_51 );
      withJICMapping( REQ_DEFN_5, JIC_5111 );
      withTaskForInv( REQ_511, REQ_REV_51, ENGINE_121 );

      Collection<TaskTaskKey> lJicsForEngine121 = jicsFor( ENGINE_121 );

      assertThat( sizeOf( lJicsForEngine121 ), is( equalTo( 2 ) ) );
      assertThat( JIC_4111, isIn( lJicsForEngine121 ) );
      assertThat( JIC_5111, isIn( lJicsForEngine121 ) );
   }


   /**
    * Returns the JIC revisions that are returned by the view using the given inventory as the
    * context.
    *
    * @param aInventoryKey
    *           The context inventory
    *
    * @return The JIC revisions returned by the view.
    */
   private Collection<TaskTaskKey> jicsFor( InventoryKey aInventoryKey ) {

      // Set the inventory context
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "filter_inv_no_db_id", aInventoryKey.getDbId() );
      lArgs.add( "filter_inv_no_id", aInventoryKey.getId() );

      // Get the data from the view
      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQueryTable( "VW_REQ_TASKS", lArgs );

      List<TaskTaskKey> lTasks = new ArrayList<TaskTaskKey>( lQuerySet.getRowCount() );

      while ( lQuerySet.next() ) {
         lTasks.add( lQuerySet.getKey( TaskTaskKey.class, "jic_task_db_id", "jic_task_id" ) );
      }

      return lTasks;
   }


   /**
    * Returns the actual REQ tasks that are returned by the view using the given inventory as the
    * context.
    *
    * @param aInventoryKey
    *           The context inventory
    *
    * @return The JIC revisions returned by the view.
    */
   private Collection<TaskKey> reqsFor( InventoryKey aInventoryKey ) {

      // Set the inventory context
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "filter_inv_no_db_id", aInventoryKey.getDbId() );
      lArgs.add( "filter_inv_no_id", aInventoryKey.getId() );

      // Get the data from the view
      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQueryTable( "VW_REQ_TASKS", lArgs );

      List<TaskKey> lTasks = new ArrayList<TaskKey>( lQuerySet.getRowCount() );

      while ( lQuerySet.next() ) {
         lTasks.add( lQuerySet.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      }

      return lTasks;
   }


   /**
    * Readable method that gets the size of a collection.
    *
    * @param aCollection
    *           The collection
    *
    * @return The size of the collection.
    */
   private <T> int sizeOf( Collection<T> aCollection ) {
      return aCollection.size();
   }


   /**
    * Creates a mapping between the given REQ task definition and the JIC task definition revision.
    *
    * @param aReqDefn
    *           The REQ task definition
    * @param aJic
    *           The JIC task definition revision
    */
   private void withJICMapping( TaskDefnKey aReqDefn, TaskTaskKey aJic ) {
      TaskJicReqMapTable lMapping = TaskJicReqMapTable.create( aJic, aReqDefn );
      lMapping.insert();
   }


   /**
    * Creates a task definition.
    *
    * @param aTaskDefn
    *           The task definition
    * @param aTaskDefnRevision
    *           The task definition revision
    */
   private void withTaskDefn( TaskDefnKey aTaskDefn, TaskTaskKey aTaskDefnRevision ) {
      TaskTaskTable lTaskTask = TaskTaskTable.create();
      lTaskTask.setTaskDefn( aTaskDefn );
      lTaskTask.insert( aTaskDefnRevision );
   }


   /**
    * Creates an actual task for the given inventory.
    *
    * @param aTaskKey
    *           The task key
    * @param aTaskDefnRevision
    *           The task definition
    * @param aInventoryKey
    *           The main inventory
    */
   private void withTaskForInv( TaskKey aTaskKey, TaskTaskKey aTaskDefnRevision,
         InventoryKey aInventoryKey ) {
      withTaskForInv( aTaskKey, aTaskDefnRevision, aInventoryKey, false );
   }


   /**
    * Creates an actual task for the given inventory.
    *
    * @param aTaskKey
    *           The task key
    * @param aTaskDefnRevision
    *           The task definition
    * @param aInventoryKey
    *           The main inventory
    * @param aHistoric
    *           Whather or nto the task is historic
    */
   private void withTaskForInv( TaskKey aTaskKey, TaskTaskKey aTaskDefnRevision,
         InventoryKey aInventoryKey, boolean aHistoric ) {
      EvtInvTable lEvtInv = EvtInvTable.create( aTaskKey );
      lEvtInv.setInventoryKey( aInventoryKey );
      lEvtInv.setMainInvBool( true );
      lEvtInv.insert();

      EvtEventTable lEvtEvent = EvtEventTable.create( aTaskKey.getEventKey() );
      lEvtEvent.setHEvent( aTaskKey.getEventKey() );
      lEvtEvent.setHistBool( aHistoric );
      lEvtEvent.insert();

      SchedStaskTable lSchedStask = SchedStaskTable.create( aTaskKey );
      lSchedStask.setTaskTaskKey( aTaskDefnRevision );
      lSchedStask.insert();
   }
}
