
package com.mxi.am.db.qrx;

/**
 * A polyfill for Java 8's java.util.function.Predicate<T>
 */
public interface Predicate<T> {

   /**
    * Evaluates this predicate on the given argument
    *
    * @param t
    *           the argument
    * @return if the predicate matches the argument
    */
   boolean test( T t );

}
