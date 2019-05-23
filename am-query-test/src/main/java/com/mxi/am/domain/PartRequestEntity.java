package com.mxi.am.domain;

import java.util.Date;

import com.mxi.mx.common.services.sequence.SequenceGeneratorFactory;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;


/**
 * Domain entity for part requests used for query testing.
 */
public class PartRequestEntity {

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

   private RefQtyUnitKey iQuantityUnit;


   public PartRequestEntity purchaseLine( PurchaseOrderLineKey aPoLineKey ) {
      iPoLineKey = aPoLineKey;
      return this;
   }


   public PurchaseOrderLineKey getPurchaseLine() {
      return iPoLineKey;
   }


   public PartRequestEntity shipmentLine( ShipmentLineKey aShipmentLineKey ) {
      iShipmentLineKey = aShipmentLineKey;
      return this;
   }


   public ShipmentLineKey getShipmentLine() {
      return iShipmentLineKey;
   }


   public PartRequestEntity partRequirement( TaskInstPartKey aPartRequirement ) {
      iPartRequirement = aPartRequirement;
      return this;
   }


   public TaskInstPartKey getPartRequirement() {
      return iPartRequirement;
   }


   public PartRequestEntity purchasePart( PartNoKey aPartNo ) {
      iPoPartNo = aPartNo;
      return this;
   }


   public PartNoKey getPurchasePart() {
      return iPoPartNo;
   }


   public PartRequestEntity specifiedPart( PartNoKey aPartNo ) {
      iSpecPartNo = aPartNo;
      return this;
   }


   public PartNoKey getSpecifiedPart() {
      return iSpecPartNo;
   }


   public PartRequestEntity reservedInventory( InventoryKey aInventory ) {
      iReservedInventory = aInventory;
      return this;
   }


   public InventoryKey getReservedInventory() {
      return iReservedInventory;
   }


   public PartRequestEntity historical() {
      iHistorical = true;
      return this;
   }


   public boolean getHistorical() {
      return iHistorical;
   }


   public PartRequestEntity task( TaskKey aTask ) {
      iTask = aTask;
      return partRequirement( new TaskInstPartKey( new TaskPartKey( aTask, 1 ), 1 ) );
   }


   public TaskKey getTask() {
      return iTask;
   }


   public PartRequestEntity neededAt( LocationKey aNeededAt ) {
      iNeededAt = aNeededAt;
      return this;
   }


   public LocationKey getNeededAt() {
      return iNeededAt;
   }


   public PartRequestEntity requiredBy( Date aDate ) {
      iReqByDate = aDate;
      return this;
   }


   public Date getRequiredBy() {
      return iReqByDate;
   }


   public PartRequestEntity priority( RefReqPriorityKey aPriority ) {
      iPriority = aPriority;
      return this;
   }


   public RefReqPriorityKey getPriority() {
      return iPriority;
   }


   public PartRequestEntity requestedQuantity( double aReqQt ) {
      iReqQt = aReqQt;
      return this;
   }


   public double getRequestedQuantity() {
      return iReqQt;
   }


   public PartRequestEntity status( RefEventStatusKey aStatus ) {
      iStatus = aStatus;
      return this;
   }


   public RefEventStatusKey getStatus() {
      return iStatus;
   }


   public PartRequestEntity type( RefReqTypeKey aType ) {
      iType = aType;
      return this;
   }


   public RefReqTypeKey getType() {
      return iType;
   }


   public PartRequestEntity reqAircraft( InventoryKey aReqAircraft ) {
      iReqAircraft = aReqAircraft;

      return this;
   }


   public InventoryKey getReqAircraft() {
      return iReqAircraft;
   }


   public PartRequestEntity partGroup( PartGroupKey aBomPart ) {
      iBomPart = aBomPart;

      return this;
   }


   public PartGroupKey getPartGroup() {
      return iBomPart;
   }


   public PartRequestEntity lastAutoRsrvDate( Date aDate ) {
      iLastAutoReservationDate = aDate;

      return this;
   }


   public Date getLastAutoRsrvDate() {
      return iLastAutoReservationDate;
   }


   public PartRequestEntity remoteLocation( LocationKey aRemoteLocation ) {
      iRemoteLoc = aRemoteLocation;
      return this;
   }


   public LocationKey getRemoteLocation() {
      return iRemoteLoc;
   }


   public PartRequestEntity estimatedArrivalDate( Date aEstimatedArrivalDate ) {
      iEstimatedArrivalDate = aEstimatedArrivalDate;

      return this;
   }


   public Date getEstimatedArrivalDate() {
      return iEstimatedArrivalDate;
   }


   public PartRequestEntity deliveryETADate( Date aDeliveryETADate ) {
      iDeliveryETADate = aDeliveryETADate;
      return this;
   }


   public Date getDeliveryETADate() {
      return iDeliveryETADate;
   }


   public String getBarcode() {
      if ( iBarcode == null ) {
         return "PR" + SequenceGeneratorFactory.getInstance().getSequenceGenerator()
               .nextValue( "PART_REQUEST_ID" );
      }
      return iBarcode;
   }


   public PartRequestEntity reqStockNo( StockNoKey aStock ) {
      iStockNo = aStock;

      return this;
   }


   public StockNoKey getReqStockNo() {
      return iStockNo;
   }


   public PartRequestEntity requestedBy( HumanResourceKey aHr ) {
      iReqHr = aHr;
      return this;
   }


   public HumanResourceKey getRequestedBy() {
      return iReqHr;
   }


   public PartRequestEntity assignedTo( HumanResourceKey aHr ) {
      iAssignHr = aHr;

      return this;
   }


   public HumanResourceKey getAssignedTo() {
      return iAssignHr;
   }


   public PartRequestEntity issueAccount( FncAccountKey aAccount ) {
      iAccount = aAccount;

      return this;
   }


   public FncAccountKey getIssueAccount() {
      return iAccount;
   }


   /**
    * Returns the value of the iQuantityUnit property.
    * 
    * @return the value of the iQuantityUnit property
    */
   public RefQtyUnitKey getQuantityUnit() {
      return iQuantityUnit;
   }


   /**
    * Sets a new value for the iQuantityUnit property.
    *
    * @param iQuantityUnit the new value for the iQuantityUnit property
    */
   public void setQuantityUnit( RefQtyUnitKey iQuantityUnit ) {
      this.iQuantityUnit = iQuantityUnit;
   }
}
