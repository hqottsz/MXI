package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.am.domain.CrewShiftDay;
import com.mxi.am.domain.Domain;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.OrgCrewShiftPlanKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.table.org.OrgCrewShiftPlan;


public class CrewShiftDayBuilder {

   public static OrgCrewShiftPlanKey build( CrewShiftDay crewShiftDay ) {

      DepartmentKey crew = crewShiftDay.getCrew().orElse( Domain.createCrew() );
      ShiftKey shift = crewShiftDay.getShift().orElse( Domain.createShift() );
      Date day = crewShiftDay.getDay().orElse( new Date() );

      OrgCrewShiftPlan row = OrgCrewShiftPlan.create( crew );
      row.setShift( shift );
      row.setDayDt( day );
      crewShiftDay.getStartTime()
            .ifPresent( startTime -> row.setDurationQt( startTime.doubleValue() ) );
      crewShiftDay.getDuration()
            .ifPresent( duration -> row.setDurationQt( duration.doubleValue() ) );
      crewShiftDay.getWorkHours()
            .ifPresent( workHours -> row.setWorkHoursQt( workHours.doubleValue() ) );
      OrgCrewShiftPlanKey key = row.insert();

      return key;
   }

}
