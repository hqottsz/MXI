package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Optional;


public class Shift {

   private String code;
   private String name;
   private BigDecimal startHour;
   private BigDecimal duration;
   private BigDecimal workHours;


   public String getCode() {
      return code;
   }


   public void setCode( String code ) {
      this.code = code;
   }


   public String getName() {
      return name;
   }


   public void setName( String name ) {
      this.name = name;
   }


   public Optional<BigDecimal> getStartHour() {
      return Optional.ofNullable( startHour );
   }


   public void setStartHour( BigDecimal startHour ) {
      this.startHour = startHour;
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
