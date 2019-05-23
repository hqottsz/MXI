package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data of inventory information
 *
 */
public class invIDs {

   simpleIDs iINVIDs;
   simpleIDs iASSMBLINVIDs;


   public invIDs(simpleIDs aINVIDs, simpleIDs aAssmblINVIDS) {
      this.iINVIDs = aINVIDs;
      this.iASSMBLINVIDs = aAssmblINVIDS;
   }


   /**
    * Returns the value of the iNVIDs property.
    *
    * @return the value of the iNVIDs property
    */
   public simpleIDs getINVIDs() {
      return iINVIDs;
   }


   /**
    * Sets a new value for the iNVIDs property.
    *
    * @param aINVIDs
    *           the new value for the iNVIDs property
    */
   public void setINVIDs( simpleIDs aINVIDs ) {
      iINVIDs = aINVIDs;
   }


   /**
    * Returns the value of the aSSMBLINVIDs property.
    *
    * @return the value of the aSSMBLINVIDs property
    */
   public simpleIDs getASSMBLINVIDs() {
      return iASSMBLINVIDs;
   }


   /**
    * Sets a new value for the aSSMBLINVIDs property.
    *
    * @param aASSMBLINVIDs
    *           the new value for the aSSMBLINVIDs property
    */
   public void setASSMBLINVIDs( simpleIDs aASSMBLINVIDs ) {
      iASSMBLINVIDs = aASSMBLINVIDs;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( iASSMBLINVIDs == null ) ? 0 : iASSMBLINVIDs.hashCode() );
      result = prime * result + ( ( iINVIDs == null ) ? 0 : iINVIDs.hashCode() );
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
      invIDs other = ( invIDs ) obj;
      if ( iASSMBLINVIDs == null ) {
         if ( other.iASSMBLINVIDs != null )
            return false;
      } else if ( !iASSMBLINVIDs.equals( other.iASSMBLINVIDs ) )
         return false;
      if ( iINVIDs == null ) {
         if ( other.iINVIDs != null )
            return false;
      } else if ( !iINVIDs.equals( other.iINVIDs ) )
         return false;
      return true;
   }

}
