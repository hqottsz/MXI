
package com.mxi.am.helper;

/**
 * A filter evaluates an argument and returns TRUE or FALSE.
 */
public interface FilterCriteria<T> {

   boolean test( T aObject );
}
