package com.mxi.am.domain;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockDistReqStatusKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * This class holds the data for the <code> STOCK_DIST_REQ</code> table.
 *
 * @author Libin Cai
 * @since September 12, 2018
 */
public class StockDistReq {

   private Float neededQuantity;
   private StockNoKey stockNo;
   private RefStockDistReqStatusKey stockDistReqStatus;
   private LocationKey neededLocation;
   private OwnerKey owner;
   private LocationKey supplierLocation;
   private HumanResourceKey assignedHr;
   private RefQtyUnitKey qtyUnit;
   private String requestId;


   public RefQtyUnitKey getQtyUnit() {
      return qtyUnit;
   }


   public void setQtyUnit( RefQtyUnitKey qtyUnit ) {
      this.qtyUnit = qtyUnit;
   }


   public Float getNeededQuantity() {
      return neededQuantity;
   }


   public void setNeededQuantity( Float neededQuantity ) {
      this.neededQuantity = neededQuantity;
   }


   public StockNoKey getStockNo() {
      return stockNo;
   }


   public void setStockNo( StockNoKey stockNo ) {
      this.stockNo = stockNo;
   }


   public RefStockDistReqStatusKey getStatus() {
      return stockDistReqStatus;
   }


   public void setStatus( RefStockDistReqStatusKey stockDistReqStatus ) {
      this.stockDistReqStatus = stockDistReqStatus;
   }


   public LocationKey getNeededLocation() {
      return neededLocation;
   }


   public void setNeededLocation( LocationKey neededLocation ) {
      this.neededLocation = neededLocation;
   }


   public OwnerKey getOwner() {
      return owner;
   }


   public void setOwner( OwnerKey owner ) {
      this.owner = owner;
   }


   public LocationKey getSupplierLocation() {
      return supplierLocation;
   }


   public void setSupplierLocation( LocationKey supplierLocation ) {
      this.supplierLocation = supplierLocation;
   }


   public HumanResourceKey getAssignedHr() {
      return assignedHr;
   }


   public void setAssignedHr( HumanResourceKey assignedHr ) {
      this.assignedHr = assignedHr;
   }


   public String getRequestId() {
      return requestId;
   }


   public void setRequestId( String requestId ) {
      this.requestId = requestId;
   }

}
