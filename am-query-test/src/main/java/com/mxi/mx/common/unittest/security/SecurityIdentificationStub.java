
package com.mxi.mx.common.unittest.security;

import com.mxi.mx.common.security.SecurityIdentificationInterface;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.utl.UtlUser;


/**
 * Replaces the security identification stub
 */
public class SecurityIdentificationStub implements SecurityIdentificationInterface {

   private final UserKey iUserKey;
   private final String iUserName;


   /**
    * Creates a new {@linkplain SecurityIdentificationStub} object.
    *
    * @param aHumanResource
    *           the current human resource
    */
   public SecurityIdentificationStub(HumanResourceKey aHumanResource) {
      UtlUser lUserTable = UtlUser.findByForeignKey( aHumanResource );
      iUserKey = lUserTable.getPk();
      iUserName = lUserTable.getUsername();
   }


   /**
    * Creates a new {@linkplain SecurityIdentificationStub} object.
    *
    * @param aUserKey
    *           the current user key
    * @param aUserName
    *           the current user name
    */
   public SecurityIdentificationStub(UserKey aUserKey, String aUserName) {
      iUserKey = aUserKey;
      iUserName = aUserName;
   }


   @Override
   public String getCurrentUser() {
      return iUserName;
   }


   @Override
   public Integer getCurrentUserId() {
      return iUserKey.getId();
   }
}
