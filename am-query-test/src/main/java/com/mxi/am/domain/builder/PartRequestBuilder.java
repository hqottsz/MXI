package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.common.services.sequence.SequenceGeneratorFactory;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefSupplyChainKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * Builder for Part Requests
 */
public class PartRequestBuilder implements DomainBuilder<PartRequestKey> {

   private LocationKey iNeededAt;

   private TaskInstPartKey iPartRequirement;

   private Date iReqByDate;

   private double iReqQt;

   private RefEventStatusKey iStatus = RefEventStatusKey.PROPEN;

   private RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;

   private RefReqTypeKey iType;

   private InventoryKey iReservedInventory;

   private boolean iHistorical = false;

   private PartNoKey iPoPartNo;

   private PartNoKey iSpecPartNo;

   private PurchaseOrderLineKey iPoLineKey;

   private ShipmentLineKey iShipmentLineKey;

   private InventoryKey iReqAircraft;

   private PartGroupKey iBomPart;

   private Date iLastAutoReservationDate;

   private LocationKey iRemoteLoc;

   private TaskKey iTask;

   private String iBarcode;

   private Date iEstimatedArrivalDate;

   private Date iDeliveryETADate;

   private StockNoKey iStockNo;

   private HumanResourceKey iReqHr;

   private HumanResourceKey iAssignHr;

   private FncAccountKey iAccount;

   private boolean iLockedReservation = false;

   private RefQtyUnitKey iQtyUnitKey;
   private Date iLastPrintedDate;


   /**
    * {@inheritDoc}
    */
   @Override
   public PartRequestKey build() {

      String lPartRequestBarcode = getBarcode();

      EventBuilder lEventBuilder = new EventBuilder().withStatus( iStatus )
            .withType( RefEventTypeKey.PR ).withDescription( lPartRequestBarcode );

      if ( iHistorical ) {
         lEventBuilder.asHistoric();
      }

      EventKey lEventKey = lEventBuilder.build();

      ReqPartTable lReqPart =
            ReqPartTable.create( new PartRequestKey( lEventKey.getDbId(), lEventKey.getId() ) );

      lReqPart.setInstalledPart( iPartRequirement );
      lReqPart.setPrSched( iTask );
      lReqPart.setReqByDt( iReqByDate );
      lReqPart.setReqQt( iReqQt );
      lReqPart.setReqType( iType );
      lReqPart.setReqLocation( iNeededAt );
      lReqPart.setSupplyChain( RefSupplyChainKey.DEFAULT );
      lReqPart.setInventory( iReservedInventory );
      lReqPart.setPoPartNo( iPoPartNo );
      lReqPart.setReqSpecPartNo( iSpecPartNo );
      lReqPart.setPOLine( iPoLineKey );
      lReqPart.setShipmentLine( iShipmentLineKey );
      lReqPart.setReqAircraft( iReqAircraft );
      lReqPart.setReqBomPart( iBomPart );
      lReqPart.setReqPriority( iPriority );
      lReqPart.setLastAutoRsrvDt( iLastAutoReservationDate );
      lReqPart.setRemoteLoc( iRemoteLoc );
      lReqPart.setReqMasterId( lPartRequestBarcode );
      lReqPart.setEstArrivalDt( iEstimatedArrivalDate );
      lReqPart.setUpdatedETA( iDeliveryETADate );
      lReqPart.setReqStockNo( iStockNo );
      lReqPart.setReqHr( iReqHr );
      lReqPart.setAssignHr( iAssignHr );
      lReqPart.setIssueAccount( iAccount );
      lReqPart.setLockReserveBool( iLockedReservation );
      lReqPart.setQuantityUnit( iQtyUnitKey );
      lReqPart.setPrintedDt( iLastPrintedDate );

      return lReqPart.insert();
   }


   /**
    * The part request will be associated with the given purchase order line
    *
    * @param aPoLineKey
    *           The po line associated with this part request
    *
    * @return The builder
    */
   public PartRequestBuilder forPurchaseLine( PurchaseOrderLineKey aPoLineKey ) {
      iPoLineKey = aPoLineKey;

      return this;
   }


   /**
    * The part request will be associated with the given shipment line
    *
    * @param aShipmentLineKey
    *           The shipment line associated with this part request
    *
    * @return The builder
    */
   public PartRequestBuilder forShipmentLine( ShipmentLineKey aShipmentLineKey ) {
      iShipmentLineKey = aShipmentLineKey;

      return this;
   }


   /**
    * The part request will be associated with the given part requirement.
    *
    * @param aPartRequirement
    *           The part requirement
    *
    * @return The builder
    */
   public PartRequestBuilder forPartRequirement( TaskInstPartKey aPartRequirement ) {
      iPartRequirement = aPartRequirement;

      return this;
   }


   /**
    * The part request will be associated with the given part
    *
    * @param aPartNo
    *           The part requested or to be purchases
    *
    * @return The builder
    */
   public PartRequestBuilder forPurchasePart( PartNoKey aPartNo ) {
      iPoPartNo = aPartNo;

      return this;
   }


   /**
    * The part request will be associated with the given specified part
    *
    * @param aPartNo
    *           The part specified
    *
    * @return The builder
    */
   public PartRequestBuilder forSpecifiedPart( PartNoKey aPartNo ) {
      iSpecPartNo = aPartNo;

      return this;
   }


   /**
    * Sets the reserved inventory
    *
    * @param aInventory
    *           reserved inventory
    *
    * @return the builder
    */
   public PartRequestBuilder withReservedInventory( InventoryKey aInventory ) {
      iReservedInventory = aInventory;

      return this;
   }


   /**
    * Sets the part request as historical
    *
    * @return the builder
    */
   public PartRequestBuilder asHistorical() {
      iHistorical = true;

      return this;
   }


   /**
    * The part request will be associated with the given task.
    *
    * @param aTask
    *           The task
    *
    * @return The builder
    */
   public PartRequestBuilder forTask( TaskKey aTask ) {
      iTask = aTask;
      return forPartRequirement( new TaskInstPartKey( new TaskPartKey( aTask, 1 ), 1 ) );
   }


   /**
    * Sets the needed location
    *
    * @param aNeededAt
    *           The location
    *
    * @return The builder
    */
   public PartRequestBuilder isNeededAt( LocationKey aNeededAt ) {
      iNeededAt = aNeededAt;

      return this;
   }


   /**
    * Sets the required by date.
    *
    * @param aDate
    *           The date
    *
    * @return The builder
    */
   public PartRequestBuilder requiredBy( Date aDate ) {
      iReqByDate = aDate;

      return this;
   }


   /**
    * Sets the priority of the part request.
    *
    * @param aPriority
    *           The priority.
    *
    * @return The builder
    */
   public PartRequestBuilder withPriority( RefReqPriorityKey aPriority ) {
      iPriority = aPriority;

      return this;
   }


   /**
    * Sets the requested quantity.
    *
    * @param aReqQt
    *           The requested quantity.
    *
    * @return The builder
    */
   public PartRequestBuilder withRequestedQuantity( double aReqQt ) {
      iReqQt = aReqQt;

      return this;
   }


   /**
    * Sets the status.
    *
    * @param aStatus
    *           The status
    *
    * @return The builder
    */
   public PartRequestBuilder withStatus( RefEventStatusKey aStatus ) {
      iStatus = aStatus;

      return this;
   }


   /**
    * Sets the request type.
    *
    * @param aType
    *           The type
    *
    * @return The builder
    */
   public PartRequestBuilder withType( RefReqTypeKey aType ) {
      iType = aType;

      return this;
   }


   /**
    * Sets the aircraft req
    *
    * @param aReqAircraft
    *           Part request on the aircraft
    *
    * @return The builder
    */
   public PartRequestBuilder withReqAircraft( InventoryKey aReqAircraft ) {
      iReqAircraft = aReqAircraft;

      return this;
   }


   /**
    * Sets a new value for the bomPart property.
    *
    * @param aBomPart
    *           the new value for the bomPart property
    */
   public PartRequestBuilder forPartGroup( PartGroupKey aBomPart ) {
      iBomPart = aBomPart;

      return this;
   }


   /**
    *
    * Sets the last auto-reservation date to mimic this part request having gone through
    * auto-reservation.
    *
    * @param aDate
    * @return The builder
    */
   public PartRequestBuilder withLastAutoRsrvDate( Date aDate ) {
      iLastAutoReservationDate = aDate;

      return this;
   }


   /**
    *
    * Sets the last printed date
    *
    * @param aDate
    * @return The builder
    */
   public PartRequestBuilder withPrintedDate( Date aDate ) {
      iLastPrintedDate = aDate;

      return this;
   }


   /**
    * Sets the remote location.
    *
    * @param aRemoteLocation
    * @return the builder
    */
   public PartRequestBuilder withRemoteLocation( LocationKey aRemoteLocation ) {
      iRemoteLoc = aRemoteLocation;

      return this;
   }


   /**
    *
    * Sets the estimated arrival date.
    *
    * @param aEstimatedArrivalDate
    * @return the builder
    */
   public PartRequestBuilder withEstimatedArrivalDate( Date aEstimatedArrivalDate ) {
      iEstimatedArrivalDate = aEstimatedArrivalDate;

      return this;
   }


   public PartRequestBuilder withDeliveryETADate( Date aDeliveryETADate ) {
      iDeliveryETADate = aDeliveryETADate;

      return this;
   }


   /**
    *
    * Sets the unit of measure of the part request
    *
    * @param aQtyUnitKey
    *           the RefQtyUnitKey
    * @return the builder
    */
   public PartRequestBuilder withQuantityUnit( RefQtyUnitKey aQtyUnitKey ) {
      iQtyUnitKey = aQtyUnitKey;

      return this;
   }


   /**
    * Gets the part request barcode, will generate a new one if no barcode is set.
    *
    * @return the barcode
    */
   private String getBarcode() {
      if ( iBarcode == null ) {
         return "PR" + SequenceGeneratorFactory.getInstance().getSequenceGenerator()
               .nextValue( "PART_REQUEST_ID" );
      }
      return iBarcode;
   }


   /**
    * Sets the stock
    *
    * @param aStock
    *           required stock
    *
    * @return the builder
    */
   public PartRequestBuilder withReqStockNo( StockNoKey aStock ) {
      iStockNo = aStock;

      return this;
   }


   /**
    * Sets the request by
    *
    * @param aHr
    *           the hr
    *
    * @return the builder
    */
   public PartRequestBuilder requestedBy( HumanResourceKey aHr ) {
      iReqHr = aHr;

      return this;
   }


   /**
    * Sets the assigned to hr
    *
    * @param aHr
    *           the hr
    *
    * @return the builder
    */
   public PartRequestBuilder assignedTo( HumanResourceKey aHr ) {
      iAssignHr = aHr;

      return this;
   }


   /**
    * Sets the issue account
    *
    * @param aAccount
    *           the account
    *
    * @return the builder
    */
   public PartRequestBuilder withIssueAccount( FncAccountKey aAccount ) {
      iAccount = aAccount;

      return this;
   }


   /**
    * Sets a new value for the locked reservation property.
    *
    * @param aLockedReservation
    *           the new value for the locked reservation property
    */
   public PartRequestBuilder isLockedReservation() {
      iLockedReservation = true;

      return this;
   }

}
