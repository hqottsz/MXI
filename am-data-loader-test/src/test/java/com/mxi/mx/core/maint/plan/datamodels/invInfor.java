package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data of inventory information
 *
 */
public class invInfor {

   simpleIDs iINVIDs;
   String iSerial_no_oem;


   public invInfor() {
      iINVIDs = null;
      iSerial_no_oem = null;

   }


   public invInfor(simpleIDs iINVIDs, String iSerial_no_oem) {
      this.iINVIDs = iINVIDs;
      this.iSerial_no_oem = iSerial_no_oem;
   }


   /**
    * Returns the value of the iINVIDs property.
    *
    * @return the value of the iINVIDs property
    */
   public simpleIDs getiINVIDs() {
      return iINVIDs;
   }


   /**
    * Sets a new value for the iINVIDs property.
    *
    * @param iINVIDs
    *           the new value for the iINVIDs property
    */
   public void setiINVIDs( simpleIDs iINVIDs ) {
      this.iINVIDs = iINVIDs;
   }


   /**
    * Returns the value of the iSerial_no_oem property.
    *
    * @return the value of the iSerial_no_oem property
    */
   public String getiSerial_no_oem() {
      return iSerial_no_oem;
   }


   /**
    * Sets a new value for the iSerial_no_oem property.
    *
    * @param iSerial_no_oem
    *           the new value for the iSerial_no_oem property
    */
   public void setiSerial_no_oem( String iSerial_no_oem ) {
      this.iSerial_no_oem = iSerial_no_oem;
   }

}
