
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertEquals;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * Tests the isApplicable function
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsApplicableTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Creates the database data
    */
   public void createTestData() {
      // Do nothing. Will be done by individual Scenario objects
   }


   /**
    * Test that when the Applicability Code is in the applicability range
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testApplicabilityCodeInRange() throws Exception {
      int lResult = execute( "100-199", "101" );

      assertEquals( 1, lResult );
   }


   /**
    * Test that when the Applicability Code is out of the applicability range
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testApplicabilityCodeOutOfRange() throws Exception {
      int lResult = execute( "200-299", "101" );

      assertEquals( 0, lResult );
   }


   /**
    * Test that when the Code is NULL. It is considered applicable when the code is NULL
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testCodeNull() throws Exception {
      int lResult = execute( "100-199", null );

      assertEquals( 1, lResult );
   }


   /**
    * Test that when the applicability range is N/A. Since the range is N/A therefore the result is
    * not papplicable
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testRangeNA() throws Exception {
      int lResult = execute( "N/A", null );

      assertEquals( 0, lResult );
   }


   /**
    * Test when the provided range contains the provided code but there is a newline character
    * before the range, that the code is deemed applicable.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNewLineCharBeforeRangeContainingCode() throws Exception {
      int lResult = execute( "\n100-199", "150" );
      assertEquals( 1, lResult );
   }


   /**
    * Test when the provided range contains the provided code but there is a newline character
    * before the hyphen, that the code is deemed applicable.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNewLineCharBeforeHyphenInRangeContainingCode() throws Exception {
      int lResult = execute( "100\n-199", "150" );
      assertEquals( 1, lResult );
   }


   /**
    * Test when the provided range contains the provided code but there is a newline character after
    * the hyphen, that the code is deemed applicable.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNewLineCharAfterHyphenInRangeContainingCode() throws Exception {
      int lResult = execute( "100-\n199", "150" );
      assertEquals( 1, lResult );
   }


   /**
    * Test when the provided range contains the provided code but there is a newline character after
    * the range, that the code is deemed applicable.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNewLineCharAfterCommaInRangeContainingCode() throws Exception {
      int lResult = execute( "100-199\n", "150" );
      assertEquals( 1, lResult );
   }


   /**
    * Test when the provided, comma separated, range contains the provided code but there is a
    * newline character before the range, that the code is deemed applicable.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNewLineCharBeforeRangeWithCommaContainingCode() throws Exception {
      int lResult = execute( "\n100,199", "199" );
      assertEquals( 1, lResult );
   }


   /**
    * Test when the provided, comma separated, range contains the provided code but there is a
    * newline character before the comma, that the code is deemed applicable.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNewLineCharBeforeCommaInRangeWithCommaContainingCode() throws Exception {
      int lResult = execute( "100\n,199", "199" );
      assertEquals( 1, lResult );
   }


   /**
    * Test when the provided, comma separated, range contains the provided code but there is a
    * newline character after the comma, that the code is deemed applicable.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNewLineCharAfterCommaInRangeWithCommaContainingCode() throws Exception {
      int lResult = execute( "100,\n199", "199" );
      assertEquals( 1, lResult );
   }


   /**
    * Test when the provided, comma separated, range contains the provided code but there is a
    * newline character after the range, that the code is deemed applicable.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testNewLineCharAfterRangeWithCommaContainingCode() throws Exception {
      int lResult = execute( "100,199\n", "199" );
      assertEquals( 1, lResult );
   }


   /**
    * Execute the function.
    *
    * @param aRange
    *           The Part Group applicability range
    * @param aCode
    *           The aircraft / assembly applicability code
    *
    * @return Determine if the give applicability code is applicable to the alternate part range.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private int execute( String aRange, String aCode ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      if ( aRange != null ) {
         lArgs.add( "as_range", aRange );
      }

      if ( aCode != null ) {
         lArgs.add( "as_code", aCode );
      }

      String[] lParmOrder = { "as_range", "as_code" };

      // Execute the query
      return Integer
            .parseInt( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getFunctionName( getClass() ), lParmOrder, lArgs ) );
   }
}
