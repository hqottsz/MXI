package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * This is a test class for testing calculateUsageRemaining function in the database
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CalculateUsageRemainingTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Test
   public void itRoundsUsageToPrecisionWhenUsageFractionalDigitsAreMoreThanPrecision()
         throws Exception {

      // Given
      double lDueUsage = 1;
      double lUsage = 0.99999;
      int lPrecision = 4;
      // When
      Double lActualCalculatedUsageRemaining = execute( lDueUsage, lUsage, lPrecision );
      // Then
      Double lExpectedCalculatedUsageRemaining = Double.valueOf( 0 );
      assertEquals(
            "Unexpected Usage Remaining. Expected calculateUsageRemaining function to round the usage value to 1 before its calculation.",
            lExpectedCalculatedUsageRemaining, lActualCalculatedUsageRemaining );

   }


   @Test
   public void itDoestNotRoundUsageToPrecisionWhenUsageFractionalDigitsAreSameAsPrecision()
         throws Exception {

      // Given
      double lDueUsage = 1;
      double lUsage = 0.99999;
      int lPrecision = 5;
      // When
      Double lActualCalculatedUsageRemaining = execute( lDueUsage, lUsage, lPrecision );
      // Then
      Double lExpectedCalculatedUsageRemaining = 0.00001;
      assertEquals(
            "Unexpected Usage Remaining. Expected calculateUsageRemaining function to not perform any rounding before its calculation.",
            lExpectedCalculatedUsageRemaining, lActualCalculatedUsageRemaining );

   }


   @Test
   public void itDoestNotRoundUsageToPrecisionWhenUsageFractionalDigitsAreLessThanPrecision()
         throws Exception {

      // Given
      double lDueUsage = 1;
      double lUsage = 0.999;
      int lPrecision = 4;
      // When
      Double lActualCalculatedUsageRemaining = execute( lDueUsage, lUsage, lPrecision );
      // Then
      Double lExpectedCalculatedUsageRemaining = 0.001;
      assertEquals(
            "Unexpected Usage Remaining. Expected calculateUsageRemaining function to not perform any rounding before its calculation.",
            lExpectedCalculatedUsageRemaining, lActualCalculatedUsageRemaining );

   }


   @Test
   public void itRoundsUsageToPrecisionWhenUsageHasFractionalDigitsAndPrecisionIsZero()
         throws Exception {

      // Given
      double lDueUsage = 10;
      double lUsage = 10.1111;
      int lPrecision = 0;
      // When
      Double lActualCalculatedUsageRemaining = execute( lDueUsage, lUsage, lPrecision );
      // Then
      Double lExpectedCalculatedUsageRemaining = Double.valueOf( 0 );
      assertEquals(
            "Unexpected Usage Remaining. Expected calculateUsageRemaining function to round the usage value to 10 before its calculation.",
            lExpectedCalculatedUsageRemaining, lActualCalculatedUsageRemaining );

   }


   @Test
   public void itRoundsUsageIntegerPartWhenPrecisionIsNegative() throws Exception {

      // Given
      double lDueUsage = -10;
      double lUsage = 16.1111;
      int lPrecision = -1;
      // When
      Double lActualCalculatedUsageRemaining = execute( lDueUsage, lUsage, lPrecision );
      // Then
      Double lExpectedCalculatedUsageRemaining = Double.valueOf( -30 );
      assertEquals(
            "Unexpected Usage Remaining. Expected calculateUsageRemaining function to round the usage value to 20 before its calculation.",
            lExpectedCalculatedUsageRemaining, lActualCalculatedUsageRemaining );

   }


   /**
    * Execute the function.
    *
    * @param aDueUsage
    *           Provided Due Usage
    * @param aUsage
    *           Provided Usage
    * @param aPrecision
    *           Usage data type's precision
    *
    * @return the calculated usage remaining
    *
    * @throws Exception
    *            if an error occurs.
    */
   private Double execute( Double aDueUsage, Double aUsage, Integer aPrecision ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      if ( aDueUsage != null ) {
         lArgs.add( "aDueUsageQt", aDueUsage );
      }

      if ( aUsage != null ) {
         lArgs.add( "aUsageQt", aUsage );
      }

      if ( aPrecision != null ) {
         lArgs.add( "aPrecisionQt", aPrecision );
      }

      String[] lParmOrder = { "aDueUsageQt", "aUsageQt", "aPrecisionQt" };

      // Execute the query
      return Double
            .parseDouble( ( QueryExecutor.executeFunction( iDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getFunctionName( getClass() ), lParmOrder, lArgs ) ) );
   }

}
