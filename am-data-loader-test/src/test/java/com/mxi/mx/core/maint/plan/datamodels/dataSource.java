package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data from EQP_DATA_SOURCE query
 *
 */
public class dataSource {

   String ASSMBL_CD;
   String DATA_SOURCES_DB_ID;
   String DATA_SOURCES_CD;


   // constructor
   public dataSource(String ASSMBL_CD, String DATA_SOURCES_DB_ID, String DATA_SOURCES_CD) {
      this.ASSMBL_CD = ASSMBL_CD;
      this.DATA_SOURCES_DB_ID = DATA_SOURCES_DB_ID;
      this.DATA_SOURCES_CD = DATA_SOURCES_CD;
   }


   /**
    * Returns the value of the aSSMBL_CD property.
    *
    * @return the value of the aSSMBL_CD property
    */
   public String getASSMBL_CD() {
      return ASSMBL_CD;
   }


   /**
    * Sets a new value for the aSSMBL_CD property.
    *
    * @param aASSMBL_CD
    *           the new value for the aSSMBL_CD property
    */
   public void setASSMBL_CD( String aASSMBL_CD ) {
      ASSMBL_CD = aASSMBL_CD;
   }


   /**
    * Returns the value of the dATA_SOURCES_DB_ID property.
    *
    * @return the value of the dATA_SOURCES_DB_ID property
    */
   public String getDATA_SOURCES_DB_ID() {
      return DATA_SOURCES_DB_ID;
   }


   /**
    * Sets a new value for the dATA_SOURCES_DB_ID property.
    *
    * @param aDATA_SOURCES_DB_ID
    *           the new value for the dATA_SOURCES_DB_ID property
    */
   public void setDATA_SOURCES_DB_ID( String aDATA_SOURCES_DB_ID ) {
      DATA_SOURCES_DB_ID = aDATA_SOURCES_DB_ID;
   }


   /**
    * Returns the value of the dATA_SOURCES_CD property.
    *
    * @return the value of the dATA_SOURCES_CD property
    */
   public String getDATA_SOURCES_CD() {
      return DATA_SOURCES_CD;
   }


   /**
    * Sets a new value for the dATA_SOURCES_CD property.
    *
    * @param aDATA_SOURCES_CD
    *           the new value for the dATA_SOURCES_CD property
    */
   public void setDATA_SOURCES_CD( String aDATA_SOURCES_CD ) {
      DATA_SOURCES_CD = aDATA_SOURCES_CD;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals( Object obj ) {
      if ( this == obj )
         return true;
      if ( obj == null )
         return false;
      if ( getClass() != obj.getClass() )
         return false;
      dataSource other = ( dataSource ) obj;
      if ( ASSMBL_CD == null ) {
         if ( other.ASSMBL_CD != null )
            return false;
      } else if ( !ASSMBL_CD.equals( other.ASSMBL_CD ) )
         return false;
      if ( DATA_SOURCES_CD == null ) {
         if ( other.DATA_SOURCES_CD != null )
            return false;
      } else if ( !DATA_SOURCES_CD.equals( other.DATA_SOURCES_CD ) )
         return false;
      if ( DATA_SOURCES_DB_ID == null ) {
         if ( other.DATA_SOURCES_DB_ID != null )
            return false;
      } else if ( !DATA_SOURCES_DB_ID.equals( other.DATA_SOURCES_DB_ID ) )
         return false;
      return true;
   }

}
