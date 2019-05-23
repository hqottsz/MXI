package com.mxi.mx.core.unittest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import com.mxi.mx.common.key.MxDbKey;
import com.mxi.mx.common.utils.StringUtils;


/**
 * A set of Mxi assert methods.
 *
 * @author asmolko
 * @created March 28, 2002
 */
public class MxAssert extends Assert {

   /**
    * Asserts that two BigDecimals are equal. If they are not an AssertionFailedError is thrown.
    *
    * @param aExpected
    *           expected value.
    * @param aActual
    *           actual value.
    */
   public static void assertEquals( BigDecimal aExpected, BigDecimal aActual ) {
      assertEquals( null, aExpected, aActual );
   }


   /**
    * Asserts that two date objects are equal. If they are not an AssertionFailedError is thrown.
    * Dates are assumed to be equal if the diffrence between both of them is not more than 5
    * minutes.
    *
    * @param aExpected
    *           expected date.
    * @param aActual
    *           actual date.
    */
   public static void assertEquals( Date aExpected, Date aActual ) {
      assertEquals( null, aExpected, aActual, 5 );
   }


   /**
    * Asserts that two date objects are equal. If they are not an AssertionFailedError is thrown.
    * Dates are assumed to be equal if the diffrence between both of them is not more than
    * 3600000ms.
    *
    * @param aColumnName
    *           failed column name.
    * @param aExpected
    *           expected date.
    * @param aActual
    *           actual date.
    */
   public static void assertEquals( String aColumnName, Date aExpected, Date aActual ) {
      assertEquals( aColumnName, aExpected, aActual, 5 );
   }


   /**
    * Asserts that two MxKey objects are equal. If they are not an AssertionFailedError is thrown.
    *
    * @param aColumnNames
    *           Description of Parameter.
    * @param aExpected
    *           expected value.
    * @param aActual
    *           actual value.
    */
   public static void assertEquals( String aColumnNames, MxDbKey aExpected, MxDbKey aActual ) {

      // compare two dates.
      if ( aExpected != null ) {
         if ( !aExpected.equals( aActual ) ) {
            Assert.fail( "For column " + aColumnNames + " an expected key value is:<" + aExpected
                  + ">, actual was: <" + aActual + ">" );
         }
      } else {
         Assert.assertNull( "Expected key is null, actual is: " + aActual, aActual );
      }
   }


   /**
    * Asserts that two Integers are equal. If they are not an AssertionFailedError is thrown.
    *
    * @param aColumnName
    *           failed column name.
    * @param aExpected
    *           expected value.
    * @param aActual
    *           actual value.
    */
   public static void assertEquals( String aColumnName, Integer aExpected, Integer aActual ) {

      // compare two dates.
      if ( aExpected != null ) {
         if ( !aExpected.equals( aActual ) ) {
            Assert.fail( "For column " + aColumnName + " an expected Integer value is: <"
                  + aExpected + ">, actual was: <" + aActual + ">" );
         }
      } else {
         Assert.assertNull( "For column " + aColumnName
               + " an expected Integer value is: <null>, actual was: <" + aActual + ">", aActual );
      }
   }


   /**
    * Asserts that two object are equal. If they are not an AssertionFailedError is thrown.
    *
    * @param aColumnName
    *           failed column name.
    * @param aExpected
    *           expected value.
    * @param aActual
    *           actual value.
    */
   public static void assertEquals( String aColumnName, Object aExpected, Object aActual ) {

      assertEquals( aColumnName, StringUtils.valueOf( aExpected ), StringUtils.valueOf( aActual ) );
   }


   /**
    * Asserts that two BigDecimals are equal. If they are not an AssertionFailedError is thrown.
    *
    * @param aColumnName
    *           failed column name.
    * @param aExpected
    *           expected value.
    * @param aActual
    *           actual value.
    */
   public static void assertEquals( String aColumnName, BigDecimal aExpected, BigDecimal aActual ) {

      // compare two dates.
      if ( aExpected != null ) {
         if ( !( aExpected.compareTo( aActual ) == 0 ) ) {
            Assert.fail( "For column " + aColumnName + " an expected String value is: <" + aExpected
                  + ">, actual was: <" + aActual + ">" );
         }
      } else {
         Assert.assertNull( "For column " + aColumnName
               + " an expected String value is: <null>, actual was: <" + aActual + ">", aActual );
      }
   }


   /**
    * Asserts that two Strings are equal. If they are not an AssertionFailedError is thrown.
    *
    * @param aColumnName
    *           failed column name.
    * @param aExpected
    *           expected value.
    * @param aActual
    *           actual value.
    */
   public static void assertEquals( String aColumnName, String aExpected, String aActual ) {

      // compare two dates.
      if ( aExpected != null ) {
         if ( !aExpected.equals( aActual ) ) {
            Assert.fail( "For column " + aColumnName + " an expected String value is: <" + aExpected
                  + ">, actual was: <" + aActual + ">" );
         }
      } else {
         Assert.assertNull( "For column " + aColumnName
               + " an expected String value is: <null>, actual was: <" + aActual + ">", aActual );
      }
   }


   /**
    * Asserts that two doubles are equal within a delta of 0.0001.
    *
    * @param aColumnName
    *           name of the column.
    * @param aExpected
    *           Expected number.
    * @param aActual
    *           Actual number.
    */
   public static void assertEquals( String aColumnName, Double aExpected, Double aActual ) {
      if ( ( ( aExpected == null ) && ( aActual != null ) )
            || ( ( aExpected != null ) && ( aActual == null ) ) ) {
         Assert.assertEquals( "For column " + aColumnName, aExpected, aActual );
      } else if ( ( aExpected != null ) && ( aActual != null ) ) {
         double lDelta = 0.0001;
         Assert.assertEquals( "For column " + aColumnName, aExpected, aActual, lDelta );
      }
   }


   /**
    * Asserts that two doubles are equal concerning a delta (1.0).
    *
    * @param aColumnName
    *           name of the column.
    * @param aExpected
    *           Expected number.
    * @param aActual
    *           Actual number.
    */
   public static void assertEquals( String aColumnName, double aExpected, double aActual ) {
      double lDelta = 0.01;
      Assert.assertEquals( "For column " + aColumnName, aExpected, aActual, lDelta );
   }


   /**
    * Asserts that two date objects are equal. If they are not an AssertionFailedError is thrown.
    * Dates are assumed to be equal if the diffrence between both of them is not more than the
    * deviation in minutes.
    *
    * @param aColumnName
    *           failed column name.
    * @param aExpected
    *           expected date.
    * @param aActual
    *           actual date.
    * @param aDeviation
    *           number of minutes of allowed deviation.
    */
   public static void assertEquals( String aColumnName, Date aExpected, Date aActual,
         int aDeviation ) {

      // if Both Dates are null, just return
      if ( ( aExpected == null ) && ( aActual == null ) ) {
         return;
      }

      // Setup the Error message
      SimpleDateFormat lFormatter = new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss" );
      int lMinutesDevAllowed = aDeviation;
      String lExpectedDate = ( aExpected != null ) ? lFormatter.format( aExpected ) : "";
      String lActualDate = ( aActual != null ) ? lFormatter.format( aActual ) : "";
      String lErrorMessage = "For column " + aColumnName + //
            " an expected value is: <" + lExpectedDate + "> but was: <" + lActualDate + ">. " + //
            "Allowed deviation in minutes:" + lMinutesDevAllowed;

      // If one is null and the other is not, then Fail
      if ( ( ( aExpected == null ) && ( aActual != null ) )
            || ( ( aExpected != null ) && ( aActual == null ) ) ) {
         Assert.fail( lErrorMessage );
      }

      // Verify that the Dates Match within the allowed deviation of minutes
      int lMinutesToMsConvertion = 60000;
      GregorianCalendar lActualCal = new GregorianCalendar();
      GregorianCalendar lExpectCal = new GregorianCalendar();

      lActualCal.setTime( aActual );
      lExpectCal.setTime( aExpected );

      // Clear out seconds and milliseconds
      lActualCal.set( Calendar.MILLISECOND, 0 );
      lActualCal.set( Calendar.SECOND, 0 );
      lExpectCal.set( Calendar.MILLISECOND, 0 );
      lExpectCal.set( Calendar.SECOND, 0 );

      long lDeviation = Math.abs( lExpectCal.getTime().getTime() - lActualCal.getTime().getTime() );
      if ( lDeviation > ( lMinutesDevAllowed * lMinutesToMsConvertion ) ) {
         Assert.fail( lErrorMessage );
      }
   }

}
