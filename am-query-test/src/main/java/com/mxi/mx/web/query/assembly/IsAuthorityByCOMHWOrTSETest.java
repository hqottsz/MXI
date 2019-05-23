
package com.mxi.mx.web.query.assembly;

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
 * Query to test if HR user has authorities to COMHW and TSE Assemblies
 *
 * @author okamenskova
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsAuthorityByCOMHWOrTSETest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsAuthorityByCOMHWOrTSETest.class );
   }


   /**
    * Tests if user with All Authorities has access the TSE and COMHW Assemblies
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIsAuthorityByAssembly_AllAuthorityUser() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 1 );
      lArgs.add( "aAssmblCd", "COMHW" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 1 );
      lArgs.add( "aAssmblCd", "TSE" );

      // execute the query
      lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
   }


   /**
    * Tests if user has Authority to access the COMHW Assembly
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIsAuthorityByAssembly_COMHW() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 2 );
      lArgs.add( "aAssmblCd", "COMHW" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // User with no Authorities at all
      lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 3 );
      lArgs.add( "aAssmblCd", "COMHW" );

      // execute the query
      lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be no rows returned
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Tests if user has Authority to access the TSE Assembly
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testIsAuthorityByAssembly_TSE() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 2 );
      lArgs.add( "aAssmblCd", "TSE" );

      // execute the query
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be 1 row
      MxAssert.assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );

      // User with no Authorities at all
      lArgs = new DataSetArgument();

      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 3 );
      lArgs.add( "aAssmblCd", "TSE" );

      // execute the query
      lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // There should be no rows returned
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }
}
