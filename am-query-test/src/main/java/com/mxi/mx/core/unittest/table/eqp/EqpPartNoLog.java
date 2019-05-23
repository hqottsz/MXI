package com.mxi.mx.core.unittest.table.eqp;

import java.util.Date;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EqpPartNoLogKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefLogActionKey;
import com.mxi.mx.core.key.RefLogReasonKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * The table test class for eqp_part_no_log table.
 *
 * @author pyapa
 */
public class EqpPartNoLog {

   /** Results of the query. */
   private DataSet iActual;

   /** Columns in the table. */
   private String[] iCols = { "*" };


   /**
    * Initializes the class. Retrieves all columns in the database table for the given <code>
    * aEqpPartNoLogKey</code> primary key.
    *
    * @param aEqpPartNoLogKey
    *           primary key of the table.
    */
   @SuppressWarnings( "deprecation" )
   public EqpPartNoLog(EqpPartNoLogKey aEqpPartNoLogKey) {

      // Create a new DataSetArgument to hold the lookup parameters
      DataSetArgument lWhereClause = new DataSetArgument();

      // Add the lookup parameters
      lWhereClause.add( "PART_NO_DB_ID", aEqpPartNoLogKey.getDbId() );
      lWhereClause.add( "PART_NO_ID", aEqpPartNoLogKey.getId() );
      lWhereClause.add( "PART_NO_LOG_ID", aEqpPartNoLogKey.getLogId() );

      // Execute the query in the table, and store the result in an instance variable
      iActual = MxDataAccess.getInstance().executeQuery( iCols, "EQP_PART_NO_LOG", lWhereClause );;
   }


   /**
    * Asserts that the table has a row corresponding to the current primary key.
    */
   public void assertExist() {

      // Check if there is at least one entry in the instance
      if ( !iActual.hasNext() ) {
         Assert.fail( "The table does not have the row for the specified primary key." );
      }
   }


   /**
    * Assert that the human resource key is as expected
    *
    * @param aHumanResource
    *           the expected human resource key
    */
   public void assertHumanResourceKey( HumanResourceKey aHumanResource ) {

      // check the vendor status db_id and cd values against ours
      MxAssert.assertEquals( "HR_DB_ID", aHumanResource.getDbId(),
            iActual.getDoubleAt( 1, "HR_DB_ID" ) );
      MxAssert.assertEquals( "HR_ID", aHumanResource.getId(), iActual.getDoubleAt( 1, "HR_ID" ) );
   }


   /**
    * Assert the log_action_db_id and log_action_cd are correct.
    *
    * @param aExpected
    *           if error happens.
    */
   @SuppressWarnings( "deprecation" )
   public void assertLogAction( RefLogActionKey aExpected ) {

      // Retrieve the actual value
      Integer lActualDbId = iActual.getIntegerAt( 1, "LOG_ACTION_DB_ID" );
      String lActualCd = iActual.getStringAt( 1, "LOG_ACTION_CD" );

      // If the actual value is null, ensure the expected is too
      if ( lActualDbId == null ) {

         MxAssert.assertNull( "ref_log_action_pk", aExpected );
      }
      // Otherwise, ensure the two values match
      else {
         MxAssert.assertEquals( "ref_log_action_pk", aExpected,
               new RefLogActionKey( lActualDbId.intValue(), lActualCd ) );
      }
   }


   /**
    * Assert that the log_date is correct.
    *
    * @param aExpected
    *           if log_date
    */
   public void assertLogDate( Date aExpected ) {
      MxAssert.assertEquals( "LOG_DT", aExpected, iActual.getDateAt( 1, "LOG_DT" ) );
   }


   /**
    * Assert the log_reason_db_id and log_reason_cd are correct.
    *
    * @param aExpected
    *           if error happens.
    */
   @SuppressWarnings( "deprecation" )
   public void assertLogReason( RefLogReasonKey aExpected ) {

      // Retrieve the actual value
      Integer lActualDbId = iActual.getIntegerAt( 1, "LOG_REASON_DB_ID" );
      String lActualCd = iActual.getStringAt( 1, "LOG_REASON_CD" );

      // If the actual value is null, ensure the expected is too
      if ( lActualDbId == null ) {

         MxAssert.assertNull( "ref_log_reason_pk", aExpected );
      }
      // Otherwise, ensure the two values match
      else {
         MxAssert.assertEquals( "ref_log_reason_pk", aExpected,
               new RefLogReasonKey( lActualDbId.intValue(), lActualCd ) );
      }
   }


   /**
    * Asserts that the table does not have a row corresponding to the current primary key.
    */
   public void assertNotExist() {

      // Check if there is at least one entry in the instance
      if ( iActual.hasNext() ) {
         Assert.fail( "The table has the row for the specified primary key." );
      }
   }


   /**
    * Assert that the system note is correct.
    *
    * @param aExpected
    *           if error happens.
    */
   public void assertSystemNote( String aExpected ) {
      MxAssert.assertEquals( "SYSTEM_NOTE", aExpected, iActual.getStringAt( 1, "SYSTEM_NOTE" ) );
   }


   /**
    * Assert that the user note is correct.
    *
    * @param aExpected
    *           if error happens.
    */
   public void assertUserNote( String aExpected ) {
      MxAssert.assertEquals( "USER_NOTE", aExpected, iActual.getStringAt( 1, "USER_NOTE" ) );
   }
}
