package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;


/**
 * Domain shipment entity used for query testing.
 *
 */
public class Shipment {

   private PurchaseOrderKey purchaseOrder;
   private RefEventStatusKey status;
   private RefShipmentTypeKey type;
   private Date shipByDate;
   private Date shipmentDate;
   private Boolean isHistorical;
   private String waybillNumber;

   private List<DomainConfiguration<ShipmentSegment>> shipmentSegments = new ArrayList<>();
   private List<DomainConfiguration<ShipmentLine>> shipmentLines = new ArrayList<>();


   public Shipment setPurchaseOrder( PurchaseOrderKey purchaseOrder ) {
      this.purchaseOrder = purchaseOrder;
      return this;
   }


   public PurchaseOrderKey getPurchaseOrder() {
      return purchaseOrder;
   }


   public Shipment setStatus( RefEventStatusKey status ) {
      this.status = status;
      return this;
   }


   public Optional<RefEventStatusKey> getStatus() {
      return Optional.ofNullable( status );
   }


   public Shipment setType( RefShipmentTypeKey type ) {
      this.type = type;
      return this;
   }


   public Optional<RefShipmentTypeKey> getType() {
      return Optional.ofNullable( type );
   }


   public Shipment setShipByDate( Date shipByDate ) {
      this.shipByDate = shipByDate;
      return this;
   }


   public Date getShipByDate() {
      return shipByDate;
   }


   public Shipment setShipmentDate( Date shipmentDate ) {
      this.shipmentDate = shipmentDate;
      return this;
   }


   public Date getShipmentDate() {
      return shipmentDate;
   }


   public void setHistorical( boolean isHistorical ) {
      this.isHistorical = isHistorical;
   }


   public Optional<Boolean> getHistorical() {
      return Optional.ofNullable( isHistorical );
   }


   public void setWaybillNumber( String waybillNumber ) {
      this.waybillNumber = waybillNumber;
   }


   public String getWaybillNumber() {
      return waybillNumber;
   }


   public Shipment addShipmentSegment( DomainConfiguration<ShipmentSegment> segment ) {
      shipmentSegments.add( segment );
      return this;
   }


   public List<DomainConfiguration<ShipmentSegment>> getSegmentConfigurations() {
      return shipmentSegments;
   }


   public Shipment addShipmentLine( DomainConfiguration<ShipmentLine> line ) {
      shipmentLines.add( line );
      return this;
   }


   public List<DomainConfiguration<ShipmentLine>> getLineConfigurations() {
      return shipmentLines;
   }

}
