
package com.mxi.mx.web.query.dropdown;

import org.junit.Assert;
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
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * This class tests the query com.mxi.mx.web.query.dropdown.UnassignedMinorModels.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class UnassignedMinorModelsTest {

   private static final String ASSIGNED_PART = "part2";
   private static final String INBUILD_PART = "part3";
   private static final String SER_PART = "part4";
   private static final String EXPECTED_PART = "PART1";

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), UnassignedMinorModelsTest.class );
   }


   /**
    * TEST CASE: Get aircraft minor models and only the ACTIVE aircraft parts without weight/balance
    * record
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetAircraftMinorModels() throws Exception {

      AssemblyKey lAssmblKey = new AssemblyKey( 4650, "AC1" );
      TaskTaskKey lTaskTaskKey = new TaskTaskKey( 4650, 21 );

      DataSet lDataSet = this.execute( lAssmblKey, lTaskTaskKey );

      Assert.assertEquals( "Number of retrieved rows", 1, lDataSet.getRowCount() );

      while ( lDataSet.next() ) {
         String lMinorModel = lDataSet.getString( "minor_model" );

         if ( ASSIGNED_PART.equals( lMinorModel ) ) {
            Assert.fail( "Shall not return assigned part." );
         }

         if ( INBUILD_PART.equals( lMinorModel ) ) {
            Assert.fail( "Shall only return ACTIVE part." );
         }

         if ( SER_PART.equals( lMinorModel ) ) {
            Assert.fail( "Shall not return AIRCRAFT part" );
         }

      }

      lDataSet.first();
      Assert.assertEquals( "Expecting minor model", EXPECTED_PART,
            lDataSet.getString( "minor_model" ) );
   }


   private DataSet execute( AssemblyKey aAssmblKey, TaskTaskKey aTaskTaskKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAssmblKey, "aAssemblyDbId", "aAssemblyCd" );
      lArgs.add( aTaskTaskKey, "aTaskDbId", "aTaskId" );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
