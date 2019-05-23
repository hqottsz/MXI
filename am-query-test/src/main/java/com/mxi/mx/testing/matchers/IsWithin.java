
package com.mxi.mx.testing.matchers;

import java.util.Calendar;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


/**
 * Matches date against a date range
 */
public class IsWithin extends TypeSafeMatcher<Date> {

   private final Date iEndDate;

   private final Date iStartDate;


   /**
    * Specifies a range with specified start and end dates
    *
    * @param aStartDate
    *           the start date
    * @param aEndDate
    *           the end date
    */
   public IsWithin(Date aStartDate, Date aEndDate) {
      iStartDate = aStartDate;
      iEndDate = aEndDate;
   }


   /**
    * Specifies a range using the current time and the amount of time on both sides
    *
    * @param aAmountOfTime
    *           the amount of time
    * @param aUnitOfTime
    *           the unit of time
    */
   public IsWithin(int aAmountOfTime, int aUnitOfTime) {
      this( aAmountOfTime, aUnitOfTime, new Date() );
   }


   /**
    * Specifies a range using the specified time and the amount of time on both sides
    *
    * @param aAmountOfTime
    *           the amount of time
    * @param aUnitOfTime
    *           the unit of time
    * @param aRelativeToDate
    *           the date
    */
   public IsWithin(int aAmountOfTime, int aUnitOfTime, Date aRelativeToDate) {
      Calendar lStartDate = Calendar.getInstance();
      lStartDate.setTime( aRelativeToDate );
      lStartDate.add( aUnitOfTime, -aAmountOfTime );
      iStartDate = lStartDate.getTime();

      Calendar lEndDate = Calendar.getInstance();
      lEndDate.setTime( aRelativeToDate );
      lEndDate.add( aUnitOfTime, aAmountOfTime );
      iEndDate = lEndDate.getTime();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText( "between " );
      aDescription.appendValue( iStartDate );
      aDescription.appendText( " and " );
      aDescription.appendValue( iEndDate );
   }


   /**
    * Ensures a date falls between the start date and the end date
    *
    * @param aDate
    *           the date to match against
    *
    * @return TRUE if date between iStartDate and iEndDate
    */
   @Override
   public boolean matchesSafely( Date aDate ) {
      return ( aDate.after( iStartDate ) && aDate.before( iEndDate ) );
   }
}
