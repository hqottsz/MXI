package com.mxi.am.domain.builder;

import com.mxi.am.domain.PurchaseOrder;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.po.PoHeaderTable;


/**
 * This is a data builder for purchase orders. Performs inserts on PO_HEADER and EVT_EVENT.
 *
 */
public class PurchaseOrderBuilder {

   public static PurchaseOrderKey build( PurchaseOrder aOrder ) {
      EventBuilder lEventBuilder = new EventBuilder().withType( RefEventTypeKey.PO )
            .withStatus( aOrder.getStatus() ).withDescription( aOrder.getDescription() )
            .withActualStartDate( aOrder.getCreationDate() );
      if ( aOrder.getHistoric() ) {
         lEventBuilder.asHistoric();
      }
      EventKey lEventKey = lEventBuilder.build();

      EvtEventDao lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable lOrderEvent = lEvtEventDao.findByPrimaryKey( lEventKey );
      if ( aOrder.getOrderNumber() != null ) {
         lOrderEvent.setEventSdesc( aOrder.getOrderNumber() );
         lEvtEventDao.update( lOrderEvent );
      }

      PoHeaderTable lPoHeader = PoHeaderTable.create( lEventKey );

      lPoHeader.setExchangeQt( aOrder.getExchangeRate() );
      lPoHeader.setPoType( aOrder.getOrderType() );
      lPoHeader.setReqPriority( aOrder.getPriority() );
      lPoHeader.setCurrency( aOrder.getUsingCurrency() );

      if ( aOrder.getVendor() != null ) {
         lPoHeader.setVendor( aOrder.getVendor() );
      }

      if ( aOrder.getBroker() != null ) {
         lPoHeader.setBroker( aOrder.getBroker() );
      }

      if ( aOrder.getIssueDate() != null ) {
         lPoHeader.setIssuedDt( aOrder.getIssueDate() );
      }

      if ( aOrder.getShippingTo() != null ) {
         lPoHeader.setShipToLoc( aOrder.getShippingTo() );
      }

      if ( aOrder.getAuthStatus() != null ) {
         lPoHeader.setAuthStatus( aOrder.getAuthStatus() );
      }

      if ( aOrder.getReexpediteTo() != null ) {
         lPoHeader.setReShipToLoc( aOrder.getReexpediteTo() );
      }

      lPoHeader.setRevisionNo( aOrder.getRevisionNumber() );

      if ( aOrder.getContactHR() != null ) {
         lPoHeader.setContactHr( aOrder.getContactHR() );
      }

      if ( aOrder.getOrganization() != null ) {
         lPoHeader.setOrganization( aOrder.getOrganization() );
      }

      return lPoHeader.insert();
   }
}
