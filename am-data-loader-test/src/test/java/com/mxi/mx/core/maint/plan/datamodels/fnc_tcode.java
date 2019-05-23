package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is store data from finance account query
 *
 */
public class fnc_tcode {

   String TCODE_CD;


   public fnc_tcode(String TCODE_CD) {
      this.TCODE_CD = TCODE_CD;
   }


   /**
    * Returns the value of the tCODE_CD property.
    *
    * @return the value of the tCODE_CD property
    */
   public String getTCODE_CD() {
      return TCODE_CD;
   }


   /**
    * Sets a new value for the tCODE_CD property.
    *
    * @param aTCODE_CD
    *           the new value for the tCODE_CD property
    */
   public void setTCODE_CD( String aTCODE_CD ) {
      TCODE_CD = aTCODE_CD;
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
      fnc_tcode other = ( fnc_tcode ) obj;
      if ( TCODE_CD == null ) {
         if ( other.TCODE_CD != null )
            return false;
      } else if ( !TCODE_CD.equalsIgnoreCase( other.TCODE_CD ) )
         return false;
      return true;
   }

}
