package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data from EQP_ASSMBL query
 *
 */
public class assmble {

   String ASSMBL_CD;
   String ASSMBL_NAME;
   String ASSMBL_CLASS_CD;
   String ASSMBL_DB_ID;


   // constructor
   public assmble(String ASSMBL_CD, String ASSMBL_NAME, String ASSMBL_CLASS_CD) {
      this.ASSMBL_CD = ASSMBL_CD;
      this.ASSMBL_NAME = ASSMBL_NAME;
      this.ASSMBL_CLASS_CD = ASSMBL_CLASS_CD;
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
    * Returns the value of the aSSMBL_NAME property.
    *
    * @return the value of the aSSMBL_NAME property
    */
   public String getASSMBL_NAME() {
      return ASSMBL_NAME;
   }


   /**
    * Sets a new value for the aSSMBL_NAME property.
    *
    * @param aASSMBL_NAME
    *           the new value for the aSSMBL_NAME property
    */
   public void setASSMBL_NAME( String aASSMBL_NAME ) {
      ASSMBL_NAME = aASSMBL_NAME;
   }


   /**
    * Returns the value of the aSSMBL_CLASS_CD property.
    *
    * @return the value of the aSSMBL_CLASS_CD property
    */
   public String getASSMBL_CLASS_CD() {
      return ASSMBL_CLASS_CD;
   }


   /**
    * Sets a new value for the aSSMBL_CLASS_CD property.
    *
    * @param aASSMBL_CLASS_CD
    *           the new value for the aSSMBL_CLASS_CD property
    */
   public void setASSMBL_CLASS_CD( String aASSMBL_CLASS_CD ) {
      ASSMBL_CLASS_CD = aASSMBL_CLASS_CD;
   }


   /**
    * Returns the value of the aSSMBL_DB_ID property.
    *
    * @return the value of the aSSMBL_DB_ID property
    */
   public String getASSMBL_DB_ID() {
      return ASSMBL_DB_ID;
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
      assmble other = ( assmble ) obj;
      if ( ASSMBL_CD == null ) {
         if ( other.ASSMBL_CD != null )
            return false;
      } else if ( !ASSMBL_CD.equals( other.ASSMBL_CD ) )
         return false;
      if ( ASSMBL_CLASS_CD == null ) {
         if ( other.ASSMBL_CLASS_CD != null )
            return false;
      } else if ( !ASSMBL_CLASS_CD.equals( other.ASSMBL_CLASS_CD ) )
         return false;
      if ( ASSMBL_NAME == null ) {
         if ( other.ASSMBL_NAME != null )
            return false;
      } else if ( !ASSMBL_NAME.equals( other.ASSMBL_NAME ) )
         return false;
      return true;
   }

}
