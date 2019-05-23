package com.mxi.am.domain.builder;

import com.mxi.am.domain.Shift;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.table.shift.ShiftShiftTable;


public class ShiftBuilder {

   public static ShiftKey build( Shift shift ) {

      ShiftShiftTable row = ShiftShiftTable.create();
      row.setShiftCd( shift.getCode() );
      row.setShiftName( shift.getName() );
      shift.getStartHour().ifPresent( startHour -> row.setStartHour( startHour.doubleValue() ) );
      shift.getDuration().ifPresent( duration -> row.setDurationQt( duration.doubleValue() ) );
      shift.getWorkHours().ifPresent( workHours -> row.setWorkHoursQt( workHours.doubleValue() ) );
      ShiftKey key = row.insert();

      return key;
   }

}
