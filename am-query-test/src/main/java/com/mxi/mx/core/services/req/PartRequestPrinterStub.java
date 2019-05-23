package com.mxi.mx.core.services.req;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartRequestKey;


/**
 * Stub for the PartRequestPrinterInterface
 *
 */
public class PartRequestPrinterStub implements PartRequestPrinterInterface {

   /**
    * {@inheritDoc}
    */
   @Override
   public void printIssueTicket( PartRequestKey aPartRequestKey ) {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void printIssueTicket( PartRequestKey aPartRequest, HumanResourceKey aAuthorizingHr,
         LocationKey aReqSupplyLocation ) {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void printIssueTickets( PartRequestKey[] aPartRequestList,
         HumanResourceKey aAuthorizingHr, LocationKey aReqSupplyLocation ) {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void rePrintIssueTicket( PartRequestKey aPickedPartRequest ) {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isEnablePrinting() {
      return false;
   }

}
