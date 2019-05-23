
package com.mxi.mx.web.query.forecast;

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
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.HumanResourceKey;


/**
 * DOCUMENT_ME
 *
 * @author dsewell
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FcAssignedAircraftsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FcAssignedAircraftsTest.class );
   }


   private static final FcModelKey FC_MODEL = new FcModelKey( 4650, 1 );
   private static final HumanResourceKey HR_NO_AUTHORITY = new HumanResourceKey( 4650, 3 );
   private static final HumanResourceKey HR_ALL_AUTHORITY = new HumanResourceKey( 4650, 4 );
   private static final HumanResourceKey HR_SPECIFIC_AUTHORITY = new HumanResourceKey( 4650, 5 );

   private DataSet iDataSet;


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testQuery() throws Exception {
      execute( FC_MODEL, HR_NO_AUTHORITY );

      assertEquals( 1, iDataSet.getTotalRowCount() );

      execute( FC_MODEL, HR_ALL_AUTHORITY );

      assertEquals( 3, iDataSet.getTotalRowCount() );

      execute( FC_MODEL, HR_SPECIFIC_AUTHORITY );

      assertEquals( 2, iDataSet.getTotalRowCount() );
   }


   /**
    * Execute the query.
    *
    * @param aFcModel
    *           The Forecast Model Pk
    * @param aHr
    *           The human resource
    */
   private void execute( FcModelKey aFcModel, HumanResourceKey aHr ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aFcModel, new String[] { "aModelDbId", "aModelId" } );
      lArgs.add( aHr, new String[] { "aHrDbId", "aHrId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      iDataSet.next();
   }
}
