package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data for C_REF_TASK_LINK table
 *
 */
public class cRefTaskLink {

   String REF_TASK_CD;
   String ASSMBL_CD;
   String ATA_CD;
   String LINK_TYPE_CD;
   String LINKED_TASK_CD;
   String LINKED_TASK_ATA_CD;


   public cRefTaskLink(String REF_TASK_CD, String ASSMBL_CD, String ATA_CD, String LINK_TYPE_CD,
         String LINKED_TASK_CD, String LINKED_TASK_ATA_CD) {

      this.REF_TASK_CD = REF_TASK_CD;
      this.ASSMBL_CD = ASSMBL_CD;
      this.ATA_CD = ATA_CD;
      this.LINK_TYPE_CD = LINK_TYPE_CD;
      this.LINKED_TASK_CD = LINKED_TASK_CD;
      this.LINKED_TASK_ATA_CD = LINKED_TASK_ATA_CD;

   }


   /**
    * Returns the value of the rEF_TASK_CD property.
    *
    * @return the value of the rEF_TASK_CD property
    */
   public String getREF_TASK_CD() {
      return REF_TASK_CD;
   }


   /**
    * Sets a new value for the rEF_TASK_CD property.
    *
    * @param rEF_TASK_CD
    *           the new value for the rEF_TASK_CD property
    */
   public void setREF_TASK_CD( String rEF_TASK_CD ) {
      REF_TASK_CD = rEF_TASK_CD;
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
    * @param aSSMBL_CD
    *           the new value for the aSSMBL_CD property
    */
   public void setASSMBL_CD( String aSSMBL_CD ) {
      ASSMBL_CD = aSSMBL_CD;
   }


   /**
    * Returns the value of the aTA_CD property.
    *
    * @return the value of the aTA_CD property
    */
   public String getATA_CD() {
      return ATA_CD;
   }


   /**
    * Sets a new value for the aTA_CD property.
    *
    * @param aTA_CD
    *           the new value for the aTA_CD property
    */
   public void setATA_CD( String aTA_CD ) {
      ATA_CD = aTA_CD;
   }


   /**
    * Returns the value of the lINK_TYPE_CD property.
    *
    * @return the value of the lINK_TYPE_CD property
    */
   public String getLINK_TYPE_CD() {
      return LINK_TYPE_CD;
   }


   /**
    * Sets a new value for the lINK_TYPE_CD property.
    *
    * @param lINK_TYPE_CD
    *           the new value for the lINK_TYPE_CD property
    */
   public void setLINK_TYPE_CD( String lINK_TYPE_CD ) {
      LINK_TYPE_CD = lINK_TYPE_CD;
   }


   /**
    * Returns the value of the lINKED_TASK_CD property.
    *
    * @return the value of the lINKED_TASK_CD property
    */
   public String getLINKED_TASK_CD() {
      return LINKED_TASK_CD;
   }


   /**
    * Sets a new value for the lINKED_TASK_CD property.
    *
    * @param lINKED_TASK_CD
    *           the new value for the lINKED_TASK_CD property
    */
   public void setLINKED_TASK_CD( String lINKED_TASK_CD ) {
      LINKED_TASK_CD = lINKED_TASK_CD;
   }


   /**
    * Returns the value of the lINKED_TASK_ATA_CD property.
    *
    * @return the value of the lINKED_TASK_ATA_CD property
    */
   public String getLINKED_TASK_ATA_CD() {
      return LINKED_TASK_ATA_CD;
   }


   /**
    * Sets a new value for the lINKED_TASK_ATA_CD property.
    *
    * @param lINKED_TASK_ATA_CD
    *           the new value for the lINKED_TASK_ATA_CD property
    */
   public void setLINKED_TASK_ATA_CD( String lINKED_TASK_ATA_CD ) {
      LINKED_TASK_ATA_CD = lINKED_TASK_ATA_CD;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( ASSMBL_CD == null ) ? 0 : ASSMBL_CD.hashCode() );
      result = prime * result + ( ( ATA_CD == null ) ? 0 : ATA_CD.hashCode() );
      result =
            prime * result + ( ( LINKED_TASK_ATA_CD == null ) ? 0 : LINKED_TASK_ATA_CD.hashCode() );
      result = prime * result + ( ( LINKED_TASK_CD == null ) ? 0 : LINKED_TASK_CD.hashCode() );
      result = prime * result + ( ( LINK_TYPE_CD == null ) ? 0 : LINK_TYPE_CD.hashCode() );
      result = prime * result + ( ( REF_TASK_CD == null ) ? 0 : REF_TASK_CD.hashCode() );
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
      cRefTaskLink other = ( cRefTaskLink ) obj;
      if ( ASSMBL_CD == null ) {
         if ( other.ASSMBL_CD != null )
            return false;
      } else if ( !ASSMBL_CD.equals( other.ASSMBL_CD ) )
         return false;
      if ( ATA_CD == null ) {
         if ( other.ATA_CD != null )
            return false;
      } else if ( !ATA_CD.equals( other.ATA_CD ) )
         return false;
      if ( LINKED_TASK_ATA_CD == null ) {
         if ( other.LINKED_TASK_ATA_CD != null )
            return false;
      } else if ( !LINKED_TASK_ATA_CD.equals( other.LINKED_TASK_ATA_CD ) )
         return false;
      if ( LINKED_TASK_CD == null ) {
         if ( other.LINKED_TASK_CD != null )
            return false;
      } else if ( !LINKED_TASK_CD.equals( other.LINKED_TASK_CD ) )
         return false;
      if ( LINK_TYPE_CD == null ) {
         if ( other.LINK_TYPE_CD != null )
            return false;
      } else if ( !LINK_TYPE_CD.equals( other.LINK_TYPE_CD ) )
         return false;
      if ( REF_TASK_CD == null ) {
         if ( other.REF_TASK_CD != null )
            return false;
      } else if ( !REF_TASK_CD.equals( other.REF_TASK_CD ) )
         return false;
      return true;
   }

}
