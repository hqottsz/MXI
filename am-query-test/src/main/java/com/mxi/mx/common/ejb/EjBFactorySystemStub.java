
package com.mxi.mx.common.ejb;

import com.mxi.mx.common.ejb.security.SecurityIdentityLocal;
import com.mxi.mx.common.ejb.security.SecurityIdentitySystemStub;


/**
 * I know the name of this class kinda sucks. But it's used to help bypass the validation downstream
 * for the SecurityIdentity. The previous stub (EJBFactoryStub) used the SecurityIdentityStub which
 * used userid 9999999, which is not a valid user. This stub used in will ensure the organization
 * validation on taskdefn's (And soon to be other parts of maintenix) passes within serverless tests
 * such as query tests.
 *
 * @author tony
 */
public class EjBFactorySystemStub extends EjbFactoryStub {

   /**
    * Return the security identity (but in this case the system user) so that organization checks in
    * task defn (and potentially other sections of maintenix) do not fail validation
    *
    * @return the security identity stub with a default of system user id 11
    */
   @Override
   public SecurityIdentityLocal createSecurityIdentity() {
      return new SecurityIdentitySystemStub();
   }
}
