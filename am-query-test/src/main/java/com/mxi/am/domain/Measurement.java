package com.mxi.am.domain;

import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Domain class for Measurement entity
 *
 */
public class Measurement {

   private int iOrder;
   private InventoryKey iInventory;
   private DataTypeKey iDataType;
   private String iNaNote;


   /**
    * Returns the value of the naNote property.
    *
    * @return the value of the naNote property
    */
   public String getNaNote() {
      return iNaNote;
   }


   /**
    * Sets a new value for the naNote property.
    *
    * @param aNaNote
    *           the new value for the naNote property
    */
   public void setNaNote( String aNaNote ) {
      iNaNote = aNaNote;
   }


   public int getOrder() {
      return iOrder;
   }


   public void setOrder( int aOrder ) {
      iOrder = aOrder;
   }


   public InventoryKey getInventory() {
      return iInventory;
   }


   public void setInventory( InventoryKey aInventory ) {
      iInventory = aInventory;
   }


   public DataTypeKey getDataType() {
      return iDataType;
   }


   public void setDataType( DataTypeKey aDataType ) {
      iDataType = aDataType;
   }

}
