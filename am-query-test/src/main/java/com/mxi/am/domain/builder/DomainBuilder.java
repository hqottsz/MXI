
package com.mxi.am.domain.builder;

import com.mxi.am.domain.Domain;


/**
 * This class represents an object builder. Object Builders are ways to easily create test-data by
 * specifying the criteria that you desire and disregard the rest.
 *
 * @deprecated the new domain builders should not have this interface as it exposes the build
 *             function outside the package. The new format uses {@link Domain} to create the
 *             builders and to call build on them appropriately.
 */
@Deprecated
public interface DomainBuilder<T> {

   /**
    * Builds an object of type <T>.
    *
    * @return the object
    */
   T build();
}
