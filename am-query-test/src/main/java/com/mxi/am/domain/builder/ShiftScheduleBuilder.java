package com.mxi.am.domain.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.ShiftKey;


/**
 * Builds a <code>shift_shift</code> object
 */
public abstract class ShiftScheduleBuilder {

   public static int ONE_DAY = 24 * 60 * 60 * 1000;

   protected Double iDuration;

   protected Date iStartDay = new Date();

   protected Date iEndDay = new Date();

   protected Double iStartHour;

   protected Double iWorkHours;

   protected List<Integer> iDoWOff = new ArrayList<>();

   protected ShiftKey iShift;

   protected HumanResourceKey iHrKey;

   protected DepartmentKey iCrewKey;


   /**
    * {@inheritDoc}
    */
   public abstract ShiftKey build();


   public ShiftScheduleBuilder forDateRange( Date aStartDate, Date aEndDate ) {

      iStartDay = aStartDate;
      iEndDay = aEndDate;

      return this;
   }


   public ShiftScheduleBuilder forUser( HumanResourceKey aHrKey ) {
      iHrKey = aHrKey;
      return this;
   }


   public ShiftScheduleBuilder withDayOff( Integer aDay ) {

      iDoWOff.add( aDay );

      return this;
   }


   /**
    * Sets the duration of the shift in hours.
    *
    * @param aDuration
    *
    * @return The builder.
    */
   public ShiftScheduleBuilder withDurationInHours( Double aDuration ) {

      iDuration = aDuration;

      return this;
   }


   /**
    * Sets the start hour of the shift.
    *
    * @param aStartHour
    *
    * @return The builder.
    */
   public ShiftScheduleBuilder withStartHour( Double aStartHour ) {

      iStartHour = aStartHour;

      return this;
   }


   /**
    * Sets the number of work hours of the shift.
    *
    * @param aWorkHours
    *
    * @return The builder.
    */
   public ShiftScheduleBuilder withWorkHours( Double aWorkHours ) {

      iWorkHours = aWorkHours;

      return this;
   }


   /**
    * DOCUMENT_ME
    *
    * @param aShift
    * @return
    */
   public ShiftScheduleBuilder withShift( ShiftKey aShift ) {

      iShift = aShift;

      return this;
   }


   /**
    * Sets the crew of the shift
    *
    * @param aOkCrewCode
    * @return
    */
   public ShiftScheduleBuilder withCrew( DepartmentKey aCrewKey ) {
      iCrewKey = aCrewKey;
      return this;
   }
}
