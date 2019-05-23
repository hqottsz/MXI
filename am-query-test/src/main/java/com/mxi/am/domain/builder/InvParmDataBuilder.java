
package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.InventoryParmDataKey;
import com.mxi.mx.core.key.RefDataValueKey;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.inv.InvParmData;


/**
 * Builds an <code>inv_parm_data</code> object
 */
public class InvParmDataBuilder {

   private EventKey iEventKey;
   private EventInventoryKey iEventInvKey;
   private int iDataOrd;
   private DataTypeKey iDataType;
   private RefDataValueKey iDataValueKey;
   private InventoryKey iInventoryKey;
   private Date iParmDt;
   private String iParmText;
   private String iRecEngUnitCd;
   private int iRecEngUnitDbid;
   private Double iRecParmQt;
   private String iNaNote;


   /**
    * Sets the EventInventoryKey property.
    *
    * @param EventInvKey
    *           from the evt_inv table.
    * @return the Builder
    */
   public InvParmDataBuilder withEventInvKey( EventInventoryKey aEventInvKey ) {
      iEventInvKey = aEventInvKey;
      return this;
   }


   public InvParmDataBuilder withNaNote( String aNaNote ) {

      iNaNote = aNaNote;
      return this;

   }


   /**
    * Sets a new value for the EventKey property.
    *
    * @param EventKey
    *           from evt_event
    * @return the Builder
    */
   public InvParmDataBuilder withEventKey( EventKey aEvent ) {
      iEventKey = aEvent;
      return this;
   }


   /**
    * Sets a new value for the value property.
    *
    * @param aValue
    *           the new value for the value property
    * @return the Builder
    */
   public InvParmDataBuilder withDataType( DataTypeKey aValue ) {
      iDataType = aValue;
      return this;
   }


   /**
    * Sets a new value for the dataValue property.
    *
    * @param aDataValueKey
    *           the new value for the dataValue property
    * @return the Builder
    */
   public InvParmDataBuilder withDataValue( RefDataValueKey aDataValueKey ) {
      iDataValueKey = aDataValueKey;
      return this;
   }


   /**
    * Sets a new value for the aInventoryKey property.
    *
    * @param aInventoryKey
    *           from inv_inv
    * @return the Builder
    */
   public InvParmDataBuilder withInventoryKey( InventoryKey aInventoryKey ) {
      iInventoryKey = aInventoryKey;
      return this;
   }


   /**
    * Sets a new value for the dataOrd property.
    *
    * @param aDataOrd
    *           the new value for the dataOrd property
    * @return the Builder
    */
   public InvParmDataBuilder withDataOrd( int aDataOrd ) {
      iDataOrd = aDataOrd;
      return this;
   }


   /**
    * Sets a new value for the parmDt property.
    *
    * @param aParmDt
    *           the new value for the parmDt property
    * @return the Builder
    */
   public InvParmDataBuilder withParmDt( Date aParmDt ) {
      iParmDt = aParmDt;
      return this;
   }


   /**
    * Sets a new value for the parmText property.
    *
    * @param aParmText
    *           the new value for the parmText property
    * @return the Builder
    */
   public InvParmDataBuilder withParmText( String aParmText ) {
      iParmText = aParmText;
      return this;
   }


   /**
    * Sets a new value for the recEngUnitCd property.
    *
    * @param aRecEngUnitCd
    *           the new value for the recEngUnitCd property
    * @return the Builder
    */
   public InvParmDataBuilder withRecEngUnitCd( String aRecEngUnitCd ) {
      iRecEngUnitCd = aRecEngUnitCd;
      return this;
   }


   /**
    * Sets a new value for the recEngUnitDbid property.
    *
    * @param aRecEngUnitDbid
    *           the new value for the recEngUnitDbid property
    * @return the Builder
    */
   public InvParmDataBuilder withRecEngUnitDbid( int aRecEngUnitDbid ) {
      iRecEngUnitDbid = aRecEngUnitDbid;
      return this;
   }


   /**
    * Sets a new value for the recParmQt property.
    *
    * @param aRecParmQt
    *           the new value for the recParmQt property
    * @return the Builder
    */
   public InvParmDataBuilder withRecParmQt( Double aRecParmQt ) {
      iRecParmQt = aRecParmQt;
      return this;
   }


   /**
    * Inserts the data into the evt_inv table.
    *
    * @return InventoryParmDataKey primary key.
    */
   public InventoryParmDataKey build() {
      if ( iEventInvKey == null ) {
         EvtInvTable lEntry = EvtInvTable.findByEventAndInventory( iEventKey, iInventoryKey );
         if ( lEntry != null ) {
            iEventInvKey = lEntry.getPk();
         } else {
            iEventInvKey = new EventInventoryBuilder().withEventKey( iEventKey )
                  .withInventoryKey( iInventoryKey ).build();
         }
      }
      InventoryParmDataKey lInventory =
            new InventoryParmDataKey( iEventInvKey, iDataType, iInventoryKey );
      InvParmData lTable = InvParmData.create( lInventory );
      lTable.setDataOrd( iDataOrd );
      lTable.setDataType( iDataType );
      lTable.setDataValue( iDataValueKey );
      lTable.setInvNo( iInventoryKey );
      lTable.setParmDt( iParmDt );
      lTable.setParmText( iParmText );
      lTable.setRecEngUnitCd( iRecEngUnitCd );
      lTable.setRecEngUnitDbid( iRecEngUnitDbid );
      lTable.setRecParmQt( iRecParmQt );
      lTable.setNaNote( iNaNote );

      InventoryParmDataKey lInventoryKey = lTable.insert();

      return lInventoryKey;
   }

}
