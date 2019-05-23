package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Domain entity for purchase order line used for query testing.
 *
 */
public class PurchaseOrderLine {

   private FncAccountKey iAccount;

   private BigDecimal iAccruedValue;

   private RefPoLineTypeKey iLineType = RefPoLineTypeKey.PURCHASE;

   private PurchaseOrderKey iOrderKey;

   private BigDecimal iOrderQty;

   private OwnerKey iOwner;

   private PartNoKey iPart;

   private List<PartRequestKey> iPartRequests = new ArrayList<PartRequestKey>();

   private Date iPromiseByDate;

   private RefQtyUnitKey iQtyUnit;

   private double iReceivedQuantity = 0.0;

   private TaskKey iTask;

   private BigDecimal iUnitPrice;

   private BigDecimal iLinePrice;

   private String iLineDescription;

   private boolean iIsDeleted = false;

   private double iPreInspQty;


   public PurchaseOrderLine orderKey( PurchaseOrderKey aPurchaseOrderKey ) {
      iOrderKey = aPurchaseOrderKey;
      return this;
   }


   public PurchaseOrderKey getOrderKey() {
      return iOrderKey;
   }


   public PurchaseOrderLine part( PartNoKey aPartNoKey ) {
      iPart = aPartNoKey;
      return this;
   }


   public PartNoKey getPart() {
      return iPart;
   }


   public PurchaseOrderLine partRequest( PartRequestKey aPartRequest ) {
      iPartRequests.add( aPartRequest );
      return this;
   }


   public List<PartRequestKey> getPartRequest() {
      return iPartRequests;
   }


   public PurchaseOrderLine deleted() {
      iIsDeleted = true;
      return this;
   }


   public boolean getDeleted() {
      return iIsDeleted;
   }


   public PurchaseOrderLine account( FncAccountKey aAccount ) {
      iAccount = aAccount;
      return this;
   }


   public FncAccountKey getAccount() {
      return iAccount;
   }


   public PurchaseOrderLine accruedValue( BigDecimal aAccruedValue ) {
      iAccruedValue = aAccruedValue;
      return this;
   }


   public BigDecimal getAccruedValue() {
      return iAccruedValue;
   }


   public PurchaseOrderLine lineDescription( String aLineDescription ) {
      iLineDescription = aLineDescription;
      return this;
   }


   public String getLineDescription() {
      return iLineDescription;
   }


   public PurchaseOrderLine lineType( RefPoLineTypeKey aLineType ) {
      iLineType = aLineType;
      return this;
   }


   public RefPoLineTypeKey getLineType() {
      return iLineType;
   }


   public PurchaseOrderLine task( TaskKey aTask ) {
      iTask = aTask;
      return this;
   }


   public TaskKey getTask() {
      return iTask;
   }


   public PurchaseOrderLine orderQuantity( BigDecimal aOrderQty ) {
      iOrderQty = aOrderQty;
      return this;
   }


   public BigDecimal getOrderQuantity() {
      return iOrderQty;
   }


   public PurchaseOrderLine owner( OwnerKey aOwner ) {
      iOwner = aOwner;
      return this;
   }


   public OwnerKey getOwner() {
      return iOwner;
   }


   public PurchaseOrderLine promisedBy( Date aDate ) {
      iPromiseByDate = aDate;
      return this;
   }


   public Date getPromisedBy() {
      return iPromiseByDate;
   }


   public PurchaseOrderLine receivedQuantity( BigDecimal aReceivedQty ) {
      iReceivedQuantity = aReceivedQty.doubleValue();
      return this;
   }


   public double getReceivedQuantity() {
      return iReceivedQuantity;
   }


   public PurchaseOrderLine unitPrice( BigDecimal aUnitPrice ) {
      iUnitPrice = aUnitPrice;

      return this;
   }


   public BigDecimal getUnitPrice() {
      return iUnitPrice;
   }


   public PurchaseOrderLine linePrice( BigDecimal aLinePrice ) {
      iLinePrice = aLinePrice;

      return this;
   }


   public BigDecimal getLinePrice() {
      return iLinePrice;
   }


   public PurchaseOrderLine unitType( RefQtyUnitKey aQtyUnit ) {
      iQtyUnit = aQtyUnit;

      return this;
   }


   public RefQtyUnitKey getUnitType() {
      return iQtyUnit;
   }


   public PurchaseOrderLine preInspQty( BigDecimal aPreInspQty ) {
      iPreInspQty = aPreInspQty.doubleValue();

      return this;
   }


   public double getPreInspQty() {
      return iPreInspQty;
   }
}
