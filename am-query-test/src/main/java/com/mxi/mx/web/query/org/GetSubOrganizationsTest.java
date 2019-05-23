
package com.mxi.mx.web.query.org;

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
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetSubOrganizationsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetSubOrganizationsTest.class );
   }


   /**
    * Asserts the database row is equal to expected data
    *
    * @param lData
    *           The Data
    * @param aOrgKey
    *           The organization
    * @param aOrgCd
    *           The organization code
    * @param aOrgSDesc
    *           The organization description
    * @param level
    *           The level of indentation
    */
   public void assertRow( DataSet lData, OrgKey aOrgKey, String aOrgCd, String aOrgSDesc,
         int level ) {

      // Assert Key
      MxAssert.assertEquals( aOrgKey.toString(), lData.getString( "org_key" ) );

      // Assert Code
      MxAssert.assertEquals( aOrgCd, lData.getString( "org_cd" ) );

      // Assert Dropdown Option
      String lOrgCdSDesc = aOrgCd + " (" + aOrgSDesc + ")";
      MxAssert.assertEquals( lOrgCdSDesc, lData.getString( "org_cd_sdesc" ) );

      // Assert HTML Display
      // LPAD(' ', (level - 1) * 6, "&#160;") + org_cd_sdesc
      String lOrgHTMLDisplay = ( ( level > 0 ) ? " " : "" ) + lOrgCdSDesc;
      for ( int i = 0; i < level; i++ ) {
         lOrgHTMLDisplay = ( ( i > 0 ) ? "      " : "     " ) + lOrgHTMLDisplay;
      }

      MxAssert.assertEquals( lOrgHTMLDisplay, lData.getString( "org_html_display" ) );
   }


   /**
    * Tests that the query returns the organization in expected order
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testBasicQuery() throws Exception {
      DataSet lResult = execute( null );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 0, 1 ), "MXI", "Mxi Technologies", 0 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 1 ), "RD", "R&D", 1 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 3 ), "HOGAN", "Team Hogan", 2 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 2 ), "SRV", "Services", 1 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 4 ), "GOONS", "The Goons", 2 );

      MxAssert.assertFalse( lResult.next() );
   }


   /**
    * Tests that the query returns the organization in expected order
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testHumanResourceQuery() throws Exception {

      // Test Case #1
      DataSet lResult = execute( new HumanResourceKey( 4650, 1 ) );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 0, 1 ), "MXI", "Mxi Technologies", 0 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 1 ), "RD", "R&D", 1 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 3 ), "HOGAN", "Team Hogan", 2 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 2 ), "SRV", "Services", 1 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 4 ), "GOONS", "The Goons", 2 );

      MxAssert.assertFalse( lResult.next() );

      /**
       * Test Case #2 covers: * Non-primary organizations are included * Heirarchy is presented in
       * terms or relative highest organization the user belongs to * When a user belongs to an
       * organization which is a suborganization that it also belongs to that it only includes the
       * sub-organization as a sub-organization.
       */
      lResult = execute( new HumanResourceKey( 4650, 2 ) );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 1 ), "RD", "R&D", 0 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 3 ), "HOGAN", "Team Hogan", 1 );

      MxAssert.assertTrue( lResult.next() );
      assertRow( lResult, new OrgKey( 4650, 4 ), "GOONS", "The Goons", 0 );

      MxAssert.assertFalse( lResult.next() );
   }


   /**
    * Execute the query.
    *
    * @param aHrKey
    *           The Human Resource Key
    *
    * @return dataSet result.
    */
   private DataSet execute( HumanResourceKey aHrKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      if ( aHrKey != null ) {
         lArgs.add( aHrKey, new String[] { "aHrDbId", "aHrId" } );
      }

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
