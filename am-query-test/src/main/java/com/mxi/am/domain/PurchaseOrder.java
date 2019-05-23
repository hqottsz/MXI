package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.VendorKey;


/**
 * Domain entity for purchase orders used for query testing.
 *
 */
public class PurchaseOrder {

   private RefPoAuthLvlStatusKey iAuthStatus;

   private BigDecimal iExchangeQty = BigDecimal.ONE;
   private Date iCreationDt;
   private Date iIssueDt;

   private RefEventStatusKey iOrderStatus = RefEventStatusKey.POOPEN;
   private RefPoTypeKey iRefPoTypeKey = RefPoTypeKey.PURCHASE;
   private RefReqPriorityKey iRefReqPriorityKey = RefReqPriorityKey.NORMAL;
   private VendorKey iVendor;
   private String iDescription;
   private int iRevisionNumber = 1;
   private LocationKey iShipToLocation;
   private RefCurrencyKey iCurrency = new RefCurrencyKey( 0, "BLK" );
   private LocationKey iReexpediteLocation;
   private HumanResourceKey iContactHR;
   private VendorKey iBroker;
   private boolean isHistoric = false;
   private String iOrderNumber;
   private OrgKey iOrganization;


   public PurchaseOrder authStatus( RefPoAuthLvlStatusKey aAuthStatus ) {
      iAuthStatus = aAuthStatus;
      return this;
   }


   public RefPoAuthLvlStatusKey getAuthStatus() {
      return iAuthStatus;
   }


   public PurchaseOrder revisionNumber( int aRevisionNumber ) {
      iRevisionNumber = aRevisionNumber;
      return this;
   }


   public int getRevisionNumber() {
      return iRevisionNumber;
   }


   public PurchaseOrder reexpediteTo( LocationKey aLocation ) {
      iReexpediteLocation = aLocation;
      return this;
   }


   public LocationKey getReexpediteTo() {
      return iReexpediteLocation;
   }


   public PurchaseOrder exchangeRate( BigDecimal aExchangeQty ) {
      iExchangeQty = aExchangeQty;
      return this;
   }


   public BigDecimal getExchangeRate() {
      return iExchangeQty;
   }


   public PurchaseOrder issueDate( Date aDate ) {
      iIssueDt = aDate;
      return this;
   }


   public Date getIssueDate() {
      return iIssueDt;
   }


   public PurchaseOrder creationDate( Date aCreationDate ) {
      iCreationDt = aCreationDate;
      return this;
   }


   public Date getCreationDate() {
      return iCreationDt;
   }


   public PurchaseOrder orderType( RefPoTypeKey aRefPoTypeKey ) {
      iRefPoTypeKey = aRefPoTypeKey;
      return this;
   }


   public RefPoTypeKey getOrderType() {
      return iRefPoTypeKey;
   }


   public PurchaseOrder priority( RefReqPriorityKey aRefReqPriorityKey ) {
      iRefReqPriorityKey = aRefReqPriorityKey;
      return this;
   }


   public RefReqPriorityKey getPriority() {
      return iRefReqPriorityKey;
   }


   public PurchaseOrder status( RefEventStatusKey aOrderStatus ) {
      iOrderStatus = aOrderStatus;
      return this;
   }


   public RefEventStatusKey getStatus() {
      return iOrderStatus;
   }


   public PurchaseOrder vendor( VendorKey aVendorKey ) {
      iVendor = aVendorKey;
      return this;
   }


   public VendorKey getVendor() {
      return iVendor;
   }


   public PurchaseOrder broker( VendorKey aBroker ) {
      iBroker = aBroker;
      return this;
   }


   public VendorKey getBroker() {
      return iBroker;
   }


   public PurchaseOrder shippingTo( LocationKey aLocation ) {
      iShipToLocation = aLocation;
      return this;
   }


   public LocationKey getShippingTo() {
      return iShipToLocation;
   }


   public PurchaseOrder usingCurrency( RefCurrencyKey aCurrency ) {
      iCurrency = aCurrency;
      return this;
   }


   public RefCurrencyKey getUsingCurrency() {
      return iCurrency;
   }


   public PurchaseOrder contactHR( HumanResourceKey aContactHR ) {
      iContactHR = aContactHR;
      return this;
   }


   public HumanResourceKey getContactHR() {
      return iContactHR;
   }


   public PurchaseOrder description( String aDescription ) {
      iDescription = aDescription;
      return this;
   }


   public String getDescription() {
      return iDescription;
   }


   public PurchaseOrder historic() {
      isHistoric = true;
      return this;
   }


   public Boolean getHistoric() {
      return isHistoric;
   }


   public String getOrderNumber() {
      return iOrderNumber;
   }


   public PurchaseOrder setOrderNumber( String aOrderNumber ) {
      iOrderNumber = aOrderNumber;
      return this;
   }


   public OrgKey getOrganization() {
      return iOrganization;
   }


   public PurchaseOrder setOrganization( OrgKey aOrganization ) {
      iOrganization = aOrganization;
      return this;
   }
}
