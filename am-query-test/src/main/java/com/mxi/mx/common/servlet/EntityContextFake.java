package com.mxi.mx.common.servlet;

import java.security.Identity;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EntityContext;
import javax.ejb.TimerService;
import javax.transaction.UserTransaction;


/**
 * A entity context stub injected into unit test to prevent it throwing null pointer exception
 *
 */
public class EntityContextFake implements EntityContext {

   private Object iPrimaryKey;


   /**
    * {@inheritDoc}
    */
   @Override
   public Identity getCallerIdentity() {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Principal getCallerPrincipal() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, Object> getContextData() {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EJBHome getEJBHome() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EJBLocalHome getEJBLocalHome() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Properties getEnvironment() {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean getRollbackOnly() throws IllegalStateException {
      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public TimerService getTimerService() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public UserTransaction getUserTransaction() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isCallerInRole( Identity aArg0 ) {
      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isCallerInRole( String aArg0 ) throws IllegalStateException {
      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Object lookup( String aArg0 ) throws IllegalArgumentException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setRollbackOnly() throws IllegalStateException {

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EJBObject getEJBObject() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Object getPrimaryKey() throws IllegalStateException {
      return iPrimaryKey;
   }


   public void setPrimaryKey( Object aPrimaryKey ) {
      iPrimaryKey = aPrimaryKey;
   }
}
