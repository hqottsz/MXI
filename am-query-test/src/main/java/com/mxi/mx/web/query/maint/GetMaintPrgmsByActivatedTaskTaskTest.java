
package com.mxi.mx.web.query.maint;

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
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the <tt>GetMaintPrgmsByActivatedTaskTask</tt> query. Various combinations of task
 * assignments to ACTV/REVISION/BUILD maintenance program revisions are verified.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetMaintPrgmsByActivatedTaskTaskTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetMaintPrgmsByActivatedTaskTaskTest.class );
   }


   private static final TaskTaskKey REQ_1 = new TaskTaskKey( 4650, 101 );
   private static final TaskTaskKey REQ_2 = new TaskTaskKey( 4650, 102 );
   private static final MaintPrgmKey MP1_ACTV = new MaintPrgmKey( 4650, 11 );
   private static final MaintPrgmKey MP2_REVISION = new MaintPrgmKey( 4650, 21 );
   private static final MaintPrgmKey MP3_BUILD = new MaintPrgmKey( 4650, 30 );


   /**
    * Tests maintenance program results for REQ_1.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testReq1() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( REQ_1, "aTaskDbId", "aTaskId" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );
      assertRow( lDs, MP1_ACTV );
      assertRow( lDs, MP2_REVISION );
   }


   /**
    * Tests maintenance program results for REQ_2.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testReq2() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( REQ_2, "aTaskDbId", "aTaskId" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      assertRow( lDs, MP1_ACTV );
      assertRow( lDs, MP3_BUILD );
   }


   /**
    * Ensures <tt>aMaintProgramKey</tt> is in the next row of the data set.
    *
    * @param aDataSet
    *           data set to verify
    * @param aMaintProgramKey
    *           maintenance program key that should be in the next row of <tt>
    *                           aDataSet</tt>
    */
   private void assertRow( DataSet aDataSet, MaintPrgmKey aMaintProgramKey ) {

      aDataSet.next();

      MxAssert.assertEquals( aMaintProgramKey,
            aDataSet.getKey( MaintPrgmKey.class, "maint_prgm_db_id", "maint_prgm_id" ) );
   }
}
