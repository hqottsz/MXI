
package com.mxi.mx.db.trigger;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * This class tests the logic triggers on SCHED_STASK.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SchedStaskTriggerTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final EventKey EVENT1 = new EventKey( 4650, 1001 );
   private static final TaskKey TASK1 = new TaskKey( EVENT1 );


   /**
    * Tests the TIBR_SCHED_STASK_HIST_BOOL trigger works correctly when inserting a non-historic row
    * in evt_event exists.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testBeforeInsertHistBoolWithEvent() throws Exception {
      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.insert();

      SchedStaskTable lTask = SchedStaskTable.create( TASK1 );
      lTask.insert();

      lTask = SchedStaskTable.findByPrimaryKey( TASK1 );
      Assert.assertFalse( lTask.isHistoric() );
   }


   /**
    * Tests the TIBR_SCHED_STASK_HIST_BOOL trigger works correctly when inserting a historic row in
    * evt_event exists.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testBeforeInsertHistBoolWithHistoricEvent() throws Exception {
      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.setHistBool( true );
      lEvtEvent.insert();

      SchedStaskTable lTask = SchedStaskTable.create( TASK1 );
      lTask.insert();

      lTask = SchedStaskTable.findByPrimaryKey( TASK1 );
      Assert.assertTrue( lTask.isHistoric() );
   }


   /**
    * Tests the TIBR_SCHED_STASK_HIST_BOOL trigger works correctly when inserting no row in
    * evt_event exists.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testBeforeInsertHistBoolWithNoEvent() throws Exception {
      SchedStaskTable lTask = SchedStaskTable.create( TASK1 );
      lTask.insert();

      lTask = SchedStaskTable.findByPrimaryKey( TASK1 );
      Assert.assertFalse( lTask.isHistoric() );
   }
}
