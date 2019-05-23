package com.mxi.mx.core.maint.plan.datamodels;

/**
 * DOCUMENT_ME
 *
 */
public class mannufact {

   String MANUFACT_CD;
   String MANUFACT_NAME;


   public mannufact(String MANUFACT_CD, String MANUFACT_NAME) {
      this.MANUFACT_CD = MANUFACT_CD;
      this.MANUFACT_NAME = MANUFACT_NAME;

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
    * Returns the value of the mANUFACT_NAME property.
    *
    * @return the value of the mANUFACT_NAME property
    */
   public String getMANUFACT_NAME() {
      return MANUFACT_NAME;
   }


   /**
    * Sets a new value for the mANUFACT_NAME property.
    *
    * @param aMANUFACT_NAME
    *           the new value for the mANUFACT_NAME property
    */
   public void setMANUFACT_NAME( String aMANUFACT_NAME ) {
      MANUFACT_NAME = aMANUFACT_NAME;
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
      mannufact other = ( mannufact ) obj;
      if ( MANUFACT_CD == null ) {
         if ( other.MANUFACT_CD != null )
            return false;
      } else if ( !MANUFACT_CD.equalsIgnoreCase( other.MANUFACT_CD ) )
         return false;
      if ( MANUFACT_NAME == null ) {
         if ( other.MANUFACT_NAME != null )
            return false;
      } else if ( !MANUFACT_NAME.equalsIgnoreCase( other.MANUFACT_NAME ) )
         return false;
      return true;
   }

}
