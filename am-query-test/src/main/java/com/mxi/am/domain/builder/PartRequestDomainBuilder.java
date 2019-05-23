package com.mxi.am.domain.builder;

import com.mxi.am.domain.PartRequestEntity;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefSupplyChainKey;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * This is a data builder for part requests for testing purposes. Performs inserts on EVT_EVENT and
 * REQ_PART.
 */
public class PartRequestDomainBuilder {

   public static PartRequestKey build( PartRequestEntity aPartRequestEntity ) {

      String lPartRequestBarcode = aPartRequestEntity.getBarcode();

      EventBuilder lEventBuilder = new EventBuilder().withStatus( aPartRequestEntity.getStatus() )
            .withType( RefEventTypeKey.PR ).withDescription( lPartRequestBarcode );

      if ( aPartRequestEntity.getHistorical() ) {
         lEventBuilder.asHistoric();
      }

      EventKey lEventKey = lEventBuilder.build();

      ReqPartTable lReqPart =
            ReqPartTable.create( new PartRequestKey( lEventKey.getDbId(), lEventKey.getId() ) );

      lReqPart.setInstalledPart( aPartRequestEntity.getPartRequirement() );
      lReqPart.setPrSched( aPartRequestEntity.getTask() );
      lReqPart.setReqByDt( aPartRequestEntity.getRequiredBy() );
      lReqPart.setReqQt( aPartRequestEntity.getRequestedQuantity() );
      lReqPart.setReqType( aPartRequestEntity.getType() );
      lReqPart.setReqLocation( aPartRequestEntity.getNeededAt() );
      lReqPart.setSupplyChain( RefSupplyChainKey.DEFAULT );
      lReqPart.setInventory( aPartRequestEntity.getReservedInventory() );
      lReqPart.setPoPartNo( aPartRequestEntity.getPurchasePart() );
      lReqPart.setReqSpecPartNo( aPartRequestEntity.getSpecifiedPart() );
      lReqPart.setPOLine( aPartRequestEntity.getPurchaseLine() );
      lReqPart.setShipmentLine( aPartRequestEntity.getShipmentLine() );
      lReqPart.setReqAircraft( aPartRequestEntity.getReqAircraft() );
      lReqPart.setReqBomPart( aPartRequestEntity.getPartGroup() );
      lReqPart.setReqPriority( aPartRequestEntity.getPriority() );
      lReqPart.setLastAutoRsrvDt( aPartRequestEntity.getLastAutoRsrvDate() );
      lReqPart.setRemoteLoc( aPartRequestEntity.getRemoteLocation() );
      lReqPart.setReqMasterId( lPartRequestBarcode );
      lReqPart.setEstArrivalDt( aPartRequestEntity.getEstimatedArrivalDate() );
      lReqPart.setUpdatedETA( aPartRequestEntity.getDeliveryETADate() );
      lReqPart.setReqStockNo( aPartRequestEntity.getReqStockNo() );
      lReqPart.setReqHr( aPartRequestEntity.getRequestedBy() );
      lReqPart.setAssignHr( aPartRequestEntity.getAssignedTo() );
      lReqPart.setIssueAccount( aPartRequestEntity.getIssueAccount() );
      lReqPart.setQuantityUnit( aPartRequestEntity.getQuantityUnit() );

      return lReqPart.insert();
   }
}
