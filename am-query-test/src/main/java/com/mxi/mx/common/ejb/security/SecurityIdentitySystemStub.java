
package com.mxi.mx.common.ejb.security;

/**
 * But it's used to help bypass the validation downstream for the SecurityIdentity. THe previous
 * stub (SecurityIdentityStub) used userid 9999999, which is not a valid user. This stub used in
 * association with the EJBFactorySystemStub will ensure the organization validation on taskdefn's
 * (And soon to be other parts of maintenix) passes within serverless tests such as query tests.
 *
 * @author tony
 */
public class SecurityIdentitySystemStub extends SecurityIdentityStub {

   /*
    * Returns the system user id - this user id ensures downstream user organization validation is
    * bypassed.
    */
   @Override
   public Integer getCurrentUserId() {
      return 11;
   }
}
