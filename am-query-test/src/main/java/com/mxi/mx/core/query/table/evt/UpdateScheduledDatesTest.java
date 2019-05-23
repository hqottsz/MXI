
package com.mxi.mx.core.query.table.evt;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

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
 * This is a query unit test to test updateTreeSchedDatesBasedOnTaskEstDuration.qrx
 *
 * @author slevert
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateScheduledDatesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UpdateScheduledDatesTest.class );
   }


   private static final String QRX_UPDATE_SCHED_DATES =
         "com.mxi.mx.core.query.table.evt.updateTreeSchedDatesBasedOnTaskEstDuration";

   private static final int DB_ID = 4650;

   private static final EventKey WP1_EVENT_KEY = new EventKey( DB_ID, 1 );

   private static final EventKey CMPLT_TASK1_KEY = new EventKey( DB_ID, 2 );

   private static final EventKey WP2_EVENT_KEY = new EventKey( DB_ID, 3 );

   private static final EventKey ACTV_TASK2_KEY = new EventKey( DB_ID, 4 );


   /**
    * Tests that a completed event is not updated when calling
    * updateTreeSchedDatesBasedOnTaskEstDuration.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testSchedDatesDoNotChangeForCompletedTask() throws Exception {

      Date lSchedStartDate = Calendar.getInstance().getTime();
      Date lSchedStartGDTDate = Calendar.getInstance().getTime();

      assertEquals( "Rows updated", 0,
            executeUpdate( WP1_EVENT_KEY, lSchedStartDate, lSchedStartGDTDate ) );

      // make sure the dates match what was in the database and not what was provided above

      Calendar lCal = Calendar.getInstance();
      lCal.set( 2015, Calendar.SEPTEMBER, 26, 10, 0, 0 );
      EvtEventUtil lEvent = new EvtEventUtil( CMPLT_TASK1_KEY );
      lEvent.assertEventSchedStartDate( lCal.getTime() );
      lEvent.assertEventSchedStartGdt( lCal.getTime() );

   }


   /**
    * Tests that an actv event is updated when calling updateTreeSchedDatesBasedOnTaskEstDuration.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testSchedDatesDoChangeForActvTask() throws Exception {

      Date lSchedStartDate = Calendar.getInstance().getTime();
      Date lSchedStartGDTDate = Calendar.getInstance().getTime();

      assertEquals( "Rows updated", 1,
            executeUpdate( WP2_EVENT_KEY, lSchedStartDate, lSchedStartGDTDate ) );

      // make sure the dates match what was provided above
      EvtEventUtil lEvent = new EvtEventUtil( ACTV_TASK2_KEY );
      lEvent.assertEventSchedStartDate( lSchedStartDate );
      lEvent.assertEventSchedStartGdt( lSchedStartGDTDate );

   }


   /**
    * This helper method executes the update with the given parameters in the collected usage parm.
    *
    * @param aUsageParm
    *           The object containing the usage data.
    *
    * @return The number of rows updated.
    */
   private int executeUpdate( EventKey aEvent, Date aScheduledStartDt, Date aScheduledStartGdt ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aEventDbId", aEvent.getDbId() );
      lArgs.add( "aEventId", aEvent.getId() );
      lArgs.add( "aSchedStartDt", aScheduledStartDt );
      lArgs.add( "aSchedStartGdt", aScheduledStartGdt );

      return MxDataAccess.getInstance().executeUpdate( QRX_UPDATE_SCHED_DATES, lArgs );
   }

}
