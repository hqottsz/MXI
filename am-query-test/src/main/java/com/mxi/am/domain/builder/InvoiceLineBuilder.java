
package com.mxi.am.domain.builder;

import java.math.BigDecimal;

import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseInvoiceLineKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.table.po.PoInvoiceLine;
import com.mxi.mx.core.table.po.PoInvoiceLineMap;


/**
 * This is a data builder that builds invoice lines.
 *
 * @author dsewell
 */
public class InvoiceLineBuilder implements DomainBuilder<PurchaseInvoiceLineKey> {

   private FncAccountKey iAccount;

   private final PurchaseInvoiceKey iInvoiceKey;

   private BigDecimal iInvoiceQty;

   private BigDecimal iLinePrice;

   private PurchaseOrderLineKey iOrderLine;

   private PartNoKey iPart;

   private RefQtyUnitKey iQtyUnit;


   /**
    * Creates a new {@linkplain InvoiceLineBuilder} object.
    *
    * @param aInvoiceKey
    *           The invoice this line belongs to.
    */
   public InvoiceLineBuilder(PurchaseInvoiceKey aInvoiceKey) {
      iInvoiceKey = aInvoiceKey;
   }


   /**
    * Builds the order line.
    *
    * @return The order line key
    */
   @Override
   public PurchaseInvoiceLineKey build() {
      PoInvoiceLine lInvoiceLine = PoInvoiceLine.create( iInvoiceKey );

      lInvoiceLine.setAccount( iAccount );
      lInvoiceLine.setLinePrice( iLinePrice );
      lInvoiceLine.setPartNo( iPart );
      lInvoiceLine.setInvoiceQt( iInvoiceQty );
      lInvoiceLine.setQtyUnit( iQtyUnit );

      PurchaseInvoiceLineKey lInvoiceLineKey = lInvoiceLine.insert();

      if ( iOrderLine != null ) {
         PoInvoiceLineMap lPoInvoiceLineMap =
               PoInvoiceLineMap.create( lInvoiceLineKey, iOrderLine );

         lPoInvoiceLineMap.insert();
      }

      return lInvoiceLineKey;
   }


   /**
    * Sets the part.
    *
    * @param aPart
    *           The part
    *
    * @return The builder
    */
   public InvoiceLineBuilder forPart( PartNoKey aPart ) {
      iPart = aPart;

      return this;
   }


   /**
    * Sets the order line to map to.
    *
    * @param aOrderLine
    *           The order line
    *
    * @return The builder
    */
   public InvoiceLineBuilder mapToOrderLine( PurchaseOrderLineKey aOrderLine ) {
      iOrderLine = aOrderLine;

      return this;
   }


   /**
    * Sets the account.
    *
    * @param aAccount
    *           The account
    *
    * @return The builder
    */
   public InvoiceLineBuilder withAccount( FncAccountKey aAccount ) {
      iAccount = aAccount;

      return this;
   }


   /**
    * Sets the invoice quantity.
    *
    * @param aInvoiceQty
    *           The invoice quantity
    *
    * @return The builder
    */
   public InvoiceLineBuilder withInvoiceQuantity( BigDecimal aInvoiceQty ) {
      iInvoiceQty = aInvoiceQty;

      return this;
   }


   /**
    * Sets the line price
    *
    * @param aLinePrice
    *           The line price
    *
    * @return The builder
    */
   public InvoiceLineBuilder withLinePrice( BigDecimal aLinePrice ) {
      iLinePrice = aLinePrice;

      return this;
   }


   /**
    * Sets the quantity unit type.
    *
    * @param aQtyUnit
    *           The unit type
    *
    * @return The builder
    */
   public InvoiceLineBuilder withUnitType( RefQtyUnitKey aQtyUnit ) {
      iQtyUnit = aQtyUnit;

      return this;
   }
}
