package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to hold Ids (DBID and ID) information. This class is shared class which just IDs
 * information.
 *
 */
public class AssmblBomID {

   String DB_ID; // Assmbl_db_id
   String CD; // Assmbl_cd
   String ID; // Assmbl_Bom_id


   public AssmblBomID(String aDB_ID, String aCD, String aID) {
      this.DB_ID = aDB_ID;
      this.CD = aCD;
      this.ID = aID;
   }


   /**
    * Returns the value of the DB_ID property.
    *
    * @return the value of the DB_ID property
    */
   public String getDB_ID() {
      return DB_ID;
   }


   /**
    * Sets a new value for the DB_ID property.
    *
    * @param aDB_ID
    *           the new value for the DB_ID property
    */
   public void setDB_ID( String aDB_ID ) {
      DB_ID = aDB_ID;
   }


   /**
    * Returns the value of the ID property.
    *
    * @return the value of the ID property
    */
   public String getID() {
      return ID;
   }


   /**
    * Sets a new value for the ID property.
    *
    * @param aID
    *           the new value for the ID property
    */
   public void setID( String aID ) {
      ID = aID;
   }


   /**
    * Returns the value of the CD property.
    *
    * @return the value of the CD property
    */
   public String getCD() {
      return CD;
   }


   /**
    * Sets a new value for the CD property.
    *
    * @param aCD
    *           the new value for the CD property
    */
   public void setCD( String aCD ) {
      CD = aCD;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( DB_ID == null ) ? 0 : DB_ID.hashCode() );
      result = prime * result + ( ( ID == null ) ? 0 : ID.hashCode() );
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
      AssmblBomID other = ( AssmblBomID ) obj;
      if ( DB_ID == null ) {
         if ( other.DB_ID != null )
            return false;
      } else if ( !DB_ID.equalsIgnoreCase( other.DB_ID ) )
         return false;
      if ( ID == null ) {
         if ( other.ID != null )
            return false;
      } else if ( !ID.equalsIgnoreCase( other.ID ) )
         return false;
      if ( CD == null ) {
         if ( other.CD != null )
            return false;
      } else if ( !CD.equalsIgnoreCase( other.CD ) )
         return false;
      return true;
   }

}
