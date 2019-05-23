package com.mxi.am.domain;

import com.mxi.mx.core.key.OrgKey;


public class Operator {

   private String iCarrierCode;
   private String iIATACode;
   private String iICAOCode;
   private OrgKey iOrgKey;


   /**
    * Returns the value of the carrierCode property.
    *
    * @return the value of the carrierCode property
    */
   public String getCarrierCode() {
      return iCarrierCode;
   }


   /**
    * Sets a new value for the carrierCode property.
    *
    * @param aCarrierCode
    *           the new value for the carrierCode property
    */
   public void setCarrierCode( String aCarrierCode ) {
      iCarrierCode = aCarrierCode;
   }


   /**
    * Returns the value of the iATACode property.
    *
    * @return the value of the iATACode property
    */
   public String getIATACode() {
      return iIATACode;
   }


   /**
    * Sets a new value for the iATACode property.
    *
    * @param aIATACode
    *           the new value for the iATACode property
    */
   public void setIATACode( String aIATACode ) {
      iIATACode = aIATACode;
   }


   /**
    * Returns the value of the iCAOCode property.
    *
    * @return the value of the iCAOCode property
    */
   public String getICAOCode() {
      return iICAOCode;
   }


   /**
    * Sets a new value for the iCAOCode property.
    *
    * @param aICAOCode
    *           the new value for the iCAOCode property
    */
   public void setICAOCode( String aICAOCode ) {
      iICAOCode = aICAOCode;
   }


   /**
    * Returns the value of the orgKey property.
    *
    * @return the value of the orgKey property
    */
   public OrgKey getOrgKey() {
      return iOrgKey;
   }


   /**
    * Sets a new value for the orgKey property.
    *
    * @param aOrgKey
    *           the new value for the orgKey property
    */
   public void setOrgKey( OrgKey aOrgKey ) {
      iOrgKey = aOrgKey;
   }

}
