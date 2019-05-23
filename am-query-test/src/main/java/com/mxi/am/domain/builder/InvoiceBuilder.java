
package com.mxi.am.domain.builder;

import com.mxi.am.domain.Invoice;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.core.table.po.PoInvoice;


/**
 * This is a data builder for invoices.
 *
 * @author dsewell
 */
public class InvoiceBuilder {

   /**
    * Builds the order.
    *
    * @return The key from the new invoice.
    */
   public static PurchaseInvoiceKey build( Invoice aInvoice ) {
      EventKey lEventKey = new EventBuilder().withType( RefEventTypeKey.PI )
            .withStatus( aInvoice.getInvoiceStatus() )
            .withDescription( aInvoice.getInvoiceNumber() ).build();

      if ( aInvoice.getStageDate() != null ) {

         EvtStageTable lEvtStage = EvtStageTable.create( lEventKey );
         lEvtStage.setStageGdt( aInvoice.getStageDate() );

         if ( aInvoice.getStageStatus() != null ) {
            lEvtStage.setEventStatus( aInvoice.getStageStatus() );
         }

         lEvtStage.insert();
      }

      PoInvoice lPoInvoice = PoInvoice.create( lEventKey );

      lPoInvoice.setExchangeQt( aInvoice.getExchangeQty() );
      lPoInvoice.setVendor( aInvoice.getVendor() );
      lPoInvoice.setCurrency( aInvoice.getCurrency() );

      return lPoInvoice.insert();
   }

}
