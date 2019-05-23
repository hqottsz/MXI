
package com.mxi.mx.core.unittest.table.evt;

import java.util.Date;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.EventKeyInterface;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>evt_sched_dead</code> table.
 *
 * @author mbajer
 * @created April 11, 2002
 */
public class EvtSchedDead extends EvtSchedDeadTable {

   /**
    * Creates a new EvtSchedDead object.
    *
    * @param aEventDeadline
    *           The event deadline key
    */
   public EvtSchedDead(EventDeadlineKey aEventDeadline) {
      super( aEventDeadline );
   }


   /**
    * Creates a new EvtSchedDead instance.
    *
    * @param aEvent
    *           the event
    * @param aDataType
    *           the data type
    */
   public EvtSchedDead(EventKeyInterface aEvent, DataTypeKey aDataType) {
      super( new EventDeadlineKey( aEvent.getEventKey(), aDataType ) );
   }


   /**
    * Returns the number of rows in the table corresponding to the given <code>aDataType</code>.
    *
    * @param aDataType
    *           the data type to use for row lookup.
    *
    * @return the number of table rows.
    */
   public static int getDeadlineCount( DataTypeKey aDataType ) {

      // Retrieve the count from the table
      String[] lCols = { "count(*) as count" };

      // Query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "data_type_db_id", aDataType.getDbId() );
      lArgs.add( "data_type_id", aDataType.getId() );

      // Execute the query
      DataSet lResult = MxDataAccess.getInstance().executeQuery( lCols, "evt_sched_dead", lArgs );

      // Retrieve the value
      return lResult.getIntAt( 1, "count" );
   }


   /**
    * Asserts that the <code>deviation_qt</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertDeviationQt( double aExpected ) {

      MxAssert.assertEquals( "deviation_qt", aExpected, getDeviationQt() );
   }


   /**
    * Asserts that the row with <code>event_db_id:event_id</code> = <code>iEvent</code> exists in
    * table.
    */
   public void assertExist() {
      Assert.assertTrue( "The evt_sched_dead table does not have the row", exists() );
   }


   /**
    * Asserts the interval qt value
    *
    * @param aExpected
    *           the expected value
    */
   public void assertIntervalQt( double aExpected ) {
      MxAssert.assertEquals( "interval_qt", aExpected, getIntervalQt(), 0.2 );
   }


   /**
    * Asserts that the <code>notify_qt</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertNotifyQt( double aExpected ) {
      MxAssert.assertEquals( "notify_qt", aExpected, getNotifyQt() );
   }


   /**
    * Asserts that the <code>postfixed_qt</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertPostfixedQt( double aExpected ) {
      MxAssert.assertEquals( "postfixed_qt", aExpected, getPostfixedQt() );
   }


   /**
    * Asserts that the <code>prefixed_qt</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertPrefixedQt( double aExpected ) {
      MxAssert.assertEquals( "prefixed_qt", aExpected, getPrefixedQt() );
   }


   /**
    * Asserts that the <code>sched_dead_dt</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertSchedDeadDt( Date aExpected ) {
      MxAssert.assertEquals( "sched_dead_dt", aExpected, getDeadlineDate() );
   }


   /**
    * Asserts that the <code>sched_dead_qt</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertSchedDeadQt( Double aExpected ) {
      Double lActual = getDeadlineQt();

      if ( ( aExpected != null ) && ( lActual != null ) ) {
         MxAssert.assertEquals( "sched_dead_qt", aExpected.doubleValue(), lActual.doubleValue(),
               0.2 );
      } else {
         MxAssert.assertEquals( "sched_dead_qt", aExpected, lActual );
      }
   }


   /**
    * Asserts that the <code>sched_from_cd</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertSchedFromCd( String aExpected ) {
      String lActual = ( getScheduledFrom() == null ) ? null : getScheduledFrom().getCd();

      MxAssert.assertEquals( "sched_from_cd", aExpected, lActual );
   }


   /**
    * Asserts that the <code>start_dt</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertStartDt( Date aExpected ) {
      MxAssert.assertEquals( "start_dt", aExpected, getStartDate() );
   }


   /**
    * Asserts that the <code>start_qt</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertStartQt( Double aExpected ) {
      Double lActual = getStartQt();

      if ( ( aExpected != null ) && ( lActual != null ) ) {
         MxAssert.assertEquals( "start_qt", aExpected.doubleValue(), lActual.doubleValue(), 0.2 );
      } else {
         MxAssert.assertEquals( "start_qt", aExpected, lActual );
      }
   }


   /**
    * This method is for testing only. Read only values should not be written.
    *
    * @param aHistoric
    *           Whether or not the deadline is on a historic task
    */
   public void setHistBoolRo( boolean aHistoric ) {
      setBoolean( ColumnName.HIST_BOOL_RO, aHistoric );
   }
}
