package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data for assemble information
 *
 */
public class asmInfor {

   simpleIDs lPNIDs;
   assmbleInfor lNHASMBL;
   assmbleInfor lASMBL;
   simpleIDs lPGIDs;


   public asmInfor(simpleIDs aPNIDs, assmbleInfor aNHASMBL, assmbleInfor aASMBL, simpleIDs aPGIDs) {
      this.lPNIDs = aPNIDs;
      this.lNHASMBL = aNHASMBL;
      this.lASMBL = aASMBL;
      this.lPGIDs = aPGIDs;

   }


   /**
    * Returns the value of the lPNIDs property.
    *
    * @return the value of the lPNIDs property
    */
   public simpleIDs getlPNIDs() {
      return lPNIDs;
   }


   /**
    * Sets a new value for the lPNIDs property.
    *
    * @param aLPNIDs
    *           the new value for the lPNIDs property
    */
   public void setlPNIDs( simpleIDs aLPNIDs ) {
      lPNIDs = aLPNIDs;
   }


   /**
    * Returns the value of the lNHASMBL property.
    *
    * @return the value of the lNHASMBL property
    */
   public assmbleInfor getlNHASMBL() {
      return lNHASMBL;
   }


   /**
    * Sets a new value for the lNHASMBL property.
    *
    * @param aLNHASMBL
    *           the new value for the lNHASMBL property
    */
   public void setlNHASMBL( assmbleInfor aLNHASMBL ) {
      lNHASMBL = aLNHASMBL;
   }


   /**
    * Returns the value of the lASMBL property.
    *
    * @return the value of the lASMBL property
    */
   public assmbleInfor getlASMBL() {
      return lASMBL;
   }


   /**
    * Sets a new value for the lASMBL property.
    *
    * @param aLASMBL
    *           the new value for the lASMBL property
    */
   public void setlASMBL( assmbleInfor aLASMBL ) {
      lASMBL = aLASMBL;
   }


   /**
    * Returns the value of the lPGIDs property.
    *
    * @return the value of the lPGIDs property
    */
   public simpleIDs getlPGIDs() {
      return lPGIDs;
   }


   /**
    * Sets a new value for the lPGIDs property.
    *
    * @param aLPGIDs
    *           the new value for the lPGIDs property
    */
   public void setlPGIDs( simpleIDs aLPGIDs ) {
      lPGIDs = aLPGIDs;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( lASMBL == null ) ? 0 : lASMBL.hashCode() );
      result = prime * result + ( ( lNHASMBL == null ) ? 0 : lNHASMBL.hashCode() );
      result = prime * result + ( ( lPGIDs == null ) ? 0 : lPGIDs.hashCode() );
      result = prime * result + ( ( lPNIDs == null ) ? 0 : lPNIDs.hashCode() );
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
      asmInfor other = ( asmInfor ) obj;
      if ( lASMBL == null ) {
         if ( other.lASMBL != null )
            return false;
      } else if ( !lASMBL.equals( other.lASMBL ) )
         return false;
      if ( lNHASMBL == null ) {
         if ( other.lNHASMBL != null )
            return false;
      } else if ( !lNHASMBL.equals( other.lNHASMBL ) )
         return false;
      if ( lPGIDs == null ) {
         if ( other.lPGIDs != null )
            return false;
      } else if ( !lPGIDs.equals( other.lPGIDs ) )
         return false;
      if ( lPNIDs == null ) {
         if ( other.lPNIDs != null )
            return false;
      } else if ( !lPNIDs.equals( other.lPNIDs ) )
         return false;
      return true;
   }

}
