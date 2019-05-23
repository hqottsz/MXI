package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is store data for part information in EQP_PART_NO
 *
 */
public class partInfo {

   String MANUFACT_CD;
   String PART_NO_OEM;
   String PART_NO_SDESC;
   String INV_CLASS_CD;


   // constructor
   public partInfo(String MANUFACT_CD, String PART_NO_OEM, String PART_NO_SDESC,
         String INV_CLASS_CD) {
      this.MANUFACT_CD = MANUFACT_CD;
      this.PART_NO_OEM = PART_NO_OEM;
      this.PART_NO_SDESC = PART_NO_SDESC;
      this.INV_CLASS_CD = INV_CLASS_CD;

   }


   /**
    * Returns the value of the mANUFACT_CD property.
    *
    * @return the value of the mANUFACT_CD property
    */
   public String getMANUFACT_CD() {
      return MANUFACT_CD;
   }


   /**
    * Sets a new value for the mANUFACT_CD property.
    *
    * @param aMANUFACT_CD
    *           the new value for the mANUFACT_CD property
    */
   public void setMANUFACT_CD( String aMANUFACT_CD ) {
      MANUFACT_CD = aMANUFACT_CD;
   }


   /**
    * Returns the value of the pART_NO_OEM property.
    *
    * @return the value of the pART_NO_OEM property
    */
   public String getPART_NO_OEM() {
      return PART_NO_OEM;
   }


   /**
    * Sets a new value for the pART_NO_OEM property.
    *
    * @param aPART_NO_OEM
    *           the new value for the pART_NO_OEM property
    */
   public void setPART_NO_OEM( String aPART_NO_OEM ) {
      PART_NO_OEM = aPART_NO_OEM;
   }


   /**
    * Returns the value of the pART_NO_SDESC property.
    *
    * @return the value of the pART_NO_SDESC property
    */
   public String getPART_NO_SDESC() {
      return PART_NO_SDESC;
   }


   /**
    * Sets a new value for the pART_NO_SDESC property.
    *
    * @param aPART_NO_SDESC
    *           the new value for the pART_NO_SDESC property
    */
   public void setPART_NO_SDESC( String aPART_NO_SDESC ) {
      PART_NO_SDESC = aPART_NO_SDESC;
   }


   /**
    * Returns the value of the iNV_CLASS_CD property.
    *
    * @return the value of the iNV_CLASS_CD property
    */
   public String getINV_CLASS_CD() {
      return INV_CLASS_CD;
   }


   /**
    * Sets a new value for the iNV_CLASS_CD property.
    *
    * @param aINV_CLASS_CD
    *           the new value for the iNV_CLASS_CD property
    */
   public void setINV_CLASS_CD( String aINV_CLASS_CD ) {
      INV_CLASS_CD = aINV_CLASS_CD;
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
      partInfo other = ( partInfo ) obj;
      if ( INV_CLASS_CD == null ) {
         if ( other.INV_CLASS_CD != null )
            return false;
      } else if ( !INV_CLASS_CD.equals( other.INV_CLASS_CD ) )
         return false;
      if ( MANUFACT_CD == null ) {
         if ( other.MANUFACT_CD != null )
            return false;
      } else if ( !MANUFACT_CD.equals( other.MANUFACT_CD ) )
         return false;
      if ( PART_NO_OEM == null ) {
         if ( other.PART_NO_OEM != null )
            return false;
      } else if ( !PART_NO_OEM.equals( other.PART_NO_OEM ) )
         return false;
      if ( PART_NO_SDESC == null ) {
         if ( other.PART_NO_SDESC != null )
            return false;
      } else if ( !PART_NO_SDESC.equals( other.PART_NO_SDESC ) )
         return false;
      return true;
   }

}
