
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
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;


/**
 * This class tests the logic triggers on EVT_SCHED_DEAD.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class EvtSchedDeadTriggerTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The first event */
   private static final EventKey EVENT1 = new EventKey( 4650, 1001 );

   /** The first deadline */
   private static final EventDeadlineKey DEADLINE1 =
         new EventDeadlineKey( EVENT1, DataTypeKey.HOURS );


   /**
    * Tests the TIBR_EVT_SCHED_DEAD_HIST_BOOL trigger works correctly when inserting a non-historic
    * row in evt_event exists.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testBeforeInsertHistBoolWithEvent() throws Exception {
      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.insert();

      createBasicDeadline( DEADLINE1 );

      assertEvtSchedDeadHistBool( false );
   }


   /**
    * Tests the TIBR_EVT_SCHED_DEAD_HIST_BOOL trigger works correctly when inserting a historic row
    * in evt_event exists.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testBeforeInsertHistBoolWithHistoricEvent() throws Exception {
      EvtEventTable lEvtEvent = EvtEventTable.create( EVENT1 );
      lEvtEvent.setHistBool( true );
      lEvtEvent.insert();

      createBasicDeadline( DEADLINE1 );

      assertEvtSchedDeadHistBool( true );
   }


   /**
    * Tests the TIBR_EVT_SCHED_DEAD_HIST_BOOL trigger works correctly when inserting no row in
    * evt_event exists.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testBeforeInsertHistBoolWithNoEvent() throws Exception {
      createBasicDeadline( DEADLINE1 );

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
      Assert.assertEquals( aHistoric, lQuerySet.getBoolean( "hist_bool_ro" ) );
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
