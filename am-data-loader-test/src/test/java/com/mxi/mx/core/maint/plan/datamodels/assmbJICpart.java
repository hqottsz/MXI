package com.mxi.mx.core.maint.plan.datamodels;

/**
 * DOCUMENT_ME
 *
 */
public class assmbJICpart {

   String ASSMBL_DB_ID;
   String ASSMBL_CD;
   String ASSMBL_BOM_ID;
   String ASSMBL_POS_ID;
   String BOM_PART_DB_ID;
   String BOM_PART_ID;


   public assmbJICpart(String ASSMBL_DB_ID, String ASSMBL_CD, String ASSMBL_BOM_ID,
         String ASSMBL_POS_ID, String BOM_PART_DB_ID, String BOM_PART_ID) {
      this.ASSMBL_DB_ID = ASSMBL_DB_ID;
      this.ASSMBL_CD = ASSMBL_CD;
      this.ASSMBL_BOM_ID = ASSMBL_BOM_ID;
      this.ASSMBL_POS_ID = ASSMBL_POS_ID;
      this.BOM_PART_DB_ID = BOM_PART_DB_ID;
      this.BOM_PART_ID = BOM_PART_ID;

   }


   /**
    * Returns the value of the aSSMBL_DB_ID property.
    *
    * @return the value of the aSSMBL_DB_ID property
    */
   public String getASSMBL_DB_ID() {
      return ASSMBL_DB_ID;
   }


   /**
    * Sets a new value for the aSSMBL_DB_ID property.
    *
    * @param aASSMBL_DB_ID
    *           the new value for the aSSMBL_DB_ID property
    */
   public void setASSMBL_DB_ID( String aASSMBL_DB_ID ) {
      ASSMBL_DB_ID = aASSMBL_DB_ID;
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
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( ASSMBL_BOM_ID == null ) ? 0 : ASSMBL_BOM_ID.hashCode() );
      result = prime * result + ( ( ASSMBL_CD == null ) ? 0 : ASSMBL_CD.hashCode() );
      result = prime * result + ( ( ASSMBL_DB_ID == null ) ? 0 : ASSMBL_DB_ID.hashCode() );
      result = prime * result + ( ( ASSMBL_POS_ID == null ) ? 0 : ASSMBL_POS_ID.hashCode() );
      result = prime * result + ( ( BOM_PART_DB_ID == null ) ? 0 : BOM_PART_DB_ID.hashCode() );
      result = prime * result + ( ( BOM_PART_ID == null ) ? 0 : BOM_PART_ID.hashCode() );
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
      assmbJICpart other = ( assmbJICpart ) obj;
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
      if ( ASSMBL_DB_ID == null ) {
         if ( other.ASSMBL_DB_ID != null )
            return false;
      } else if ( !ASSMBL_DB_ID.equals( other.ASSMBL_DB_ID ) )
         return false;
      if ( ASSMBL_POS_ID == null ) {
         if ( other.ASSMBL_POS_ID != null )
            return false;
      } else if ( !ASSMBL_POS_ID.equals( other.ASSMBL_POS_ID ) )
         return false;
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
      return true;
   }

}
