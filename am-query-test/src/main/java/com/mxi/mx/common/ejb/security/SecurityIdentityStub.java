
package com.mxi.mx.common.ejb.security;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;


/**
 * This stub replaces the security identity interface.
 *
 * @author asmolko
 */
public class SecurityIdentityStub implements SecurityIdentityLocal {

   private final int iUserId;
   private final String iUsername;


   /**
    * Creates a new {@linkplain SecurityIdentityStub} object.
    */
   public SecurityIdentityStub() {
      this( "currentuser", 9999999 );
   }


   /**
    * Creates a new {@linkplain SecurityIdentityStub} object.
    *
    * @param aUsername
    *           The username
    * @param aUserId
    *           The user id
    */
   public SecurityIdentityStub(String aUsername, int aUserId) {
      iUsername = aUsername;
      iUserId = aUserId;
   }


   @Override
   public String getCurrentUser() {
      return iUsername;
   }


   @Override
   public Integer getCurrentUserId() {
      return iUserId;
   }


   @Override
   public EJBLocalHome getEJBLocalHome() throws EJBException {
      throw new UnsupportedOperationException();
   }


   @Override
   public Object getPrimaryKey() throws EJBException {
      throw new UnsupportedOperationException();
   }


   @Override
   public boolean isIdentical( EJBLocalObject aObj ) throws EJBException {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void remove() throws RemoveException, EJBException {
      throw new UnsupportedOperationException();
   }
}
