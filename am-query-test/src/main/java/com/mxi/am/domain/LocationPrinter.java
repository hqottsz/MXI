package com.mxi.am.domain;

import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefPrinterTypeKey;


public class LocationPrinter {

   private LocationKey location;
   private String printerParm;
   private String printerSdesc;
   private RefPrinterTypeKey printerType;


   public LocationPrinter() {

   }


   /**
    * Returns the value of the location property.
    *
    * @return the value of the location property
    */
   public LocationKey getLocation() {
      return location;
   }


   /**
    * Sets a new value for the location property.
    *
    * @param location
    *           the new value for the location property
    */
   public void setLocation( LocationKey location ) {
      this.location = location;
   }


   /**
    * Returns the value of the printerParm property.
    *
    * @return the value of the printerParm property
    */
   public String getPrinterParm() {
      return printerParm;
   }


   /**
    * Sets a new value for the printerParm property.
    *
    * @param printerParm
    *           the new value for the printerParm property
    */
   public void setPrinterParm( String printerParm ) {
      this.printerParm = printerParm;
   }


   /**
    * Returns the value of the printerSdesc property.
    *
    * @return the value of the printerSdesc property
    */
   public String getPrinterSdesc() {
      return printerSdesc;
   }


   /**
    * Sets a new value for the printerSdesc property.
    *
    * @param printerSdesc
    *           the new value for the printerSdesc property
    */
   public void setPrinterSdesc( String printerSdesc ) {
      this.printerSdesc = printerSdesc;
   }


   /**
    * Returns the value of the printerType property.
    *
    * @return the value of the printerType property
    */
   public RefPrinterTypeKey getPrinterType() {
      return printerType;
   }


   /**
    * Sets a new value for the printerType property.
    *
    * @param printerType
    *           the new value for the printerType property
    */
   public void setPrinterType( RefPrinterTypeKey printerType ) {
      this.printerType = printerType;
   }

}
