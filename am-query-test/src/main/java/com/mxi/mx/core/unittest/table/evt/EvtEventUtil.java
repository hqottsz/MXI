package com.mxi.mx.core.unittest.table.evt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.junit.Assert;

import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventKeyInterface;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>evt_event</code> table.
 *
 * @author asmolko
 * @created March 18, 2002
 */
public class EvtEventUtil {

   private EvtEventTable iTable;


   /**
    * Returns the value of the table property.
    *
    * @return the value of the table property
    */
   public EvtEventTable getTable() {
      return iTable;
   }


   /**
    * Initializes the class.
    *
    * @param aEvent
    *           primary key of the table.
    */
   public EvtEventUtil(EvtEventTable aTable) {
      iTable = aTable;
   }


   /**
    * Initializes the class.
    *
    * @param aEvent
    *           primary key of the table.
    */
   public EvtEventUtil(EventKey aEventKey) {
      iTable = EvtEventTable.findByPrimaryKey( aEventKey );
   }


   /**
    * Initializes the class.
    *
    * @param aEvent
    *           primary key of the table.
    */
   public EvtEventUtil(EventKeyInterface aFaultKey) {
      this( aFaultKey.getEventKey() );
   }


   /**
    * Asserts that the <code>editor_hr</code> is equal <code>aExpectedHr</code>
    *
    * @param aExpectedHr
    *           expeced editor_hr
    */
   public void assertEditorHr( HumanResourceKey aExpectedHr ) {

      MxAssert.assertEquals( "EDITOR_HR", aExpectedHr, iTable.getEditorHr() );
   }


   /**
    * Asserts that the <code>event_dt and event_gdt</code> is equal <code>aExpectedDate</code> .
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventActualEndDate( Date aExpectedDate ) {
      MxAssert.assertEquals( "event_dt", aExpectedDate, iTable.getEventDate(), 2 );
      MxAssert.assertEquals( "event_gdt", aExpectedDate, iTable.getEventGdt(), 2 );
   }


   /**
    * Asserts that the row with <code>event_db_id:event_id</code> = <code>iEvent</code> exists in
    * table.
    */
   public void assertExist() {
      Assert.assertTrue( "The evt_event table does not have a row", iTable.exists() );
   }


   /**
    * Asserts that the <code>actual_start_dt and actual_start_gdt</code> is equal <code>
    * aExpectedDate</code> .
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventActualStartDate( Date aExpectedDate ) {
      MxAssert.assertEquals( "actual_start_dt", aExpectedDate, iTable.getActualStartDt(), 2 );
      MxAssert.assertEquals( "actual_start_gdt", aExpectedDate, iTable.getActualStartGdt(), 2 );
   }


   /**
    * Asserts that the <code>actual_start_dt</code> is equal <code>aExpectedDate</code>.
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventActualStartDt( Date aExpectedDate ) {

      MxAssert.assertEquals( "actual_start_dt", aExpectedDate, iTable.getActualStartDt(), 1 );
   }


   /**
    * Asserts that the <code>actual_start_gdt</code> is equal <code>aExpectedDate</code> .
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventActualStartGdt( Date aExpectedDate ) {

      MxAssert.assertEquals( "actual_start_gdt", aExpectedDate, iTable.getActualStartGdt(), 1 );
   }


   /**
    * Asserts that the <code>event_gdt</code> is equal <code>aExpectedDate</code>.
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventGdtDt( Date aExpectedDate ) {

      MxAssert.assertEquals( "event_gdt", aExpectedDate, iTable.getEventGdt(), 1 );
   }


   /**
    * Asserts that the <code>event_ldesc</code> value and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           expected event ldesc value.
    */
   public void assertEventLdesc( String aExpected ) {

      // Check if the aExpected parameter is blank; the database treats null
      // and blank both as null, so convert aExpected to null if it is blank
      if ( ( aExpected != null ) && aExpected.equalsIgnoreCase( "" ) ) {
         aExpected = null;
      }

      MxAssert.assertEquals( "event_ldesc", aExpected, iTable.getEventLdesc() );
   }


   /**
    * Asserts that the <code>event_sdesc</code> value and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           expected event sdesc value.
    */
   public void assertEventSdesc( String aExpected ) {

      // Check if the aExpected parameter is blank; the database treats null
      // and blank both as null, so convert aExpected to null if it is blank
      if ( ( aExpected != null ) && aExpected.equalsIgnoreCase( "" ) ) {
         aExpected = null;
      }

      MxAssert.assertEquals( "event_sdesc", aExpected, iTable.getEventSdesc() );
   }


   private static class WrapperEvtEventTable extends EvtEventTable {

      /**
       * Creates a new {@linkplain WrapperEvtEventTable} object.
       *
       * @param aKey
       * @param aTable
       * @throws InvocationTargetException
       * @throws IllegalArgumentException
       * @throws IllegalAccessException
       * @throws SecurityException
       * @throws NoSuchMethodException
       */
      protected WrapperEvtEventTable(EventKey aKey, EvtEventTable aTable)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
         super( aKey, aTable );

         Method lReadMethod = aTable.getClass().getDeclaredMethod( "getReadAttributes" );
         lReadMethod.setAccessible( true );
         super.loadData( ( Map<String, Object> ) lReadMethod.invoke( aTable ) );
      }


      @Override
      protected Map<String, Object> getSetAttributes() {
         return super.getSetAttributes();
      }


      @Override
      protected Object getAttribute( String aAttributeKey ) {
         return super.getAttribute( aAttributeKey );
      }
   }


   /**
    * Asserts that the <code>event_dt</code> is equal <code>aExpectedDate</code>.
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventLocalDt( Date aExpectedDate ) {

      MxAssert.assertEquals( "EVENT_DT", aExpectedDate, iTable.getEventDate(), 1 );
   }


   /**
    * Asserts that the <code>sched_start_dt and sched_start_gdt</code> is equal <code>
    * aExpectedDate</code> .
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventSchedStartDate( Date aExpectedDate ) {
      MxAssert.assertEquals( "sched_start_dt", aExpectedDate, iTable.getSchedStartDt(), 2 );
      MxAssert.assertEquals( "sched_start_gdt", aExpectedDate, iTable.getSchedStartGdt(), 2 );
   }


   /**
    * Asserts that the <code>sched_start_dt</code> is equal <code>aExpectedDate</code>.
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventSchedStartDt( Date aExpectedDate ) {

      MxAssert.assertEquals( "sched_start_dt", aExpectedDate, iTable.getSchedStartDt(), 1 );
   }


   /**
    * Asserts that the <code>sched_start_gdt</code> is equal <code>aExpectedDate</code>.
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertEventSchedStartGdt( Date aExpectedDate ) {

      MxAssert.assertEquals( "sched_start_gdt", aExpectedDate, iTable.getSchedStartGdt(), 1 );
   }


   /**
    * Asserts that the <code>event_status_cd</code> value and <code>aEventStatus</code> are equal.
    *
    * @param aExpected
    *           Description of Parameter.
    */
   public void assertEventStatus( RefEventStatusKey aExpected ) {

      MxAssert.assertEquals( "EVENT_STATUS", aExpected, iTable.getEventStatus() );
   }


   /**
    * Asserts that the <code>stage reason key</code> value and <code>aExpectedStageReason</code> are
    * equal.
    *
    * @param aExpectedStageReason
    *           expected stage reason key.
    */
   public void assertStageReason( RefStageReasonKey aExpectedStageReason ) {

      MxAssert.assertEquals( "STAGE_REASON", aExpectedStageReason, iTable.getStageReasonKey() );
   }


   /**
    * Asserts that the <code>event_type_cd</code> value and <code>aEventType</code> are equal.
    *
    * @param aExpected
    *           expected event type.
    */
   public void assertEventType( String aExpected ) {
      MxAssert.assertEquals( "event_type_cd", aExpected, iTable.getEventType().getCd() );
   }


   /**
    * Asserts that the <code>hist_bool</code> is equal <code>aHistBool</code>.
    *
    * @param aHistBool
    *           expected hist bool.
    */
   public void assertHistBool( boolean aHistBool ) {

      MxAssert.assertEquals( "hist_bool should be", aHistBool, iTable.getHistBool() );
   }


   /**
    * Asserts that the <code>actual_start_dt</code> is equal <code>aExpectedDate</code>.
    *
    * @param aExpectedDate
    *           expected date.
    */
   public void assertActualLocalDt( Date aExpectedDate ) {

      MxAssert.assertEquals( "actual_start_dt", aExpectedDate, iTable.getActualStartDt(), 1 );
   }
}
