package com.mxi.am.domain.builder;

import java.util.Calendar;
import java.util.Date;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.table.org.OrgHrShiftPlan;


/**
 * Builds a <code>org_hr_shift_plan</code> object
 */
public class HrShiftScheduleBuilder extends ShiftScheduleBuilder {

   private HumanResourceKey iHrKey;
   private LocationKey iLocationKey;


   /**
    * {@inheritDoc}
    */
   @Override
   public ShiftKey build() {

      Calendar iCal = Calendar.getInstance();

      if ( iShift == null ) {
         iShift = new ShiftDomainBuilder().withStartHour( iStartHour )
               .withDurationInHours( iDuration ).withWorkHours( iWorkHours ).build();
      }

      for ( Date lDay = new Date( iStartDay.getTime() ); lDay.compareTo( iEndDay ) <= 0; lDay
            .setTime( lDay.getTime() + ONE_DAY ) ) {
         iCal.setTime( lDay );
         iCal.set( Calendar.HOUR_OF_DAY, 0 );

         OrgHrShiftPlan lHrShift = OrgHrShiftPlan.create( iHrKey );
         lHrShift.setDepartment( iCrewKey );
         lHrShift.setLocation( iLocationKey );

         lHrShift.setDayDt( lDay );
         if ( iDoWOff.contains( iCal.get( Calendar.DAY_OF_WEEK ) ) ) {
            lHrShift.setStartHour( 0.0 );
            lHrShift.setDurationQt( 0.0 );
            lHrShift.setWorkHoursQt( 0.0 );
         } else {
            lHrShift.setStartHour( iStartHour );
            lHrShift.setDurationQt( iDuration );
            lHrShift.setShift( iShift );
            lHrShift.setWorkHoursQt( iWorkHours );
         }
         lHrShift.insert();
      }

      return iShift;
   }


   public HrShiftScheduleBuilder withHr( HumanResourceKey aHrKey ) {

      iHrKey = aHrKey;

      return this;
   }


   public HrShiftScheduleBuilder withLocation( LocationKey aLocationKey ) {

      iLocationKey = aLocationKey;

      return this;
   }
}
