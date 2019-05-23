package com.mxi.am.domain.builder;

import com.mxi.am.domain.PurchaseOrderLine;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * Domain Entity Builder for Purchase Order Line. Performs inserts on PO_LINE and REQ_PART
 */
public class PurchaseOrderLineBuilder {

   public static PurchaseOrderLineKey build( PurchaseOrderLine aPurchaseOrderLine ) {
      POLineTable lPoLine = POLineTable.create( aPurchaseOrderLine.getOrderKey() );

      lPoLine.setReceivedQt( aPurchaseOrderLine.getReceivedQuantity() );
      lPoLine.setPoLineType( aPurchaseOrderLine.getLineType() );
      lPoLine.setUnitPrice( aPurchaseOrderLine.getUnitPrice() );
      lPoLine.setOrderQt( aPurchaseOrderLine.getOrderQuantity() );
      lPoLine.setQtyUnit( aPurchaseOrderLine.getUnitType() );
      lPoLine.setAccount( aPurchaseOrderLine.getAccount() );
      lPoLine.setAccruedValue( aPurchaseOrderLine.getAccruedValue() );
      lPoLine.setTask( aPurchaseOrderLine.getTask() );
      lPoLine.setOwner( aPurchaseOrderLine.getOwner() );
      lPoLine.setDeletedBool( aPurchaseOrderLine.getDeleted() );
      lPoLine.setPreInspQty( aPurchaseOrderLine.getPreInspQty() );

      if ( aPurchaseOrderLine.getLinePrice() != null ) {
         lPoLine.setLinePrice( aPurchaseOrderLine.getLinePrice() );
      } else if ( aPurchaseOrderLine.getOrderQuantity() != null
            && aPurchaseOrderLine.getUnitPrice() != null ) {
         lPoLine.setLinePrice( aPurchaseOrderLine.getOrderQuantity()
               .multiply( aPurchaseOrderLine.getUnitPrice() ) );
      }

      if ( aPurchaseOrderLine.getLineDescription() != null ) {
         lPoLine.setLineLdesc( aPurchaseOrderLine.getLineDescription() );
      }

      if ( aPurchaseOrderLine.getPromisedBy() != null ) {
         lPoLine.setPromisedBy( aPurchaseOrderLine.getPromisedBy() );
      }

      if ( aPurchaseOrderLine.getPart() != null ) {
         lPoLine.setPartNo( aPurchaseOrderLine.getPart() );
      }

      try {
         lPoLine.setLineNoOrd( MxCoreUtils.getNextVal( aPurchaseOrderLine.getOrderKey(), "po_line",
               "line_no_ord" ) );
      } catch ( MxException e ) {
         // cannot create more than 99 lines for an order
      }

      PurchaseOrderLineKey lPoLineKey = lPoLine.insert();

      for ( PartRequestKey lPartRequest : aPurchaseOrderLine.getPartRequest() ) {
         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lPartRequest );
         lReqPart.setPOLine( lPoLineKey );
         lReqPart.update();
      }

      return lPoLineKey;
   }
}
