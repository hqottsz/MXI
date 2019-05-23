
package com.mxi.mx.core.query.table.evt;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;


/**
 * This is a query unit test to test setEstEndDateOnSubtasks.qrx
 *
 * @author slevert
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SetEstEndDateOnSubtasksTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            SetEstEndDateOnSubtasksTest.class );
   }


   private static final String QRX = "com.mxi.mx.core.query.table.evt.setEstEndDateOnSubtasks";

   private static final int DB_ID = 4650;

   private static final EventKey WP1_EVENT_KEY = new EventKey( DB_ID, 1 );

   private static final EventKey CMPLT_TASK_KEY = new EventKey( DB_ID, 4 );

   private static final EventKey ACTV_TASK1_KEY = new EventKey( DB_ID, 2 );

   private static final EventKey ACTV_TASK2_KEY = new EventKey( DB_ID, 3 );


   /**
    * Tests that a completed event is not updated when calling setEstEndDateOnSubtasks.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testSchedDatesDoNotChangeForCompletedTask() throws Exception {

      assertEquals( "Rows updated", 2, executeUpdate( WP1_EVENT_KEY, 1d ) );

      // make sure the dates match what was in the database and not what was provided above

      Calendar lCal = Calendar.getInstance();
      lCal.set( 2015, Calendar.SEPTEMBER, 29, 10, 0, 0 );
      EvtEventUtil lEvent = new EvtEventUtil( CMPLT_TASK_KEY );
      lEvent.assertEventActualStartDate( lCal.getTime() );
      lEvent.assertEventActualStartGdt( lCal.getTime() );
      lEvent.assertEventGdtDt( lCal.getTime() );
      lEvent.assertEventLocalDt( lCal.getTime() );

      lCal = Calendar.getInstance();
      lCal.set( 2015, Calendar.SEPTEMBER, 28, 10, 0, 0 );
      lEvent = new EvtEventUtil( ACTV_TASK1_KEY );
      lEvent.assertEventGdtDt( lCal.getTime() );
      lEvent.assertEventLocalDt( lCal.getTime() );

      lCal = Calendar.getInstance();
      lCal.set( 2015, Calendar.SEPTEMBER, 29, 10, 0, 0 );
      lEvent = new EvtEventUtil( ACTV_TASK2_KEY );
      lEvent.assertEventGdtDt( lCal.getTime() );
      lEvent.assertEventLocalDt( lCal.getTime() );

   }


   /**
    * This helper method executes the update with the given parameters in the collected usage parm.
    *
    * @param aUsageParm
    *           The object containing the usage data.
    *
    * @return The number of rows updated.
    */
   private int executeUpdate( EventKey aEvent, Double aDelta ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aRootEventDbId", aEvent.getDbId() );
      lArgs.add( "aRootEventId", aEvent.getId() );
      lArgs.add( "aEventDateDelta", aDelta );

      return MxDataAccess.getInstance().executeUpdate( QRX, lArgs );
   }

}
