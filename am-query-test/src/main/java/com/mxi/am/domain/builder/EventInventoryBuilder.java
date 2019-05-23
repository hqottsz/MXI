package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventKeyInterface;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * builds an <code>evt_inv</code> object
 *
 */
public class EventInventoryBuilder {

   private EventKeyInterface iEventKey;
   private InventoryKey iInventoryKey;
   private AssemblyKey iAssemblyKey;
   private InventoryKey iAssemblyInvKey;
   private Double iBinQt;
   private ConfigSlotKey iConfigSlotKey;
   private ConfigSlotPositionKey iBomItemPosKey;
   private PartGroupKey iPartGroupKey;
   private ConfigSlotPositionKey iConfigSlotPos;
   private InventoryKey iHInventoryKey;
   private boolean iIsMainInv;
   private InventoryKey iNHInventoryKey;
   private PartNoKey iPartNoKey;


   /**
    * Sets a new value for the eventKey property.
    *
    * @param aEventKey
    *           the new value for the eventKey property
    */
   public EventInventoryBuilder withEventKey( EventKeyInterface aEventKey ) {
      iEventKey = aEventKey;
      return this;
   }


   /**
    * Sets a new value for the inventoryKey property.
    *
    * @param aInventoryKey
    *           the new value for the inventoryKey property
    */
   public EventInventoryBuilder withInventoryKey( InventoryKey aInventoryKey ) {
      iInventoryKey = aInventoryKey;
      InvInvTable lEntry = InvInvTable.findByPrimaryKey( aInventoryKey );
      withPartNoKey( lEntry.getPartNo() ).withAssemblyKey( lEntry.getOrigAssmbl() )
            .withBomItemKey( lEntry.getBOMItem() ).withBomItemPosKey( lEntry.getBOMItemPosition() )
            .withAssemblyInvKey( lEntry.getAssmblInvNo() ).withHInventoryKey( lEntry.getHInvNo() )
            .withNHInventoryKey( lEntry.getNhInvNo() );

      return this;
   }


   /**
    * Sets a new value for the assemblyKey property.
    *
    * @param aAssemblyKey
    *           the new value for the assemblyKey property
    */
   public EventInventoryBuilder withAssemblyKey( AssemblyKey aAssemblyKey ) {
      iAssemblyKey = aAssemblyKey;
      return this;
   }


   /**
    * Sets a new value for the assemblyInvKey property.
    *
    * @param aAssemblyInvKey
    *           the new value for the assemblyInvKey property
    */
   public EventInventoryBuilder withAssemblyInvKey( InventoryKey aAssemblyInvKey ) {
      iAssemblyInvKey = aAssemblyInvKey;
      return this;
   }


   /**
    * Sets a new value for the binQt property.
    *
    * @param aBinQt
    *           the new value for the binQt property
    */
   public EventInventoryBuilder withBinQt( Double aBinQt ) {
      iBinQt = aBinQt;
      return this;
   }


   /**
    * Sets a new value for the bomItemKey property.
    *
    * @param aConfigSlotKey
    *           the new value for the bomItemKey property
    */
   public EventInventoryBuilder withBomItemKey( ConfigSlotKey aConfigSlotKey ) {
      iConfigSlotKey = aConfigSlotKey;
      return this;
   }


   /**
    * Sets a new value for the bomItemPKey property.
    *
    * @param aBomItemPosKey
    *           the new value for the bomItemPKey property
    */
   public EventInventoryBuilder withBomItemPosKey( ConfigSlotPositionKey aBomItemPosKey ) {
      iBomItemPosKey = aBomItemPosKey;
      return this;
   }


   /**
    * Sets a new value for the bomPartKey property.
    *
    * @param aPartGroupKey
    *           the new value for the bomPartKey property
    */
   public EventInventoryBuilder withBomPartKey( PartGroupKey aPartGroupKey ) {
      iPartGroupKey = aPartGroupKey;
      return this;
   }


   /**
    * Sets a new value for the bomItemPosKey property.
    *
    * @param aConfigSlotPos
    *           the new value for the bomItemPosKey property
    */
   public EventInventoryBuilder withConfigSlotPosKey( ConfigSlotPositionKey aConfigSlotPos ) {
      iConfigSlotPos = aConfigSlotPos;
      return this;
   }


   /**
    * Sets a new value for the hInventoryKey property.
    *
    * @param aHInventoryKey
    *           the new value for the hInventoryKey property
    */
   public EventInventoryBuilder withHInventoryKey( InventoryKey aHInventoryKey ) {
      iHInventoryKey = aHInventoryKey;
      return this;
   }


   /**
    * Sets a new value for the isMainInv property.
    *
    * @param aIsMainInv
    *           the new value for the isMainInv property
    */
   public EventInventoryBuilder isMainInv( boolean aIsMainInv ) {
      iIsMainInv = aIsMainInv;
      return this;
   }


   /**
    * Sets a new value for the nHInventoryKey property.
    *
    * @param aNHInventoryKey
    *           the new value for the nHInventoryKey property
    */
   public EventInventoryBuilder withNHInventoryKey( InventoryKey aNHInventoryKey ) {
      iNHInventoryKey = aNHInventoryKey;
      return this;
   }


   /**
    * Sets a new value for the partNoKey property.
    *
    * @param aPartNoKey
    *           the new value for the partNoKey property
    */
   public EventInventoryBuilder withPartNoKey( PartNoKey aPartNoKey ) {
      iPartNoKey = aPartNoKey;
      return this;
   }


   /**
    *
    * Inserts the provided data into the evt_inv table.
    *
    * @return EventInventoryKey primary key
    */
   public EventInventoryKey build() {
      EvtInvTable lEntry = EvtInvTable.findByEventAndInventory( iEventKey, iInventoryKey );
      boolean lIsNewEntry = false;
      if ( lEntry == null ) {
         lIsNewEntry = true;
         lEntry = EvtInvTable.create( iEventKey );
      }
      lEntry.setInventoryKey( iInventoryKey );
      lEntry.setAssemblyKey( iAssemblyKey );
      lEntry.setAssmblInventoryKey( iAssemblyInvKey );
      lEntry.setBinQt( iBinQt );
      lEntry.setBomItemKey( iConfigSlotKey );
      lEntry.setBomItemPositionKey( iBomItemPosKey );
      lEntry.setBomPartKey( iPartGroupKey );
      lEntry.setConfigSlotPosition( iConfigSlotPos );
      lEntry.setHInventoryKey( iHInventoryKey );
      lEntry.setMainInvBool( iIsMainInv );
      lEntry.setNHInventoryKey( iNHInventoryKey );
      lEntry.setPartNoKey( iPartNoKey );
      if ( lIsNewEntry ) {
         lEntry.insert();
      } else {
         lEntry.update();
      }
      return lEntry.getPk();
   }
}
