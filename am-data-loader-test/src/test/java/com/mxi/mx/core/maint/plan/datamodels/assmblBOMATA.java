package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data from EQP_ASSMBL_BOM query corresponding C_ATA_SYS table.
 *
 */
public class assmblBOMATA {

   String ASSMBL_CD;
   String NH_ASSMBL_CD;
   String ASSMBL_BOM_CD;
   String ASSMBL_BOM_NAME;
   String BOM_CLASS_CD;
   String NH_ASSMBL_DB_ID;// nh_assmbl_db_id;
   String NH_ASSMBL_BOM_ID;// nh_assmbl_bom_id


   // constructor
   public assmblBOMATA(String ASSMBL_CD, String NH_ASSMBL_CD, String ASSMBL_BOM_CD,
         String ASSMBL_BOM_NAME, String BOM_CLASS_CD, String NH_ASSMBL_DB_ID,
         String NH_ASSMBL_BOM_ID) {
      this.ASSMBL_CD = ASSMBL_CD;
      this.NH_ASSMBL_CD = NH_ASSMBL_CD;
      this.ASSMBL_BOM_CD = ASSMBL_BOM_CD;
      this.ASSMBL_BOM_NAME = ASSMBL_BOM_NAME;
      this.BOM_CLASS_CD = BOM_CLASS_CD;
      this.NH_ASSMBL_DB_ID = NH_ASSMBL_DB_ID;
      this.NH_ASSMBL_BOM_ID = NH_ASSMBL_BOM_ID;

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
    * Returns the value of the nH_ASSMBL_CD property.
    *
    * @return the value of the nH_ASSMBL_CD property
    */
   public String getNH_ASSMBL_CD() {
      return NH_ASSMBL_CD;
   }


   /**
    * Sets a new value for the nH_ASSMBL_CD property.
    *
    * @param aNH_ASSMBL_CD
    *           the new value for the nH_ASSMBL_CD property
    */
   public void setNH_ASSMBL_CD( String aNH_ASSMBL_CD ) {
      NH_ASSMBL_CD = aNH_ASSMBL_CD;
   }


   /**
    * Returns the value of the aSSMBL_BOM_CD property.
    *
    * @return the value of the aSSMBL_BOM_CD property
    */
   public String getASSMBL_BOM_CD() {
      return ASSMBL_BOM_CD;
   }


   /**
    * Sets a new value for the aSSMBL_BOM_CD property.
    *
    * @param aASSMBL_BOM_CD
    *           the new value for the aSSMBL_BOM_CD property
    */
   public void setASSMBL_BOM_CD( String aASSMBL_BOM_CD ) {
      ASSMBL_BOM_CD = aASSMBL_BOM_CD;
   }


   /**
    * Returns the value of the aSSMBL_BOM_NAME property.
    *
    * @return the value of the aSSMBL_BOM_NAME property
    */
   public String getASSMBL_BOM_NAME() {
      return ASSMBL_BOM_NAME;
   }


   /**
    * Sets a new value for the aSSMBL_BOM_NAME property.
    *
    * @param aASSMBL_BOM_NAME
    *           the new value for the aSSMBL_BOM_NAME property
    */
   public void setASSMBL_BOM_NAME( String aASSMBL_BOM_NAME ) {
      ASSMBL_BOM_NAME = aASSMBL_BOM_NAME;
   }


   /**
    * Returns the value of the bOM_CLASS_CD property.
    *
    * @return the value of the bOM_CLASS_CD property
    */
   public String getBOM_CLASS_CD() {
      return BOM_CLASS_CD;
   }


   /**
    * Sets a new value for the bOM_CLASS_CD property.
    *
    * @param aBOM_CLASS_CD
    *           the new value for the bOM_CLASS_CD property
    */
   public void setBOM_CLASS_CD( String aBOM_CLASS_CD ) {
      BOM_CLASS_CD = aBOM_CLASS_CD;
   }


   /**
    * Returns the value of the nH_ASSMBL_DB_ID property.
    *
    * @return the value of the nH_ASSMBL_DB_ID property
    */
   public String getNH_ASSMBL_DB_ID() {
      return NH_ASSMBL_DB_ID;
   }


   /**
    * Sets a new value for the nH_ASSMBL_DB_ID property.
    *
    * @param aNH_ASSMBL_DB_ID
    *           the new value for the nH_ASSMBL_DB_ID property
    */
   public void setNH_ASSMBL_DB_ID( String aNH_ASSMBL_DB_ID ) {
      NH_ASSMBL_DB_ID = aNH_ASSMBL_DB_ID;
   }


   /**
    * Returns the value of the nH_ASSMBL_BOM_ID property.
    *
    * @return the value of the nH_ASSMBL_BOM_ID property
    */
   public String getNH_ASSMBL_BOM_ID() {
      return NH_ASSMBL_BOM_ID;
   }


   /**
    * Sets a new value for the nH_ASSMBL_BOM_ID property.
    *
    * @param aNH_ASSMBL_BOM_ID
    *           the new value for the nH_ASSMBL_BOM_ID property
    */
   public void setNH_ASSMBL_BOM_ID( String aNH_ASSMBL_BOM_ID ) {
      NH_ASSMBL_BOM_ID = aNH_ASSMBL_BOM_ID;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( ASSMBL_BOM_CD == null ) ? 0 : ASSMBL_BOM_CD.hashCode() );
      result = prime * result + ( ( ASSMBL_BOM_NAME == null ) ? 0 : ASSMBL_BOM_NAME.hashCode() );
      result = prime * result + ( ( ASSMBL_CD == null ) ? 0 : ASSMBL_CD.hashCode() );
      result = prime * result + ( ( BOM_CLASS_CD == null ) ? 0 : BOM_CLASS_CD.hashCode() );
      result = prime * result + ( ( NH_ASSMBL_BOM_ID == null ) ? 0 : NH_ASSMBL_BOM_ID.hashCode() );
      result = prime * result + ( ( NH_ASSMBL_CD == null ) ? 0 : NH_ASSMBL_CD.hashCode() );
      result = prime * result + ( ( NH_ASSMBL_DB_ID == null ) ? 0 : NH_ASSMBL_DB_ID.hashCode() );
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
      assmblBOMATA other = ( assmblBOMATA ) obj;
      if ( ASSMBL_BOM_CD == null ) {
         if ( other.ASSMBL_BOM_CD != null )
            return false;
      } else if ( !ASSMBL_BOM_CD.equalsIgnoreCase( other.ASSMBL_BOM_CD ) )
         return false;
      if ( ASSMBL_BOM_NAME == null ) {
         if ( other.ASSMBL_BOM_NAME != null )
            return false;
      } else if ( !ASSMBL_BOM_NAME.equalsIgnoreCase( other.ASSMBL_BOM_NAME ) )
         return false;
      if ( ASSMBL_CD == null ) {
         if ( other.ASSMBL_CD != null )
            return false;
      } else if ( !ASSMBL_CD.equalsIgnoreCase( other.ASSMBL_CD ) )
         return false;
      if ( BOM_CLASS_CD == null ) {
         if ( other.BOM_CLASS_CD != null )
            return false;
      } else if ( !BOM_CLASS_CD.equalsIgnoreCase( other.BOM_CLASS_CD ) )
         return false;
      if ( NH_ASSMBL_BOM_ID == null ) {
         if ( other.NH_ASSMBL_BOM_ID != null )
            return false;
      } else if ( !NH_ASSMBL_BOM_ID.equalsIgnoreCase( other.NH_ASSMBL_BOM_ID ) )
         return false;
      if ( NH_ASSMBL_CD == null ) {
         if ( other.NH_ASSMBL_CD != null )
            return false;
      } else if ( !NH_ASSMBL_CD.equalsIgnoreCase( other.NH_ASSMBL_CD ) )
         return false;
      if ( NH_ASSMBL_DB_ID == null ) {
         if ( other.NH_ASSMBL_DB_ID != null )
            return false;
      } else if ( !NH_ASSMBL_DB_ID.equalsIgnoreCase( other.NH_ASSMBL_DB_ID ) )
         return false;
      return true;
   }

}
