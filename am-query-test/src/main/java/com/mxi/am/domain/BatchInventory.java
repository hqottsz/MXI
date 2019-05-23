
package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Optional;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;


public class BatchInventory {

   private String iBatchNumbr;
   private PartNoKey iPartNumber;
   private LocationKey iLocation;
   private RefInvCondKey iCondition;
   private BigDecimal iBinQt;
   private OwnerKey iOwner;
   private boolean iComplete;
   private InventoryKey iParent;
   private RefOwnerTypeKey iOwnershipType;
   private PurchaseOrderLineKey iOrderLineKey;
   private FinanceStatusCd iFinanceStatusCd;
   private boolean iIssued = false;


   public boolean isIssued() {
      return iIssued;
   }


   public void setIssued( boolean aIssued ) {
      iIssued = aIssued;
   }


   public void setFinanceStatusCd( FinanceStatusCd aFinanceStatusCd ) {
      iFinanceStatusCd = aFinanceStatusCd;
   }


   public FinanceStatusCd getFinanceStatusCd() {
      return iFinanceStatusCd;
   }


   public void setBinQt( double aBinQt ) {
      iBinQt = BigDecimal.valueOf( aBinQt );
   }


   public void setBinQt( BigDecimal aBinQt ) {
      iBinQt = aBinQt;
   }


   public Optional<BigDecimal> getBinQt() {
      return Optional.ofNullable( iBinQt );
   }


   public void setOrderLine( PurchaseOrderLineKey aOrderLineKey ) {
      iOrderLineKey = aOrderLineKey;
   }


   public Optional<PurchaseOrderLineKey> getOrderLine() {
      return Optional.ofNullable( iOrderLineKey );
   }


   public void setOwner( OwnerKey aOwner ) {
      iOwner = aOwner;
   }


   public OwnerKey getOwner() {
      return iOwner;
   }


   public void setOwnershipType( RefOwnerTypeKey aOwnershipType ) {
      iOwnershipType = aOwnershipType;
   }


   public RefOwnerTypeKey getOwnershipType() {
      return iOwnershipType;
   }


   public void setCondition( RefInvCondKey aCondition ) {
      iCondition = aCondition;
   }


   public Optional<RefInvCondKey> getCondition() {
      return Optional.ofNullable( iCondition );
   }


   public String getBatchNumber() {
      return iBatchNumbr;
   }


   public void setBatchNumber( String aBatchNumber ) {
      iBatchNumbr = aBatchNumber;
   }


   public void setPartNumber( PartNoKey aPartNumber ) {
      iPartNumber = aPartNumber;
   }


   public PartNoKey getPartNumber() {
      return iPartNumber;
   }


   public void setLocation( LocationKey aLocation ) {
      iLocation = aLocation;
   }


   public LocationKey getLocation() {
      return iLocation;
   }


   public void setComplete( boolean aInvComplete ) {
      iComplete = aInvComplete;
   }


   public boolean getComplete() {
      return iComplete;
   }


   public void setParent( InventoryKey aParent ) {
      iParent = aParent;
   }


   public InventoryKey getParent() {
      return iParent;
   }

}
