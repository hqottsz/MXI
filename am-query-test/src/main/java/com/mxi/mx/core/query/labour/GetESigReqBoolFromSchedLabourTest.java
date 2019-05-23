
package com.mxi.mx.core.query.labour;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 * This class tests the GetESigReqBoolFromSchedLabour query.
 *
 * @author Huan Gao
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetESigReqBoolFromSchedLabourTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetESigReqBoolFromSchedLabourTest.class );
   }


   private static final SchedLabourKey LABOUR_DISABLED = new SchedLabourKey( 4650, 46086 );

   private static final SchedLabourKey LABOUR_ENABLED = new SchedLabourKey( 4650, 46085 );

   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where the labour skill doesn't have esigReqBool enable.
    *
    * <ol>
    * <li>Query by labour id.</li>
    * <li>Verify that esig_req_bool is false.</li> *
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testEsigReqBoolDisable() throws Exception {
      execute( LABOUR_DISABLED );
      assertFalse( iDataSet.hasNext() );
   }


   /**
    * Test the case where the labour skill does have esigReqBool enable.
    *
    * <ol>
    * <li>Query by labour id.</li>
    * <li>Verify that esig_req_bool is 1.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testEsigReqBoolEnable() throws Exception {
      execute( LABOUR_ENABLED );
      assertTrue( iDataSet.hasNext() );
   }


   /**
    * Execute the query.
    *
    * @param aLabour
    *           the labour key
    */
   private void execute( SchedLabourKey aLabour ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( new HumanResourceKey( 4650, 6000029 ), "aHrDbId", "aHrId" );
      lArgs.addWhereIn( new String[] { "sched_labour.labour_db_id", "sched_labour.labour_id" },
            aLabour );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
