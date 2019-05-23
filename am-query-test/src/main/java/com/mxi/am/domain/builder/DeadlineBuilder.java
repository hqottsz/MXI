package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.DataTypeKey.CDY;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.am.domain.Deadline;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.task.TaskTaskTable;


public class DeadlineBuilder {

   static SchedStaskDao iSchedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );


   public static EventDeadlineKey build( Deadline aDeadline ) {

      // The usage type is mandatory so if not provided then use CDY as a default.
      DataTypeKey lUsageType = aDeadline.getUsageType().orElse( CDY );

      EvtSchedDeadTable lEvtSchedDead = EvtSchedDeadTable.create( aDeadline.getTask(), lUsageType );

      lEvtSchedDead.setScheduledFrom( aDeadline.getScheduledFrom() );
      lEvtSchedDead.setDeadlineDate( aDeadline.getDueDate() );
      lEvtSchedDead.setDriver( aDeadline.isDriving().orElse( true ) );

      // If start date is not provided then check if scheduled from an effective date.
      // And if so, then use the effective date as the start date.
      lEvtSchedDead.setStartDate(
            aDeadline.getStartDate().orElse( ( isScheduledFromEffectiveDate( aDeadline ) )
                  ? getEffectiveDate( aDeadline ) : null ) );

      // Deviation is needed to be set (cannot be null) for logic that calculates deadlines.
      BigDecimal lDeviation = aDeadline.getDeviation().orElse( BigDecimal.ZERO );
      lEvtSchedDead.setDeviationQt( lDeviation.doubleValue() );

      // We cannot default these to zero as that has an effect on the deadline.
      // So only set these values if provided.
      if ( aDeadline.getStartTsn() != null ) {
         lEvtSchedDead.setStartQt( aDeadline.getStartTsn().doubleValue() );
      }
      if ( aDeadline.getInterval() != null ) {
         lEvtSchedDead.setIntervalQt( aDeadline.getInterval().doubleValue() );
      }
      if ( aDeadline.getDueValue() != null ) {
         lEvtSchedDead.setDeadlineQt( aDeadline.getDueValue().doubleValue() );
      }
      if ( aDeadline.getUsageRemaining() != null ) {
         lEvtSchedDead.setUsageRemaining( aDeadline.getUsageRemaining().doubleValue() );
      }
      if ( aDeadline.getScheduleToPlanLow() != null ) {
         lEvtSchedDead.setPrefixedQt( aDeadline.getScheduleToPlanLow().doubleValue() );
      }
      if ( aDeadline.getScheduleToPlanHigh() != null ) {
         lEvtSchedDead.setPostfixedQt( aDeadline.getScheduleToPlanHigh().doubleValue() );
      }

      EventDeadlineKey lDeadline = lEvtSchedDead.insert();

      return lDeadline;
   }


   private static Date getEffectiveDate( Deadline aDeadline ) {
      Date lStartDate = null;
      TaskTaskKey lTaskDefinitionKey =
            iSchedStaskDao.findByPrimaryKey( aDeadline.getTask() ).getTaskTaskKey();
      if ( lTaskDefinitionKey != null ) {
         lStartDate = TaskTaskTable.findByPrimaryKey( lTaskDefinitionKey ).getEffectiveDt();
      }
      return lStartDate;
   }


   private static boolean isScheduledFromEffectiveDate( Deadline aDeadline ) {
      return RefSchedFromKey.EFFECTIV.equals( aDeadline.getScheduledFrom() );
   }
}
