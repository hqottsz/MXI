
package com.mxi.mx.web.query.ietm;

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
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the FaultDefinitions query file.
 *
 * @author akash
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FaultDefinitionsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FaultDefinitionsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test Search Results of FaultDefinitions query
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testFaultDefinitions() throws Exception {
      execute( new IetmTopicKey( "5000000:1000:53" ) );

      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "5000000:43061:53", "5000000:43061", "TestCd", "5000000:A320", "A320", "21-21-00" );
   }


   /**
    * Execute the query.
    *
    * @param aIetmTopic
    *           Ietm Topic Key
    */
   private void execute( IetmTopicKey aIetmTopic ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aIetmTopic, new String[] { "aIetmDbId", "aIetmId", "aIetmTopicId" } );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aFailModeIetm
    *           Failure Mode Ietm Key
    * @param aFailMode
    *           Failure Mode Key
    * @param aFailModeCd
    *           Failure Mode Code
    * @param aAssembly
    *           Assembly Key
    * @param aAssemblyCd
    *           Assembly Code
    * @param aAssemblyBomCd
    *           Assembly Bom Code
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aFailModeIetm, String aFailMode, String aFailModeCd,
         String aAssembly, String aAssemblyCd, String aAssemblyBomCd ) throws Exception {
      MxAssert.assertEquals( aFailModeIetm, iDataSet.getString( "fail_mode_ietm_key" ) );
      MxAssert.assertEquals( aFailMode, iDataSet.getString( "fail_mode_key" ) );
      MxAssert.assertEquals( aFailModeCd, iDataSet.getString( "fail_mode_cd" ) );
      MxAssert.assertEquals( aAssembly, iDataSet.getString( "assembly_key" ) );
      MxAssert.assertEquals( aAssemblyCd, iDataSet.getString( "assmbl_cd" ) );
      MxAssert.assertEquals( aAssemblyBomCd, iDataSet.getString( "assmbl_bom_cd" ) );
   }
}
