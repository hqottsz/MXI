package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.ShiftKey;


/**
 *
 * A "crew shift day" is the association between a crew and a shift for a particular day.
 *
 */
public class CrewShiftDay {

   private DepartmentKey crew;
   private ShiftKey shift;
   private Date day;
   private BigDecimal startTime;
   private BigDecimal duration;
   private BigDecimal workHours;


   public Optional<DepartmentKey> getCrew() {
      return Optional.ofNullable( crew );
   }


   public void setCrew( DepartmentKey crew ) {
      this.crew = crew;
   }


   public Optional<ShiftKey> getShift() {
      return Optional.ofNullable( shift );
   }


   public void setShift( ShiftKey shift ) {
      this.shift = shift;
   }


   public Optional<Date> getDay() {
      return Optional.ofNullable( day );
   }


   public void setDay( Date day ) {
      this.day = day;
   }


   public Optional<BigDecimal> getStartTime() {
      return Optional.ofNullable( startTime );
   }


   public void setStartTime( BigDecimal startTime ) {
      this.startTime = startTime;
   }


   public Optional<BigDecimal> getDuration() {
      return Optional.ofNullable( duration );
   }


   public void setDuration( BigDecimal duration ) {
      this.duration = duration;
   }


   public Optional<BigDecimal> getWorkHours() {
      return Optional.ofNullable( workHours );
   }


   public void setWorkHours( BigDecimal workHours ) {
      this.workHours = workHours;
   }

}
