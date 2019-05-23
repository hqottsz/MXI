package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data for bom part and Part information
 *
 */
public class bomPartPN {

   String BOM_PART_DB_ID;
   String BOM_PART_ID;
   String PART_NO_DB_ID;
   String PART_NO_ID;


   public bomPartPN(String aBOM_PART_DB_ID, String aBOM_PART_ID, String aPART_NO_DB_ID,
         String aPART_NO_ID) {
      this.BOM_PART_DB_ID = aBOM_PART_DB_ID;
      this.BOM_PART_ID = aBOM_PART_ID;
      this.PART_NO_DB_ID = aPART_NO_DB_ID;
      this.PART_NO_ID = aPART_NO_ID;

   }


   /**
    * Returns the value of the bOM_PART_DB_ID property.
    *
    * @return the value of the bOM_PART_DB_ID property
    */
   public String getBOM_PART_DB_ID() {
      return BOM_PART_DB_ID;
   }


   /**
    * Sets a new value for the bOM_PART_DB_ID property.
    *
    * @param aBOM_PART_DB_ID
    *           the new value for the bOM_PART_DB_ID property
    */
   public void setBOM_PART_DB_ID( String aBOM_PART_DB_ID ) {
      BOM_PART_DB_ID = aBOM_PART_DB_ID;
   }


   /**
    * Returns the value of the bOM_PART_ID property.
    *
    * @return the value of the bOM_PART_ID property
    */
   public String getBOM_PART_ID() {
      return BOM_PART_ID;
   }


   /**
    * Sets a new value for the bOM_PART_ID property.
    *
    * @param aBOM_PART_ID
    *           the new value for the bOM_PART_ID property
    */
   public void setBOM_PART_ID( String aBOM_PART_ID ) {
      BOM_PART_ID = aBOM_PART_ID;
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
      result = prime * result + ( ( BOM_PART_DB_ID == null ) ? 0 : BOM_PART_DB_ID.hashCode() );
      result = prime * result + ( ( BOM_PART_ID == null ) ? 0 : BOM_PART_ID.hashCode() );
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
      bomPartPN other = ( bomPartPN ) obj;
      if ( BOM_PART_DB_ID == null ) {
         if ( other.BOM_PART_DB_ID != null )
            return false;
      } else if ( !BOM_PART_DB_ID.equals( other.BOM_PART_DB_ID ) )
         return false;
      if ( BOM_PART_ID == null ) {
         if ( other.BOM_PART_ID != null )
            return false;
      } else if ( !BOM_PART_ID.equals( other.BOM_PART_ID ) )
         return false;
      if ( PART_NO_DB_ID == null ) {
         if ( other.PART_NO_DB_ID != null )
            return false;
      } else if ( !PART_NO_DB_ID.equals( other.PART_NO_DB_ID ) )
         return false;
      if ( PART_NO_ID == null ) {
         if ( other.PART_NO_ID != null )
            return false;
      } else if ( !PART_NO_ID.equals( other.PART_NO_ID ) )
         return false;
      return true;
   }

}
