
package com.mxi.mx.report.query.task;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query com.mxi.mx.report.Panels.qrx
 *
 * @author krangaswamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PanelsTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), PanelsTest.class );
   }


   /**
    * Tests the retrieval of panels.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testPanels() throws Exception {
      QuerySet lQs = this.execute( new TaskKey( 4650, 101 ) );
      Assert.assertTrue( lQs.next() );

      // Determine if the following are returned :
      Assert.assertEquals( "TEST - PANEL_DESC", lQs.getString( "panel" ) );

      Assert.assertFalse( lQs.next() );
   }


   /**
    * This method executes the query in Panels.qrx
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The dataset after execution.
    */
   private QuerySet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aTaskKey, "aTaskDbId", "aTaskId" );

      return QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }
}
