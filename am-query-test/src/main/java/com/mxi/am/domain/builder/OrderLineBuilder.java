
package com.mxi.am.domain.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.dao.po.PoLineMpTableDao;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PoLineMpKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.table.po.POLineTable;
import com.mxi.mx.core.table.po.PoLineMpTableRow;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * This is a data builder that builds order lines.
 *
 * @author dsewell
 */
public class OrderLineBuilder implements DomainBuilder<PurchaseOrderLineKey> {

   private FncAccountKey iAccount;

   private BigDecimal iAccruedValue;

   private RefPoLineTypeKey iLineType = RefPoLineTypeKey.PURCHASE;

   private final PurchaseOrderKey iOrderKey;

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

   private String iMpKeyDesc;


   /**
    * Creates a new {@linkplain OrderLineBuilder} object.
    *
    * @param aOrderKey
    *           The order this line belongs to.
    */
   public OrderLineBuilder(PurchaseOrderKey aOrderKey) {
      iOrderKey = aOrderKey;
   }


   /**
    * Builds the order line.
    *
    * @return The order line key
    */
   @Override
   public PurchaseOrderLineKey build() {
      POLineTable lPoLine = POLineTable.create( iOrderKey );

      lPoLine.setReceivedQt( iReceivedQuantity );
      lPoLine.setPoLineType( iLineType );
      lPoLine.setUnitPrice( iUnitPrice );
      lPoLine.setOrderQt( iOrderQty );
      lPoLine.setQtyUnit( iQtyUnit );
      lPoLine.setAccount( iAccount );
      lPoLine.setAccruedValue( iAccruedValue );
      lPoLine.setTask( iTask );
      lPoLine.setOwner( iOwner );
      lPoLine.setDeletedBool( iIsDeleted );
      lPoLine.setPreInspQty( iPreInspQty );

      if ( iLinePrice != null ) {
         lPoLine.setLinePrice( iLinePrice );
      } else if ( iOrderQty != null && iUnitPrice != null ) {
         lPoLine.setLinePrice( iOrderQty.multiply( iUnitPrice ) );
      }

      if ( iLineDescription != null ) {
         lPoLine.setLineLdesc( iLineDescription );
      }

      if ( iPromiseByDate != null ) {
         lPoLine.setPromisedBy( iPromiseByDate );
      }

      if ( iPart != null ) {
         lPoLine.setPartNo( iPart );
      }

      try {
         lPoLine.setLineNoOrd( MxCoreUtils.getNextVal( iOrderKey, "po_line", "line_no_ord" ) );
      } catch ( MxException e ) {
         // cannot create more than 99 lines for an order
      }

      PurchaseOrderLineKey lPoLineKey = lPoLine.insert();

      for ( PartRequestKey lPartRequest : iPartRequests ) {
         ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( lPartRequest );
         lReqPart.setPOLine( lPoLineKey );
         lReqPart.update();
      }

      if ( iMpKeyDesc != null ) {
         PoLineMpTableRow lPoLineMp = InjectorContainer.get().getInstance( PoLineMpTableDao.class )
               .create( new PoLineMpKey( lPoLineKey ) );
         lPoLineMp.setMpKeySdesc( iMpKeyDesc );
         lPoLineMp.insert();
      }

      return lPoLineKey;
   }


   /**
    * Sets the part
    *
    * @param aPartNoKey
    *           the part key
    *
    * @return The builder
    */
   public OrderLineBuilder forPart( PartNoKey aPartNoKey ) {
      iPart = aPartNoKey;

      return this;
   }


   /**
    * Sets the part request. This method may be called more than once as a PO can be associated to
    * many part requests.
    *
    * @param aPartRequest
    *           The part request
    *
    * @return The builder
    */
   public OrderLineBuilder forPartRequest( PartRequestKey aPartRequest ) {
      iPartRequests.add( aPartRequest );

      return this;
   }


   /**
    * Sets the deleted boolean to true.
    *
    * @return The builder
    */
   public OrderLineBuilder isDeleted() {
      iIsDeleted = true;

      return this;
   }


   /**
    * Sets the account
    *
    * @param aAccount
    *           The account
    *
    * @return The builder
    */
   public OrderLineBuilder withAccount( FncAccountKey aAccount ) {
      iAccount = aAccount;

      return this;
   }


   /**
    * Sets the accrual amount
    *
    * @param aAccruedValue
    *           The accrual amount
    *
    * @return The builder
    */
   public OrderLineBuilder withAccruedValue( BigDecimal aAccruedValue ) {
      iAccruedValue = aAccruedValue;

      return this;
   }


   /**
    * Sets the line description.
    *
    * @param aLineDescription
    *           The line description
    *
    * @return The builder
    */
   public OrderLineBuilder withLineDescription( String aLineDescription ) {
      iLineDescription = aLineDescription;

      return this;
   }


   public OrderLineBuilder withMpKeySdesc( String aMpKeyDesc ) {
      iMpKeyDesc = aMpKeyDesc;

      return this;
   }


   /**
    * Sets the line type.
    *
    * @param aLineType
    *           The line type
    *
    * @return The builder
    */
   public OrderLineBuilder withLineType( RefPoLineTypeKey aLineType ) {
      iLineType = aLineType;

      return this;
   }


   /**
    * Sets the task.
    *
    * @param aTask
    *           The task
    *
    * @return The builder
    */
   public OrderLineBuilder forTask( TaskKey aTask ) {
      iTask = aTask;

      return this;
   }


   /**
    * Sets the order quantity.
    *
    * @param aOrderQty
    *           The order quantity
    *
    * @return The builder
    */
   public OrderLineBuilder withOrderQuantity( BigDecimal aOrderQty ) {
      iOrderQty = aOrderQty;

      return this;
   }


   /**
    * Sets the owner
    *
    * @param aOwner
    *           the owner key
    *
    * @return The builder
    */
   public OrderLineBuilder withOwner( OwnerKey aOwner ) {
      iOwner = aOwner;

      return this;
   }


   /**
    * Sets the order promise by date.
    *
    * @param aDate
    *           promise date
    *
    * @return The builder
    */
   public OrderLineBuilder promisedBy( Date aDate ) {

      iPromiseByDate = aDate;

      return this;
   }


   /**
    * Sets the received quantity
    *
    * @param aReceivedQty
    *           the received quantity
    *
    * @return The builder
    */
   public OrderLineBuilder withReceivedQuantity( BigDecimal aReceivedQty ) {
      iReceivedQuantity = aReceivedQty.doubleValue();

      return this;
   }


   /**
    * Sets the task.
    *
    * @param aTask
    *           The task
    *
    * @return The builder
    */
   public OrderLineBuilder withTask( TaskKey aTask ) {
      iTask = aTask;

      return this;
   }


   /**
    * Sets the order line unit price.
    *
    * @param aUnitPrice
    *           The unit price
    *
    * @return The builder
    */
   public OrderLineBuilder withUnitPrice( BigDecimal aUnitPrice ) {
      iUnitPrice = aUnitPrice;

      return this;
   }


   /**
    * Sets the order line price.
    *
    * @param aLinePrice
    *           The line price
    *
    * @return The builder
    */
   public OrderLineBuilder withLinePrice( BigDecimal aLinePrice ) {
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
   public OrderLineBuilder withUnitType( RefQtyUnitKey aQtyUnit ) {
      iQtyUnit = aQtyUnit;

      return this;
   }


   /**
    * Sets the Pre Inspected quantity
    *
    * @param aPreInspQty
    *           the Pre Inspected quantity
    *
    * @return The builder
    */
   public OrderLineBuilder withPreInspQty( BigDecimal aPreInspQty ) {
      iPreInspQty = aPreInspQty.doubleValue();

      return this;
   }
}
