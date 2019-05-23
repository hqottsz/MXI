package com.mxi.am.domain;

import com.mxi.mx.core.key.LocationPrinterKey;
import com.mxi.mx.core.key.RefJobTypeKey;


public class LocationPrinterJob {

   private LocationPrinterKey printer;
   private RefJobTypeKey jobType;
   private boolean isDefault;


   /**
    * Returns the value of the printer property.
    *
    * @return the value of the printer property
    */
   public LocationPrinterKey getPrinter() {
      return printer;
   }


   /**
    * Sets a new value for the printer property.
    *
    * @param printer
    *           the new value for the printer property
    */
   public void setPrinter( LocationPrinterKey printer ) {
      this.printer = printer;
   }


   /**
    * Returns the value of the jobType property.
    *
    * @return the value of the jobType property
    */
   public RefJobTypeKey getJobType() {
      return jobType;
   }


   /**
    * Sets a new value for the jobType property.
    *
    * @param jobType
    *           the new value for the jobType property
    */
   public void setJobType( RefJobTypeKey jobType ) {
      this.jobType = jobType;
   }


   /**
    * Returns the value of the isDefault property.
    *
    * @return the value of the isDefault property
    */
   public boolean isDefault() {
      return isDefault;
   }


   /**
    * Sets a new value for the isDefault property.
    *
    * @param isDefault
    *           the new value for the isDefault property
    */
   public void setDefault( boolean isDefault ) {
      this.isDefault = isDefault;
   }

}
