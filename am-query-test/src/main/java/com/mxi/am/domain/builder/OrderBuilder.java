
package com.mxi.am.domain.builder;

import java.math.BigDecimal;
import java.util.Date;

import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.po.PoHeaderTable;


/**
 * This is a data builder for orders.
 *
 * @author dsewell
 */
public class OrderBuilder implements DomainBuilder<PurchaseOrderKey> {

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
   private OrgKey iOrganization;
   private boolean isHistoric = false;


   /**
    * Builds the order.
    *
    * @return The key from the new order.
    */
   @Override
   public PurchaseOrderKey build() {

      EventBuilder lEventBuilder =
            new EventBuilder().withType( RefEventTypeKey.PO ).withStatus( iOrderStatus )
                  .withDescription( iDescription ).withActualStartDate( iCreationDt );
      if ( isHistoric ) {
         lEventBuilder.asHistoric();
      }
      EventKey lEventKey = lEventBuilder.build();

      PoHeaderTable lPoHeader = PoHeaderTable.create( lEventKey );

      lPoHeader.setExchangeQt( iExchangeQty );
      lPoHeader.setPoType( iRefPoTypeKey );
      lPoHeader.setReqPriority( iRefReqPriorityKey );
      lPoHeader.setCurrency( iCurrency );

      if ( iVendor != null ) {
         lPoHeader.setVendor( iVendor );
      }

      if ( iBroker != null ) {
         lPoHeader.setBroker( iBroker );
      }

      if ( iIssueDt != null ) {
         lPoHeader.setIssuedDt( iIssueDt );
      }

      if ( iShipToLocation != null ) {
         lPoHeader.setShipToLoc( iShipToLocation );
      }

      if ( iAuthStatus != null ) {
         lPoHeader.setAuthStatus( iAuthStatus );
      }

      if ( iReexpediteLocation != null ) {
         lPoHeader.setShipToLoc( iReexpediteLocation );
      }
      if ( iOrganization != null ) {
         lPoHeader.setOrganization( iOrganization );
      }
      lPoHeader.setRevisionNo( iRevisionNumber );

      if ( iContactHR != null ) {
         lPoHeader.setContactHr( iContactHR );
      }

      return lPoHeader.insert();
   }


   /**
    * Sets the authorization status.
    *
    * @param aAuthStatus
    *           The authorization status
    *
    * @return The builder
    */
   public OrderBuilder withAuthStatus( RefPoAuthLvlStatusKey aAuthStatus ) {

      iAuthStatus = aAuthStatus;

      return this;
   }


   /**
    * Sets the order revision number
    *
    * @param aRevisionNumber
    *
    * @return The builder
    */
   public OrderBuilder withRevisionNumber( int aRevisionNumber ) {
      iRevisionNumber = aRevisionNumber;

      return this;
   }


   /**
    * Sets the re-expedite location.
    *
    * @param aLocation
    *
    * @return The builder
    */
   public OrderBuilder reexpediteTo( LocationKey aLocation ) {
      iReexpediteLocation = aLocation;

      return this;
   }


   /**
    * Sets the exchange rate.
    *
    * @param aExchangeQty
    *           The exchange rate
    *
    * @return The builder
    */
   public OrderBuilder withExchangeRate( BigDecimal aExchangeQty ) {
      iExchangeQty = aExchangeQty;

      return this;
   }


   /**
    * Sets the issue date.
    *
    * @param aDate
    *           issue date
    *
    * @return The builder
    */
   public OrderBuilder withIssueDate( Date aDate ) {

      iIssueDt = aDate;

      return this;
   }


   /**
    * Sets the creation date.
    *
    * @param aCreationDate
    *           the creation date
    *
    * @return The builder
    */
   public OrderBuilder withCreationDate( Date aCreationDate ) {

      iCreationDt = aCreationDate;

      return this;
   }


   /**
    * Sets the order type
    *
    * @param aRefPoTypeKey
    *           order type key
    *
    * @return The builder
    */
   public OrderBuilder withOrderType( RefPoTypeKey aRefPoTypeKey ) {

      iRefPoTypeKey = aRefPoTypeKey;

      return this;
   }


   /**
    * Sets the order priority.
    *
    * @param aRefReqPriorityKey
    *           req priority key
    *
    * @return The builder
    */
   public OrderBuilder withPriority( RefReqPriorityKey aRefReqPriorityKey ) {

      iRefReqPriorityKey = aRefReqPriorityKey;

      return this;
   }


   /**
    * Sets the order status.
    *
    * @param aOrderStatus
    *           The order status
    *
    * @return The builder
    */
   public OrderBuilder withStatus( RefEventStatusKey aOrderStatus ) {
      iOrderStatus = aOrderStatus;

      return this;
   }


   /**
    * Sets the vendor
    *
    * @param aVendorKey
    *           vendor key
    *
    * @return The builder
    */
   public OrderBuilder withVendor( VendorKey aVendorKey ) {
      iVendor = aVendorKey;

      return this;
   }


   /**
    * Sets the receipt organization
    *
    * @param aOrgKey
    *           organization key
    *
    * @return The builder
    */
   public OrderBuilder withReceiptOrganization( OrgKey aOrgKey ) {
      iOrganization = aOrgKey;

      return this;
   }


   /**
    * Sets the broker
    *
    * @param aBroker
    *           broker key
    *
    * @return The builder
    */
   public OrderBuilder withBroker( VendorKey aBroker ) {
      iBroker = aBroker;

      return this;
   }


   /**
    * Sets the ship to location.
    *
    * @param aLocation
    *
    * @return The builder
    */
   public OrderBuilder shippingTo( LocationKey aLocation ) {
      iShipToLocation = aLocation;

      return this;
   }


   /**
    * Sets the currency used by the vendor.
    *
    * @param aCurrency
    *
    * @return The builder
    */
   public OrderBuilder usingCurrency( RefCurrencyKey aCurrency ) {
      iCurrency = aCurrency;

      return this;
   }


   /**
    * Sets the contact HR
    *
    * @param aContactHR
    *
    * @return The builder
    */
   public OrderBuilder withContactHR( HumanResourceKey aContactHR ) {
      iContactHR = aContactHR;

      return this;
   }


   /**
    * Sets the description.
    *
    * @param aDescription
    *
    * @return The builder
    */
   public OrderBuilder withDescription( String aDescription ) {
      iDescription = aDescription;

      return this;
   }


   /**
    *
    * Set the order to be historic
    *
    * @return the builder
    */
   public OrderBuilder asHistoric() {
      isHistoric = true;
      return this;
   }
}
