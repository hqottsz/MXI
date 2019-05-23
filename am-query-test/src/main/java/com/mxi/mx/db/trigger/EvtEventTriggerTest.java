
package com.mxi.mx.db.trigger;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * This class tests the logic triggers on EVT_EVENT.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class EvtEventTriggerTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The first event */
   private static final EventKey EVENT1 = new EventKey( 4650, 1001 );

   /** The second event */
   private static final EventKey EVENT2 = new EventKey( 4650, 1002 );

   /** The first task */
   private static final TaskKey TASK1 = new TaskKey( EVENT1 );

   /** The first deadline */
   private static final EventDeadlineKey DEADLINE1 =
         new EventDeadlineKey( EVENT1, DataTypeKey.HOURS );


   /**
    * Tests the TIUAR_EVT_EVENT_HIST_BOOL trigger works correctly after inserting a row in evt_event
    * when a deadline exists.
    *
    * @throws Exception
    *            If an error occurs.
    */

   public void testAfterInsertHistBoolWithDeadline() throws Exception {

      createBasicDeadline( DEADLINE1 );

      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.setEventType( RefEventTypeKey.TS );
      lEvtEvent.insert();

      lEvtEvent = EvtEventTable.findByPrimaryKey( EVENT1 );

      Assert.assertFalse( lEvtEvent.getHistBool() );

      assertEvtSchedDeadHistBool( false );
   }


   /**
    * Tests the TIUAR_EVT_EVENT_HIST_BOOL trigger works correctly after inserting a row in evt_event
    * when a historic task exists.
    *
    * @throws Exception
    *            If an error occurs.
    */

   public void testAfterInsertHistBoolWithHistoricTask() throws Exception {

      // historic-task
      SchedStaskTable lTask = SchedStaskTable.create( TASK1 );
      lTask.insert();

      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.setEventType( RefEventTypeKey.TS );
      lEvtEvent.setHistBool( true );
      lEvtEvent.insert();

      lEvtEvent = EvtEventTable.findByPrimaryKey( EVENT1 );

      Assert.assertTrue( lEvtEvent.getHistBool() );

      lTask = SchedStaskTable.findByPrimaryKey( TASK1 );

      Assert.assertTrue( lTask.isHistoric() );
   }


   /**
    * Tests the TIUAR_EVT_EVENT_HIST_BOOL trigger works correctly after inserting a row in evt_event
    * when no task or deadline exists.
    *
    * @throws Exception
    *            If an error occurs.
    */

   public void testAfterInsertHistBoolWithNoTaskOrDeadline() throws Exception {
      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.insert();

      lEvtEvent = EvtEventTable.findByPrimaryKey( EVENT1 );

      Assert.assertFalse( "evt_event.hist_bool must be false", lEvtEvent.getHistBool() );

      lEvtEvent = EvtEventTable.create( EVENT2 );
      lEvtEvent.setEventType( RefEventTypeKey.TS );
      lEvtEvent.setHistBool( true );
      lEvtEvent.insert();

      lEvtEvent = EvtEventTable.findByPrimaryKey( EVENT2 );

      Assert.assertTrue( "evt_event.hist_bool must be true", lEvtEvent.getHistBool() );
   }


   /**
    * Tests the TIUAR_EVT_EVENT_HIST_BOOL trigger works correctly after inserting a row in evt_event
    * when a task exists.
    *
    * @throws Exception
    *            If an error occurs.
    */

   public void testAfterInsertHistBoolWithTask() throws Exception {

      // non-historic task
      SchedStaskTable lTask = SchedStaskTable.create( TASK1 );
      lTask.insert();

      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.setEventType( RefEventTypeKey.TS );
      lEvtEvent.insert();

      lEvtEvent = EvtEventTable.findByPrimaryKey( EVENT1 );

      Assert.assertFalse( "evt_event.hist_bool must be false", lEvtEvent.getHistBool() );

      lTask = SchedStaskTable.findByPrimaryKey( TASK1 );

      Assert.assertFalse( "sched_stask.hist_bool_ro must be false", lTask.isHistoric() );
   }


   /**
    * Tests the TUAR_EVT_EVENT_HIST_BOOL trigger works correctly when the hist_bool value is updated
    * with a task and a deadline.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAfterUpdateHistBoolWithDeadlineAndTask() throws Exception {
      SchedStaskTable lTask = SchedStaskTable.create( TASK1 );
      lTask.insert();

      createBasicDeadline( DEADLINE1 );

      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.setEventType( RefEventTypeKey.TS );
      lEvtEvent.insert();

      lEvtEvent = EvtEventTable.findByPrimaryKey( EVENT1 );
      lEvtEvent.setHistBool( true );
      lEvtEvent.update();

      lTask = SchedStaskTable.findByPrimaryKey( TASK1 );
      Assert.assertTrue( "sched_stask.hist_bool_ro must be true", lTask.isHistoric() );

      assertEvtSchedDeadHistBool( true );

      lEvtEvent = EvtEventTable.findByPrimaryKey( EVENT1 );
      lEvtEvent.setHistBool( false );
      lEvtEvent.update();

      lTask = SchedStaskTable.findByPrimaryKey( TASK1 );
      Assert.assertFalse( "sched_stask.hist_bool_ro must be false", lTask.isHistoric() );

      assertEvtSchedDeadHistBool( false );
   }


   /**
    * Checks the status of the deadline table's hist_bool_ro column.
    *
    * @param aHistoric
    *           Whether or not the deadline should be historic
    */
   private void assertEvtSchedDeadHistBool( boolean aHistoric ) {
      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery(
            new String[] { "hist_bool_ro" }, "evt_sched_dead", DEADLINE1.getPKWhereArg() );

      Assert.assertTrue( lQuerySet.first() );
      Assert.assertEquals( "evt_sched_dead.hist_bool_ro must be " + aHistoric, aHistoric,
            lQuerySet.getBoolean( "hist_bool_ro" ) );
   }


   /**
    * Creates a basic deadline.
    *
    * @param aPrimaryKey
    *           The primary key of the deadline
    */
   private void createBasicDeadline( EventDeadlineKey aPrimaryKey ) {
      EvtSchedDeadTable lEvtSchedDead =
            EvtSchedDeadTable.create( aPrimaryKey.getEvent(), aPrimaryKey.getDataType() );
      lEvtSchedDead.setDriver( true );
      lEvtSchedDead.setDeviationQt( 0.0 );
      lEvtSchedDead.setNotifyQt( 0.0 );
      lEvtSchedDead.setIntervalQt( 0.0 );
      lEvtSchedDead.setPrefixedQt( 0.0 );
      lEvtSchedDead.setPostfixedQt( 0.0 );
      lEvtSchedDead.insert();
   }
}
