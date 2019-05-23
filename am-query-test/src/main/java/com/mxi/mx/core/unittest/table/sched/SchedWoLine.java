
package com.mxi.mx.core.unittest.table.sched;

import java.util.Date;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.WoLineKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>sched_wo_line</code> table.
 *
 * @author cjb
 * @created April 12, 2004
 */
public class SchedWoLine {

   /** DOCUMENT ME! */
   private static final String TABLE_NAME = "sched_wo_line";

   /** Results of the query. */
   private DataSet iActual;

   /** Columns in the table. */

   private String[] iCols = { "WO_SCHED_DB_ID", "WO_SCHED_ID", "WO_LINE_ID", "SCHED_DB_ID",
         "SCHED_ID", "RO_LINE_SDESC", "UNASSIGN_BOOL", "COLLECTED_BOOL", "COLLECTION_HR_DB_ID",
         "COLLECTION_HR_ID", "COLLECTION_DT", "WORKSCOPE_ORDER" };


   /**
    * Initializes the class.
    *
    * @param aWoLine
    *           Work Order Line Key.
    */
   public SchedWoLine(WoLineKey aWoLine) {
      DataSetArgument lWhereClause;

      // Obtain actual value
      lWhereClause = new DataSetArgument();

      lWhereClause.add( "wo_sched_db_id", aWoLine.getDbId() );
      lWhereClause.add( "wo_sched_id", aWoLine.getId() );
      lWhereClause.add( "wo_line_id", aWoLine.getLineId() );

      iActual = MxDataAccess.getInstance().executeQuery( iCols, "sched_wo_line", lWhereClause );
   }


   /**
    * Initializes the table data.
    *
    * @param aCheck
    *           primary key of the check.
    * @param aLineItem
    *           primary key of the line item.
    */
   public SchedWoLine(TaskKey aCheck, TaskKey aLineItem) {

      // Create arguments for the query to look up table values
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "wo_sched_db_id", aCheck.getDbId() );
      lArgs.add( "wo_sched_id", aCheck.getId() );
      lArgs.add( "sched_db_id", aLineItem.getDbId() );
      lArgs.add( "sched_id", aLineItem.getId() );

      // Lookup the table values
      iActual = MxDataAccess.getInstance().executeQuery( iCols, TABLE_NAME, lArgs );
   }


   /**
    * Initializes the class.
    *
    * @param aWoLine
    *           Work Order Line Key.
    * @param aTask
    *           Task Key
    */
   public SchedWoLine(WoLineKey aWoLine, TaskKey aTask) {
      DataSetArgument lWhereClause;

      // Obtain actual value
      lWhereClause = new DataSetArgument();

      lWhereClause.add( "wo_sched_db_id", aWoLine.getDbId() );
      lWhereClause.add( "wo_sched_id", aWoLine.getId() );
      lWhereClause.add( "wo_line_id", aWoLine.getLineId() );
      lWhereClause.add( "sched_db_id", aTask.getDbId() );
      lWhereClause.add( "sched_id", aTask.getId() );

      iActual = MxDataAccess.getInstance().executeQuery( iCols, "sched_wo_line", lWhereClause );
   }


   /**
    * Returns the value of the line items for check property.
    *
    * @param aCheck
    *           the check
    *
    * @return the value of the line items for check property.
    */
   public static SchedWoLine[] getLineItemsForCheck( TaskKey aCheck ) {
      SchedWoLine[] lLines;

      // Create arguments for the query to look up table values
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "wo_sched_db_id", aCheck.getDbId() );
      lArgs.add( "wo_sched_id", aCheck.getId() );

      String[] lCols = { "sched_db_id", "sched_id" };

      // Lookup the table values
      QuerySet lDs = QuerySetFactory.getInstance().executeQuery( lCols, TABLE_NAME, lArgs );

      TaskKey lTask;

      lLines = new SchedWoLine[lDs.getRowCount()];
      for ( int i = 0; lDs.next(); i++ ) {
         lTask = new TaskKey( lDs.getInt( "sched_db_id" ), lDs.getInt( "sched_id" ) );
         lLines[i] = new SchedWoLine( aCheck, lTask );
      }

      return lLines;
   }


   /**
    * Returns the maximum line item number associated with the given <code>aCheck</code>.
    *
    * @param aCheck
    *           the check whose maximum line item number is to be retrieved.
    *
    * @return the maximum line item number associated with the given <code>aCheck</code>.
    */
   public static int getMaxWoLineId( TaskKey aCheck ) {

      // The columns to select
      String[] lCols = new String[] { "max( wo_line_id ) AS MAX_WO_LINE_ID" };

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "wo_sched_db_id", aCheck.getDbId() );
      lArgs.add( "wo_sched_id", aCheck.getId() );

      // Execute the query
      DataSet lResults = MxDataAccess.getInstance().executeQuery( lCols, TABLE_NAME, lArgs );

      // Retrieve the results
      return lResults.getIntAt( 1, "MAX_WO_LINE_ID" );
   }


   /**
    * Returns the number of line items associated with the given <code>aCheck</code>.
    *
    * @param aCheck
    *           the check whose line items are to be counted.
    *
    * @return the number of line items associated with the given <code>aCheck</code>.
    */
   public static int getNumLineItems( TaskKey aCheck ) {

      // The columns to select
      String[] lCols = new String[] { "count(*) AS COUNT" };

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "wo_sched_db_id", aCheck.getDbId() );
      lArgs.add( "wo_sched_id", aCheck.getId() );

      // Execute the query
      DataSet lResults = MxDataAccess.getInstance().executeQuery( lCols, TABLE_NAME, lArgs );

      // Retrieve the results
      return lResults.getIntAt( 1, "COUNT" );
   }


   /**
    * Asserts that the <code>collected_bool</code> value is equal to expected
    *
    * @param aExpected
    *           expected value
    */
   public void assertCollectedBool( boolean aExpected ) {
      boolean lActual = iActual.getBooleanAt( 1, "collected_bool" );
      MxAssert.assertEquals( "collected_bool", aExpected, lActual );
   }


   /**
    * Asserts that the <code>collection_dt</code> value is equal to expected
    *
    * @param aExpected
    *           expected value
    */
   public void assertCollectionDate( Date aExpected ) {
      Date lActual = iActual.getDateAt( 1, "collection_dt" );
      MxAssert.assertEquals( "collection_dt", aExpected, lActual );
   }


   /**
    * Asserts that the <code>COLLECTION_HR_DB_ID:COLLECTION_HR_ID</code> value is equal to expected
    *
    * @param aExpected
    *           expected value
    */
   public void assertCollectionHr( HumanResourceKey aExpected ) {

      Integer lDbId;
      Integer lId;

      HumanResourceKey lActual = null;

      lDbId = iActual.getIntegerAt( 1, "COLLECTION_HR_DB_ID" );
      lId = iActual.getIntegerAt( 1, "COLLECTION_HR_ID" );

      if ( ( lDbId != null ) && ( lId != null ) ) {
         lActual = new HumanResourceKey( lDbId.intValue(), lId.intValue() );
      }

      MxAssert.assertEquals( "COLLECTION_HR_DB_ID:COLLECTION_HR_ID", aExpected, lActual );
   }


   /**
    * Asserts that the line item exists for the check.
    */
   public void assertExist() {

      // If the table data does not have a row, then the record does not exist
      if ( !iActual.hasNext() ) {
         MxAssert.fail( "The table does not have a record for the given check and line item." );
      }
   }


   /**
    * Asserts that the line item does not exist for the check.
    */
   public void assertNotExist() {

      // If the table data has a row, then the record exists
      if ( iActual.hasNext() ) {
         MxAssert.fail( "The table has a record for the given check and line item." );
      }
   }


   /**
    * Asserts that the actual table column value and the given expected value are equal.
    *
    * @param aExpected
    *           the expected column value.
    */
   public void assertRoLineSdesc( String aExpected ) {

      // Assert that the expected and actual values match
      MxAssert.assertEquals( "ro_line_sdesc", aExpected,
            iActual.getStringAt( 1, "ro_line_sdesc" ) );
   }


   /**
    * Asserts that the actual table column value and the given expected value are equal.
    *
    * @param aExpected
    *           the expected column value.
    */
   public void assertUnassignBool( boolean aExpected ) {

      // Assert that the expected and actual values match
      MxAssert.assertEquals( "unassign_bool", aExpected,
            iActual.getBooleanAt( 1, "unassign_bool" ) );
   }


   /**
    * Asserts that the actual table column value and the given expected value are equal.
    *
    * @param aWorkscopeOrder
    *           the expected column value.
    */
   public void assertWorkscopeOrder( int aWorkscopeOrder ) {

      // Assert that the expected and actual values match
      MxAssert.assertEquals( "workscope_order", aWorkscopeOrder,
            iActual.getIntAt( 1, "workscope_order" ) );
   }


   public TaskKey getTask() {
      return iActual.getKeyAt( TaskKey.class, 1, "sched_db_id", "sched_id" );
   }


   /**
    * Retrieves the workscope order of the work order line
    *
    * @return the workscope order
    */
   public int getWorkscopeOrder() {
      return iActual.getIntAt( 1, "workscope_order" );
   }
}
