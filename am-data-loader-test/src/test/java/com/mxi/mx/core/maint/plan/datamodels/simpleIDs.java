package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to hold Ids (DBID and ID) information. This class is shared class which just IDs
 * information.
 *
 */
public class simpleIDs {

   String NO_DB_ID;
   String NO_ID;


   public simpleIDs(String NO_DB_ID, String NO_ID) {
      this.NO_DB_ID = NO_DB_ID;
      this.NO_ID = NO_ID;
   }


   /**
    * Returns the value of the nO_DB_ID property.
    *
    * @return the value of the nO_DB_ID property
    */
   public String getNO_DB_ID() {
      return NO_DB_ID;
   }


   /**
    * Sets a new value for the nO_DB_ID property.
    *
    * @param aNO_DB_ID
    *           the new value for the nO_DB_ID property
    */
   public void setNO_DB_ID( String aNO_DB_ID ) {
      NO_DB_ID = aNO_DB_ID;
   }


   /**
    * Returns the value of the nO_ID property.
    *
    * @return the value of the nO_ID property
    */
   public String getNO_ID() {
      return NO_ID;
   }


   /**
    * Sets a new value for the nO_ID property.
    *
    * @param aNO_ID
    *           the new value for the nO_ID property
    */
   public void setNO_ID( String aNO_ID ) {
      NO_ID = aNO_ID;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( NO_DB_ID == null ) ? 0 : NO_DB_ID.hashCode() );
      result = prime * result + ( ( NO_ID == null ) ? 0 : NO_ID.hashCode() );
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
      simpleIDs other = ( simpleIDs ) obj;
      if ( NO_DB_ID == null ) {
         if ( other.NO_DB_ID != null )
            return false;
      } else if ( !NO_DB_ID.equalsIgnoreCase( other.NO_DB_ID ) )
         return false;
      if ( NO_ID == null ) {
         if ( other.NO_ID != null )
            return false;
      } else if ( !NO_ID.equalsIgnoreCase( other.NO_ID ) )
         return false;
      return true;
   }

}
