
package com.mxi.mx.core.query.plsql.schedstaskpkg;

import java.sql.CallableStatement;
import java.sql.Types;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;


/**
 * This test class asserts that the function FindLatestTaskInstance within the SCHED_STASK_PKG
 * operates correctly.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FindLatestTaskInstanceTest {

   final int EXPECTED_EVENT_DB_ID = 1;
   final int EXPECTED_EVENT_ID = 2;
   final int EXPECTED_RESULT = 1;
   final int EXPECTED_INVALID = -1;

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FindLatestTaskInstanceTest.class );
   }


   /**
    * <p>
    * Make sure that when we submit the id of a <b>non existing</b> task for inventory, the return
    * val of '-1', '-1' are returned and the expected result is 1.
    * </p>
    */
   @Test
   public void testWithNoInstanceOfTask() throws Exception {

      // ARRANGE - data has been loaded in the Before Class loadData() method
      int lTaskDbId = 1;
      int lTaskId = 3;
      int lInvNoDbId = 1;
      int lInvNoId = 1;

      // ACT
      TaskInstanceResult lResult = execute( lTaskDbId, lTaskId, lInvNoDbId, lInvNoId );
      TaskInstanceResult lExpectedResult =
            new TaskInstanceResult( EXPECTED_INVALID, EXPECTED_INVALID, EXPECTED_RESULT );

      // ASSERT
      Assert.assertTrue(
            "Invalid Task Instance. Expected " + lExpectedResult + " but got " + lResult,
            lResult.equals( lExpectedResult ) );

   }


   /**
    * <p>
    * Make sure that when we submit the id of an <b>existing</b> task for inventory, the most recent
    * will be returned.
    * </p>
    */
   @Test
   public void testForMostRecentInstanceOfTask() throws Exception {

      // ARRANGE - data has been loaded in the Before Class loadData() method
      int lTaskDbId = 1;
      int lTaskId = 1;
      int lInvNoDbId = 1;
      int lInvNoId = 1;

      // ACT
      TaskInstanceResult lResult = execute( lTaskDbId, lTaskId, lInvNoDbId, lInvNoId );
      TaskInstanceResult lExpectedResult =
            new TaskInstanceResult( EXPECTED_EVENT_DB_ID, EXPECTED_EVENT_ID, EXPECTED_RESULT );

      // ASSERT
      Assert.assertTrue(
            "Invalid Task Instance. Expected " + lExpectedResult + " but got " + lResult,
            lResult.equals( lExpectedResult ) );
   }


   /**
    * Execute the function.
    *
    * @return the {@link TaskInstanceResult}.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private TaskInstanceResult execute( int aTaskDbId, int aTaskId, int aInvNoDbId, int aInvNoId )
         throws Exception {

      // Build query arguments
      CallableStatement lImportCall = sDatabaseConnectionRule.getConnection()
            .prepareCall( " BEGIN   "
                  + " SCHED_STASK_PKG.FindLatestTaskInstance( an_TaskDbId => ?,an_TaskId=> ?,an_InvNoDbId=> ?,an_InvNoId=> ?,an_TaskInstanceDbId => ?,an_TaskInstanceId=> ?,on_Return=> ? ); "
                  + " END; " );

      lImportCall.setInt( 1, aTaskDbId );
      lImportCall.setInt( 2, aTaskId );
      lImportCall.setInt( 3, aInvNoDbId );
      lImportCall.setInt( 4, aInvNoId );
      lImportCall.registerOutParameter( 5, Types.NUMERIC );
      lImportCall.registerOutParameter( 6, Types.NUMERIC );
      lImportCall.registerOutParameter( 7, Types.NUMERIC );

      lImportCall.executeQuery();

      int lTaskInstanceDbId = lImportCall.getInt( 5 );
      int lTaskInstanceId = lImportCall.getInt( 6 );
      int lReturnVal = lImportCall.getInt( 7 );

      // return the results
      return new TaskInstanceResult( lTaskInstanceDbId, lTaskInstanceId, lReturnVal );
   }


   private class TaskInstanceResult {

      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + getOuterType().hashCode();
         result = prime * result + lReturnVal;
         result = prime * result + lTaskInstanceDbId;
         result = prime * result + lTaskInstanceId;
         return result;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals( Object obj ) {
         if ( this == obj )
            return true;
         if ( obj == null )
            return false;
         if ( getClass() != obj.getClass() )
            return false;
         TaskInstanceResult other = ( TaskInstanceResult ) obj;
         if ( !getOuterType().equals( other.getOuterType() ) )
            return false;
         if ( lReturnVal != other.lReturnVal )
            return false;
         if ( lTaskInstanceDbId != other.lTaskInstanceDbId )
            return false;
         if ( lTaskInstanceId != other.lTaskInstanceId )
            return false;
         return true;
      }


      private int lTaskInstanceDbId;
      private int lTaskInstanceId;
      private int lReturnVal;


      TaskInstanceResult(int aTaskInstanceDbId, int aTaskInstanceId, int aReturnVal) {
         lTaskInstanceDbId = aTaskInstanceDbId;
         lTaskInstanceId = aTaskInstanceId;
         lReturnVal = aReturnVal;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public String toString() {
         return "TaskInstanceResult [lTaskInstanceDbId=" + lTaskInstanceDbId + ", lTaskInstanceId="
               + lTaskInstanceId + ", lReturnVal=" + lReturnVal + "]";
      }


      private FindLatestTaskInstanceTest getOuterType() {
         return FindLatestTaskInstanceTest.this;
      }
   }
}
