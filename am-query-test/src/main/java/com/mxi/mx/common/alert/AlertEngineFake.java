package com.mxi.mx.common.alert;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.exception.ValueTooLargeException;
import com.mxi.mx.common.key.AlertKey;
import com.mxi.mx.common.key.AlertTypeKey;
import com.mxi.mx.common.services.alert.AlertTypeNotSupportedException;
import com.mxi.mx.common.services.alert.InvalidAlertException;
import com.mxi.mx.common.utils.ArrayUtils;


/**
 * Fake for {@linkplain AlertEngine}.
 *
 *
 */
public class AlertEngineFake implements AlertEngine {

   private Set<Alert> iSentAlerts = new TreeSet<Alert>();


   public Set<Alert> getSentAlerts() {
      return iSentAlerts;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void acknowledgeAlerts( int aUser, AlertKey... aAlerts ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void addNoteAlert( int aUserId, String aUserNote, AlertKey aAlert ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void archiveAlerts( int aUserId, AlertKey... aAlerts ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void assignAlerts( int aUser, AlertKey... aAlerts ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteAlerts( int aUser, AlertKey... aAlert ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void dispositionAlerts( int aUser, String aUserNote, AlertKey... aAlerts ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Alert getAlert( AlertKey aAlert ) throws InvalidAlertException {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<Alert> getAlerts( AlertKey... aAlert ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertAssignment> getAssignedAlerts( int aUser ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertAssignment> getGroupAlerts( int aUser ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertAssignment> getUnAssignedAlerts( int aUser ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<AlertAssignment> getUserAlerts( int aUser ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void refreshCache() {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<Alert> sendAlerts( Alert... aAlerts ) throws AlertTypeNotSupportedException {
      iSentAlerts = ArrayUtils.asSet( aAlerts );
      return iSentAlerts;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Set<Alert> sendAlertsIsolated( Alert... aAlerts ) throws AlertTypeNotSupportedException {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setActive( boolean aActive, AlertTypeKey... aAlertTypes ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setAlertTypePriority( int aPriority, AlertTypeKey... aAlertTypes )
         throws NegativeValueException, ValueTooLargeException {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setAlertTypeRoles( Map<AlertTypeKey, Set<Integer>> aMap ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void unAssignAlerts( int aUserId, AlertKey... aAlerts ) {
      throw new UnsupportedOperationException();
   }

}
