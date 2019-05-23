package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data from EQP_ASSMBL_POS query
 *
 */
public class assmblPOS {

   String ASSMBL_CD;
   String ASSMBL_BOM_ID;
   String ASSMBL_POS_ID;
   String EQP_POS_CD;

   String NH_ASSMBL_CD;
   String NH_ASSMBL_BOM_ID;
   String NH_ASSMBL_POS_ID;


   // constructor
   public assmblPOS(String ASSMBL_CD, String ASSMBL_BOM_ID, String ASSMBL_POS_ID,
         String EQP_POS_CD) {
      this.ASSMBL_CD = ASSMBL_CD;
      this.ASSMBL_BOM_ID = ASSMBL_BOM_ID;
      this.ASSMBL_POS_ID = ASSMBL_POS_ID;
      this.EQP_POS_CD = EQP_POS_CD;
   }


   // constructor
   public assmblPOS(String ASSMBL_CD, String ASSMBL_BOM_ID, String ASSMBL_POS_ID, String EQP_POS_CD,
         String NH_ASSMBL_CD, String NH_ASSMBL_BOM_ID, String NH_ASSMBL_POS_ID) {
      this.ASSMBL_CD = ASSMBL_CD;
      this.ASSMBL_BOM_ID = ASSMBL_BOM_ID;
      this.ASSMBL_POS_ID = ASSMBL_POS_ID;
      this.EQP_POS_CD = EQP_POS_CD;
      this.NH_ASSMBL_CD = NH_ASSMBL_CD;
      this.NH_ASSMBL_BOM_ID = NH_ASSMBL_BOM_ID;
      this.NH_ASSMBL_POS_ID = NH_ASSMBL_POS_ID;
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
    * @param nH_ASSMBL_CD
    *           the new value for the nH_ASSMBL_CD property
    */
   public void setNH_ASSMBL_CD( String nH_ASSMBL_CD ) {
      NH_ASSMBL_CD = nH_ASSMBL_CD;
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
    * @param nH_ASSMBL_BOM_ID
    *           the new value for the nH_ASSMBL_BOM_ID property
    */
   public void setNH_ASSMBL_BOM_ID( String nH_ASSMBL_BOM_ID ) {
      NH_ASSMBL_BOM_ID = nH_ASSMBL_BOM_ID;
   }


   /**
    * Returns the value of the nH_ASSMBL_POS_ID property.
    *
    * @return the value of the nH_ASSMBL_POS_ID property
    */
   public String getNH_ASSMBL_POS_ID() {
      return NH_ASSMBL_POS_ID;
   }


   /**
    * Sets a new value for the nH_ASSMBL_POS_ID property.
    *
    * @param nH_ASSMBL_POS_ID
    *           the new value for the nH_ASSMBL_POS_ID property
    */
   public void setNH_ASSMBL_POS_ID( String nH_ASSMBL_POS_ID ) {
      NH_ASSMBL_POS_ID = nH_ASSMBL_POS_ID;
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
    * Returns the value of the aSSMBL_POS_ID property.
    *
    * @return the value of the aSSMBL_POS_ID property
    */
   public String getASSMBL_POS_ID() {
      return ASSMBL_POS_ID;
   }


   /**
    * Sets a new value for the aSSMBL_POS_ID property.
    *
    * @param aASSMBL_POS_ID
    *           the new value for the aSSMBL_POS_ID property
    */
   public void setASSMBL_POS_ID( String aASSMBL_POS_ID ) {
      ASSMBL_POS_ID = aASSMBL_POS_ID;
   }


   /**
    * Returns the value of the eQP_POS_CD property.
    *
    * @return the value of the eQP_POS_CD property
    */
   public String getEQP_POS_CD() {
      return EQP_POS_CD;
   }


   /**
    * Sets a new value for the eQP_POS_CD property.
    *
    * @param aEQP_POS_CD
    *           the new value for the eQP_POS_CD property
    */
   public void setEQP_POS_CD( String aEQP_POS_CD ) {
      EQP_POS_CD = aEQP_POS_CD;
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
      assmblPOS other = ( assmblPOS ) obj;
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
      if ( ASSMBL_POS_ID == null ) {
         if ( other.ASSMBL_POS_ID != null )
            return false;
      } else if ( !ASSMBL_POS_ID.equals( other.ASSMBL_POS_ID ) )
         return false;
      if ( EQP_POS_CD == null ) {
         if ( other.EQP_POS_CD != null )
            return false;
      } else if ( !EQP_POS_CD.equals( other.EQP_POS_CD ) )
         return false;
      return true;
   }

}
