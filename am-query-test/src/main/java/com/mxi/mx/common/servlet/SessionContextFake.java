package com.mxi.mx.common.servlet;

import java.security.Identity;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionContext;
import javax.ejb.TimerService;
import javax.transaction.UserTransaction;
import javax.xml.rpc.handler.MessageContext;


@SuppressWarnings( "deprecation" )
public class SessionContextFake implements SessionContext {

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
   public Principal getCallerPrincipal() {
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
   public EJBHome getEJBHome() {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EJBLocalHome getEJBLocalHome() {
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
   public boolean isCallerInRole( String aArg0 ) {
      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Object lookup( String aArg0 ) {
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
   public <T> T getBusinessObject( Class<T> aArg0 ) throws IllegalStateException {
      return null;
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
   public Class getInvokedBusinessInterface() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public MessageContext getMessageContext() throws IllegalStateException {
      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean wasCancelCalled() throws IllegalStateException {
      return false;
   }

}
