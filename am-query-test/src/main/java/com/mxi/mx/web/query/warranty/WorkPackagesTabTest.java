
package com.mxi.mx.web.query.warranty;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the WorkPackagesTab Page query file with the same package and
 * name.
 *
 * @author asachan
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class WorkPackagesTabTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), WorkPackagesTabTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Results of testWorkPackagesTab query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testWorkPackagesTab() throws Exception {
      execute();

      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();

      MxAssert.assertEquals( "4650:102", iDataSet.getString( "sched_key" ) );
      MxAssert.assertEquals( "ENG  T/R", iDataSet.getString( "event_sdesc" ) );
      MxAssert.assertEquals( "MONCTON", iDataSet.getString( "loc_cd" ) );
      MxAssert.assertEquals( "10:1000", iDataSet.getString( "work_loc_key" ) );
      MxAssert.assertEquals( "4650:1", iDataSet.getString( "inventory_key" ) );
      MxAssert.assertEquals( "2039", iDataSet.getString( "serial_no_oem" ) );
      MxAssert.assertEquals( "Ace Electronics", iDataSet.getString( "vendor_name" ) );
      MxAssert.assertEquals( "4650:1", iDataSet.getString( "vendor_pk" ) );
      MxAssert.assertEquals( "4650:100002", iDataSet.getString( "warranty_init_pk" ) );
      MxAssert.assertEquals( "4650:1", iDataSet.getString( "warranty_defn_pk" ) );
      MxAssert.assertEquals( "4650:1", iDataSet.getString( "warranty_eval_pk" ) );
      MxAssert.assertEquals( "Alert", iDataSet.getString( "warranty_sdesc" ) );
      MxAssert.assertEquals( "Alert", iDataSet.getString( "summary_sdesc" ) );
      MxAssert.assertEquals( "TASK", iDataSet.getString( "type_subtype" ) );
   }


   /**
    * Execute the query.
    */
   private void execute() {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aDayCount", 1 );
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
