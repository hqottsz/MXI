package com.mxi.mx.core.services.inventory.reservation;

import java.util.HashSet;
import java.util.LinkedList;

import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.eqp.EqpBomPart;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.trigger.inventory.MxInventoryAutoReservation;
import com.mxi.mx.core.unittest.table.eqp.EqpPartNo;


/**
 * An implementation of MxInventoryAutoReservation that uses an in-memory autoreservation request
 * queue. Emulates AutoReservationRequestHandler. Use only for testing.
 *
 */
public class AutoReservationRequestHandlerFake implements MxInventoryAutoReservation {

   // The set of reservation requests already in the list. Don't add if already in the set
   private HashSet<SimpleAutoReservationRequest> lHash = new HashSet<>();

   // The list of reservation requests processing or still to process
   private LinkedList<SimpleAutoReservationRequest> lList = new LinkedList<>();


   /**
    * Create an auto-reservation request and then iterate over the queue until it is empty. This
    * method can recurse, for example if an inventory is not found in the where needed supply
    * location it will search the hub location. If we enter this method through recursion don't
    * start a new loop, just add to the queue, and the existing loop will find it. *
    */
   private void createAutoReservationRequest( PartNoKey aPartNoKey, PartGroupKey aPartGroupKey,
         LocationKey aLocationKey ) throws TriggerException {

      SimpleAutoReservationRequest lNewRequest =
            new SimpleAutoReservationRequest( aPartNoKey, aPartGroupKey, aLocationKey );

      // If already processing, add to the queue
      if ( !lList.isEmpty() ) {
         lList.add( lNewRequest );
         lHash.add( lNewRequest );
      } else {
         lList.add( lNewRequest );
         lHash.add( lNewRequest );
         while ( !lList.isEmpty() ) {
            SimpleAutoReservationRequest lNextRequest = lList.peek();
            // remove from the hash, so that the same request can be readded during this call
            lHash.remove( lNextRequest );

            getProperReservationService( lNextRequest ).autoReserveInventory(
                  lNextRequest.getPartGroupKey(), lNextRequest.getPartNoKey(),
                  lNextRequest.getLocationKey() );

            // don't remove from the list until after the call, so that this loop will continue
            // if the call recurses
            lList.pop();
         }

      }
   }


   /*
    * Returns either the Serialized or Batch AutoReservationService depending of the inventory class
    * requested.
    */
   private AbstractAutoReservationService
         getProperReservationService( SimpleAutoReservationRequest aRequest ) {
      RefInvClassKey lInvClass;
      PartNoKey lSpecPartNoKey = aRequest.getPartNoKey();
      PartGroupKey lPartGroupKey = aRequest.getPartGroupKey();

      if ( lSpecPartNoKey != null ) {
         lInvClass = EqpPartNo.findByPrimaryKey( lSpecPartNoKey ).getInvClass();
      } else {
         lInvClass = EqpBomPart.findByPrimaryKey( lPartGroupKey ).getInvClass();
      }

      if ( lInvClass.isSerialized() ) {
         return new SerializedAutoReservationService();
      } else {
         return new BatchAutoReservationService();
      }

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void autoReserveInventory( PartRequestKey aPartRequestKey ) throws TriggerException {
      // TODO Auto-generated method stub

      ReqPartTable lReqPart = ReqPartTable.findByPrimaryKey( aPartRequestKey );
      createAutoReservationRequest( lReqPart.getSpecPartNo(), lReqPart.getBomPart(),
            lReqPart.getReqLoc() );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void autoReserveInventory( PartGroupKey aPartGroupKey, LocationKey aLocationKey )
         throws TriggerException {
      createAutoReservationRequest( null, aPartGroupKey, aLocationKey );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void autoReserveInventory( PartNoKey aPartNoKey, LocationKey aLocationKey )
         throws TriggerException {
      createAutoReservationRequest( aPartNoKey, null, aLocationKey );

   }


   // A simple representation of the auto-reservation queue. Compare to
   // com.mxi.mx.model.inventory.reservation.AutoReservationRequest
   private class SimpleAutoReservationRequest {

      PartNoKey iPartNoKey;
      PartGroupKey iPartGroupKey;
      LocationKey iLocationKey;


      SimpleAutoReservationRequest(PartNoKey aPartNoKey, PartGroupKey aPartGroupKey,
            LocationKey aLocationKey) {
         iPartNoKey = aPartNoKey;
         iPartGroupKey = aPartGroupKey;
         iLocationKey = aLocationKey;
      }


      public PartGroupKey getPartGroupKey() {
         return iPartGroupKey;
      }


      public PartNoKey getPartNoKey() {
         return iPartNoKey;
      }


      public LocationKey getLocationKey() {
         return iLocationKey;
      }

   }
}
