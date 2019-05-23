
package com.mxi.mx.common.key;

/**
 * Stub class used for testing
 *
 * @author asmolko
 */
public class PrimaryKeyServiceStub implements DatabaseIdAccessor {

   private long iId = 1234;


   /**
    * Creates a new PrimaryKeyServiceStub object.
    */
   public PrimaryKeyServiceStub() {
   }


   /**
    * Creates a new PrimaryKeyServiceStub object.
    *
    * @param aId
    *           DOCUMENT_ME
    */
   public PrimaryKeyServiceStub(long aId) {
      iId = aId;
   }


   @Override
   public long getDatabaseId() {
      return iId;
   }
}
