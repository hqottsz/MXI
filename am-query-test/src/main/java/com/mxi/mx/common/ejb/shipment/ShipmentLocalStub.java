package com.mxi.mx.common.ejb.shipment;

import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;

import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.ejb.shipment.ShipmentBean;
import com.mxi.mx.core.ejb.shipment.ShipmentLocal;
import com.mxi.mx.core.key.EventAttachmentKey;
import com.mxi.mx.core.key.EventIetmTopicKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.services.event.attach.AttachmentTO;
import com.mxi.mx.core.services.ietm.IetmDetailsTO;
import com.mxi.mx.core.services.inventory.MxMoveInventoryWarning;
import com.mxi.mx.core.services.shipment.PickShipmentTO;
import com.mxi.mx.core.services.shipment.ReceiveResultsTO;
import com.mxi.mx.core.services.shipment.ReceiveShipmentLineTO;
import com.mxi.mx.core.services.shipment.SendShipmentTO;
import com.mxi.mx.core.services.shipment.ShipmentTO;
import com.mxi.mx.core.services.shipment.WaybillGroupTO;
import com.mxi.mx.core.services.shipment.message.MessageReceivedPart;


/**
 * Stub from Local interface for ShipmentLocal.
 *
 */
public class ShipmentLocalStub implements ShipmentLocal {

   private ShipmentBean iShipmentBean;


   /**
    * Initialized shipment bean to be delegated
    *
    * @param aShipmentBean
    */
   public ShipmentLocalStub(ShipmentBean aShipmentBean) {
      iShipmentBean = aShipmentBean;
   }


   /**
    * no-op method. Not currently required.
    */
   @Override
   public EJBLocalHome getEJBLocalHome() throws EJBException {
      return null;
   }


   /**
    * no-op method. Not currently required.
    */
   @Override
   public Object getPrimaryKey() throws EJBException {
      return null;
   }


   /**
    * no-op method. Not currently required.
    */
   @Override
   public boolean isIdentical( EJBLocalObject aArg0 ) throws EJBException {
      return false;
   }


   /**
    * no-op method. Not currently required.
    */
   @Override
   public void remove() throws RemoveException, EJBException {

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EventAttachmentKey addAttachment( ShipmentKey aShipment, AttachmentTO aAttachmentTO )
         throws MxException {
      return iShipmentBean.addAttachment( aShipment, aAttachmentTO );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EventIetmTopicKey addIetm( ShipmentKey aShipment, IetmDetailsTO aIetmTO )
         throws MxException {
      return iShipmentBean.addIetm( aShipment, aIetmTO );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void addNote( ShipmentKey aShipment, String aNote, HumanResourceKey aAuthorizingHr )
         throws MxException {
      iShipmentBean.addNote( aShipment, aNote, aAuthorizingHr );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public List<ShipmentLineKey> addShipmentLine( ShipmentKey aShipment, InventoryKey aInventory,
         Double aExpectedQty ) throws MxException, TriggerException {
      return iShipmentBean.addShipmentLine( aShipment, aInventory, aExpectedQty );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public List<ShipmentLineKey> addShipmentLine( ShipmentKey aShipment, String aPartNoOEM,
         String aSerialNoOEM, String aManufacturerCd, String aBarcode, String aCondition,
         Double aExpectedQty, String aMpKeySdesc ) throws MxException, TriggerException {
      return iShipmentBean.addShipmentLine( aShipment, aPartNoOEM, aSerialNoOEM, aManufacturerCd,
            aBarcode, aCondition, aExpectedQty, aMpKeySdesc );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public List<ShipmentLineKey> addShipmentLine( ShipmentKey aShipment, String aPartNoOEM,
         String aSerialNoOEM, String aManufacturerCd, String aBarcode, String aCondition,
         Double aExpectedQty ) throws MxException, TriggerException {
      return iShipmentBean.addShipmentLine( aShipment, aPartNoOEM, aSerialNoOEM, aManufacturerCd,
            aBarcode, aCondition, aExpectedQty );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void assignToWaybillGroup( String aWaybill, ShipmentKey[] aShipments )
         throws MxException {
      iShipmentBean.assignToWaybillGroup( aWaybill, aShipments );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void cancel( ShipmentKey aShipment, HumanResourceKey aAuthorizingHr, String aReason,
         String aNote ) throws MxException, TriggerException {
      iShipmentBean.cancel( aShipment, aAuthorizingHr, aReason, aNote );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void confirmShipmentReceipt( ShipmentKey aShipment ) throws MxException {
      iShipmentBean.confirmShipmentReceipt( aShipment );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ShipmentKey create( ShipmentTO aShipmentTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      ShipmentKey lShipmentKey = iShipmentBean.create( aShipmentTO, aAuthorizingHr );
      return lShipmentKey;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ShipmentTO get( ShipmentKey aShipment ) throws MxException {
      return iShipmentBean.get( aShipment );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public MessageReceivedPart[] getReceiveWarnings( ShipmentKey aShipment,
         ReceiveShipmentLineTO[] aReceiveShipmentLineTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iShipmentBean.getReceiveWarnings( aShipment, aReceiveShipmentLineTO, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void moveShipmentLineInventory( ShipmentLineKey[] aShipmentLine )
         throws MxException, TriggerException {
      iShipmentBean.moveShipmentLineInventory( aShipmentLine );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void pickShipment( PickShipmentTO aPickShipmentTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iShipmentBean.pickShipment( aPickShipmentTO, aAuthorizingHr );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ReceiveResultsTO receive( ShipmentKey aShipment, Date aReceivedDate,
         ReceiveShipmentLineTO[] aReceiveShipmentLineTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iShipmentBean.receive( aShipment, aReceivedDate, aReceiveShipmentLineTO,
            aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void removeAttachment( ShipmentKey aShipment, EventAttachmentKey[] aEventAttachment )
         throws MxException {
      iShipmentBean.removeAttachment( aShipment, aEventAttachment );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void removeIetm( ShipmentKey aShipment, EventIetmTopicKey[] aIetmTopic )
         throws MxException {
      iShipmentBean.removeIetm( aShipment, aIetmTopic );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void removeShipmentLine( ShipmentLineKey[] aShipmentLine )
         throws MxException, TriggerException {
      iShipmentBean.removeShipmentLine( aShipmentLine );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void send( ShipmentKey aShipment, HumanResourceKey aAuthorizingHr, Date aShipmentDate,
         Date aReceivedDate, String aNote ) throws MxException, TriggerException {
      iShipmentBean.send( aShipment, aAuthorizingHr, aShipmentDate, aReceivedDate, aNote );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void set( ShipmentKey aShipmentKey, ShipmentTO aShipmentTO,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iShipmentBean.set( aShipmentKey, aShipmentTO, aAuthorizingHr );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setLineNotes( ShipmentLineKey aShipmentLine, String aNotes ) throws MxException {
      iShipmentBean.setLineNotes( aShipmentLine, aNotes );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void unassignFromWaybillGroup( ShipmentKey[] aShipments ) throws MxException {
      iShipmentBean.unassignFromWaybillGroup( aShipments );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void unpickShipmentLines( ShipmentLineKey[] aShipmentLines )
         throws MxException, TriggerException {
      iShipmentBean.unpickShipmentLines( aShipmentLines );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void updateAndSend( ShipmentKey aShipment, SendShipmentTO aTO )
         throws MxException, TriggerException {
      iShipmentBean.updateAndSend( aShipment, aTO );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void updateWaybillDetails( WaybillGroupTO aWaybillTO )
         throws MxException, TriggerException {
      iShipmentBean.updateWaybillDetails( aWaybillTO );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PartNoKey[] validatePart( String aPartNoOem, String aSerialNo, String aManufacturerCode )
         throws MxException {
      return iShipmentBean.validatePart( aPartNoOem, aSerialNo, aManufacturerCode );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public List<MxMoveInventoryWarning> validatePickedItem( String aShipmentBarcode,
         PartNoKey aPartNo, String aSerialNo ) throws MxException {
      return iShipmentBean.validatePickedItem( aShipmentBarcode, aPartNo, aSerialNo );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public List<MxMoveInventoryWarning> validatePickedItem( ShipmentKey aShipmentKey,
         PartNoKey aPartNo, String aSerialNo ) {
      return iShipmentBean.validatePickedItem( aShipmentKey, aPartNo, aSerialNo );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public InventoryKey validatePutAwayInventory( ShipmentLineKey[] aShipmentLines )
         throws MxException {
      return iShipmentBean.validatePutAwayInventory( aShipmentLines );
   }

}
