package com.mxi.am.stepdefn.persona.lineplanner;

import java.math.BigDecimal;


public class ReservationLine {

   private String iExternalID;
   private String iPartNumber;
   private String iManufacturer;
   private String iSerialNumber;
   private BigDecimal iPartQuantity;
   private String iStatus;
   private String iETA;


   public ReservationLine(String aExternalID, String aPartNumber, String aManufacturer,
         String aSerialNumber, BigDecimal aQuantity, String aStatus, String aETA) {
      iExternalID = aExternalID;
      iPartNumber = aPartNumber;
      iManufacturer = aManufacturer;
      iSerialNumber = aSerialNumber;
      iPartQuantity = aQuantity;
      iStatus = aStatus;
      iETA = aETA;
   }


   public String getExternalID() {
      return iExternalID;
   }


   public String getPartNumber() {
      return iPartNumber;
   }


   public String getSerialNumber() {
      return iSerialNumber;
   }


   public String getManufacturer() {
      return iManufacturer;
   }


   public BigDecimal getQuantity() {
      return iPartQuantity;
   }


   public String getStatus() {
      return iStatus;
   }


   public String getETA() {
      return iETA;
   }
}
