
package com.mxi.mx.core.query.part;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TransferKey;


/**
 * This class tests the GetExpectedTurnInForPartReq query.
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetExpectedTurnInForPartReqTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetExpectedTurnInForPartReqTest.class );
   }


   private static final TaskPartKey TASK_PART = new TaskPartKey( 4650, 2000, 1 );
   private static final TransferKey TURNIN = new TransferKey( 4650, 4000 );

   private static final TaskPartKey TASK_PART_DEPLOYED = new TaskPartKey( 4650, 2001, 1 );

   private static final TaskPartKey TASK_PART_NOT_ISSUED = new TaskPartKey( 4650, 2002, 1 );

   private static final TaskPartKey TASK_PART_NO_TURNIN = new TaskPartKey( 4650, 2003, 1 );

   private static final TaskPartKey TASK_PART_TURNIN_HAS_INV = new TaskPartKey( 4650, 2004, 1 );

   private static final TaskPartKey TASK_PART_TURNIN_COMPLETE = new TaskPartKey( 4650, 2005, 1 );


   /**
    * This tests the case where the turn in is pending.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResults = executeQuery( TASK_PART );

      assertEquals( 1, lResults.getRowCount() );

      lResults.first();

      assertEquals( TURNIN, lResults.getKey( TransferKey.class, "xfer_db_id", "xfer_id" ) );
   }


   /**
    * This tests the case where the part request is deployed.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryDeployed() throws Exception {
      DataSet lResults = executeQuery( TASK_PART_DEPLOYED );

      assertEquals( 0, lResults.getRowCount() );
   }


   /**
    * This tests the case where part request is not issued.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryNotIssued() throws Exception {
      DataSet lResults = executeQuery( TASK_PART_NOT_ISSUED );

      assertEquals( 0, lResults.getRowCount() );
   }


   /**
    * This tests the case where there is no turn in.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryNoTurnIn() throws Exception {
      DataSet lResults = executeQuery( TASK_PART_NO_TURNIN );

      assertEquals( 0, lResults.getRowCount() );
   }


   /**
    * Tesxts the case where the turn in is complete.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryTurnInComplete() throws Exception {
      DataSet lResults = executeQuery( TASK_PART_TURNIN_COMPLETE );

      assertEquals( 0, lResults.getRowCount() );
   }


   /**
    * Tesxts the case where the turn in has inventory associated with it.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testQueryTurnInHasInv() throws Exception {
      DataSet lResults = executeQuery( TASK_PART_TURNIN_HAS_INV );

      assertEquals( 0, lResults.getRowCount() );
   }


   /**
    * Executes the getPendingTurnInTransferForPartReq query with the given task part primary key.
    *
    * @param aTaskPart
    *           The task part key
    *
    * @return The resulting data set.
    */
   private DataSet executeQuery( TaskPartKey aTaskPart ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskPart, "aSchedDbId", "aSchedId", "aSchedPartId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
