package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data from EQP_PART_BASE_LINE and EQP_PART_NO tables
 *
 */
public class partNo {

   String PART_NO_DB_ID;
   String PART_NO_ID;


   // constructor
   public partNo(String PART_NO_DB_ID, String PART_NO_ID) {
      this.PART_NO_DB_ID = PART_NO_DB_ID;
      this.PART_NO_ID = PART_NO_ID;

   }


   /**
    * Returns the value of the pART_NO_DB_ID property.
    *
    * @return the value of the pART_NO_DB_ID property
    */
   public String getPART_NO_DB_ID() {
      return PART_NO_DB_ID;
   }


   /**
    * Sets a new value for the pART_NO_DB_ID property.
    *
    * @param aPART_NO_DB_ID
    *           the new value for the pART_NO_DB_ID property
    */
   public void setPART_NO_DB_ID( String aPART_NO_DB_ID ) {
      PART_NO_DB_ID = aPART_NO_DB_ID;
   }


   /**
    * Returns the value of the pART_NO_ID property.
    *
    * @return the value of the pART_NO_ID property
    */
   public String getPART_NO_ID() {
      return PART_NO_ID;
   }


   /**
    * Sets a new value for the pART_NO_ID property.
    *
    * @param aPART_NO_ID
    *           the new value for the pART_NO_ID property
    */
   public void setPART_NO_ID( String aPART_NO_ID ) {
      PART_NO_ID = aPART_NO_ID;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( PART_NO_DB_ID == null ) ? 0 : PART_NO_DB_ID.hashCode() );
      result = prime * result + ( ( PART_NO_ID == null ) ? 0 : PART_NO_ID.hashCode() );
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
      partNo other = ( partNo ) obj;
      if ( PART_NO_DB_ID == null ) {
         if ( other.PART_NO_DB_ID != null )
            return false;
      } else if ( !PART_NO_DB_ID.equalsIgnoreCase( other.PART_NO_DB_ID ) )
         return false;
      if ( PART_NO_ID == null ) {
         if ( other.PART_NO_ID != null )
            return false;
      } else if ( !PART_NO_ID.equalsIgnoreCase( other.PART_NO_ID ) )
         return false;
      return true;
   }

}
