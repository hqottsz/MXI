package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.VendorKey;


public class Invoice {

   private BigDecimal exchangeQty = BigDecimal.ONE;

   private RefEventStatusKey invoiceStatus;

   private String invoiceNumber;

   private VendorKey vendor;

   private RefCurrencyKey currency;

   private RefEventStatusKey stageStatus;

   private Date stageDate;


   public BigDecimal getExchangeQty() {
      return exchangeQty;
   }


   public void setExchangeQty( BigDecimal exchangeQty ) {
      this.exchangeQty = exchangeQty;
   }


   public RefEventStatusKey getInvoiceStatus() {
      return invoiceStatus;
   }


   public void setInvoiceStatus( RefEventStatusKey invoiceStatus ) {
      this.invoiceStatus = invoiceStatus;
   }


   public String getInvoiceNumber() {
      return invoiceNumber;
   }


   public void setInvoiceNumber( String invoiceNumber ) {
      this.invoiceNumber = invoiceNumber;
   }


   public VendorKey getVendor() {
      return vendor;
   }


   public void setVendor( VendorKey vendor ) {
      this.vendor = vendor;
   }


   public RefCurrencyKey getCurrency() {
      return currency;
   }


   public void setCurrency( RefCurrencyKey currency ) {
      this.currency = currency;
   }


   public RefEventStatusKey getStageStatus() {
      return stageStatus;
   }


   public void setStageStatus( RefEventStatusKey stageStatus ) {
      this.stageStatus = stageStatus;
   }


   public Date getStageDate() {
      return stageDate;
   }


   public void setStageDate( Date stageDate ) {
      this.stageDate = stageDate;
   }

}
