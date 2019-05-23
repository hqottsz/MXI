package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is store mm_data_type information (type_db_id and type_id) which equivalent to
 * mim_data_type table
 *
 */
public class mim_data_type {

   String DATA_TYPE_DB_ID;
   String DATA_TYPE_ID;


   public mim_data_type(String DATA_TYPE_DB_ID, String DATA_TYPE_ID) {
      this.DATA_TYPE_DB_ID = DATA_TYPE_DB_ID;
      this.DATA_TYPE_ID = DATA_TYPE_ID;

   }


   /**
    * Returns the value of the dATA_TYPE_DB_ID property.
    *
    * @return the value of the dATA_TYPE_DB_ID property
    */
   public String getDATA_TYPE_DB_ID() {
      return DATA_TYPE_DB_ID;
   }


   /**
    * Sets a new value for the dATA_TYPE_DB_ID property.
    *
    * @param aDATA_TYPE_DB_ID
    *           the new value for the dATA_TYPE_DB_ID property
    */
   public void setDATA_TYPE_DB_ID( String aDATA_TYPE_DB_ID ) {
      DATA_TYPE_DB_ID = aDATA_TYPE_DB_ID;
   }


   /**
    * Returns the value of the dATA_TYPE_ID property.
    *
    * @return the value of the dATA_TYPE_ID property
    */
   public String getDATA_TYPE_ID() {
      return DATA_TYPE_ID;
   }


   /**
    * Sets a new value for the dATA_TYPE_ID property.
    *
    * @param aDATA_TYPE_ID
    *           the new value for the dATA_TYPE_ID property
    */
   public void setDATA_TYPE_ID( String aDATA_TYPE_ID ) {
      DATA_TYPE_ID = aDATA_TYPE_ID;
   }

}
