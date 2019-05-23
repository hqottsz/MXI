
package com.mxi.mx.core.unittest.authority;

import com.mxi.mx.core.key.AuthorityKey;


/**
 * This class is used to store key values that are needed by the Authority JUnit tests.
 *
 * @author amccormack
 * @created Nov 15, 2004
 */
public class AUTestData {

   /**
    * Returns a AuthorityKey with assigned inventory
    *
    * @return the value AuthorityKey
    */
   public static AuthorityKey getAuthorityWithInventory() {

      return new AuthorityKey( "5000000:1" );
   }

}
