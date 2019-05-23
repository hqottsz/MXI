package com.mxi.mx.common.ejb.po;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;

import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.ejb.po.PurchaseOrderBean;
import com.mxi.mx.core.ejb.po.PurchaseOrderLocal;
import com.mxi.mx.core.key.ChargeKey;
import com.mxi.mx.core.key.EventAttachmentKey;
import com.mxi.mx.core.key.EventIetmTopicKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.POLineWarrantyDefnKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RFQLineVendorKey;
import com.mxi.mx.core.key.RefBorrowRateKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefPoAuthFlowKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.TaxKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.key.WarrantyContractKey;
import com.mxi.mx.core.services.event.attach.AttachmentTO;
import com.mxi.mx.core.services.ietm.IetmDetailsTO;
import com.mxi.mx.core.services.order.ConvertOrdersTO;
import com.mxi.mx.core.services.order.EditOrderDetailsTO;
import com.mxi.mx.core.services.order.EditOrderLineTO;
import com.mxi.mx.core.services.order.MiscOrderLineTO;
import com.mxi.mx.core.services.order.OrderDetailsTO;
import com.mxi.mx.core.services.order.OrderLineTO;


/**
 * Stub from Local interface for PurchaseOrderLocal.
 *
 */
public class PurchaseOrderLocalStub implements PurchaseOrderLocal {

   private PurchaseOrderBean iPurchaseOrderBean;


   /**
    * Initialized PurchaseOrder bean to be delegated
    *
    * @param aPurchaseOrderBean
    */
   public PurchaseOrderLocalStub(PurchaseOrderBean aPurchaseOrderBean) {
      iPurchaseOrderBean = aPurchaseOrderBean;
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
   public EventAttachmentKey addAttachment( PurchaseOrderKey aPurchaseOrder,
         AttachmentTO aAttachmentTO ) throws MxException {

      return iPurchaseOrderBean.addAttachment( aPurchaseOrder, aAttachmentTO );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EventIetmTopicKey addIetm( PurchaseOrderKey aPurchaseOrder, IetmDetailsTO aIetmTO )
         throws MxException {
      return iPurchaseOrderBean.addIetm( aPurchaseOrder, aIetmTO );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PurchaseOrderLineKey addMiscLine( PurchaseOrderKey aPurchaseOrder,
         MiscOrderLineTO aMiscPOLineTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iPurchaseOrderBean.addMiscLine( aPurchaseOrder, aMiscPOLineTO, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void addNote( PurchaseOrderKey aPurchaseOrder, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      iPurchaseOrderBean.addNote( aPurchaseOrder, aNote, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void addPartLine( PurchaseOrderKey aPurchaseOrder, OrderLineTO aOrderLineTO,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.addPartLine( aPurchaseOrder, aOrderLineTO, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PurchaseOrderLineKey addXchgPartLine( PurchaseOrderKey aPurchaseOrder,
         OrderLineTO aPoDetailsTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iPurchaseOrderBean.addXchgPartLine( aPurchaseOrder, aPoDetailsTO, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void assignPartRequest( PurchaseOrderKey aPurchaseOrder, PartRequestKey[] aPartRequest,
         FncAccountKey aChargeToAccount, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iPurchaseOrderBean.assignPartRequest( aPurchaseOrder, aPartRequest, aChargeToAccount,
            aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public POLineWarrantyDefnKey assignWarrantyContract( PurchaseOrderLineKey aOrderLine,
         WarrantyContractKey aContract ) throws MxException {
      return iPurchaseOrderBean.assignWarrantyContract( aOrderLine, aContract );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void authorize( PurchaseOrderKey aPurchaseOrder, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      iPurchaseOrderBean.authorize( aPurchaseOrder, aNote, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void authorizeBudget( PurchaseOrderKey aPurchaseOrder, String aNote ) throws MxException {
      iPurchaseOrderBean.authorizeBudget( aPurchaseOrder, aNote );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public BigDecimal calculateBorrowCost( RefBorrowRateKey aBorrowRate, BigDecimal aBasePrice,
         int aNumOfDays ) throws MxException {
      return iPurchaseOrderBean.calculateBorrowCost( aBorrowRate, aBasePrice, aNumOfDays );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void cancel( PurchaseOrderKey aPOKey, HumanResourceKey aAuthorizingHr,
         RefStageReasonKey aReason, String aNote ) throws MxException, TriggerException {
      iPurchaseOrderBean.cancel( aPOKey, aAuthorizingHr, aReason, aNote );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public Date close( PurchaseOrderKey aPurchaseOrder, HumanResourceKey aAuthorizingHr,
         RefStageReasonKey aReason, String aNote ) throws MxException, TriggerException {
      return iPurchaseOrderBean.close( aPurchaseOrder, aAuthorizingHr, aReason, aNote );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void closeOrders( List<PurchaseOrderKey> aPurchaseOrders, HumanResourceKey aAuthorizingHr,
         RefStageReasonKey aReason, String aNote ) throws MxException, TriggerException {
      iPurchaseOrderBean.closeOrders( aPurchaseOrders, aAuthorizingHr, aReason, aNote );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void convertBorrowToPurchase( PurchaseOrderKey aPurchaseOrder,
         ConvertOrdersTO aConvertOrdersTO, RefStageReasonKey aReason, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.convertBorrowToPurchase( aPurchaseOrder, aConvertOrdersTO, aReason, aNote,
            aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void convertBorrowToXchg( PurchaseOrderKey aPurchaseOrder,
         ConvertOrdersTO aConvertOrdersTO, RefStageReasonKey aReason, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.convertBorrowToXchg( aPurchaseOrder, aConvertOrdersTO, aReason, aNote,
            aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void convertPurchaseToXchg( PurchaseOrderKey aPurchaseOrder,
         List<ConvertOrdersTO> aConvertOrdersTOs, RefStageReasonKey aReason, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.convertPurchaseToXchg( aPurchaseOrder, aConvertOrdersTOs, aReason, aNote,
            aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void convertRepairToXchg( PurchaseOrderKey aPurchaseOrder,
         List<ConvertOrdersTO> aConvertOrdersTO, RefStageReasonKey aReason, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.convertRepairToXchg( aPurchaseOrder, aConvertOrdersTO, aReason, aNote,
            aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void convertXchgToPurchase( PurchaseOrderKey aPurchaseOrder,
         List<ConvertOrdersTO> aConvertOrdersTOs, RefStageReasonKey aReason, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.convertXchgToPurchase( aPurchaseOrder, aConvertOrdersTOs, aReason, aNote,
            aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PurchaseOrderKey create( OrderDetailsTO aPODetailsTO, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      return iPurchaseOrderBean.create( aPODetailsTO, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PurchaseOrderKey create( OrderDetailsTO aPODetailsTO, PartRequestKey[] aPurchaseRequest,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      return iPurchaseOrderBean.create( aPODetailsTO, aPurchaseRequest, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PurchaseOrderKey create( OrderDetailsTO aPODetailsTO, RFQLineVendorKey[] aRFQLineVendors,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      return iPurchaseOrderBean.create( aPODetailsTO, aRFQLineVendors, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EditOrderDetailsTO get( PurchaseOrderKey aPurchaseOrder ) throws MxException {
      return iPurchaseOrderBean.get( aPurchaseOrder );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isPendingInvoicesExist( List<PurchaseOrderKey> aPurchaseOrders )
         throws MxException {
      return iPurchaseOrderBean.isPendingInvoicesExist( aPurchaseOrders );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isPendingShipmentsExist( List<PurchaseOrderKey> aPurchaseOrders )
         throws MxException {
      return iPurchaseOrderBean.isPendingShipmentsExist( aPurchaseOrders );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public ShipmentKey issue( PurchaseOrderKey aPurchaseOrder, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      return iPurchaseOrderBean.issue( aPurchaseOrder, aNote, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void markNoWarranty( PurchaseOrderLineKey[] aOrderLine ) throws MxException {
      iPurchaseOrderBean.markNoWarranty( aOrderLine );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void overrideAuthorization( PurchaseOrderKey aPurchaseOrder, String aNote,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      iPurchaseOrderBean.overrideAuthorization( aPurchaseOrder, aNote, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void overrideBudgetRejection( PurchaseOrderKey aPurchaseOrder,
         HumanResourceKey aAuthorizingHr, String aNote ) throws MxException {
      iPurchaseOrderBean.overrideBudgetRejection( aPurchaseOrder, aAuthorizingHr, aNote );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void reject( PurchaseOrderKey aPurchaseOrder, HumanResourceKey aAuthorizingHr,
         String aNote ) throws MxException {
      iPurchaseOrderBean.reject( aPurchaseOrder, aAuthorizingHr, aNote );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void rejectBudget( PurchaseOrderKey aPurchaseOrder, String aNote ) throws MxException {
      iPurchaseOrderBean.rejectBudget( aPurchaseOrder, aNote );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void removeAttachment( PurchaseOrderKey aPurchaseOrder,
         EventAttachmentKey[] aEventAttachment ) throws MxException {
      iPurchaseOrderBean.removeAttachment( aPurchaseOrder, aEventAttachment );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void removeIetm( PurchaseOrderKey aPurchaseOrder, EventIetmTopicKey[] aIetmTopic )
         throws MxException {
      iPurchaseOrderBean.removeIetm( aPurchaseOrder, aIetmTopic );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void removePOLines( PurchaseOrderKey aPurchaseOrder,
         PurchaseOrderLineKey[] aPurchaseOrderLines, HumanResourceKey aAuthorizingHr )
         throws MxException, TriggerException {
      iPurchaseOrderBean.removePOLines( aPurchaseOrder, aPurchaseOrderLines, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void reOpen( PurchaseOrderKey aPurchaseOrder, RefEventStatusKey aNewStatus,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.reOpen( aPurchaseOrder, aNewStatus, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void requestAuthorization( PurchaseOrderKey aPurchaseOrder, RefPoAuthFlowKey aPoAuthFlow,
         String aNote, HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.requestAuthorization( aPurchaseOrder, aPoAuthFlow, aNote, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void set( PurchaseOrderKey aPurchaseOrder, EditOrderDetailsTO aEditPODetailsTO,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.set( aPurchaseOrder, aEditPODetailsTO, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setCharges( PurchaseOrderKey aPurchaseOrder, Collection<ChargeKey> aChargeKeys )
         throws MxException, TriggerException {
      iPurchaseOrderBean.setCharges( aPurchaseOrder, aChargeKeys );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setPOLines( PurchaseOrderKey aPurchaseOrder, EditOrderLineTO[] aEditPOLines,
         HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.setPOLines( aPurchaseOrder, aEditPOLines, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setReceiverNote( PurchaseOrderLineKey aPurchaseOrderLine, String aReceiverNote )
         throws MxException {
      iPurchaseOrderBean.setReceiverNote( aPurchaseOrderLine, aReceiverNote );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setReturnConsignInventory( TransferKey aTransfer, InventoryKey aReturnInventory ) {
      iPurchaseOrderBean.setReturnConsignInventory( aTransfer, aReturnInventory );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setReturnInventory( PurchaseOrderLineKey aPoLine, InventoryKey aReturnInv,
         HumanResourceKey aHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.setReturnInventory( aPoLine, aReturnInv, aHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setTaxes( PurchaseOrderKey aPurchaseOrder, Collection<TaxKey> aTaxKeys )
         throws MxException {
      iPurchaseOrderBean.setTaxes( aPurchaseOrder, aTaxKeys );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setVendorNote( PurchaseOrderLineKey aPurchaseOrderLine, String aVendorNote )
         throws MxException {
      iPurchaseOrderBean.setVendorNote( aPurchaseOrderLine, aVendorNote );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void unassignPartRequest( PurchaseOrderKey aPurchaseOrder, PartRequestKey[] aPartRequest,
         HumanResourceKey aAuthorizingHr ) throws MxException {
      iPurchaseOrderBean.unassignPartRequest( aPurchaseOrder, aPartRequest, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void unassignWarrantyContract( PurchaseOrderLineKey aOrderLine ) throws MxException {
      iPurchaseOrderBean.unassignWarrantyContract( aOrderLine );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void unauthorize( PurchaseOrderKey aPurchaseOrder, RefStageReasonKey aReason,
         String aNote, HumanResourceKey aAuthorizingHr ) throws MxException, TriggerException {
      iPurchaseOrderBean.unauthorize( aPurchaseOrder, aReason, aNote, aAuthorizingHr );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void validateOrderLine( PurchaseOrderKey aPurchaseOrder, PartNoKey aPartNoKey,
         RefPoLineTypeKey aPoLineType ) throws MxException {
      iPurchaseOrderBean.validateOrderLine( aPurchaseOrder, aPartNoKey, aPoLineType );
   }
}
