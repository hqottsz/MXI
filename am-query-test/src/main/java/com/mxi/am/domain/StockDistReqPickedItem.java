package com.mxi.am.domain;

import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.TransferKey;


/**
 * This class holds the data for the <code> STOCK_DIST_REQ_PICKED_ITEM</code> table.
 *
 * @author Libin Cai
 * @since September 14, 2018
 */
public class StockDistReqPickedItem {

   private StockDistReqKey stockDistReq;
   private TransferKey transfer;


   public StockDistReqKey getStockDistReq() {
      return stockDistReq;
   }


   public void setStockDistReq( StockDistReqKey stockDistReq ) {
      this.stockDistReq = stockDistReq;
   }


   public TransferKey getTransferKey() {
      return transfer;
   }


   public void setTransferKey( TransferKey transfer ) {
      this.transfer = transfer;
   }

}
