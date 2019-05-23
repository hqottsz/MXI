package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data from EQP_ASSMBL_PART query
 *
 */
public class assmblPART {

   String ASSMBL_CD;
   String BOM_PART_CD;
   String BOM_PART_NAME;
   String ASSMBL_BOM_ID;
   String BOM_PART_DB_ID;
   String BOM_PART_ID;
   String INV_CLASS_DB_ID;
   String INV_CLASS_CD;


   // constructor
   public assmblPART(String ASSMBL_CD, String BOM_PART_CD, String BOM_PART_NAME,
         String ASSMBL_BOM_ID, String INV_CLASS_CD) {
      this.ASSMBL_CD = ASSMBL_CD;
      this.BOM_PART_CD = BOM_PART_CD;
      this.BOM_PART_NAME = BOM_PART_NAME;
      this.ASSMBL_BOM_ID = ASSMBL_BOM_ID;
      this.INV_CLASS_CD = INV_CLASS_CD;

   }


   // constructor
   public assmblPART(String ASSMBL_CD, String BOM_PART_CD, String BOM_PART_NAME,
         String INV_CLASS_CD) {
      this.ASSMBL_CD = ASSMBL_CD;
      this.BOM_PART_CD = BOM_PART_CD;
      this.BOM_PART_NAME = BOM_PART_NAME;
      this.INV_CLASS_CD = INV_CLASS_CD;

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
    * Returns the value of the bOM_PART_CD property.
    *
    * @return the value of the bOM_PART_CD property
    */
   public String getBOM_PART_CD() {
      return BOM_PART_CD;
   }


   /**
    * Sets a new value for the bOM_PART_CD property.
    *
    * @param aBOM_PART_CD
    *           the new value for the bOM_PART_CD property
    */
   public void setBOM_PART_CD( String aBOM_PART_CD ) {
      BOM_PART_CD = aBOM_PART_CD;
   }


   /**
    * Returns the value of the bOM_PART_NAME property.
    *
    * @return the value of the bOM_PART_NAME property
    */
   public String getBOM_PART_NAME() {
      return BOM_PART_NAME;
   }


   /**
    * Sets a new value for the bOM_PART_NAME property.
    *
    * @param aBOM_PART_NAME
    *           the new value for the bOM_PART_NAME property
    */
   public void setBOM_PART_NAME( String aBOM_PART_NAME ) {
      BOM_PART_NAME = aBOM_PART_NAME;
   }


   /**
    * Returns the value of the aSSMBL_BOM_ID property.
    *
    * @return the value of the aSSMBL_BOM_ID property
    */
   public String getASSMBL_BOM_ID() {
      return ASSMBL_BOM_ID;
   }


   /**
    * Sets a new value for the aSSMBL_BOM_ID property.
    *
    * @param aASSMBL_BOM_ID
    *           the new value for the aSSMBL_BOM_ID property
    */
   public void setASSMBL_BOM_ID( String aASSMBL_BOM_ID ) {
      ASSMBL_BOM_ID = aASSMBL_BOM_ID;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( ASSMBL_BOM_ID == null ) ? 0 : ASSMBL_BOM_ID.hashCode() );
      result = prime * result + ( ( ASSMBL_CD == null ) ? 0 : ASSMBL_CD.hashCode() );
      result = prime * result + ( ( BOM_PART_CD == null ) ? 0 : BOM_PART_CD.hashCode() );
      result = prime * result + ( ( BOM_PART_NAME == null ) ? 0 : BOM_PART_NAME.hashCode() );
      result = prime * result + ( ( INV_CLASS_DB_ID == null ) ? 0 : INV_CLASS_DB_ID.hashCode() );
      return result;
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
      assmblPART other = ( assmblPART ) obj;
      if ( ASSMBL_BOM_ID == null ) {
         if ( other.ASSMBL_BOM_ID != null )
            return false;
      } else if ( !ASSMBL_BOM_ID.equals( other.ASSMBL_BOM_ID ) )
         return false;
      if ( ASSMBL_CD == null ) {
         if ( other.ASSMBL_CD != null )
            return false;
      } else if ( !ASSMBL_CD.equals( other.ASSMBL_CD ) )
         return false;
      if ( BOM_PART_CD == null ) {
         if ( other.BOM_PART_CD != null )
            return false;
      } else if ( !BOM_PART_CD.equals( other.BOM_PART_CD ) )
         return false;
      if ( BOM_PART_NAME == null ) {
         if ( other.BOM_PART_NAME != null )
            return false;
      } else if ( !BOM_PART_NAME.equals( other.BOM_PART_NAME ) )
         return false;
      if ( INV_CLASS_DB_ID == null ) {
         if ( other.INV_CLASS_DB_ID != null )
            return false;
      } else if ( !INV_CLASS_DB_ID.equals( other.INV_CLASS_DB_ID ) )
         return false;
      return true;
   }

}
