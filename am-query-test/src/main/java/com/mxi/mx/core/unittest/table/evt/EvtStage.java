
package com.mxi.mx.core.unittest.table.evt;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>evt_stage</code> table.
 *
 * @author asmolko
 * @created March 18, 2002
 */
public class EvtStage {

   /** Actual values. */
   private DataSet iActual;

   /** Columns in the table. */
   private String[] iCols = { "event_status_cd", "hr_db_id", "hr_id", "stage_event_db_id",
         "stage_event_id", "stage_reason_cd", "stage_dt", "stage_gdt", "stage_note", "system_bool",
         "stage_id" };


   /**
    * Initializes the class with the latest stage. The latest stage is the stage with the max value
    * of <code>stage_id</code> column in the table.
    *
    * @param aEvent
    *           primary key of the table.
    */
   public EvtStage(EventKey aEvent) {
      DataSetArgument lWhereClause;

      // Obtain actual value
      lWhereClause = new DataSetArgument();
      lWhereClause.add( "event_db_id", aEvent.getDbId() );
      lWhereClause.add( "event_id", aEvent.getId() );
      iActual = MxDataAccess.getInstance().executeQuery( iCols, "evt_stage", lWhereClause );
   }


   /**
    * Asserts that the table has a row.
    *
    * @param aExpectedRowCount
    *           Description of Parameter.
    */
   public void assertCount( int aExpectedRowCount ) {
      MxAssert.assertEquals( "The number of columns increased/decreased from <" + aExpectedRowCount
            + "> to <" + iActual.getRowCount() + ">", aExpectedRowCount, iActual.getRowCount() );
   }


   /**
    * Asserts that the <code>hr_db_id:hr_id</code> and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           expected hr.
    */
   public void assertHr( HumanResourceKey aExpected ) {
      HumanResourceKey lActual = null;

      Integer lHrDbId = iActual.getIntegerAt( 1, "hr_db_id" );
      Integer lHrId = iActual.getIntegerAt( 1, "hr_id" );

      if ( ( lHrDbId != null ) && ( lHrId != null ) ) {

         // Obtain actual value
         lActual = new HumanResourceKey( lHrDbId.intValue(), lHrId.intValue() );
      }

      MxAssert.assertEquals( "hr_db_id:hr_id", aExpected, lActual );
   }


   /**
    * Asserts that the <code>system_bool</code> is equal <code>aExpected</code> .
    *
    * @param aExpected
    *           expected value of the column.
    */
   public void assertSystemBool( int aExpected ) {
      int lActual;

      lActual = iActual.getIntAt( 1, "system_bool" );

      MxAssert.assertEquals( "system_bool", aExpected, lActual );
   }


   /**
    * Asserts that the <code>user_stage_note</code> and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           expected user stage note.
    */
   public void assertStageNote( String aExpected ) {
      String lActual;

      lActual = iActual.getStringAt( 1, "stage_note" );

      MxAssert.assertEquals( "stage_note", aExpected, lActual );
   }


   /**
    * Asserts that the <code>user_stage_note</code> and <code>aExpected</code> are equal.
    *
    * @param aRow
    *           the row number to test.
    * @param aExpected
    *           expected user stage note.
    */
   public void assertStageNote( int aRow, String aExpected ) {
      String lActual;

      lActual = iActual.getStringAt( aRow, "stage_note" );

      MxAssert.assertEquals( "stage_note", aExpected, lActual );
   }


   /**
    * Asserts that the <code>event_status_cd</code> and <code>aExpected</code> are equal.
    *
    * @param aExpected
    *           expected event status.
    */
   public void assertEventStatus( String aExpected ) {
      assertEventStatus( 1, aExpected );
   }


   /**
    *
    * Asserts that the <code>event_status_cd</code> and <code>aExpected</code> are equal
    *
    * @param aRow
    * @param aExpected
    */
   public void assertEventStatus( int aRow, String aExpected ) {
      String lActual;

      // Obtain actual value
      lActual = iActual.getStringAt( aRow, "event_status_cd" );

      MxAssert.assertEquals( "event_status_cd", aExpected, lActual );
   }

}
