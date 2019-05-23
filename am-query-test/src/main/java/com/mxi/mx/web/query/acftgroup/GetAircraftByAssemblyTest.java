
package com.mxi.mx.web.query.acftgroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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


/**
 * This class runs a unit test on the GetAircraftsByAssemby query file.
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftByAssemblyTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftByAssemblyTest.class );
   }


   /**
    * Test Case : retrieves the correct number of aircraft to select by assembly and group id.
    */
   @Test
   public void testGetAircraftByAssemblyAndGroupId() {

      // ARRANGE
      String lAcftAssembly = "A320";
      Integer lAircraftGroupId = 1001;
      String lUserId = "1";

      // ACT
      DataSet lResult = getAircraftByAssemblyAndGroupId( lAcftAssembly, lAircraftGroupId, lUserId );

      // ASSERT
      assertNotNull( lResult );

      assertEquals( "Number of retrieved rows for Aircraft Assembly:" + lAcftAssembly
            + " and Group Id: " + lAircraftGroupId, 3, lResult.getTotalRowCount() );
   }


   /**
    * Test Case : retrieves aircraft by group id without specified assembly, so all of aircraft from
    * all of aircraft assemblies will be retrieved excluding those already assigned to this group..
    */
   @Test
   public void testGetAircraftByGroupIdWithoutSpecifiedAssembly() {

      // ARRANGE
      String lAcftAssembly = null;
      Integer lAircraftGroupId = 1001;
      String lUserId = "1";

      // ACT
      DataSet lResult = getAircraftByAssemblyAndGroupId( lAcftAssembly, lAircraftGroupId, lUserId );

      // ASSERT
      assertNotNull( lResult );

      assertEquals( "Number of retrieved rows for Aircraft Assembly:" + lAcftAssembly
            + " and Group Id: " + lAircraftGroupId, 5, lResult.getTotalRowCount() );
   }


   /**
    * Test Case : retrieves the correct number of aircraft to select by assembly and group id but
    * the user has no authority.
    */
   @Test
   public void testGetAircraftByAssemblyAndGroupIdButUserWithoutAuthority() {

      // ARRANGE
      String lAcftAssembly = "A320";
      Integer lAircraftGroupId = 1001;
      String lUserId = "2";

      // ACT
      DataSet lResult = getAircraftByAssemblyAndGroupId( lAcftAssembly, lAircraftGroupId, lUserId );

      // ASSERT
      assertNotNull( lResult );

      assertEquals( "Number of retrieved rows for Aircraft Assembly:" + lAcftAssembly
            + " and Group Id: " + lAircraftGroupId, 2, lResult.getTotalRowCount() );
   }


   /**
    * Test Case : retrieves the correct number of aircraft to select by assembly and group id and
    * the user with all authority.
    */
   @Test
   public void testGetAircraftByAssemblyAndGroupIdAndUserWithAllAuthority() {

      // ARRANGE
      String lAcftAssembly = "A320";
      Integer lAircraftGroupId = 1001;
      String lUserId = "3";

      // ACT
      DataSet lResult = getAircraftByAssemblyAndGroupId( lAcftAssembly, lAircraftGroupId, lUserId );

      // ASSERT
      assertNotNull( lResult );

      assertEquals( "Number of retrieved rows for Aircraft Assembly:" + lAcftAssembly
            + " and Group Id: " + lAircraftGroupId, 3, lResult.getTotalRowCount() );
   }


   /**
    * Retrieve the aircrafts by aircraft assembly code and group id as well as user id.
    *
    */
   private DataSet getAircraftByAssemblyAndGroupId( String aAcftAssembly, Integer aGroupId,
         String aUserId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aAssembly", aAcftAssembly );
      lArgs.add( "aAircraftGroupId", aGroupId );
      lArgs.add( "aUserId", aUserId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }
}
