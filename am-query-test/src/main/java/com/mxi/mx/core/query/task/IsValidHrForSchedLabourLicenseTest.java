
package com.mxi.mx.core.query.task;

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
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.SchedLabourKey;


/**
 * This class tests the IsValidHrForSchedLabourLicense query.
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsValidHrForSchedLabourLicenseTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsValidHrForSchedLabourLicenseTest.class );
   }


   /**
    * Ensure the query returns no rows when the human resource is invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testInvalidHumanResource_NoLicense() throws Exception {
      assertNoRows( new HumanResourceKey( 4650, 2 ), new SchedLabourKey( 4650, 2002 ) );
   }


   /**
    * Ensure the query returns no rows when the human resource is invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testInvalidHumanResource_NotACTV() throws Exception {
      assertNoRows( new HumanResourceKey( 4650, 1 ), new SchedLabourKey( 4650, 2001 ) );
   }


   /**
    * Ensure the query returns no rows when the human resource is invalid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testInvalidHumanResource_WrongCarrier() throws Exception {
      assertNoRows( new HumanResourceKey( 4650, 3 ), new SchedLabourKey( 4650, 2003 ) );
   }


   /**
    * Test that one row is return if the human resource is valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testValidHumanResource_ACFT() throws Exception {
      assertOneRow( new HumanResourceKey( 4650, 4 ), new SchedLabourKey( 4650, 2004 ) );
   }


   /**
    * Test that one row is return if the human resource is valid.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testValidHumanResource_NonACFT() throws Exception {
      assertOneRow( new HumanResourceKey( 4650, 5 ), new SchedLabourKey( 4650, 2005 ) );
   }


   /**
    * Asserts the no rows are returned when called witht the given arguments.
    *
    * @param aHrKey
    *           The human resource
    * @param aLabourKey
    *           The labour
    */
   private void assertNoRows( HumanResourceKey aHrKey, SchedLabourKey aLabourKey ) {

      assertEquals( 0, executeQuery( aHrKey, aLabourKey ).getRowCount() );
   }


   /**
    * Asserts the no rows are returned when called witht the given arguments.
    *
    * @param aHrKey
    *           The human resource
    * @param aLabourKey
    *           The labour
    */
   private void assertOneRow( HumanResourceKey aHrKey, SchedLabourKey aLabourKey ) {

      assertEquals( 1, executeQuery( aHrKey, aLabourKey ).getRowCount() );
   }


   /**
    * Execute query.
    *
    * @param aHrKey
    *           Hr Key
    * @param aLabourKey
    *           Labour Key
    *
    * @return dataset
    */
   private DataSet executeQuery( HumanResourceKey aHrKey, SchedLabourKey aLabourKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHrKey, "aHrDbId", "aHrId" );
      lArgs.addWhereIn( new String[] { "sched_labour.labour_db_id", "sched_labour.labour_id" },
            aLabourKey );

      DataSet lDS = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.isValidHrForSchedLabourLicense", lArgs );

      return lDS;
   }
}
