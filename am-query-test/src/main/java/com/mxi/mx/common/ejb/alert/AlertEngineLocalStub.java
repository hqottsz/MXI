
package com.mxi.mx.common.ejb.alert;

import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;

import com.mxi.mx.common.alert.Alert;
import com.mxi.mx.common.alert.AlertAssignment;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.key.AlertKey;
import com.mxi.mx.common.key.AlertTypeKey;


/**
 * Stub to substitute {@link AlertEngineLocal}
 */
public class AlertEngineLocalStub implements AlertEngineLocal {

   /**
    * {@inheritDoc}
    */
   @Override
   public void acknowledgeAlerts( int aUserId, Set<AlertKey> aAlerts ) throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void addNoteToAlert( int aUserId, String aUserNote, AlertKey aAlert ) throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void archiveAlerts( int aUserId, Set<AlertKey> aAlerts ) throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void assignAlerts( int aUserId, Set<AlertKey> aAlerts ) throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteAlerts( int aUser, Set<AlertKey> aAlerts ) throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void dispositionAlerts( int aUserId, String aUserNote, Set<AlertKey> aAlerts )
         throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Alert getAlert( AlertKey aAlert ) throws MxException {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<Alert> getAlerts( Set<AlertKey> aAlerts ) {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertAssignment> getAssignedAlerts( int aUserId ) {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EJBLocalHome getEJBLocalHome() throws EJBException {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertAssignment> getGroupAlerts( int aUserId ) {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Object getPrimaryKey() throws EJBException {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertAssignment> getUnAssignedAlerts( int aUserId ) {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertAssignment> getUserAlerts( int aUserId ) {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isIdentical( EJBLocalObject aArg0 ) throws EJBException {

      return false;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void refreshCache() {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void remove() throws RemoveException, EJBException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<Alert> sendAlerts( Set<Alert> aAlert ) throws MxException {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<Alert> sendAlertsIsolated( Set<Alert> aAlerts ) throws MxException {

      return null;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setActive( boolean aActive, Set<AlertTypeKey> aAlertType ) throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setAlertTypePriority( int aPriority, Set<AlertTypeKey> aAlertType )
         throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setAlertTypeRoles( Map<AlertTypeKey, Set<Integer>> aMap ) throws MxException {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void unAssignAlerts( int aUser, Set<AlertKey> aAlerts ) throws MxException {
   }
}
