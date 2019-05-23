package com.mxi.am.stepdefn.persona.lineplanner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PartReservation {

   private String iReqPartNo;
   private int iReqQty;
   private String iInventoryClass;
   private String iPartGroupName;
   private String iMasterRequestID;
   private List<ReservationLine> iReservationLine = new ArrayList();


   public PartReservation(String aReqPartNo, int aReqQty, List<String> aExternalId,
         List<String> aPartNumber, List<String> aManufacturer, String aSerialNumber,
         String aInventoryClass, String aPartGroupName, List<Integer> aQuantity,
         List<String> aStatus, List<String> aETA) {
      iReqPartNo = aReqPartNo;
      iReqQty = aReqQty;
      iInventoryClass = aInventoryClass;
      iPartGroupName = aPartGroupName;
      Iterator<String> lIterator = aPartNumber.iterator();
      int j = 0;
      while ( lIterator.hasNext() ) {
         lIterator.next();
         iReservationLine.add( new ReservationLine( aExternalId.get( j ), aPartNumber.get( j ),
               aManufacturer.get( j ), aSerialNumber, BigDecimal.valueOf( aQuantity.get( j ) ),
               aStatus.get( j ), aETA.get( j ) ) );
         j++;
      }

   }


   public String getReqPartNo() {
      return iReqPartNo;
   }


   public int getReqQty() {
      return iReqQty;
   }


   public String getInventoryClass() {
      return iInventoryClass;
   }


   public String getPartGroupName() {
      return iPartGroupName;
   }


   public void setMasterRequestId( String aRequestId ) {
      iMasterRequestID = aRequestId;
   }


   public String getMasterRequestId() {
      return iMasterRequestID;
   }


   public List<ReservationLine> getReservationLine() {
      return iReservationLine;
   }

}
