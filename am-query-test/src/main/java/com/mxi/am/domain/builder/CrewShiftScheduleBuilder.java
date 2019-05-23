package com.mxi.am.domain.builder;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.mxi.mx.common.tuple.Pair;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.org.OrgCrewShiftPlan;
import com.mxi.mx.core.table.org.OrgCrewShiftTask;


/**
 * Builds a <code>org_crew_shift_plan</code> object
 */
public class CrewShiftScheduleBuilder extends ShiftScheduleBuilder {

   private DepartmentKey iCrewKey;

   private Pair<TaskKey, Date> iTaskAssignedToShift;


   /**
    * {@inheritDoc}
    */
   @Override
   public ShiftKey build() {

      Calendar lCal = Calendar.getInstance();

      if ( iShift == null ) {
         iShift = new ShiftDomainBuilder().withStartHour( iStartHour )
               .withDurationInHours( iDuration ).withWorkHours( iWorkHours ).build();
      }

      for ( Date lDay = new Date( iStartDay.getTime() ); lDay.compareTo( iEndDay ) <= 0; lDay
            .setTime( lDay.getTime() + ONE_DAY ) ) {

         lCal.setTime( lDay );
         lCal.set( Calendar.HOUR_OF_DAY, 0 );

         OrgCrewShiftPlan lCrewShift = OrgCrewShiftPlan.create( iCrewKey );
         lCrewShift.setDayDt( lDay );

         if ( iDoWOff.contains( lCal.get( Calendar.DAY_OF_WEEK ) ) ) {
            lCrewShift.setStartHour( 0.0 );
            lCrewShift.setDurationQt( 0.0 );
            lCrewShift.setWorkHoursQt( 0.0 );
         } else {
            lCrewShift.setStartHour( iStartHour );
            lCrewShift.setDurationQt( iDuration );
            lCrewShift.setShift( iShift );
            lCrewShift.setWorkHoursQt( iWorkHours );
         }

         lCrewShift.insert();

         // Assign the task to the crew shift
         if ( iTaskAssignedToShift != null
               && DateUtils.isSameDay( iTaskAssignedToShift.getComponent2(), lDay ) ) {
            OrgCrewShiftTask lOrgCrewShiftTask = OrgCrewShiftTask
                  .create( iTaskAssignedToShift.getComponent1(), lCrewShift.getPk() );
            lOrgCrewShiftTask.insert();
         }
      }

      return iShift;
   }


   @Override
   public CrewShiftScheduleBuilder withCrew( DepartmentKey aCrewKey ) {

      iCrewKey = aCrewKey;

      return this;
   }


   public CrewShiftScheduleBuilder withTask( TaskKey aTaskKey, Date aDate ) {

      iTaskAssignedToShift = new Pair<TaskKey, Date>( aTaskKey, aDate );

      return this;
   }
}
