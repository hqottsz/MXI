
package com.mxi.am.domain.builder;

/**
 * The {@link TestDataBuilderStub} allows you to replace builders with actual values.
 */
public class TestDataBuilderStub<T> implements DomainBuilder<T> {

   private final T iObject;


   /**
    * Creates a new {@linkplain TestDataBuilderStub} object.
    *
    * @param aObject
    *           the object
    */
   public TestDataBuilderStub(T aObject) {
      iObject = aObject;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public T build() {
      return iObject;
   }
}
