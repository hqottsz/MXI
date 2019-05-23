
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.table.shift.ShiftShiftTable;


/**
 * Builds a <code>shift_shift</code> object
 */
public class ShiftDomainBuilder implements DomainBuilder<ShiftKey> {

   private Double iDuration;

   private Double iStartHour;
   private Double iWorkHours;

   private String iShiftCd;


   /**
    * {@inheritDoc}
    */
   @Override
   public ShiftKey build() {

      ShiftShiftTable lShiftShiftTable = ShiftShiftTable.create();
      lShiftShiftTable.setStartHour( iStartHour );
      lShiftShiftTable.setDurationQt( iDuration );
      lShiftShiftTable.setWorkHoursQt( iWorkHours );
      lShiftShiftTable.setShiftCd( iShiftCd );
      lShiftShiftTable.insert();

      return lShiftShiftTable.getPk();
   }


   /**
    * Sets the duration of the shift in hours.
    *
    * @param aDuration
    *
    * @return The builder.
    */
   public ShiftDomainBuilder withDurationInHours( Double aDuration ) {
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
   public ShiftDomainBuilder withStartHour( Double aStartHour ) {
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
   public ShiftDomainBuilder withWorkHours( Double aWorkHours ) {
      iWorkHours = aWorkHours;

      return this;
   }


   public ShiftDomainBuilder withShiftCd( String aShiftCd ) {
      iShiftCd = aShiftCd;
      return this;
   }
}
