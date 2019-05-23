
package com.mxi.am.domain.builder;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncXactionLogKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefXactionTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.fnc.FncXactionLog;


/**
 * Builds a <code>fnc_xaction_log</code> object
 */
public class FinancialTransactionBuilder implements DomainBuilder<FncXactionLogKey> {

   private RefXactionTypeKey iTransactionType;
   private Date iTransactionDate;
   private BigDecimal iUnitPrice = BigDecimal.ZERO;
   private PurchaseOrderLineKey iPurchaseOrderLineKey;
   private PurchaseInvoiceLineKey iInvoiceLineKey;
   private PartNoKey iPartNoKey;
   private InventoryKey iInventoryKey;
   private String iDescription;
   private EventKey iEventKey;
   private TaskKey iTaskKey;


   /**
    * Creates a new {@linkplain FinancialTransactionBuilder} object.
    */
   public FinancialTransactionBuilder() {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public FncXactionLogKey build() {

      FncXactionLog lFncXactionLog = FncXactionLog.create();

      lFncXactionLog.setXactionType( iTransactionType );
      lFncXactionLog.setXactionDt( iTransactionDate );
      lFncXactionLog.setUnitPrice( iUnitPrice );
      lFncXactionLog.setXactionLdesc( iDescription );

      if ( iPurchaseOrderLineKey != null ) {
         lFncXactionLog.setPOLine( iPurchaseOrderLineKey );
      }

      if ( iInvoiceLineKey != null ) {
         lFncXactionLog.setPOInvoiceLine( iInvoiceLineKey );
      }

      if ( iPartNoKey != null ) {
         lFncXactionLog.setPartNo( iPartNoKey );
      }

      if ( iInventoryKey != null ) {
         lFncXactionLog.setInventory( iInventoryKey );
      }

      if ( iEventKey != null ) {
         lFncXactionLog.setEvent( iEventKey );
      }

      if ( iTaskKey != null ) {
         lFncXactionLog.setTask( iTaskKey );
      }

      return lFncXactionLog.insert();
   }


   /*
    * Sets the transaction type.
    * 
    * @param aTransactionType The transaction type
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder withType( RefXactionTypeKey aTransactionType ) {
      iTransactionType = aTransactionType;

      return this;
   }


   /*
    * Sets the unit price
    * 
    * @param aUnitPrice The unit price
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder withUnitPrice( BigDecimal aUnitPrice ) {
      iUnitPrice = aUnitPrice;

      return this;
   }


   /*
    * Sets the transaction date
    * 
    * @param aTransactionDate The transaction date
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder withTransactionDate( Date aTransactionDate ) {
      iTransactionDate = aTransactionDate;

      return this;
   }


   /*
    * Sets the purchase order line
    * 
    * @param aPurchaseOrderLineKey The purchase order line
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder forOrderLine( PurchaseOrderLineKey aPurchaseOrderLineKey ) {
      iPurchaseOrderLineKey = aPurchaseOrderLineKey;

      return this;
   }


   /*
    * Sets the invoice order line
    * 
    * @param aInvoiceLineKey The invoice order line
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder forInvoiceLine( PurchaseInvoiceLineKey aInvoiceLineKey ) {
      iInvoiceLineKey = aInvoiceLineKey;

      return this;
   }


   /*
    * Sets the part no
    * 
    * @param aPartNoKey The part no
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder withPart( PartNoKey aPartNoKey ) {
      iPartNoKey = aPartNoKey;

      return this;
   }


   /*
    * Sets the inventory
    * 
    * @param aInventoryKey The inventory
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder withInventory( InventoryKey aInventoryKey ) {
      iInventoryKey = aInventoryKey;

      return this;
   }


   /*
    * Sets the transaction description
    * 
    * @param aDescription The transaction description
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder withDescription( String aDescription ) {
      iDescription = aDescription;

      return this;
   }


   /*
    * Sets the event
    * 
    * @param aEventKey The transfer event for manually issue or expected turn in
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder withEvent( EventKey aEventKey ) {
      iEventKey = aEventKey;

      return this;
   }


   /*
    * Sets the task
    * 
    * @param aTaskKey The task
    * 
    * @return The builder
    */
   public FinancialTransactionBuilder withTask( TaskKey aTaskKey ) {
      iTaskKey = aTaskKey;

      return this;
   }
}
