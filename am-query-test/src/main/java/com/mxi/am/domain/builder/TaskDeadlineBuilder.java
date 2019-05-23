
package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;


/**
 * Builds an <code>evt_sched_dead</code> object.
 */
public class TaskDeadlineBuilder implements DomainBuilder<EventDeadlineKey> {

   private DataTypeKey iDataType = DataTypeKey.HOURS;
   private Double iDeviation = 0.0d;
   private Date iDueDate;
   private Double iDueQuantity = 1.0d;
   private Double iInterval = 1.0d;
   private boolean iIsDrivingDeadline = true;
   private Double iNotifyQuantity = 0.0d;
   private Double iPostfixQuantity = 0.0d;
   private Double iPrefixQuantity = 0.0d;
   private RefSchedFromKey iSchedFrom = RefSchedFromKey.BIRTH;
   private Date iStartDate;
   private Double iStartQuantity = 0.0d;
   private TaskKey iTask;


   /**
    * Creates a new {@linkplain TaskDeadlineBuilder} object.
    *
    * @param aTaskKey
    */
   public TaskDeadlineBuilder(TaskKey aTaskKey) {
      this();
      withTask( aTaskKey );
   }


   /**
    * Creates a new {@linkplain TaskDeadlineBuilder} object.
    */
   public TaskDeadlineBuilder() {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EventDeadlineKey build() {

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.create( iTask, iDataType );

      lEvtSchedDead.setDriver( iIsDrivingDeadline );
      lEvtSchedDead.setDeviationQt( iDeviation );
      lEvtSchedDead.setNotifyQt( iNotifyQuantity );
      lEvtSchedDead.setIntervalQt( iInterval );
      lEvtSchedDead.setPrefixedQt( iPrefixQuantity );
      lEvtSchedDead.setPostfixedQt( iPostfixQuantity );
      lEvtSchedDead.setDeadlineDate( iDueDate );
      lEvtSchedDead.setDeadlineQt( iDueQuantity );
      lEvtSchedDead.setStartDate( iStartDate );
      lEvtSchedDead.setScheduledFrom( iSchedFrom );
      lEvtSchedDead.setStartQt( iStartQuantity );

      lEvtSchedDead.insert();

      return lEvtSchedDead.getPk();
   }


   /**
    * Indicates that this is the driving deadline.
    *
    * @return the builder
    */
   public TaskDeadlineBuilder isDrivingDeadline() {
      iIsDrivingDeadline = true;

      return this;
   }


   /**
    * Indicates that this is not the driving deadline.
    *
    * @return the builder
    */
   public TaskDeadlineBuilder isNotDrivingDeadline() {
      iIsDrivingDeadline = false;

      return this;
   }


   /**
    * Sets the schduel from value.
    *
    * @param aSchedFrom
    *
    * @return the builder
    */
   public TaskDeadlineBuilder scheduledFrom( RefSchedFromKey aSchedFrom ) {
      iSchedFrom = aSchedFrom;

      return this;
   }


   /**
    * Sets the data type.
    *
    * @param aDataType
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withDataType( DataTypeKey aDataType ) {
      iDataType = aDataType;

      return this;
   }


   /**
    * Sets the deviation.
    *
    * @param aDeviation
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withDeviation( Double aDeviation ) {
      iDeviation = aDeviation;

      return this;
   }


   /**
    * Sets the due date.
    *
    * @param aDueDate
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withDueDate( Date aDueDate ) {
      iDueDate = aDueDate;

      return this;
   }


   /**
    * Sets the due quantity.
    *
    * @param aDueQuantity
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withDueQuantity( Double aDueQuantity ) {
      iDueQuantity = aDueQuantity;

      return this;
   }


   /**
    * Sets the interval.
    *
    * @param aInterval
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withInterval( double aInterval ) {
      iInterval = aInterval;

      return this;
   }


   /**
    * Sets the notify quantity.
    *
    * @param aNotifyQuantity
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withNotifyQuantity( Double aNotifyQuantity ) {
      iNotifyQuantity = aNotifyQuantity;

      return this;
   }


   /**
    * Sets the postfix quantity.
    *
    * @param aPostfixQuantity
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withPostfixQuantity( Double aPostfixQuantity ) {
      iPostfixQuantity = aPostfixQuantity;

      return this;
   }


   /**
    * Sets the prefix quantity.
    *
    * @param aPrefixQuantity
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withPrefixQuantity( Double aPrefixQuantity ) {
      iPrefixQuantity = aPrefixQuantity;

      return this;
   }


   /**
    * Sets the start date.
    *
    * @param aStartDate
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withStartDate( Date aStartDate ) {
      iStartDate = aStartDate;

      return this;
   }


   /**
    * Sets the start quantity.
    *
    * @param aStartQuantity
    *
    * @return the builder
    */
   public TaskDeadlineBuilder withStartQuantity( Double aStartQuantity ) {
      iStartQuantity = aStartQuantity;

      return this;
   }


   public TaskDeadlineBuilder withTask( TaskKey aTask ) {
      iTask = aTask;

      return this;
   }
}
