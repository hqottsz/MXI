
package com.mxi.mx.testing.matchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;

import com.ibm.icu.util.Calendar;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.key.MxKey;
import com.mxi.mx.common.utils.IOUtils;
import com.mxi.mx.core.key.DataTypeKey;


/**
 * Provides a collection of matchers
 */
public class MxMatchers extends Matchers {

   /**
    * Creates a new {@linkplain MxMatchers} object.
    */
   private MxMatchers() {
      // Use static functions instead
   }


   /**
    * Returns a matcher that validates that all collection item matches the specified matcher
    *
    * @param aMatcher
    *           the item matcher
    *
    * @return the iterable matcher
    */
   public static <T> AllElements<T> allElements( Matcher<T> aMatcher ) {
      return new AllElements<T>( aMatcher );
   }


   /**
    * Syntactic sugar to allow generics for allOf. The default allOf uses varargs (i.e.: Object...
    * aObjects). Warnings are generated since it cannot use Java's generics due to type-erasure.
    * This approach allows us to use generics by overriding the varargs method with generic-safe
    * one.
    *
    * @param aMatcher1
    *           the matcher
    *
    * @return a matcher that matches all the matchers
    */
   public static <T> Matcher<T> allOf( Matcher<T> aMatcher1 ) {
      List<Matcher<? super T>> lList = new ArrayList<Matcher<? super T>>();
      lList.add( aMatcher1 );

      return allOf( lList );
   }


   /**
    * Syntactic sugar to allow generics for allOf. The default allOf uses varargs (i.e.: Object...
    * aObjects). Warnings are generated since it cannot use Java's generics due to type-erasure.
    * This approach allows us to use generics by overriding the varargs method with generic-safe
    * one.
    *
    * @param aMatcher1
    *           the matcher
    * @param aMatcher2
    *           the matcher
    *
    * @return a matcher that matches all the matchers
    */
   public static <T> Matcher<T> allOf( Matcher<? super T> aMatcher1,
         Matcher<? super T> aMatcher2 ) {
      List<Matcher<? super T>> lList = new ArrayList<Matcher<? super T>>();
      lList.add( aMatcher1 );
      lList.add( aMatcher2 );

      return allOf( lList );
   }


   /**
    * Syntactic sugar to allow generics for allOf. The default allOf uses varargs (i.e.: Object...
    * aObjects). Warnings are generated since it cannot use Java's generics due to type-erasure.
    * This approach allows us to use generics by overriding the varargs method with generic-safe
    * one.
    *
    * @param aMatcher1
    *           the matcher
    * @param aMatcher2
    *           the matcher
    * @param aMatcher3
    *           the matcher
    *
    * @return a matcher that matches all the matchers
    */
   public static <T> Matcher<T> allOf( Matcher<? super T> aMatcher1, Matcher<? super T> aMatcher2,
         Matcher<? super T> aMatcher3 ) {
      List<Matcher<? super T>> lList = new ArrayList<Matcher<? super T>>();
      lList.add( aMatcher1 );
      lList.add( aMatcher2 );
      lList.add( aMatcher3 );

      return allOf( lList );
   }


   /**
    * Syntactic sugar to allow generics for allOf. The default allOf uses varargs (i.e.: Object...
    * aObjects). Warnings are generated since it cannot use Java's generics due to type-erasure.
    * This approach allows us to use generics by overriding the varargs method with generic-safe
    * one.
    *
    * @param aMatcher1
    *           the matcher
    * @param aMatcher2
    *           the matcher
    * @param aMatcher3
    *           the matcher
    * @param aMatcher4
    *           the matcher
    *
    * @return a matcher that matches all the matchers
    */
   public static <T> Matcher<T> allOf( Matcher<? super T> aMatcher1, Matcher<? super T> aMatcher2,
         Matcher<? super T> aMatcher3, Matcher<? super T> aMatcher4 ) {
      List<Matcher<? super T>> lList = new ArrayList<Matcher<? super T>>();
      lList.add( aMatcher1 );
      lList.add( aMatcher2 );
      lList.add( aMatcher3 );
      lList.add( aMatcher4 );

      return allOf( lList );
   }


   /**
    * Syntactic sugar to allow generics for allOf. The default allOf uses varargs (i.e.: Object...
    * aObjects). Warnings are generated since it cannot use Java's generics due to type-erasure.
    * This approach allows us to use generics by overriding the varargs method with generic-safe
    * one.
    *
    * @param aMatcher1
    *           the matcher
    * @param aMatcher2
    *           the matcher
    * @param aMatcher3
    *           the matcher
    * @param aMatcher4
    *           the matcher
    * @param aMatcher5
    *           the matcher
    *
    * @return a matcher that matches all the matchers
    */
   public static <T> Matcher<T> allOf( Matcher<? super T> aMatcher1, Matcher<? super T> aMatcher2,
         Matcher<? super T> aMatcher3, Matcher<? super T> aMatcher4,
         Matcher<? super T> aMatcher5 ) {
      List<Matcher<? super T>> lList = new ArrayList<Matcher<? super T>>();
      lList.add( aMatcher1 );
      lList.add( aMatcher2 );
      lList.add( aMatcher3 );
      lList.add( aMatcher4 );
      lList.add( aMatcher5 );

      return allOf( lList );
   }


   /**
    * Syntactic sugar to allow generics for allOf. The default allOf uses varargs (i.e.: Object...
    * aObjects). Warnings are generated since it cannot use Java's generics due to type-erasure.
    * This approach allows us to use generics by overriding the varargs method with generic-safe
    * one.
    *
    * @param aMatcher1
    *           the matcher
    * @param aMatcher2
    *           the matcher
    * @param aMatcher3
    *           the matcher
    * @param aMatcher4
    *           the matcher
    * @param aMatcher5
    *           the matcher
    * @param aMatcher6
    *           the matcher
    *
    * @return a matcher that matches all the matchers
    */
   public static <T> Matcher<T> allOf( Matcher<? super T> aMatcher1, Matcher<? super T> aMatcher2,
         Matcher<? super T> aMatcher3, Matcher<? super T> aMatcher4, Matcher<? super T> aMatcher5,
         Matcher<? super T> aMatcher6 ) {
      List<Matcher<? super T>> lList = new ArrayList<Matcher<? super T>>();
      lList.add( aMatcher1 );
      lList.add( aMatcher2 );
      lList.add( aMatcher3 );
      lList.add( aMatcher4 );
      lList.add( aMatcher5 );
      lList.add( aMatcher6 );

      return allOf( lList );
   }


   /**
    * Returns a matcher that validates that at least one of collection item matches the specified
    * matcher
    *
    * @param aMatcher
    *           the item matcher
    *
    * @return the iterable matcher
    */
   public static <T> AnyElements<T> anyElements( Matcher<T> aMatcher ) {
      return new AnyElements<T>( aMatcher );
   }


   /**
    * Returns a matches the dataset argument value
    *
    * @param aArgument
    *           the argument name
    * @param aMatcher
    *           the argument value
    *
    * @return the dataset argument matcher
    */
   public static DataSetArgumentMatcher argument( String aArgument, Matcher<?> aMatcher ) {
      return new DataSetArgumentMatcher( aArgument, aMatcher );
   }


   /**
    * See {@link Assert#assertThat(Object, org.hamcrest.Matcher)}
    *
    * @param aObject
    *           the object to match
    * @param aMatcher
    *           the matching criterion
    */
   public static <T> void assertThat( T aObject, Matcher<T> aMatcher ) {
      Assert.assertThat( aObject, aMatcher );
   }


   /**
    * Syntactic sugar to get the time component of the date only. This sets the current date to Day
    * 0 (Jan 1, 1970).
    *
    * @param aDateTime
    *           the date-time
    *
    * @return the time
    */
   public static Date asTime( Date aDateTime ) {
      Calendar lCalendar = Calendar.getInstance();
      lCalendar.setTime( aDateTime );
      lCalendar.set( 1970, 0, 1 );

      return lCalendar.getTime();
   }


   /**
    * Returns TRUE if object matches the matcher
    *
    * @param aObject
    *           the object
    * @param aMatcher
    *           the matcher
    *
    * @return TRUE if match
    */
   public static <T> boolean checkThat( T aObject, Matcher<T> aMatcher ) {
      return aMatcher.matches( aObject );
   }


   /**
    * Syntactic sugar to get the current time.
    *
    * @return the current time
    */
   public static Date currentTime() {
      return asTime( new Date() );
   }


   /**
    * Returns a matcher for an empty collection.
    *
    * @return a matcher
    */
   public static Matcher<Collection<?>> empty() {
      return new IsEmpty();
   }


   /**
    * Returns a matcher for a value that matches (compareTo(aObject) == 0) the provided object. See
    * {@link IsEquivalentTo}
    *
    * @param aObject
    *           the object
    *
    * @return a matcher
    */
   public static Matcher<BigDecimal> equalTo( BigDecimal aObject ) {
      return new IsBigDecimalEquivalentTo<BigDecimal>( aObject );
   }


   /**
    * Returns a matcher to match keys in dataset arguments
    *
    * @param aKey
    *           the key value
    * @param aKeyColumns
    *           the query arguments
    *
    * @return the {@link DataSetArgument} matcher
    */
   public static Matcher<DataSetArgument> keyArgument( MxKey aKey, String... aKeyColumns ) {
      List<Matcher<? super DataSetArgument>> lMatchers =
            new ArrayList<Matcher<? super DataSetArgument>>();

      for ( int i = 0; i < aKeyColumns.length; i++ ) {
         lMatchers.add( argument( aKeyColumns[i], is( equalTo( aKey.getKeyValue( i + 1 ) ) ) ) );
      }

      return allOf( lMatchers );
   }


   /**
    * Returns a matcher that determines if the given matchers match all the items in a list.
    *
    * @param aMatcher1
    *           The first matcher
    * @param aMatcher2
    *           The other matcher
    *
    * @return The matcher
    */
   public static <T> Matcher<List<T>> listContainingOnly( Matcher<? super T> aMatcher1,
         Matcher<? super T> aMatcher2 ) {
      List<Matcher<? super T>> lMatchers = new ArrayList<Matcher<? super T>>();
      lMatchers.add( aMatcher1 );
      lMatchers.add( aMatcher2 );

      return ListContainingOnly.listContainingOnly( lMatchers );
   }


   /**
    * Returns a matcher that checks that all elements do not match the element matcher
    *
    * @param aMatcher
    *           the element matcher
    *
    * @return the iterable matcher
    */
   public static <T> NoElements<T> noElements( Matcher<T> aMatcher ) {
      return new NoElements<T>( aMatcher );
   }


   /**
    * Syntactic sugar to make testing readable. e.g.:
    *
    * <pre>
    * within( 5, Calendar.SECOND, of( DEADLINE_START_DATE ) )
    * </pre>
    *
    * @param aObject
    *           an object
    *
    * @return the object
    */
   public static <T> T of( T aObject ) {
      return aObject;
   }


   /**
    * Asserts that the query name is called and that the query exists
    *
    * @param aQueryName
    *           the name of the query
    *
    * @return the query
    */
   public static Matcher<String> query( String aQueryName ) {

      // Unit tests do not check the existence of the query name. If the query name changes, no
      // tests would fail. This is beyond the scope of a unit test, however most unit tests will not
      // have corresponding integration tests to verify that this integration point works. This call
      // will throw an exception is the query does not exist.
      IOUtils.getQueryFile( aQueryName );

      return equalTo( aQueryName );
   }


   /**
    * Returns a matcher for a usage delta of the given amount for the give data type.
    *
    * @param aDelta
    *           The delta
    * @param aDataType
    *           The data type
    *
    * @return The matcher
    */
   public static UsageDeltaOf usageDeltaOf( Matcher<Double> aDelta, DataTypeKey aDataType ) {
      return new UsageDeltaOf( aDelta, aDataType );
   }


   /**
    * Returns a matcher that checks if a date is within an amount of time (relative to current time)
    *
    * @param aAmountOfTime
    *           the amount of time
    * @param aUnitOfTime
    *           the unit of time (@see {@link Calendar})
    *
    * @return a matcher
    */
   public static Matcher<Date> within( int aAmountOfTime, int aUnitOfTime ) {
      return new IsWithin( aAmountOfTime, aUnitOfTime );
   }


   /**
    * Returns a matcher that checks if a date is within two dates
    *
    * @param aStartDate
    *           the start date
    * @param aEndDate
    *           the end date
    *
    * @return a matcher
    */
   public static Matcher<Date> within( Date aStartDate, Date aEndDate ) {
      return new IsWithin( aStartDate, aEndDate );
   }


   /**
    * Returns a matcher that checks if a date is within an amount of time (relative to specified
    * time)
    *
    * @param aAmountOfTime
    *           the amount of time
    * @param aUnitOfTime
    *           the unit of time (@see {@link Calendar})
    * @param aRelativeToDate
    *           the relative time
    *
    * @return a matcher
    */
   public static Matcher<Date> within( int aAmountOfTime, int aUnitOfTime, Date aRelativeToDate ) {
      return new IsWithin( aAmountOfTime, aUnitOfTime, aRelativeToDate );
   }
}
